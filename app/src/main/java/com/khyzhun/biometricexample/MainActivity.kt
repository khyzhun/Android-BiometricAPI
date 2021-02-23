package com.khyzhun.biometricexample

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

	lateinit var textView: TextView

	private lateinit var executor: Executor
	private lateinit var biometricPrompt: BiometricPrompt
	private lateinit var promptInfo: BiometricPrompt.PromptInfo

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		textView = findViewById(R.id.textView)
		textView.setOnClickListener {
			promptInfo = createBiometricPromptBuilder()

			val canStrong = BiometricManager.from(this).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
			val canWeak = BiometricManager.from(this).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)

			if (canStrong == BiometricManager.BIOMETRIC_SUCCESS || canWeak == BiometricManager.BIOMETRIC_SUCCESS) {
				showBiometricDialog()
			} else {
				loginWithPassword()
			}
		}

	}

	private fun showBiometricDialog() {
		executor = ContextCompat.getMainExecutor(this)
		biometricPrompt = BiometricPrompt(this, executor, BiometricPromptCallback(
			::onSuccess,
			::onError,
			::onFailed
		))
		biometricPrompt.authenticate(promptInfo)
	}

	private fun createBiometricPromptBuilder() = BiometricPrompt.PromptInfo.Builder()
		.setTitle(getString(R.string.biometric_title))
		.setSubtitle(getString(R.string.biometric_subtitle))
		.setConfirmationRequired(true)
		/**
		 * API: from 21 till 29.
		 * setDeviceCredentialAllowed(true)
		 */
		.setNegativeButtonText(getString(R.string.biometric_negative_button))
		.setDescription(getString(R.string.biometric_description))
		.build()

	private fun onError(code: Int, message: String) {
		renderMessage(message)
		if (code == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
			// Because negative button says use application password
			loginWithPassword()
		}
	}


	private fun onSuccess(result: BiometricPrompt.AuthenticationResult) = renderMessage(getString(R.string.message_success))

	private fun onFailed() = renderMessage(getString(R.string.message_failed))

	private fun renderMessage(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

	private fun loginWithPassword() { /*...*/ }


}