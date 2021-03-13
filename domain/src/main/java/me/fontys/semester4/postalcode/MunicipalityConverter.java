package me.fontys.semester4.postalcode;

import com.healthmarketscience.jackcess.*;
import com.sun.istack.NotNull;
import me.fontys.semester4.interfaces.FileConverter;
import me.fontys.semester4.tempdata.entity.MunicipalityTemp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MunicipalityConverter implements FileConverter<MunicipalityTemp>
{
    private final static String PC_TABLE="POSTCODES";
    private final static String PC_MUNICIPALITIES_TABLE="GEMEENTEN";
    protected final static String PC_ROW_MUN_CODE="N42_GEM_KODE";
    protected final static String PC_ROW_MUN_NAME="N42_GEM_NAAM";

    private final List<MunicipalityTemp> municipalityTemps;
    private final Logger LOGGER = LoggerFactory.getLogger(MunicipalityConverter.class);
    private Database anImport;
    private IndexHelper indexHelper;

    @Autowired
    public MunicipalityConverter(DatabaseBuilder databaseBuilder,IndexHelper indexHelper)
    {
        municipalityTemps = new ArrayList<>();
        this.indexHelper = indexHelper;
        try
        {
            anImport = databaseBuilder
                    .setFileFormat(Database.FileFormat.V2000)
                    .open();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }



    @Override
    public List<MunicipalityTemp> doConvert() throws IOException
    {

        // check tables
        for (var table: new String[] {PC_TABLE,PC_MUNICIPALITIES_TABLE})
        {
            if(!anImport.getTableNames().contains(table))
            {
                throw new UnsupportedEncodingException("Table missing: "+table);
            }
        }


        // retrieve unique postal codes
        IndexCursor cursor = indexHelper.createCursor(
                anImport.getTable(PC_MUNICIPALITIES_TABLE).getIndex(PC_ROW_MUN_CODE));

        int counter=0,total=0;
        for(Row row: cursor)
        {

            long id = row.getBigDecimal(PC_ROW_MUN_CODE).longValueExact();
            String name = row.getString(PC_ROW_MUN_NAME);


            MunicipalityTemp municipality = new MunicipalityTemp(
                    id,
                    name);

            municipalityTemps.add(municipality);
            counter++;
            if(counter == 200)
            {
                total += counter;
                counter = 0;
                LOGGER.info(String.format("Loaded %d municipality entries in memory...",total));
            }


        }

        return municipalityTemps;

    }
}
