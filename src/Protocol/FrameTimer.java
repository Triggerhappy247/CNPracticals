package Protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class FrameTimer {
    private Timer frameTimer;
    private FrameTimeOut frameTimeOut;
    private List<TimeoutEventListener> timeoutEventListeners = new ArrayList<>();

    public void addFrameTimerListener(TimeoutEventListener timeoutEventListener){
        timeoutEventListeners.add(timeoutEventListener);
    }

    public List<TimeoutEventListener> getTimeoutEventListeners() {
        return timeoutEventListeners;
    }

    public FrameTimer() {
        setFrameTimer(new Timer());
        setFrameTimeOut(new FrameTimeOut(this));
    }

    public Timer getFrameTimer() {
        return frameTimer;
    }

    public void setFrameTimer(Timer frameTimer) {
        this.frameTimer = frameTimer;
    }

    public FrameTimeOut getFrameTimeOut() {
        return frameTimeOut;
    }

    public void setFrameTimeOut(FrameTimeOut frameTimeOut) {
        this.frameTimeOut = frameTimeOut;
    }

    public void startFrameTimer(){
        frameTimer.schedule(getFrameTimeOut(),1000);
    }

    public void stopFrameTimer(){
        frameTimer.cancel();
        frameTimer.purge();
    }
}
