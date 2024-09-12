package ru.otus.hw.config;

import com.mongodb.lang.NonNull;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.converter.EntityConverter;
import ru.otus.hw.models.h2.Author;
import ru.otus.hw.models.h2.Book;
import ru.otus.hw.models.h2.Comment;
import ru.otus.hw.models.h2.Genre;
import ru.otus.hw.models.mongo.AuthorMongo;
import ru.otus.hw.models.mongo.BookMongo;
import ru.otus.hw.models.mongo.CommentMongo;
import ru.otus.hw.models.mongo.GenreMongo;

@Configuration
public class BatchConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchConfig.class);

    private static final int CHUNK_SIZE = 100;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final EntityManagerFactory entityManagerFactory;

    private final MongoOperations mongoOperations;

    private final EntityConverter entityConverter;

    @Autowired
    public BatchConfig(JobRepository jobRepository,
                       PlatformTransactionManager platformTransactionManager,
                       EntityManagerFactory entityManagerFactory,
                       MongoOperations mongoOperations,
                       EntityConverter entityConverter) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.entityManagerFactory = entityManagerFactory;
        this.mongoOperations = mongoOperations;
        this.entityConverter = entityConverter;
    }

    @Bean
    public ItemReader<Book> bookReader() {
        return new JpaPagingItemReaderBuilder<Book>()
                .entityManagerFactory(entityManagerFactory)
                .name("bookReader")
                .queryString("SELECT b FROM Book b")
                .pageSize(CHUNK_SIZE)
                .build();
    }

    @Bean
    public ItemReader<Author> authorReader() {
        return new JpaPagingItemReaderBuilder<Author>()
                .entityManagerFactory(entityManagerFactory)
                .name("authorReader")
                .queryString("SELECT a FROM Author a")
                .pageSize(CHUNK_SIZE)
                .build();
    }

    @Bean
    public ItemReader<Genre> genreReader() {
        return new JpaPagingItemReaderBuilder<Genre>()
                .name("genreReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT g FROM Genre g")
                .pageSize(CHUNK_SIZE)
                .build();
    }

    @Bean
    public ItemReader<Comment> commentReader() {
        return new JpaPagingItemReaderBuilder<Comment>()
                .name("commentReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT c FROM Comment c")
                .pageSize(CHUNK_SIZE)
                .build();
    }

    @Bean
    public ItemProcessor<Author, AuthorMongo> authorItemProcessor() {
        return entityConverter::convertAuthor;
    }

    @Bean
    public ItemProcessor<Genre, GenreMongo> genreItemProcessor() {
        return entityConverter::convertGenre;
    }

    @Bean
    public ItemProcessor<Book, BookMongo> bookItemProcessor() {
        return entityConverter::convertBook;
    }

    @Bean
    public ItemProcessor<Comment, CommentMongo> commentItemProcessor() {
        return entityConverter::convertComment;
    }

    @Bean
    public ItemWriter<BookMongo> bookWriter() {
        return new MongoItemWriterBuilder<BookMongo>().template(mongoOperations).build();
    }

    @Bean
    public ItemWriter<AuthorMongo> authorWriter() {
        return new MongoItemWriterBuilder<AuthorMongo>().template(mongoOperations).build();
    }

    @Bean
    public ItemWriter<GenreMongo> genreWriter() {
        return new MongoItemWriterBuilder<GenreMongo>().template(mongoOperations).build();
    }

    @Bean
    public ItemWriter<CommentMongo> commentWriter() {
        return new MongoItemWriterBuilder<CommentMongo>().template(mongoOperations).build();
    }

    @Bean
    public Job migrationJob(Step migrateBooksStep, Step migrateAuthorsStep,
                            Step migrateGenresStep, Step migrateCommentsStep) {
        return new JobBuilder("migrationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(migrateAuthorsStep)
                .next(migrateGenresStep)
                .next(migrateBooksStep)
                .next(migrateCommentsStep)
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        LOGGER.info("Начало job");
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        LOGGER.info("Конец job");
                    }
                })
                .build();
    }

    @Bean
    public Step migrateBooksStep(ItemReader<Book> bookItemReader, ItemWriter<BookMongo> bookItemWriter,
                                 ItemProcessor<Book, BookMongo> bookItemProcessor) {
        return new StepBuilder("migrateBooksStep", jobRepository)
                .<Book, BookMongo>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(bookItemReader)
                .processor(bookItemProcessor)
                .writer(bookItemWriter)
//                .taskExecutor(taskExecutor())
                .listener(new ItemReadListener<Book>() {
                    @Override
                    public void beforeRead() {
                        LOGGER.info("Начало чтения");
                    }

                    @Override
                    public void afterRead(@NonNull Book item) {
                        LOGGER.info("Конец чтения");
                    }

                    @Override
                    public void onReadError(@NonNull Exception ex) {
                        LOGGER.info("Ошибка чтения");
                    }
                })
                .listener(new ItemProcessListener<Book, BookMongo>() {
                    @Override
                    public void beforeProcess(@NonNull Book item) {
                        LOGGER.info("Начало обработки");
                    }

                    @Override
                    public void afterProcess(@NonNull Book item, BookMongo result) {
                        LOGGER.info("Конец обработки");
                    }

                    @Override
                    public void onProcessError(@NonNull Book item, @NonNull Exception e) {
                        LOGGER.info("Ошибка обработки");
                    }
                })
                .listener(new ItemWriteListener<BookMongo>() {
                    @Override
                    public void beforeWrite(@NonNull Chunk<? extends BookMongo> items) {
                        LOGGER.info("Начало записи");
                    }

                    @Override
                    public void afterWrite(@NonNull Chunk<? extends BookMongo> items) {
                        LOGGER.info("Конец записи");
                    }

                    @Override
                    public void onWriteError(@NonNull Exception exception, @NonNull Chunk<? extends BookMongo> items) {
                        LOGGER.info("Ошибка записи");
                    }
                })
                .listener(new ChunkListener() {
                    @Override
                    public void beforeChunk(@NonNull ChunkContext chunkContext) {
                        LOGGER.info("Начало пачки");
                    }

                    @Override
                    public void afterChunk(@NonNull ChunkContext chunkContext) {
                        LOGGER.info("Конец пачки");
                    }

                    @Override
                    public void afterChunkError(@NonNull ChunkContext chunkContext) {
                        LOGGER.info("Ошибка пачки");
                    }
                })
                .build();
    }

    @Bean
    public Step migrateAuthorsStep(ItemReader<Author> authorItemReader, ItemWriter<AuthorMongo> authorItemWriter,
                                   ItemProcessor<Author, AuthorMongo> authorItemProcessor) {
        return new StepBuilder("migrateAuthorsStep", jobRepository)
                .<Author, AuthorMongo>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(authorItemReader)
                .processor(authorItemProcessor)
                .writer(authorItemWriter)
                .build();
    }

    @Bean
    public Step migrateGenresStep(ItemReader<Genre> genreItemReader, ItemWriter<GenreMongo> genreItemWriter,
                                  ItemProcessor<Genre, GenreMongo> genreItemProcessor) {
        return new StepBuilder("migrateGenresStep", jobRepository)
                .<Genre, GenreMongo>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(genreItemReader)
                .processor(genreItemProcessor)
                .writer(genreItemWriter)
                .build();
    }

    @Bean
    public Step migrateCommentsStep(ItemReader<Comment> commentItemReader, ItemWriter<CommentMongo> commentItemWriter,
                                    ItemProcessor<Comment, CommentMongo> commentItemProcessor) {
        return new StepBuilder("migrateCommentsStep", jobRepository)
                .<Comment, CommentMongo>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(commentItemReader)
                .processor(commentItemProcessor)
                .writer(commentItemWriter)
                .build();
    }

}
