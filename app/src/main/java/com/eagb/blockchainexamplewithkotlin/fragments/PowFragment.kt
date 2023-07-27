package com.eagb.blockchainexamplewithkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.eagb.blockchainexamplewithkotlin.R
import com.eagb.blockchainexamplewithkotlin.databinding.FragmentPowBinding
import com.eagb.blockchainexamplewithkotlin.managers.AppManager
import com.eagb.blockchainexamplewithkotlin.managers.SharedPreferencesManager

class PowFragment : Fragment(R.layout.fragment_pow) {

    private var _binding: FragmentPowBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: SharedPreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPowBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = SharedPreferencesManager(requireContext())
        binding.edtSetPow.setText(prefs.getPowValue().toString())
        binding.btnClose.setOnClickListener(clickListener)
        binding.btnContinue.setOnClickListener(clickListener)
    }

    private val clickListener = View.OnClickListener { view ->
        when (view?.id) {
            R.id.btn_close -> requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            R.id.btn_continue -> setNewPow()
        }
    }

    private fun setNewPow() {
        binding.edtSetPow.text?.let {
            val pow = binding.edtSetPow.text.toString()
            val appManager = AppManager(requireContext())
            prefs.setPowValue(pow.toInt())
            appManager.restartApp()
        }
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }
}
