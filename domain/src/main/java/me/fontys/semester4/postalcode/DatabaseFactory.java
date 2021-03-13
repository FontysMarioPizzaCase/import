package me.fontys.semester4.postalcode;

import com.healthmarketscience.jackcess.DatabaseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;

@Component
public class DatabaseFactory
{
    @Value("classpath:postalcodes.zip")
    private Resource pcResource;
    private final Logger LOGGER = LoggerFactory.getLogger(PCImporter.class);

    public File Uncompress(Resource importFile) throws IOException
    {
        File res = File.createTempFile("pccodes-",".mdb");
        LOGGER.info(String.format("decompressing postalcodes zip: %s to: %s",importFile,res));
        ZipInputStream zis = new ZipInputStream(importFile.getInputStream());
        byte[] buffer = new byte[1024];

        zis.getNextEntry();

        FileOutputStream fos = new FileOutputStream(res);
        int len;
        while ((len = zis.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
        }
        fos.close();
        zis.closeEntry();
        zis.close();
        res.deleteOnExit();

        return res;
    }

    @Bean
    public DatabaseBuilder createDatabaseBuilder() throws IOException
    {
        File uncompressed = Uncompress(pcResource);
        return new DatabaseBuilder(uncompressed);
    }
}
