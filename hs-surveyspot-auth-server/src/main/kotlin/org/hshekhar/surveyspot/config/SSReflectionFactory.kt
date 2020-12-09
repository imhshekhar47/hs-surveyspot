package org.hshekhar.surveyspot.config

import io.grpc.protobuf.services.ProtoReflectionService
import io.micronaut.context.annotation.Factory
import javax.inject.Singleton

@Factory
internal class SSReflectionFactory {
    @Singleton
    fun reflectionService(): ProtoReflectionService {
        return ProtoReflectionService.newInstance() as ProtoReflectionService
    }
}