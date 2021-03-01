package me.fontys.semester4.interfaces;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileConverter<T>
{
    List<T> doConvert(File importFile) throws IOException;
}
