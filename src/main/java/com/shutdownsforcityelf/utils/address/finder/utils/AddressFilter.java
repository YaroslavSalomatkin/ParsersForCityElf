package com.shutdownsforcityelf.utils.address.finder.utils;

import com.shutdownsforcityelf.model.Address;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AddressFilter {

  private final Logger logger = LogManager.getLogger();
  @Autowired
  private NumberExtractor numberExtractor;

  public AddressFilter() {
  }

  public AddressFilter(NumberExtractor numberExtractor) {
    this.numberExtractor = numberExtractor;
  }

  public List<Address> filterAddresses(List<Address> preSelectionAddresses,
      Collection<String> buildingNumbers, String streetName) {
    logger.traceEntry("Preselection: {}, numbers: {}, street name: {}",
        preSelectionAddresses.size(),
        buildingNumbers,
        streetName);
    return logger.traceExit(buildingNumbers.isEmpty()
        ? preSelectionAddresses
        : preSelectionAddresses
            .stream()
            .filter(address -> accept(buildingNumbers, streetName, address))
            .collect(Collectors.toList()));
  }

  private boolean accept(Collection<String> buildingNumbers, String streetName, Address address) {
    String streetNameDb = address.getAddress();
    Optional<String> number = numberExtractor.getNumber(streetNameDb);
    return number.isPresent()
        && (buildingNumbers.contains(number.get())
        || buildingNumbers.contains(number.get().split("-")[0] + "+"))
        && RoadType.getRoadType(streetNameDb) == RoadType.getRoadType(streetName);
  }
}
