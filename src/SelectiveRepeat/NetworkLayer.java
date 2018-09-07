package SelectiveRepeat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NetworkLayer {

    public static final int SEND = 0;
    public static final int RECEIVE = 1;
    private NetworkPacket networkPacket;
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;
    private boolean isLayerEnabled,isPacketSet;
    private List<NetworkEventListener> networkEventListeners = new ArrayList<>();

    public NetworkLayer(String filename,int mode) {
        setNetworkPacket(new NetworkPacket());
        try {
            if(mode == NetworkLayer.SEND)
            {
                setFileInputStream(new FileInputStream(filename));
            }
            else if(mode == NetworkLayer.RECEIVE)
            {
                setFileOutputStream(new FileOutputStream(filename));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addNetworkEventListener(NetworkEventListener networkEventListener){
        networkEventListeners.add(networkEventListener);
    }

    public NetworkPacket getNetworkPacket() {
        return networkPacket;
    }

    public void setNetworkPacket(NetworkPacket networkPacket) {
        try {
            this.networkPacket = networkPacket;
            wait();
            if(isLayerEnabled()){
                for (NetworkEventListener nel : networkEventListeners){
                    nel.onNetworkLayerReady();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public FileInputStream getFileInputStream() {
        return fileInputStream;
    }

    public void setFileInputStream(FileInputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }

    public FileOutputStream getFileOutputStream() {
        return fileOutputStream;
    }

    public void setFileOutputStream(FileOutputStream fileOutputStream) {
        this.fileOutputStream = fileOutputStream;
    }

    public boolean isLayerEnabled() {
        return isLayerEnabled;
    }

    private void setLayerEnabled(boolean layerEnabled) {
        isLayerEnabled = layerEnabled;
    }

    public boolean isPacketSet() {
        return isPacketSet;
    }

    public void setPacketSet(boolean packetSet) {
        isPacketSet = packetSet;
    }

    public void enableNetworkLayer(){
        setLayerEnabled(true);
    }

    public void disableNetworkLayer(){
        setLayerEnabled(false);
    }

    public void toNetworkLayer(NetworkPacket networkPacket){
        try {
            fileOutputStream.write(networkPacket.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NetworkPacket fromNetworkLayer(){
        NetworkPacket networkPacket = getNetworkPacket();
        notify();
        return  networkPacket;
    }
}
