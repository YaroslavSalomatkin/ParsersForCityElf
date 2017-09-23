package com.shutdownsforcityelf;

import com.shutdownsforcityelf.repository.ElectricityForecastRepository;
import com.shutdownsforcityelf.repository.FirebaseNotificationRepository;
import com.shutdownsforcityelf.repository.GasForecastRepository;
import com.shutdownsforcityelf.repository.WaterForecastRepository;
import com.shutdownsforcityelf.service.DataCollectorService;
import com.shutdownsforcityelf.service.FirebaseNotificationService;
import com.shutdownsforcityelf.utils.FormatDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.time.LocalDateTime;
import java.time.ZoneId;

@EnableScheduling
public class ScheduledTasks {

  private final int hours = 24;

  @Autowired
  private FirebaseNotificationService firebaseNotificationService;
  @Autowired
  private DataCollectorService dataCollectorService;
  @Autowired
  private WaterForecastRepository waterForecastRepository;
  @Autowired
  private ElectricityForecastRepository electricityForecastRepository;
  @Autowired
  private GasForecastRepository gasForecastRepository;
  @Autowired
  private FormatDateTime formatDateTime;
  @Autowired
  private FirebaseNotificationRepository firebaseNotificationRepository;

  @Scheduled(initialDelay = 10000, fixedRate = 1800000)
  public void startCollector() {
    System.out
        .println("\nSTART " + formatDateTime.dateTimeFormat(LocalDateTime.now(ZoneId.of("UTC+3"))));
    dataCollectorService.startCollector();
    firebaseNotificationService.firebaseNotificate();
    System.out.println(
        "\nSTOP " + formatDateTime.dateTimeFormat(LocalDateTime.now(ZoneId.of("UTC+3"))) + "\n");
  }

  @Scheduled(cron = "0 0 1 * * *")
  public void startCleaner() {
    System.out.println("\nCLEANER START!!!");
    waterForecastRepository.deleteByStartBefore(LocalDateTime.now().minusHours(hours));
    electricityForecastRepository.deleteByStartBefore(LocalDateTime.now().minusHours(hours));
    gasForecastRepository.deleteByStartBefore(LocalDateTime.now().minusHours(hours));
    firebaseNotificationRepository.deleteByTimeOfEntryBefore(LocalDateTime.now().minusHours(hours));
    System.out.println("\nCLEANER END!!!");
  }

  @Bean
  public TaskScheduler taskScheduler() {
    return new ConcurrentTaskScheduler(); //single threaded by default
  }
}