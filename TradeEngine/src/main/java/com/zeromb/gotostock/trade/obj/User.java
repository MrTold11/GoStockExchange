package com.zeromb.gotostock.trade.obj;

/**
 * @author Mr_Told
 */
public class User {

    final int UID;

    volatile double coins;

    volatile double reserved;

    public User(int uid) {
        UID = uid;
    }

    public User(String uid) {
        this(Integer.parseInt(uid));
    }

}
