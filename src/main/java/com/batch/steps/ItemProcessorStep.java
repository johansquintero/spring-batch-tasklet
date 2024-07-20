package com.batch.steps;

import com.batch.domain.dto.PersonDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class ItemProcessorStep implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("<------------------>INICIO DEL PASO DE PROCESAMIENTO<---------------------->");
        List<PersonDto> personDtoList = (List<PersonDto>) chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .get("personList");
        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("/dd/MM/yyyy HH:mm:ss");
        assert personDtoList != null;
        List<PersonDto> finalList = personDtoList.stream().map(personDto -> {
            personDto.setInsertionDate(formatter.format(LocalDateTime.now()));
            return personDto;
        }).toList();
        chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .put("personList",finalList);
        log.info("<------------------>FIN DEL PASO DE PROCESAMIENTO<---------------------->");
        return RepeatStatus.FINISHED;
    }
}
