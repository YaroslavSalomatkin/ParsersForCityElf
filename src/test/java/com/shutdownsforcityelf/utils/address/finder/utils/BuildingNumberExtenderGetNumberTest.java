package com.shutdownsforcityelf.utils.address.finder.utils;

import static org.junit.Assert.assertEquals;

import com.shutdownsforcityelf.utils.address.finder.utils.BuildingNumberExtender;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(Parameterized.class)
public class BuildingNumberExtenderGetNumberTest {

  @Parameters
  public static List<Object[]> getParams() {
    return Arrays.asList(new Object[][]{
        {"3/б", "3-б"},
        {"23/б", "23-б"},
        {"123/б", "123-б"},

        {"3б", "3-б"},
        {"23б", "23-б"},
        {"123б", "123-б"},

        {"2/1б", "2-1б"},
        {"2/23б", "2-23б"},

        {"2", "2"},
        {"23", "23"},

        {"23/1б", "23-1б"},
        {"23/12б", "23-12б"}
    });
  }

  private BuildingNumberExtender numberExtender = new BuildingNumberExtender();
  private String input;
  private String expected;

  public BuildingNumberExtenderGetNumberTest(String input, String expected) {
    this.input = input;
    this.expected = expected;
  }

  @Test
  public void getNumber() throws Exception {
    Optional<String> number = numberExtender.getNumber(input);
    assertEquals(expected, number.orElse(""));
  }

}