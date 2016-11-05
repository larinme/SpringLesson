package com.logging.impl;

import com.logging.Event;
import com.logging.EventLogger;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileLockInterruptionException;

@Component
public class FileEventLogger implements EventLogger {

    protected File logFile;
    @Value("${logFile}")
    private String filePath;

    @PostConstruct
    public void init() throws IOException {
        logFile = new File(filePath);
        if (logFile.exists() && !logFile.canWrite()) {
            throw new IllegalArgumentException(
                    "Can't write to file " + filePath);
        } else if (!logFile.exists()) {
            logFile.createNewFile();
        }

    }
    public FileEventLogger(){

    }

    public FileEventLogger(File file){

        this.logFile = file;
    }

    @Override
    public void logEvent(Event event) {

        try {
            FileUtils.writeStringToFile(logFile, event.toString() + "\n", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
