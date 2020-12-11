package org.hshekhar.surveyspot

import io.micronaut.runtime.Micronaut
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License


/*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("org.hshekhar.surveyspot")
		.start()
}

*/
@OpenAPIDefinition(
		info = Info(
				title = "Hello World",
				version = "1.0.0",
				description = "Survey-spot web APIs",
				license = License(
						name = "Apache 2.0",
						url = "https://foo.bar"),
				contact = Contact(
						url = "http://github.com/imhshekhar47",
						name = "imhshekhar47",
						email = "himanshu.shekhar.in@gmail.com")
		)
)
object Application {
	@JvmStatic
	fun main(args: Array<String>) {
		Micronaut.build()
				//.packages("org.hshekhar.surveyspot")
				//.eagerInitSingletons(true)
				.mainClass(Application.javaClass)
				.start()
	}
}