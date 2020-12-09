package org.hshekhar.surveyspot.grpc

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.hshekhar.surveyspot.proto.LoginAPIGrpc
import org.hshekhar.surveyspot.proto.LoginRequest
import org.hshekhar.surveyspot.proto.TokenVerificationRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import javax.inject.Inject

@MicronautTest
internal class AuthServerTest {

    @Inject
    lateinit var blockingStub: LoginAPIGrpc.LoginAPIBlockingStub

    @Test
    fun `should able to call login`() {
        val request = LoginRequest.newBuilder()
            .setUsername("demo")
            .setPassword("demo")
            .build()

        Assertions.assertNotNull(blockingStub.logIn(request).token)
    }

    @Test
    fun `should fail to call verifyToken`() {
        val request = TokenVerificationRequest.newBuilder()
            .setToken("12345")
            .build()

        try {
            blockingStub.verifyToken(request)
            fail("UNAUTHENTICATED access should not be allowed")
        } catch (e: Exception) {
            Assertions.assertEquals("UNAUTHENTICATED: Authorization token is missing", e.message)
        }
    }
}