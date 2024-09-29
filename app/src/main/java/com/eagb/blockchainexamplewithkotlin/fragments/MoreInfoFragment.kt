package com.eagb.blockchainexamplewithkotlin.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.eagb.blockchainexamplewithkotlin.BuildConfig
import com.eagb.blockchainexamplewithkotlin.R
import com.eagb.blockchainexamplewithkotlin.databinding.FragmentMoreInfoBinding

class MoreInfoFragment : Fragment(R.layout.fragment_more_info) {
    private var _binding: FragmentMoreInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMoreInfoBinding.inflate(
            layoutInflater,
            container,
            false,
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setUpView()
    }

    /**
     * Sets up the view.
     */
    private fun FragmentMoreInfoBinding.setUpView() {
        val versionName = BuildConfig.VERSION_NAME
        val versionCode = BuildConfig.VERSION_CODE
        val appVersion = "v.$versionName - Build $versionCode"
        txtAppVersion.text = appVersion

        btnClose.setOnClickListener(clickListener)
        llCheckBlockchain.setOnClickListener(clickListener)
        llCheckWhitePaper.setOnClickListener(clickListener)
        llCheckBook1.setOnClickListener(clickListener)
        llCheckBook2.setOnClickListener(clickListener)
        llCheckRepo.setOnClickListener(clickListener)
        llCheckWeb.setOnClickListener(clickListener)
        txtHeart.setOnClickListener(clickListener)
    }

    private val clickListener = View.OnClickListener { view ->
        val url: String

        when (view?.id) {
            R.id.btn_close -> requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()

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
                url = "https://eagbcorp.com"
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
