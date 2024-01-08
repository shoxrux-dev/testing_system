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
@Table(name = "solved_test")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SolvedTest extends BaseModel {
    boolean isRight;
    String ansByUser;
    String question;
}
