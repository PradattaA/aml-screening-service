package com.pradatta.amlscreening.intSpec;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pradatta.amlscreening.api.response.AmlScreeningResponse;
import com.pradatta.amlscreening.api.response.ScreeningStatusColour;
import com.pradatta.amlscreening.jpa.datamodel.FinancialSanctionedEntity;
import com.pradatta.amlscreening.jpa.repository.FinancialSanctionedEntityRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import static com.pradatta.amlscreening.jpa.datamodel.SanctionEntityType.PERSON;
import static com.pradatta.amlscreening.jpa.datamodel.SanctionEntityType.valueOf;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@ActiveProfiles(profiles = {"integrationtest"})
public class SanctionedNameRestControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Resource
    private FinancialSanctionedEntityRepository financialSanctionedEntityRepository;

    @BeforeEach
    public void beforeEach() {

        String[][] names = {
                {"PERSON", "John", "", "Doe", "John Doe"},
                {"PERSON", "Malcolm", "", "Function", "Malcolm Function"},
                {"PERSON", "Archibald", "", "Northbottom", "Archibald Northbottom"},
                {"PERSON", "Rodney", "", "Artichoke", "Rodney Artichoke"},
                {"PERSON", "Harry", "James", "Potter", "Harry James Potter"},
                {"PERSON", "Ronald", "", "Weasley", "Ronald Weasley"},
                {"PERSON", "Osama", "", "Bin Laden", "Osama Bin Laden"},
                {"ENTERPRISE", "", "", "", "Umbrella Corp."},
        };


        for (String[] name : names) {
            FinancialSanctionedEntity financialSanctioned = new FinancialSanctionedEntity(
                    valueOf(name[0]), new Random().nextLong(), name[1], name[2], name[3], name[4],
                    new Date(), "www.testurl.com");
            financialSanctionedEntityRepository.save(financialSanctioned);
        }
        financialSanctionedEntityRepository.flush();
    }

    @AfterEach
    public void afterEach() {
        financialSanctionedEntityRepository.deleteAll();
        financialSanctionedEntityRepository.flush();
    }

    @Test
    @DisplayName("When find by ID, returns correct Sanctioned Entity")
    public void whenFindByIdThenReturnSanctionedEntity() throws Exception {
        mvc.perform(get("/sanctioned-list/api/v1/find/{id}", 1)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                                   .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.type", is("PERSON")))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.wholeName", is("John Doe")));
    }

    @Test
    @DisplayName("When add new Entity, new Sanctioned Entity is added")
    public void whenAddThenSanctionedEntityIsAdded() throws Exception {

        //given
        FinancialSanctionedEntity sanctionedEntity = new FinancialSanctionedEntity(
                PERSON, new Random().nextLong(), "Harry", "James", "Potter", "Harry Potter",
                new Date(), "www.testurl.com");

        //then
        AtomicReference<FinancialSanctionedEntity> returnEntity = new AtomicReference<>();
        mvc.perform(post("/sanctioned-list/api/v1/add")
                            .content(asJsonString(sanctionedEntity))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    returnEntity.set((FinancialSanctionedEntity) convertJSONStringToObject(json,
                                                                                           FinancialSanctionedEntity.class));
                });
        ;

        //add
        Optional<FinancialSanctionedEntity> storedEntity = financialSanctionedEntityRepository.findById(
                returnEntity.get().getId());

        assertThat(storedEntity.isPresent()).isTrue();
        assertThat(storedEntity.get().getWholeName()).isEqualTo(sanctionedEntity.getWholeName());
    }

    @Test
    @DisplayName("When update endpoint called, Sanctioned Entity is updated")
    public void updateSanctionedEntityWorks() throws Exception {
        //given
        FinancialSanctionedEntity sanctionedEntity = financialSanctionedEntityRepository.findByWholeName(
                "Rodney Artichoke").get();

        sanctionedEntity.setMiddleName("Gregory");

        //then
        mvc.perform(put("/sanctioned-list/api/v1/update/{id}", 2)
                            .content(asJsonString(sanctionedEntity))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.middleName").value("Gregory"));
    }

    @ParameterizedTest(name = "When passed name is {0} -  Expected result is: {1}")
    @DisplayName("AML Screening Checks, returns correct Sanctioned Entity")
    @CsvSource(value = {
            "Harry Potter           | RED",
            "Harry James Potter     | RED",
            "Harry J Potter         | RED",
            "Harry J. Potter        | RED",
            "Potter Harry           | RED",
            "Potter, Harry          | RED",
            "Potter, Harry James    | RED",
            "Potter Harry J         | RED",
            "Potter Harry J.        | RED",
            "H J Potter             | RED",
            "Harold Potter          | GREEN",
            "Harri Patter           | AMBER",
            "Horri Puttar           | GREEN",
            "Harold Potts           | GREEN",
            "H J P                  | GREEN",
            "Osama Laden            | RED",
            "Osama Bin Laden        | RED",
            "Bin Laden, Osama       | RED",
            "Laden Osama Bin        | RED",
            "to the osama bin laden | RED",
            "osama and bin laden    | RED",
            "Umbrella Corporation   | RED",
            "Umbrella Blues Inc.    | GREEN"

    }, delimiter = '|')
    public void whenSearchedWithNameReturnSanctionedEntityWithScore(String name,
                                                                    String expectedStatus) throws Exception {
        MvcResult result = mvc.perform(get("/aml-screening/api/v1/check/{name}", name)
                                               .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();
        String json = result.getResponse().getContentAsString();
        AmlScreeningResponse amlScreeningResponse = (AmlScreeningResponse) convertJSONStringToObject(json,
                                                                                                     AmlScreeningResponse.class);
        assertThat(amlScreeningResponse.getStatusColour()).isEqualTo(ScreeningStatusColour.valueOf(expectedStatus));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Object convertJSONStringToObject(String json, Class<T> objectClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);
        return mapper.readValue(json, objectClass);
    }

}
