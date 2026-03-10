package com.tdcolvin.passkeyauthdemo.ui.signup

import android.app.Activity
import androidx.credentials.CreatePublicKeyCredentialRequest
import androidx.credentials.CreatePublicKeyCredentialResponse
import androidx.credentials.CredentialManager
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

data class SignUpUiState(
    val getRegistrationOptionsJsonResult: Result<String>? = null,
    val isGettingRegistrationOptionsJson: Boolean = false,

    val createPasskeyResult: Result<String>? = null,
    val isCreatingPasskey: Boolean = false,

    val sendRegistrationResponseToServerResult: Result<Unit>? = null,
    val isSendingRegistrationResponseToServer: Boolean = false
)

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val credentialManager: CredentialManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState get() = _uiState.asStateFlow()

    fun getPasskeyRegistrationOptionsJson(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isGettingRegistrationOptionsJson = true) }

            val result = runCatching {

                /* TODO 2
                   Use okHttpClient (or your favourite HTTP library) to GET the authentication options at
                   https://auth.tomcolvin.co.uk/generate-authentication-options?username=$username

                   Return the response body.
                 */
                throw Exception("Remove this line!")
            }

            _uiState.update { it.copy(
                getRegistrationOptionsJsonResult = result,
                isGettingRegistrationOptionsJson = false
            ) }
        }
    }

    fun createPasskeyFromRegistrationOptions(activity: Activity) {
        _uiState.update { it.copy(isCreatingPasskey = true) }

        viewModelScope.launch(Dispatchers.IO) {
            val registrationResponse = runCatching {
                val registerRequestJson = _uiState.value.getRegistrationOptionsJsonResult?.getOrNull()
                    ?: throw Exception("No registration options available")

                /* TODO 3: Build the passkey using Jetpack Credential Manager
                   Hint:
                    1. Build a CreatePublicKeyCredentialRequest with the JSON above. Use
                       preferImmediatelyAvailableCredentials = false to force it to actually
                       create a new passkey.
                    2. Use Credential Manager to create the credential
                    3. Return the registrationResponseJson it produces
                 */
                throw Exception("Remove this line!")
            }

            _uiState.update { it.copy(
                createPasskeyResult = registrationResponse,
                isCreatingPasskey = false
            ) }
        }
    }

    fun sendRegistrationResponseToServer() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isSendingRegistrationResponseToServer = true) }

            val result = runCatching {
                val registrationResponseJson = _uiState.value.createPasskeyResult?.getOrNull()
                    ?: throw Exception("No registration response available")

                /* TODO 4
                   POST the registration request to
                   https://auth.tomcolvin.co.uk/verify-registration

                   Return the response body. Throw an exception if the response code != 200
                 */

                throw Exception("Remove this line!")
            }

            _uiState.update { it.copy(
                sendRegistrationResponseToServerResult = result,
                isSendingRegistrationResponseToServer = false
            ) }
        }
    }
}