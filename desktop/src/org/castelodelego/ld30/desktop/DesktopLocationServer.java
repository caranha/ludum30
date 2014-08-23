package org.castelodelego.ld30.desktop;

import org.castelodelego.ld30.LocationServer;
import java.util.Random;

/**
 * Serves the location. In the Desktop case, two random doubles are served.
 *
 * Created by caranha on 8/23/14.
 */
public class DesktopLocationServer implements LocationServer {

    Random dice;

    DesktopLocationServer()
    {
        dice = new Random();
    }

    @Override
    public double[] getLocation() {
        double[] ret = new double[2];

        ret[0] = dice.nextDouble()*180;
        ret[1] = dice.nextDouble()*90;

        return ret;
    }

    @Override
    public String getLocationString() {
        return "Desktop Play! Location is Randomized.";
    }

    @Override
    public void pauseService() {
    }

    @Override
    public void restartService() {
    }
}
