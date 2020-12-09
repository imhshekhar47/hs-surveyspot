package org.hshekhar.surveyspot.controller

import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.simple.SimpleHttpRequest
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.render.AccessRefreshToken
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.function.Executable
import javax.inject.Inject

@MicronautTest
internal class AppControllerTest {

    @Inject
    @field:Client("/")
    lateinit var httpClient: RxHttpClient

    @Test
    fun `should be able to call insecure api`() {
        val response = httpClient.toBlocking().exchange(
            SimpleHttpRequest(HttpMethod.GET, "/api/ping", null), String::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, response.status, "Status should be OK")
        Assertions.assertEquals("pong", response.body.get(), "pong should be returned")
    }

    @Test
    fun `should fail to call secure api`() {
        val e = Executable {
                httpClient.toBlocking().exchange(
                    SimpleHttpRequest(HttpMethod.GET, "/api/ask", null), String::class.java)
            }
        val thrown = Assertions.assertThrows(HttpClientResponseException::class.java, e)
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, thrown.status, "Status should be UNAUTHORIZED")
    }

    @Test
    fun `should fail to call secure api with access_token`() {
        val credentials = UsernamePasswordCredentials("demo", "demo")
        val response = httpClient.toBlocking().exchange(
            SimpleHttpRequest(HttpMethod.POST, "/login", credentials), AccessRefreshToken::class.java)
        Assertions.assertEquals(HttpStatus.OK, response.status, "Login should return status OK")


        val responseWithToken = httpClient.toBlocking().exchange(
            SimpleHttpRequest(HttpMethod.GET, "/api/ask", null).bearerAuth(response.body()?.accessToken), String::class.java)

        Assertions.assertEquals(HttpStatus.OK, responseWithToken.status, "Status should be OK")
    }
}