package cz.greyson.slama.homework.repository;

import cz.greyson.slama.homework.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
