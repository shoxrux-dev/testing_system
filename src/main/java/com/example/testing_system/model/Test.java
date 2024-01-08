package com.example.testing_system.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@ToString
@Table(name = "tests")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Test extends BaseModel implements Comparable<Test> {
    @Column(unique = true)
    String question;
    String answer;
    String answers;
    @ManyToOne()
    Category category;

    @Override
    public int compareTo(Test o) {
        return this.getId().compareTo(o.getId());
    }
}