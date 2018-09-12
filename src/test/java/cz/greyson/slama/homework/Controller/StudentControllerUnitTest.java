package cz.greyson.slama.homework.Controller;

import cz.greyson.slama.homework.model.Student;
import cz.greyson.slama.homework.repository.StudentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StudentController.class)
public class StudentControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StudentRepository repository;

    @Test
    public void testGetAllStudents()
            throws Exception {
        Student student = new Student("RestStudent", "fake", "Kaprova 25", "Prague", 20, LocalDate.of(2000, 2, 22));
        List<Student> allStudents = Arrays.asList(student);

        given(repository.findAll()).willReturn(allStudents);

        mvc.perform(get("/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(student.getName())));
    }

    //ToDo more tests...
}
