package me.fontys.semester4.postalcode;

import com.healthmarketscience.jackcess.*;
import com.sun.istack.NotNull;
import me.fontys.semester4.interfaces.FileConverter;
import me.fontys.semester4.tempdata.entity.PostalcodeTemp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PCConverter implements FileConverter<PostalcodeTemp>
{
    protected final static String PC_TABLE="POSTCODES";
    protected final static String PC_MUNICIPALITIES_TABLE="GEMEENTEN";
    protected final static String PC_ROW_PC="A13_POSTCODE";
    protected final static String PC_ROW_RANGE_TYPE="A13_REEKSIND";
    protected final static String PC_ROW_RANGE_START="A13_BREEKPUNT_VAN";
    protected final static String PC_ROW_RANGE_END="A13_BREEKPUNT_TEM";
    protected final static String PC_ROW_PCMUN_CODE="A13_GEMEENTECODE";
    protected final static String PC_ROW_STREET="A13_STRAATNAAM";
    protected final static String PC_ROW_CITY="A13_WOONPLAATS";
    private final List<PostalcodeTemp> postalcodes;
    private final Logger LOGGER = LoggerFactory.getLogger(PCConverter.class);
    private IndexHelper indexHelper;
    private Database anImport;

    public PCConverter(DatabaseBuilder databaseBuilder, IndexHelper indexHelper)
    {
        postalcodes = new ArrayList<>();
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
    @NotNull
    public List<PostalcodeTemp> doConvert() throws IOException
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
                anImport.getTable(PC_TABLE).getIndex(PC_ROW_PC));

        int counter=0,total=0;
        for(Row row: cursor)
        {
            // normalize postal code (no space)
            String postalcode = row.getString(PC_ROW_PC).replace(" ","");

            String even = row.getString(PC_ROW_RANGE_TYPE);
            String street = row.getString(PC_ROW_STREET);

            long fromrange = row.getBigDecimal(PC_ROW_RANGE_START).longValueExact();
            long torange = row.getBigDecimal(PC_ROW_RANGE_END).longValueExact();
            long municipality = row.getBigDecimal(PC_ROW_PCMUN_CODE).longValueExact();
            String city = row.getString(PC_ROW_CITY);

            PostalcodeTemp pc = new PostalcodeTemp(
                    null,
                    postalcode,
                    fromrange,
                    torange,
                    even,
                    municipality,
                    street,
                    city);

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
