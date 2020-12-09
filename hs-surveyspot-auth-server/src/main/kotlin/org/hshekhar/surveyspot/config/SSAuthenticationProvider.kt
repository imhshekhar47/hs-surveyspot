package org.hshekhar.surveyspot.config

import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import org.hshekhar.surveyspot.service.UserService
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class SSAuthenticationProvider(private val userService: UserService) : AuthenticationProvider {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(SSAuthenticationProvider::class.java)
    }

    override fun authenticate(
        httpRequest: HttpRequest<*>?,
        authenticationRequest: AuthenticationRequest<*, *>?
    ): Publisher<AuthenticationResponse> {
        LOGGER.debug("entry: authenticate(${authenticationRequest?.identity})")
        return Flowable.create({
            val username = authenticationRequest?.identity.toString()
            val password = authenticationRequest?.secret.toString()

            val userDetail = userService.getUserByUserNameAndPassword(username = username, password = password)
            if (null !== userDetail) {
                it.onNext(userDetail)
                it.onComplete()
            } else {
                it.onError(AuthenticationException(AuthenticationFailed("Invalid Username or Password")))
            }
        }, BackpressureStrategy.ERROR)
    }
}