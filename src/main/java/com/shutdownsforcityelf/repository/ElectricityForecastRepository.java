package com.shutdownsforcityelf.repository;

import com.shutdownsforcityelf.model.Address;
import com.shutdownsforcityelf.model.ElectricityForecast;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ElectricityForecastRepository extends CrudRepository<ElectricityForecast, Long> {

  List<ElectricityForecast> findByStart(LocalDateTime startTime);

  Optional<ElectricityForecast> findByStartAndAddress(LocalDateTime startTime, Address address);

  Optional<ElectricityForecast> findByStartAndAddress_Address(LocalDateTime startTime,
      String address);

  List<ElectricityForecast> findByAddress(Address address);

  void deleteElectricityForecastByStart(LocalDateTime startTime);

  List<ElectricityForecast> findElectricityForecastsByStartLessThanEqualAndEstimatedStopGreaterThan(
      LocalDateTime checkStart, LocalDateTime checkEnd);

  @Modifying
  @Query("delete from ElectricityForecast ef where ef.peopleReport = false")
  void deletePreviousServiceReports();

  @Transactional
  void deleteByStartBefore(LocalDateTime timeOfEntry);

  @Query("select ef from ElectricityForecast ef where ef.peopleReport = false")
  List<ElectricityForecast> getElectricityForecasts();
}
