package cz.greyson.slama.homework.Controller;

import cz.greyson.slama.homework.model.Student;
import cz.greyson.slama.homework.repository.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//ToDo service layer
//ToDo HATEOAS
//ToDo Security?
@RestController
class StudentController {

    private final StudentRepository repository;

    StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    @GetMapping("/students/{id}")
    public Student getStudent(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @PostMapping("/students")
    public Student createStudent(@RequestBody Student newStudent) {
        return repository.save(newStudent);
    }

    @PutMapping("/students/{id}")
    public Student replaceStudent(@RequestBody Student newStudent, @PathVariable Long id) {
        return repository.findById(id)
                .map(student -> {
                    student.setName(newStudent.getName());
                    student.setSurname(newStudent.getSurname());
                    student.setStreet(newStudent.getStreet());
                    student.setTown(newStudent.getTown());
                    student.setZipCode(newStudent.getZipCode());
                    student.setDateBirth(newStudent.getDateBirth());
                    return repository.save(student);
                })
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @DeleteMapping("/students/{id}")
    public void deleteStudent(@PathVariable Long id) {
        repository.findById(id).ifPresentOrElse(student -> repository.deleteById(id), () -> {
            throw new StudentNotFoundException(id);
        });
    }
}