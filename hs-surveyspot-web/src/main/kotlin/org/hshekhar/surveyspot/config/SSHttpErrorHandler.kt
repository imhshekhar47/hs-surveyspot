package org.hshekhar.surveyspot.config

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.server.exceptions.ExceptionHandler
import org.hshekhar.surveyspot.error.SSError
import org.hshekhar.surveyspot.error.SSErrorCode
import javax.inject.Singleton

@Singleton
class SSHttpErrorHandler: ExceptionHandler<SSError, HttpResponse<*>> {
    override fun handle(request: HttpRequest<*>?, exception: SSError?): HttpResponse<*> {

        return if(null != exception) {
            when(exception.errorCode) {
                SSErrorCode.GRPC_SERVER_ERROR -> HttpResponse.serverError<Any>()
                else -> HttpResponse.serverError<Any>()
            }
        } else {
            HttpResponse.ok<Any>()
        }
    }
}