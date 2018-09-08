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

    public List<TimeoutEventListener> getTimeoutEventListeners() {
        return timeoutEventListeners;
    }

    public AcknowledgementTimer() {
        setTimer(new Timer());
        setAcknowledgementTimeOut(new AcknowledgementTimeOut(this));
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public AcknowledgementTimeOut getAcknowledgementTimeOut() {
        return acknowledgementTimeOut;
    }

    public void setAcknowledgementTimeOut(AcknowledgementTimeOut acknowledgementTimeOut) {
        this.acknowledgementTimeOut = acknowledgementTimeOut;
    }

    public void startAcknowledgementTimer(){
        timer.schedule(acknowledgementTimeOut,1000);
    }

    public void stopAcknowledgementTimer(){
        timer.cancel();
        timer.purge();
    }
}
