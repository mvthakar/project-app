package my.project.api.signup

import my.project.api.base.Request

data class SendOtpRequest(val email: String) : Request()