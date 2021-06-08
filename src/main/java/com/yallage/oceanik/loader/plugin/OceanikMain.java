package com.yallage.oceanik.loader.plugin;

import com.yallage.oceanik.loader.OceanikLoader;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The plugin main class.
 *
 * @author Milkory
 */
@SuppressWarnings("unused")
public class OceanikMain extends JavaPlugin {

    @Override public void onLoad() {
        OceanikLoader.getInstance(this).loadOceanik();
    }

}
