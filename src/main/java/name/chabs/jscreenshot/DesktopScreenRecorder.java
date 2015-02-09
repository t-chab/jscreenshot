package name.chabs.jscreenshot;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

class DesktopScreenRecorder {

    final Logger logger = LoggerFactory.getLogger(App.class);

    private boolean useWhiteCursor = false;
    private Robot robot;
    private BufferedImage mouseCursor;

    public DesktopScreenRecorder() {
        String mouseCursorFile;

        if (useWhiteCursor) {
            mouseCursorFile = "cursors/white_cursor.png";
        } else {
            mouseCursorFile = "cursors/black_cursor.png";
        }
        URL cursorURL = getClass().getClassLoader().getResource(mouseCursorFile);

        if (cursorURL != null) {
            try {
                mouseCursor = ImageIO.read(cursorURL);
            } catch (IllegalArgumentException | IOException e) {
                logger.warn("Exception {} when trying to read {}", e.getMessage(), cursorURL);
            }
        }
    }

    public Rectangle initialiseScreenCapture() {
        Rectangle rect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        try {
            robot = new Robot();
        } catch (AWTException awe) {
            logger.warn("AWTException {} when trying to create java.awt.Robot - Stacktrace : {}",
                    awe.getMessage(), ExceptionUtils.getStackTrace(awe));
            rect = null;
        }
        return rect;
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
