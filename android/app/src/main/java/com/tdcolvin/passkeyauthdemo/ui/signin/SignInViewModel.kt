package com.tdcolvin.passkeyauthdemo.ui.signin

import android.app.Activity
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.PublicKeyCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

data class SignInUiState(
    val getAuthenticationOptionsJsonResult: Result<String>? = null,
    val isGettingAuthenticationOptionsJson: Boolean = false,

    val getCredentialResult: Result<String>? = null,
    val isGettingCredential: Boolean = false,

    val sendAuthenticationResponseToServerResult: Result<Unit>? = null,
    val isSendingAuthenticationResponseToServer: Boolean = false
)

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val credentialManager: CredentialManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState = _uiState.asStateFlow()

    fun getPasskeyAuthenticationRequest(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isGettingAuthenticationOptionsJson = true) }

            val result = runCatching {

                /* TODO 5
                   Use okHttpClient (or your favourite HTTP library) to GET
                   https://auth.tomcolvin.co.uk/generate-authentication-options?username=$username

                   Return the response body.
                 */
                val url = HttpUrl.Builder()
                    .scheme("https")
                    .host("auth.tomcolvin.co.uk")
                    .addPathSegment("generate-authentication-options")
                    .addQueryParameter("username", username)
                    .build()
                val request = Request.Builder()
                    .url(url)
                    .build()

                val response = okHttpClient.newCall(request).execute()
                response.body.string()
            }

            _uiState.update {
                it.copy(
                    getAuthenticationOptionsJsonResult = result,
                    isGettingAuthenticationOptionsJson = false
                )
            }
        }
    }

    fun getCredential(activity: Activity) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isGettingCredential = true) }

            val result = runCatching {
                val authenticationRequestJson = _uiState.value.getAuthenticationOptionsJsonResult?.getOrNull()
                    ?: throw Exception("No authentication options available")
                Log.v("passkey", "Authentication request JSON: $authenticationRequestJson")

                /* TODO 6: Use Jetpack Credential Manager to generate a passkey

                   Hint:
                    1. Build a GetPublicKeyCredentialOption with the JSON above
                    2. Build a GetCredentialRequest using the above
                    3. Use Credential Manager to create the credential, and return the
                       authenticationRequestJson it produces
                 */

                val getPublicKeyCredentialOption = GetPublicKeyCredentialOption(
                    requestJson = authenticationRequestJson
                )
                val signInRequest = GetCredentialRequest(listOf(getPublicKeyCredentialOption))

                val result = credentialManager.getCredential(
                    context = activity,
                    request = signInRequest
                )

                val credential = result.credential

                if (credential !is PublicKeyCredential) {
                    throw Exception("Incorrect credential type")
                }

                credential.authenticationResponseJson
            }

            _uiState.update {
                it.copy(
                    getCredentialResult = result,
                    isGettingCredential = false
                )
            }
        }
    }

    fun sendAuthenticationResponse() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isSendingAuthenticationResponseToServer = true) }

            val result = runCatching {
                val authenticationResponseJson = _uiState.value.getCredentialResult?.getOrNull()
                    ?: throw Exception("No authentication response available")

                /* TODO 7
                   POST the authentication request to
                   https://auth.tomcolvin.co.uk/verify-authentication

                   Return the response body. Throw an exception if the response code != 200
                 */
                val url = HttpUrl.Builder()
                    .scheme("https")
                    .host("auth.tomcolvin.co.uk")
                    .addPathSegment("verify-authentication")
                    .build()
                val request = Request.Builder()
                    .url(url)
                    .post(authenticationResponseJson.toRequestBody("application/json".toMediaType()))
                    .build()

                val response = okHttpClient.newCall(request).execute()
                if (response.code != 200) {
                    throw Exception("Authentication failed: ${response.body.string()}")
                }
            }

            _uiState.update {
                it.copy(
                    sendAuthenticationResponseToServerResult = result,
                    isSendingAuthenticationResponseToServer = false
                )
            }
        }
    }
}
