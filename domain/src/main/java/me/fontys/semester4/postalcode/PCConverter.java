package me.fontys.semester4.postalcode;

import com.healthmarketscience.jackcess.*;
import me.fontys.semester4.interfaces.FileConverter;
import me.fontys.semester4.tempdata.entity.PostalcodeTemp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class PCConverter implements FileConverter<PostalcodeTemp>
{
    private final static String PC_TABLE="POSTCODES";
    private final static String PC_MUNICIPALITIES_TABLE="GEMEENTEN";
    private final static String PC_ROW_PC="A13_POSTCODE";
    private final static String PC_ROW_RANGE_TYPE="A13_REEKSIND";
    private final static String PC_ROW_RANGE_START="A13_BREEKPUNT_VAN";
    private final static String PC_ROW_RANGE_END="A13_BREEKPUNT_TEM";
    private final static String PC_ROW_PCMUN_CODE="A13_GEMEENTECODE";
    private final List<PostalcodeTemp> postalcodes;
    private final Logger LOGGER = LoggerFactory.getLogger(PCConverter.class);

    public PCConverter()
    {
        postalcodes = new ArrayList<>();
    }

    @Override
    public List<PostalcodeTemp> doConvert(File importFile) throws IOException
    {
        Database anImport = new DatabaseBuilder(importFile)
                .setFileFormat(Database.FileFormat.V2000)
                .open();

        // check tables
        for (var table: new String[] {PC_TABLE,PC_MUNICIPALITIES_TABLE})
        {
            if(!anImport.getTableNames().contains(table))
            {
                throw new UnsupportedEncodingException("Table missing: "+table);
            }
        }

        // retrieve unique postal codes
        IndexCursor cursor = CursorBuilder.createCursor(
                anImport.getTable(PC_TABLE).getIndex("A13_POSTCODE"));

        int counter=0,total=0;
        for(Row row: cursor)
        {
            // normalize postal code (no space)
            String postalcode = row.getString(PC_ROW_PC).replace(" ","");

            String even = row.getString(PC_ROW_RANGE_TYPE); // TODO: there is a type 2 and 3 as well..

            long fromrange = row.getBigDecimal(PC_ROW_RANGE_START).longValueExact();
            long torange = row.getBigDecimal(PC_ROW_RANGE_END).longValueExact();
            long municipality = row.getBigDecimal(PC_ROW_PCMUN_CODE).longValueExact();

            PostalcodeTemp pc = new PostalcodeTemp(
                    null,
                    postalcode,
                    fromrange,
                    torange,
                    even,
                    municipality);

            postalcodes.add(pc);
            counter++;
            if(counter == 100000)
            {
                total += counter;
                counter = 0;
                LOGGER.info(String.format("Loaded %d postal code entries in memory...",total));
            }
        }
        return postalcodes;
    }
}
