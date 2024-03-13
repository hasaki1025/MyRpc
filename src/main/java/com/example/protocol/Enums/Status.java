package com.example.protocol.Enums;

public enum Status {
    OK(0),Error(1);
    private final int value;

    Status(int i) {
        this.value=i;
    }

    public int getValue() {
        return value;
    }

    private final static Status[] statuses={OK,Error};

    public static Status forInt(int i)
    {
        return statuses[i];
    }

}
