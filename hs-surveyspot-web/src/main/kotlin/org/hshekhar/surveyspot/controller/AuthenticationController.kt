package org.hshekhar.surveyspot.controller

import io.micronaut.core.annotation.Order
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.swagger.v3.oas.annotations.tags.Tag
import org.hshekhar.surveyspot.dto.LoginRequestDTO
import org.hshekhar.surveyspot.dto.ResponseDTO
import org.hshekhar.surveyspot.proto.LoginRequest
import org.hshekhar.surveyspot.proto.LoginResponse
import org.hshekhar.surveyspot.service.AuthenticationService
import org.slf4j.LoggerFactory
import javax.inject.Inject

@Tag(name = "Authentication", description = "Authentication APIs for survey spot")
@Controller(value = "/api/authentication")
class AuthenticationController {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(AuthenticationController::class.java)
    }

    @Inject
    private lateinit var authenticationService: AuthenticationService

    @Post(consumes = [MediaType.APPLICATION_JSON])
    fun login(@Body request: LoginRequestDTO): MutableHttpResponse<*>? {
        LOGGER.trace("entry: login()")
        val response = try {
            val response = authenticationService.login(request)
            if (response.authToken.isNullOrEmpty()) {
                HttpResponse.badRequest(response)
            } else {
                HttpResponse.ok(response)
            }
        } catch (e: Throwable) {
            LOGGER.error("Login failed", e)
            HttpResponse.serverError(ResponseDTO<Any>(error = e.message))
        }

        LOGGER.trace("exit: login()")
        return response
    }
}