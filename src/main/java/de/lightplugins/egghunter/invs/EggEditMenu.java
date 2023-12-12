package de.lightplugins.egghunter.invs;

import de.lightplugins.egghunter.EggHunter;
import de.lightplugins.egghunter.util.ItemBuilder;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.TimeSetting;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EggEditMenu implements InventoryProvider {

    public void editMenu(Player player, String eggKey) {


        RyseInventory.builder()
                .title("TEST")
                .rows(3)
                .period(1, TimeSetting.SECONDS)
                .provider(new InventoryProvider() {
                    @Override
                    public void update(Player player, InventoryContents contents) {


                    }

                    @Override
                    public void init(Player player, InventoryContents contents) {

                        FileConfiguration eggs = EggHunter.getInstance.eggs.getConfig();
                        FileConfiguration editMenu = EggHunter.getInstance.editMenu.getConfig();

                        ItemStack fillItem = new ItemStack(Material.valueOf(editMenu.getString("menu.fillMaterial")));
                        ItemMeta fillMeta = fillItem.getItemMeta();

                        if(fillMeta == null) {
                            return;
                        }

                        fillMeta.setDisplayName(" ");
                        fillItem.setItemMeta(fillMeta);

                        contents.fill(fillItem);

                        String eggType = eggs.getString("eggs." + eggKey + ".type");
                        double chance = eggs.getDouble("eggs." + eggKey + ".chance");
                        boolean inInv = eggs.getBoolean("eggs." + eggKey + ".inInventory");
                        int amount = eggs.getInt("eggs." + eggKey + ".amount");
                        List<String> blacklist = eggs.getStringList("eggs." + eggKey + ".blacklistWorlds");

                        for(String settingPath : editMenu.getConfigurationSection("menu").getKeys(false)) {

                            contents.set(10, IntelligentItem.of(buildItemByID(
                                    editMenu, settingPath, chance, inInv, amount, eggType, blacklist
                            ), event -> {


                                if(settingPath.equalsIgnoreCase("setChanceButton")) {

                                    // TODO add setChance Action here!

                                }
                            }));
                        }
                    }
                }).build(EggHunter.getInstance).open(player);


    }

    private ItemStack buildItemByID(
            FileConfiguration config, String key, double chance, boolean inInv,
            int amount, String eggType, List<String> blacklistWorld) {

        Material material = Material.valueOf(
                config.getString("menu." + key + ".material"));

        ItemStack is = new ItemStack(material);
        ItemMeta ism = is.getItemMeta();

        if(ism == null) {
            return new ItemStack(Material.STONE);
        }

        String displayname = EggHunter.colorTranslation.hexTranslation(
                config.getString("menu." + key + ".displayname"));

        ism.setDisplayName(EggHunter.colorTranslation.hexTranslation(displayname));

        List<String> lore = new ArrayList<>();

        for(String singleLine : config.getStringList("menu." + key + ".lore")) {

            if(singleLine.contains("#blacklistedworlds#")) {

                if(blacklistWorld.isEmpty()) {
                    lore.add(EggHunter.colorTranslation.hexTranslation(
                            config.getString(singleLine
                                    .replace("#blacklistedworlds#", "none"))
                    ));
                }

                blacklistWorld.forEach(singleWorld -> {

                    lore.add(EggHunter.colorTranslation.hexTranslation(
                            config.getString(singleLine
                                    .replace("#blacklistedworlds#", singleWorld))
                    ));
                });

                continue;
            }

            lore.add(EggHunter.colorTranslation.hexTranslation(singleLine
                    .replace("#eggtype#", eggType)
                    .replace("#amount#", String.valueOf(amount))
                    .replace("#chance#", String.valueOf(chance))));

        }

        ism.setLore(lore);
        is.setItemMeta(ism);

        return is;

    }
}
