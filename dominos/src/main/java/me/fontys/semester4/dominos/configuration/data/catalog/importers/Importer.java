package me.fontys.semester4.dominos.configuration.data.catalog.importers;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.HasExtendedLogger;
import java.io.IOException;

public interface Importer extends HasExtendedLogger {
    void doImport() throws IOException;
}
