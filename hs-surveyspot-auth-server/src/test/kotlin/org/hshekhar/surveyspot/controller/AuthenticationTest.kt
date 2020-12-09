package org.hshekhar.surveyspot.controller

import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.simple.SimpleHttpRequest
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.endpoints.TokenRefreshRequest
import io.micronaut.security.token.jwt.render.AccessRefreshToken
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import javax.inject.Inject

@MicronautTest
internal class AuthenticationTest {

    @Inject
    @field:Client("/")
    lateinit var httpClient: RxHttpClient

    @Test
    fun `should be able to obtain access token`() {
        val credentials = UsernamePasswordCredentials("demo", "demo")
        val response = httpClient.toBlocking().exchange(SimpleHttpRequest(HttpMethod.POST, "/login", credentials), AccessRefreshToken::class.java)
        Assertions.assertEquals(HttpStatus.OK, response.status, "Login should return status OK")
        Assertions.assertTrue(response.body.isPresent, "Response body must contain AccessToken")
        Assertions.assertNotNull(response.body.get().accessToken, "Token should be present")
        Assertions.assertTrue(JWTParser.parse(response.body.get().accessToken) is SignedJWT, "Token must be a signed one")
        Assertions.assertNotNull(response.body.get().refreshToken, "Refresh token should be present")
        Assertions.assertTrue(JWTParser.parse(response.body.get().refreshToken) is SignedJWT, "Refresh token must be a signed one")
    }

    @Test
    fun `should be able to refresh token on expiration`() {
        val credentials = UsernamePasswordCredentials("demo", "demo")
        val response = httpClient.toBlocking().exchange(
            SimpleHttpRequest(HttpMethod.POST, "/login", credentials), AccessRefreshToken::class.java)
        Assertions.assertEquals(HttpStatus.OK, response.status, "Login should return status OK")

        val token = response.body.get()
        if( token.expiresIn < 100) {
            Thread.sleep(token.expiresIn + 10L)

            val tokenRefreshRequest = TokenRefreshRequest(token.refreshToken)
            val responseOnRefreshToken = httpClient.toBlocking().exchange(
                SimpleHttpRequest(HttpMethod.POST, "/oauth/access_token", tokenRefreshRequest),
                AccessRefreshToken::class.java
            )

            Assertions.assertEquals(HttpStatus.OK, responseOnRefreshToken.status)
            Assertions.assertNotNull(responseOnRefreshToken.body.get().accessToken, "Must return a new access token")
            Assertions.assertNotNull(responseOnRefreshToken.body.get().refreshToken, "Must return a new refresh token")
        } else {
            fail("Token expiration ${token.expiresIn} is too long to test")
        }
    }
}