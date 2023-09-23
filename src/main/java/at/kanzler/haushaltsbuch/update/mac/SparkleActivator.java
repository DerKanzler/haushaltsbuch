package at.kanzler.haushaltsbuch.update.mac;

public class SparkleActivator {

    /**
     * Native method declaration
     */
    public native static void initSparkle(String pathToSparkleFramework, boolean updateAtStartup, int checkInterval);

    /**
     * Whether updates are checked at startup
     */
    private boolean updateAtStartup = false;

    /**
     * Check interval period, in seconds
     */
    private int checkInterval = 86400; // 1 day

    /**
     * Dynamically loads the JNI object. Will fail if it is launched on an
     * non-MacOSX system or when libinit_sparkle.dylib is outside of the
     * LD_LIBRARY_PATH
     */
    static {
        System.loadLibrary("sparkle_init");
    }

    /**
     * Initialize and start Sparkle
     * 
     * @throws Exception
     */
    public void start() throws Exception {
        initSparkle(System.getProperty("user.dir") + "/../../Frameworks/Sparkle.framework", updateAtStartup,
                checkInterval);
    }

}