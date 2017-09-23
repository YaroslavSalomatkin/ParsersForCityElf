package com.shutdownsforcityelf.service;

import com.shutdownsforcityelf.model.Forecast;
import com.shutdownsforcityelf.model.GasForecast;
import com.shutdownsforcityelf.model.NotificationToken;
import com.shutdownsforcityelf.model.User;
import com.shutdownsforcityelf.model.WaterForecast;
import com.shutdownsforcityelf.repository.FirebaseNotificationRepository;
import com.shutdownsforcityelf.utils.FormatDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FirebaseNotificationService {

  @Autowired
  private GasForecastService gasForecastService;
  @Autowired
  private ElectricityForecastService electricityForecastService;
  @Autowired
  private WaterForecastService waterForecastService;
  @Autowired
  FirebaseNotificationRepository firebaseNotificationRepository;
  @Autowired
  FormatDateTime formatDateTime;

  @Autowired
  AppServerFirebase appServerFirebase;

  String webUserFirebaseId = "WEB";

  public void firebaseNotificate() {
    gasForecastService.getGasForecasts().stream()
        .forEach(forecast -> startNotification(forecast));

    electricityForecastService.getElectricityForecasts().stream()
        .forEach(forecast -> startNotification(forecast));

    waterForecastService.getWaterForecasts().stream()
        .forEach(forecast -> startNotification(forecast));
  }

  private void startNotification(Forecast forecast) {
    NotificationToken notificationToken;
    List<User> users = forecast.getAddress().getUsers();
    if (users != null) {
      for (User user : users) {
        if (!user.getFirebaseId().equals(webUserFirebaseId)) {
          notificationToken = new NotificationToken(forecast.getAddress().getAddress(),
              forecast.getStart(),
              forecast.getEstimatedStop(), user.getId());
          if (firebaseNotificationRepository.findByTextHash(notificationToken.getTextHash())
              == null) {
            try {
              appServerFirebase.pushFCMNotification(user.getFirebaseId(), "New shutdown",
                  createMessageToFirebase(forecast));
              firebaseNotificationRepository.save(notificationToken);
            } catch (Exception exception) {
              exception.printStackTrace();
            }
          }
        }
      }
    }
  }

  private String createMessageToFirebase(Forecast forecast) {
    String typeOfForecast;
    if (forecast instanceof GasForecast) {
      typeOfForecast = " будет отключен газ ";
    } else if (forecast instanceof WaterForecast) {
      typeOfForecast = " будет отключено водоснабжение ";
    } else {
      typeOfForecast = " будет отключено электричество ";
    }
    return "По адресу " + forecast.getAddress().getAddress() + typeOfForecast
        + "предположительно с " + formatDateTime.timeFormat(forecast.getStart()) + " до "
        + formatDateTime.timeFormat(forecast.getEstimatedStop()) + "  " + formatDateTime
        .dateFormat(forecast.getStart());
  }
}