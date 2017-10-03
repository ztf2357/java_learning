package com.beyondhost.week4;

public class DoubleCheckSingleton {

    private static DoubleCheckSingleton _instance = null;

    private DoubleCheckSingleton()
    {
    }

    static DoubleCheckSingleton getInstance()
    {
        if(_instance==null) {
            synchronized (DoubleCheckSingleton.class) {
                if(_instance==null) {
                    _instance = new DoubleCheckSingleton();
                }
            }
        }
        return _instance;
    }
}
