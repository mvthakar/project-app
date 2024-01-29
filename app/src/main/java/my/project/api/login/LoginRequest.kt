package my.project.api.login

import my.project.api.base.Request

data class LoginRequest(val email: String, val password: String) : Request()