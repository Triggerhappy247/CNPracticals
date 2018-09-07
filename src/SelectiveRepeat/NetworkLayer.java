package SelectiveRepeat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class NetworkLayer {

    public static final int SEND = 0;
    public static final int RECEIVE = 1;
    private NetworkPacket networkPacket;
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;
    private boolean isLayerEnabled;

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

    public NetworkPacket getNetworkPacket() {
        return networkPacket;
    }

    public void setNetworkPacket(NetworkPacket networkPacket) {
        this.networkPacket = networkPacket;
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
        NetworkPacket networkPacket = new NetworkPacket();
        try {
            fileInputStream.read(networkPacket.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return networkPacket;
    }
}
