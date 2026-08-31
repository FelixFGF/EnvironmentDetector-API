package de.felixfgf.environmentdetector;

public record EnvironmentInfo(
    String minecraftVersion,
    String loader,
    String loaderVersion,
    String launcher,
    String platform,
    String packName,
    String environment,
    String operatingSystem,
    boolean developmentEnvironment
) {}
