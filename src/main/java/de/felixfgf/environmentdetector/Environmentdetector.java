package de.felixfgf.environmentdetector;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Environmentdetector implements ModInitializer {
    public static final String MOD_ID = "environmentdetector";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // Pre-cache environment info (this runs all detection logic)
        EnvironmentInfo info = EnvironmentDetectorAPI.getEnvironment();
        
        // Build the formatted info string
        if (info.launcher().equals("Unknown Launcher") && info.packName().equals("Unknown Pack")) {
            LOGGER.info("[EnvironmentDetector] Unknown Environment");
        } else {
            // Check for NoRisk specifically as requested in the example
            boolean isNoRisk = info.launcher().equals("NoRisk Client");
            
            String logOutput = String.format("Launcher=%s, Instance=%s, NoRisk=%s, Loader=%s, Version=%s", 
                info.launcher(), 
                info.packName(), 
                isNoRisk,
                info.loader(),
                info.minecraftVersion()
            );
            
            LOGGER.info("[EnvironmentDetector] {}", logOutput);
        }
    }
}
