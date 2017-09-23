package com.shutdownsforcityelf.utils;

import com.shutdownsforcityelf.domain.ForcastData;
import com.shutdownsforcityelf.exceptions.ParserUnavailableException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class ParserElectro {

  @Value("${oblenergo.url.template.fact}")
  private String urlTemplateFact;
  @Value("${oblenergo.url.template.future}")
  private String urlTemplateFuture;
  @Value("${oblenergo.regions}")
  private int[] regions;
  @Value("${days.forward}")
  private int daysForward;
  @Autowired
  private ContentLoader loader;
  @Autowired
  private ElectroForcaster forcaster;

  public void setLoader(ContentLoader loader) {
    this.loader = loader;
  }

  public List<ForcastData> getForcastDataList() throws ParserUnavailableException {
    List<ForcastData> forcastDataList = new ArrayList<>();
    try {
      for (int region : regions) {
        for (int i = 0; i <= daysForward; i++) {
          URL url = (i == 0)
              ? new URL(MessageFormat.format(urlTemplateFact, region, getFormatedDateToday(i)))
              : new URL(MessageFormat.format(urlTemplateFuture, region, getFormatedDateToday(i)));

          String rawWebContent = loader.load(url);
          forcastDataList.addAll(forcaster.getForcastsData(rawWebContent));
        }
      }

    } catch (MalformedURLException ex) {
      ex.printStackTrace();
    }
    return forcastDataList;
  }

  private String getFormatedDateToday(int daysForward) {
    LocalDate localDate = LocalDate.now(ZoneId.of("UTC+3")).plusDays(daysForward);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    return localDate.format(formatter);
  }
}