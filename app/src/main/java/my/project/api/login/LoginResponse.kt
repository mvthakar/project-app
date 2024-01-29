package my.project.api.login

import my.project.api.base.Response

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val clientId: String,
) : Response()
