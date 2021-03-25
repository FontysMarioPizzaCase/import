package me.fontys.semester4.postalcode;

import me.fontys.semester4.data.entity.LogEntry;
import me.fontys.semester4.interfaces.Importer;
import me.fontys.semester4.tempdata.entity.MunicipalityTemp;
import me.fontys.semester4.tempdata.entity.PostalcodeTemp;
import me.fontys.semester4.tempdata.repository.MunicipalityTempRepository;
import me.fontys.semester4.tempdata.repository.PostalcodeTempRepository;
import me.fontys.semester4.utility.ProcessPCStoredProc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.persistence.EntityManagerFactory;
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

    private final ProcessPCStoredProc processPCStoredProc;

    @Autowired
    public PCImporter(
            PostalcodeTempRepository postalcodeTempRepository,
            MunicipalityTempRepository municipalityTempRepository,
            EntityManagerFactory emf,
            PCConverter pcConverter, MunicipalityConverter municipalityConverter,
            ProcessPCStoredProc processPCStoredProc)
    {
        this.postalcodeTempRepository = postalcodeTempRepository;
        this.municipalityTempRepository = municipalityTempRepository;
        this.emf = emf;
        this.municipalityConverter = municipalityConverter;
        this.pcConverter = pcConverter;
        this.processPCStoredProc = processPCStoredProc;
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
        processPCStoredProc.Execute();
        reportSecondsSP = ((System.currentTimeMillis() - start) / 1000);
        LOGGER.info(String.format("seconds it took to transform postal codes: %s", reportSecondsSP));
    }

    @Override
    public void report()
    {
        for(LogEntry logEntry : processPCStoredProc.RetrieveLogs())
        {
            LOGGER.info(logEntry.getMessage());
        }


    }
}
