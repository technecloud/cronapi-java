package io.cronapp.testing.northwind.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "regions")
public class Region {

  @Id
  @Column(name = "region_id", nullable = false, length = 5)
  private Short regionId;

  @Column(name = "region_description", nullable = false)
  private String regionDescription;

  public Short getRegionId() {
    return regionId;
  }

  public void setRegionId(Short regionId) {
    this.regionId = regionId;
  }

  public String getRegionDescription() {
    return regionDescription;
  }

  public void setRegionDescription(String regionDescription) {
    this.regionDescription = regionDescription;
  }
}