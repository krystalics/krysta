package com.krysta.ioc.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Krysta on 2019/8/22.
 *
 * @since ioc1.0
 */
public enum KrystaLogger {
    INSTANCE;
    private final String MSGNAME = "Krysta";

    public void info(String msg) {
        Logger.getLogger(MSGNAME).info(msg);
    }

    public void debug(String msg) {
        Logger.getLogger(MSGNAME).log(Level.WARNING, msg);
    }

    public void error(String msg) {
        Logger.getLogger(MSGNAME).log(Level.SEVERE, msg);
    }

}
