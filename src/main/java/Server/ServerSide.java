package Server;

import shared.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerSide {

    static final int PORT = 8008;
    static final int MAX_CLIENTS = 3; // Limitar a 3 conexiones concurrentes
    private static final ExecutorService executorService = Executors.newFixedThreadPool(MAX_CLIENTS);

    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor está esperando conexiones en el puerto " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                executorService.submit(() -> handleClient(clientSocket));
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream())) {

            // Leer solicitud del cliente
            ClientRequest request = (ClientRequest) input.readObject();
            System.out.println("Solicitud recibida del cliente: " + request.operation);

            // Procesar la solicitud
            String response = handleRequest(request);

            // Enviar respuesta al cliente
            output.writeObject(response);
            output.flush();
            System.out.println("Respuesta enviada al cliente: " + response);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String handleRequest(ClientRequest request) {
        GameDAO gameDAO = new GameDAO();
        AchievementDAO achievementDAO = new AchievementDAO();
        PlatformDAO platformDAO = new PlatformDAO();
        GenreDAO genreDAO = new GenreDAO();
        String operation = request.operation;
        Object data = request.data;

        switch (operation) {
            case "CREATE":
                if (data instanceof Game) {
                    Game game = (Game) data;
                    gameDAO.save(game);
                    return "CREATED: " + game.toString();
                }
                if (data instanceof Achievement) {
                    Achievement achievement = (Achievement) data;

                    achievement.setGame(gameDAO.findGameById(((Achievement) data).getGame().getId()));
                    achievementDAO.save(achievement);
                    return "CREATED: " + achievement.toString();
                }
                if (data instanceof Platform) {
                    Platform platform = (Platform) data;

                    platformDAO.save(platform);
                    return "CREATED: " + platform.toString();
                }
                if (data instanceof Genre) {
                    Genre genre = (Genre) data;

                    genreDAO.save(genre);
                    return "CREATED: " + genre.toString();
                }
                return "Invalid data for operation: " + operation;
            case "READ":
                if (data instanceof Integer) {
                    Game game = gameDAO.findGameById((Integer) data);
                    return game != null ? game.toString() : "Game not found";
                }
                return "Invalid data for operation: " + operation;
            case "UPDATE":
                if (data instanceof Game) {
                    Game gameToUpdate = gameDAO.findGameById(((Game) data).getId());
                    if (gameToUpdate != null) {
                        gameToUpdate.setSlug((String) ((Game) data).getSlug());
                        gameToUpdate.setName((String)((Game) data).getName());
                        gameToUpdate.setRating((Double) ((Game) data).getRating());
                        gameDAO.update(gameToUpdate);
                        return "UPDATED: " + gameToUpdate.toString();
                    } else {
                        return "Game not found";
                    }

                }
                if (data instanceof Achievement) {
                    Achievement achievementToUpdate = achievementDAO.findAchievementById(((Achievement) data).getId());
                    if (achievementToUpdate != null) {
                        achievementToUpdate.setGame(gameDAO.findGameById(((Achievement) data).getGame().getId()));
                        achievementToUpdate.setName((String)((Achievement) data).getName());
                        achievementToUpdate.setDescription((String)((Achievement) data).getDescription());
                        achievementDAO.update(achievementToUpdate);
                        return "UPDATED: " + achievementToUpdate.toString();
                    } else {
                        return "Achievement not found";
                    }
                }
                if (data instanceof Platform) {
                    Platform platformToUpdate = platformDAO.findPlatformById(((Platform) data).getId());
                    if (platformToUpdate != null) {
                        platformToUpdate.setName((String)((Platform) data).getName());
                        platformDAO.update(platformToUpdate);
                        return "UPDATED: " + platformToUpdate.toString();
                    } else {
                        return "Platform not found";
                    }
                }
                if (data instanceof Genre) {
                    Genre genreToUpdate = genreDAO.findGenreById(((Genre) data).getId());
                    if (genreToUpdate != null) {
                        genreToUpdate.setName((String)((Genre) data).getName());
                        genreDAO.update(genreToUpdate);
                        return "UPDATED: " + genreToUpdate.toString();
                    } else {
                        return "Genre not found";
                    }
                }
                return "Invalid data for operation: " + operation;
            case "DELETE":
                if (data instanceof Game) {
                    Game gameToDelete = gameDAO.findGameById(((Game) data).getId());
                    if (gameToDelete != null) {
                        gameDAO.delete(gameToDelete);
                        return "DELETED";
                    } else {
                        return "Game not found";
                    }
                }
                if (data instanceof Genre) {
                    Genre genreToDelete = genreDAO.findGenreById(((Genre) data).getId());
                    if (genreToDelete != null) {
                        genreDAO.delete(genreToDelete);
                        return "DELETED";
                    } else {
                        return "Genre not found";
                    }
                }
                if (data instanceof Achievement) {
                    Achievement achievementToDelete = achievementDAO.findAchievementById(((Achievement) data).getId());
                    if (achievementToDelete != null) {
                        achievementDAO.delete(achievementToDelete);
                        return "DELETED";
                    } else {
                        return "achievement not found";
                    }
                }
                if (data instanceof Platform) {
                    Platform platformToDelete = platformDAO.findPlatformById(((Platform) data).getId());
                    if (platformToDelete != null) {
                        platformDAO.delete(platformToDelete);
                        return "DELETED";
                    } else {
                        return "Platform not found";
                    }
                }
            default:
                return "Unknown operation: " + operation;
        }
    }
}
