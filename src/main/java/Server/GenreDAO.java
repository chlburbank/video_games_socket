package Server;

import shared.Genre;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class GenreDAO {
    HibernateUtil hibernateUtil = new HibernateUtil();

    public void save(Genre genre) {
        Transaction tx = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(genre);
            tx.commit();
            System.out.println("Saved Genre " + genre);
        } catch (Exception e) {
            if (tx != null) {
                System.out.println("Genre " + genre.getId() + " already exists.");
                tx.rollback();
            }
        }
    }

    public Genre findGenreById(int id) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            return session.get(Genre.class, id);
        } catch (Exception e) {
            System.out.println("Genre " + id + " does not exist.");
            return null;
        }
    }

    public List<Genre> getAllGenres() {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Genre").list();
        } catch (Exception e) {
            System.out.println("Genre " + e + " does not exist.");
            return null;
        }
    }

    public void update(Genre genre) {
        Transaction  tx = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(genre);
            tx.commit();
            System.out.println("Updated Genre " + genre);
        } catch (Exception e) {
            if (tx != null) {
                System.out.println("Genre " + genre.getId() + " already exists.");
            }
        }
    }

    public void delete(Genre genre) {
        Transaction  tx = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.delete(genre);
            tx.commit();
            System.out.println("Deleted achievement " + genre);
        } catch (Exception e) {
            if (tx != null) {
                System.out.println("Genre " + genre.getId() + " does not exist.");
                tx.rollback();
            }
        }
    }
}
