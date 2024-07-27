package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            ioService.printLine(question.text());
            var index = 1;
            for (var answer : question.answers()) {
                ioService.printFormattedLine("%d) %s", index++, answer.text());
            }

            int answerIndex = ioService.readIntForRange(1, question.answers().size(),
                    "Please, enter the number of the correct answer");
            var isAnswerValid = question.answers().get(answerIndex - 1).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    @Override
    public List<String> getCorrectAnswers() {
        var questions = questionDao.findAll();
        List<String> answers = new ArrayList<>();
        var index = 1;
        for (var question : questions) {
            for (var answer : question.answers()) {
                if (answer.isCorrect()) {
                    answers.add(index + ". " + answer.text());
                    index++;
                }
            }
        }
        return answers;
    }
}