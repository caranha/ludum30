package org.castelodelego.ld30;

import java.util.Random;

/**
 * Created by caranha on 8/23/14.
 */
public class GPSRandom {

    private Random dice;

    public GPSRandom(double[] pos)
    {
        dice = new Random((long)(pos[0]*1000)*(long)(pos[1]*1000));
    }

    public int nextInt(int n)
    {
        return dice.nextInt(n);
    }

    public double nextDouble()
    {
        return dice.nextDouble();
    }
}
