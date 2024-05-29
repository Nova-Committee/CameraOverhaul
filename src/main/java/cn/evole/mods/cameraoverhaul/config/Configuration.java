package cn.evole.mods.cameraoverhaul.config;

import cn.evole.mods.cameraoverhaul.Const;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Configuration {
    private static final Path configPath = Const.configPath;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static <T extends BaseConfigData> T LoadConfig(Class<T> tClass, String configName, int configVersion) {
        T configData = null;
        Path configFile = configPath.resolve(configName + ".json");
        boolean saveConfig = false;

        try {
            Files.createDirectories(configPath);

            if (Files.exists(configFile)) {
                BufferedReader fileReader = Files.newBufferedReader(configFile);
                configData = gson.fromJson(fileReader, tClass);
                fileReader.close();

                //Save the config on first runs of new versions.
                if (configData.configVersion < configVersion) {
                    saveConfig = true;
                }
            } else {
                configData = tClass.getDeclaredConstructor().newInstance();
                saveConfig = true;
            }
        } catch (Exception e) {
            Const.LOGGER.error("Error when initializing config", e);
        }

        if (saveConfig) {
            SaveConfig(configData, configName, configVersion);
        }

        return configData;
    }

    public static <T extends BaseConfigData> void SaveConfig(T configData, String configName, int configVersion) {
        Path configFile = configPath.resolve(configName + ".json");

        configData.configVersion = configVersion;

        try (BufferedWriter writer = Files.newBufferedWriter(configFile)) {
            writer.write(gson.toJson(configData));
        } catch (IOException e) {
            Const.LOGGER.error("Couldn't save config file", e);
        }
    }
}