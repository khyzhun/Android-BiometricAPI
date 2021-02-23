package com.khyzhun.biometricexample

import androidx.biometric.BiometricPrompt

class BiometricPromptCallback(
	private val onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
	private val onError: (Int, String) -> Unit,
	private val onFailed: () -> Unit
) : BiometricPrompt.AuthenticationCallback() {

	override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
		super.onAuthenticationSucceeded(result)
		onSuccess.invoke(result)
	}

	override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
		super.onAuthenticationError(errorCode, errString)
		onError.invoke(errorCode, errString.toString())
	}

	override fun onAuthenticationFailed() {
		super.onAuthenticationFailed()
		onFailed.invoke()
	}

}
