package SelectiveRepeat;


import java.util.ArrayList;
import java.util.Timer;

public class FrameTimer {
    private ArrayList<Timer> timerArrayList;
    private FrameTimeOut frameTimeOut;

    public FrameTimer() {
        setTimerArrayList(new ArrayList<>());
        setFrameTimeOut(new FrameTimeOut());
    }

    public ArrayList<Timer> getTimerArrayList() {
        return timerArrayList;
    }

    public void setTimerArrayList(ArrayList<Timer> timerArrayList) {
        this.timerArrayList = timerArrayList;
    }

    public FrameTimeOut getFrameTimeOut() {
        return frameTimeOut;
    }

    public void setFrameTimeOut(FrameTimeOut frameTimeOut) {
        this.frameTimeOut = frameTimeOut;
    }

    public void startFrameTimer(int sequenceNumber){
        timerArrayList.add(sequenceNumber,new Timer(String.valueOf(sequenceNumber)));
        timerArrayList.get(sequenceNumber).schedule(getFrameTimeOut(),1000);
    }

    public void stopFrameTimer(int sequenceNumber){
        Timer timer = timerArrayList.get(sequenceNumber);
        timer.cancel();
        timer.purge();
    }
}
