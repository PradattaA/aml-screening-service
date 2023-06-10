package com.pradatta.amlscreening.api.controller;

import com.pradatta.amlscreening.api.error.EntityNotFoundException;
import com.pradatta.amlscreening.jpa.datamodel.FinancialSanctionedEntity;
import com.pradatta.amlscreening.jpa.repository.FinancialSanctionedEntityRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/sanctioned-list/api/v1")
public class SanctionedNameController {

    private final FinancialSanctionedEntityRepository repository;

    SanctionedNameController(FinancialSanctionedEntityRepository repository) {
        this.repository = repository;
    }

    // Find a single item
    @GetMapping("/find/{id}")
    FinancialSanctionedEntity findOne(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    @PostMapping("/add")
    ResponseEntity<FinancialSanctionedEntity>  newSanctionedEntity(@RequestBody FinancialSanctionedEntity newSanctionedEntity) {
        FinancialSanctionedEntity financialSanctioned = repository.save(newSanctionedEntity);
        return new ResponseEntity<>(financialSanctioned, CREATED);
    }

    @PutMapping("/update/{id}")
    FinancialSanctionedEntity replaceSanctionedEntity(@RequestBody FinancialSanctionedEntity newSanctionedEntity, @PathVariable Long id) {

        return repository.findById(id)
                .map(sanctionedEntity -> {
                    sanctionedEntity.setType(newSanctionedEntity.getType());
                    sanctionedEntity.setLogicalId(newSanctionedEntity.getLogicalId());
                    sanctionedEntity.setFirstName(newSanctionedEntity.getFirstName());
                    sanctionedEntity.setMiddleName(newSanctionedEntity.getMiddleName());
                    sanctionedEntity.setLastName(newSanctionedEntity.getLastName());
                    sanctionedEntity.setWholeName(newSanctionedEntity.getWholeName());
                    sanctionedEntity.setPublicationDate(newSanctionedEntity.getPublicationDate());
                    sanctionedEntity.setPublicationUrl(newSanctionedEntity.getPublicationUrl());
                    return repository.save(sanctionedEntity);
                })
                .orElseGet(() -> {
                    newSanctionedEntity.setId(id);
                    return repository.save(newSanctionedEntity);
                });
    }

    @DeleteMapping("/delete/{id}")
    void deleteSanctionedEntity(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
