package my.project.api.signup

import my.project.api.base.Request

data class SignUpRequest(
    val email: String,
    val password: String
) : Request()
