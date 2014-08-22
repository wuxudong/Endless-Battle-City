package com.wupipi.tankwar.model;

public enum ScoreNumber {
    _100(100), _200(200), _400(400), _500(500);

    private int value;

    private ScoreNumber(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}