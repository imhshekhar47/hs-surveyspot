package org.hshekhar.surveyspot.config

import io.grpc.ManagedChannelBuilder
import org.hshekhar.surveyspot.error.SSError
import org.hshekhar.surveyspot.error.SSErrorCode
import org.hshekhar.surveyspot.proto.LoginAPIGrpc
import org.hshekhar.surveyspot.proto.LoginRequest
import org.hshekhar.surveyspot.proto.LoginResponse
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class LoginGrpcClient {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(LoginGrpcClient::class.java)
    }

    private var stub: LoginAPIGrpc.LoginAPIBlockingStub? = null

    private fun getStub(): LoginAPIGrpc.LoginAPIBlockingStub {

        if(null == stub) {
            LOGGER.debug("LoginGrpcClient stub initialization")
            this.stub = LoginAPIGrpc.newBlockingStub(ManagedChannelBuilder
                    .forAddress("localhost", 50051)
                    .usePlaintext()
                    .build())
        }

        return this.stub!!
    }

    fun login(request: LoginRequest): LoginResponse {
        LOGGER.trace("entry: login()")
        try {
            val response = getStub().logIn(LoginRequest.newBuilder()
                .setUsername(request.username)
                .setPassword(request.password)
                .build())
            LOGGER.trace("exit: login()")
            return response
        } catch (e: Throwable) {
            LOGGER.error("exit: login(): SSErrorCode.GRPC_SERVER_ERROR", e)
            when (e) {
                is RuntimeException -> throw SSError(
                        errorCode = SSErrorCode.GRPC_SERVER_ERROR,
                        message = e.message,
                        cause = e)
                else -> throw SSError(
                        errorCode = SSErrorCode.GRPC_SERVER_ERROR,
                        message = e.message,
                        cause = e)
            }
        }
    }
}