package com.example.testing_system.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@ToString
@Table(name = "solved_tests")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SolvedTests extends BaseModel implements Comparable<SolvedTests> {
    int truePercentage;
    int falsePercentage;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<SolvedTest> solvedTestList;

    @ManyToOne()
    User user;

    @Override
    public int compareTo(SolvedTests o) {
        return this.getId().compareTo(o.getId());
    }
}
