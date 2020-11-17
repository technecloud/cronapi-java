package io.cronapp.testing.northwind.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "employees")
public class Employee {

  @Id
  @Column(name = "employee_id", nullable = false, length = 5)
  private Short employeeId;

  @Column(name = "address", length = 60)
  private String address;

  @Temporal(TemporalType.DATE)
  @Column(name = "birth_date")
  private Date birthDate;

  @Column(name = "city", length = 15)
  private String city;

  @Column(name = "country", length = 15)
  private String country;

  @ManyToOne
  @JoinColumn(name = "reports_to", referencedColumnName = "employee_id")
  private Employee employee;

  @Column(name = "extension", length = 4)
  private String extension;

  @Column(name = "first_name", nullable = false, length = 10)
  private String firstName;

  @Temporal(TemporalType.DATE)
  @Column(name = "hire_date")
  private Date hireDate;

  @Column(name = "home_phone", length = 24)
  private String homePhone;

  @Column(name = "last_name", nullable = false, length = 20)
  private String lastName;

  @Column(name = "notes", length = 2147483647)
  private String notes;

  @Column(name = "photo", length = 2147483647)
  private byte[] photo;

  @Column(name = "photo_path")
  private String photoPath;

  @Column(name = "postal_code", length = 10)
  private String postalCode;

  @Column(name = "region", length = 15)
  private String region;

  @Column(name = "title", length = 30)
  private String title;

  @Column(name = "title_of_courtesy", length = 25)
  private String titleOfCourtesy;

  public Short getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(Short employeeId) {
    this.employeeId = employeeId;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public Date getHireDate() {
    return hireDate;
  }

  public void setHireDate(Date hireDate) {
    this.hireDate = hireDate;
  }

  public String getHomePhone() {
    return homePhone;
  }

  public void setHomePhone(String homePhone) {
    this.homePhone = homePhone;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public byte[] getPhoto() {
    return photo;
  }

  public void setPhoto(byte[] photo) {
    this.photo = photo;
  }

  public String getPhotoPath() {
    return photoPath;
  }

  public void setPhotoPath(String photoPath) {
    this.photoPath = photoPath;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitleOfCourtesy() {
    return titleOfCourtesy;
  }

  public void setTitleOfCourtesy(String titleOfCourtesy) {
    this.titleOfCourtesy = titleOfCourtesy;
  }
}