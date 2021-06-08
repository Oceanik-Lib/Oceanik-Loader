package com.yallage.oceanik.loader;

import com.google.common.base.Strings;
import com.yallage.oceanik.loader.util.IO;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.logging.Logger;

/**
 * The Oceanik loader.
 *
 * @author Milkory
 */
public class OceanikLoader {

    @Nullable private static OceanikLoader instance = null;

    /** Get an instance of Oceanik loader. */
    @NotNull public static OceanikLoader getInstance(JavaPlugin booter) {
        if (instance != null) return instance;
        else return new OceanikLoader(booter);
    }

    /**
     * The status of Oceanik. <br>
     * -1 = Load Failed, 0 = Not Loaded, 1 = Loaded
     */
    @Getter private int status = 0;

    /** The booter of Oceanik. */
    @Getter private final JavaPlugin booter;

    private final Logger logger = Logger.getLogger("Oceanik"); // TODO

    private OceanikLoader(JavaPlugin booter) {
        instance = this;
        this.booter = booter;
    }

    /** Check if the Oceanik is loaded. */
    public boolean isLoaded() {
        return status == 1;
    }

    /** Check if the Oceanik has failed to load before. */
    public boolean isFailed() {
        return status == 2;
    }

    /**
     * Method to try loading Oceanik, returning the load result. <br>
     * -2 = Failed Before, -1 = Failed, 0 = Already Loaded, 1 = Succeed
     */
    public int loadOceanik() {
        if (isLoaded()) return 0;
        if (isFailed()) return -2;
        return forceLoadOceanik();
    }

    /** Force to load Oceanik. Mostly same as {@link #loadOceanik()} except ignoring failures and whether is already loaded. */
    public int forceLoadOceanik() {
        var config = YamlConfiguration
                .loadConfiguration(IO.getResourceReader(booter, "oceanik-loader.yml"));
        var url = config.getString("target");
        Proxy proxy = getProxyFromConfig(config.getConfigurationSection("proxy"));
        try { // TODO: only for testing.
            System.out.println(IO.readFromURL(url, proxy));
            IO.downloadFile(url, new File(booter.getDataFolder(), "test.yml"), proxy);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    /** Get the proxy settings from 'proxy' {@link ConfigurationSection}. */
    private Proxy getProxyFromConfig(ConfigurationSection config) {
        if (config != null) {
            var type = config.getString("type");
            if (!Strings.isNullOrEmpty(type)) { // enabled if type is found.
                var ip = config.getString("ip");
                if (!(Strings.isNullOrEmpty(ip))) { // enabled if ip is found.
                    var port = config.getInt("port");
                    logger.info("Found proxy settings, testing it.");
                    Proxy proxy;
                    try { // check if the proxy is valid.
                        IO.checkProxy((proxy = new Proxy(Proxy.Type.valueOf(type.toUpperCase()), new InetSocketAddress(ip, port))));
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.warning("Proxy seems to be invalid, ending up using no proxy.");
                        return Proxy.NO_PROXY;
                    }
                    logger.info(String.format("Proxy using: %s %s:%s", type.toLowerCase(), ip, port));
                    return proxy;
                }
            }
        }
        return Proxy.NO_PROXY;
    }

}
