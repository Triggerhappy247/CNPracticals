package SelectiveRepeat;

import java.io.Serializable;


public class NetworkPacket implements Serializable {
    private byte data[];

    public NetworkPacket() {
        setData(new byte[Protocol.MAX_PKT_SIZE]);
    }

    public NetworkPacket(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
