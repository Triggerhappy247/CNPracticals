package Protocol;

public class PLFAClient implements FrameArrivalListener{
    PhysicalLayer client;
    public static void main(String args[])
    {
        PLFAClient plfaClient = new PLFAClient();
        plfaClient.client = new PhysicalLayer("127.0.0.1",800);
        plfaClient.client.addFrameArrivalListeners(plfaClient);
    }

    @Override
    public void onFrameArrival() {
        Frame frame;
        frame = client.fromPhysicalLayer();
        if(frame.getFrameType() == FrameType.STOP) {
            client.stopPhysicalLayer();
            return;
        }
        System.out.println(frame.getSequenceNumber());
        frame.setAcknowledgmentNumber(frame.getSequenceNumber());
        client.toPhysicalLayer(frame);
    }
}
