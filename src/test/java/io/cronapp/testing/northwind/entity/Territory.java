package io.cronapp.testing.northwind.entity;

import javax.persistence.*;

@Entity
@Table(name = "territories")
public class Territory {
  @Id
  @Column(name = "territory_id", nullable = false, length = 20)
  private String territoryId;

  @ManyToOne
  @JoinColumn(name = "region_id", nullable = false, referencedColumnName = "region_id")
  private Region region;

  @Column(name = "territory_description", nullable = false, length = 2147483647)
  private String territoryDescription;

  public String getTerritoryId() {
    return territoryId;
  }

  public void setTerritoryId(String territoryId) {
    this.territoryId = territoryId;
  }

  public Region getRegion() {
    return region;
  }

  public void setRegion(Region region) {
    this.region = region;
  }

  public String getTerritoryDescription() {
    return territoryDescription;
  }

  public void setTerritoryDescription(String territoryDescription) {
    this.territoryDescription = territoryDescription;
  }
}