package com.shutdownsforcityelf.utils;

import com.shutdownsforcityelf.domain.ForcastData;
import com.shutdownsforcityelf.domain.Place;
import com.shutdownsforcityelf.domain.Report;
import com.shutdownsforcityelf.utils.address.finder.utils.NumberExtractor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
class WaterForcaster {

  private Logger logger = LogManager.getLogger();

  @Autowired
  private NumberExtractor numberExtractor;
  @Autowired
  private StreetExtractor streetExtractor;

  public List<ForcastData> getForcastsData(Report[] reports) {
    List<ForcastData> waterDataList = new ArrayList<>();
    for (Report report : reports) {
      for (Place place : report.places) {
        try {
          ForcastData forcastData = new ForcastData();
          forcastData.setAdress(streetExtractor.getStreetName(place.address));
          forcastData.setRawAdress(place.address);
          forcastData
              .setBuildingNumberList(numberExtractor.getNumbers(place.address));
          forcastData.setStartOff(parseToDate(place.startTime));
          forcastData.setEndOff(parseToDate(place.endTime));
          logger.info(forcastData);
          waterDataList.add(forcastData);
        } catch (Exception ex) {
          logger.error("Place can't be parsed: {}", place);
        }
      }
    }
    return waterDataList;
  }

  private LocalDateTime parseToDate(String stringDate) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    return LocalDateTime.parse(stringDate, formatter);
  }
}
