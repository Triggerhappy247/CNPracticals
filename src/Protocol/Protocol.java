package Protocol;


import SelectiveRepeat.DataLinkLayer;

public class Protocol {
    public static AcknowledgementTimer ACKNOWLEDGE_TIMER = new AcknowledgementTimer();
    public static FrameTimer FRAME_TIMER = new FrameTimer();

    public static int increment(int sequenceNumber){
        return (sequenceNumber+1) % DataLinkLayer.MAXIMUM_SEQUENCE;
    }

    public static void start_timer(){
        FRAME_TIMER.startFrameTimer();
    }

    public static void stop_timer(){
        FRAME_TIMER.stopFrameTimer();
    }

    public static void start_ack_timer(){
        ACKNOWLEDGE_TIMER.startAcknowledgementTimer();
    }

    public static void stop_ack_timer(){
        ACKNOWLEDGE_TIMER.stopAcknowledgementTimer();
    }

    //Testing the Timers
    public static void main(String args[]){
        System.out.println("Testing timers");

        class Test extends TimeoutEventAdapter{
            @Override
            public void onFrameTimeout() {
                System.out.println("Frame Timed Out");
            }

            @Override
            public void onAcknowledgementTimeout() {
                System.out.println("Acknowledgement Timed Out");
            }
        }

        Test test = new Test();
        FRAME_TIMER.addFrameTimerListener(test);
        ACKNOWLEDGE_TIMER.addAcknowledgementTimerListener(test);

        System.out.println("Starting Timers");
        start_timer();
        start_ack_timer();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stop_timer();
        stop_ack_timer();

        System.out.println("Ending Timers");
    }
}
