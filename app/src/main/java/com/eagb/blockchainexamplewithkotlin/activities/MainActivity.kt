package com.eagb.blockchainexamplewithkotlin.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.os.PowerManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eagb.blockchainexamplewithkotlin.R
import com.eagb.blockchainexamplewithkotlin.databinding.ActivityMainBinding
import com.eagb.blockchainexamplewithkotlin.databinding.InputContainerBinding
import com.eagb.blockchainexamplewithkotlin.fragments.MoreInfoFragment
import com.eagb.blockchainexamplewithkotlin.fragments.PowFragment
import com.eagb.blockchainexamplewithkotlin.managers.BlockChainManager
import com.eagb.blockchainexamplewithkotlin.managers.SharedPreferencesManager
import com.eagb.blockchainexamplewithkotlin.utils.CipherUtils
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingInput: InputContainerBinding
    private lateinit var prefs: SharedPreferencesManager
    private lateinit var appUpdateManager: AppUpdateManager

    private var progressDialog: ProgressDialog? = null
    private var blockChain: BlockChainManager? = null
    private var isEncryptionActivated = false
    private var isDarkThemeActivated = false

    companion object {
        const val UPDATE_REQUEST_CODE = 1000
        const val TAG_POW_DIALOG = "proof_of_work_dialog"
        const val TAG_MORE_INFO_DIALOG = "more_info_dialog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = SharedPreferencesManager(this)
        isDarkThemeActivated = prefs.isDarkTheme()

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        var isPowerSaveMode: Boolean

        powerManager.let {
            isPowerSaveMode = it.isPowerSaveMode
        }

        if (isPowerSaveMode) {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
            )
        } else {
            if (isDarkThemeActivated) {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES,
                )
            } else {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO,
                )
            }
        }

        // Setting the Night mode - must be done before calling super()
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityMainBinding.inflate(layoutInflater)
        binding = ActivityMainBinding.bind(viewBinding.root)
        binding.toolbar.apply {
            setTitleTextColor(resources.getColor(android.R.color.white, context.theme))
            setSubtitleTextColor(resources.getColor(android.R.color.white, context.theme))
        }
        bindingInput = InputContainerBinding.bind(binding.inputContainer.root)
        setContentView(viewBinding.root)
        setSupportActionBar(binding.toolbar)

        // Check a possible update from Play Store
        checkUpdate()

        isEncryptionActivated = prefs.getEncryptionStatus()

        // Setting the Progress Dialog
        showProgressDialog(resources.getString(R.string.text_creating_blockchain))

        binding.recyclerContent.apply {
            // Use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // Use a linear layout manager
            layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            // Starting BlockChain request on a thread
            Thread(
                Runnable {
                    runOnUiThread {
                        // Initializing BlockChain...
                        // PROOF_OF_WORK = difficulty.
                        // Given some difficulty, the CPU will has to find a hash for the block
                        // starting with a given number of zeros.
                        // More Proof-of-Work will be harder to mine and will take longer time.
                        // Watch out!
                        blockChain = BlockChainManager(context, prefs.getPowValue())
                        adapter = blockChain?.adapter
                        cancelProgressDialog(progressDialog)
                    }
                },
            ).start()
        }

        bindingInput.btnSendData.setOnClickListener {
            // Start new request on a UI thread
            startBlockChain()
        }
    }

    /**
     * Checks a possible update from Play Store.
     */
    private fun checkUpdate() {
        // Creates instance of the manager
        appUpdateManager = AppUpdateManagerFactory.create(this)

        // Returns an intent object that you use to check for an update
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        // Checks that the platform will allow the specified type of update
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                // Request the update
                startTheUpdate(appUpdateManager, appUpdateInfo)
            }
        }
    }

    /**
     * If an update exist, request for the update.
     *
     * @param appUpdateManager is the manager to start the flow for result
     * [AppUpdateManager.startUpdateFlowForResult].
     * @param appUpdateInfo gets the app info [AppUpdateInfo].
     */
    private fun startTheUpdate(
        appUpdateManager: AppUpdateManager,
        appUpdateInfo: AppUpdateInfo,
    ) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                // Pass the intent that is returned by 'getAppUpdateInfo()'
                appUpdateInfo,
                // Or 'AppUpdateType.FLEXIBLE' for flexible updates
                AppUpdateType.IMMEDIATE,
                // The current activity making the update request
                this,
                // Include a request code to later monitor this update request
                UPDATE_REQUEST_CODE,
            )
        } catch (e: SendIntentException) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()

        // Continue with the update if one exists
        resumeTheUpdate()
    }

    /**
     * Continue with the update if one exists.
     */
    private fun resumeTheUpdate() {
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    // If an in-app update is already running, resume the update.
                    startTheUpdate(appUpdateManager, appUpdateInfo)
                }
            }
    }

    /**
     * Starting new request or block on a thread.
     */
    private fun startBlockChain() {
        // Setting the Progress Dialog
        showProgressDialog(resources.getString(R.string.text_mining_blocks))

        runOnUiThread {
            blockChain?.let {
                if (bindingInput.editMessage.text != null && binding.recyclerContent.adapter != null) {
                    val message = bindingInput.editMessage.text.toString()

                    if (message.isNotEmpty()) {
                        // Verification if encryption is activated
                        if (!isEncryptionActivated) {
                            // Broadcast data
                            it.addBlock(it.newBlock(message))
                        } else {
                            try {
                                // Broadcast data
                                it.addBlock(
                                    it.newBlock(
                                        CipherUtils.encryptIt(
                                            message,
                                        )?.trim(),
                                    ),
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                                printErrorSomethingWrong()
                            }
                        }

                        // Validate block's data
                        println(
                            resources.getString(
                                R.string.log_blockchain_valid,
                                it.isBlockChainValid(),
                            ),
                        )
                        if (it.isBlockChainValid()) {
                            // Preparing data to insert to RecyclerView
                            binding.recyclerContent.smoothScrollToPosition(it.adapter.itemCount - 1)
                            binding.recyclerContent.adapter?.notifyDataSetChanged()
                            // Cleaning the EditText
                            bindingInput.editMessage.setText("")
                        } else {
                            printErrorBlockchainCorrupted()
                        }
                    } else {
                        printErrorEmptyData()
                    }

                    cancelProgressDialog(progressDialog)
                } else {
                    printErrorSomethingWrong()
                }
            }
        }
    }

    private fun printErrorBlockchainCorrupted() {
        Toast.makeText(
            this,
            R.string.error_blockchain_corrupted,
            Toast.LENGTH_LONG,
        ).show()
    }

    private fun printErrorEmptyData() {
        Toast.makeText(
            this,
            R.string.error_empty_data,
            Toast.LENGTH_LONG,
        ).show()
    }

    private fun printErrorSomethingWrong() {
        Toast.makeText(
            this,
            R.string.error_something_wrong,
            Toast.LENGTH_LONG,
        ).show()
    }

    /**
     * Setting the Progress Dialog.
     *
     * @param loadingMessage is the message in [String].
     */
    private fun showProgressDialog(loadingMessage: String) {
        progressDialog = ProgressDialog(this@MainActivity)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setMessage(loadingMessage)
        progressDialog!!.setCancelable(false)
        progressDialog!!.max = 100
        progressDialog!!.show()
    }

    private fun cancelProgressDialog(progressDialog: ProgressDialog?) {
        progressDialog?.cancel()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val checkEncrypt = menu.findItem(R.id.action_encrypt)
        checkEncrypt.isChecked = isEncryptionActivated

        val checkTheme = menu.findItem(R.id.action_dark)
        checkTheme.isChecked = isDarkThemeActivated

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_pow -> onPowOptionTapped()
            R.id.action_encrypt -> {
                onEncryptionOptionTapped(item)
                return true
            }
            R.id.action_dark -> {
                onDarkThemeOptionTapped(item)
                return true
            }
            R.id.action_more -> onMoreInfoOptionTapped()
            R.id.action_exit -> finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun onPowOptionTapped() {
        val powFragment = PowFragment.newInstance()
        powFragment.show(this.supportFragmentManager, TAG_POW_DIALOG)
    }

    private fun onEncryptionOptionTapped(item: MenuItem) {
        isEncryptionActivated = !item.isChecked
        item.isChecked = isEncryptionActivated
        if (item.isChecked) {
            Toast.makeText(this, R.string.text_encryption_activated, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, R.string.text_encryption_deactivated, Toast.LENGTH_SHORT).show()
        }
        prefs.setEncryptionStatus(isEncryptionActivated)
    }

    private fun onDarkThemeOptionTapped(item: MenuItem) {
        isDarkThemeActivated = !item.isChecked
        item.isChecked = isDarkThemeActivated
        prefs.setDarkTheme(isDarkThemeActivated)
        val intent =
            this.packageManager.getLaunchIntentForPackage(this.packageName)
        startActivity(intent)
        finish()
    }

    private fun onMoreInfoOptionTapped() {
        val moreInfoFragment = MoreInfoFragment.newInstance()
        moreInfoFragment.show(this.supportFragmentManager, TAG_MORE_INFO_DIALOG)
    }
}
