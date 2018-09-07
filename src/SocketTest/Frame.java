package SocketTest;

import java.io.Serializable;

public class Frame implements Serializable {
    private String stringData;
    private int intData;

    public Frame(String stringData, int intData) {
        setStringData(stringData);
        setIntData(intData);
    }

    public String getStringData() {
        return stringData;
    }

    public void setStringData(String stringData) {
        this.stringData = stringData;
    }

    public int getIntData() {
        return intData;
    }

    public void setIntData(int intData) {
        this.intData = intData;
    }
}
