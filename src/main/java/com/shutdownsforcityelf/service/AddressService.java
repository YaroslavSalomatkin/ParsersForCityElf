package com.shutdownsforcityelf.service;

import com.shutdownsforcityelf.model.Address;
import com.shutdownsforcityelf.repository.AddressesRepository;
import com.shutdownsforcityelf.utils.address.finder.utils.AddressFilter;
import com.shutdownsforcityelf.utils.address.finder.utils.NumberExtractor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AddressService {

  private final Logger logger = LogManager.getLogger();
  private final String removePatternString = "одесса|одеса|одесская\\sобл[^\\s]*|одеська\\sобл[^\\s]*|";
  private Pattern removePattern = Pattern.compile(removePatternString);

  @Autowired
  private AddressesRepository addressesRepository;
  @Autowired
  private AddressFilter addressFilter;
  @Autowired
  private NumberExtractor numberExtractor;

  public AddressService() {
  }

  public AddressService(AddressesRepository addressesRepository,
      AddressFilter addressFilter,
      NumberExtractor numberExtractor) {
    this.addressesRepository = addressesRepository;
    this.addressFilter = addressFilter;
    this.numberExtractor = numberExtractor;
  }

  public List<Address> getAddresses(String streetName, Collection<String> numbers) {
    logger.traceEntry("Method \"getAddresses\", params: streetName: {}, numbers: {}", streetName,
        numbers);

    List<Address> preSelectionAddresses = transformToAddresses(
        addressesRepository.findSimilarAddresses(streetName));
    List<Address> addresses = addressFilter
        .filterAddresses(preSelectionAddresses, numbers, streetName);
    if (addresses.isEmpty()) {
      logger.error("Address not found in DB: {},{}", streetName, numbers);
    }
    return logger.traceExit("Method \"getAddresses\" return {}", addresses);
  }

  public List<String> getUniqueStreetsNames() {
    return addressesRepository.getUniqueStreetsNames();
  }

  private List<Address> transformToAddresses(List<Object[]> params) {
    return params.stream().map(
        record -> new Address(
            ((BigInteger) record[0]).longValue(),
            ((String) record[1]),
            ((String) record[2])
        ))
        .collect(Collectors.toList());
  }
}
