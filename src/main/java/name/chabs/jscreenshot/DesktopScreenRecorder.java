package name.chabs.jscreenshot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class DesktopScreenRecorder {

    public static boolean useWhiteCursor;
    private Robot robot;
    private BufferedImage mouseCursor;

    public DesktopScreenRecorder() {
        try {

            String mouseCursorFile;

            if (useWhiteCursor) {
                mouseCursorFile = "cursors/white_cursor.png";
            } else {
                mouseCursorFile = "cursors/black_cursor.png";
            }
            URL cursorURL = getClass().getClassLoader().getResource(mouseCursorFile);

            if (cursorURL != null) {
                mouseCursor = ImageIO.read(cursorURL);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Rectangle initialiseScreenCapture() {
        try {
            robot = new Robot();
        } catch (AWTException awe) {
            awe.printStackTrace();
            return null;
        }
        return new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    }

    public BufferedImage captureScreen(Rectangle recordArea) {
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        BufferedImage image = robot.createScreenCapture(recordArea);

        Graphics2D graphics = image.createGraphics();

        graphics.drawImage(mouseCursor, mousePosition.x - 8, mousePosition.y - 5,
                null);

        graphics.dispose();

        return image;
    }
}
