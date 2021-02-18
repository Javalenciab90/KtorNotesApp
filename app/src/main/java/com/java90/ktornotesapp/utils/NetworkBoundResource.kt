package com.java90.ktornotesapp.utils

import kotlinx.coroutines.flow.*

/**
 * ResultType  -> load from database
 * RequestType -> what response from Api
 *
 * If ShouldFetch data >>
 * try to request to server with fetch -> get fetchedResult and save into Database
 * with saveFetchResult, then use query() to return data from database.
 * If the fetchResult failed, then use onFetchFailed and use query() to return
 * data that it was there, and send the Error because we can't connect with API.
 * If NOT ShouldFetch data >>
 * just return data from database with query()
 */


inline fun <ResultType, RequestType> networkBoundResource(
       crossinline query: () -> Flow<ResultType>,
       crossinline fetch: suspend () -> RequestType,
       crossinline saveFetchResult: suspend (RequestType) -> Unit,
       crossinline onFetchFailed: (Throwable) -> Unit = { Unit },
       crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {

    emit(Resource.loading(null))

    // the first emit of that flow (query) to data
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(Resource.loading(data))

        try {
            val fetchedResult = fetch()
            saveFetchResult(fetchedResult)
            query().map {
                Resource.success(it)
            }
        }catch (t: Throwable) {
            onFetchFailed(t)
            query().map {
                Resource.error("Couldn't reach server. It might be down", it)
            }
        }
    } else {
        query().map {
            Resource.success(it)
        }
    }
    emitAll(flow)
}