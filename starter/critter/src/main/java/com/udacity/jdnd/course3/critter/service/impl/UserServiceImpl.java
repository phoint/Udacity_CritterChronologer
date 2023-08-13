package com.udacity.jdnd.course3.critter.service.impl;

import com.udacity.jdnd.course3.critter.dao.CustomerRepository;
import com.udacity.jdnd.course3.critter.dao.EmployeeRepository;
import com.udacity.jdnd.course3.critter.dao.PetRepository;
import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.service.UserService;
import com.udacity.jdnd.course3.critter.user.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final ModelMapper mapper;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final PetRepository petRepository;

    public UserServiceImpl(ModelMapper mapper, EmployeeRepository employeeRepository, CustomerRepository customerRepository, PetRepository petRepository) {
        this.mapper = mapper;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.petRepository = petRepository;
    }

    /**
     * Save a new customer to database
     * @param customer representing the customer's information to be stored
     * @return a Customer containing the stored information
     */
    @Override
    public Customer saveCustomer(CustomerDTO customer) {
        Customer cust = mapper.map(customer, Customer.class);
        return customerRepository.save(cust);
    }

    /**
     * Retrieve all existed customer
     * @return a List of Customer stored in database
     */
    @Override
    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }

    /**
     * Retrieve owner of a Pet has petId
     * @param petId representing the id of Pet stored in database
     * @return a Customer as the owner of the Pet
     */
    @Override
    public Customer getOwnerByPet(long petId) {
        return Optional.ofNullable(customerRepository.findByPet(petId)).orElseThrow(NoSuchElementException::new);
    }

    /**
     * Save a new employee to database
     * @param employee representing the employee's information to be stored
     * @return a Employee containing the stored information
     */
    @Override
    public Employee saveEmployee(EmployeeDTO employee) {
        Employee empl = mapper.map(employee, Employee.class);
        return employeeRepository.saveAndFlush(empl);
    }

    /**
     * Retrieve the employee with the employeeId
     * @param employeeId representing the id of Employee
     * @return a Employee existed in database
     */
    @Override
    public Employee getEmployee(long employeeId) {
        return employeeRepository.getOne(employeeId);
    }

    /**
     * Update the employee with new available days of week
     * @param daysAvailable representing the days of week the employee is available
     * @param employeeId representing the id of Employee
     */
    @Override
    public void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        employee.setDaysAvailable(daysAvailable);
        employeeRepository.save(employee);
    }

    /**
     * Retrieve all valid employee base on services
     * @param employeeRequest representing the available day and skills set of employee are looking for
     * @return a List of valid Employee
     */
    @Override
    public List<Employee> findEmployeeByService(EmployeeRequestDTO employeeRequest) {
        List<Employee> availableByDate = employeeRepository.findByDaysAvailable(employeeRequest.getDate().getDayOfWeek());
        return availableByDate.stream().filter(e -> e.getSkills().containsAll(employeeRequest.getSkills())).collect(Collectors.toList());
    }
}
