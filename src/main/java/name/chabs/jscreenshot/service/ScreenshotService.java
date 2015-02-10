package name.chabs.jscreenshot.service;

import name.chabs.jscreenshot.exceptions.ScreenshotException;

/**
 * Created by tchabaud on 10/02/2015.
 * This service provides method to take screenshots.
 */
public interface ScreenshotService {
    /**
     * Save a PNG screenshot to fileName.
     *
     * @param fileName File name to save screenshot
     * @throws name.chabs.jscreenshot.exceptions.ScreenshotException if there is a problem
     */
    void saveScreenshot(final String fileName) throws ScreenshotException;
}
