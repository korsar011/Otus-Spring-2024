package ru.otus.hw.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Locale;
import java.util.Map;

@Setter
@ConfigurationProperties(prefix = "test")
public class AppProperties implements TestConfig, TestFileNameProvider, LocaleConfig, AnswersAvailableChecker {

    @Getter
    private int rightAnswersCountToPass;

    @Getter
    private Locale locale;

    private Map<String, String> fileNameByLocaleTag;

    @Getter
    private boolean answersAvailable;

    @ConstructorBinding
    public AppProperties(int rightAnswersCountToPass, String locale, Map<String,
            String> fileNameByLocaleTag, boolean answersAvailable) {
        this.rightAnswersCountToPass = rightAnswersCountToPass;
        this.locale = Locale.forLanguageTag(locale);
        this.fileNameByLocaleTag = fileNameByLocaleTag;
        this.answersAvailable = answersAvailable;
    }

    public void setLocale(String locale) {

        this.locale = Locale.forLanguageTag(locale);
    }


    @Override
    public String getTestFileName() {
        return fileNameByLocaleTag.get(locale.toLanguageTag());
    }

   }
