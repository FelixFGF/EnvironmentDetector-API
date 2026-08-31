package de.felixfgf.environmentdetector.detection;

import de.felixfgf.environmentdetector.Environmentdetector;
import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

public class LauncherDetector {
    public static String detectLauncher() {
        try {
            // 0. Manual Override via Config
            String manual = ConfigDetector.getManualLauncherName();
            if (!manual.isEmpty()) return manual;

            // 1. Scan JVM Arguments for Modrinth or other markers
            RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
            List<String> arguments = runtimeMxBean.getInputArguments();
            for (String arg : arguments) {
                if (arg.toLowerCase().contains("modrinth")) {
                    return "Modrinth App";
                }
            }

            Path gameDir = FabricLoader.getInstance().getGameDir();
            Path parentDir = gameDir.getParent();
            Path grandparentDir = parentDir != null ? parentDir.getParent() : null;

            // 2. Modrinth App - File check (Some versions still use index)
            if (Files.exists(gameDir.resolve("modrinth.index.json")) ||
                (parentDir != null && Files.exists(parentDir.resolve("modrinth.index.json"))) ||
                (grandparentDir != null && Files.exists(grandparentDir.resolve("modrinth.index.json")))) {
                return "Modrinth App";
            }
            
            // Classpath scan
            if (System.getProperty("java.class.path", "").toLowerCase().contains("modrinth")) {
                return "Modrinth App";
            }

            // Fallback for Modrinth via Mod IDs
            if (FabricLoader.getInstance().isModLoaded("theseus") || 
                FabricLoader.getInstance().isModLoaded("modrinth") ||
                FabricLoader.getInstance().isModLoaded("modrinth-app-helper")) {
                return "Modrinth App";
            }

            // 3. CurseForge
            if (System.getenv("CURSEFORGE_INSTANCE_NAME") != null || 
                Files.exists(gameDir.resolve("minecraftinstance.json")) ||
                Files.exists(gameDir.resolve(".curseclient")) ||
                (parentDir != null && Files.exists(parentDir.resolve(".curseclient")))) {
                return "CurseForge";
            }

            // 4. NoRisk Client
            if (Files.exists(gameDir.resolve("norisk.config")) ||
                (parentDir != null && Files.exists(parentDir.resolve("norisk.config"))) ||
                FabricLoader.getInstance().isModLoaded("norisk")) {
                return "NoRisk Client";
            }

            // 5. GDLauncher
            if (Files.exists(gameDir.resolve("gd-launcher.json")) ||
                Files.exists(gameDir.resolve("gdlauncher.json"))) {
                return "GDLauncher";
            }

            // 6. Feather Client
            if (FabricLoader.getInstance().isModLoaded("feather") || 
                FabricLoader.getInstance().isModLoaded("feather-core") ||
                Files.exists(gameDir.resolve(".feather"))) {
                return "Feather Client";
            }

            // 7. Prism/MultiMC
            if (Files.exists(gameDir.resolve("prism.config")) || 
                (parentDir != null && Files.exists(parentDir.resolve("prism.json")))) {
                return "Prism Launcher";
            }

            // 8. Lunar Client
            if (System.getProperty("java.class.path", "").toLowerCase().contains("lunar")) {
                return "Lunar Client";
            }

            // 9. Badlion Client
            if (System.getProperty("java.class.path", "").toLowerCase().contains("badlion")) {
                return "Badlion Client";
            }

            // 10. SKLauncher
            if (Files.exists(gameDir.resolve("sklauncher.json")) ||
                System.getProperty("java.class.path", "").toLowerCase().contains("sklauncher")) {
                return "SKLauncher";
            }

            // 11. ATLauncher
            if (Files.exists(gameDir.resolve("instance.json")) ||
                Files.exists(gameDir.resolve("atlauncher.json"))) {
                return "ATLauncher";
            }

            // 9. Official Launcher
            if (parentDir != null && Files.exists(parentDir.resolve("launcher_profiles.json"))) {
                return "Minecraft Launcher";
            }

        } catch (Exception e) {
            Environmentdetector.LOGGER.warn("Failed to detect launcher", e);
        }

        return "Unknown Launcher";
    }
}
