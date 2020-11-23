package io.cronapp.testing.northwind.entity;

import javax.persistence.*;

@Entity
@IdClass(EmployeeTerritoryPK.class)
@Table(name = "employees_territories")
public class EmployeeTerritory {

  @Id
  @JoinColumn(name = "employee_id", nullable = false, referencedColumnName = "employee_id")
  private Employee employee;

  @Id
  @JoinColumn(name = "territory_id", nullable = false, referencedColumnName = "territory_id")
  private Territory territory;

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public Territory getTerritory() {
    return territory;
  }

  public void setTerritory(Territory territory) {
    this.territory = territory;
  }
}