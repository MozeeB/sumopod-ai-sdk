package com.cikup.sumopod.ai.error

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val error: ErrorBody? = null,
) {
    @Serializable
    data class ErrorBody(
        val message: String? = null,
        val type: String? = null,
        val param: String? = null,
        val code: String? = null,
    )
}
