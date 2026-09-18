import ComposeApp
import GoogleSignIn
import SwiftUI

@main
struct iOSApp: App {
    init() {
        InitKoinKt.doInitKoin()
        GoogleSignInBridge.shared.configure()
        IosGoogleSignInProvider.shared.bridge = GoogleSignInBridge.shared
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    GIDSignIn.sharedInstance.handle(url)
                }
        }
    }
}
