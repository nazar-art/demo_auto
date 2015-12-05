package com.epam.core.common;

import java.util.Date;

/**
 * @author Nazar_Lelyak.
 */
public class Clock {
    private static long time;

    private Clock() {
    }

    public static Date getCurrentDate() {
        return new Date(getTimeMillis());
    }

    public static long getTimeMillis() {
        return (time == 0 ? System.currentTimeMillis() : time);
    }

    public static void setTimeMillis(long millis) {
        Clock.time = millis;
    }

    public static void resetTime() {
        Clock.time = 0;
    }


    public static void main(String[] args) {
//        setTimeMillis(12586898899999L);

        System.out.println(getCurrentDate());
        System.out.println(getTimeMillis());
    }
}
