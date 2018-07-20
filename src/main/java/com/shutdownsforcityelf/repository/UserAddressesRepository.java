package com.shutdownsforcityelf.repository;

import com.shutdownsforcityelf.model.UserAddresses;

import org.springframework.data.repository.CrudRepository;


public interface UserAddressesRepository extends CrudRepository<UserAddresses, Long> {

  UserAddresses findByUserId(Long userId);

  int findByAddressId(String addressId);

  UserAddresses save(UserAddresses userAddresses);

  void deleteUserAddressesByUserId(Long userId);
}