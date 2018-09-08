package Protocol;

//Only for testing
public class PhysicalLayerServer {

    public static void main(String args[])
    {
        PhysicalLayer server = new PhysicalLayer(800);
        Frame frame = new Frame(new NetworkPacket(0),1,0,FrameType.DATA);
        server.toPhysicalLayer(frame);
        server.setCommunicationDone(true);
    }

}
