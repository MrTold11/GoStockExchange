package com.zeromb.gotostock.util;

/**
 * @author Mr_Told
 */
public class TransactionTable {

    double[] keys;
    int[] values;
    int a_pos, b_pos, pos;

    public TransactionTable(int size) {
        keys = new double[size];
        values = new int[size];
    }

    public void add(double d, int i) {
        if (d == keys[pos]) {
            values[pos] += i;
            return;
        }

        pos++;
        keys[i] = d;
        values[i] = i;
    }

    public void commit() {
        pos++;
        a_pos = b_pos;
        b_pos = pos;
    }

    public double[] getKeys() {
        return keys;
    }

    public int[] getValues() {
        return values;
    }

    public int getA_pos() {
        return a_pos;
    }

    public int getB_pos() {
        return b_pos;
    }

    public int getPos() {
        return pos;
    }
}
