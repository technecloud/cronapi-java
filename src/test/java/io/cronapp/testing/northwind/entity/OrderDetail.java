package io.cronapp.testing.northwind.entity;

import javax.persistence.*;

@Entity
@IdClass(OrderDetailPK.class)
@Table(name = "orders_details")
public class OrderDetail {

  @Id
  @JoinColumn(name = "order_id", nullable = false, referencedColumnName = "order_id")
  private Order order;

  @Id
  @JoinColumn(name = "product_id", nullable = false, referencedColumnName = "product_id")
  private Product product;

  @Column(name = "discount", nullable = false, precision = 8, scale = 8)
  private Float discount;

  @Column(name = "quantity", nullable = false, length = 5)
  private Short quantity;

  @Column(name = "unit_price", nullable = false, precision = 8, scale = 8)
  private Float unitPrice;

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Float getDiscount() {
    return discount;
  }

  public void setDiscount(Float discount) {
    this.discount = discount;
  }

  public Short getQuantity() {
    return quantity;
  }

  public void setQuantity(Short quantity) {
    this.quantity = quantity;
  }

  public Float getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(Float unitPrice) {
    this.unitPrice = unitPrice;
  }
}