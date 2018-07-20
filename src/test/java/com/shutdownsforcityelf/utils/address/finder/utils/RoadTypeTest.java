package com.shutdownsforcityelf.utils.address.finder.utils;

import static com.shutdownsforcityelf.utils.address.finder.utils.RoadType.AVENUE;
import static com.shutdownsforcityelf.utils.address.finder.utils.RoadType.BOULEVARD;
import static com.shutdownsforcityelf.utils.address.finder.utils.RoadType.DESCENT;
import static com.shutdownsforcityelf.utils.address.finder.utils.RoadType.LANE;
import static com.shutdownsforcityelf.utils.address.finder.utils.RoadType.LINE;
import static com.shutdownsforcityelf.utils.address.finder.utils.RoadType.STREET;
import static org.junit.Assert.assertEquals;

import com.shutdownsforcityelf.utils.address.finder.utils.RoadType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class RoadTypeTest {

  @Parameters
  public static List<Object[]> getParams() {
    return Arrays.asList(new Object[][]{
        {"8 Марта, 5", STREET},
        {"ул. 8 Марта, 5", STREET},
        {"улица 8 Марта, 5", STREET},
        {"8 Марта 5-й переулок, 9", LANE},
        {"пер. 8 Марта 5-й, 9", LANE},
        {"8 Марта Спуск, 5", DESCENT},
        {"сп. 8 Марта, 5", DESCENT},
        {"Десантный бул., 1а", BOULEVARD},
        {"Десантный бульвар, 1а", BOULEVARD},
        {"Адмиральский проспект, 1а", AVENUE},
        {"просп. Адмиральский, 1а", AVENUE},
        {"добровольского проспект", AVENUE},
        {"8 Марта 5-линия, 3", LINE}
    });
  }

  private String input;
  private RoadType expectedResult;

  public RoadTypeTest(String input,
      RoadType expectedResult) {
    this.input = input;
    this.expectedResult = expectedResult;
  }

  @Test
  public void test() {
    RoadType result = RoadType.getRoadType(input);
    assertEquals(expectedResult, result);
  }
}