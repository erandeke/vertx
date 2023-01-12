package com.kedar.employeerest;

/**
 * @author Kedar Erande
 */
public class Employee {

  private String empName;
  private String empId;
  private String empDept;

  public Employee() {

  }

  public Employee(String empName, String empId, String empDept) {
    this.empName = empName;
    this.empId = empId;
    this.empDept = empDept;
  }

  public String getEmpName() {
    return empName;
  }

  public void setEmpName(String empName) {
    this.empName = empName;
  }

  public String getEmpId() {
    return empId;
  }

  public void setEmpId(String empId) {
    this.empId = empId;
  }

  public String getEmpDept() {
    return empDept;
  }

  public void setEmpDept(String empDept) {
    this.empDept = empDept;
  }
}
