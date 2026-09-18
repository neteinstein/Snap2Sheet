package org.neteinstein.snap2sheet.auth

/**
 * OAuth 2.0 client IDs from the Google Cloud Console project's "APIs & Services > Credentials"
 * page. Both must be replaced before Google Sign-In will work — see README setup notes.
 */
object GoogleAuthConfig {
    /**
     * The **Web application** client ID. Android's Credential Manager always authenticates
     * against this one (not an Android-type client ID) because it's what lets the backend verify
     * the returned ID token.
     */
    const val WEB_CLIENT_ID = "YOUR_WEB_CLIENT_ID.apps.googleusercontent.com"

    /** The **iOS** client ID, registered under the app's bundle identifier. */
    const val IOS_CLIENT_ID = "YOUR_IOS_CLIENT_ID.apps.googleusercontent.com"

    val isWebClientIdConfigured: Boolean get() = !WEB_CLIENT_ID.startsWith("YOUR_")
    val isIosClientIdConfigured: Boolean get() = !IOS_CLIENT_ID.startsWith("YOUR_")
}
