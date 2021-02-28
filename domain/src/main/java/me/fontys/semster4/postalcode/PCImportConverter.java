package me.fontys.semster4.postalcode;

import com.healthmarketscience.jackcess.CursorBuilder;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.IndexCursor;
import me.fontys.semester4.data.entity.PostalcodePart;
import me.fontys.semester4.data.repository.PostalcodePartRepository;
import me.fontys.semester4.interfaces.ImportConverter;
import org.springframework.data.jpa.domain.Specification;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class PCImportConverter implements ImportConverter
{
    private final static String PC_TABLE="POSTCODES";
    private final static String PC_RANGE_TABLE="GEMEENTEN";
    private final static String PC_ROW_PC="A13_POSTCODE";
    private final static String PC_ROW_RANGE_TYPE="A13_REEKSIND";
    private final static String PC_ROW_RANGE_START="A13_BREEKPUNT_VAN";
    private final static String PC_ROW_RANGE_END="A13_BREEKPUNT_TEM";

    private Database Import;
    private final PostalcodePartRepository postalcodePartRepository;

    public PCImportConverter(PostalcodePartRepository postalcodePartRepository)
    {
        this.postalcodePartRepository = postalcodePartRepository;

    }

    static Specification<PostalcodePart> hasPC(String Postalcode, boolean even, int from, int to) {
        return (pc, cq, cb) ->
                cb.equal(pc.get(PC_ROW_PC), Postalcode);

        }


    @Override
    public void ProcessImport(File importFile) throws IOException
    {
        Import = new DatabaseBuilder(importFile)
                .setFileFormat(Database.FileFormat.V2000)
                .open();

        // check tables
        for (var table: new String[] {PC_TABLE,PC_RANGE_TABLE})
        {
            if(!Import.getTableNames().contains(table))
            {
                throw new UnsupportedEncodingException("Table missing: "+table);
            }
        }

        // retrieve unique postal codes
        IndexCursor cursor = CursorBuilder.createCursor(
                Import.getTable(PC_TABLE).getIndex("A13_POSTCODE"));

        for(var row: cursor)
        {
            var postalcode = row.getString(PC_ROW_PC);
            var even = row.getInt(PC_ROW_RANGE_TYPE) != 0; // TODO: there is a type 2 and 3 as well..
            var fromrange = row.getInt(PC_ROW_RANGE_START);
            var torange = row.getInt(PC_ROW_RANGE_END);

            // check if existing item in database
            //postalcodePartRepository.findAll();

        }



    }
}
