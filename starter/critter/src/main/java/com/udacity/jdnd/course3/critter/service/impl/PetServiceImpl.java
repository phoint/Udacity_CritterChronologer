package com.udacity.jdnd.course3.critter.service.impl;

import com.udacity.jdnd.course3.critter.dao.CustomerRepository;
import com.udacity.jdnd.course3.critter.dao.PetRepository;
import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.user.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetServiceImpl implements PetService {
    private final PetRepository petRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper mapper;

    @Autowired
    public PetServiceImpl(PetRepository petRepository, CustomerRepository customerRepository, ModelMapper mapper) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    /**
     * Save new pet with existed customer to database
     * @param petDTO representing the information of a pet
     * @return a Pet containing stored information
     */
    @Override
    public Pet save(PetDTO petDTO) {
        Pet pet = mapper.map(petDTO, Pet.class);
        Customer owner = customerRepository.getOne(petDTO.getOwnerId());
        pet.setCustomer(owner);
        Pet newPet = petRepository.save(pet);
        owner.getPets().add(newPet);
        return newPet;
    }

    /**
     * Retrieve the pet with id
     * @param id representing the id of Pet
     * @return a existed Pet from database
     */
    @Override
    public Pet getPet(long id) {
        Optional<Pet> petOptional = petRepository.findById(id);
        return petOptional.orElse(null);
    }

    /**
     * Retrieve all pets in database
     * @return a List of existed Pet
     */
    @Override
    public List<Pet> getPets() {
        return petRepository.findAll();
    }

    /**
     * Retrieve all pets of a customer
     * @param ownerId representing the id of customer
     * @return a list of existed Pet
     */
    @Override
    public List<Pet> getPetsByOwner(long ownerId) {
        return petRepository.findByCustomerId(ownerId);
    }
}
