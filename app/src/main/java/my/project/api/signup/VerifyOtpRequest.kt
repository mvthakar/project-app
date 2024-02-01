package my.project.api.signup

import my.project.api.base.Request

data class VerifyOtpRequest(
    val email: String,
    val otp: String
) : Request()