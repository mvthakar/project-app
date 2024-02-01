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
import my.project.api.signup.VerifyOtpRequest

class Step2OtpFragment : Fragment()
{
    private lateinit var etOtp: TextInputEditText
    private lateinit var prgVerifyOrResendOtp: ProgressBar
    private lateinit var btnVerifyOtp: Button
    private lateinit var btnResendOtp: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_step2_otp, container, false)

        etOtp = view.findViewById(R.id.etOtp)
        prgVerifyOrResendOtp = view.findViewById(R.id.prgVerifyOrResendOtp)

        btnVerifyOtp = view.findViewById(R.id.btnVerifyOtp)
        btnVerifyOtp.setOnClickListener { verifyOtp() }

        btnResendOtp = view.findViewById(R.id.btnResendOtp)
        btnResendOtp.setOnClickListener { resendOtp() }

        return view
    }

    private fun verifyOtp()
    {
        prgVerifyOrResendOtp.visibility = View.VISIBLE
        btnVerifyOtp.visibility = View.GONE
        btnResendOtp.visibility = View.GONE

        val email = (requireActivity() as SignUpActivity).signUpEmail!!
        val otp = etOtp.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            try
            {
                val request = VerifyOtpRequest(email, otp)
                Api.post(
                    Urls.VERIFY_OTP,
                    request,
                    Response::class.java,
                    refreshTokensIfNecessary = false
                )

                withContext(Dispatchers.Main) {
                    prgVerifyOrResendOtp.visibility = View.GONE
                    btnVerifyOtp.visibility = View.VISIBLE
                    btnResendOtp.visibility = View.VISIBLE

                    findNavController().navigate(R.id.step3_password)
                }
            }
            catch (ex: ApiError)
            {
                withContext(Dispatchers.Main) {
                    prgVerifyOrResendOtp.visibility = View.GONE
                    btnVerifyOtp.visibility = View.VISIBLE
                    btnResendOtp.visibility = View.VISIBLE

                    Snackbar.make(etOtp, "Invalid OTP", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun resendOtp()
    {
        val email = (requireActivity() as SignUpActivity).signUpEmail

        prgVerifyOrResendOtp.visibility = View.VISIBLE
        btnVerifyOtp.visibility = View.GONE
        btnResendOtp.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            try
            {
                val request = SendOtpRequest(email!!)
                Api.post(
                    Urls.SEND_OTP,
                    request,
                    Response::class.java,
                    refreshTokensIfNecessary = false
                )

                withContext(Dispatchers.Main) {
                    prgVerifyOrResendOtp.visibility = View.GONE
                    btnVerifyOtp.visibility = View.VISIBLE
                    btnResendOtp.visibility = View.VISIBLE

                    (requireActivity() as SignUpActivity).signUpEmail = email
                }
            }
            catch (ex: ApiError)
            {
                withContext(Dispatchers.Main) {
                    prgVerifyOrResendOtp.visibility = View.GONE
                    btnVerifyOtp.visibility = View.VISIBLE
                    btnResendOtp.visibility = View.VISIBLE

                    Snackbar.make(etOtp, ex.messages?.get(0) ?: "An error occurred", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}