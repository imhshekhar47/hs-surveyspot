package org.hshekhar.surveyspot

import io.micronaut.runtime.Micronaut.*

fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("org.hshekhar.surveyspot")
		.start()
}

