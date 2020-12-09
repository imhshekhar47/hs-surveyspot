package org.hshekhar.surveyspot.config

import io.micronaut.security.authentication.UserDetails
import io.micronaut.security.errors.IssuingAnAccessTokenErrorCode
import io.micronaut.security.errors.OauthErrorResponseException
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent
import io.micronaut.security.token.refresh.RefreshTokenPersistence
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import javax.inject.Singleton

data class SSRefreshTokenState(val username: String,
                               val token: String,
                               val userDetails: UserDetails,
                               val revoked: Boolean = false)

@Singleton
class SSRefreshTokenPersistence: RefreshTokenPersistence {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(SSRefreshTokenPersistence::class.java)
    }

    private val refreshTokenRepository = mutableMapOf<String, SSRefreshTokenState>()

    override fun persistToken(event: RefreshTokenGeneratedEvent?) {
        LOGGER.debug("entry: persistToken()")
        val refreshToken = event?.refreshToken
        if(null != refreshToken) {
            val userDetails = event.userDetails
            refreshTokenRepository[refreshToken] = SSRefreshTokenState(username = userDetails.username,
                token = refreshToken,
                userDetails = userDetails,
                revoked = false
            )
        }
        LOGGER.debug("exit: persistToken()")
    }

    override fun getUserDetails(refreshToken: String?): Publisher<UserDetails> {
        LOGGER.debug("entry: getUserDetails(***)")
        return Flowable.create({ emitter ->
            val tokenState = refreshTokenRepository[refreshToken]
            when {
                null == tokenState -> {
                    LOGGER.debug("exit: getUserDetails():INVALID_GRANT")
                    emitter.onError(OauthErrorResponseException(IssuingAnAccessTokenErrorCode.INVALID_GRANT, "Refresh token not found", null))
                }
                tokenState.revoked -> {
                    LOGGER.debug("exit: getUserDetails():INVALID_GRANT")
                    emitter.onError(OauthErrorResponseException(IssuingAnAccessTokenErrorCode.INVALID_GRANT, "Refresh token is revoked", null))
                }
                else -> {
                    LOGGER.debug("exit: getUserDetails():OK")
                    val userDetails = tokenState.userDetails
                    refreshTokenRepository[refreshToken!!] = tokenState.copy(revoked = true)
                    emitter.onNext(userDetails)
                    emitter.onComplete()
                }
            }
        }, BackpressureStrategy.ERROR)
    }
}