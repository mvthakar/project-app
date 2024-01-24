package my.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class LoginActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}