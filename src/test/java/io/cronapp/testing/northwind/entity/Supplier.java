package io.cronapp.testing.northwind.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "suppliers")
public class Supplier {

  @Id
  @Column(name = "supplier_id", nullable = false, length = 5)
  private Short supplierId;

  @Column(name = "address", length = 60)
  private String address;

  @Column(name = "city", length = 15)
  private String city;

  @Column(name = "company_name", nullable = false, length = 40)
  private String companyName;

  @Column(name = "contact_name", length = 30)
  private String contactName;

  @Column(name = "contact_title", length = 30)
  private String contactTitle;

  @Column(name = "country", length = 15)
  private String country;

  @Column(name = "fax", length = 24)
  private String fax;

  @Column(name = "homepage", length = 2147483647)
  private String homepage;

  @Column(name = "phone", length = 24)
  private String phone;

  @Column(name = "postal_code", length = 10)
  private String postalCode;

  @Column(name = "region", length = 15)
  private String region;

  public Short getSupplierId() {
    return supplierId;
  }

  public void setSupplierI(Short supplierId) {
    this.supplierId = supplierId;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getContactName() {
    return contactName;
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  public String getContactTitle() {
    return contactTitle;
  }

  public void setContactTitle(String contactTitle) {
    this.contactTitle = contactTitle;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getFax() {
    return fax;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  public String getHomepage() {
    return homepage;
  }

  public void setHomepage(String homepage) {
    this.homepage = homepage;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
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
}