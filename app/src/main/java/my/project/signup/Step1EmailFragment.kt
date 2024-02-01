package my.project.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
import my.project.api.signup.SendOtpRequest

class Step1EmailFragment : Fragment()
{
    private lateinit var etEmail: TextInputEditText
    private lateinit var prgSendOtp: ProgressBar
    private lateinit var btnSendOtp: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_step1_email, container, false)

        etEmail = view.findViewById(R.id.etEmail)
        prgSendOtp = view.findViewById(R.id.prgSendOtp)
        btnSendOtp = view.findViewById(R.id.btnSendOtp)
        btnSendOtp.setOnClickListener { sendOtp() }

        return view
    }

    private fun sendOtp()
    {
        val email = etEmail.text.toString()
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Snackbar.make(etEmail, "Email is invalid", Snackbar.LENGTH_LONG).show()
            return
        }

        prgSendOtp.visibility = View.VISIBLE
        btnSendOtp.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            try
            {
                val request = SendOtpRequest(email)
                Api.post(
                    Urls.SEND_OTP,
                    request,
                    Response::class.java,
                    refreshTokensIfNecessary = false
                )

                withContext(Dispatchers.Main) {
                    prgSendOtp.visibility = View.GONE
                    btnSendOtp.visibility = View.VISIBLE

                    (requireActivity() as SignUpActivity).signUpEmail = email
                    findNavController().navigate(R.id.step2_otp)
                }
            }
            catch (ex: ApiError)
            {
                withContext(Dispatchers.Main) {
                    prgSendOtp.visibility = View.GONE
                    btnSendOtp.visibility = View.VISIBLE

                    Snackbar.make(etEmail, ex.messages?.get(0) ?: "An error occurred", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}