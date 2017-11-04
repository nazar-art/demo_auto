package net.lelyak.core.logging;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Volodymyr_Chaban
 * Date: 21.12.12
 */
public final class LogMarkers {

    public static final Marker MARKER_PASS = MarkerFactory.getMarker("MARKER_PASS");
    public static final Marker MARKER_INFO = MarkerFactory.getMarker("MARKER_INFO");
    public static final Marker MARKER_DEBUG = MarkerFactory.getMarker("MARKER_DEBUG");
    public static final Marker MARKER_FAIL = MarkerFactory.getMarker("MARKER_FAIL");
    public static final Marker MARKER_ERROR = MarkerFactory.getMarker("MARKER_ERROR");
    public static final Marker MARKER_ENV = MarkerFactory.getMarker("MARKER_ENV");

    private LogMarkers() {
    }
}
