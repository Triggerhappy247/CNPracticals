package SelectiveRepeat;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class AcknowledgementTimeOut extends TimerTask {

    private List<TimeoutEventListener> timeoutEventListeners = new ArrayList<>();

    public void addAcknowledgementTimerListener(TimeoutEventListener timeoutEventListener){
        timeoutEventListeners.add(timeoutEventListener);
    }

    @Override
    public void run() {
        for(TimeoutEventListener timeoutEventListener : timeoutEventListeners)
                timeoutEventListener.onTimeout(TimeoutEventListener.ACKNOWLEDGMENT_TIMEOUT);
    }
}
