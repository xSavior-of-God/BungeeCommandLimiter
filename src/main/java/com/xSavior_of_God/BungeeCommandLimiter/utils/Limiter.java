package com.xSavior_of_God.BungeeCommandLimiter.utils;

import com.xSavior_of_God.BungeeCommandLimiter.objects.BCLPlayer;

import java.util.HashMap;
import java.util.Map;

public class Limiter {
    private final Map<String, BCLPlayer> list;
    private final long arcOfTime;
    private final int max;

    /**
     * This class is used to manage the cooldown of the commands
     *
     * @param max       indicate the max of the command
     * @param arcOfTime indicate the arc of time(millis) in which the commands are counted
     */
    public Limiter(int max, long arcOfTime) {
        this.list = new HashMap<String, BCLPlayer>();
        this.max = max;
        this.arcOfTime = arcOfTime;
    }

    /**
     * This method is used to check if the player has reached the max of the command
     *
     * @param player indicate the player nickname to check
     * @return true if the player has reached the max of the command
     */
    public boolean checkPlayer(String player) {
        if (list.get(player) == null) {
            list.put(player, list.getOrDefault(player, new BCLPlayer(player, System.currentTimeMillis() + arcOfTime, 1)));
        } else {
            if (list.get(player).getTimeCheckerStartTime() >= System.currentTimeMillis()) {
                if (list.get(player).getTimeCheckerCounter() >= max) {
                    return true;
                } else {
                    list.get(player).addTimeCheckerCounter();
                }
            } else {
                list.get(player).setTimeCheckerReset(arcOfTime);
            }
        }
        return false;
    }

    public BCLPlayer getBCLPlayer(String player) {
        return list.get(player);
    }

    public void removePlayer(String player) {
        list.remove(player);
    }

}
