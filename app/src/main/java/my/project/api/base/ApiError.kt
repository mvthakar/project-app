package my.project.api.base

import java.lang.Exception

class ApiError : Exception()
{
    var statusCode: Int = 0
    val messages: MutableList<String> = mutableListOf()
}