package com.batch.steps;

import com.batch.domain.dto.PersonDto;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ItemReaderStep implements Tasklet {

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("<------------------>INICIO DEL PASO DE LECTURA<---------------------->");
        Reader reader = new FileReader(
                resourceLoader
                        .getResource("classpath:files/destination/persons.csv")
                        .getFile()
        );

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .build();

        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withCSVParser(parser)
                .withSkipLines(1)
                .build();

        List<PersonDto> personDtoList = new ArrayList<>();
        String[] actualLine;
        while((actualLine=csvReader.readNext()) != null){
            PersonDto personDto = new PersonDto();
            personDto.setName(actualLine[0]);
            personDto.setLastName(actualLine[1]);
            personDto.setAge(Integer.parseInt(actualLine[2]));
            personDtoList.add(personDto);
        }
        csvReader.close();
        reader.close();

        chunkContext
                .getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .put("personList",personDtoList);
        log.info("<------------------>FIN DEL PASO DE LECTURA<---------------------->");
        return RepeatStatus.FINISHED;
    }
}
