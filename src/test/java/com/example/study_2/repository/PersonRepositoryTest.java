package com.example.study_2.repository;

import com.example.study_2.domain.Person;
import com.example.study_2.domain.dto.Birthday;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Transactional
@SpringBootTest
class PersonRepositoryTest {
    @Autowired
    private PersonRepository personRepository;

    @Test
    void findByName(){
        List<Person> people = personRepository.findByName("tony");
        Assertions.assertThat(people.size()).isEqualTo(1);

        Person person = people.get(0);
        org.junit.jupiter.api.Assertions.assertAll(
                () -> Assertions.assertThat(person.getName()).isEqualTo("tony"),
                () -> Assertions.assertThat(person.getHobby()).isEqualTo("reading"),
                () -> Assertions.assertThat(person.getAddress()).isEqualTo("Seoul"),
                () -> Assertions.assertThat(person.getBirthday()).isEqualTo(Birthday.of(LocalDate.of(1991,7,10))),
                () -> Assertions.assertThat(person.getJob()).isEqualTo("officer"),
                () -> Assertions.assertThat(person.getPhoneNumber()).isEqualTo("010-2222-5555"),
                () -> Assertions.assertThat(person.isDeleted()).isEqualTo(false)
        );
    }

    @Test
    void findByNameIfDeleted(){
        List<Person> people = personRepository.findByName("andrew");

        Assertions.assertThat(people.size()).isEqualTo(0);
    }

    @Test
    void findByMonthOfBirthday(){
        List<Person> people = personRepository.findByMonthOfBirthday(7);

        Assertions.assertThat(people.size()).isEqualTo(2);
        org.junit.jupiter.api.Assertions.assertAll(
                () -> Assertions.assertThat(people.get(0).getName()).isEqualTo("david"),
                () -> Assertions.assertThat(people.get(1).getName()).isEqualTo("tony")
        );
    }

    @Test
    void findPeopleDeleted(){
        List<Person> people = personRepository.findPeopleDeleted();

        Assertions.assertThat(people.size()).isEqualTo(1);
        Assertions.assertThat(people.get(0).getName()).isEqualTo("andrew");
    }
}