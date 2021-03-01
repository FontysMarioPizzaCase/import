package me.fontys.semester4.postalcode;

import me.fontys.semester4.interfaces.Importer;
import me.fontys.semester4.tempdata.entity.MunicipalityTemp;
import me.fontys.semester4.tempdata.entity.PostalcodeTemp;
import me.fontys.semester4.tempdata.repository.MunicipalityTempRepository;
import me.fontys.semester4.tempdata.repository.PostalcodeTempRepository;
import org.hibernate.internal.SessionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.zip.ZipInputStream;


@Component
public class PCImporter implements Importer
{
    private final PostalcodeTempRepository postalcodeTempRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(PCImporter.class);
    private long reportSecondsRead, reportSecondsWrite;
    private final PCConverter pcConverter;
    private final MunicipalityConverter municipalityConverter;
    private final MunicipalityTempRepository municipalityTempRepository;
    private final EntityManagerFactory emf;

    @Value("classpath:postalcodes.zip")
    private Resource pcResource;

    @Value("classpath:processPostalcodes.sql")
    private Resource storedProcResource;

    @Autowired
    public PCImporter(
            PostalcodeTempRepository postalcodeTempRepository,
            MunicipalityTempRepository municipalityTempRepository,
            PCConverter pcConverter,
            MunicipalityConverter municipalityConverter,
            EntityManagerFactory emf)
    {
        this.postalcodeTempRepository = postalcodeTempRepository;
        this.pcConverter = pcConverter;
        this.municipalityTempRepository = municipalityTempRepository;
        this.municipalityConverter = municipalityConverter;
        this.emf = emf;
    }

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

    private void myCustomSqlExecutor(String statement,boolean transaction) throws SQLException
    {
        EntityManager em = emf.createEntityManager();
        if(transaction)
            em.getTransaction().begin();
        Statement st = em.unwrap(SessionImpl.class).connection().createStatement();
        st.executeUpdate(statement);
        if(transaction)
            em.getTransaction().commit();
    }

    @Override
    public void doImport()
    {
        // Import all converted postal code parts.
        long start = System.currentTimeMillis();
        LOGGER.info("Reading postal codes from mdb...");
        List<PostalcodeTemp> postalcodeTemps = null;
        List<MunicipalityTemp> municipalityTemps = null;

        try
        {
            // create or update stored procedure in database
            myCustomSqlExecutor(
                    StreamUtils.copyToString(storedProcResource.getInputStream()
                            , Charset.defaultCharset()),
                    true
            );
            File uncompressed = Uncompress(pcResource);

            postalcodeTemps = pcConverter.doConvert(uncompressed);
            municipalityTemps = municipalityConverter.doConvert(uncompressed);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        municipalityTempRepository.saveAll(municipalityTemps);

        reportSecondsRead = ((System.currentTimeMillis() - start) / 1000);

        LOGGER.info(String.format("seconds it took to read all current postal codes: %s", reportSecondsRead));

        LOGGER.info(String.format("Saving %d new postcodes...",postalcodeTemps.size()));
        start = System.currentTimeMillis();
        postalcodeTempRepository.saveAll(postalcodeTemps);

        reportSecondsWrite = ((System.currentTimeMillis() - start) / 1000);
        LOGGER.info(String.format("seconds it took to import postal codes: %s", reportSecondsWrite));
        start = System.currentTimeMillis();
        LOGGER.info("start stored procedure, transform postal codes...");
        try
        {
            myCustomSqlExecutor("CALL process_postalcodes()",false);
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        LOGGER.info(String.format("seconds it took to transform postal codes: %s", reportSecondsWrite));
    }

    @Override
    public void report()
    {

    }
}
