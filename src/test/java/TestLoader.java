import com.yallage.oceanik.loader.plugin.OceanikMain;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static org.mockito.Mockito.*;

/**
 * @author Milkory
 */
@SuppressWarnings("ALL")
public class TestLoader {

    @Test
    public void loadTest() {
        System.out.println(System.getProperty("user.dir"));
        var main = mock(OceanikMain.class);
        when(main.getDataFolder())
                .thenReturn(new File(System.getProperty("user.dir"), "oceanik-test"));
        doCallRealMethod().when(main).onLoad();
        when(main.getResource(anyString())).thenReturn(getResource(main));
        main.onLoad();
    }

    @SneakyThrows public InputStream getResource(OceanikMain main) {
        var cont = main.getClass().getClassLoader().getResource("oceanik-loader.yml").openConnection();
        cont.setUseCaches(false);
        return cont.getInputStream();
    }

}
