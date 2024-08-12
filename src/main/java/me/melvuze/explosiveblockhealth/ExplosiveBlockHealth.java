package me.melvuze.explosiveblockhealth;

import com.jeff_media.customblockdata.CustomBlockData;
import lombok.Getter;
import me.melvuze.explosiveblockhealth.core.Config;
import me.melvuze.explosiveblockhealth.listeners.ExplodeListener;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import javax.naming.Name;
import java.util.HashMap;
import java.util.Map;

public final class ExplosiveBlockHealth extends JavaPlugin {
    @Getter
    private Map<Material, Integer>  explosiveBlocks;

    @Getter
    private NamespacedKey healthKey = new NamespacedKey(this, "BLOCK_HEALTH_KEY");

    @Override
    public void onEnable() {
        Config.register(this);
        getServer().getPluginManager().registerEvents(new ExplodeListener(this), this);
        explosiveBlocks = new HashMap<>();
        loadExplosiveBlocks();
    }

    private void loadExplosiveBlocks(){
        ConfigurationSection section = Config.getSection("list");
        if(section == null || section.getKeys(false).isEmpty()){
            getLogger().warning(Config.getMessage("no-blocks-list-sec"));
            return;
        }

        for(String materialName: section.getKeys(true)){
            Material mat = Material.getMaterial(materialName);

            if(mat == null){
                String message = Config.getMessage("no-mat-blocks-list-sec")
                        .replace("{mat}", materialName);
                getLogger().warning(message);
                continue;
            }

            int health = section.getInt(materialName);
            explosiveBlocks.put(mat, health);
        }
    }
}
