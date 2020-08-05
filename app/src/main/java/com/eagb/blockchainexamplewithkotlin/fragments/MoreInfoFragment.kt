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

    private lateinit var viewBinding: FragmentMoreInfoBinding

    companion object {
        fun newInstance(): MoreInfoFragment {
            return MoreInfoFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = FragmentMoreInfoBinding.inflate(
            layoutInflater,
            container,
            false
        )

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appVersion =
            "v.".plus(BuildConfig.VERSION_NAME.plus(" - Build ".plus(BuildConfig.VERSION_CODE)))
        viewBinding.txtAppVersion.text = appVersion

        viewBinding.btnClose.setOnClickListener(clickListener)
        viewBinding.llCheckBlockchain.setOnClickListener(clickListener)
        viewBinding.llCheckWhitePaper.setOnClickListener(clickListener)
        viewBinding.llCheckBook1.setOnClickListener(clickListener)
        viewBinding.llCheckBook2.setOnClickListener(clickListener)
        viewBinding.llCheckRepo.setOnClickListener(clickListener)
        viewBinding.llCheckWeb.setOnClickListener(clickListener)
        viewBinding.txtHeart.setOnClickListener(clickListener)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        return dialog
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        dialog?.let {
            it.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
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
}