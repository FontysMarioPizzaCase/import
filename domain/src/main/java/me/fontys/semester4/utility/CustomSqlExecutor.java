package me.fontys.semester4.utility;

import org.hibernate.internal.SessionImpl;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class CustomSqlExecutor
{
    private EntityManagerFactory emf;

    public CustomSqlExecutor(EntityManagerFactory emf)
    {
        this.emf = emf;
    }

    protected void SQLExecute(String script, boolean transaction)
    {
        EntityManager em = emf.createEntityManager();
        if(transaction)
            em.getTransaction().begin();
        try
        {
            Statement st = em.unwrap(SessionImpl.class).connection().createStatement();
            st.executeUpdate(script);
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        if(transaction)
            em.getTransaction().commit();
    }

    public abstract void Execute();
}
