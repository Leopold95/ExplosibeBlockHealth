package me.melvuze.explosiveblockhealth.listeners;

import com.jeff_media.customblockdata.CustomBlockData;
import me.melvuze.explosiveblockhealth.ExplosiveBlockHealth;
import me.melvuze.explosiveblockhealth.core.Config;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataType;

public class ExplodeListener implements Listener {
    private ExplosiveBlockHealth plugin;

    public ExplodeListener(ExplosiveBlockHealth plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onTntExplodes(EntityExplodeEvent event) {
        Location location = event.getEntity().getLocation();

        int radius = Config.getInt("check-radius");

        int startX = location.getBlockX() - radius;
        int startY = location.getBlockY() - radius;
        int startZ = location.getBlockZ() - radius;

        int endX = location.getBlockX() + radius;
        int endY = location.getBlockY() + radius;
        int endZ = location.getBlockZ() + radius;

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                for (int z = startZ; z <= endZ; z++) {
                    Block block = location.getWorld().getBlockAt(x, y, z);

                    //plugin.getLogger().warning("no block data 1");

                    if(!plugin.getExplosiveBlocks().containsKey(block.getType()))
                        continue;

                    //plugin.getLogger().warning("no block data 2");

                    CustomBlockData data = new CustomBlockData(block, plugin);
                    if (data.has(plugin.getHealthKey())) {
                        int health = data.get(plugin.getHealthKey(), PersistentDataType.INTEGER);

                        plugin.getLogger().warning(String.valueOf(health));

                        if (health == 1){
                            block.breakNaturally();
                        }

                        health--;

                        data.set(plugin.getHealthKey(), PersistentDataType.INTEGER, health);
                    }
                    else {
                        //plugin.getLogger().warning("no block data");
                        int health = plugin.getExplosiveBlocks().get(block.getType());
                        data.set(plugin.getHealthKey(), PersistentDataType.INTEGER, health);
                    }
                }
            }
        }
    }
}

