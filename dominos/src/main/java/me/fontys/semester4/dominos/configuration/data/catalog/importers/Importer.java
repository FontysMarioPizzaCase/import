package me.fontys.semester4.dominos.configuration.data.catalog.importers;
import me.fontys.semester4.dominos.configuration.data.catalog.logging.HasDatabaseLogger;
import java.io.IOException;

public interface Importer extends HasDatabaseLogger {
    void doImport() throws IOException;
}
