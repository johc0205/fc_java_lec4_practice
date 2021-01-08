package com.example.study_2.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDate;

@Entity
@ToString
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class Block {

    @Id
    @GeneratedValue
    private Long id;
    @NonNull
    private String name;

    private String reason;

    private LocalDate startDate;

    private LocalDate endDate;

}
