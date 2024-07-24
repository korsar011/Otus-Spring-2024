package ru.otus.hw.service;

import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

public interface TestService {
    TestResult executeTestFor(Student student);
    List<String> getCorrectAnswers();
}