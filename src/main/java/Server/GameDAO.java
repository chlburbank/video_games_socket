package Server;

import shared.Game;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class GameDAO {
    HibernateUtil hibernateUtil = new HibernateUtil();

    public void save(Game game) {
        Transaction tx = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(game);
            tx.commit();
            System.out.println("Saved Game " + game);
        } catch (Exception e) {
            if (tx != null) {
                System.out.println("Game " + game.getId() + " already exists.");
                tx.rollback();
            }
        }
    }

    public Game findGameById(int id) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            return session.get(Game.class, id);
        } catch (Exception e) {
            System.out.println("Game " + id + " does not exist.");
            return null;
        }
    }

    public List<Game> getAllGames() {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Game ").list();
        } catch (Exception e) {
            System.out.println("Game " + e + " does not exist.");
            return null;
        }
    }

    public void update(Game game) {
        Transaction  tx = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(game);
            tx.commit();
            System.out.println("Updated game " + game);
        } catch (Exception e) {
            if (tx != null) {
                System.out.println("Game " + game.getId() + " already exists.");
            }
        }
    }

    public void delete(Game game) {
        Transaction  tx = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.delete(game);
            tx.commit();
            System.out.println("Deleted game " + game);
        } catch (Exception e) {
            if (tx != null) {
                System.out.println("Game with id " + game.getId() + " does not exist.");
                tx.rollback();
            }
        }
    }
}
