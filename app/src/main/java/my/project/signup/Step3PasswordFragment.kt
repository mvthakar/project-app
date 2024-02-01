package my.project.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import my.project.R
import my.project.SignUpActivity
import my.project.api.base.Api
import my.project.api.base.ApiError
import my.project.api.base.Response
import my.project.api.base.Urls
import my.project.api.signup.SignUpRequest

class Step3PasswordFragment : Fragment()
{
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var prgSignUp: ProgressBar
    private lateinit var btnSignUp: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_step3_password, container, false)

        etPassword = view.findViewById(R.id.etPassword)
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword)
        prgSignUp = view.findViewById(R.id.prgSignUp)

        btnSignUp = view.findViewById(R.id.btnSignUp)
        btnSignUp.setOnClickListener { signUp() }

        return view
    }

    private fun signUp()
    {
        val email = (requireActivity() as SignUpActivity).signUpEmail!!
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        if (password != confirmPassword)
        {
            Snackbar.make(etPassword, "Passwords do not match!", Snackbar.LENGTH_LONG).show()
            return
        }

        prgSignUp.visibility = View.VISIBLE
        btnSignUp.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            try
            {
                val request = SignUpRequest(email, password)
                Api.post(
                    Urls.SIGN_UP,
                    request,
                    Response::class.java,
                    refreshTokensIfNecessary = false
                )

                withContext(Dispatchers.Main) {
                    prgSignUp.visibility = View.GONE
                    btnSignUp.visibility = View.VISIBLE

                    requireActivity().finish()
                }
            }
            catch (ex: ApiError)
            {
                withContext(Dispatchers.Main) {
                    prgSignUp.visibility = View.GONE
                    btnSignUp.visibility = View.VISIBLE

                    Snackbar.make(etPassword, ex.messages?.get(0) ?: "An error occurred", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}