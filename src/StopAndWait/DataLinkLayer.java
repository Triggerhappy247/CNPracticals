package StopAndWait;

import Protocol.*;

import java.util.Scanner;


public class DataLinkLayer{

    private NetworkLayer networkLayer;
    private PhysicalLayer physicalLayer;
    //Testing the DLL
    public static void main(String args[]){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Filename: ");
        String filename = scanner.nextLine();
        System.out.println("1.Send\n2.Receive\nChoice:");
        int choice = scanner.nextInt();
        DataLinkLayer dataLinkLayer;
        switch (choice) {
            case 1:
                dataLinkLayer = new DataLinkLayer();
                dataLinkLayer.networkLayer = new NetworkLayer(String.format("C:/Users/qasim/Desktop/Send/%s",filename),NetworkLayer.SEND);
                dataLinkLayer.networkLayer.startReading();
                dataLinkLayer.physicalLayer = new PhysicalLayer(800);
                dataLinkLayer.sender();
                break;
            case 2:
                dataLinkLayer = new DataLinkLayer();
                dataLinkLayer.networkLayer = new NetworkLayer(String.format("C:/Users/qasim/Desktop/Receive/%s",filename),NetworkLayer.RECEIVE);
                dataLinkLayer.physicalLayer = new PhysicalLayer("127.0.0.1",800);
                dataLinkLayer.receiver();
                break;
            default:System.out.println("Incorrect");
        }
    }

    public void sender(){
        int nextFrame = 0;
        Frame frame = new Frame();
        NetworkPacket networkPacket;
        networkPacket = networkLayer.fromNetworkLayer();
        do{
            frame.setNetworkPacket(networkPacket);
            frame.setSequenceNumber(nextFrame);
            physicalLayer.toPhysicalLayer(frame);
            Protocol.start_timer(nextFrame);
            frame = physicalLayer.fromPhysicalLayer();
            if(frame.getAcknowledgmentNumber() == nextFrame){
                Protocol.stop_timer(nextFrame);
                networkPacket = networkLayer.fromNetworkLayer();
                nextFrame = 1 - nextFrame;
            }
        }while (!networkLayer.isDone());
        frame = new Frame(new NetworkPacket(),0,0,FrameType.STOP);
        System.out.println("STOP Frame");
        physicalLayer.toPhysicalLayer(frame);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        physicalLayer.stopPhysicalLayer();
    }

    public void receiver(){
        int frameExpected = 0;
        Frame sendFrame,receiveFrame;
        do{
            receiveFrame = physicalLayer.fromPhysicalLayer();
            if(receiveFrame.getFrameType() == FrameType.STOP)
                break;
            if(receiveFrame.getSequenceNumber() == frameExpected){
                networkLayer.toNetworkLayer(receiveFrame.getNetworkPacket());
                frameExpected = 1 - frameExpected;
            }
            sendFrame = new Frame();
            sendFrame.setAcknowledgmentNumber(1 - frameExpected);
            physicalLayer.toPhysicalLayer(sendFrame);
        }while (true);
        physicalLayer.stopPhysicalLayer();
    }

}
