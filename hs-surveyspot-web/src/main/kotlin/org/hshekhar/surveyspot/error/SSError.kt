package org.hshekhar.surveyspot.error

enum class SSErrorCode {
    GRPC_SERVER_ERROR,
    INTERNAL_ERROR,
    BAD_REQUEST
}

class SSError(val errorCode: SSErrorCode, message: String?, cause: Throwable?) : Exception(message, cause)