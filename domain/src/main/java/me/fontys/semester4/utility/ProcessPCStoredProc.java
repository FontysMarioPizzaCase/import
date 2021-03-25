package me.fontys.semester4.utility;


import me.fontys.semester4.data.entity.LogEntry;
import me.fontys.semester4.data.repository.LogEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ProcessPCStoredProc extends CustomSqlExecutor
{
    @Value("classpath:processPostalcodes.sql")
    private Resource processProcResource;

    @Value("classpath:createLogEntry.sql")
    private Resource createLogProcResource;

    private Date sessionDate;
    private final LogEntryRepository logEntryRepository;

    @Autowired
    public ProcessPCStoredProc(EntityManagerFactory emf,
                               LogEntryRepository logEntryRepository)
    {
        super(emf);
        this.logEntryRepository = logEntryRepository;
    }

    @PostConstruct
    private void PostInit()
    {
        // initialize procedure:
        try
        {
            SQLExecute(StreamUtils.copyToString(createLogProcResource.getInputStream()
                    , Charset.defaultCharset()),
                    true);
            SQLExecute(StreamUtils.copyToString(processProcResource.getInputStream()
                    , Charset.defaultCharset()),
                    true);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void Execute()
    {
        sessionDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String dateStr = dateFormat.format(sessionDate);
        try
        {
            sessionDate = dateFormat.parse(dateStr);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        SQLExecute("CALL process_postalcodes( to_timestamp( '"+dateStr+"','YYYY-MM-DD HH24:mi:ss'))",
                false);
    }

    public Iterable<LogEntry> RetrieveLogs()
    {
        logEntryRepository.flush();
        return logEntryRepository.findAllBylogentrytime(sessionDate);
    }
}
