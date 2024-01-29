package my.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import my.project.api.base.Api
import my.project.api.base.ApiError
import my.project.api.base.Urls
import my.project.api.login.LoginRequest
import my.project.api.login.LoginResponse

class LoginActivity : AppCompatActivity()
{
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText

    private lateinit var prgLogin: ProgressBar
    private lateinit var btnLogin: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?)
    {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        prgLogin = findViewById(R.id.prgLogin)

        btnLogin = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener { login() }

        findViewById<Button>(R.id.btnGoToSignUp).setOnClickListener { goToSignUp() }
    }

    private fun validate(): Boolean
    {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        var isValid = true

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Snackbar.make(etEmail, "Email is invalid", Snackbar.LENGTH_LONG).show()
            isValid = false
        }

        val hasMinSize = password.length >= 8
        if (!hasMinSize)
        {
            Snackbar.make(etPassword, "Password must be 8 or more characters long", Snackbar.LENGTH_LONG).show()
            return false
        }

        val hasCapital = Regex("[A-Z]+").containsMatchIn(password)
        val hasSmall = Regex("[a-z]+").containsMatchIn(password)
        val hasDigit = Regex("[0-9]+").containsMatchIn(password)
        val hasSpecial = Regex("[^A-Za-z0-9]+").containsMatchIn(password)

        if (!hasCapital || !hasSmall || !hasDigit || !hasSpecial)
        {
            Snackbar.make(etPassword, "Password must contain at least one capital, one small, one digit and one special character", Snackbar.LENGTH_LONG).show()
            isValid = false
        }

        return isValid
    }

    private fun login()
    {
        if (!validate())
            return

        btnLogin.visibility = View.GONE
        prgLogin.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch { sendRequest() }
    }

    private suspend fun sendRequest()
    {
        try
        {
            val request = LoginRequest(etEmail.text.toString(), etPassword.text.toString())
            val response = Api.post(Urls.LOGIN, request, LoginResponse::class.java)

            withContext(Dispatchers.Main) {
                val prefs = this@LoginActivity.getSharedPreferences("tokens", MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putString("accessToken", response.accessToken)
                editor.putString("refreshToken", response.refreshToken)
                editor.putString("clientId", response.clientId)
                editor.apply()

                btnLogin.visibility = View.VISIBLE
                prgLogin.visibility = View.GONE

                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
        catch (e: ApiError)
        {
            withContext(Dispatchers.Main) {
                btnLogin.visibility = View.VISIBLE
                prgLogin.visibility = View.GONE

                Snackbar.make(etEmail, e.messages.get(0), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun goToSignUp()
    {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}