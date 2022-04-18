package common.http

import com.mongodb.rx.client.Success
import io.reactivex.netty.protocol.http.server.HttpServer
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import org.apache.log4j.BasicConfigurator
import rx.Observable

abstract class Server {

    abstract fun <T> respond(request: HttpServerRequest<T>): Observable<String>

    protected fun Observable<Success>.toResponse(): Observable<String> = this
        .map { it.toString() }
        .onErrorReturn { it.message }

    fun start(port: Int) {
        BasicConfigurator.configure()
        HttpServer
            .newServer(port)
            .start { req, resp ->
                val response = respond(req)
                resp.writeString(response.map { it + '\n' })
            }
            .awaitShutdown()
    }

}
