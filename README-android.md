# Passkeys workshop: The Android app

1. Ensure that you have [Android Studio Otter 3 or higher](https://developer.android.com/studio) installed.

2. You will need either an Android phone and cable to connect it to your computer, or you will need to set up an emulator. If you choose emulator, you will need:

    * A "Google APIs" binary

    * To sign into a Google account

    * To enable a proper keylock (not "swipe").

3. Open Android Studio and load the project. Sync it with Gradle. This might take a while as it downloads all the dependencies for the first time.

4. To allow it to build, you will need to complete TODO 1 in di/CredentialManagerModule. You will need to create a Jetpack Credential Manager instance. To do this, add the code `return CredentialManager.create(context)` to the **provideCredentialManager** function.

5. Build and run the code. It should now build successfully, but you won't be able to create or use passkeys just yet!

6. Now, let's get registration (sign *up*) working. Follow the instructions for TODO 2-4 in the ui/signup/SignUpViewModel.kt file. Once you've completed those, you should be able to create a passkey! Have a look in your Google Password store, and you should see the passkey there.

7. Lastly, let's get authentication (sign *in*) working. Follow the instructions for TODO 5-7 in the ui/signin/SignInViewModel.kt file. Once you've completed those, you should be able to sign in with your newly created passkey.

