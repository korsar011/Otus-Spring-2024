package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;


import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TestServiceImplTest {

    @InjectMocks
    private TestServiceImpl testService;

    @Mock
    private QuestionDao questionDao;

    @Mock
    private IOService ioService;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testExecuteTest() {
        List<Question> mockQuestions = Collections.singletonList(
                new Question("Test question", Collections.singletonList(new Answer("Test answer", true)))
        );
        when(questionDao.findAll()).thenReturn(mockQuestions);

        testService.executeTest();

        verify(ioService).printLine("Test question");
        verify(ioService).printLine("Test answer");
    }
}
