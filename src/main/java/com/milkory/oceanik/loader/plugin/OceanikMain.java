package com.milkory.oceanik.loader.plugin;

import com.milkory.oceanik.loader.OceanikLoader;
import com.milkory.oceanik.plugin.OPluginManager;
import com.milkory.oceanik.plugin.OceanikPlugin;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The plugin main class.
 *
 * @author Milkory
 */
public class OceanikMain extends JavaPlugin {

    @Getter private OceanikPlugin oceanikPlugin = null;

    @SneakyThrows @Override public void onLoad() {
        var result = OceanikLoader.getInstance(this).loadOceanik();
        if (result >= 0) {
            this.oceanikPlugin = OPluginManager.getInstance().loadPlugin(this);
        } else throw new RuntimeException("Oceanik not loaded.");
    }

}
