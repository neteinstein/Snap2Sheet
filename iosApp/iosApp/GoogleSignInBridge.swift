import ComposeApp
import GoogleSignIn
import UIKit

/// Implements Kotlin's `IosGoogleSignInBridge` (composeApp/src/iosMain/.../GoogleSignIn.ios.kt)
/// using the native GoogleSignIn-iOS SDK, which needs a presenting `UIViewController` that only
/// this side of the KMP boundary has access to.
final class GoogleSignInBridge: NSObject, IosGoogleSignInBridge {
    static let shared = GoogleSignInBridge()

    private override init() {
        super.init()
    }

    /// Call once at app launch, before any sign-in attempt.
    func configure() {
        guard GoogleAuthConfig.shared.isIosClientIdConfigured else { return }
        GIDSignIn.sharedInstance.configuration = GIDConfiguration(clientID: GoogleAuthConfig.shared.IOS_CLIENT_ID)
    }

    func signIn(onResult: @escaping (GoogleAccount?, String?) -> Void) {
        guard GoogleAuthConfig.shared.isIosClientIdConfigured else {
            onResult(nil, "Google Sign-In isn't configured yet. Set GoogleAuthConfig.IOS_CLIENT_ID.")
            return
        }
        guard let presenter = Self.topViewController() else {
            onResult(nil, "No screen available to present Google Sign-In.")
            return
        }

        GIDSignIn.sharedInstance.signIn(withPresenting: presenter) { result, error in
            if let error {
                onResult(nil, (error as NSError).localizedDescription)
                return
            }
            guard let profile = result?.user.profile else {
                onResult(nil, "Google Sign-In didn't return a profile.")
                return
            }
            let account = GoogleAccount(
                email: profile.email,
                initials: GoogleAccount.companion.initialsFrom(displayName: profile.name, email: profile.email)
            )
            onResult(account, nil)
        }
    }

    private static func topViewController(base: UIViewController? = rootViewController()) -> UIViewController? {
        if let nav = base as? UINavigationController {
            return topViewController(base: nav.visibleViewController)
        }
        if let tab = base as? UITabBarController {
            return topViewController(base: tab.selectedViewController)
        }
        if let presented = base?.presentedViewController {
            return topViewController(base: presented)
        }
        return base
    }

    private static func rootViewController() -> UIViewController? {
        UIApplication.shared.connectedScenes
            .compactMap { ($0 as? UIWindowScene)?.keyWindow }
            .first?.rootViewController
    }
}
