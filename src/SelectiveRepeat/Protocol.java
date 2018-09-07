package SelectiveRepeat;


public class Protocol {
    final static int MAX_PKT_SIZE = 1024;

    public static int increment(int sequenceNumber){
        return (sequenceNumber+1) % DataLinkLayer.MAXIMUM_SEQUENCE;
    }
}
