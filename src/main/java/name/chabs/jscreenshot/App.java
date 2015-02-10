package name.chabs.jscreenshot;

import name.chabs.jscreenshot.service.ScreenshotImpl;
import name.chabs.jscreenshot.service.ScreenshotService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Sample app which take a screenshot and save it
 * to png file in temporary system directory.
 */
class App {
    private final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        final String tmpDir = "/tmp"; //System.getProperty("java.io.tmpdir");
        final SimpleDateFormat dateFormat = new SimpleDateFormat("HH.mm.ss-dd.MM.yyyy");
        final String currentDate = dateFormat.format(new Date());
        final String fileName = tmpDir + File.separator + "screenshot-" + currentDate + ".png";
        final ScreenshotService screenshot = new ScreenshotImpl();
        try {
            screenshot.saveScreenshot(fileName);
        } catch (Exception e) {
            logger.error("Error taking screenshot {} - Stacktrace : ",
                    e.getMessage(), ExceptionUtils.getStackTrace(e));
        }
    }
}
