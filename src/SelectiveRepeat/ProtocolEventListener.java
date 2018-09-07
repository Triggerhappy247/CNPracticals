package SelectiveRepeat;

public interface ProtocolEventListener {
    void onTimeout();
    void onAcknowledgementTimeout();
}
