package org.castelodelego.ld30;

/**
 * Created by caranha on 8/23/14.
 */
public interface LocationServer {

    /**
     * Makes a best effort to obtain the location. In case it is not possible to obtain the location for some
     * reason, the last obtained location (or a random location) will be served.
     *
     * @return longitude and latitude;
     */
    double[] getLocation();

    String getLocationString();

    void pauseService();
    void restartService();

}
