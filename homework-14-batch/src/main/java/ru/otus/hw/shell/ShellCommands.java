package ru.otus.hw.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ShellCommands {

    private static final Logger logger = LoggerFactory.getLogger(ShellCommands.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job migrationJob;

    @ShellMethod(value = "Run the migration job", key = "run")
    public void runMigration() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(migrationJob, jobParameters);
            logger.info("Migration job completed successfully.");
        } catch (Exception e) {
            logger.error("Error occurred while running migration job", e);
        }
    }
}