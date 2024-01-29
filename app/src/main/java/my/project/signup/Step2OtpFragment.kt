package my.project.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import my.project.R

class Step2OtpFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_step2_otp, container, false)

        view.findViewById<Button>(R.id.btnVerifyOtp).setOnClickListener { showStep3() }

        return view
    }

    private fun showStep3()
    {
        findNavController().navigate(R.id.step3_password)
    }
}