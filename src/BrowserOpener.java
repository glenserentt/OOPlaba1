
import java.awt.Desktop;
import java.net.URI;

public class BrowserOpener {

    public static void openInBrowser(String url) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI(url));
                    return;
                }
            }
            // Альтернативные способы для разных ОС
            openFallback(url);
        } catch (Exception e) {
            System.out.println("Не удалось открыть браузер. Ссылка: " + url);
        }
    }

    private static void openFallback(String url) throws Exception {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
        } else if (os.contains("mac")) {
            Runtime.getRuntime().exec(new String[]{"open", url});
        } else if (os.contains("nix") || os.contains("nux")) {
            Runtime.getRuntime().exec(new String[]{"xdg-open", url});
        }
    }
}

