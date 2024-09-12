package ru.otus.hw.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ShellCommands {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShellCommands.class);

    private final JobLauncher jobLauncher;

    private final Job migrationJob;

    public ShellCommands(JobLauncher jobLauncher, Job migrationJob) {
        this.jobLauncher = jobLauncher;
        this.migrationJob = migrationJob;
    }

    @ShellMethod(value = "Run the migration job", key = "run")
    public void runMigration() {
        try {
            JobParameters jobParameters = new JobParametersBuilder().addLong(
                    "time", System.currentTimeMillis()).toJobParameters();
            jobLauncher.run(migrationJob, jobParameters);
            LOGGER.info("Migration job completed successfully.");
        } catch (Exception e) {
            LOGGER.error("Error occurred while running migration job", e);
        }
    }
}