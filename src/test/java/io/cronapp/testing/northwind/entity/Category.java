package io.cronapp.testing.northwind.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "categories")
public class Category {
  @Id
  @Column(name = "category_id", nullable = false, length = 5)
  private Short categoryId;

  @Column(name = "category_name", nullable = false, length = 15)
  private String categoryName;

  @Column(name = "description")
  private String description;

  @Column(name = "picture")
  private byte[] picture;

  public Short getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Short categoryId) {
    this.categoryId = categoryId;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public byte[] getPicture() {
    return picture;
  }

  public void setPicture(byte[] picture) {
    this.picture = picture;
  }
}