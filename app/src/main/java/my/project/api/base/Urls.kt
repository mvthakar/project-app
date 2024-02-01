package my.project.api.base

class Urls
{
    companion object
    {
        // const val URL = "https://project.maxoduke.dev/api"
        private const val BASE_URL = "http://10.0.2.2/project/api"

        const val SEND_OTP = "$BASE_URL/auth/otp/check-and-send.php"
        const val VERIFY_OTP = "$BASE_URL/auth/otp/verify.php"

        const val SIGN_UP = "$BASE_URL/auth/signup.php"

        const val LOGIN = "$BASE_URL/auth/login.php"
        const val REFRESH_TOKEN = "$BASE_URL/auth/tokens/refresh.php"
    }
}