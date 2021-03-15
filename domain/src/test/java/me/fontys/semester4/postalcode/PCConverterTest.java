package me.fontys.semester4.postalcode;

import com.healthmarketscience.jackcess.*;
import me.fontys.semester4.tempdata.entity.PostalcodeTemp;
import org.junit.jupiter.api.*;
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

import static me.fontys.semester4.postalcode.PCConverter.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("PostalCode Converter Test")
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PCConverterTest
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

    @Mock Table table;

    @Mock Row row;

    @InjectMocks
    private final PCConverter pcConverter = new PCConverter(databaseBuilder,indexHelper);

    @Test
    @DisplayName("Convert Test")
    public void doConvertTest() throws IOException
    {
        MockitoAnnotations.initMocks(this);
        Mockito.when(anImport.getTableNames()).thenReturn(Set.of(PC_TABLE,PC_MUNICIPALITIES_TABLE));
        Mockito.when(anImport.getTable(PC_TABLE)).thenReturn(table);
        Mockito.when(table.getIndex(PC_ROW_PC)).thenReturn(index);
        Mockito.when(indexHelper.createCursor(index)).thenReturn(cursor);

        // mock cursor iterator
        Iterator<Row> mockIterator = mock(Iterator.class);
        when(cursor.iterator()).thenReturn(mockIterator);
        when(mockIterator.hasNext()).thenReturn(true, false);
        when(mockIterator.next()).thenReturn(row);

        // define row test result
        when(row.getString(PC_ROW_PC)).thenReturn("1234 AB");
        when(row.getString(PC_ROW_RANGE_TYPE)).thenReturn("1");
        when(row.getString(PC_ROW_STREET)).thenReturn("test Straat");
        when(row.getBigDecimal(PC_ROW_RANGE_START)).thenReturn(BigDecimal.valueOf(2L));
        when(row.getBigDecimal(PC_ROW_RANGE_END)).thenReturn(BigDecimal.valueOf(100L));
        when(row.getBigDecimal(PC_ROW_PCMUN_CODE)).thenReturn(BigDecimal.valueOf(123L));

        List<PostalcodeTemp> rows = pcConverter.doConvert();

        verify(mockIterator,atMostOnce()).next();
        verify(indexHelper.createCursor(index),atMostOnce()).getIndex();

        assertEquals(1, rows.size(),"invalid number of test rows returned");
        assertEquals("1234AB",rows.get(0).getPostalcode(),"invalid postalcode");
        assertEquals(2L,rows.get(0).getStarthousenr());
        assertEquals(100L,rows.get(0).getEndhousenr());
        assertEquals(123L,rows.get(0).getMunicipality());
        assertEquals("test Straat",rows.get(0).getStreet());

    }
}
