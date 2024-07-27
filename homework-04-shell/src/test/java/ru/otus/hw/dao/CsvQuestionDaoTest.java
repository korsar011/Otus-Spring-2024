package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.ApplicationConfig;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {CsvQuestionDao.class})
public class CsvQuestionDaoTest {

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @MockBean
    private TestFileNameProvider fileNameProvider;

    @BeforeEach
    public void setUp() {
        when(fileNameProvider.getTestFileName()).thenReturn("test_questions_ru.csv");
    }

    @Test
    public void testFindAll() {
        List<Question> questions = csvQuestionDao.findAll();
        assertNotNull(questions);
        assertEquals(7, questions.size());
    }

    @Test
    public void testFileNotFound() {
        when(fileNameProvider.getTestFileName()).thenReturn("nonexistent-file.csv");

        QuestionReadException exception = assertThrows(QuestionReadException.class, () -> {
            csvQuestionDao.findAll();
        });

        assertTrue(exception.getMessage().contains("Файл не найден: "));
    }
}