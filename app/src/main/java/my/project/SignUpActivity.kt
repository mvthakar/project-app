package my.project

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.shuhart.stepview.StepView

class SignUpActivity : AppCompatActivity()
{
    var signUpEmail: String? = null
    private lateinit var stepView: StepView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        stepView = findViewById(R.id.steps)
        findViewById<Button>(R.id.btnGoToLogin).setOnClickListener { goToLogin() }

        val step1NavHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = step1NavHostFragment.navController
        navController.addOnDestinationChangedListener { _, navDestination, _ -> fragmentChanged(navDestination) }
    }

    private fun fragmentChanged(navDestination: NavDestination)
    {
        val step = when (navDestination.id)
        {
            R.id.step1_email -> 0
            R.id.step2_otp -> 1
            R.id.step3_password -> 2
            else -> -1
        }

        stepView.go(step, true)
    }

    private fun goToLogin()
    {
         finish()
    }
}
