package com.shutdownsforcityelf.utils.address.finder.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BuildingNumberExtender {

  private final Logger logger = LogManager.getLogger();
  private final Pattern buildingSingleNumberPattern = Pattern.compile("^\\d+");
  private final Pattern singleWithDashPattern = Pattern.compile("\\d+-[^\\d]+|\\d+-$");

  public Set<String> getNumbers(Collection<String> rawNumbers) {
    logger.traceEntry("Method \"getNumbers\", param: {}", rawNumbers);
    Set<String> exactNumbers = new HashSet<>();
    for (String rawNumber : rawNumbers) {
      if (rawNumber.contains("-")) {
        exactNumbers.addAll(getRangeNumbers(rawNumber));
      } else {
        getNumber(rawNumber).ifPresent(exactNumber -> exactNumbers.add(exactNumber));
      }
    }
    return logger.traceExit("Method \"getNumbers\" return: {}", exactNumbers);
  }

  public Optional<String> getNumber(String rawNumber) {
    logger.traceEntry("Method \"getNumber\", param: {}", rawNumber);

    if (rawNumber.contains("/")) {
      return logger
          .traceExit("Method \"getNumber\" return: {}",
              Optional.of(rawNumber.replace("/", "-")));
    }
    Optional<String> cleanNumber = getNumberOnly(rawNumber);
    if (cleanNumber.isPresent()) {
      String number = cleanNumber.get();
      if (number.length() == rawNumber.length()) {
        return logger.traceExit("Method \"getNumber\" return: {}", Optional.of(number));
      }
      return logger.traceExit("Method \"getNumber\" return: {}",
          Optional.of(rawNumber.replaceFirst(number, number + "-")));
    }
    return logger.traceExit("Method \"getNumber\" return: {}", cleanNumber);
  }

  private List<String> getRangeNumbers(String rawNumber) {
    List<String> exactNumbers = new ArrayList<>();
    if (singleWithDashPattern.matcher(rawNumber).find()) {
      String replace = rawNumber.replace("-", "");
      Optional<String> number = getNumber(replace);
      if (number.isPresent()) {
        exactNumbers.add(number.get());
      }
      return exactNumbers;
    }
    try {
      String[] split = rawNumber.split("-");
      int end = Integer
          .parseInt(getNumberOnly(split[1]).orElseThrow(() -> new RuntimeException()));
      int start = Integer
          .parseInt(getNumberOnly(split[0]).orElseThrow(() -> new RuntimeException()));

      int increment = (end - start) % 2 == 0 ? 2 : 1;
      while (start < end) {
        exactNumbers.add(start + "+");
        start += increment;
      }
      exactNumbers.add(getNumber(split[1]).orElseThrow(() -> new RuntimeException()));
    } catch (RuntimeException ex) {
      logger.error("Error while range of building numbers creating. Param: " + rawNumber, ex);
    }
    return exactNumbers;
  }

  private Optional<String> getNumberOnly(String number) {
    Matcher matcher = buildingSingleNumberPattern.matcher(number);
    return matcher.find()
        ? Optional.of(matcher.group())
        : Optional.empty();
  }
}
