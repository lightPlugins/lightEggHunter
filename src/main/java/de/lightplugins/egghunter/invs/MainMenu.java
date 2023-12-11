package de.lightplugins.egghunter.invs;

import de.lightplugins.egghunter.EggHunter;
import de.lightplugins.egghunter.util.ItemBuilder;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.TimeSetting;
import io.github.rysefoxx.inventory.plugin.pagination.Pagination;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainMenu implements InventoryProvider {

    public void paginationInventory(Player player, FileConfiguration jobConfig) {


        RyseInventory.builder()
                .title("lightEggHunter | Menu")
                .rows(6)
                .period(1, TimeSetting.SECONDS)
                .provider(new InventoryProvider() {

                    @Override
                    public void update(Player player, InventoryContents contents) {



                    }

                    @Override
                    public void init(Player player, InventoryContents contents) {

                        FileConfiguration mainMenu = EggHunter.getInstance.mainMenu.getConfig();
                        FileConfiguration eggs = EggHunter.getInstance.eggs.getConfig();

                        ItemStack fillItem = new ItemStack(Material.valueOf(mainMenu.getString("menu.fillMaterial")));
                        ItemMeta fillMeta = fillItem.getItemMeta();

                        if(fillMeta == null) {
                            return;
                        }

                        fillMeta.setDisplayName(" ");
                        fillItem.setItemMeta(fillMeta);

                        contents.fill(fillItem);

                        Pagination pagination = contents.pagination();
                        pagination.setItemsPerPage(7);
                        pagination.iterator(SlotIterator
                                .builder()
                                .override()
                                .startPosition(2, 1)
                                .type(SlotIterator.SlotIteratorType.HORIZONTAL)
                                .blackList(Arrays.asList(26, 27))
                                .build());

                        String pageForward = EggHunter.colorTranslation.hexTranslation(
                                mainMenu.getString("menu.nextPageName"));
                        String pageBack = EggHunter.colorTranslation.hexTranslation(
                                mainMenu.getString("menu.previousPageName"));

                        /**
                         *      Seite zurück
                         */


                        int previousPage = pagination.page() - 1;
                        contents.set(5, 2, IntelligentItem.of(new ItemBuilder(Material.ARROW).
                                amount(pagination.isFirst()
                                        ? 1
                                        : pagination.page() - 1)
                                .displayName(pageBack).build(), event -> {
                            if (pagination.isFirst()) {
                                return;
                            }

                            RyseInventory currentInventory = pagination.inventory();
                            currentInventory.open(player, pagination.previous().page());
                        }));

                        /**
                         *      Seite vorwärts
                         */

                        int page = pagination.page() + 1;
                        contents.set(5, 6, IntelligentItem.of(new ItemBuilder(Material.ARROW)
                                .amount((pagination.isLast() ? 1 : page))
                                .displayName(pageForward).build(), event -> {
                            if (pagination.isLast()) {
                                return;
                            }

                            RyseInventory currentInventory = pagination.inventory();
                            currentInventory.open(player, pagination.next().page());
                        }));


                        for(String id : Objects.requireNonNull(eggs.getConfigurationSection("eggs")).getKeys(false)) {

                            ItemStack eggItem = new ItemStack( Material.valueOf(
                                    eggs.getString("eggs." + id + ".type").toUpperCase() + "_SPAWN_EGG"));
                            ItemMeta eggMeta = eggItem.getItemMeta();

                            if(eggMeta == null) {
                                return;
                            }

                            String displayName = Objects.requireNonNull(mainMenu.getString("menu.existingEggs.displayname"))
                                            .replace("#eggname#", eggMeta.getLocalizedName());

                            eggMeta.setDisplayName(EggHunter.colorTranslation.hexTranslation(displayName));

                            List<String> lore = new ArrayList<>();

                            for(String singleLine : mainMenu.getStringList("menu.existingEggs.lore")) {

                                lore.add(EggHunter.colorTranslation.hexTranslation(singleLine
                                        .replace("#eggname#" ,
                                                eggMeta.getLocalizedName())
                                        .replace("#dropchance#",
                                                String.valueOf(eggs.getDouble("eggs." + id + ".chance")))));

                            }

                            eggMeta.setLore(lore);

                            eggItem.setItemMeta(eggMeta);



                        }
                    }

                }).build(EggHunter.getInstance).open(player);

    }
}
