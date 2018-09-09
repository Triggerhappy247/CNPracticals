package Protocol;

public interface NetworkEventListener {
    void onNetworkLayerReady();
    void onClose();
}
