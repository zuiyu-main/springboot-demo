package com.zuiyu.elk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ElkApplication {
    public static final Logger log = LoggerFactory.getLogger(ElkApplication.class);
    Random random = new Random(10000);

    public static void main(String[] args) {
        SpringApplication.run(ElkApplication.class, args);
        new ElkApplication().initTask();

    }

    private void initTask() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                log.info("seed info msg :" + random.nextInt(999999));
            }
        }, 100, 100, TimeUnit.MILLISECONDS);
    }

}
