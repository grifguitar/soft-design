package common.service

import com.mongodb.rx.client.Success
import rx.Observable

interface Command<S : Service, D : Any> {
    fun S.executeCommandWith(data: D): Observable<Success>
}
