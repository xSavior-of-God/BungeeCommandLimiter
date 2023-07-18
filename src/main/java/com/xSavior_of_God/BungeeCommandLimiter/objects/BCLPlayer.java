package com.xSavior_of_God.BungeeCommandLimiter.objects;

public class BCLPlayer {
    private String name;
    private long timeCheckerStartTime;
    private int timeCheckerCounter;

    public BCLPlayer(String name, long timeCheckerStartTime, int timeCheckerCounter) {
        this.name = name;
        this.timeCheckerStartTime = timeCheckerStartTime;
        this.timeCheckerCounter = timeCheckerCounter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimeCheckerStartTime() {
        return timeCheckerStartTime;
    }

    public void setTimeCheckerStartTime(long timeCheckerStartTime) {
        this.timeCheckerStartTime = timeCheckerStartTime;
    }

    public int getTimeCheckerCounter() {
        return timeCheckerCounter;
    }

    public void setTimeCheckerCounter(int timeCheckerCounter) {
        this.timeCheckerCounter = timeCheckerCounter;
    }

    public void addTimeCheckerCounter() {
        this.timeCheckerCounter++;
    }

    public void setTimeCheckerReset(long arcOfTime) {
        this.timeCheckerCounter = 1;
        this.timeCheckerStartTime = System.currentTimeMillis() + arcOfTime;
    }

    @Override
    public String toString() {
        return "BCLPlayer{" +
                "name='" + name + '\'' +
                ", timeCheckerStartTime=" + timeCheckerStartTime +
                ", timeCheckerCounter=" + timeCheckerCounter +
                '}';
    }
}
