package com.catan.main.server;

import com.catan.main.datamodel.DataModel;
import com.catan.main.datamodel.User;
import com.catan.main.datamodel.game.CreateGameRequest;
import com.catan.main.datamodel.game.Game;
import com.catan.main.datamodel.player.Color;
import com.catan.main.persistence.ContextCreator;
import com.catan.main.persistence.DataAccessException;
import com.catan.main.persistence.DataContext;
import com.catan.main.persistence.DataUtils;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

enum ServerLogLevel {OFF, SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST, ALL}

public class ServerUtils {

    private static DataContext dataContext;

    // initializer
    public static void initialize(DataContext context) {
        dataContext = context;
        try {
            if (dataContext.getUserAccess().getAll().size() < 1) {

                User user1 = new User("Koli", "koli");
                User user2 = new User("Jake", "jake");
                User user3 = new User("Matt", "matt");
                User user4 = new User("Reed", "reed");

                try {
                    user1.setId((long) dataContext.getUserAccess().insert(user1));
                    user2.setId((long) dataContext.getUserAccess().insert(user2));
                    user3.setId((long) dataContext.getUserAccess().insert(user3));
                    user4.setId((long) dataContext.getUserAccess().insert(user4));
                } catch (Exception e) {
                    DataUtils.crashOnException(e);
                }

                // default game 1
                try {
                    Game game1 = createGame(new CreateGameRequest(true, true, true, "Default1"));
                    Game game2 = createGame(new CreateGameRequest(true, true, true, "Default2"));

                    dataContext.endTransaction(true);
                    dataContext.startTransaction();

                    addUserToGame(user1, Color.blue, 1L);
                    addUserToGame(user2, Color.green, 1L);
                    addUserToGame(user3, Color.orange, 1L);
                    addUserToGame(user4, Color.white, 1L);

                    // default game 2
                    addUserToGame(user1, Color.blue, 2L);
                    addUserToGame(user2, Color.green, 2L);
                    addUserToGame(user3, Color.white, 2L);
                } catch (Exception e) {
                    DataUtils.crashOnException(e);
                }

                // default game 3
                try {
                    Game game3 = createGame(new CreateGameRequest(true, true, true, "Default3"));
                } catch (Exception e) {
                    DataUtils.crashOnException(e);
                }
            }
        } catch (Exception ex) {
            DataUtils.crashOnException(ex);
        }
    }

    private static Logger LOGGER = LogManager.getLogManager().getLogger("global");

    public static Map<String, String> getCookies(HttpExchange exchange) {
        HashMap<String, String> cookiesMap = new HashMap<String, String>();
        String cookiesStr = exchange.getRequestHeaders().getFirst("cookie");
        if (cookiesStr != null) {
            String[] cookies = cookiesStr.split("; ");
            for (String cookie : cookies) {
                String[] keyValuePair = cookie.split("=", -1);
                String key = keyValuePair[0];
                String value = keyValuePair[1];
                cookiesMap.put(key, value);
            }
        }
        return cookiesMap;
    }

    public static Map<String, String> getValuesFromForm(HttpExchange exchange) throws IOException {
        String requestString = streamToString(exchange.getRequestBody());
        LOGGER.log(Level.FINER, requestString);
        return decodeUri(requestString);
    }

    public static Map<String, String> decodeUri(String uri) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        if (uri != null) {
            String[] pairings = uri.split("&");
            for (String qPair : pairings) {
                String left = qPair.split("=", -1)[0];
                String right = qPair.split("=", -1)[1];
                parameters.put(left, right);
            }
        }
        return parameters;
    }

    public static Client getClient(HttpExchange exchange) {
        Map<String, String> cookies = getCookies(exchange);
        if (cookies.containsKey("catan.user")) {
            String playerInfoJSONCookie = cookies.get("catan.user");
            String cookieValue = URLDecoder.decode(playerInfoJSONCookie);
            User user = new Gson().fromJson(cookieValue, User.class);
            Client client = new Client(user);
            String gameId = cookies.get("catan.game");
            if (gameId != null) {
                client.setGameID(Long.parseLong(cookies.get("catan.game")));
            }
            return client;
        }
        return null;
    }

    public static User getUserCookie(HttpExchange exchange) {
        Map<String, String> cookies = getCookies(exchange);
        if (cookies.containsKey("catan.user")) {
            String user = cookies.get("catan.user");
            String value = URLDecoder.decode(user);
            return new Gson().fromJson(value, User.class);
        }
        return null;
    }

    public static String streamToString(InputStream stream) {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "failed to read input stream");
        }
        return sb.toString();
    }

    public static DataModel getModel(HttpExchange exchange) throws DataAccessException {
        Client client = ServerUtils.getClient(exchange);
        Game game = getGame(client.getGameID());
        return game.getModel();
    }

    public static User registerUser(User newUser) throws DataAccessException {
        if ((canRegister(newUser.getName())) && (
                (newUser.getId() == null) || (dataContext.getUserAccess().get((int) newUser.getId().longValue()) == null))) {
            newUser.generateAuthentication();
            dataContext.getUserAccess().insert(newUser);
            return newUser;
        }
        return null;
    }

    public static boolean canRegister(String name) throws DataAccessException {
        return getPlayerInfo(name) == null;
    }

    public static User validateLogin(String username, String password) throws DataAccessException {
        User player = getPlayerInfo(username);
        if ((player != null) && (password.equals(player.getPassword()))) {
            return player;
        }
        return null;
    }

    public static User validateUser(User outsideUser) throws DataAccessException {
        return validateLogin(outsideUser.getName(), outsideUser.getPassword());
    }

    public static boolean addUserToGame(User user, Color color, long gameID) throws Exception {
        Game g = (Game) dataContext.getGameAccess().get((int) gameID);
        if (user == null) {
            LOGGER.log(Level.WARNING, "Unable to grab player info from cookie.");
            return false;
        }
        if (g != null) {
            for (int i = 0; i < 4; i++) {
                if (g.getModel().getPlayers()[i] != null) {
                    if (g.getModel().getPlayers()[i].getColor().equals(color) && !g.getModel().getPlayers()[i].getName().equals(user.getName())) {
                        throw new Exception("Someone has that color");
                    }
                }
            }
            if (LOGGER != null) {
                LOGGER.log(Level.FINE, user.toString());
            }
            if (g.addPlayer(user.getId(), color, user.getName())) {
                dataContext.getGameAccess().update(g);
                return true;
            }
        }
        return false;
    }

    public static Game createGame(CreateGameRequest request) throws DataAccessException {
        Game g = Game.requestNewGame(request);
        g.setId((long) dataContext.getGameAccess().insert(g));
        return g;
    }

    public static boolean resetGame(Client client) throws DataAccessException {
        int gameId = (int) client.getGameID();
        Game game = (Game) dataContext.getGameAccess().get(gameId);
        if (game != null) {
            game.reset();
            dataContext.getGameAccess().update(game);
            return true;
        }
        return false;
    }

    public static ArrayList<GsonGame> getGames() throws DataAccessException {
        ArrayList<GsonGame> gameStubs = new ArrayList<GsonGame>();
        List<Game> games = dataContext.getGameAccess().getAll();
        for (Game game : games) {
            gameStubs.add(new GsonGame(game));
        }
        return gameStubs;
    }

    public static User getPlayerInfo(long playerId) throws DataAccessException {
        return (User) dataContext.getUserAccess().get((int) playerId);
    }

    public static User getPlayerInfo(String username) throws DataAccessException {
        List<User> users = dataContext.getUserAccess().getAll();
        for (User user : users) {
            if (user.getName().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static Game getGame(long gameId) throws DataAccessException {
        Game game = (Game) dataContext.getGameAccess().get((int) gameId);
        dataContext.getCommandAccess().fastForward(game);
        return game;
    }
}