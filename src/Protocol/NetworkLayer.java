package Protocol;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class NetworkLayer {

    public static final int SEND = 0;
    public static final int RECEIVE = 1;
    private int packetsReceived, packetSent,mode;
    private NetworkPacket networkPacket;
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;
    private boolean isLayerEnabled;
    private boolean isPacketSet;
    private NetworkLayerSendThread networkLayerSendThread;
    private List<NetworkEventListener> networkEventListeners = new ArrayList<>();
    private Timer fileOutputTimer;

    public NetworkLayer(String filename,int mode) {
        disableNetworkLayer();
        setPacketSet(false);
        setMode(mode);
        try {
            if(mode == NetworkLayer.SEND)
            {
                setFileInputStream(new FileInputStream(filename));
                setNetworkLayerSendThread(new NetworkLayerSendThread(this));
                setPacketSent(0);
            }
            else if(mode == NetworkLayer.RECEIVE)
            {
                setFileOutputStream(new FileOutputStream(filename));
                setFileOutputTimer(new Timer(true));
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
            fileOutputTimer.cancel();
            fileOutputTimer.purge();
            setFileOutputTimer(new Timer(true));
            fileOutputStream.write(networkPacket.getData());
            packetsReceived++;
            System.out.println(packetsReceived + " Packets Received");
            fileOutputTimer.schedule(new CloseReceiverTask(this),5000);
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
        NetworkLayer networkLayerSend = new NetworkLayer("C:/Users/qasim/Desktop/Send/Star Wars The Clone Wars - S01E01.mp4",NetworkLayer.SEND);
        NetworkLayer networkLayerReceive = new NetworkLayer("C:/Users/qasim/Desktop/Receive/Star Wars The Clone Wars - S01E01.mp4",NetworkLayer.RECEIVE);
        System.out.println("Networks Declared");
        class Test implements NetworkEventListener{
            @Override
            public void onNetworkLayerReady() {
                networkLayerReceive.toNetworkLayer(networkLayerSend.fromNetworkLayer());
                System.out.println(networkLayerReceive.getPacketsReceived() + " Packets Received");
            }

        }

        Test test = new Test();
        networkLayerSend.addNetworkEventListener(test);
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
        setLayerEnabled(true);
        if(isPacketSet()) {
            for (NetworkEventListener nel : networkEventListeners){
                nel.onNetworkLayerReady();
            }
        }
    }

    public void disableNetworkLayer(){
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

    public void setFileOutputTimer(Timer fileOutputTimer) {
        this.fileOutputTimer = fileOutputTimer;
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
}
