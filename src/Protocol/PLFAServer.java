package Protocol;

public class PLFAServer implements FrameArrivalListener {

    PhysicalLayer server;
    public static void main(String args[])
    {
        PLFAServer plfaServer = new PLFAServer();
        plfaServer.server = new PhysicalLayer(800);
        plfaServer.server.addFrameArrivalListeners(plfaServer);
        Frame frame;
        for (int i = 0; i < 10; i++) {
            frame = new Frame(new NetworkPacket(),i,0,FrameType.DATA);
            plfaServer.server.toPhysicalLayer(frame);
        }
        frame = new Frame(new NetworkPacket(),0,0,FrameType.STOP);
        plfaServer.server.toPhysicalLayer(frame);
        plfaServer.server.stopPhysicalLayer();
    }

    @Override
    public void onFrameArrival() {
        Frame frame;
        frame = server.fromPhysicalLayer();
        System.out.println("Acknowledge: " + frame.getAcknowledgmentNumber());
    }
}
