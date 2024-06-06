package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        printIntroduction();
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            askQuestion(question, testResult);
        }

        return testResult;
    }

    private void printIntroduction() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
    }

    private void askQuestion(Question question, TestResult testResult) {
        ioService.printLine(question.text());
        printAnswers(question);
        int answerIndex = ioService.readIntForRange(1, question.answers().size(), "Please, enter the number of the correct answer");
        boolean isAnswerValid = question.answers().get(answerIndex - 1).isCorrect();
        testResult.applyAnswer(question, isAnswerValid);
    }

    private void printAnswers(Question question) {
        int index = 1;
        for (var answer : question.answers()) {
            ioService.printFormattedLine("%d) %s", index++, answer.text());
        }
    }
}