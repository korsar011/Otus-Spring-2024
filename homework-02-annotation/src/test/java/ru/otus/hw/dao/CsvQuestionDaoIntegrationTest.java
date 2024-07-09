package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CsvQuestionDaoIntegrationTest {

    private CsvQuestionDao csvQuestionDao;
    private TestFileNameProvider fileNameProvider;

    @BeforeEach
    public void setUp() {
        fileNameProvider = Mockito.mock(TestFileNameProvider.class);
        when(fileNameProvider.getTestFileName()).thenReturn("test_questions.csv");
        csvQuestionDao = new CsvQuestionDao(fileNameProvider);
    }

    @Test
    public void testFindAll() {
        List<Question> questions = csvQuestionDao.findAll();
        assertNotNull(questions);
        assertEquals(7, questions.size()); // В тестовом файле с вопросами должно быть 7 вопросов
    }

    @Test
    public void testFileNotFound() {
        when(fileNameProvider.getTestFileName()).thenReturn("nonexistent-file.csv");

        QuestionReadException exception = assertThrows(QuestionReadException.class, () -> {
            csvQuestionDao.findAll();
        });

        assertTrue(exception.getMessage().contains("File not found"));
    }

}
