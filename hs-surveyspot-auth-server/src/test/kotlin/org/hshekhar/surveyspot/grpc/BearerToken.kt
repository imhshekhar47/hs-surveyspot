package org.hshekhar.surveyspot.grpc

import io.grpc.CallCredentials
import io.grpc.Metadata
import io.grpc.Status
import java.util.concurrent.Executor

class BearerToken(private val value: String): CallCredentials() {

    override fun thisUsesUnstableApi() {}

    override fun applyRequestMetadata(requestInfo: RequestInfo?, appExecutor: Executor?, applier: MetadataApplier?) {
        appExecutor?.execute {
            try {
                val header = Metadata()
                header.put(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER), "Bearer $value")
                applier?.apply(header)
            } catch (e: Throwable) {
                applier?.fail(Status.UNAUTHENTICATED.withCause(e))
            }
        }
    }
}