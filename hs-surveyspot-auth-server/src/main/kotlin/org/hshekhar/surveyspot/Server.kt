package org.hshekhar.surveyspot

import io.micronaut.context.annotation.Bean
import io.micronaut.runtime.Micronaut
import org.hshekhar.surveyspot.service.UserService

object Server {

    @Bean
    fun userService(): UserService {
        return UserService()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
            .packages("org.hshekhar.surveyspot")
            .mainClass(Server.javaClass)
            .start()
    }
}