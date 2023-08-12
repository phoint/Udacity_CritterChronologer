package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetDTO;

import java.util.List;

public interface PetService {
    Pet save(PetDTO petDTO);
    Pet getPet(long id);
    List<Pet> getPets();
    List<Pet> getPetsByOwner(long ownerId);
}
