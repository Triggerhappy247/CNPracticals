package Protocol;

//Only for testing
public class PhysicalLayerClient{

    public static void main(String args[])
    {
        PhysicalLayer client = new PhysicalLayer("127.0.0.1",800);
        Frame frame;
        while(true)
        {
            frame = client.fromPhysicalLayer();
            if(frame.getFrameType() == FrameType.STOP)
                break;
            System.out.println(frame.getSequenceNumber());
            frame.setAcknowledgmentNumber(frame.getSequenceNumber());
            client.toPhysicalLayer(frame);
        }
        client.stopPhysicalLayer();
    }
}
