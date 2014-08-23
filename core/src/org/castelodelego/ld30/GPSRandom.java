package org.castelodelego.ld30;

import java.util.Random;

/**
 * Created by caranha on 8/23/14.
 */
public class GPSRandom {

    private Random dice;

    public GPSRandom()
    {
        dice = new Random();
    }

    public void reset(double lon, double lat)
    {
        dice.setSeed((long)(lon*1000)*(long)(lat*1000));
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
