package com.example.study_2.controller;

import com.example.study_2.controller.dto.PersonDto;
import com.example.study_2.domain.Person;
import com.example.study_2.domain.dto.Birthday;
import com.example.study_2.repository.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@Transactional
class PersonControllerTest {
    @Autowired
    private PersonController personController;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MappingJackson2HttpMessageConverter messageConverter;

    private MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(personController).setMessageConverters(messageConverter).build();
    }//매 테스트마다 한번씩 실행된다.

    @Test
    void getPerson() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/person/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("martin"))
                .andExpect(jsonPath("$.hobby").isEmpty())
                .andExpect(jsonPath("$.address").isEmpty())
                .andExpect(jsonPath("$.birthday").value("1991-08-15"))
                .andExpect(jsonPath("$.job").isEmpty())
                .andExpect(jsonPath("$.phoneNumber").isEmpty())
                .andExpect(jsonPath("$.deleted").value(false))
                .andExpect(jsonPath("$.age").isNumber())
                .andExpect(jsonPath("$.birthdayToday").isBoolean());
        //달러는 객체를 의미
    }

    @Test
    void postPerson() throws Exception {
        PersonDto dto = PersonDto.of("martin", "programming","송도", LocalDate.now(),"programmer","010-1111-2222");

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/person")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(toJsonString(dto)))
                .andDo(print())
                .andExpect(status().isCreated());

        Person result = personRepository.findAll(Sort.by(Sort.Direction.DESC,"id")).get(0);

        Assertions.assertAll(
                () -> org.assertj.core.api.Assertions.assertThat(result.getName()).isEqualTo("martin"),
                () -> org.assertj.core.api.Assertions.assertThat(result.getAddress()).isEqualTo("송도"),
                () -> org.assertj.core.api.Assertions.assertThat(result.getHobby()).isEqualTo("programming"),
                () -> org.assertj.core.api.Assertions.assertThat(result.getBirthday()).isEqualTo(Birthday.of(LocalDate.now())),
                () -> org.assertj.core.api.Assertions.assertThat(result.getJob()).isEqualTo("programmer"),
                () -> org.assertj.core.api.Assertions.assertThat(result.getPhoneNumber()).isEqualTo("010-1111-2222")
        );
    }

    @Test
    void modifyPerson() throws Exception {
        PersonDto dto = PersonDto.of("martin", "programming","송도", LocalDate.now(),"programmer","010-1111-2222");

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/person/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(toJsonString(dto)))
                .andDo(print())
                .andExpect(status().isOk());

        Person result = personRepository.findById(1L).get();

        Assertions.assertAll(
                () -> org.assertj.core.api.Assertions.assertThat(result.getName()).isEqualTo("martin"),
                () -> org.assertj.core.api.Assertions.assertThat(result.getAddress()).isEqualTo("송도"),
                () -> org.assertj.core.api.Assertions.assertThat(result.getHobby()).isEqualTo("programming"),
                () -> org.assertj.core.api.Assertions.assertThat(result.getBirthday()).isEqualTo(Birthday.of(LocalDate.now())),
                () -> org.assertj.core.api.Assertions.assertThat(result.getJob()).isEqualTo("programmer"),
                () -> org.assertj.core.api.Assertions.assertThat(result.getPhoneNumber()).isEqualTo("010-1111-2222")
        );


    }

    @Test
    void modifyPersonIfNameDifferent() throws Exception {
        PersonDto dto = PersonDto.of("james", "programming","송도", LocalDate.now(),"programmer","010-1111-2222");

        Assertions.assertThrows(NestedServletException.class, ()->mockMvc.perform(
                MockMvcRequestBuilders.put("/api/person/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(toJsonString(dto)))
                .andDo(print())
                .andExpect(status().isOk()));
    }

    @Test
    void modifyName() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/person/1")
                        .param("name", "martinModified"))
                .andDo(print())
                .andExpect(status().isOk());

        org.assertj.core.api.Assertions.assertThat(personRepository.findById(1L).get().getName()).isEqualTo("martinModified");
    }

    @Test
    void deletePerson() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/person/1"))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertTrue(personRepository.findPeopleDeleted().stream().anyMatch(person -> person.getId().equals(1L)));
    }

    private String toJsonString(PersonDto personDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(personDto);
    }
}