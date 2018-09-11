package Protocol;

import java.util.*;

public class FrameTimer {
    private Map<Integer,Timer> frameTimerMap = new HashMap<>();
    private List<TimeoutEventListener> timeoutEventListeners = new ArrayList<>();

    public void addFrameTimerListener(TimeoutEventListener timeoutEventListener){
        timeoutEventListeners.add(timeoutEventListener);
    }

    public void removeFrameTimerListener(TimeoutEventListener timeoutEventListener){
        timeoutEventListeners.remove(timeoutEventListener);
    }

    public List<TimeoutEventListener> getTimeoutEventListeners() {
        return timeoutEventListeners;
    }

    public void startFrameTimer(int sequenceNumber){
        if(frameTimerMap.containsKey(sequenceNumber)){
            frameTimerMap.get(sequenceNumber).cancel();
        }
        Timer frameTimer = new Timer(true);
        frameTimerMap.put(sequenceNumber,frameTimer);
        System.out.println("Timer Started for " + sequenceNumber);
        frameTimer.schedule(new FrameTimeOut(this),5000);
    }

    public void stopFrameTimer(int sequenceNumber){
        if(frameTimerMap.containsKey(sequenceNumber)){
            System.out.println("Timer stopped for " + sequenceNumber);
            Timer frameTimer = frameTimerMap.get(sequenceNumber);
            frameTimer.cancel();
            frameTimerMap.remove(sequenceNumber);
        }
    }
}
