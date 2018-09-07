package SelectiveRepeat;

public interface TimeoutEventListener {
    public static final int FRAME_TIMEOUT = 0;
    public static final int ACKNOWLEDGMENT_TIMEOUT = 1;
    void onTimeout(int timeoutType);
}
