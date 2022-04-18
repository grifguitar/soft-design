import com.beust.klaxon.Klaxon
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.testcontainers.containers.FixedHostPortGenericContainer
import kotlin.test.*

class AllTests {
    companion object {
        private const val ZERO = 0
        private const val ONE = 1
        private const val TWO = 2
        private const val N8 = 20
        private const val N6 = 100
        private const val N11 = 150
        private const val N9 = 250
        private const val N14 = 453
        private const val N4 = 500
        private const val N1 = 567
        private const val N10 = 750
        private const val N12 = 850
        private const val N2 = 1000
        private const val N3 = 2259
        private const val N5 = 2943
        private const val N7 = 4000
        private const val N13 = 10000000
        private const val C1 = "POLYTEX"
        private const val C2 = "GAZPROM"
        private const val C3 = "NORNIKEL"
    }

    private val port = 8080
    private val client = HttpClient(CIO)
    private val server = "http://localhost:$port"

    class MyFixedHostPortGenericContainer(name: String) :
        FixedHostPortGenericContainer<MyFixedHostPortGenericContainer>(name)

    private val container =
        MyFixedHostPortGenericContainer("khlyting-server")
            .withFixedExposedPort(port, port)
            .withExposedPorts(port)

    @BeforeTest
    fun setUp() {
        container.start()
        runBlocking {
            client.post<HttpResponse>(
                "$server/add-company?companyName=$C1&price=$N1"
            )
            client.post<HttpResponse>(
                "$server/add-paper-currency?companyName=$C1&quantity=$N2"
            )
            client.post<HttpResponse>(
                "$server/add-company?companyName=$C2&price=$N3"
            )
            client.post<HttpResponse>(
                "$server/add-paper-currency?companyName=$C2&quantity=$N4"
            )
            client.post<HttpResponse>(
                "$server/add-company?companyName=$C3&price=$N5"
            )
            client.post<HttpResponse>(
                "$server/add-paper-currency?companyName=$C3&quantity=$N6"
            )
        }
    }

    @AfterTest
    fun tearDown() {
        container.stop()
    }

    @Test
    fun buyManyPaperCurrency() {
        withTestApplication(Application::module1) {
            val userId = addUser(N7.toDouble())
            handleRequest(
                HttpMethod.Post,
                "/buy?userId=$userId&companyName=$C1&quantity=$TWO"
            ).apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
            handleRequest(
                HttpMethod.Post,
                "/buy?userId=$userId&companyName=$C2&quantity=$ONE"
            ).apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
            checkUserPaperCurrency(
                userId,
                listOf(
                    PaperCurrency(C1, N1.toDouble(), TWO),
                    PaperCurrency(C2, N3.toDouble(), ONE)
                )
            )
            checkUserMoney(
                userId,
                N7.toDouble()
            )
        }
    }

    @Test
    fun buyPaperCurrencyAndChangePrice() {
        withTestApplication({ module1() }) {
            runBlocking {
                val userId = addUser(N2.toDouble())
                handleRequest(
                    HttpMethod.Post,
                    "/buy?userId=$userId&companyName=$C1&quantity=$ONE"
                ).apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
                checkUserPaperCurrency(
                    userId,
                    listOf(
                        PaperCurrency(C1, N1.toDouble(), ONE)
                    )
                )
                checkUserMoney(
                    userId,
                    N2.toDouble()
                )
                HttpClient(CIO).post<String>(
                    "$server/change-price?companyName=$C1&price=$N8"
                )
                checkUserPaperCurrency(
                    userId,
                    listOf(
                        PaperCurrency(C1, N8.toDouble(), ONE)
                    )
                )
                checkUserMoney(
                    userId,
                    N14.toDouble()
                )
            }
        }
    }

    @Test
    fun buyPaperCurrency() {
        withTestApplication(Application::module1) {
            val userId = addUser(N2.toDouble())
            handleRequest(
                HttpMethod.Post,
                "/buy?userId=$userId&companyName=$C1&quantity=$ONE"
            ).apply {
                assertEquals(
                    HttpStatusCode.OK,
                    response.status()
                )
            }
            checkUserPaperCurrency(
                userId,
                listOf(
                    PaperCurrency(C1, N1.toDouble(), ONE)
                )
            )
            checkUserMoney(
                userId,
                N2.toDouble()
            )
        }
    }

    @Test
    fun sellPaperCurrency() {
        withTestApplication(Application::module1) {
            runBlocking {
                val userId = addUser(N13.toDouble())
                checkPaperCurrency(
                    C1,
                    PaperCurrency(C1, N1.toDouble(), N2)
                )

                handleRequest(
                    HttpMethod.Post,
                    "/buy?userId=$userId&companyName=$C1&quantity=$N9"
                ).apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
                checkUserPaperCurrency(
                    userId,
                    listOf(
                        PaperCurrency(C1, N1.toDouble(), N9)
                    )
                )
                checkPaperCurrency(
                    C1,
                    PaperCurrency(C1, N1.toDouble(), N10)
                )

                handleRequest(
                    HttpMethod.Post,
                    "/sell?userId=$userId&companyName=$C1&quantity=$N6"
                ).apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }

                checkUserPaperCurrency(
                    userId,
                    listOf(
                        PaperCurrency(C1, N1.toDouble(), N11)
                    )
                )
                checkPaperCurrency(
                    C1,
                    PaperCurrency(C1, N1.toDouble(), N12)
                )
            }
        }
    }

    @Test
    fun emptyMoneyAtUser() {
        withTestApplication(Application::module1) {
            val userId = addUser()
            checkUserMoney(
                userId,
                ZERO.toDouble()
            )
        }
    }

    @Test
    fun emptyPaperCurrencyAtUser() {
        withTestApplication(Application::module1) {
            val userId = addUser()
            checkUserPaperCurrency(
                userId,
                listOf()
            )
        }
    }

    private fun TestApplicationEngine.addUser(money: Double = ZERO.toDouble()): Int {
        var userId: Int
        handleRequest(
            HttpMethod.Post,
            "/add-user?name=Ivan"
        ).apply {
            userId = response.content!!.toInt()
        }
        handleRequest(
            HttpMethod.Post,
            "/add-money?userId=$userId&amount=$money"
        )
        return userId
    }

    private fun TestApplicationEngine.checkUserPaperCurrency(
        userId: Int, expected: List<PaperCurrency>
    ) {
        handleRequest(
            HttpMethod.Get,
            "/get-paper-currency?userId=$userId"
        ).apply {
            val userPaperCurrencies = Klaxon().parseArray<PaperCurrency>(response.content!!)!!
            assertEquals(expected.size, userPaperCurrencies.size)
            assertTrue(expected.containsAll(userPaperCurrencies))
            assertTrue(userPaperCurrencies.containsAll(expected))
        }
    }

    private fun TestApplicationEngine.checkUserMoney(
        userId: Int, expected: Double
    ) {
        handleRequest(
            HttpMethod.Get,
            "/get-total-money?userId=$userId"
        ).apply {
            assertEquals(expected, response.content!!.toDouble())
        }
    }

    private fun checkPaperCurrency(companyName: String, expected: PaperCurrency) {
        runBlocking {
            val paperCurrencyJson = HttpClient(CIO).get<String>(
                "$server/get-paper-currency?companyName=$companyName"
            )
            val paperCurrency = Klaxon().parse<PaperCurrency>(paperCurrencyJson)
            assertEquals(expected, paperCurrency)
        }
    }

}
