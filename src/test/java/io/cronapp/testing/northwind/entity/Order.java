package io.cronapp.testing.northwind.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {

  @Id
  @Column(name = "order_id", nullable = false, length = 5)
  private Short orderId;

  @ManyToOne
  @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
  private Customer customer;

  @ManyToOne
  @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")

  private Employee employee;
  @Column(name = "freight", precision = 8, scale = 8)

  private Float freight;

  @Temporal(TemporalType.DATE)
  @Column(name = "order_date")
  private java.util.Date orderDate;

  @Temporal(TemporalType.DATE)
  @Column(name = "required_date")
  private java.util.Date requiredDate;

  @Column(name = "ship_address", length = 60)
  private String shipAddress;

  @Column(name = "ship_city", length = 15)
  private String shipCity;

  @Column(name = "ship_country", length = 15)
  private String shipCountry;

  @Column(name = "ship_name", length = 40)
  private String shipName;

  @Column(name = "ship_postal_code", length = 10)
  private String shipPostalCode;

  @Column(name = "ship_region", length = 15)
  private String shipRegion;

  @Temporal(TemporalType.DATE)
  @Column(name = "shipped_date")
  private java.util.Date shippedDate;

  @ManyToOne
  @JoinColumn(name = "ship_via", referencedColumnName = "shipper_id")
  private Shipper shipper;

  public Short getOrderId() {
    return orderId;
  }

  public void setOrderId(Short orderId) {
    this.orderId = orderId;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public Float getFreight() {
    return freight;
  }

  public void setFreight(Float freight) {
    this.freight = freight;
  }

  public Date getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

  public Date getRequiredDate() {
    return requiredDate;
  }

  public void setRequiredDate(Date requiredDate) {
    this.requiredDate = requiredDate;
  }

  public String getShipAddress() {
    return shipAddress;
  }

  public void setShipAddress(String shipAddress) {
    this.shipAddress = shipAddress;
  }

  public String getShipCity() {
    return shipCity;
  }

  public void setShipCity(String shipCity) {
    this.shipCity = shipCity;
  }

  public String getShipCountry() {
    return shipCountry;
  }

  public void setShipCountry(String shipCountry) {
    this.shipCountry = shipCountry;
  }

  public String getShipName() {
    return shipName;
  }

  public void setShipName(String shipName) {
    this.shipName = shipName;
  }

  public String getShipPostalCode() {
    return shipPostalCode;
  }

  public void setShipPostalCode(String shipPostalCode) {
    this.shipPostalCode = shipPostalCode;
  }

  public String getShipRegion() {
    return shipRegion;
  }

  public void setShipRegion(String shipRegion) {
    this.shipRegion = shipRegion;
  }

  public Date getShippedDate() {
    return shippedDate;
  }

  public void setShippedDate(Date shippedDate) {
    this.shippedDate = shippedDate;
  }

  public Shipper getShipper() {
    return shipper;
  }

  public void setShipper(Shipper shipper) {
    this.shipper = shipper;
  }
}