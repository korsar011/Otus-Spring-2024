package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.domain.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class StudentServiceImplTest {

    @Mock
    private LocalizedIOService ioService;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void determineCurrentStudent_ReturnsCorrectStudent() {
        String firstName = "John";
        String lastName = "Doe";
        when(ioService.readStringWithPromptLocalized("StudentService.input.first.name")).thenReturn(firstName);
        when(ioService.readStringWithPromptLocalized("StudentService.input.last.name")).thenReturn(lastName);

        Student student = studentService.determineCurrentStudent();

        assertEquals(firstName, student.firstName());
        assertEquals(lastName, student.lastName());
    }
}