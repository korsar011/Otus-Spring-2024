package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.hw.config.AnswersAvailableChecker;
import ru.otus.hw.config.LocaleConfig;
import ru.otus.hw.service.TestRunnerService;
import ru.otus.hw.service.TestService;


import java.util.List;


@ShellComponent(value = "Application Events Commands")
@RequiredArgsConstructor
public class ApplicationEventsCommands {

    private final TestRunnerService testRunnerService;

    private final TestService testService;

    private final LocaleConfig localeConfig;

    private final AnswersAvailableChecker answersAvailableChecker;


    @ShellMethod(value = "Run application and specify locale", key = {"r", "run"})
    public void run(@ShellOption(defaultValue = "en-US") String locale) throws Exception {
        if (!locale.isEmpty()) {
            localeConfig.setLocale(locale);
        }
        testRunnerService.run();
    }

    @ShellMethod(value = "Answers", key = {"a", "answers"})
    @ShellMethodAvailability(value = "isAnswersAvailable")
    public String showAnswers() {
        List<String> answers = testService.getCorrectAnswers();
        return answers.toString();
    }

    public Availability isAnswersAvailable() {
        if (answersAvailableChecker.isAnswersAvailable()) {
            return Availability.available();
        }

        return Availability.unavailable("Answers are not available");
    }

}