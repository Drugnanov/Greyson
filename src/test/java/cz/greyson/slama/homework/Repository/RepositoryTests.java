package cz.greyson.slama.homework.Repository;

import cz.greyson.slama.homework.HomeworkApplication;
import cz.greyson.slama.homework.model.Student;
import cz.greyson.slama.homework.repository.StudentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        HomeworkApplication.class,
})
public class RepositoryTests {

    @Autowired
    private StudentRepository repository;

    private Student newStudent;

    @Before
    public void setUp() throws Exception {
        newStudent = new Student("Michal", "Sláma", "5.Května 25", "Prague", 14000, LocalDate.of(1984, Month.FEBRUARY, 12));
        repository.save(newStudent);
        repository.flush();
    }

    @Test
    public void testCreateNewStudent() {
        Optional<Student> found = repository.findById(newStudent.getId());
        assertTrue("Student with id 3 was not found.", found.isPresent());
        assertEquals("Student was wrongly saved.", newStudent, found.get());
    }

    @Test
    public void testEditStudent() {
        Optional<Student> found = repository.findById(newStudent.getId());
        assertTrue("Student with id 3 was not found.", found.isPresent());
        Student studentToEdit = found.get();
        studentToEdit.setName("MichalEdit");
        repository.save(studentToEdit);
        repository.flush();

        found = repository.findById(3L);
        assertTrue("Student with id 3 was not found.", found.isPresent());
        assertEquals("Student was wrongly edited.", "MichalEdit", found.get().getName());
    }
}
