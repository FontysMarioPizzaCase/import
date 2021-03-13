package me.fontys.semester4.postalcode;

import com.healthmarketscience.jackcess.CursorBuilder;
import com.healthmarketscience.jackcess.Index;
import com.healthmarketscience.jackcess.IndexCursor;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IndexHelper
{

    protected IndexCursor createCursor(@NotNull Index index) throws IOException
    {
        return CursorBuilder.createCursor(index);
    }
}
