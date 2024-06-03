package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.hw.Application;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CsvQuestionDaoIntegrationTest {

    @Test
    public void testFindAll() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        CsvQuestionDao csvQuestionDao = context.getBean(CsvQuestionDao.class);

        List<Question> questions = csvQuestionDao.findAll();
        assertNotNull(questions);
        assertEquals(7, questions.size()); // В тестовом файле с вопросами должно быть 7 вопросов, а не 5 вопросов

        context.close();
    }
}
