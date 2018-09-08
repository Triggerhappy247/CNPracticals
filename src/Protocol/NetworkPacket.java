package Protocol;

import java.io.Serializable;


public class NetworkPacket implements Serializable {
    public static final int MAX_PKT_SIZE = 1024;
    private byte data[];

    public NetworkPacket(int size) {
        setData(new byte[size]);
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
