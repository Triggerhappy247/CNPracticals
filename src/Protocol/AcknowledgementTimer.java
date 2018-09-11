package Protocol;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class AcknowledgementTimer {

    private Timer timer;
    private AcknowledgementTimeOut acknowledgementTimeOut;
    private List<TimeoutEventListener> timeoutEventListeners = new ArrayList<>();

    public void addAcknowledgementTimerListener(TimeoutEventListener timeoutEventListener){
        timeoutEventListeners.add(timeoutEventListener);
    }

    public void removeAcknowledgementTimerListener(TimeoutEventListener timeoutEventListener){
        timeoutEventListeners.remove(timeoutEventListener);
    }

    public List<TimeoutEventListener> getTimeoutEventListeners() {
        return timeoutEventListeners;
    }


    public void startAcknowledgementTimer(){
        if(timer == null)
        {
            Timer timer = new Timer();
            acknowledgementTimeOut = new AcknowledgementTimeOut(this);
            System.out.println("Acknowledgement Timer Started");
            timer.schedule(acknowledgementTimeOut,500);
        }
    }

    public void stopAcknowledgementTimer(){
        if(timer != null) {
            System.out.println("Acknowledgment Timer Stopped");
            timer.cancel();
            timer = null;
        }
    }
}
