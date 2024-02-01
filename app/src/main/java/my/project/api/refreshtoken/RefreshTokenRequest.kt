package my.project.api.refreshtoken

import my.project.api.base.Request

data class RefreshTokenRequest(
    val refreshToken: String?,
    val clientId: String?
) : Request()
