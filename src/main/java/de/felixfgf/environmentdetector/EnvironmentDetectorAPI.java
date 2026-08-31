package de.felixfgf.environmentdetector;

import de.felixfgf.environmentdetector.detection.*;
import de.felixfgf.environmentdetector.placeholder.PlaceholderManager;

public class EnvironmentDetectorAPI {
    private static volatile EnvironmentInfo cachedInfo;

    public static EnvironmentInfo getEnvironment() {
        if (cachedInfo == null) {
            synchronized (EnvironmentDetectorAPI.class) {
                if (cachedInfo == null) {
                    try {
                        cachedInfo = new EnvironmentInfo(
                            RuntimeEnvironmentDetector.getMinecraftVersion(),
                            LoaderDetector.getLoaderName(),
                            LoaderDetector.getLoaderVersion(),
                            LauncherDetector.detectLauncher(),
                            PlatformDetector.detectPlatform(),
                            PackDetector.detectPackName(),
                            RuntimeEnvironmentDetector.getEnvironment(),
                            RuntimeEnvironmentDetector.getOS(),
                            RuntimeEnvironmentDetector.isDevelopment()
                        );
                    } catch (Exception e) {
                        Environmentdetector.LOGGER.error("Critical error during environment detection!", e);
                        // Emergency Fallback
                        cachedInfo = new EnvironmentInfo(
                            "Unknown Version", "Unknown Loader", "Unknown Version",
                            "Vanilla / Unknown", "Unknown Platform", "Unknown Pack",
                            "unknown", "Unknown OS", false
                        );
                    }
                }
            }
        }
        return cachedInfo;
    }

    public static String resolvePlaceholders(String text) {
        return PlaceholderManager.resolve(text);
    }
}
