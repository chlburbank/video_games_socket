package Server;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    public SessionFactory getSessionFactory() {
        try {
            Configuration configuration = new Configuration().configure();
            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void shutdown() {
        getSessionFactory().close();
    }
}
