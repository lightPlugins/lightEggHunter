package de.lightplugins.egghunter.util;

import de.lightplugins.egghunter.EggHunter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Set;

public class EggConfigManager {

    private final FileConfiguration data;
    private final String masterKey = "eggs";

    EggConfigManager() {
        this.data = EggHunter.getInstance.eggs.getConfig();
    }

    public String setDefaultEntries() {

        Set<String> entries = data.getConfigurationSection("eggs").getKeys(false);
        int size = entries.size();
        ConfigurationSection section = data.createSection(masterKey + ".'" + (size + 1) + "'");

        section.set("type", "villager");
        section.set("amount", 1);
        section.set("chance", 0.20);
        section.set("inInventory", false);

        EggHunter.getInstance.eggs.saveConfig();

        return String.valueOf((size + 1));

    }

    public void setType(String type, String id) {
        ConfigurationSection section = data.createSection(masterKey + "." + id);
        section.set("type", type);
        EggHunter.getInstance.eggs.saveConfig();

    }

    public void setAmount(int amount, String id) {
        ConfigurationSection section = data.createSection(masterKey + "." + id);
        section.set("amount", amount);
        EggHunter.getInstance.eggs.saveConfig();

    }

    public void setChance(double chance, String id) {
        ConfigurationSection section = data.createSection(masterKey + "." + id);
        section.set("chance", chance);
        EggHunter.getInstance.eggs.saveConfig();

    }

    public void setInventory(boolean inInventory, String id) {
        ConfigurationSection section = data.createSection(masterKey + "." + id);
        section.set("inInventory", inInventory);
        EggHunter.getInstance.eggs.saveConfig();

    }

    public boolean getSetInventory(String key) {
        return data.getBoolean(masterKey + "." + key + ".inInventory");
    }
    public double getChance(String key) {
        return data.getDouble(masterKey + "." + key + ".chance");
    }
    public int getAmount(String key) {
        return data.getInt(masterKey + "." + key + ".amount");
    }
    public String getType(String key) {
        return data.getString(masterKey + "." + key + ".type");
    }
}
