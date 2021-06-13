package com.yallage.oceanik.loader.util;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * IO utilities for Oceanik loader.
 *
 * @author Milkory
 */
public class IO {

    /** The URL used to test the Internet or proxy status. */
    private static final String TEST_URL = "https://google.cn";

    /** Get an {@link InputStream} from a URL with a proxy. */
    public static BufferedInputStream getURLInput(String url, Proxy proxy) throws IOException {
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
        return readFully(getURLInput(url, proxy), new ByteArrayOutputStream()).toString(StandardCharsets.UTF_8);
    }

    /** Download a file from a URL and save it to the target path. */
    public static File downloadFile(String url, File target, Proxy proxy) throws IOException {
        var in = getURLInput(url, proxy);
        readFully(in, new FileOutputStream(initFile(target)));
        return target;
    }

    @SuppressWarnings("UnstableApiUsage") // I guess they are stable.
    public static File safeDownloadFile(String url, File target, Proxy proxy, String sha256) throws IOException {
        var tempFile = IO.downloadFile(url, File.createTempFile("Oceanik", null), proxy);
        var hash = Files.hash(tempFile, Hashing.sha256());
        tempFile.deleteOnExit();
        if (hash.toString().equals(sha256)) {
            Files.move(tempFile, IO.initFile(target));
            return target;
        } else throw new IllegalArgumentException("File integrity verification failed - " + tempFile.getPath());
    }

    public static InputStreamReader getResourceReader(Plugin plugin, String file) {
        return new InputStreamReader(Objects.requireNonNull(plugin.getResource(file)));
    }

    /** Throw a {@link IOException} when the proxy is invalid. */
    public static void checkProxy(Proxy proxy) throws IOException {
        var cont = new URL(TEST_URL).openConnection(proxy);
        cont.setConnectTimeout(1000);
        cont.getInputStream();
    }

    /** Initialize a file, including creating the parent folders and itself. */
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
