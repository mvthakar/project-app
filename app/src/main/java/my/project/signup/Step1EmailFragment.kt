package my.project.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import my.project.R

class Step1EmailFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_step1_email, container, false)

        view.findViewById<Button>(R.id.btnSendOtp).setOnClickListener { showStep2() }

        return view
    }

    private fun showStep2()
    {
        findNavController().navigate(R.id.step2_otp)
    }
}