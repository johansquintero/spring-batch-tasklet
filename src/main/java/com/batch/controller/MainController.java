package com.batch.controller;

import com.batch.domain.dto.PersonDto;
import com.batch.domain.services.IPersonService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/person")
public class MainController {

    private final IPersonService personService;
    private final JobLauncher jobLauncher;
    private final Job job;

    @GetMapping
    public ResponseEntity<List<PersonDto>> getAll(){
        return ResponseEntity.ok(this.personService.getAll());
    }

    @GetMapping(path = "/id/{id}")
    public ResponseEntity<PersonDto> getById(@PathVariable(name = "id") Long id){
        return ResponseEntity.of(this.personService.getPersonById(id));
    }

    @GetMapping(path = "/name/{name}")
    public ResponseEntity<PersonDto> getByName(@PathVariable(name = "name") String name){
        return ResponseEntity.of(this.personService.getPersonByName(name));
    }

    @PostMapping(path = "/upload-file")
    public ResponseEntity<Map<String,String>> upload(@RequestParam(name = "file")  MultipartFile multipartFile){
        String fileName = multipartFile.getOriginalFilename();

        try {
            Path path = Paths.get("src"+ File.separator+"main"+File.separator+"resources"+File.separator+"files"+File.separator+fileName);
            Files.createDirectories(path.getParent());
            Files.copy(multipartFile.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);

            log.info("<-------------> INICIO DEL PROCESO BATCH <--------------->");
            JobParameters jobParameters = new JobParametersBuilder()
                    .addDate("date", new Date())
                    .addString("fileName",fileName)
                    .toJobParameters();

            this.jobLauncher.run(this.job,jobParameters);

            Map<String,String> response = new HashMap<>();

            response.put("file",fileName);
            response.put("state","received");

            return ResponseEntity.ok(response);


        }catch (Exception e){
            log.error("Error to initiating the batch process, Error {}",e.getMessage());
            throw new RuntimeException();
        }
    }

    @PostMapping
    public ResponseEntity<PersonDto> save(@RequestBody PersonDto person){
        return ResponseEntity.of(this.personService.save(person));
    }

    @PutMapping
    public ResponseEntity<PersonDto> update(@RequestBody PersonDto person){
        return ResponseEntity.of(this.personService.update(person));
    }

    @DeleteMapping(path = "/id/{id}")
    public ResponseEntity<Map<String,Boolean>> delete(@PathVariable(name = "id") Long id){
        Map<String,Boolean> response=  new HashMap<>();
        response.put("deleted",this.personService.delete(id));
        return ResponseEntity.ok(response);
    }
}
