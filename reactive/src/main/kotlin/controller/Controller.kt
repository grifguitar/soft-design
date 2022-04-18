package controller

import model.Catalog
import rx.Observable

object Controller {
    private const val OPEN_TAG = "<body>"
    private const val CLOSE_TAG = "</body>"
    private const val USAGE =
        "<h3>EXPECTED FORMAT:</h3><p>localhost:PORT/query?parameter=value;</p>"
    private const val QUERY_USAGE =
        "<h3>AVAILABLE QUERIES:</h3><p>/addUser</p><p>/getUser</p><p>/addProduct</p><p>/getAllProducts</p>"

    private fun withUsage(message: String): String {
        return "$OPEN_TAG$USAGE$QUERY_USAGE<h3>MESSAGE:</h3><p><font color=red>$message</font></p>$CLOSE_TAG"
    }

    class ControllerException(override var message: String) : Exception(message) {
        init {
            message = withUsage(message)
        }
    }

    private fun getValue(param: Map<String, List<String>>, name: String): String {
        return param[name]?.getOrNull(0) ?: throw ControllerException("missing parameter: $name")
    }

    fun addUser(param: Map<String, List<String>>): Observable<String> {
        return Catalog.addUser(getValue(param, "name"), getValue(param, "currency"))
    }

    fun getUser(param: Map<String, List<String>>): Observable<String> {
        return Catalog.getUser(getValue(param, "id"))
    }

    fun addProduct(param: Map<String, List<String>>): Observable<String> {
        return Catalog.addProduct(getValue(param, "name"), getValue(param, "price").toDouble())
    }

    fun getAllProducts(param: Map<String, List<String>>): Observable<String> {
        return Catalog.getAllProducts(getValue(param, "userId"))
    }
}
