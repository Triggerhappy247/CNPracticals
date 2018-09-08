package Protocol;

//Only for testing
public class PhysicalLayerClient{

    public static void main(String args[])
    {
        PhysicalLayer client = new PhysicalLayer("127.0.0.1",800);
        Frame frame = client.fromPhysicalLayer();
        System.out.println(frame.getSequenceNumber());
        client.setCommunicationDone(true);
    }

}
