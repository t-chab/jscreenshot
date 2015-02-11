package name.chabs.jscreenshot.service;

import name.chabs.jscreenshot.exceptions.ScreenshotException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenshotImpl implements ScreenshotService {

    private final static float SCREEN_PREVIEW_FACTOR = new Float(0.8);
    private final Logger logger = LoggerFactory.getLogger(ScreenshotImpl.class);
    Rectangle captureRect;

    private Rectangle makeScreenshot(final BufferedImage screen) {
        final BufferedImage screenCopy = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
        final JLabel screenLabel = new JLabel(new ImageIcon(screenCopy));
        JScrollPane screenScroll = new JScrollPane(screenLabel);

        int width = Math.round((new Float(screen.getWidth()) * SCREEN_PREVIEW_FACTOR));
        int height = Math.round((new Float(screen.getHeight()) * SCREEN_PREVIEW_FACTOR));

        screenScroll.setPreferredSize(new Dimension(width, height));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(screenScroll, BorderLayout.CENTER);

        final JLabel selectionLabel = new JLabel("Select zone to capture");
        panel.add(selectionLabel, BorderLayout.SOUTH);

        repaint(screen, screenCopy);
        screenLabel.repaint();

        screenLabel.addMouseMotionListener(new MouseMotionAdapter() {

            Point start = new Point();

            @Override
            public void mouseMoved(MouseEvent me) {
                start = me.getPoint();
                repaint(screen, screenCopy);
                logger.debug("Start Point: {}", start);
                screenLabel.repaint();
            }

            @Override
            public void mouseDragged(MouseEvent me) {
                Point end = me.getPoint();
                captureRect = new Rectangle(start,
                        new Dimension(end.x - start.x, end.y - start.y));
                repaint(screen, screenCopy);
                screenLabel.repaint();
                logger.debug("Rectangle: {}", captureRect);
            }
        });

        JOptionPane.showMessageDialog(null, panel);

        logger.debug("Rectangle of interest: {}", captureRect);

        return captureRect;
    }

    void repaint(BufferedImage orig, BufferedImage copy) {
        Graphics2D g = copy.createGraphics();
        g.drawImage(orig, 0, 0, null);
        if (captureRect != null) {
            g.setColor(Color.RED);
            g.draw(captureRect);
            g.setColor(new Color(255, 255, 255, 150));
            g.fill(captureRect);
        }
        g.dispose();
    }

    public void saveScreenshot(final String fileName) throws ScreenshotException {
        // Init output file
        if (StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("Filename can't be null or empty !");
        }

        File outputFile = new File(fileName);

        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        try {
            final Robot robot = new Robot();
            // Create full screenshot
            final BufferedImage image = robot.createScreenCapture(new Rectangle(screenSize));

            // Ask user to choose which part of screen to use
            Rectangle rect = makeScreenshot(image);

            // Save selected part
            final BufferedImage lImg = robot.createScreenCapture(rect);

            ImageIO.write(lImg, "png", outputFile);
        } catch (AWTException | IOException lEx) {
            throw new ScreenshotException(lEx.getMessage(), lEx);
        }
    }
}
