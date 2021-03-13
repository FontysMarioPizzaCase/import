package me.fontys.semester4.postalcode;

import com.healthmarketscience.jackcess.*;
import me.fontys.semester4.tempdata.entity.MunicipalityTemp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static me.fontys.semester4.postalcode.MunicipalityConverter.PC_ROW_MUN_CODE;
import static me.fontys.semester4.postalcode.MunicipalityConverter.PC_ROW_MUN_NAME;
import static me.fontys.semester4.postalcode.PCConverter.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Municipality Converter Test")
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MunicipalityConverterTest
{
    @Mock
    DatabaseBuilder databaseBuilder = mock(DatabaseBuilder.class,RETURNS_SELF);

    @Mock
    Database anImport = mock(Database.class, RETURNS_SELF);

    @Mock
    Index index = mock(Index.class,RETURNS_DEEP_STUBS);

    @Mock
    IndexCursor cursor;

    @Mock IndexHelper indexHelper;

    @Mock
    Table table;

    @Mock Row row;

    @InjectMocks
    private final MunicipalityConverter municipalityConverter = new MunicipalityConverter(databaseBuilder,indexHelper);

    @Test
    @DisplayName("Convert Test")
    public void doConvertTest() throws IOException
    {
        MockitoAnnotations.initMocks(this);
        Mockito.when(anImport.getTableNames()).thenReturn(Set.of(PC_TABLE,PC_MUNICIPALITIES_TABLE));
        Mockito.when(anImport.getTable(PC_MUNICIPALITIES_TABLE)).thenReturn(table);
        Mockito.when(table.getIndex(PC_ROW_MUN_CODE)).thenReturn(index);
        Mockito.when(indexHelper.createCursor(index)).thenReturn(cursor);

        // mock cursor iterator
        Iterator<Row> mockIterator = mock(Iterator.class);
        when(cursor.iterator()).thenReturn(mockIterator);
        when(mockIterator.hasNext()).thenReturn(true, false);
        when(mockIterator.next()).thenReturn(row);

        // define row test result
        when(row.getString(PC_ROW_MUN_NAME)).thenReturn("Test gemeente");
        when(row.getBigDecimal(PC_ROW_MUN_CODE)).thenReturn(BigDecimal.valueOf(3L));

        List<MunicipalityTemp> rows = municipalityConverter.doConvert();

        verify(mockIterator,atMostOnce()).next();
        verify(indexHelper.createCursor(index),atMostOnce()).getIndex();

        assertEquals(1, rows.size(),"invalid number of test rows returned");
        assertEquals("Test gemeente",rows.get(0).getName(),"invalid postalcode");
        assertEquals(3L,rows.get(0).getMunicipalityId());

    }
}
