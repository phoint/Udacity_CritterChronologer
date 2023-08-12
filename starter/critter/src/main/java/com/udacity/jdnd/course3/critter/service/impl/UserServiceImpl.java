package com.udacity.jdnd.course3.critter.service.impl;

import com.udacity.jdnd.course3.critter.dao.CustomerRepository;
import com.udacity.jdnd.course3.critter.dao.EmployeeRepository;
import com.udacity.jdnd.course3.critter.dao.PetRepository;
import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.service.UserService;
import com.udacity.jdnd.course3.critter.user.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
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

    @Override
    public Customer saveCustomer(CustomerDTO customer) {
        Customer cust = mapper.map(customer, Customer.class);
        return customerRepository.save(cust);
    }

    @Override
    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getOwnerByPet(long petId) {
        return Optional.ofNullable(customerRepository.findByPet(petId)).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Employee saveEmployee(EmployeeDTO employee) {
        Employee empl = mapper.map(employee, Employee.class);
        return employeeRepository.saveAndFlush(empl);
    }

    @Override
    public Employee getEmployee(long employeeId) {
        return employeeRepository.getOne(employeeId);
    }

    @Override
    public void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        employee.setDaysAvailable(daysAvailable);
        employeeRepository.save(employee);
    }

    @Override
    public List<Employee> findEmployeeByService(EmployeeRequestDTO employeeRequest) {
        List<Employee> availableByDate = employeeRepository.findByDaysAvailable(employeeRequest.getDate().getDayOfWeek());
        return availableByDate.stream().filter(e -> e.getSkills().containsAll(employeeRequest.getSkills())).collect(Collectors.toList());
    }
}
