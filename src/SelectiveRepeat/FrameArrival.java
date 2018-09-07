package SelectiveRepeat;

import java.io.IOException;

public class FrameArrival implements Runnable {

    private PhysicalLayer physicalLayer;
    private Thread thread;

    public FrameArrival(PhysicalLayer physicalLayer) {
        setPhysicalLayer(physicalLayer);
        thread = new Thread(this,"FrameArrival");
        thread.start();
    }

    public void setPhysicalLayer(PhysicalLayer physicalLayer) {
        this.physicalLayer = physicalLayer;
    }

    @Override
    public void run() {
        Frame frame;
        while (!physicalLayer.isCommunicationDone())
        {
            try {
                frame = (Frame) physicalLayer.getObjectInputStream().readObject();
                if(frame != null)
                    physicalLayer.setFrame(frame);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
