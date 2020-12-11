package org.hshekhar.surveyspot.config

import io.micronaut.context.annotation.Factory

@Factory
class GrpcClient {

    /*@Bean
    fun blockingStub(@GrpcChannel("authorizer") channel: ManagedChannel): LoginAPIGrpc.LoginAPIBlockingStub {
        println("==================================LoginAPIGrpc.LoginAPIBlockingStub")
        return LoginAPIGrpc.newBlockingStub(channel)
    }*/
}

