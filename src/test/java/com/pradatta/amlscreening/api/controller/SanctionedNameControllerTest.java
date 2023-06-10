package com.pradatta.amlscreening.api.controller;

import com.pradatta.amlscreening.jpa.datamodel.FinancialSanctionedEntity;
import com.pradatta.amlscreening.jpa.repository.FinancialSanctionedEntityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.Optional;

import static com.pradatta.amlscreening.jpa.datamodel.SanctionEntityType.PERSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SanctionedNameControllerTest {

    @InjectMocks
    SanctionedNameController sanctionedNameController;

    @Mock
    FinancialSanctionedEntityRepository financialSanctionedEntityRepository;

    @Test
    @DisplayName("Controller returns one sanctioned entity by ID")
    void findOne() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        FinancialSanctionedEntity financialSanctioned = new FinancialSanctionedEntity(PERSON, 0L, "", "John", "", "Doe",
                                                                                      new Date(), "");
        financialSanctioned.setId(1L);
        when(financialSanctionedEntityRepository.findById(any())).thenReturn(Optional.of(financialSanctioned));

        FinancialSanctionedEntity sanctionedEntity = sanctionedNameController.findOne(1L);
        assertThat(sanctionedEntity.getId()).isEqualTo(1L);
    }

    @Test
    void newSanctionedEntity() {
    }

    @Test
    void replaceSanctionedEntity() {
    }

    @Test
    void deleteSanctionedEntity() {
    }
}