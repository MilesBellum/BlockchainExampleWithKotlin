package com.eagb.blockchainexamplewithkotlin.fragments

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.eagb.blockchainexamplewithkotlin.BuildConfig
import com.eagb.blockchainexamplewithkotlin.R
import com.eagb.blockchainexamplewithkotlin.databinding.FragmentMoreInfoBinding

class MoreInfoFragment : DialogFragment() {

    private var _binding: FragmentMoreInfoBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): MoreInfoFragment {
            return MoreInfoFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMoreInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val versionName = BuildConfig.VERSION_NAME
        val versionCode = BuildConfig.VERSION_CODE
        val appVersion = "v.$versionName - Build $versionCode"
        binding.txtAppVersion.text = appVersion

        binding.btnClose.setOnClickListener(clickListener)
        binding.llCheckBlockchain.setOnClickListener(clickListener)
        binding.llCheckWhitePaper.setOnClickListener(clickListener)
        binding.llCheckBook1.setOnClickListener(clickListener)
        binding.llCheckBook2.setOnClickListener(clickListener)
        binding.llCheckRepo.setOnClickListener(clickListener)
        binding.llCheckWeb.setOnClickListener(clickListener)
        binding.txtHeart.setOnClickListener(clickListener)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.let {
                it.setLayout(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
                it.setBackgroundDrawableResource(android.R.color.transparent)
            }
        }

        return dialog
    }

    private val clickListener = View.OnClickListener { view ->
        val url: String

        when (view?.id) {
            R.id.btn_close -> dismiss()

            R.id.ll_check_blockchain -> {
                // Checking a Blockchain tutorial
                url = "https://www.guru99.com/blockchain-tutorial.html"
                openUrl(url)
            }

            R.id.ll_check_white_paper -> {
                // Checking the White Paper of Bitcoin
                url = "https://bitcoin.org/bitcoin.pdf"
                openUrl(url)
            }

            R.id.ll_check_book_1 -> {
                // Checking Blockchain Revolution book
                url = "https://www.amazon.com/dp/1101980141/ref=cm_sw_em_r_mt_dp_U_amjmDbR0D5S46"
                openUrl(url)
            }

            R.id.ll_check_book_2 -> {
                // Checking The Science of the Blockchain book
                url = "https://www.amazon.com/dp/1544232101/ref=cm_sw_em_r_mt_dp_U_wnjmDbKXPKTCP"
                openUrl(url)
            }

            R.id.ll_check_repo -> {
                // Checking the official repo to fork
                url = "https://github.com/MilesBellum/BlockchainExampleWithKotlin"
                openUrl(url)
            }

            R.id.ll_check_web -> {
                // Checking the official web site
                url = "https://eagb-corp.web.app"
                openUrl(url)
            }

            R.id.txt_heart -> {
                // Showing an EasterEgg
                Toast.makeText(context, R.string.text_thank_you, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }
}
