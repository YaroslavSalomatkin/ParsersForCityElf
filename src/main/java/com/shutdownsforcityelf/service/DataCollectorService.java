package com.shutdownsforcityelf.service;

import com.shutdownsforcityelf.domain.ForcastData;
import com.shutdownsforcityelf.exceptions.ParserUnavailableException;
import com.shutdownsforcityelf.model.Address;
import com.shutdownsforcityelf.model.ElectricityForecast;
import com.shutdownsforcityelf.model.Forecast;
import com.shutdownsforcityelf.model.GasForecast;
import com.shutdownsforcityelf.model.WaterForecast;
import com.shutdownsforcityelf.utils.ParserElectro;
import com.shutdownsforcityelf.utils.ParserGas;
import com.shutdownsforcityelf.utils.ParserWater;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DataCollectorService {

  private final Logger logger = LogManager.getLogger();
  @Autowired
  private ElectricityForecastService electricityForecastService;
  @Autowired
  private WaterForecastService waterForecastService;
  @Autowired
  private GasForecastService gasForecastService;
  @Autowired
  private AddressService addressService;

  @Autowired
  private ParserElectro parserElectro;
  @Autowired
  private ParserWater parserWater;
  @Autowired
  private ParserGas parserGas;


  public void startCollector() {
    logger.traceEntry("Parser collector started...");
    Set<Forecast> forecasts = getAllForecasts();
    databaseRewriteAll(forecasts);
    logger.traceExit("Parser collector stoped!");
  }

  private Set<Forecast> getAllForecasts() {
    Set<Forecast> forecasts = new HashSet<>();
    List<ForcastData> forcastDataList = Collections.EMPTY_LIST;

    logger.traceEntry("Getting water forecasts started...");
    try {
      forcastDataList = parserWater.getForcastDataList();
      for (ForcastData forcastData : forcastDataList) {
        Collection<Address> addresses = addressService
            .getAddresses(forcastData.getAdress(), forcastData.getBuildingNumberList());
        for (Address address : addresses) {
          WaterForecast forecast = new WaterForecast();
          forecast.setStart(forcastData.getStartOff());
          forecast.setEstimatedStop(forcastData.getEndOff());
          forecast.setAddress(address);
          forecasts.add(forecast);
        }
      }
    } catch (ParserUnavailableException ex) {
      logger.error("WaterParser unavailable", ex);
    } catch (Exception ex) {
      logger.error("WaterParser ERROR", ex);
    }
    logger.traceEntry("Getting water forecasts stoped");

    logger.traceEntry("Getting electricity forecasts started...");
    try {
      forcastDataList = parserElectro.getForcastDataList();
      for (ForcastData forcastData : forcastDataList) {
        Collection<Address> addresses = addressService
            .getAddresses(forcastData.getAdress(), forcastData.getBuildingNumberList());
        for (Address address : addresses) {
          ElectricityForecast forecast = new ElectricityForecast();
          forecast.setStart(forcastData.getStartOff());
          forecast.setEstimatedStop(forcastData.getEndOff());
          forecast.setAddress(address);
          forecasts.add(forecast);
        }
      }
    } catch (ParserUnavailableException ex) {
      logger.error("ElectricityParser unavailable", ex);
    } catch (Exception ex) {
      logger.error("ElectricityParser ERROR", ex);
    }
    logger.traceEntry("Getting electricity forecasts stoped");

    logger.traceEntry("Getting gas forecasts started...");
    try {
      forcastDataList = parserGas.getForcastDataList();
      for (ForcastData forcastData : forcastDataList) {
        Collection<Address> addresses = addressService
            .getAddresses(forcastData.getAdress(), forcastData.getBuildingNumberList());
        for (Address address : addresses) {
          GasForecast forecast = new GasForecast();
          forecast.setStart(forcastData.getStartOff());
          forecast.setEstimatedStop(forcastData.getEndOff());
          forecast.setAddress(address);
          forecasts.add(forecast);
        }
      }
    } catch (ParserUnavailableException ex) {
      logger.error("GasParser unavailable", ex);
    } catch (Exception ex) {
      logger.error("GasParser ERROR", ex);
    }
    logger.traceEntry("Getting gas forecasts stoped");
    return forecasts;
  }

  private void databaseRewriteAll(Set<Forecast> forecasts) {
    try {
      List<GasForecast> gasForecasts = new ArrayList<>();
      List<ElectricityForecast> electricityForecasts = new ArrayList<>();
      List<WaterForecast> waterForecasts = new ArrayList<>();

      for (Forecast forecast : forecasts) {
        if (forecast instanceof ElectricityForecast) {
          electricityForecasts.add(((ElectricityForecast) forecast));
        } else if (forecast instanceof WaterForecast) {
          waterForecasts.add(((WaterForecast) forecast));
        } else {
          gasForecasts.add(((GasForecast) forecast));
        }
      }
      if (electricityForecasts.size() > 0) {
        logger.trace("Saving electo forecasts...");
        electricityForecastService.rewriteAll(electricityForecasts);
        logger.trace("Electro forecasts saved");
      }
      if (waterForecasts.size() > 0) {
        logger.trace("Saving water forecasts...");
        waterForecastService.rewriteAll(waterForecasts);
        logger.trace("Water forecasts saved");
      }
      if (gasForecasts.size() > 0) {
        logger.trace("Saving gas forecasts...");
        gasForecastService.rewriteAll(gasForecasts);
        logger.trace("Gas forecasts saved");
      }
    } catch (Exception ex) {
      logger.error("Error while forecasts saving", ex);
    }
  }

}
