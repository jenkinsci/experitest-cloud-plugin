package com.experitest.plugin;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.*;

public class Log {
    private transient StreamHandler streamHandler;
    private transient Logger logger;
    private transient Class clazz;

    private Log(Class clazz) {
        this.clazz = clazz;
        this.logger = Logger.getLogger(this.clazz.getName());
    }

    public static Log get(Class clazz) {
        return new Log(clazz);
    }

    public static Log get(Class clazz, PrintStream out) {
        Log log = get(clazz);

        log.streamHandler = new StreamHandler(out, new SimpleFormatter());
        log.logger.addHandler(log.streamHandler);

        return log;
    }

    public void error(final String message, final Object... args) {
        write(Level.SEVERE, message, args);
    }

    public void info(final String message, final Object... args) {
        write(Level.INFO, message, args);
    }

    public void debug(final String message, final Object... args) {
        write(Level.CONFIG, message, args);
    }

    private void write(final Level level, String message, Object... args) {
//        StringBuilder source = new StringBuilder();

        args = ArrayUtils.insert(0, args, this.clazz.getSimpleName());
        String msg = String.format("[%s] " + message, args);
        if (level == Level.CONFIG) {
            msg = "[DEBUG] " + msg;
        } else if (level == Level.SEVERE) {
            msg = "[ERROR] " + msg;
        }

        this.logger.logp(level, "", "", msg);//we dont need to write source-class, method..
        if (this.streamHandler != null) {
            this.streamHandler.flush();
        }
    }

    public Logger getLogger() {
        return logger;
    }
}
