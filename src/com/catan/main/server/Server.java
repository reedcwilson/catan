package com.catan.main.server;

import com.catan.main.datamodel.DataModel;

import com.catan.main.datamodel.User;

import com.catan.main.datamodel.commands.*;
import com.catan.main.datamodel.game.CreateGameRequest;
import com.catan.main.datamodel.game.Game;
import com.catan.main.datamodel.player.Color;
import com.catan.main.persistence.ContextCreator;
import com.catan.main.persistence.DataAccessException;
import com.catan.main.persistence.DataContext;
import com.catan.main.persistence.DataUtils;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.Headers;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.*;
import java.net.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class Server {

    //region Fields
    private static final int SERVER_PORT_NUMBER = 8081;
    private static final int MAX_WAITING_CONNECTIONS = 10;
    private static final int OPERATIONS_TILL_SAVE = 10;
    private final String _serverErrorStr = "ERROR 500. Internal Server Error: ";
    private final String _successStr = "Success!";
    private final String _clientErrorStr = "ERROR 400. Client Error: ";
    private final String _textStr = "text/html";
    private final String _jsonStr = "application/json";
    private int _port;
    private HttpServer _server;
    private Gson _gson;
    private DataContext dataContext;
    private int executesBetweenSaves;
    private int currentExecuteIndex = 0;

    private DataContext getDataContext() {
        return this.dataContext;
    }
    private void setDataContext(DataContext dataContext) {
        this.dataContext = dataContext;
    }
    //endregion

    //region Helper Methods
    private void respondWithString(HttpExchange exchange, String message, int code, String contentType) throws IOException {
        byte[] bytes = message.getBytes();
        exchange.getResponseHeaders().add("Content-Type", contentType);
        exchange.sendResponseHeaders(code, bytes.length);
        exchange.getResponseBody().write(bytes);
    }
    private void handleCommand(HttpExchange exchange, Class type) throws IOException, DataAccessException {
        dataContext.startTransaction();
        String json = ServerUtils.streamToString(exchange.getRequestBody());
        DataModel model = ServerUtils.getModel(exchange);
        Command command = (Command)_gson.fromJson(json, type);
        Client client = ServerUtils.getClient(exchange);
        Game game = ServerUtils.getGame(client.getGameID());
        command.initialize(dataContext, game.getId());
        command.execute(model);
        persistGame(game);
        String response = model.toJSON();
        respondWithString(exchange, response, 200, _jsonStr);
        dataContext.endTransaction(true);
    }
    private void persistGame(Game game) throws DataAccessException {
        if (executesBetweenSaves == (currentExecuteIndex = (++currentExecuteIndex % executesBetweenSaves + 1))) {
            dataContext.getGameAccess().update(game);
        }
    }
    private void handleServerException(HttpExchange exchange, Exception e) throws IOException {
        // send server exception response header
        System.out.println(_serverErrorStr + e.getMessage());
        e.printStackTrace();
        respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
        dataContext.endTransaction(false);
    }
    //endregion

    //region Handlers

	//region User

    //region Login
    private HttpHandler loginHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                dataContext.startTransaction();
                Map<String, String> values = ServerUtils.getValuesFromForm(exchange);
                String username = values.get("username");
                String password = values.get("password");
                User user = ServerUtils.validateLogin(username, password);
                Headers headers = exchange.getResponseHeaders();
                headers.set("Content-Type", _textStr);
                if (user != null) {
                    String cookie = new Gson().toJson(user);
                    String encodedCookie = URLEncoder.encode(cookie);
                    headers.set("Access-Control-Allow-Origin", "*");
                    headers.set("Set-cookie", "catan.user=" + encodedCookie + ";Path=/;");

                    respondWithString(exchange, _successStr, 200, "test/html");
                } else {
                    respondWithString(exchange, "Invalid Username or Password", 400, _textStr);
                }
                dataContext.endTransaction(true);
            } catch (Exception e) {
                // send server exception response header
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
                dataContext.endTransaction(false);
            }
            exchange.close();
        }
    };
    //endregion

    //region Register
    private HttpHandler registerHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                dataContext.startTransaction();
                Map<String, String> values = ServerUtils.getValuesFromForm(exchange);
                String username = values.get("username");
                String password = values.get("password");
                Headers headers = exchange.getResponseHeaders();
                User loginResult = ServerUtils.registerUser(new User(username, password));

                headers.set("Content-Type", _textStr);
                if (loginResult != null) {
                    String jsonCookieData = new Gson().toJson(loginResult);

                    String cookieData = URLEncoder.encode(jsonCookieData);
                    headers.set("Access-Control-Allow-Origin", "*");
                    headers.set("Set-cookie", "catan.user=" + cookieData + ";Path=/;");

                    respondWithString(exchange, _successStr, 200, _textStr);
                } else {
                    respondWithString(exchange, _clientErrorStr + "Register unsuccessful. Username may not be unique", 400, _textStr);
                }
                dataContext.endTransaction(true);
            } catch (Exception e) {
                // send server exception response header
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, 500, _textStr);
                dataContext.endTransaction(false);
            }
            exchange.close();
        }
    };
    //endregion

    //endregion

    //region Games

    //region List
    private HttpHandler listGamesHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                dataContext.startTransaction();
                String response = new Gson().toJson(ServerUtils.getGames());
                respondWithString(exchange, response, 200, _jsonStr);
                dataContext.endTransaction(true);
            } catch (Exception e) {
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
                dataContext.endTransaction(false);
            }
            exchange.close();
        }
    };
    //endregion

    //region Create
    private HttpHandler createGameHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                dataContext.startTransaction();
                Map<String, String> values = ServerUtils.getValuesFromForm(exchange);

                CreateGameRequest request = new CreateGameRequest(
                        Boolean.parseBoolean(values.get("randomHexes")),
                        Boolean.parseBoolean(values.get("randomTiles")),
                        Boolean.parseBoolean(values.get("randomtile")),
                        values.get("name"));

                GsonGame game = new GsonGame(ServerUtils.createGame(request));
                String gameStr = new Gson().toJson(game);
                respondWithString(exchange, gameStr, 200, _jsonStr);
                dataContext.endTransaction(true);
            } catch (Exception e) {
                handleServerException(exchange, e);
            }
            exchange.close();
        }
    };
    //endregion

    //region Join
    private HttpHandler joinGameHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                dataContext.startTransaction();
                Map<String, String> values = ServerUtils.getValuesFromForm(exchange);
                Long gameId = Long.parseLong(values.get("id"));
                Color color = Color.valueOf(values.get("color"));
                User user = ServerUtils.getUserCookie(exchange);
                if (ServerUtils.addUserToGame(user, color, gameId)) {
                    exchange.getResponseHeaders().set("Set-cookie", "catan.game=" + gameId + ";Path=/;");
                    respondWithString(exchange, _successStr, 200, _textStr);
                } else {
                    respondWithString(exchange, _clientErrorStr + "The game is full.", 400, _textStr);
                }
                dataContext.endTransaction(true);
            } catch (Exception e) {
                handleServerException(exchange, e);
            }
            exchange.close();
        }
    };
    //endregion

    //endregion

    //region Game

    //region Model
    private HttpHandler getModelHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                dataContext.startTransaction();
                Client client = ServerUtils.getClient(exchange);
                Game game = ServerUtils.getGame(client.getGameID());
                String versionString = ServerUtils.decodeUri(exchange.getRequestURI().getQuery()).get("revision");
                int version = versionString == null ? -1 : Integer.parseInt(versionString);
                if (game != null) {
                    String response;
                    if (game.getModel().getRevision() == version) {
                        response = "\"true\"";
                    } else {
                        response = ServerUtils.getGame(client.getGameID()).getModel().toJSON();
                    }
                    respondWithString(exchange, response, 200, _jsonStr);
                } else {
                    respondWithString(exchange, _clientErrorStr + "no game found", 400, _textStr);
                }
                dataContext.endTransaction(true);
            } catch (Exception e) {

                // send server exception response header
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
                dataContext.endTransaction(false);
            }
            exchange.close();
        }
    };
    //endregion

    //region Reset
    private HttpHandler resetGameHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                dataContext.startTransaction();
                Client client = ServerUtils.getClient(exchange);
                Game game = ServerUtils.getGame(client.getGameID());
                if (!ServerUtils.resetGame(client) || game == null) {
                    respondWithString(exchange, _clientErrorStr, 400, _textStr);
                }
                String response = game.getModel().toJSON();
                exchange.getResponseHeaders().set("Content-Type", _jsonStr);
                respondWithString(exchange, response, 200, _jsonStr);
                dataContext.endTransaction(true);
            } catch (Exception e) {

                // send server exception response header
                handleServerException(exchange, e);
            }
            exchange.close();
        }
    };
    //endregion

    //region Commands
    private HttpHandler getCommandsHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                dataContext.startTransaction();
                Client client = ServerUtils.getClient(exchange);
                Game game = ServerUtils.getGame(client.getGameID());
                if (game == null) {
                    respondWithString(exchange, _clientErrorStr, 400, _textStr);
                } else {
                    String response = _gson.toJson(game.getHistory().getCommands());
                    respondWithString(exchange, response, 200, _jsonStr);
                }
                dataContext.endTransaction(true);
            } catch (Exception e) {

                // send server exception response header
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
                dataContext.endTransaction(false);
            }
            exchange.close();
        }
    };
    //endregion

    //region Commands - Post
    private HttpHandler postCommandsHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                // TODO: I am really unsure about this one
                dataContext.startTransaction();
                String requestString = ServerUtils.streamToString(exchange.getRequestBody());
                List<Command> commands = _gson.fromJson(requestString, new TypeToken() {}.getType());
                Client client = ServerUtils.getClient(exchange);
                Game game = ServerUtils.getGame(client.getGameID());
                DataModel model = game.getModel();
                for (Command command : commands) {
                    command.execute(model);
                }
                String response = game.getModel().toJSON();
                respondWithString(exchange, response, 200, _jsonStr);
                dataContext.endTransaction(true);
            } catch (Exception e) {

                // send server exception response header
                handleServerException(exchange, e);
                dataContext.endTransaction(false);
            }
            exchange.close();
        }
    };
    //endregion

    //region listAi
    private HttpHandler listAiHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                respondWithString(exchange, "[ \"LARGEST_ARMY\" ]", 200, _jsonStr);
            } catch (Exception e) {

                // send server exception response header
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
            }
            exchange.close();
        }
    };
    //endregion

    //region addAi
    private HttpHandler addAiHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                respondWithString(exchange, _successStr, 200, _textStr);
            } catch (Exception e) {

                // send server exception response header
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
            }
            exchange.close();
        }
    };
    //endregion

    //endregion

    //region Util
    private HttpHandler changeLogLevelHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {

                // TODO: make this actually work
                respondWithString(exchange, _clientErrorStr, 400, _textStr);

            } catch (Exception e) {

                // send server exception response header
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
            }
            exchange.close();
        }
    };
    //endregion

    //region Moves

    //region SendChat
    private HttpHandler sendChatHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                handleCommand(exchange, SendChat.class);
            } catch (Exception e) {
                handleServerException(exchange, e);


            }
            exchange.close();
        }
    };
    //endregion

    //region acceptTrade
    private HttpHandler acceptTradeHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                handleCommand(exchange, AcceptTrade.class);
            } catch (Exception e) {

                // send server exception response header
                handleServerException(exchange, e);
            }
            exchange.close();
        }
    };
    //endregion

    //region discard
    private HttpHandler discardCardsHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                handleCommand(exchange, Discard.class);
            } catch (Exception e) {

                // send server exception response header
                handleServerException(exchange, e);
            }
            exchange.close();
        }
    };
    //endregion

    //region roll
    private HttpHandler rollNumberHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                handleCommand(exchange, RollDice.class);
            } catch (Exception e) {

                // send server exception response header
                handleServerException(exchange, e);
            }
            exchange.close();
        }
    };
    //endregion

    //region buildRoad
    private HttpHandler buildRoadHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                handleCommand(exchange, PlaceRoad.class);
            } catch (Exception e) {

                // send server exception response header
                handleServerException(exchange, e);
            }
            exchange.close();
        }
    };
    //endregion

    //region buildSettlement
    private HttpHandler buildSettlementHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                handleCommand(exchange, PlaceSettlement.class);
            } catch (Exception e) {

                // send server exception response header
                handleServerException(exchange, e);
            }
            exchange.close();
        }
    };
    //endregion

    //region buildCity
    private HttpHandler buildCityHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                handleCommand(exchange, PlaceCity.class);
            } catch (Exception e) {

                // send server exception response header
                handleServerException(exchange, e);
            }
            exchange.close();
        }
    };
    //endregion

    //region offerTrade
    private HttpHandler offerTradeHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                handleCommand(exchange, OfferTrade.class);
            } catch (Exception e) {

                // send server exception response header
                handleServerException(exchange, e);
            }
            exchange.close();
        }
    };
    //endregion

    //region offerTrade
    private HttpHandler robPlayerHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                handleCommand(exchange, RobPlayer.class);
            } catch (Exception e) {

                // send server exception response header
                handleServerException(exchange, e);
            }
            exchange.close();
        }
    };
    //endregion

    //region maritimeTrade
    private HttpHandler maritimeTradeHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                handleCommand(exchange, MaritimeTrade.class);
            } catch (Exception e) {

                // send server exception response header
                handleServerException(exchange, e);
            }
            exchange.close();
        }
    };
    //endregion

    //region finishTurn
    private HttpHandler finishTurnHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                handleCommand(exchange, FinishTurn.class);
            } catch (Exception e) {

                // send server exception response header
                handleServerException(exchange, e);
            }
            exchange.close();
        }
    };
    //endregion

    //region buyDevCard
    private HttpHandler buyDevCardHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                handleCommand(exchange, BuyDevCard.class);
            } catch (Exception e) {

                // send server exception response header
                handleServerException(exchange, e);
            }
            exchange.close();
        }
    };
    //endregion

    //region yearOfPlenty
    private HttpHandler yearOfPlentyHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                handleCommand(exchange, YearOfPlenty.class);
            } catch (Exception e) {

                // send server exception response header
                handleServerException(exchange, e);
            }
            exchange.close();
        }
    };

    //endregion

    //region roadBuilding
    private HttpHandler roadBuildingHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                handleCommand(exchange, RoadBuilding.class);
            } catch (Exception e) {

                // send server exception response header
                handleServerException(exchange, e);
            }
            exchange.close();
        }
    };
    //endregion

    //region soldier
    private HttpHandler soldierHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                handleCommand(exchange, Soldier.class);
            } catch (Exception e) {

                // send server exception response header
                handleServerException(exchange, e);
            }
            exchange.close();
        }
    };
    //endregion

    //region monopoly
    private HttpHandler monopolyHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                handleCommand(exchange, Monopoly.class);
            } catch (Exception e) {

                // send server exception response header
                handleServerException(exchange, e);
            }
            exchange.close();
        }
    };
    //endregion

    //region monument
    private HttpHandler monumentHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                handleCommand(exchange, Monument.class);
            } catch (Exception e) {

                // send server exception response header
                handleServerException(exchange, e);
            }
            exchange.close();
        }
    };
    //endregion

    //endregion

    //region Misc
    private HttpHandler getSwaggerFilesHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            File file = null;
            URI uri = exchange.getRequestURI();
            String path = uri.getPath().substring(1);

            InputStream stream = null;
            try {
                if (path.equals("docs/api/data")) {
                    String str = "{\"apiVersion\":\"1.0\",\"swaggerVersion\":\"1.2\",\"apis\":[{\"path\":\"/user\",\"description\":\"Operations about users\"},{\"path\":\"/games\",\"description\":\"Game queries/actions (pre-joining)\"},{\"path\":\"/game\",\"description\":\"Operations for the game you're in. (requires cookie)\"},{\"path\":\"/moves\",\"description\":\"Actions you can take mid game. (requires cookie)\"},{\"path\":\"/util\",\"description\":\"Change how the server runs\"}]}";
                    byte[] bytes = str.getBytes();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, bytes.length);
                    exchange.getResponseBody().write(bytes, 0, bytes.length);
                } else {
                    file = new File("./" + path + ".json");
                    byte[] bytes = new byte[(int) file.length()];
                    stream = new BufferedInputStream(new FileInputStream(file));
                    stream.read(bytes, 0, bytes.length);
                    exchange.getResponseHeaders().add("Content-Type", _jsonStr);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, bytes.length);
                    exchange.getResponseBody().write(bytes, 0, bytes.length);
                }
            } catch (Exception e) {
                System.out.println("ERROR 500. Internal Server Error" + e.getMessage());
                e.printStackTrace();
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
            } finally {
                exchange.close();
                if (stream != null) {
                    stream.close();
                }
            }
        }
    };
    private HttpHandler downloadFileHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            File file = null;
            URI uri = exchange.getRequestURI();
            String path = uri.getPath().substring(1);

            if (path.isEmpty()) {                 // default to index.html
                file = new File("./gameplay/index.html");
            } else if (path.startsWith("docs")) {
                file = new File("./" + path);
            } else {                             // go to gameplay
                file = new File("./gameplay/" + path);
            }

            InputStream stream = null;
            try {
                if (file.exists() && file.isFile()) {
                    // read in file and send it
                    byte[] bytes = new byte[(int) file.length()];
                    stream = new BufferedInputStream(new FileInputStream(file));
                    stream.read(bytes, 0, bytes.length);

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, bytes.length);
                    exchange.getResponseBody().write(bytes, 0, bytes.length);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, -1);
                }
            } catch (Exception e) {
                System.out.println("ERROR 500. Internal Server Error" + e.getMessage());
                e.printStackTrace();
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
            } finally {
                exchange.close();
                if (stream != null)
                    stream.close();
            }
        }
    };
    private HttpHandler changePersistenceHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                Map<String, String> values = ServerUtils.getValuesFromForm(exchange);
                String directory = values.get("directory");
                String className = values.get("className");
                DataContext dataContext = ContextCreator.getDataContext(directory, className);
                DataUtils.unzipJar("plugins/" + className + "/", directory);
                dataContext.initialize();
                dataContext.reset();
                dataContext.initializeDataStore();
                dataContext.startTransaction();
                ServerUtils.initialize(dataContext);
                dataContext.endTransaction(true);
                String responseStr = "changed persistence type. Using " + className;
                System.out.println(responseStr);
                setDataContext(dataContext);
                respondWithString(exchange, responseStr, 200, _jsonStr);
            } catch (Exception e) {
                handleServerException(exchange, e);
                dataContext.endTransaction(false);
            }
            exchange.close();
        }
    };
    //endregion

    //endregion

    //region Server Methods
    private Server(int p, int s, ContextCreator.ContextType storageType, boolean wipe) {
        dataContext = ContextCreator.getDataContext(storageType);
        this._port = p;
        this.executesBetweenSaves = s;
        _gson = new Gson();
        if(wipe){
            resetDataContext();
            System.out.println("Reset Server");
        }
        dataContext.startTransaction();
        ServerUtils.initialize(this.dataContext);
        dataContext.endTransaction(true);
    }

    public static ContextCreator.ContextType parseStorage(String type) {
        String t = type.toLowerCase();
        if(t.equals("file")) {
            return ContextCreator.ContextType.FILE;
        } else if (t.equals("sqlite")){
            return ContextCreator.ContextType.SQLITE;
        } else if (t.equals("mongo")){
            return ContextCreator.ContextType.MONGO;
        } else {
            System.out.println("Could not parse database type");
            return ContextCreator.ContextType.SQLITE;
        }
    }

    public static void main(String[] args) {
        Server server;
        ContextCreator.ContextType storageType = ContextCreator.ContextType.SQLITE;
        if (args.length < 1 || args[0].isEmpty())
            new Server(SERVER_PORT_NUMBER, OPERATIONS_TILL_SAVE, storageType, false).run();
        else {
            try {
                int port = Integer.parseInt(args[0]);
                int saveNum = Integer.parseInt(args[1]);
                storageType = parseStorage(args[3].toString());
                server = new Server(port, saveNum, storageType, Boolean.parseBoolean(args[2]));
                System.out.println("Running server on port: " + port + " as " + storageType);
                System.out.println(saveNum + " commands per saved game");
                server.run();
            } catch (NumberFormatException e) {
                System.out
                        .println("Could not parse command line argument as port number. "
                                + "Running server as default port: 8081 as " + storageType
                                + e.getMessage());
                server = new Server(SERVER_PORT_NUMBER, OPERATIONS_TILL_SAVE, storageType, false);
                if(Boolean.parseBoolean(args[2])){
                    server.resetDataContext();
                    System.out.println("Reset Server");
                }
                server.run();
            }
        }
    }

    private void resetDataContext() {
        dataContext.reset();
    }

    private void run() {

        try {
            this._server = HttpServer.create(new InetSocketAddress(this._port),
                    MAX_WAITING_CONNECTIONS);
        } catch (IOException e) {
            System.out.println("Could not create HTTP server: "
                    + e.getMessage());
            e.printStackTrace();
            return;
        }

        this._server.setExecutor(null); // use the default executor

        // create handlers
        this._server.createContext("/user/login", loginHandler);
        this._server.createContext("/user/register", registerHandler);
        this._server.createContext("/games/list", listGamesHandler);
        this._server.createContext("/games/create", createGameHandler);
        this._server.createContext("/games/join", joinGameHandler);
        this._server.createContext("/game/model", getModelHandler);
        this._server.createContext("/game/reset", resetGameHandler);
        this._server.createContext("/game/commands", getCommandsHandler); // get
        this._server.createContext("/game/commands", postCommandsHandler); // post
        this._server.createContext("/game/listAI", listAiHandler);
        this._server.createContext("/game/addAI", addAiHandler);
        this._server.createContext("/util/changeLogLevel", changeLogLevelHandler);
        this._server.createContext("/moves/sendChat", sendChatHandler);
        this._server.createContext("/moves/acceptTrade", acceptTradeHandler);
        this._server.createContext("/moves/discardCards", discardCardsHandler);
        this._server.createContext("/moves/rollNumber", rollNumberHandler);
        this._server.createContext("/moves/buildRoad", buildRoadHandler);
        this._server.createContext("/moves/buildSettlement", buildSettlementHandler);
        this._server.createContext("/moves/buildCity", buildCityHandler);
        this._server.createContext("/moves/offerTrade", offerTradeHandler);
        this._server.createContext("/moves/robPlayer", robPlayerHandler);
        this._server.createContext("/moves/maritimeTrade", maritimeTradeHandler);
        this._server.createContext("/moves/finishTurn", finishTurnHandler);
        this._server.createContext("/moves/buyDevCard", buyDevCardHandler);
        this._server.createContext("/moves/Year_of_Plent", yearOfPlentyHandler);
        this._server.createContext("/moves/Road_Building", roadBuildingHandler);
        this._server.createContext("/moves/Soldier", soldierHandler);
        this._server.createContext("/moves/Monopoly", monopolyHandler);
        this._server.createContext("/moves/Monument", monumentHandler);
        this._server.createContext("/docs/api/data", getSwaggerFilesHandler);
        this._server.createContext("/persistence", changePersistenceHandler);
        this._server.createContext("/", downloadFileHandler);

        this._server.start();
    }
    //endregion
}