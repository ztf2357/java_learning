package com.beyondhost.week4;

public class EnumSingleton {

    private EnumSingleton()
    {
    }

    static EnumSingleton getInstance()
    {
        return EnumForSingleton.INSTANCE.getInstance();
    }

    private enum EnumForSingleton {
        INSTANCE;
        private EnumSingleton instance;
        EnumForSingleton() {
            instance = new EnumSingleton();
        }

        public EnumSingleton getInstance()
        {
            return instance;
        }
    }
}


