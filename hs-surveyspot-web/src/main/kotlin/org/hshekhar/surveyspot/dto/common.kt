package org.hshekhar.surveyspot.dto

data class ResponseDTO<T>(val data: T? = null, val error: String? = null)
