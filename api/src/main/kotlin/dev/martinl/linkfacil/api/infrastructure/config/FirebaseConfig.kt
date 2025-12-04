package dev.martinl.linkfacil.api.infrastructure.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream
import java.io.IOException


@Configuration
class FirebaseConfig {
    @PostConstruct
    @Throws(IOException::class)
    fun initialize() {

        val keyPath = System.getenv("FIREBASE_KEY_PATH") ?: "/run/secrets/firebase-admin.json"
        val stream = FileInputStream(keyPath)
//                        ClassPathResource("firebase-service-account.json").getInputStream()

        if (FirebaseApp.getApps().isEmpty()) {
            val options: FirebaseOptions? = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(stream))
                .build()

            FirebaseApp.initializeApp(options)
        }
    }
}