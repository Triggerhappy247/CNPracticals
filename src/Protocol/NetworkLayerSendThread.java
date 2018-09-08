package Protocol;


import java.io.IOException;

public class NetworkLayerSendThread implements Runnable{

    private NetworkLayer networkLayer;
    private Thread thread;

    public NetworkLayerSendThread(NetworkLayer networkLayer) {
        this.networkLayer = networkLayer;
        thread = new Thread(this,"NetworkLayerSendThread");
    }

    public void startReading(){
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
        byte data[] = new byte[NetworkPacket.MAX_PKT_SIZE];
        byte packetData[];
        NetworkPacket networkPacket;
        int bytesRead;
        try {
            while ((bytesRead = networkLayer.getFileInputStream().read(data)) != -1) {
                networkPacket = new NetworkPacket(bytesRead);
                packetData = networkPacket.getData();
                for (int i = 0; i < bytesRead; i++) {
                    packetData[i] = data[i];
                }
                networkLayer.setNetworkPacket(networkPacket);
            }
            networkLayer.getFileInputStream().close();
        } catch (IOException e) {
                e.printStackTrace();
        }

    }
}
