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
        section.set("permission" ,"lightegghunter.egg.villager");

        EggHunter.getInstance.eggs.saveConfig();

        return String.valueOf((size + 1));

    }

    public void setType(String type, String id) {
        ConfigurationSection section = data.createSection(masterKey + "." + id);
        section.set("type", type);

    }

    public void setAmount(int amount, String id) {
        ConfigurationSection section = data.createSection(masterKey + "." + id);
        section.set("amount", amount);

    }

    public void setChance(double chance, String id) {
        ConfigurationSection section = data.createSection(masterKey + "." + id);
        section.set("chance", chance);

    }

    public void setPerm(String perm, String id) {
        ConfigurationSection section = data.createSection(masterKey + "." + id);
        section.set("permission", perm);

    }
}
