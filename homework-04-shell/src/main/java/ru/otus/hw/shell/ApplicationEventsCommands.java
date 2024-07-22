package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.hw.config.AnswersAvailableChecker;
import ru.otus.hw.config.LocaleConfig;
import ru.otus.hw.dao.QuestionDao;


import java.util.ArrayList;
import java.util.List;


@ShellComponent(value = "Application Events Commands")
@RequiredArgsConstructor
public class ApplicationEventsCommands {

    private final CommandLineRunner commandLineRunner;

    private final LocaleConfig localeConfig;

    private final AnswersAvailableChecker answersAvailableChecker;

    private final QuestionDao questionDao;



    @ShellMethod(value = "Run application and specify locale", key = {"r", "run"})
    public void run (@ShellOption(defaultValue = "en-US") String locale) throws Exception {
        if (!locale.isEmpty()) {
            localeConfig.setLocale(locale);
        }
        commandLineRunner.run();
    }

    @ShellMethod(value = "Answers", key = {"a", "answers"})
    @ShellMethodAvailability(value = "isAnswersAvailable")
    public String showAnswers() {
        var questions = questionDao.findAll();
        List <String> answers = new ArrayList<>();
        var index = 1;
        for (var question: questions) {
                        for (var answer : question.answers()) {
                if (answer.isCorrect()) {
                   answers.add(index + ". " + answer.text());
                    index++;
                }
               }
                        }
        return answers.toString();
    }

    public Availability isAnswersAvailable() {
        if (answersAvailableChecker.isAnswersAvailable()) {
            return Availability.available();
        }

        return Availability.unavailable("Answers are not available");
    }

}