package Protocol;

public interface TimeoutEventListener {
    void onFrameTimeout();
    void onAcknowledgementTimeout();
}
