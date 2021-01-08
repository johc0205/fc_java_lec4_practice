package com.example.study_2.service;

import com.example.study_2.domain.Block;
import com.example.study_2.domain.Person;
import com.example.study_2.repository.BlockRepository;
import com.example.study_2.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PersonService {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private BlockRepository blockRepository;

    public List<Person> getPeopleExcludeBlocks(){
        //List<Person> people = personRepository.findAll();
//        List<Block> blocks = blockRepository.findAll();
//        List<String> blockNames = blocks.stream().map(Block::getName).collect(Collectors.toList()); //getname만 뽑아오는것
    //filter는 어떤 조건에 일치하는 것만 돌려주는 함수
        //return people.stream().filter(person -> person.getBlock() == null).collect(Collectors.toList());
        // 위는 blockNames에 person.getName()이 없다면 포함시키겠다는 의미

        return personRepository.findByBlockIsNull();
    }
    @Transactional(readOnly = true)
    public Person getPerson(Long id){
        Person person = personRepository.findById(id).get();

        log.info("person : {}",person);
        return person;
    }
    public List<Person> getPeopleByName(String name){
//        List<Person> people = personRepository.findAll();
//
//        return people.stream().filter(person -> person.getName().equals(name)).collect(Collectors.toList());
        return personRepository.findByName(name);
    }
}
