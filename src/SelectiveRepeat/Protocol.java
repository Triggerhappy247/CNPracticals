package SelectiveRepeat;


public class Protocol {
    public static final int MAX_PKT_SIZE = 1024;
    public static final AcknowledgeTimer ACKNOWLEDGE_TIMER = new AcknowledgeTimer();
    public static final FrameTimer FRAME_TIMER = new FrameTimer();

    public static int increment(int sequenceNumber){
        return (sequenceNumber+1) % DataLinkLayer.MAXIMUM_SEQUENCE;
    }

    public static void start_timer(int sequenceNumber){
        FRAME_TIMER.startFrameTimer(sequenceNumber);
    }

    public static void stop_timer(int sequnceNumber){
        FRAME_TIMER.stopFrameTimer(sequnceNumber);
    }

    public static void start_ack_timer(){
        ACKNOWLEDGE_TIMER.startAcknowledgementTimer();
    }

    public static void stop_ack_timer(){
        ACKNOWLEDGE_TIMER.stopAcknowledgementTimer();
    }
}
