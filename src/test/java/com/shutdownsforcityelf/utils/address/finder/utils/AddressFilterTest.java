package com.shutdownsforcityelf.utils.address.finder.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.shutdownsforcityelf.model.Address;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RunWith(Parameterized.class)
public class AddressFilterTest {

  @Parameters
  public static List<Object[]> getParams() {
    return Arrays.asList(new Object[][]{
        {
            Arrays.asList(new Address("Лазурный 1-й переулок, 7")),
            Arrays.asList("1", "8"),
            "Лазурный 1-й пер.",
            Optional.of("7"),
            0
        },
        {
            Arrays.asList(new Address("Лазурный 1-й переулок, 7")),
            Arrays.asList("2", "7"),
            "Лазурный 1-й пер.",
            Optional.of("7"),
            1
        },
        {
            Arrays.asList(new Address("Лазурный 1-й переулок, 7/б")),
            Arrays.asList("2", "7"),
            "Лазурный 1-й пер.",
            Optional.of("7-б"),
            0
        },
        {
            Arrays.asList(new Address("Лазурный 1-й переулок, 7/б")),
            Arrays.asList("1", "7-б", "17"),
            "Лазурный 1-й пер.",
            Optional.of("7-б"),
            1
        }
    });
  }

  private List<Address> preSelectionAddresses;
  private Collection<String> numbers;
  private String streetName;
  private Optional<String> numberExtractorResult;
  private int expectedResultSize;
  private AddressFilter addressFilter;

  public AddressFilterTest(
      List<Address> preSelectionAddresses,
      Collection<String> numbers,
      String streetName,
      Optional<String> numberExtractorResult,
      int expectedResultSize) {
    this.preSelectionAddresses = preSelectionAddresses;
    this.numbers = numbers;
    this.streetName = streetName;
    this.numberExtractorResult = numberExtractorResult;
    this.expectedResultSize = expectedResultSize;
  }

  @Before
  public void setUp(){
    NumberExtractor numberExtractorMockInjection = Mockito.mock(NumberExtractor.class);
    addressFilter = new AddressFilter(numberExtractorMockInjection);
    when(numberExtractorMockInjection.getNumber(any(String.class)))
        .thenReturn(numberExtractorResult);
  }
  @Test
  public void filterAddresses() throws Exception {
    List<Address> resultAddresses = addressFilter
        .filterAddresses(preSelectionAddresses, numbers, streetName);
    assertThat(resultAddresses.size()).isEqualTo(expectedResultSize);
  }

}