package name.chabs.jscreenshot.service;

import name.chabs.jscreenshot.exceptions.ScreenshotException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ScreenshotImpl implements ScreenshotService {

    private final Logger logger = LoggerFactory.getLogger(ScreenshotImpl.class);

    private BufferedImage captureScreen(Rectangle recordArea) throws AWTException {
        final String mouseCursorFile = "cursors/screenshot_cursor.png";
        final URL cursorURL = getClass().getClassLoader().getResource(mouseCursorFile);
        final Robot robot = new Robot();

        // Init cursor
        BufferedImage mouseCursor = null;
        if (cursorURL != null) {
            try {
                mouseCursor = ImageIO.read(cursorURL);
            } catch (IllegalArgumentException | IOException e) {
                logger.warn("Exception {} when trying to read {}. Mouse cursor will not be initialized.",
                        e.getMessage(), cursorURL);
            }
        }

        final Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        final BufferedImage image = robot.createScreenCapture(recordArea);

        Graphics2D graphics = image.createGraphics();
        graphics.drawImage(mouseCursor, mousePosition.x - 8, mousePosition.y - 5, null);
        graphics.dispose();

        return image;
    }

    public void saveScreenshot(final String fileName) throws ScreenshotException {

        // Init output file
        if (StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("Filename can't be null or empty !");
        }

        File outputFile = new File(fileName);

        final Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        try {
            final BufferedImage lImg = captureScreen(rectangle);
            ImageIO.write(lImg, "png", outputFile);
        } catch (AWTException | IOException lEx) {
            throw new ScreenshotException(lEx.getMessage(), lEx);
        }
    }
}
