package Protocol;

import java.util.TimerTask;

public class AcknowledgementTimeOut extends TimerTask {

    private AcknowledgementTimer acknowledgementTimer;


    public AcknowledgementTimeOut(AcknowledgementTimer acknowledgementTimer) {
        this.acknowledgementTimer = acknowledgementTimer;
    }

    @Override
    public void run() {
        for(TimeoutEventListener timeoutEventListener : acknowledgementTimer.getTimeoutEventListeners()) {
            timeoutEventListener.onAcknowledgementTimeout();
            acknowledgementTimer.stopAcknowledgementTimer();
        }
    }
}
