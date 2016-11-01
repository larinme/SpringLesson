package com.logging.impl;

import com.logging.Event;
import com.logging.EventLogger;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileLockInterruptionException;

public class FileEventLogger implements EventLogger {

    protected File logFile;

    public void init() throws FileLockInterruptionException {

        if (!logFile.canWrite()){

            throw new FileLockInterruptionException();
        }
    }

    public FileEventLogger(File file){

        this.logFile = file;
    }
    @Override
    public void logEvent(String message) {

        try {
            FileUtils.writeStringToFile(logFile, message, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void logEvent(Event event) {

        try {
            FileUtils.writeStringToFile(logFile, event.toString(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
