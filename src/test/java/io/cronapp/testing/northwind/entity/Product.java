package io.cronapp.testing.northwind.entity;

import javax.persistence.*;

@Entity
@Table(name = "products")
public class Product {

  @Id
  @Column(name = "product_id", nullable = false, length = 5)
  private Short productId;

  @ManyToOne
  @JoinColumn(name = "category_id", referencedColumnName = "category_id")
  private Category category;

  @Column(name = "discontinued", nullable = false)
  private Integer discontinued;

  @Column(name = "product_name", nullable = false, length = 40)
  private String productName;

  @Column(name = "quantity_per_unit", length = 20)
  private String quantityPerUnit;

  @Column(name = "reorder_level")
  private Short reorderLevel;

  @ManyToOne
  @JoinColumn(name = "supplier_id", referencedColumnName = "supplier_id")
  private Supplier supplier;

  @Column(name = "unit_price", precision = 8, scale = 8)
  private Float unitPrice;

  @Column(name = "units_in_stock")
  private Short unitsInStock;

  @Column(name = "units_on_order")
  private Short unitsOnOrder;

  public Short getProductId() {
    return productId;
  }

  public void setProductId(Short productId) {
    this.productId = productId;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public Integer getDiscontinued() {
    return discontinued;
  }

  public void setDiscontinued(Integer discontinued) {
    this.discontinued = discontinued;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getQuantityPerUnit() {
    return quantityPerUnit;
  }

  public void setQuantityPerUnit(String quantityPerUnit) {
    this.quantityPerUnit = quantityPerUnit;
  }

  public Short getReorderLevel() {
    return reorderLevel;
  }

  public void setReorderLevel(Short reorderLevel) {
    this.reorderLevel = reorderLevel;
  }

  public Supplier getSupplier() {
    return supplier;
  }

  public void setSupplier(Supplier supplier) {
    this.supplier = supplier;
  }

  public Float getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(Float unitPrice) {
    this.unitPrice = unitPrice;
  }

  public Short getUnitsInStock() {
    return unitsInStock;
  }

  public void setUnitsInStock(Short unitsInStock) {
    this.unitsInStock = unitsInStock;
  }

  public Short getUnitsOnOrder() {
    return unitsOnOrder;
  }

  public void setUnitsOnOrder(Short unitsOnOrder) {
    this.unitsOnOrder = unitsOnOrder;
  }
}