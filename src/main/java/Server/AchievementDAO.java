package Server;

import shared.Achievement;
import org.hibernate.Session;
import org.hibernate.Transaction;
import shared.Game;

import java.util.List;

public class AchievementDAO {

    HibernateUtil hibernateUtil = new HibernateUtil();

    public void save(Achievement achievement) {
        Transaction tx = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // Check if a game_id is provided
            if (achievement.getGame() != null && achievement.getGame().getId() != null) {
                // Fetch the Game entity from the database
                Game game = session.get(Game.class, achievement.getGame().getId());
                if (game != null) {
                    achievement.setGame(game); // Associate the managed Game entity with the Achievement
                } else {
                    throw new RuntimeException("Game with ID " + achievement.getGame().getId() + " does not exist.");
                }
            } else {
                throw new RuntimeException("Game ID must be provided for Achievement.");
            }

            // Persist the Achievement
            session.persist(achievement);
            tx.commit();
            System.out.println("Saved achievement: " + achievement);
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("Error saving achievement: " + e.getMessage());
            throw e;
        }
    }


    public Achievement findAchievementById(int id) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            return session.get(Achievement.class, id);
        } catch (Exception e) {
            System.out.println("Achievement " + id + " does not exist.");
            return null;
        }
    }

    public List<Achievement> getAllAchievements() {
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Achievement").list();
        } catch (Exception e) {
            System.out.println("Achievement " + e + " does not exist.");
            return null;
        }
    }

    public void update(Achievement achievement) {
        Transaction  tx = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(achievement);
            tx.commit();
            System.out.println("Updated achievement " + achievement);
        } catch (Exception e) {
            if (tx != null) {
                System.out.println("Achievement " + achievement.getId() + " already exists.");
            }
        }
    }

    public void delete(Achievement achievement) {
        Transaction  tx = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.delete(achievement);
            tx.commit();
            System.out.println("Deleted achievement " + achievement);
        } catch (Exception e) {
            if (tx != null) {
                System.out.println("Achievement " + achievement.getId() + " does not exist.");
                tx.rollback();
            }
        }
    }

}
