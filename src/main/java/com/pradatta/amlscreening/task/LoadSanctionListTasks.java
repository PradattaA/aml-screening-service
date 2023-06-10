package com.pradatta.amlscreening.task;


import com.pradatta.amlscreening.service.SanctionEntityLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class LoadSanctionListTasks {
    private static final Logger log = LoggerFactory.getLogger(LoadSanctionListTasks.class);

    @Autowired
    private SanctionEntityLoader sanctionEntityLoader;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * This a cron job that should run every day to automatically load Sanctioned List
     * For now it's just set to run every minute and log.
     * Will implement loading logic by downloading it from internet and then load it in to database
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void reportCurrentTime() {
        log.info("Loading the sanction lists at {}", dateFormat.format(new Date()));
        try {
            sanctionEntityLoader.loadSanctionFile();
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
        }
    }
}
