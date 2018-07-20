package com.shutdownsforcityelf.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class FormatDateTime {

  public String dateFormat(LocalDateTime localDateTime) {
    DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    return localDateTime.format(formatterDate);
  }

  public String timeFormat(LocalDateTime localDateTime) {
    DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
    return localDateTime.format(formatterTime);
  }

  public String dateTimeFormat(LocalDateTime localDateTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
    return localDateTime.format(formatter);
  }
}