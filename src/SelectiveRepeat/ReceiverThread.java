package SelectiveRepeat;

import Protocol.*;


public class ReceiverThread implements Runnable{

    private DataLinkLayer dataLinkLayer;
    private NetworkLayer networkLayer;
    private PhysicalLayer physicalLayer;
    public Thread thread;
    public ReceiverThread(DataLinkLayer dataLinkLayer) {
        thread = new Thread(this);
        this.dataLinkLayer = dataLinkLayer;
        networkLayer = dataLinkLayer.getNetworkLayer();
        physicalLayer = dataLinkLayer.getPhysicalLayer();
        thread.start();
    }

    @Override
    public void run() {
        while (true){
            Frame frame = physicalLayer.fromPhysicalLayer();
            System.out.println("Frame Arrival");
            if(frame.getFrameType() == FrameType.STOP){
                System.out.println("STOP Frame Received");
                networkLayer.toNetworkLayer(frame.getNetworkPacket());
                break;
            }
            if (frame.getFrameType() == FrameType.DATA) {
                if (frame.getSequenceNumber() != dataLinkLayer.frameExpected && dataLinkLayer.isNoNAK())
                    dataLinkLayer.sendFrame(FrameType.NAK, 0, dataLinkLayer.frameExpected, dataLinkLayer.outBound);
                if (DataLinkLayer.isBetween(dataLinkLayer.frameExpected, frame.getSequenceNumber(), dataLinkLayer.tooFar) && !dataLinkLayer.arrived[frame.getSequenceNumber() % DataLinkLayer.WINDOW_SIZE]) {
                    dataLinkLayer.arrived[frame.getSequenceNumber() % DataLinkLayer.WINDOW_SIZE] = true;
                    dataLinkLayer.inBound[frame.getSequenceNumber() % DataLinkLayer.WINDOW_SIZE] = frame.getNetworkPacket();
                    while (dataLinkLayer.arrived[dataLinkLayer.frameExpected % DataLinkLayer.WINDOW_SIZE]) {
                        networkLayer.toNetworkLayer(dataLinkLayer.inBound[dataLinkLayer.frameExpected % DataLinkLayer.WINDOW_SIZE]);
                        dataLinkLayer.setNoNAK(true);
                        dataLinkLayer.arrived[dataLinkLayer.frameExpected % DataLinkLayer.WINDOW_SIZE] = false;
                        dataLinkLayer.setFrameExpected(Protocol.increment(dataLinkLayer.frameExpected,DataLinkLayer.MAXIMUM_SEQUENCE));
                        dataLinkLayer.setTooFar(Protocol.increment(dataLinkLayer.tooFar,DataLinkLayer.MAXIMUM_SEQUENCE));
                        //Protocol.start_ack_timer();
                        dataLinkLayer.sendFrame(FrameType.ACK, 0, dataLinkLayer.frameExpected, dataLinkLayer.outBound);
                    }
                }
            }
            if((frame.getFrameType() == FrameType.NAK) && DataLinkLayer.isBetween(dataLinkLayer.acknowledgementExpected,(frame.getAcknowledgmentNumber()+1) % (DataLinkLayer.MAXIMUM_SEQUENCE + 1),dataLinkLayer.nextFrame))
                dataLinkLayer.sendFrame(FrameType.DATA,(frame.getAcknowledgmentNumber()+1) % (DataLinkLayer.MAXIMUM_SEQUENCE + 1),dataLinkLayer.frameExpected,dataLinkLayer.outBound);
            while (DataLinkLayer.isBetween(dataLinkLayer.acknowledgementExpected, frame.getAcknowledgmentNumber(), dataLinkLayer.nextFrame)) {
                dataLinkLayer.setBufferedNum(dataLinkLayer.getBufferedNum() - 1);
                //Protocol.stop_timer(dataLinkLayer.acknowledgementExpected % DataLinkLayer.WINDOW_SIZE);
                dataLinkLayer.setAcknowledgementExpected(Protocol.increment(dataLinkLayer.acknowledgementExpected,DataLinkLayer.MAXIMUM_SEQUENCE));
            }
            System.out.println("OnDecrement Buffered: " + dataLinkLayer.bufferedNum);
        }
        while (!networkLayer.isSendingDone()){
            Frame frame = physicalLayer.fromPhysicalLayer();
            if (DataLinkLayer.isBetween(dataLinkLayer.frameExpected, frame.getSequenceNumber(), dataLinkLayer.tooFar) && !dataLinkLayer.arrived[frame.getSequenceNumber() % DataLinkLayer.WINDOW_SIZE]) {

                while (dataLinkLayer.arrived[dataLinkLayer.frameExpected % DataLinkLayer.WINDOW_SIZE]) {
                    dataLinkLayer.setFrameExpected(Protocol.increment(dataLinkLayer.frameExpected,DataLinkLayer.MAXIMUM_SEQUENCE));
                    dataLinkLayer.setTooFar(Protocol.increment(dataLinkLayer.tooFar,DataLinkLayer.MAXIMUM_SEQUENCE));
                    //Protocol.start_ack_timer();
                    dataLinkLayer.sendFrame(FrameType.ACK, 0, dataLinkLayer.frameExpected, dataLinkLayer.outBound);
                }
            }
            while (DataLinkLayer.isBetween(dataLinkLayer.acknowledgementExpected, frame.getAcknowledgmentNumber(), dataLinkLayer.nextFrame)) {
                dataLinkLayer.setBufferedNum(dataLinkLayer.getBufferedNum() - 1);
            //Protocol.stop_timer(dataLinkLayer.acknowledgementExpected % DataLinkLayer.WINDOW_SIZE);
                dataLinkLayer.setAcknowledgementExpected(Protocol.increment(dataLinkLayer.acknowledgementExpected,DataLinkLayer.MAXIMUM_SEQUENCE));
            }
            System.out.println("OnDecrement Buffered: " + dataLinkLayer.bufferedNum);
        }
    }
}
