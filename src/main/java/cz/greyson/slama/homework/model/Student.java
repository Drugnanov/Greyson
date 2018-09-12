package cz.greyson.slama.homework.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Data  //ToDo why a non arguments constructor is not generated
@Entity
public class Student {
    private @Id @GeneratedValue Long id;
    private String name;
    private String surname;
    private String street;
    private String town;
    private Integer zipCode;
    private LocalDate dateBirth;

    public Student(String name, String surname, String street, String town, Integer zipCode, LocalDate dateBirth) {
        this.name = name;
        this.surname = surname;
        this.street = street;
        this.town = town;
        this.zipCode = zipCode;
        this.dateBirth = dateBirth;
    }

    private Student() {
    }
}