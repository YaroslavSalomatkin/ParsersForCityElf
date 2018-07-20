package com.shutdownsforcityelf.utils.address.finder.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(Parameterized.class)
public class NumberExtractorGetNumberTest {

  @Parameters
  public static List<Object[]> getParams() {
    return Arrays.asList(new Object[][]{
        {"Ивана Франко, 19 к2", "19"},
        {"Ивана Франко, 19", "19"},
        {"Ивана Франко, 19/к2,", "19-к2"},
        {"Ивана Франко, 19к2.", "19-к2"},
        {"1-линия Ивана Франко, 19к2", "19-к2"}
    });
  }

  private String inputRawAddress;
  private String expectedResult;
  private NumberExtractor numberExtractor;

  public NumberExtractorGetNumberTest(String inputRawAddress, String expectedResult) {
    this.inputRawAddress = inputRawAddress;
    this.expectedResult = expectedResult;
  }

  @Before
  public void setUp() {
    numberExtractor = new NumberExtractor(new BuildingNumberExtender());
  }

  @Test
  public void getNumber() {
    Optional<String> number = numberExtractor.getNumber(inputRawAddress);
    assertEquals(expectedResult, number.orElse(""));
  }
}