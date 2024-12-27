package Server;

import shared.Platform;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PlatformDAO {
    HibernateUtil hibernateUtil = new HibernateUtil();

    public void save(Platform platform) {
        Transaction tx = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(platform);
            tx.commit();
            System.out.println("Saved Platform " + platform);
        } catch (Exception e) {
            if (tx != null) {
                System.out.println("Platform " + platform.getId() + " already exists.");
                tx.rollback();
            }
        }
    }

    public Platform findPlatformById(int id) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            return session.get(Platform.class, id);
        } catch (Exception e) {
            System.out.println("Platform " + id + " does not exist.");
            return null;
        }
    }

    public List<Platform> getAllPlatforms() {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Platform ").list();
        } catch (Exception e) {
            System.out.println("Platform " + e + " does not exist.");
            return null;
        }
    }

    public void update(Platform platform) {
        Transaction  tx = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(platform);
            tx.commit();
            System.out.println("Updated platform " + platform);
        } catch (Exception e) {
            if (tx != null) {
                System.out.println("Platform " + platform.getId() + " already exists.");
            }
        }
    }

    public void delete(Platform platform) {
        Transaction  tx = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.delete(platform);
            tx.commit();
            System.out.println("Deleted platform " + platform);
        } catch (Exception e) {
            if (tx != null) {
                System.out.println("Platform " + platform.getId() + " does not exist.");
                tx.rollback();
            }
        }
    }
}
