package io.cronapp.testing.northwind.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shippers")
public class Shipper {

  @Id
  @Column(name = "shipper_id", nullable = false, length = 5)
  private Short shipperId;

  @Column(name = "company_name", nullable = false, length = 40)
  private String companyName;

  @Column(name = "phone", length = 24)
  private String phone;

  public Short getShipperId() {
    return shipperId;
  }

  public void setShipperId(Short shipperId) {
    this.shipperId = shipperId;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }
}