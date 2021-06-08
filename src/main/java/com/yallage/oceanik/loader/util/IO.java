package com.yallage.oceanik.loader.util;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.apache.commons.lang.Validate.*;

/**
 * IO utils for Oceanik loader.
 *
 * @author Milkory
 */
public class IO {

    private static final String TEST_URL = "https://google.cn";

    /** Get an {@link InputStream} from a URL with a proxy. */
    public static BufferedInputStream newInputStream(String url, Proxy proxy) throws IOException {
        return new BufferedInputStream(new URL(url).openConnection(proxy).getInputStream());
    }

    /** Let an {@link OutputStream} read all bytes from an {@link InputStream}, then close and return it. */
    public static <T extends OutputStream> T readFully(InputStream in, T out) throws IOException {
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        out.close();
        return out;
    }

    /** Read contents from a URL. */
    public static String readFromURL(String url, Proxy proxy) throws IOException {
        return readFully(newInputStream(url, proxy), new ByteArrayOutputStream()).toString(StandardCharsets.UTF_8);
    }

    /** Download a file from a URL and save it to the target path. */
    public static void downloadFile(String url, File target, Proxy proxy) throws IOException {
        var in = newInputStream(url, proxy);
        readFully(in, new FileOutputStream(initFile(target)));
    }

    public static InputStreamReader getResourceReader(JavaPlugin plugin, String file) {
        return new InputStreamReader(Objects.requireNonNull(plugin.getResource(file)));
    }

    public static void checkProxy(Proxy proxy) throws IOException {
        var cont = new URL(TEST_URL).openConnection(proxy);
        cont.setConnectTimeout(1000);
        cont.getInputStream();
    }

    public static File initFile(File file) throws IOException {
        if (!file.exists()) {
            var parent = file.getParentFile();
            if (!(((parent.exists() && !parent.isFile()) || parent.mkdirs()) && file.createNewFile())) {
                throw new IOException("Failed to create file - " + file.getPath());
            }
        }
        return file;
    }

}
