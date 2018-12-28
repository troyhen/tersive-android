package com.troy.tersive.model.ws

data class ServerResponse(
    val result: ServerResult,
    val message: String? = null,
    val data: Map<String, String>? = null
)

enum class ServerResult {
    SUCCESS, INVALID, FAIL, ERROR
}

data class UserDto(
    val email: String,
    val passHash: Long
)
