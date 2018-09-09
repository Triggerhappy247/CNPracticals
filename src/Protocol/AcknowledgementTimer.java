package Protocol;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

public class AcknowledgementTimer {

    private LinkedList<Timer> timerList = new LinkedList<>();
    private AcknowledgementTimeOut acknowledgementTimeOut;
    private List<TimeoutEventListener> timeoutEventListeners = new ArrayList<>();

    public void addAcknowledgementTimerListener(TimeoutEventListener timeoutEventListener){
        timeoutEventListeners.add(timeoutEventListener);
    }

    public List<TimeoutEventListener> getTimeoutEventListeners() {
        return timeoutEventListeners;
    }


    public void startAcknowledgementTimer(){
        Timer timer = new Timer();
        timerList.add(timer);
        acknowledgementTimeOut = new AcknowledgementTimeOut(this);
        timer.schedule(acknowledgementTimeOut,1000);
    }

    public void stopAcknowledgementTimer(){
        if(!timerList.isEmpty())
        {
            timerList.removeFirst().cancel();
        }
    }
}
