package common.service

import rx.Observable

interface Query<S : Service, D : Any, T : Any> {
    fun S.doRequestWith(data: D): Observable<T>
}
