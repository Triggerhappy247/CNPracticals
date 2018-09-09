package Protocol;

import java.io.IOException;
import java.util.TimerTask;

public class CloseReceiverTask extends TimerTask {

    NetworkLayer networkLayer;

    public CloseReceiverTask(NetworkLayer networkLayer) {
        this.networkLayer = networkLayer;
    }

    @Override
    public void run() {
        try {
            System.out.println("Closing Network Stream - Timeout");
            networkLayer.getFileOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
