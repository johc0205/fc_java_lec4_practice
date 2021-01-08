package com.example.study_2.repository;

import com.example.study_2.domain.Person;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PersonRepositoryTest {
    @Autowired
    private PersonRepository personRepository;
    @Test
    void crud(){
        Person person = new Person();
        person.setName("martin");
        person.setAge(10);
        person.setBloodType("A");

        personRepository.save(person);

        List<Person> people = personRepository.findAll();

        Assertions.assertThat(people.size()).isEqualTo(1);
        Assertions.assertThat(people.get(0).getName()).isEqualTo("martin");
        Assertions.assertThat(people.get(0).getAge()).isEqualTo(10);
        Assertions.assertThat(people.get(0).getBloodType()).isEqualTo("A");

        System.out.println(personRepository.findAll());
    }

    @Test
    void findByBloodType(){
        givenPerson("martin",10,"A");
        givenPerson("david",9,"B");
        givenPerson("dennis",8,"O");
        givenPerson("sophia",7,"AB");
        givenPerson("benny", 6, "A");
        givenPerson("John", 5, "A");

        List<Person> result = personRepository.findByBloodType("A");

        result.forEach(System.out::println);

    }
    @Test
    void findByBirthdayBetween(){
        givenPerson("martin",10,"A",LocalDate.of(1991,8,15));
        givenPerson("david",9,"B",LocalDate.of(1995,2,5));
        givenPerson("dennis",8,"O",LocalDate.of(1993,1,5));
        givenPerson("sophia",7,"AB",LocalDate.of(1994,6,30));
        givenPerson("benny", 6, "A",LocalDate.of(1995,8,30));

        List<Person> result = personRepository.findByBirthdayBetween(LocalDate.of(1991,8,1),LocalDate.of(1991,8,31));

        result.forEach(System.out::println);

    }
    private void givenPerson(String name, int age, String bloodType){
        givenPerson(name,age, bloodType, null);
    } // -> 이렇게 썼던것을 또 사용하는 것을 메쏘드 오버로딩이라고 한다.
    private void givenPerson(String name, int age, String bloodType, LocalDate birthday){
        Person person= new Person(name,age,bloodType) ;
        person.setBirthday(birthday);
        personRepository.save(person);
    }
    @Test
    void hashCodeAndEquals(){
        Person person1 = new Person("martin", 10, "A");
        Person person2 = new Person("martin",10, "A");

        System.out.println(person1.equals(person2));
        System.out.println(person1.hashCode());
        System.out.println(person2.hashCode());
    }

}