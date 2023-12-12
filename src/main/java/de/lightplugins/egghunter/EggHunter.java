package de.lightplugins.egghunter;

import de.lightplugins.egghunter.util.ColorTranslation;
import de.lightplugins.egghunter.util.FileManager;
import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;

import java.io.File;

public class EggHunter extends JavaPlugin {

    public static final String consolePrefix = "§r[light§cEggs§r] ";
    public final InventoryManager inventoryManager = new InventoryManager(this);

    public static EggHunter getInstance;
    public static ColorTranslation colorTranslation;

    public FileManager eggs;
    public FileManager mainMenu;
    public FileManager editMenu;



    public void onLoad() {

        getInstance = this;

        eggs = new FileManager(this, "eggs", "data.yml");
        mainMenu = new FileManager(this, "inventories", "mainMenu.yml");
        editMenu = new FileManager(this, "inventories" , "editMenu.yml");

        colorTranslation = new ColorTranslation();

    }

    public void onEnable() {


        inventoryManager.invoke();

    }

    public void onDisable() {

    }


}