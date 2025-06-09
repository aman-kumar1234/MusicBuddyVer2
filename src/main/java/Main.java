package com.musicapp;

import com.musicapp.database.DatabaseSetup;
import com.musicapp.utils.NavigationManager;
import com.musicapp.services.MusicPlayerService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void init() throws Exception {
        try {
            // Initialize database
            DatabaseSetup.initializeDatabase();
            logger.info("Database initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize database", e);
            throw e;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            logger.error("Uncaught exception in thread: " + thread.getName(), throwable);
            Platform.runLater(() -> showErrorAndExit(throwable));
        });

        try {
            // Configure primary stage
            primaryStage.setTitle("MusicBuddy");
            
            // Set up the navigation manager
            NavigationManager navigationManager = NavigationManager.getInstance();
            navigationManager.setMainStage(primaryStage);
            
            // Navigate to login view
            navigationManager.navigateToLogin();
            
        } catch (Exception e) {
            logger.error("Error starting application", e);
            showErrorAndExit(e);
        }
    }

    private void showErrorAndExit(Throwable e) {
        logger.error("Fatal error occurred", e);
        Platform.exit();
        System.exit(1);
    }

    @Override
    public void stop() {
        try {
            // Cleanup resources if needed
            logger.info("Application shutting down");
        } catch (Exception e) {
            logger.error("Error during application shutdown", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}