package com.shutdownsforcityelf.utils.address.finder.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RunWith(Parameterized.class)
public class BuildingNumberExtenderGetNumbersTest {

  @Parameters
  public static List<Object[]> getParams() {
    return Arrays.asList(new Object[][]{
        {Arrays.asList("12/б"), Arrays.asList("12-б")},
        {Arrays.asList("12/б"), Arrays.asList("12-б")},
        {Arrays.asList("1-3б"), Arrays.asList("1+", "3-б")},
        {Arrays.asList("1-3б"), Arrays.asList("1+", "3-б")},
        {Arrays.asList("5-7/1б"), Arrays.asList("5+", "7-1б")},
        {Arrays.asList("22"), Arrays.asList("22")},
        {Arrays.asList("10-а", "11-", "12а", "13-а"), Arrays.asList("10-а", "11", "12-а", "13-а")}
    });
  }

  private Collection<String> input;
  private Collection<String> expectedResult;
  private BuildingNumberExtender buildingNumberExtender = new BuildingNumberExtender();

  public BuildingNumberExtenderGetNumbersTest(Collection<String> input,
      Collection<String> expectedResult) {
    this.input = input;
    this.expectedResult = expectedResult;
  }

  @Test
  public void getNumbers() {
    Set<String> numbers = buildingNumberExtender.getNumbers(input);
    assertThat(numbers).containsAll(expectedResult);
    assertEquals(expectedResult.size(), numbers.size());
    assertThat(numbers).isInstanceOf(Set.class);
  }
}
