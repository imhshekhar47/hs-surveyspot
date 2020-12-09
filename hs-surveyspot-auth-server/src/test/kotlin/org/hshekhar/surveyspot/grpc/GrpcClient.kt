package org.hshekhar.surveyspot.grpc

import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import org.hshekhar.surveyspot.proto.LoginAPIGrpc

@Factory
class GrpcClient {

    @Bean
    fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): LoginAPIGrpc.LoginAPIBlockingStub {
        return LoginAPIGrpc.newBlockingStub(channel)
    }
}