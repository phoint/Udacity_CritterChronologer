package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.user.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

public interface UserService {
    Customer saveCustomer(CustomerDTO customer);
    List<Customer> getAllCustomer();
    Customer getOwnerByPet(long petId);
    Employee saveEmployee(EmployeeDTO employee);
    Employee getEmployee(long employeeId);
    void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId);
    List<Employee> findEmployeeByService(EmployeeRequestDTO employeeRequest);
}
