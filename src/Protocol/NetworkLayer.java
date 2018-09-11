package Protocol;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class NetworkLayer {

    public static final int SEND = 0;
    public static final int RECEIVE = 1;
    private int packetsReceived, packetsSent,mode;
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;
    private boolean isSendingDone,isReceivingDone;

    public NetworkLayer(String filename,int mode) {
        setMode(mode);
        try {
            if(mode == NetworkLayer.SEND)
            {
                setFileInputStream(new FileInputStream(filename));
                packetsSent = 0;
                isSendingDone = false;
            }
            else if(mode == NetworkLayer.RECEIVE)
            {
                setFileOutputStream(new FileOutputStream(filename));
                packetsReceived = 0;
                isReceivingDone = false;

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public NetworkLayer(String inputFile,String outputFile) {
        try {
            setFileInputStream(new FileInputStream(inputFile));
            packetsSent = 0;
            isSendingDone = false;
            setFileOutputStream(new FileOutputStream(outputFile));
            packetsReceived = 0;
            isReceivingDone = false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void toNetworkLayer(NetworkPacket networkPacket){
        try {
            packetsReceived++;
            System.out.println(packetsReceived + " Packets Received");
            if(networkPacket.getPacketType() == NetworkPacket.DATA){
                fileOutputStream.write(networkPacket.getData(),0,networkPacket.getSize());
            }
            else if(networkPacket.getPacketType() == NetworkPacket.STOP){
                System.out.println("Receiver Network Layer STOP");
                isReceivingDone = true;
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NetworkPacket fromNetworkLayer() throws IOException{
            NetworkPacket networkPacket = new NetworkPacket();
            int bytesRead;
            byte data[] = new byte[NetworkPacket.MAX_PKT_SIZE];
            bytesRead = fileInputStream.read(data);
            if(bytesRead == -1) {
                System.out.println("From - Network layer - EOF");
                networkPacket.setPacketType(NetworkPacket.STOP);
                fileInputStream.close();
                isSendingDone = true;
            }
            else {
                networkPacket.setPacketType(NetworkPacket.DATA);
            }
            networkPacket.setSize(bytesRead);
            networkPacket.setData(data);
            packetsSent++;
            System.out.println(packetsSent + " Packets Sent");
            return networkPacket;
    }


    //Testing the Network Layer
    //Test Successful
    public static void main(String args[]) throws IOException{
        System.out.println("Testing Network Layer");
        NetworkLayer networkLayerSend = new NetworkLayer("C:/Users/qasim/Desktop/Send/image.jpg",NetworkLayer.SEND);
        NetworkLayer networkLayerReceive = new NetworkLayer("C:/Users/qasim/Desktop/Receive/image.jpg",NetworkLayer.RECEIVE);
        System.out.println("Networks Declared");
        while (!networkLayerSend.isSendingDone()){
            networkLayerReceive.toNetworkLayer(networkLayerSend.fromNetworkLayer());
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

    public void setPacketsReceived(int packetsReceived) {
        this.packetsReceived = packetsReceived;
    }

    public boolean isSendingDone() {
        return isSendingDone;
    }

    public boolean isReceivingDone() {
        return isReceivingDone;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

}
