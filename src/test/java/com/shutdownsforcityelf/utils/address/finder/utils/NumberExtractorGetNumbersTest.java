package com.shutdownsforcityelf.utils.address.finder.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RunWith(Parameterized.class)
public class NumberExtractorGetNumbersTest {

  @Parameters
  public static List<Object[]> getParams() {
    return Arrays.asList(new Object[][]{
        {"БАЛКОВСКАЯ УЛ., 20 и 26 все дома", Arrays.asList("20", "26")},
        {"ОСИПОВА УЛ., 47-50/52 все дома", Arrays.asList("47+", "48+", "49+", "50-52")},
        {"ОСИПОВА УЛ., 47 - 50/52 все дома", Arrays.asList("47+", "48+", "49+", "50-52")},
        {"МАЧТОВАЯ УЛ., 6 - 8 все дома", Arrays.asList("6+", "8")},
        {"АКАДЕМИКА ВОРОБЬЁВА УЛ., р-н СЛОБОДКА все дома", Collections.emptyList()},
        {"2-Й АЛЕКСАНДРА НЕВСКОГО ПЕР., 3-5 все дома", Arrays.asList("3+", "5")},
        {"ЧЕРНИГОВСКАЯ УЛ., 5-9а все дома", Arrays.asList("5+", "7+", "9-а")},
        {"ЧЕРНИГОВСКАЯ УЛ., 5 - 9а все дома", Arrays.asList("5+", "7+", "9-а")},
        {"ШИШКИНА УЛ., 48-52/2а все дома", Arrays.asList("48+", "50+", "52-2а")},
        {"РЕКОРДНАЯ УЛ., 68/1 все дома", Arrays.asList("68-1")},
        {"АЛЕКСАНДРА НЕВСКОГО УЛ., 1-3/2 все дома", Arrays.asList("1+", "3-2")},
        {"АКАДЕМИКА ВИЛЬЯМСА УЛ., 70-76/Б все дома",
            Arrays.asList("70+", "72+", "74+", "76-Б")},
        {"Жилые дома №: 35, 7, 7а, 5/24", Arrays.asList("35", "7", "7-а", "5-24")},
        {"МАРШАЛА ЖУКОВА ПР., 34,34а,101а,103 все дома",
            Arrays.asList("34", "34-а", "101-а", "103")},
        {"МАРШАЛА ЖУКОВА ПР., 34, 34а, 101а , 103 все дома",
            Arrays.asList("34", "34-а", "101-а", "103")},
        {"ТРАМВАЙНАЯ УЛ., 1 верх этажи-4 все дома", Arrays.asList("1+", "2+", "3+", "4")},
        {"ТРАМВАЙНАЯ УЛ., 1 верх этажи - 4 все дома", Arrays.asList("1+", "2+", "3+", "4")},
        {"ИЛЬФА И ПЕТРОВА УЛ., 1 верх этажи-3/1 все дома", Arrays.asList("1+", "3-1")},
        {"ИЛЬФА И ПЕТРОВА УЛ., 1 верх этажи - 3/1 все дома", Arrays.asList("1+", "3-1")},
        {"АКАДЕМИКА ГЛУШКО ПР., 1 верх этажи-3а все дома", Arrays.asList("1+", "3-а")},
        {"ЛЮСТДОРФСКАЯ ДОРОГА УЛ., 162 верх этажи-163/1 все дома", Arrays.asList("162+", "163-1")},
        {"МАРШАЛА ЖУКОВА ПР., 10,4,4/а все дома", Arrays.asList("10", "4", "4-а")},
        {"ЧУБАЕВСКАЯ УЛ., 11/б все дома", Arrays.asList("11-б")},
        {"АВДЕЕВА-ЧЕРНОМОРСКОГО УЛ., 1-2/а/б/в все дома", Arrays.asList("1+", "2-а-б-в")},
        {"ПАУСТОВСКОГО УЛ., 2А.2Б, все дома", Arrays.asList("2-А", "2-Б")},
        {"АКАДЕМИКА ВИЛЬЯМСА УЛ., 52/1-54 все дома", Arrays.asList("52+", "54")}
    });
  }

  private String inputRawAddress;
  private Collection<String> expectedResult;
  private NumberExtractor numberExtractor;

  public NumberExtractorGetNumbersTest(String inputRawAddress,
      Collection<String> expectedResult) {
    this.inputRawAddress = inputRawAddress;
    this.expectedResult = expectedResult;
  }

  @Before
  public void setUp() {
    numberExtractor = new NumberExtractor(new BuildingNumberExtender());
  }

  @Test
  public void getNumbers() {
    Set<String> numbers = numberExtractor.getNumbers(inputRawAddress);
    assertThat(numbers).containsAll(expectedResult);
    assertEquals(expectedResult.size(), numbers.size());
  }
}
