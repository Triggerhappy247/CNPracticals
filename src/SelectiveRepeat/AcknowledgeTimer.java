package SelectiveRepeat;

import java.util.Timer;

public class AcknowledgeTimer {

    private Timer timer;
    private AcknowledgementTimeOut acknowledgementTimeOut;

    public AcknowledgeTimer() {
        setTimer(new Timer());
        setAcknowledgementTimeOut(new AcknowledgementTimeOut());
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
