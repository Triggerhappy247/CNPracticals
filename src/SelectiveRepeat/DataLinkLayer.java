package SelectiveRepeat;

import Protocol.*;

import java.util.Scanner;


public class DataLinkLayer implements NetworkEventListener, TimeoutEventListener, Runnable {
    public final static int MAXIMUM_SEQUENCE = 7;
    public static final int WINDOW_SIZE = (MAXIMUM_SEQUENCE + 1)/2;
    public static final int OLDEST_FRAME = MAXIMUM_SEQUENCE + 1;
    private boolean noNAK = true;
    private int acknowledgementExpected,nextFrame,frameExpected,tooFar,bufferedNum;
    private NetworkPacket outBound[];
    private NetworkPacket inBound[];
    private boolean arrived[];
    private NetworkLayer networkLayer;
    private Thread frameArrival;
    private PhysicalLayer physicalLayer;

    //Testing the DLL
    public static void main(String args[]){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Filename: ");
        String filename = scanner.nextLine();
        System.out.println("1.Send\n2.Receive\nChoice:");
        int choice = scanner.nextInt();
        NetworkLayer networkLayer;
        PhysicalLayer physicalLayer;
        DataLinkLayer dataLinkLayer;
        switch (choice) {
            case 1:
                networkLayer = new NetworkLayer(String.format("C:/Users/qasim/Desktop/Send/%s",filename),NetworkLayer.SEND);
                physicalLayer = new PhysicalLayer(800);
                dataLinkLayer = new DataLinkLayer(networkLayer,physicalLayer);
                break;
            case 2:
                networkLayer = new NetworkLayer(String.format("C:/Users/qasim/Desktop/Receive/%s",filename),NetworkLayer.RECEIVE);
                physicalLayer = new PhysicalLayer("127.0.0.1",800);
                dataLinkLayer = new DataLinkLayer(networkLayer,physicalLayer);
                break;
            default:System.out.println("Incorrect");
        }
    }

    public DataLinkLayer(NetworkLayer networkLayer,PhysicalLayer physicalLayer) {
        this.networkLayer = networkLayer;
        this.physicalLayer = physicalLayer;

        Protocol.FRAME_TIMER.addFrameTimerListener(this);
        Protocol.ACKNOWLEDGE_TIMER.addAcknowledgementTimerListener(this);
        if(networkLayer.getMode() == NetworkLayer.SEND) {
            networkLayer.addNetworkEventListener(this);
            networkLayer.enableNetworkLayer();
            networkLayer.startReading();
        }
        acknowledgementExpected = 0;
        nextFrame = 0;
        frameExpected = 0;
        tooFar = WINDOW_SIZE;
        bufferedNum = 0;
        arrived = new boolean[WINDOW_SIZE];
        outBound = new NetworkPacket[WINDOW_SIZE];
        inBound = new NetworkPacket[WINDOW_SIZE];
        frameArrival = new Thread(this);
        frameArrival.start();
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
        if(frameType == FrameType.DATA)
            Protocol.start_timer(sequenceNumber % WINDOW_SIZE);
        //Protocol.stop_ack_timer();
    }

    @Override
    public void run() {
        while(true) {
            Frame frame = physicalLayer.fromPhysicalLayer();
            System.out.println("Frame Arrival");
            if (frame.getFrameType() == FrameType.DATA) {
                if (frame.getSequenceNumber() != frameExpected && isNoNAK())
                    sendFrame(FrameType.NAK, 0, frameExpected, outBound);
                if (isBetween(frameExpected, frame.getSequenceNumber(), tooFar) && !arrived[frame.getSequenceNumber() % WINDOW_SIZE]) {
                    arrived[frame.getSequenceNumber() % WINDOW_SIZE] = true;
                    inBound[frame.getSequenceNumber() % WINDOW_SIZE] = frame.getNetworkPacket();
                    while (arrived[frameExpected % WINDOW_SIZE]) {
                        networkLayer.toNetworkLayer(inBound[frameExpected % WINDOW_SIZE]);
                        setNoNAK(true);
                        arrived[frameExpected % WINDOW_SIZE] = false;
                        setFrameExpected(Protocol.increment(frameExpected));
                        setTooFar(Protocol.increment(frameExpected));
                        onAcknowledgementTimeout();
                        //Protocol.start_ack_timer();
                    }
                }
            }
            if((frame.getFrameType() == FrameType.NAK) && isBetween(acknowledgementExpected,(frame.getAcknowledgmentNumber()+1) % (MAXIMUM_SEQUENCE + 1),nextFrame))
                sendFrame(FrameType.DATA,(frame.getAcknowledgmentNumber()+1) % (MAXIMUM_SEQUENCE + 1),frameExpected,outBound);
            while (isBetween(acknowledgementExpected,frame.getAcknowledgmentNumber(),nextFrame)){
                bufferedNum--;
                Protocol.stop_timer(acknowledgementExpected % WINDOW_SIZE);
                setAcknowledgementExpected(Protocol.increment(acknowledgementExpected));
            }
            if(bufferedNum < WINDOW_SIZE)
                networkLayer.enableNetworkLayer();
            else
                networkLayer.disableNetworkLayer();
        }

    }

    @Override
    public void onNetworkLayerReady() {
        System.out.println("Network Layer Ready");
        bufferedNum++;
        outBound[nextFrame % WINDOW_SIZE] = networkLayer.fromNetworkLayer();
        sendFrame(FrameType.DATA,nextFrame,frameExpected,outBound);
        setNextFrame(Protocol.increment(nextFrame));
        if(bufferedNum < WINDOW_SIZE)
            networkLayer.enableNetworkLayer();
        else
            networkLayer.disableNetworkLayer();
    }

    @Override
    public void onFrameTimeout() {
        System.out.println("Frame TimeOut");
        sendFrame(FrameType.DATA,OLDEST_FRAME,frameExpected,outBound);
        if(bufferedNum < WINDOW_SIZE)
            networkLayer.enableNetworkLayer();
        else
            networkLayer.disableNetworkLayer();
    }

    @Override
    public void onAcknowledgementTimeout() {
        System.out.println("Acknowledgement Timeout");
        sendFrame(FrameType.ACK,0,frameExpected,outBound);
        if(bufferedNum < WINDOW_SIZE)
            networkLayer.enableNetworkLayer();
        else
            networkLayer.disableNetworkLayer();
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
