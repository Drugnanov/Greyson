package cz.greyson.slama.homework.Controller;

import cz.greyson.slama.homework.Configuration.DatabaseInit;
import cz.greyson.slama.homework.HomeworkApplication;
import cz.greyson.slama.homework.model.Student;
import cz.greyson.slama.homework.repository.StudentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;

//ToDo correct exception handling with http status checking
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HomeworkApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @Autowired
    private StudentRepository repository;

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private HttpHeaders headers = new HttpHeaders();

    private Student student = new Student("RestStudent", "fake", "Kaprova 25", "Prague", 20, LocalDate.of(2000, 2, 22));
    private Student newStudent = new Student("RestStudentNew", "fake", "Kaprova 25", "Prague", 20, LocalDate.of(2000, 2, 22));

    private List<Student> initStudents = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        repository.deleteAllInBatch();
        for (Student student : DatabaseInit.getInitStudents()) {
            initStudents.add(repository.save(student));
        }
        initStudents.add(repository.save(student));
        repository.flush();
    }

    @Test
    public void testGetAllStudents() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                createURLWithPort("/students"),
                HttpMethod.GET, entity, new ParameterizedTypeReference<List<Student>>() {
                });

        assertEquals(initStudents, response.getBody());
    }

    @Test
    public void testGetStudent() {
        assertEquals(student, getStudent(student.getId()));
    }

    @Test(expected = RestClientException.class)
    public void testGetNonExistingStudent() {
        assertEquals(student, getStudent(Long.MAX_VALUE));
    }

    @Test
    public void testCreateStudent() {
        HttpEntity<Student> entity = new HttpEntity<>(newStudent, headers);

        ResponseEntity<Student> response = restTemplate.exchange(
                createURLWithPort("/students"),
                HttpMethod.POST, entity, new ParameterizedTypeReference<Student>() {
                });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        newStudent.setId(response.getBody().getId());

        assertEquals(newStudent, getStudent(response.getBody().getId()));
    }

    @Test
    public void testReplaceExistingStudent() {
        student.setName("edited");
        HttpEntity<Student> entity = new HttpEntity<>(student, headers);

        ResponseEntity<Student> response = restTemplate.exchange(
                createURLWithPort("/students/" + student.getId()),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<Student>() {
                });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(student, response.getBody());

        assertEquals(student, getStudent(student.getId()));
    }

    @Test(expected = RestClientException.class)
    public void testReplaceNonExistingStudent() {
        HttpEntity<Student> entity = new HttpEntity<>(newStudent, headers);

        ResponseEntity<Student> response = restTemplate.exchange(
                createURLWithPort("/students/" + Long.MAX_VALUE),
                HttpMethod.PUT, entity, new ParameterizedTypeReference<Student>() {
                });
    }

    @Test
    public void testDeleteStudent() {
        HttpEntity<Student> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Student> response = restTemplate.exchange(
                createURLWithPort("/students/" + student.getId()),
                HttpMethod.DELETE, entity, new ParameterizedTypeReference<Student>() {
                });
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test(expected = RestClientException.class)
    public void testDeleteNonExistingStudent() {
        HttpEntity<Student> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Student> response = restTemplate.exchange(
                createURLWithPort("/students/" + Long.MAX_VALUE),
                HttpMethod.DELETE, entity, new ParameterizedTypeReference<Student>() {
                });
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private Student getStudent(long id){
        HttpEntity<Student> entity = new HttpEntity<>(null, headers);
        ResponseEntity<Student> response = restTemplate.exchange(
                createURLWithPort("/students/" + id),
                HttpMethod.GET, entity, new ParameterizedTypeReference<Student>() {
                });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
