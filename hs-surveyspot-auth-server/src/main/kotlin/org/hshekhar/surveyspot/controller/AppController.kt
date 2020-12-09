package org.hshekhar.surveyspot.controller

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Controller(value = "/api")
class AppController {

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get(value = "/ping")
    fun ping(): String {
        return "pong"
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Get(value = "/ask")
    fun ask(): String {
        return "Ask me"
    }
}