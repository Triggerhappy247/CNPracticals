package Protocol;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NetworkLayer {

    public static final int SEND = 0;
    public static final int RECEIVE = 1;
    private int packetsReceived, packetSent,mode;
    private NetworkPacket networkPacket;
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;
    private File file;
    private boolean isLayerEnabled;
    private boolean isPacketSet;
    private NetworkLayerSendThread networkLayerSendThread;
    private List<NetworkEventListener> networkEventListeners = new ArrayList<>();

    public NetworkLayer(String filename,int mode) {
        disableNetworkLayer();
        setPacketSet(false);
        setMode(mode);
        try {
            if(mode == NetworkLayer.SEND)
            {
                file = new File(filename);
                setFileInputStream(new FileInputStream(filename));
                setNetworkLayerSendThread(new NetworkLayerSendThread(this));
                setPacketSent(0);
            }
            else if(mode == NetworkLayer.RECEIVE)
            {
                setFileOutputStream(new FileOutputStream(filename));
                setPacketsReceived(0);

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

    synchronized public void setNetworkPacket(NetworkPacket networkPacket) {
        while(isPacketSet()){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.networkPacket = networkPacket;
        setPacketSet(true);
        notify();
        if(isLayerEnabled()){
            for (NetworkEventListener nel : networkEventListeners){
                nel.onNetworkLayerReady();
            }
        }
    }

    public void toNetworkLayer(NetworkPacket networkPacket){
        try {
            if(networkPacket.getPacketType() == NetworkPacket.DATA){
                fileOutputStream.write(networkPacket.getData());
                packetsReceived++;
                System.out.println(packetsReceived + " Packets Received");
            }
            else if(networkPacket.getPacketType() == NetworkPacket.STOP){
                fileOutputStream.close();
                for (NetworkEventListener nel : networkEventListeners){
                    nel.onClose();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized public NetworkPacket fromNetworkLayer(){
        while (!isPacketSet()){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        NetworkPacket networkPacket = getNetworkPacket();
        packetSent++;
        System.out.println(packetSent + " Packets Sent");
        setPacketSet(false);
        notify();
        return networkPacket;
    }

    public void startReading(){
        networkLayerSendThread.startReading();
    }

    //Testing the Network Layer
    //Test Successful
    public static void main(String args[]){
        System.out.println("Testing Network Layer");
        NetworkLayer networkLayerSend = new NetworkLayer("C:/Users/qasim/Desktop/Send/1.pdf",NetworkLayer.SEND);
        NetworkLayer networkLayerReceive = new NetworkLayer("C:/Users/qasim/Desktop/Receive/1.pdf",NetworkLayer.RECEIVE);
        System.out.println("Networks Declared");
        class Test implements NetworkEventListener{
            @Override
            public void onNetworkLayerReady() {
                networkLayerReceive.toNetworkLayer(networkLayerSend.fromNetworkLayer());
            }

            @Override
            public void onClose() {
                System.out.println("Done");
            }
        }

        Test test = new Test();
        networkLayerSend.addNetworkEventListener(test);
        networkLayerReceive.addNetworkEventListener(test);
        networkLayerSend.enableNetworkLayer();
        networkLayerSend.startReading();
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
        System.out.println("Network Enabled");
        setLayerEnabled(true);
        if(isPacketSet()) {
            for (NetworkEventListener nel : networkEventListeners){
                nel.onNetworkLayerReady();
            }
        }
    }

    public void disableNetworkLayer(){
        System.out.println("Network Disabled");
        setLayerEnabled(false);
    }

    public int getPacketsReceived() {
        return packetsReceived;
    }

    public void setPacketsReceived(int packetsReceived) {
        this.packetsReceived = packetsReceived;
    }

    public int getPacketSent() {
        return packetSent;
    }

    public void setPacketSent(int packetSent) {
        this.packetSent = packetSent;
    }

    public File getFile() {
        return file;
    }

    public NetworkLayerSendThread getNetworkLayerSendThread() {
        return networkLayerSendThread;
    }

    public void setNetworkLayerSendThread(NetworkLayerSendThread networkLayerSendThread) {
        this.networkLayerSendThread = networkLayerSendThread;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public List<NetworkEventListener> getNetworkEventListeners() {
        return networkEventListeners;
    }
}
