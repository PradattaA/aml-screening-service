package com.pradatta.amlscreening.jpa.repository;

import com.pradatta.amlscreening.jpa.datamodel.FinancialSanctionedEntity;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


@Configuration
public interface FinancialSanctionedEntityRepository extends JpaRepository<FinancialSanctionedEntity, Long> {
    List<FinancialSanctionedEntity> findByLogicalId(Long logicalId);

    Optional<FinancialSanctionedEntity> findByWholeName(String wholeName);

    /**
     * Need to fetch close enough matched form the DB
     * Ideally need to use something like SOUNDEX, TRIGRAMS
     * @param name Name
     * @return Lists of matching entity
     */
    List<FinancialSanctionedEntity> findByWholeNameIgnoreCaseContaining(String name);

    List<FinancialSanctionedEntity> findByFirstNameSoundex(String soundex);
    List<FinancialSanctionedEntity> findByMiddleNameSoundex(String soundex);
    List<FinancialSanctionedEntity> findByLastNameSoundex(String soundex);
    List<FinancialSanctionedEntity> findByWholeNameSoundex(String soundex);

    List<FinancialSanctionedEntity> findByWholeNameContaining(String name);
}
