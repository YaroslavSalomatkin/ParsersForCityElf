package com.shutdownsforcityelf.repository;

import com.shutdownsforcityelf.model.User;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

  User findByEmail(String email);

  Optional<User> findByFirebaseId(String firebaseId);

  Optional<User> findById(long id);
}