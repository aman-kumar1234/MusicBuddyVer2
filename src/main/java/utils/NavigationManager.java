package com.musicapp.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URL;

public class NavigationManager {
    private static final Logger logger = LoggerFactory.getLogger(NavigationManager.class);
    private static NavigationManager instance;
    private Stage mainStage;
    private FXMLLoader currentLoader;
    
    private NavigationManager() {}
    
    public static synchronized NavigationManager getInstance() {
        if (instance == null) {
            instance = new NavigationManager();
        }
        return instance;
    }
    
    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }
    
    public void navigateToHome() {
        loadView("/fxml/MainView.fxml", "MusicBuddy");
    }
    
    public void navigateToLogin() {
        loadView("/fxml/Login.fxml", "MusicBuddy - Login");
    }
    
    public void navigateToRegister() {
        loadView("/fxml/Register.fxml", "MusicBuddy - Register");
    }
    
    public FXMLLoader navigateToEmailVerification() {
        return loadView("/fxml/EmailVerificationView.fxml", "MusicBuddy - Email Verification");
    }
    
    private FXMLLoader loadView(String fxmlPath, String title) {
        try {
            // Get the resource URL
            URL resource = getClass().getResource(fxmlPath);
            if (resource == null) {
                throw new IOException("Cannot find resource: " + fxmlPath);
            }
            
            // Create and configure the loader
            currentLoader = new FXMLLoader();
            currentLoader.setLocation(resource);
            
            // Load the FXML
            Parent root = currentLoader.load();
            if (root == null) {
                throw new IOException("Failed to load root node from FXML");
            }
            
            // Create and configure the scene
            Scene scene = new Scene(root);
            URL cssResource = getClass().getResource("/styles/styles.css");
            if (cssResource != null) {
                scene.getStylesheets().add(cssResource.toExternalForm());
            } else {
                logger.warn("Could not load CSS file: /styles/styles.css");
            }
            
            // Configure the stage
            if (mainStage == null) {
                throw new IllegalStateException("Main stage has not been set");
            }
            
            // Set window size based on view
            if (fxmlPath.contains("MainView")) {
                mainStage.setMinWidth(1000);
                mainStage.setMinHeight(700);
            } else {
                mainStage.setMinWidth(400);
                mainStage.setMinHeight(600);
            }
            
            mainStage.setTitle(title);
            mainStage.setScene(scene);
            mainStage.show();
            
            return currentLoader;
        } catch (Exception e) {
            logger.error("Error loading view: " + fxmlPath, e);
            throw new RuntimeException("Failed to load view: " + fxmlPath, e);
        }
    }
    
    public <T> T getCurrentController() {
        return currentLoader != null ? currentLoader.getController() : null;
    }
} 