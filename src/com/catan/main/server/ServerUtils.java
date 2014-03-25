package com.catan.main.server;

import com.catan.main.datamodel.User;
import com.catan.main.datamodel.game.CreateGameRequest;
import com.catan.main.datamodel.game.Game;
import com.catan.main.datamodel.player.Color;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

class ServerUtils {
    private static Logger LOGGER = LogManager.getLogManager().getLogger("global");
    private static Map<Long, User> _users = new HashMap<Long, User>();
    private static Map<Long, Game> _games = new HashMap<Long, Game>();
    private static Long _userId = 0L;
    private static Long _gameId = 0L;

    private static Long getUserId() {
        return _userId++;
    }

    private static Long getGameId() {
        return _gameId++;
    }

    static Map<String, String> getCookies(HttpExchange exchange) {
        HashMap<String, String> cookiesMap = new HashMap();
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

    static Map<String, String> getValuesFromForm(HttpExchange exchange) throws IOException {
        String requestString = streamToString(exchange.getRequestBody());
        LOGGER.log(Level.FINER, requestString);
        return decodeUri(requestString);
    }

    static Map<String, String> decodeUri(String uri) {
        HashMap<String, String> parameters = new HashMap();
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

    static Client getSenderInformation(HttpExchange exchange) {
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

    static String streamToString(InputStream stream) {
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


    public static User registerUser(User newUser) {
        if ((canRegister(newUser.getName())) && (
                (newUser.getPlayerID() == null) || (_users.get(newUser.getPlayerID()) == null))) {
            newUser.generateAuthentication();
            newUser.setPlayerID(getUserId());
            _users.put(newUser.getPlayerID(), newUser);
            return newUser;
        }
        return null;
    }

    protected static boolean canRegister(String name) {
        return getPlayerInfo(name) == null;
    }

    public static User validateLogin(String username, String password) {
        User player = getPlayerInfo(username);
        if ((player != null) && (password.equals(player.getPassword()))) {
            return player;
        }
        return null;
    }

    public static User validateUser(User outsideUser) {
        return validateLogin(outsideUser.getName(), outsideUser.getPassword());
    }

    public static boolean addUserToGame(User playerInfo, Color color, Long gameID) {
        Game g = _games.get(gameID);
        if (playerInfo == null) {
            LOGGER.log(Level.WARNING, "Unable to grab player info from cookie.");
            return false;
        }
        if (g != null) {
            LOGGER.log(Level.FINE, playerInfo.toString());
            if (g.addPlayer(playerInfo.getPlayerID(), color, playerInfo.getName())) {
                _games.put(g.getId(), g);
                return true;
            }
        }
        return false;
    }

    public static Game createGame(CreateGameRequest request) {
        Game g = Game.requestNewGame(request);
        g.setId(getGameId());
        _games.put(g.getId(), g);
        return g;
    }

    public static boolean resetGame(Client client) {
        Long gameId = client.getGameID();
        Game g = _games.get(gameId);
        if (g != null) {
            Game updated = g;
            updated.reset();
            _games.put(gameId, updated);
            return true;
        }
        return false;
    }

    public static ArrayList<GsonGame> getGames() {
        ArrayList<GsonGame> gameStubs = new ArrayList<GsonGame>();
        for (Game game : _games.values()) {
            gameStubs.add(new GsonGame(game));
        }
        return gameStubs;
    }

    protected static User getPlayerInfo(Long playerId) {
        return _users.get(playerId);
    }

    protected static User getPlayerInfo(String username) {
        for (User user : _users.values()) {
            if (user.getName().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static Game getGame(Long gameId) {
        return _games.get(gameId);
    }
}