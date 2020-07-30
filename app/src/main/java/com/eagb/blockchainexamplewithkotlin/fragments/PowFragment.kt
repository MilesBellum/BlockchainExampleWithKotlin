package com.eagb.blockchainexamplewithkotlin.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.eagb.blockchainexamplewithkotlin.R
import com.eagb.blockchainexamplewithkotlin.databinding.FragmentPowBinding
import com.eagb.blockchainexamplewithkotlin.managers.SharedPreferencesManager

class PowFragment : DialogFragment() {

    private lateinit var viewBinding: FragmentPowBinding
    private lateinit var prefs: SharedPreferencesManager

    companion object {
        fun newInstance(): PowFragment {
            return PowFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = FragmentPowBinding.inflate(
            layoutInflater,
            container,
            false
        )

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = SharedPreferencesManager(requireContext())
        viewBinding.edtSetPow.setText(prefs.getPowValue().toString())
        viewBinding.btnClose.setOnClickListener(clickListener)
        viewBinding.btnContinue.setOnClickListener(clickListener)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )

        return dialog
    }

    private val clickListener = View.OnClickListener { view ->
        when (view?.id) {
            R.id.btn_close -> dismiss()
            R.id.btn_continue -> setNewPow()
        }
    }

    private fun setNewPow() {
        viewBinding.edtSetPow.text?.let {
            val pow = viewBinding.edtSetPow.text.toString()
            prefs.setPowValue(pow.toInt())

            if (activity != null) {
                val intent = requireContext().packageManager
                    .getLaunchIntentForPackage(requireContext().packageName)
                startActivity(intent)
                requireActivity().finish()
            } else {
                dismiss()
            }
        }
    }
}