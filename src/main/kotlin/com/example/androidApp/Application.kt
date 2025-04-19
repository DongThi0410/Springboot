package com.example.androidApp

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.stereotype.Component
import java.io.FileInputStream

@Component
class FirebaseInitializer {
    init {
        if (FirebaseApp.getApps().isEmpty()){
            val serviceAcc = FileInputStream("src/main/resources/serviceAccountKey.json")
            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAcc))
                .build()
            FirebaseApp.initializeApp(options)
        }
    }
}