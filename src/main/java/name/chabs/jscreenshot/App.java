package name.chabs.jscreenshot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Sample app which take a screenshot and save it
 * to png file in temporary system directory
 */
class App {

    final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        DesktopScreenRecorder recorder = new DesktopScreenRecorder();
        final BufferedImage lImg = recorder.captureScreen(recorder.initialiseScreenCapture());
        final String tmpDir = System.getProperty("java.io.tmpdir");
        final SimpleDateFormat dateFormat = new SimpleDateFormat("HH.mm.ss-dd.MM.yyyy");
        final String currentDate = dateFormat.format(new Date());
        final File outputFile = new File(tmpDir + File.separator + "screenshot-" + currentDate + ".png");
        try {
            ImageIO.write(lImg, "png", outputFile);
        } catch (IOException lEx) {
            logger.error("Exception {} when trying to write output file !", lEx.getMessage());
        }
    }
}
