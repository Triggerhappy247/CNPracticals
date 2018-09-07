package SelectiveRepeat;

import java.io.File;

public class NetworkLayer {

    private NetworkPacket networkPacket;
    private File sourceFile;
    private boolean isLayerEnabled;

    public NetworkPacket getNetworkPacket() {
        return networkPacket;
    }

    public void setNetworkPacket(NetworkPacket networkPacket) {
        this.networkPacket = networkPacket;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public boolean isLayerEnabled() {
        return isLayerEnabled;
    }

    public void setLayerEnabled(boolean layerEnabled) {
        isLayerEnabled = layerEnabled;
    }

    public void toNetworkLayer(NetworkPacket networkPacket){

    }

    public NetworkPacket fromNetworkLayer(){
        return new NetworkPacket();
    }
}
