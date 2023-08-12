package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.service.PetService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    private final PetService petService;
    private final ModelMapper mapper;

    @Autowired
    public PetController(PetService petService, ModelMapper mapper) {
        this.petService = petService;
        this.mapper = mapper;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = petService.save(petDTO);
        return buildPetResponse(pet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.getPet(petId);
        if (Objects.isNull(pet)) {
            return null;
        }
        return buildPetResponse(pet);
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> pets = petService.getPets();
        return pets.stream().map(this::buildPetResponse).collect(Collectors.toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets = petService.getPetsByOwner(ownerId);
        return pets.stream().map(this::buildPetResponse).collect(Collectors.toList());
    }

    private PetDTO buildPetResponse(Pet pet) {
        PetDTO response = mapper.map(pet, PetDTO.class);
        response.setOwnerId(pet.getCustomer().getId());
        return response;
    }

}
