package com.shutdownsforcityelf.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.shutdownsforcityelf.model.Address;
import com.shutdownsforcityelf.repository.AddressesRepository;
import com.shutdownsforcityelf.utils.address.finder.utils.AddressFilter;
import com.shutdownsforcityelf.utils.address.finder.utils.BuildingNumberExtender;
import com.shutdownsforcityelf.utils.address.finder.utils.NumberExtractor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class AddressServiceTest {

  @Parameters
  public static List<Object[]> getParams() {
    return Arrays.asList(new Object[][]{
        {
            "добровольского  проспект",
            Arrays.asList("1", "114-1", "157", "159"),
            Arrays.asList(new Address("Добровольського проспект, 1"))
        },
        {
            "добровольского  проспект",
            Arrays.asList("1+", "114-1", "157", "159"),
            Arrays.asList(
                new Address("Добровольського проспект, 1"),
                new Address("Добровольського проспект, 1б"),
                new Address("Добровольського проспект, 1/2"))
        },
        {
            "1-линия добровольского  проспект",
            Arrays.asList("1", "1-2", "157", "159"),
            Arrays.asList(
                new Address("Добровольського проспект, 1"),
                new Address("Добровольського проспект, 1/2"))
        }
    });
  }

  private AddressService addressService;
  private AddressesRepository addressesRepository;
  private List<Address> addressesFromDb;

  private String streetName;
  private Collection<String> numbers;
  private Collection<Address> expectedResult;

  public AddressServiceTest(
      String streetName,
      Collection<String> numbers,
      Collection<Address> expectedResult) {
    this.streetName = streetName;
    this.numbers = numbers;
    this.expectedResult = expectedResult;
    this.addressesFromDb = Arrays.asList(
        new Address("Добровольського проспект, 1"),
        new Address("Добровольського проспект, 1б"),
        new Address("Добровольського проспект, 1/2"),
        new Address("Добровольського проспект, 11"),
        new Address("Добровольського проспект, 21")
    );
  }

  @Before
  public void setUp() throws Exception {
    NumberExtractor numberExtractor = new NumberExtractor(new BuildingNumberExtender());
    addressesRepository = mock(AddressesRepository.class);
    addressService = new AddressService(
        addressesRepository,
        new AddressFilter(numberExtractor),
        numberExtractor);

  }

  @Test
  public void getAddresses() throws Exception {
    when(addressesRepository.findSimilarAddresses(any(String.class))).thenReturn(addressesFromDb);

    List<Address> addresses = addressService.getAddresses(streetName, numbers);
    assertThat(addresses).containsAll(expectedResult);
    assertEquals(expectedResult.size(), addresses.size());
  }

}