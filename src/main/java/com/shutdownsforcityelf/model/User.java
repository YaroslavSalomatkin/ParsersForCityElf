package com.shutdownsforcityelf.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.validator.constraints.Email;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "email")
  @Email(message = "invalid email format",
      regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
          + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
  private String email;
  @Column(name = "phone")
  private String phone;
  @JsonIgnore
  @Column(name = "password")
  private String password;
  @Embedded
  @NotNull
  private Notification notification;
  @Column(name = "token")
  private String token;
  @Column(name = "expiration_date")
  @JsonIgnore
  private LocalDateTime expirationDate;
  @Column(name = "activated")
  @JsonIgnore
  private boolean activated;
  @Column(name = "firebase_id")
  private String firebaseId;
  @OneToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_addresses",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "address_id"))
  private List<Address> addresses;

  public User() {
    this.notification = new Notification();
    this.addresses = new ArrayList<>();
  }

  public List<Address> getAddresses() {
    return addresses;
  }

  public void setAddresses(List<Address> addresses) {
    this.addresses = addresses;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getFirebaseId() {
    return firebaseId;
  }

  public Notification getNotification() {
    return notification;
  }

  public boolean isActivated() {
    return activated;
  }

  public long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getPhone() {
    return phone;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    User another = (User) obj;
    return Objects.equals(id, another.id)
        && Objects.equals(email, another.email)
        && Objects.equals(phone, another.phone)
        && Objects.equals(password, another.password)
        && Objects.equals(firebaseId, another.firebaseId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email, phone, password, firebaseId);
  }
}