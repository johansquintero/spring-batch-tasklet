package com.batch.steps;

import com.batch.domain.dto.PersonDto;
import com.batch.domain.services.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class ItemWriterStep implements Tasklet {

    @Autowired
    private IPersonService personService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("<------------------>INICIO DEL PASO DE ESCRITURA<---------------------->");
        List<PersonDto> personDtoList = (List<PersonDto>) chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .get("personList");
        personDtoList.forEach(personDto -> {
            if (personDto!=null){
                log.info(personDto.toString());
            }
        });
        this.personService.saveAll(personDtoList);
        log.info("<------------------>FIN DEL PASO DE ESCRITURA<---------------------->");
        return RepeatStatus.FINISHED;
    }
}
