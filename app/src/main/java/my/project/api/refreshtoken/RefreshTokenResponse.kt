package my.project.api.refreshtoken

import my.project.api.base.Response

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val clientId: String,
) : Response()