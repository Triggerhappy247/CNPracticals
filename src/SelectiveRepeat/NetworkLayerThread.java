package SelectiveRepeat;

import java.io.IOException;

public class NetworkLayerThread implements Runnable{

    private NetworkLayer networkLayer;
    private Thread thread;

    public NetworkLayerThread(NetworkLayer networkLayer) {
        this.networkLayer = networkLayer;
        thread = new Thread(this,"NetworkLayerThread");
        thread.start();
    }

    public NetworkLayer getNetworkLayer() {
        return networkLayer;
    }

    public void setNetworkLayer(NetworkLayer networkLayer) {
        this.networkLayer = networkLayer;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    @Override
    public void run() {
        NetworkPacket networkPacket = new NetworkPacket();
        while (true)
        {
            try {
                networkLayer.getFileInputStream().read(networkPacket.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
