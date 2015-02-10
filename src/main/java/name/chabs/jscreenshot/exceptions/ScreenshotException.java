package name.chabs.jscreenshot.exceptions;

/**
 * Created by tchabaud on 10/02/2015.
 * Exception thrown when error occurs during screenshot taking.
 */
public class ScreenshotException extends Exception {
    public ScreenshotException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
