package com.shutdownsforcityelf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShutdownsApplication {

  public static void main(String[] args) {
    SpringApplication.run(new Object[]{ShutdownsApplication.class, ScheduledTasks.class}, args);
  }
}