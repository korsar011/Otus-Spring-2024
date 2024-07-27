package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.domain.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest (classes = {StudentServiceImpl.class})
public class StudentServiceImplTest {

    @Autowired
    private StudentServiceImpl studentService;

    @MockBean
    private LocalizedIOService localizedIOService;

    @BeforeEach
    void setUp() {
        when(localizedIOService.readStringWithPromptLocalized("StudentService.input.first.name")).thenReturn("John");
        when(localizedIOService.readStringWithPromptLocalized("StudentService.input.last.name")).thenReturn("Doe");
    }

    @Test
    void determineCurrentStudent_ReturnsCorrectStudent() {
        Student student = studentService.determineCurrentStudent();

        assertEquals("John", student.firstName());
        assertEquals("Doe", student.lastName());
    }
}