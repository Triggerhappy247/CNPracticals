package Protocol;

import java.io.IOException;

public class PhysicalLayerThread implements Runnable {

    private PhysicalLayer physicalLayer;
    private Thread thread;

    public PhysicalLayerThread(PhysicalLayer physicalLayer) {
        setPhysicalLayer(physicalLayer);
        thread = new Thread(this,"PhysicalLayerThread");
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
