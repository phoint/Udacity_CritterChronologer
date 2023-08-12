package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;

import java.util.List;

public interface ScheduleService {
    Schedule createSchedule(ScheduleDTO scheduleDTO);
    List<Schedule> getAllSchedule();
    List<Schedule> getScheduleForPet(long petId);
    List<Schedule> getScheduleForEmployee(long employeeId);
    List<Schedule> getScheduleForCustomer(long customerId);
}
