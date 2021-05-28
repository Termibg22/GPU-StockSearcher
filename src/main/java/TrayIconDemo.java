import java.awt.Image;
import java.awt.AWTException;
import java.awt.TrayIcon;
import java.awt.Toolkit;
import java.awt.SystemTray;

public class TrayIconDemo
{
    public void displayTray(final String title, final String text) {
        final SystemTray tray = SystemTray.getSystemTray();
        final Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        final TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("System tray icon demo");
        try {
            tray.add(trayIcon);
        }
        catch (AWTException e) {
            e.printStackTrace();
        }
        trayIcon.displayMessage(title, text, TrayIcon.MessageType.INFO);
    }
}