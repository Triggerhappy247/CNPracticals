package Protocol;

//Only for testing
public class PhysicalLayerServer {

    public static void main(String args[])
    {
        PhysicalLayer server = new PhysicalLayer(800);
        Frame frame;
        for (int i = 0; i < 10; i++) {
            frame = new Frame(new NetworkPacket(),i,0,FrameType.DATA);
            server.toPhysicalLayer(frame);
            frame = server.fromPhysicalLayer();
            System.out.println("Acknowledge: " + frame.getAcknowledgmentNumber());
        }
        frame = new Frame(new NetworkPacket(),0,0,FrameType.STOP);
        server.toPhysicalLayer(frame);
        server.stopPhysicalLayer();;
    }

}
