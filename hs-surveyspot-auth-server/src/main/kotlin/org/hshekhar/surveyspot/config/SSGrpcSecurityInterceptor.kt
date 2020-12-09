package org.hshekhar.surveyspot.config

import com.nimbusds.jwt.JWTParser
import com.sun.media.jfxmedia.logging.Logger
import io.grpc.*
import io.grpc.Metadata.ASCII_STRING_MARSHALLER
import io.grpc.kotlin.AbstractCoroutineServerImpl
import io.micronaut.context.ApplicationContext
import io.micronaut.core.order.Ordered
import io.micronaut.security.annotation.Secured
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/*class SSGrpcSecurityFilterChain: ServerFilterChain {
    override fun proceed(request: HttpRequest<*>?): Publisher<MutableHttpResponse<*>> {
        return Publishers.just(SimpleHttpResponseFactory.INSTANCE.ok(""))
    }
}

class SSGrpcHttpRequest<ReqT : Any?, RespT : Any?>(val call: ServerCall<ReqT, RespT>?, headers: MutableHttpHeaders?)
    : SimpleHttpRequest<MutableHttpHeaders>(HttpMethod.POST, call?.methodDescriptor?.fullMethodName, null) {
    
    companion object {
        val ANNOTATION_VALUES = "micronaut.grpc.security.annotation.values"
    }
}*/

@Singleton
class SSGrpcSecurityInterceptor: ServerInterceptor, Ordered {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(SSGrpcSecurityInterceptor::class.java)
    }

    @Inject
    private lateinit var applicationContext: ApplicationContext

    private val mapOfSecuredRPC: MutableMap<String, List<String>> = mutableMapOf()

    override fun <ReqT : Any?, RespT : Any?> interceptCall(
        call: ServerCall<ReqT, RespT>?,
        headers: Metadata?,
        next: ServerCallHandler<ReqT, RespT>?
    ): ServerCall.Listener<ReqT> {
        LOGGER.debug("entry: interceptCall()")
        val requestedRPC = call?.methodDescriptor?.fullMethodName?.toLowerCase()

        if (mapOfSecuredRPC.isEmpty()) {
            loadSecuredRPCMetadata()
        }

        if (mapOfSecuredRPC.containsKey(requestedRPC)) {
            val status = when(val bearer = headers?.get(Metadata.Key.of("Authorization", ASCII_STRING_MARSHALLER))) {
                null -> Status.UNAUTHENTICATED.withDescription("Authorization token is missing")
                else ->
                    if (bearer.startsWith("Bearer")) {
                        val token = bearer.substring(7)
                        val claimSet = JWTParser.parse(token).jwtClaimsSet

                        //check if expired already expired
                        if(claimSet.expirationTime.after(Date())) {
                            LOGGER.debug("Token already expired at ${claimSet?.expirationTime}")
                            Status.UNAUTHENTICATED.withDescription("Token has expired")
                        }

                        //check if token has necessary roles
                        val requiredClaims = mapOfSecuredRPC[requestedRPC]
                        val tokenClaims = claimSet.getStringListClaim("roles")
                        val allowed = requiredClaims?.map { tokenClaims.contains(it) }
                            ?.reduce { acc, b -> acc || b }?:true

                        if(allowed) {
                            val ctx = Context.current().withValue(Context.key("clientid"), claimSet.subject)
                            return Contexts.interceptCall(ctx, call, headers, next)
                        } else {
                            Status.PERMISSION_DENIED.withDescription("You do not have sufficient privilege")
                        }

                    } else {
                        Status.UNAUTHENTICATED.withDescription("Invalid bearer token format")
                    }
            }
            call?.close(status, headers)
            return object : ServerCall.Listener<ReqT>() {}
        }
        return next?.startCall(call, headers)!!
    }

    private fun loadSecuredRPCMetadata() {
        LOGGER.debug("entry: loadSecuredRPCMetadata()")
        applicationContext.getBeansOfType(AbstractCoroutineServerImpl::class.java).forEach { serverImpl ->
            val serviceName = (serverImpl as AbstractCoroutineServerImpl).bindService().serviceDescriptor.name
            serverImpl.javaClass.methods
                .filter { it.isAnnotationPresent(Secured::class.java) }
                .forEach() { method ->
                    val securedAnnotation = (method.annotations[0] as Secured)
                    mapOfSecuredRPC.putIfAbsent(
                        "${serviceName}/${method.name}".toLowerCase(),
                        listOf(*securedAnnotation.value)
                    )
                }
        }
        if(LOGGER.isDebugEnabled) {
            mapOfSecuredRPC.forEach { (rpc, policy) ->
                LOGGER.debug("rpc[$rpc], policy[${policy.joinToString(",")}]")
            }
        }
        LOGGER.debug("exit: loadSecuredRPCMetadata()")
    }

    override fun getOrder(): Int {
        return Ordered.HIGHEST_PRECEDENCE
    }

}