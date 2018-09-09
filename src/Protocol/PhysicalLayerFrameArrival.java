package Protocol;

import java.io.BufferedInputStream;
import java.io.IOException;

public class PhysicalLayerFrameArrival implements Runnable{

    private PhysicalLayer physicalLayer;

    public PhysicalLayerFrameArrival(PhysicalLayer physicalLayer) {
        this.physicalLayer = physicalLayer;
        new Thread(this).start();
    }

    @Override
    public void run() {
        BufferedInputStream bufferedInputStream = physicalLayer.getBufferedInputStream();
        System.out.println("Thread Running");
        try {
            while (!physicalLayer.isStopFrameArrival()){
                if(bufferedInputStream.available() >= 1056)
                    for (FrameArrivalListener frameArrivalListener : physicalLayer.getFrameArrivalListeners()){
                        frameArrivalListener.onFrameArrival();
                    }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
