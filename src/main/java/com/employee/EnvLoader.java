package com.employee;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Environment Variable Loader
 * Loads environment variables from .env file for secure configuration
 */
public class EnvLoader {
    
    private static Map<String, String> envVars = new HashMap<>();
    private static boolean loaded = false;
    
    /**
     * Load environment variables from .env file
     */
    public static void loadEnv() {
        if (loaded) {
            return; // Already loaded
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                // Skip empty lines and comments
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                // Parse key=value pairs
                int equalIndex = line.indexOf('=');
                if (equalIndex > 0) {
                    String key = line.substring(0, equalIndex).trim();
                    String value = line.substring(equalIndex + 1).trim();
                    envVars.put(key, value);
                }
            }
            loaded = true;
            System.out.println("🔐 Environment variables loaded successfully from .env file");
            
        } catch (IOException e) {
            System.out.println("⚠️  .env file not found, using system environment variables");
            loaded = true; // Still mark as loaded to avoid repeated attempts
        }
    }
    
    /**
     * Get environment variable value
     * First checks .env file, then system environment variables
     * 
     * @param key Environment variable key
     * @return Environment variable value or null if not found
     */
    public static String getEnv(String key) {
        if (!loaded) {
            loadEnv();
        }
        
        // First check .env file
        String value = envVars.get(key);
        
        // If not found in .env, check system environment variables
        if (value == null) {
            value = System.getenv(key);
        }
        
        return value;
    }
    
    /**
     * Get environment variable with default value
     * 
     * @param key Environment variable key
     * @param defaultValue Default value if key not found
     * @return Environment variable value or default value
     */
    public static String getEnv(String key, String defaultValue) {
        String value = getEnv(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Check if environment variable exists
     * 
     * @param key Environment variable key
     * @return true if exists, false otherwise
     */
    public static boolean hasEnv(String key) {
        return getEnv(key) != null;
    }
}