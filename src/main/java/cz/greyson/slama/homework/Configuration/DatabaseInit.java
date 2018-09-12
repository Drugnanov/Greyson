package cz.greyson.slama.homework.Configuration;

import cz.greyson.slama.homework.repository.StudentRepository;
import cz.greyson.slama.homework.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

//init data - should be removed for production environment
@Configuration
@Slf4j
public class DatabaseInit {

    @Bean
    CommandLineRunner initDatabase(StudentRepository repository) {
        return args -> {
            for (Student student :getInitStudents()) {
                log.info("Preloading " + repository.save(student));
            }
        };
    }

    public static List<Student> getInitStudents(){
        return INIT_STUDENTS;
    }

    private static final List<Student> INIT_STUDENTS = new ArrayList<>(){{
        add(new Student("Michal", "Sláma", "5.Května 25", "Prague", 14000, LocalDate.of(1984, Month.FEBRUARY, 12)));
        add(new Student("Jan", "Pokorný", "Jiráskova 15", "Prague", 14300, LocalDate.of(1994, Month.APRIL, 20)));
    }};
}
