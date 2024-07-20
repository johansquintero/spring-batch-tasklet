package com.batch.config;

import com.batch.steps.ItemDescompressStep;
import com.batch.steps.ItemProcessorStep;
import com.batch.steps.ItemReaderStep;
import com.batch.steps.ItemWriterStep;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    @JobScope
    public ItemReaderStep itemReaderStep() {
        return new ItemReaderStep();
    }

    @Bean
    @JobScope
    public ItemProcessorStep itemProcessorStep() {
        return new ItemProcessorStep();
    }

    @Bean
    @JobScope
    public ItemDescompressStep itemDescompressStep() {
        return new ItemDescompressStep();
    }

    @Bean
    @JobScope
    public ItemWriterStep itemWriterStep() {
        return new ItemWriterStep();
    }

    @Bean
    public Step descompressFileStep(){
        return stepBuilderFactory.get("descompressFileStep")
                .tasklet(this.itemDescompressStep()).build();
    }

    @Bean
    public Step readFileStep(){
        return stepBuilderFactory.get("itemReader")
                .tasklet(this.itemReaderStep())
                .build();
    }

    @Bean
    public Step proccessDataStep(){
        return stepBuilderFactory.get("itemProcessor").tasklet(this.itemProcessorStep()).build();
    }

    @Bean
    public Step writePersonStep(){
        return stepBuilderFactory.get("itemWriter").tasklet(this.itemWriterStep()).build();
    }

    @Bean
    public Job readCSVJob(){
        return jobBuilderFactory.get("readCSVJob")
                .start(this.descompressFileStep())
                .next(this.readFileStep())
                .next(this.proccessDataStep())
                .next(this.writePersonStep())
                .build();
    }
}
