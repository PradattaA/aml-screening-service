package com.pradatta.amlscreening.jpa.repository;

import com.pradatta.amlscreening.jpa.datamodel.FinancialSanctionedEntity;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


@Configuration
public interface FinancialSanctionedEntityRepository extends JpaRepository<FinancialSanctionedEntity, Long> {
    Optional<FinancialSanctionedEntity> findByLogicalId(Long logicalId);

    Optional<FinancialSanctionedEntity> findByWholeName(String wholeName);
}
