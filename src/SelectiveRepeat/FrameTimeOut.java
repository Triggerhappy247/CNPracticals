package SelectiveRepeat;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class FrameTimeOut extends TimerTask {

    private List<TimeoutEventListener> timeoutEventListeners = new ArrayList<>();

    public void addFrameTimerListener(TimeoutEventListener timeoutEventListener){
        timeoutEventListeners.add(timeoutEventListener);
    }

    @Override
    public void run() {
        for (TimeoutEventListener timeoutEventListener : timeoutEventListeners)
            timeoutEventListener.onTimeout(TimeoutEventListener.FRAME_TIMEOUT);
    }
}
