package com.zeromb.gotostock.util;

/**
 * @author Mr_Told
 */
public class TransactionTable {

    double[] keys;
    int[] values;
    int c_pos, pos;

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
        c_pos = pos;
    }

    public double[] getKeys() {
        return keys;
    }

    public int[] getValues() {
        return values;
    }

    public int getC_pos() {
        return c_pos;
    }

    public int getPos() {
        return pos;
    }
}
