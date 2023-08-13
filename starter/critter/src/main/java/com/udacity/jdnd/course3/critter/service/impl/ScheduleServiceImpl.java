package com.udacity.jdnd.course3.critter.service.impl;

import com.udacity.jdnd.course3.critter.dao.CustomerRepository;
import com.udacity.jdnd.course3.critter.dao.EmployeeRepository;
import com.udacity.jdnd.course3.critter.dao.PetRepository;
import com.udacity.jdnd.course3.critter.dao.ScheduleRepository;
import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final PetRepository petRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper mapper;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, PetRepository petRepository, EmployeeRepository employeeRepository, CustomerRepository customerRepository, ModelMapper mapper) {
        this.scheduleRepository = scheduleRepository;
        this.petRepository = petRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    /**
     * Create new schedule for pets with available activities
     * @param scheduleDTO representing an available day for booking activities
     * @return a Schedule containing the available day, employees, pets and activities stored in database
     */
    @Override
    public Schedule createSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = mapper.map(scheduleDTO, Schedule.class);
        List<Pet> pets = scheduleDTO.getPetIds().stream().map(id -> {
            Pet pet = petRepository.getOne(id);
            pet.getSchedule().add(schedule);
            return pet;
        }).collect(Collectors.toList());
        List<Employee> employees = scheduleDTO.getEmployeeIds().stream().map(id -> {
            Employee employee = employeeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            employee.getSchedule().add(schedule);
            return employee;
        }).collect(Collectors.toList());
        schedule.setPets(pets);
        schedule.setEmployees(employees);
        return scheduleRepository.save(schedule);
    }

    /**
     * Retrieve all schedule on database
     * @return a List of existed Schedule
     */
    @Override
    public List<Schedule> getAllSchedule() {
        return scheduleRepository.findAll();
    }

    /**
     * Retrieve all schedule for a pet
     * @param petId representing the id of pet
     * @return a List of existed Schedule
     */
    @Override
    public List<Schedule> getScheduleForPet(long petId) {
        return scheduleRepository.findAllByPets_Id(petId);
    }

    /**
     * Retrieve all schedule for a employee
     * @param employeeId representing the id of Employee
     * @return a List of existed Schedule
     */
    @Override
    public List<Schedule> getScheduleForEmployee(long employeeId) {
        return scheduleRepository.findAllByEmployees_Id(employeeId);
    }

    /**
     * Retrieve all schedule for a customer
     * @param customerId representing the id of Customer
     * @return a List of existed Schedule
     */
    @Override
    public List<Schedule> getScheduleForCustomer(long customerId) {
        Customer customer = customerRepository.getOne(customerId);
        List<Pet> pets = customer.getPets();
        return pets.stream().flatMap(pet -> scheduleRepository.findAllByPets_Id(pet.getId()).stream()).collect(Collectors.toList());
    }
}
