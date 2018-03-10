package com.demo.utils;

public class MathTools {

    public static int floor(int numberA, double numberB) {
        return (int) Math.floor(numberA / numberB);
    }

    public static int floor(int numberA, double numberB, int defaultValue) {
        if (numberB == 0) {
            return defaultValue;
        }
        return (int) Math.floor(numberA / numberB);
    }

    public static long floor(long numberA, double numberB) {
        return (long) Math.floor(numberA / numberB);
    }

    public static long floor(long numberA, double numberB, long defaultValue) {
        if (numberB == 0) {
            return defaultValue;
        }
        return (long) Math.floor(numberA / numberB);
    }

    public static int ceil(int numberA, double numberB) {
        return (int) Math.ceil(numberA / numberB);
    }

    public static int ceil(int numberA, double numberB, int defaultValue) {
        if (numberB == 0) {
            return defaultValue;
        }
        return (int) Math.ceil(numberA / numberB);
    }

    public static long ceil(long numberA, double numberB) {
        return (long) Math.ceil(numberA / numberB);
    }

    public static long ceil(long numberA, double numberB, long defaultValue) {
        if (numberB == 0) {
            return defaultValue;
        }
        return (long) Math.ceil(numberA / numberB);
    }

    public static long toPositive(long number) {
        return (number >= 0 ? number : 0);
    }

    public static int toPositive(int number) {
        return (number >= 0 ? number : 0);
    }

    public static float toPositive(float number) {
        return (number >= 0 ? number : 0);
    }

    public static float toNotNullValue(Float number, float defaultValue) {
        return (number != null ? number : defaultValue);
    }

    public static float toNotNullValue(Float number) {
        return toNotNullValue(number, 0f);
    }

    public static double toNotNullValue(Double number, double defaultValue) {
        return (number != null ? number : defaultValue);
    }

    public static double toNotNullValue(Double number) {
        return toNotNullValue(number, 0d);
    }

    public static int toNotNullValue(Integer number, int defaultValue) {
        return (number != null ? number : defaultValue);
    }

    public static int toNotNullValue(Integer number) {
        return toNotNullValue(number, 0);
    }

    public static long toNotNullValue(Long number, long defaultValue) {
        return (number != null ? number : defaultValue);
    }

    public static long toNotNullValue(Long number) {
        return toNotNullValue(number, 0L);
    }

    public static float toPercentage(long loaded, long total, float defaultValue) {
        return total != 0 ? (float) ((double) loaded / total * 100) : defaultValue;
    }

    public static Integer parseInt(String str, Integer defaultValue) {
        try {
            return Integer.valueOf(str);
        } catch (Exception ex) { // ignore ex
        }
        return defaultValue;
    }

    public static int parseInt(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception ex) { // ignore ex
        }
        return defaultValue;
    }

    public static float parseInt(String str, float defaultValue) {
        try {
            return Float.parseFloat(str);
        } catch (Exception ex) { // ignore ex
        }
        return defaultValue;
    }

    public static Float parseFloat(String str, Float defaultValue) {
        try {
            return Float.valueOf(str);
        } catch (Exception ex) { // ignore ex
        }
        return defaultValue;
    }

    public static long parseLong(String str, long defaultValue) {
        try {
            return Long.parseLong(str);
        } catch (Exception ex) { // ignore ex
        }
        return defaultValue;
    }

    public static boolean equals(Integer number, Integer query) {
        return number != null && number.equals(query);
    }

}