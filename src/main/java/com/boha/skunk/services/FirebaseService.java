package com.boha.skunk.services;

import com.boha.skunk.util.E;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Initializes Firebase
 */
@RequiredArgsConstructor
@Component
public class FirebaseService {

    private static final Logger logger = Logger.getLogger(FirebaseService.class.getSimpleName());

    @Value("${storageBucket}")
    private String storageBucket;
    private FirebaseApp app;
    @Value("${projectId}")
    private String projectId;

    @PostConstruct
    public void init() {
        logger.info(E.ALIEN + E.ALIEN + E.ALIEN + E.ALIEN
                + " FirebaseService: PostConstruct - \uD83C\uDF4E \uD83C\uDF4E" +
                " initializeFirebase ...... ");
        initializeFirebase();

        logger.info(E.ALIEN + E.ALIEN + E.ALIEN + E.ALIEN
                + " FirebaseService initialized! \uD83E\uDD66\uD83E\uDD66");

    }
    public void initializeFirebase() {
        logger.info(E.AMP+E.AMP+E.AMP+ "FirebaseService: .... initializing Firebase ....");
        FirebaseOptions options;
        logger.info(E.AMP+E.AMP+E.AMP+
                " Project Id from Properties: "+E.RED_APPLE + " " + projectId);
        try {
            options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .setDatabaseUrl("https://" + projectId + ".firebaseio.com/")
                    .setStorageBucket(storageBucket)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Firebase initialization failed!  " + e.getMessage());
        }

        app = FirebaseApp.initializeApp(options);
        logger.info(E.AMP+E.AMP+E.AMP+E.AMP+E.AMP+
                " Firebase has been initialized: "
                + app.getOptions().getDatabaseUrl()
                + " " + E.RED_APPLE);
    }

}
