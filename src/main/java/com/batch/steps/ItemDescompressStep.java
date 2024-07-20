package com.batch.steps;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Slf4j
public class ItemDescompressStep implements Tasklet {
    @Autowired
    private ResourceLoader resourceLoader;
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("<------------------>INICIO DEL PASO DE DESCOMPRESION<---------------------->");

        //se establecen las rutas relativas y absolutas del recurso del archivo .zip
        Resource resource = resourceLoader.getResource("classpath:files/persons.zip");
        String filePath = resource.getFile().getAbsolutePath();

        //se instacia el objeto de tipo ZipFile y se lee el archivo .zip previamente establecido con las rutas
        ZipFile zipFile = new ZipFile(filePath);
        //se instancia el archivo destination como ruta hija de la carpeta files
        File desDir = new File(resource.getFile().getParent(),"destination");
        //si no existe se crea la carpeta destination
        if (!desDir.exists()){
            desDir.mkdir();
        }

        //se establecen las entrades del archivo .zip
        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        //se recorre cada uno de los elementos o entradas
        while (entries.hasMoreElements()){
            //se instancia la entrada actual
            ZipEntry zipEntry = entries.nextElement();
            //se obtiene y se crea la ruta de archivo de la entrada actual
            File file = new File(desDir,zipEntry.getName());
            //si la entrada es una carpeta se crea la misma
            if (file.isDirectory()){
                file.mkdirs();
            }else {
                //se crea la entrada como una secuencia de bytes
                InputStream inputStream = zipFile.getInputStream(zipEntry);
                //se escribe esta secuencia de bytes en la ruta
                FileOutputStream outputStream = new FileOutputStream(file);
                //se crea el buffer de 1024 bytes
                // que seria el fragmento de bytes establecido para dividir el archivo a descomprimir
                byte[] buffer = new byte[1024];
                int length;
                //mientra la secuencia de bytes sea mayor a 0 se realizara la escritura del archivo en la ruta
                while ((length = inputStream.read(buffer))>0){
                    //se realiza la escritura en trozo de 1024 bytes
                    //desde la posicion cero
                    //con la secuencia actual de bytes que se esta leyendo
                    outputStream.write(buffer,0,length);
                }
                outputStream.close();
                inputStream.close();
            }
        }
        zipFile.close();
        log.info("<------------------>FIN DEL PASO DE DESCOMPRESION<---------------------->");
        return RepeatStatus.FINISHED;
    }
}
