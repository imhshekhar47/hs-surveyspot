package org.hshekhar.surveyspot.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/api")
class HomeController {

    @Get("/{title}")
    fun indexView(title: String): HttpResponse<*> {
        return HttpResponse.ok("hello $title")
    }
}