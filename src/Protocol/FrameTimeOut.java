package Protocol;

import java.util.TimerTask;

public class FrameTimeOut extends TimerTask {

    private FrameTimer frameTimer;
    private int sequence;

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public void setFrameTimer(FrameTimer frameTimer) {
        this.frameTimer = frameTimer;
    }

    public FrameTimeOut(FrameTimer frameTimer) {
        this.frameTimer = frameTimer;
    }

    @Override
    public void run() {
        for (TimeoutEventListener timeoutEventListener : frameTimer.getTimeoutEventListeners()) {
            timeoutEventListener.onFrameTimeout();
            frameTimer.stopFrameTimer();
        }
    }
}
