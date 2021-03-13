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
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


@Component
public class PCImporter implements Importer
{
    private final PostalcodeTempRepository postalcodeTempRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(PCImporter.class);
    private long reportSecondsRead, reportSecondsWrite, reportSecondsSP;
    private final MunicipalityTempRepository municipalityTempRepository;
    private final EntityManagerFactory emf;
    private final MunicipalityConverter municipalityConverter;
    private final PCConverter pcConverter;

    @Value("classpath:processPostalcodes.sql")
    private Resource storedProcResource;

    @Autowired
    public PCImporter(
            PostalcodeTempRepository postalcodeTempRepository,
            MunicipalityTempRepository municipalityTempRepository,
            EntityManagerFactory emf,
            PCConverter pcConverter, MunicipalityConverter municipalityConverter)
    {
        this.postalcodeTempRepository = postalcodeTempRepository;
        this.municipalityTempRepository = municipalityTempRepository;
        this.emf = emf;
        this.municipalityConverter = municipalityConverter;
        this.pcConverter = pcConverter;
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


        try
        {
            List<PostalcodeTemp> postalcodeTemps = pcConverter.doConvert();
            List<MunicipalityTemp> municipalityTemps = municipalityConverter.doConvert();

            municipalityTempRepository.deleteAll();
            postalcodeTempRepository.deleteAll();
            // create or update stored procedure in database
            myCustomSqlExecutor(
                    StreamUtils.copyToString(storedProcResource.getInputStream()
                            , Charset.defaultCharset()),
                    true
            );



            municipalityTempRepository.saveAll(municipalityTemps);

            reportSecondsRead = ((System.currentTimeMillis() - start) / 1000);

            LOGGER.info(String.format("seconds it took to read all current postal codes: %s", reportSecondsRead));

            LOGGER.info(String.format("Saving %d new postcodes...",postalcodeTemps.size()));
            start = System.currentTimeMillis();
            postalcodeTempRepository.saveAll(postalcodeTemps);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

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
        reportSecondsSP = ((System.currentTimeMillis() - start) / 1000);
        LOGGER.info(String.format("seconds it took to transform postal codes: %s", reportSecondsSP));
    }

    @Override
    public void report()
    {

    }
}
