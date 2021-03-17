package me.fontys.semester4.utils;

import org.hibernate.internal.SessionImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class StoredProcedureExecutor {

    private final EntityManagerFactory entityManagerFactory;

    public StoredProcedureExecutor(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     *
     * @param statement
     * @param transaction
     * @throws SQLException
     */
    public void executeSql(String statement, boolean transaction) throws SQLException
    {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        if(transaction) {
            entityManager.getTransaction().begin();
        }
        Statement _statement = entityManager.unwrap(SessionImpl.class).connection().createStatement();
        _statement.executeUpdate(statement);
        if(transaction) {
            entityManager.getTransaction().commit();
        }
    }

    public void createOrReplaceStoredProcedure(Resource resource) throws IOException, SQLException {
        executeSql(StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset()), true);
    }
}
