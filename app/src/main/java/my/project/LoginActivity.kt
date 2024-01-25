package my.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class LoginActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.btnGoToSignUp).setOnClickListener { goToSignUp() }
    }

    private fun goToSignUp()
    {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}