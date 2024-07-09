package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.ApplicationConfig;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.LocalizedIOService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CsvQuestionDao.class, ApplicationConfig.class})
public class CsvQuestionDaoTest {

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @Autowired
    private TestFileNameProvider fileNameProvider;

    @MockBean
    private LocalizedIOService ioServiceMock;

    @Test
    public void testFindAll() {
        when(ioServiceMock.readStringWithPromptLocalized("StudentService.input.first.name")).thenReturn("Adam");
        when(ioServiceMock.readStringWithPromptLocalized("StudentService.input.last.name")).thenReturn("Mada");

        when(ioServiceMock.readIntForRange(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).thenReturn(1);

        List<Question> questions = csvQuestionDao.findAll();
        assertNotNull(questions);

        String expectedFileName = "test_questions_ru.csv";
        String actualFileName = fileNameProvider.getTestFileName();
        assertEquals(expectedFileName, actualFileName);
    }
}
