package SelectiveRepeat;

import Protocol.FrameType;
import Protocol.NetworkLayer;
import Protocol.Protocol;

import java.io.IOException;

public class SenderThread implements Runnable {

    private DataLinkLayer dataLinkLayer;
    private NetworkLayer networkLayer;
    public Thread thread;
    public SenderThread(DataLinkLayer dataLinkLayer) {
        thread = new Thread(this);
        this.dataLinkLayer = dataLinkLayer;
        networkLayer = dataLinkLayer.getNetworkLayer();
        thread.start();
    }

    @Override
    public void run() {
        try {
            while (true){
                if (dataLinkLayer.getBufferedNum() < DataLinkLayer.WINDOW_SIZE){
                    System.out.println("Network Layer Ready");
                    dataLinkLayer.setBufferedNum(dataLinkLayer.getBufferedNum() + 1);
                    dataLinkLayer.outBound[dataLinkLayer.nextFrame % DataLinkLayer.WINDOW_SIZE] = networkLayer.fromNetworkLayer();
                    if(networkLayer.isSendingDone()) {
                        System.out.println("STOP Frame Sending");
                        dataLinkLayer.sendFrame(FrameType.STOP, dataLinkLayer.nextFrame,dataLinkLayer.frameExpected, dataLinkLayer.outBound);
                        dataLinkLayer.setNextFrame(Protocol.increment(dataLinkLayer.nextFrame,DataLinkLayer.MAXIMUM_SEQUENCE));
                        break;
                    }
                    dataLinkLayer.sendFrame(FrameType.DATA,dataLinkLayer.nextFrame,dataLinkLayer.frameExpected,dataLinkLayer.outBound);
                    dataLinkLayer.setNextFrame(Protocol.increment(dataLinkLayer.nextFrame,DataLinkLayer.MAXIMUM_SEQUENCE));
                    System.out.println("OnIncrement Buffered: " + dataLinkLayer.bufferedNum);
                }
                else{
                    Thread.yield();
                }

            }
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }
}
