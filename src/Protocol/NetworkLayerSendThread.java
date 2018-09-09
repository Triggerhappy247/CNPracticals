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
        NetworkPacket networkPacket;
        int bytesRead;
        try {
            while ((bytesRead = networkLayer.getFileInputStream().read(data)) != -1) {
                networkPacket = new NetworkPacket();
                networkPacket.setSize(bytesRead);
                networkPacket.setData(data);
                networkPacket.setPacketType(NetworkPacket.DATA);
                networkLayer.setNetworkPacket(networkPacket);
                System.out.println("Bytes Read " + bytesRead);
            }
            networkPacket = new NetworkPacket();
            networkPacket.setPacketType(NetworkPacket.STOP);
            networkLayer.setNetworkPacket(networkPacket);
            networkLayer.getFileInputStream().close();
            networkLayer.setDone(true);
            System.out.println("Sender Network Layer STOP");
        } catch (IOException e) {
                e.printStackTrace();
        }

    }
}
