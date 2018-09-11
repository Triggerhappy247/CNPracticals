package SelectiveRepeat;

import Protocol.*;

import java.util.Scanner;


public class DataLinkLayer implements TimeoutEventListener{
    public static int MAXIMUM_SEQUENCE = 7;
    public static int WINDOW_SIZE = (MAXIMUM_SEQUENCE + 1)/2;
    public static int OLDEST_FRAME = MAXIMUM_SEQUENCE + 1;
    public boolean noNAK = true;
    public int acknowledgementExpected,nextFrame,frameExpected,tooFar,bufferedNum;
    public NetworkPacket outBound[];
    public NetworkPacket inBound[];
    public boolean arrived[];
    private NetworkLayer networkLayer;
    private PhysicalLayer physicalLayer;
    public SenderThread senderThread;
    public ReceiverThread receiverThread;

    //Testing the DLL
    public static void main(String args[]){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Sending Filename: ");
        String inputfilename = scanner.nextLine();
        System.out.println("Receiving Filename: ");
        String outputfilename = scanner.nextLine();
        System.out.println("1.Server\n2.Client\nChoice:");
        int choice = scanner.nextInt();
        NetworkLayer networkLayer;
        PhysicalLayer physicalLayer;
        DataLinkLayer dataLinkLayer = null;
        switch (choice) {
            case 1:
                networkLayer = new NetworkLayer(String.format("C:/Users/qasim/Desktop/Server/%s",inputfilename),String.format("C:/Users/qasim/Desktop/Server/%s",outputfilename));
                physicalLayer = new PhysicalLayer(800);
                dataLinkLayer = new DataLinkLayer(networkLayer,physicalLayer);
                Protocol.FRAME_TIMER.addFrameTimerListener(dataLinkLayer);
                Protocol.ACKNOWLEDGE_TIMER.addAcknowledgementTimerListener(dataLinkLayer);
                dataLinkLayer.senderThread = new SenderThread(dataLinkLayer);
                dataLinkLayer.receiverThread = new ReceiverThread(dataLinkLayer);
                break;
            case 2:
                networkLayer = new NetworkLayer(String.format("C:/Users/qasim/Desktop/Client/%s",inputfilename),String.format("C:/Users/qasim/Desktop/Client/%s",outputfilename));
                physicalLayer = new PhysicalLayer("127.0.0.1",800);
                dataLinkLayer = new DataLinkLayer(networkLayer,physicalLayer);
                //Protocol.FRAME_TIMER.addFrameTimerListener(dataLinkLayer);
                //Protocol.ACKNOWLEDGE_TIMER.addAcknowledgementTimerListener(dataLinkLayer);
                dataLinkLayer.senderThread = new SenderThread(dataLinkLayer);
                dataLinkLayer.receiverThread = new ReceiverThread(dataLinkLayer);
                break;
            default:System.out.println("Incorrect");
        }
        try{
            dataLinkLayer.senderThread.thread.join();
            dataLinkLayer.receiverThread.thread.join();
            //Protocol.FRAME_TIMER.removeFrameTimerListener(dataLinkLayer);
            //Protocol.ACKNOWLEDGE_TIMER.removeAcknowledgementTimerListener(dataLinkLayer);
            dataLinkLayer.physicalLayer.stopPhysicalLayer();

        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public DataLinkLayer(NetworkLayer networkLayer,PhysicalLayer physicalLayer) {
        this.networkLayer = networkLayer;
        this.physicalLayer = physicalLayer;
        acknowledgementExpected = 0;
        nextFrame = 0;
        frameExpected = 0;
        tooFar = WINDOW_SIZE;
        bufferedNum = 0;
        arrived = new boolean[WINDOW_SIZE];
        outBound = new NetworkPacket[WINDOW_SIZE];
        inBound = new NetworkPacket[WINDOW_SIZE];
    }

    public static boolean isBetween(int a, int b, int c){
        return (((a <= b) && (b < c)) || ((c < a) && (a <= b)) || ((b < c) && (c < a)));
    }

    synchronized public void sendFrame(int frameType,int sequenceNumber,int frameExpected,NetworkPacket networkPacket[]){
        Frame frame = new Frame(networkPacket[sequenceNumber % WINDOW_SIZE],sequenceNumber,(frameExpected + MAXIMUM_SEQUENCE) % (MAXIMUM_SEQUENCE + 1),frameType);
        if(frameType == FrameType.NAK)
            setNoNAK(false);
        physicalLayer.toPhysicalLayer(frame);
        System.out.println("Sending Frame " + sequenceNumber);
        //if(frameType == FrameType.DATA)
          //  Protocol.start_timer(sequenceNumber % WINDOW_SIZE);
        //Protocol.stop_ack_timer();
    }

    public NetworkLayer getNetworkLayer() {
        return networkLayer;
    }

    public PhysicalLayer getPhysicalLayer() {
        return physicalLayer;
    }

    @Override
    public void onFrameTimeout() {
        System.out.println("Frame TimeOut");
        sendFrame(FrameType.DATA, OLDEST_FRAME, frameExpected, outBound);
    }

    @Override
    public void onAcknowledgementTimeout() {
        System.out.println("Sending Acknowledgment");
        sendFrame(FrameType.ACK, 0, frameExpected, outBound);
    }

    synchronized public int getBufferedNum() {
        return bufferedNum;
    }

    synchronized public void setBufferedNum(int bufferedNum) {
        this.bufferedNum = bufferedNum;
    }

    public boolean isNoNAK() {
        return noNAK;
    }

    public void setNoNAK(boolean noNAK) {
        this.noNAK = noNAK;
    }

    public void setNextFrame(int nextFrame) {
        this.nextFrame = nextFrame;
    }

    public void setFrameExpected(int frameExpected) {
        this.frameExpected = frameExpected;
    }

    public void setTooFar(int tooFar) {
        this.tooFar = tooFar;
    }

    public void setAcknowledgementExpected(int acknowledgementExpected) {
        this.acknowledgementExpected = acknowledgementExpected;
    }
}
