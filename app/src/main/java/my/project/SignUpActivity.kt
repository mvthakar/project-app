package my.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.shuhart.stepview.StepView

class SignUpActivity : AppCompatActivity()
{
    private lateinit var stepView: StepView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        stepView = findViewById(R.id.steps)
        findViewById<Button>(R.id.btnGoToLogin).setOnClickListener { goToLogin() }
    }

    private fun goToLogin()
    {
         finish()
    }
}