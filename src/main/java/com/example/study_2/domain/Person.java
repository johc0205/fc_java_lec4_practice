package com.example.study_2.domain;

import jdk.vm.ci.meta.Local;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Person {
    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String name;
    @NonNull
    private int age;

    private String hobby;
    @NonNull
    private String bloodType;

    private String address;

    private LocalDate birthday;

    private String job;

    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true )
    @ToString.Exclude
    //cascade는 폭포수라는 의미인데 Person에서 Block에 대한 영속성을 함께 관리하겠다는 의미
    private Block block;

}
