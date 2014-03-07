/**
 * 
 */
package com.catan;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * @author reed.wilson
 * 
 */
public class Server {

	private static final int SERVER_PORT_NUMBER = 8081;
	private static final int MAX_WAITING_CONNECTIONS = 10;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length < 1 || args[0].isEmpty())
			new Server(SERVER_PORT_NUMBER).run();
		else {
			try {
				int port = Integer.parseInt(args[0]);
				Server server = new Server(port);
				System.out.println("Running server on port: " + port);
				server.run();
			} catch (NumberFormatException e) {
				System.out
						.println("Could not parse command line argument as port number. "
								+ "Running server as default port: 8081 "
								+ e.getMessage());
				new Server(SERVER_PORT_NUMBER).run();
			}
		}
	}

	private int _port;
	private HttpServer _server;

	private Server(int p) {
		this._port = p;
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
		this._server.createContext("/game/listai", listAiHandler);
		this._server.createContext("/game/addai", addAiHandler);
		this._server.createContext("/util/changeloglevel", changeLogLevelHandler);
		this._server.createContext("/moves/sendchat", sendChatHandler);
		this._server.createContext("/moves/accepttrade", acceptTradeHandler);
		this._server.createContext("/moves/discardcards", discardCardsHandler);
		this._server.createContext("/moves/rollnumber", rollNumberHandler);
		this._server.createContext("/moves/buildroad", buildRoadHandler);
		this._server.createContext("/moves/buildsettlement", buildSettlementHandler);
		this._server.createContext("/moves/buildcity", buildCityHandler);
		this._server.createContext("/moves/offertrade", offerTradeHandler);
		this._server.createContext("/moves/maritimetrade", maritimeTradeHandler);
		this._server.createContext("/moves/finishturn", finishTurnHandler);
		this._server.createContext("/moves/buydevcard", buyDevCardHandler);
		this._server.createContext("/moves/year_of_plent", yearOfPlentyHandler);
		this._server.createContext("/moves/road_building", roadBuildingHandler);
		this._server.createContext("/moves/soldier", soldierHandler);
		this._server.createContext("/moves/monopoly", monopolyHandler);
		this._server.createContext("/moves/monument", monumentHandler);
		this._server.createContext("/", downloadFileHandler);

		this._server.start();
	}

	private final String model = "{\"deck\":{\"yearOfPlenty\":2,\"monopoly\":2,\"soldier\":14,\"roadBuilding\":2,\"monument\":5},\"map\":{\"hexGrid\":{\"hexes\":[[{\"isLand\":false,\"location\":{\"x\":0,\"y\":-3},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":false,\"location\":{\"x\":1,\"y\":-3},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":false,\"location\":{\"x\":2,\"y\":-3},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":false,\"location\":{\"x\":3,\"y\":-3},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]}],[{\"isLand\":false,\"location\":{\"x\":-1,\"y\":-2},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landtype\":\"Ore\",\"isLand\":true,\"location\":{\"x\":0,\"y\":-2},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landtype\":\"Wheat\",\"isLand\":true,\"location\":{\"x\":1,\"y\":-2},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landtype\":\"Sheep\",\"isLand\":true,\"location\":{\"x\":2,\"y\":-2},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":false,\"location\":{\"x\":3,\"y\":-2},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]}],[{\"isLand\":false,\"location\":{\"x\":-2,\"y\":-1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landtype\":\"Ore\",\"isLand\":true,\"location\":{\"x\":-1,\"y\":-1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landtype\":\"Wood\",\"isLand\":true,\"location\":{\"x\":0,\"y\":-1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landtype\":\"Wood\",\"isLand\":true,\"location\":{\"x\":1,\"y\":-1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landtype\":\"Sheep\",\"isLand\":true,\"location\":{\"x\":2,\"y\":-1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":false,\"location\":{\"x\":3,\"y\":-1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]}],[{\"isLand\":false,\"location\":{\"x\":-3,\"y\":0},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landtype\":\"Wheat\",\"isLand\":true,\"location\":{\"x\":-2,\"y\":0},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landtype\":\"Sheep\",\"isLand\":true,\"location\":{\"x\":-1,\"y\":0},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landtype\":\"Wood\",\"isLand\":true,\"location\":{\"x\":0,\"y\":0},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landtype\":\"Brick\",\"isLand\":true,\"location\":{\"x\":1,\"y\":0},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":true,\"location\":{\"x\":2,\"y\":0},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":false,\"location\":{\"x\":3,\"y\":0},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]}],[{\"isLand\":false,\"location\":{\"x\":-3,\"y\":1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landtype\":\"Wood\",\"isLand\":true,\"location\":{\"x\":-2,\"y\":1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landtype\":\"Sheep\",\"isLand\":true,\"location\":{\"x\":-1,\"y\":1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landtype\":\"Ore\",\"isLand\":true,\"location\":{\"x\":0,\"y\":1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landtype\":\"Wheat\",\"isLand\":true,\"location\":{\"x\":1,\"y\":1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":false,\"location\":{\"x\":2,\"y\":1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]}],[{\"isLand\":false,\"location\":{\"x\":-3,\"y\":2},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landtype\":\"Brick\",\"isLand\":true,\"location\":{\"x\":-2,\"y\":2},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landtype\":\"Wheat\",\"isLand\":true,\"location\":{\"x\":-1,\"y\":2},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landtype\":\"Brick\",\"isLand\":true,\"location\":{\"x\":0,\"y\":2},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":false,\"location\":{\"x\":1,\"y\":2},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]}],[{\"isLand\":false,\"location\":{\"x\":-3,\"y\":3},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":false,\"location\":{\"x\":-2,\"y\":3},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":false,\"location\":{\"x\":-1,\"y\":3},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":false,\"location\":{\"x\":0,\"y\":3},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]}]],\"offsets\":[3,2,1,0,0,0,0],\"radius\":4,\"x0\":3,\"y0\":3},\"radius\":4,\"numbers\":{\"2\":[{\"x\":1,\"y\":-2}],\"3\":[{\"x\":0,\"y\":-2},{\"x\":0,\"y\":-1}],\"4\":[{\"x\":0,\"y\":2},{\"x\":-2,\"y\":1}],\"5\":[{\"x\":-2,\"y\":2},{\"x\":-1,\"y\":-1}],\"6\":[{\"x\":-1,\"y\":2},{\"x\":0,\"y\":0}],\"8\":[{\"x\":1,\"y\":0},{\"x\":-2,\"y\":0}],\"9\":[{\"x\":2,\"y\":-2},{\"x\":0,\"y\":1}],\"10\":[{\"x\":2,\"y\":-1},{\"x\":-1,\"y\":1}],\"11\":[{\"x\":1,\"y\":-1},{\"x\":1,\"y\":1}],\"12\":[{\"x\":-1,\"y\":0}]},\"ports\":[{\"ratio\":2,\"inputResource\":\"Wood\",\"validVertex1\":{\"direction\":\"NE\",\"x\":-2,\"y\":3},\"validVertex2\":{\"direction\":\"E\",\"x\":-2,\"y\":3},\"orientation\":\"NE\",\"location\":{\"x\":-2,\"y\":3}},{\"ratio\":2,\"inputResource\":\"Wheat\",\"validVertex1\":{\"direction\":\"SE\",\"x\":1,\"y\":-3},\"validVertex2\":{\"direction\":\"SW\",\"x\":1,\"y\":-3},\"orientation\":\"S\",\"location\":{\"x\":1,\"y\":-3}},{\"ratio\":3,\"validVertex1\":{\"direction\":\"E\",\"x\":-3,\"y\":0},\"validVertex2\":{\"direction\":\"SE\",\"x\":-3,\"y\":0},\"orientation\":\"SE\",\"location\":{\"x\":-3,\"y\":0}},{\"ratio\":2,\"inputResource\":\"Ore\",\"validVertex1\":{\"direction\":\"W\",\"x\":3,\"y\":-1},\"validVertex2\":{\"direction\":\"NW\",\"x\":3,\"y\":-1},\"orientation\":\"NW\",\"location\":{\"x\":3,\"y\":-1}},{\"ratio\":3,\"validVertex1\":{\"direction\":\"NW\",\"x\":0,\"y\":3},\"validVertex2\":{\"direction\":\"NE\",\"x\":0,\"y\":3},\"orientation\":\"N\",\"location\":{\"x\":0,\"y\":3}},{\"ratio\":3,\"validVertex1\":{\"direction\":\"W\",\"x\":2,\"y\":1},\"validVertex2\":{\"direction\":\"NW\",\"x\":2,\"y\":1},\"orientation\":\"NW\",\"location\":{\"x\":2,\"y\":1}},{\"ratio\":2,\"inputResource\":\"Brick\",\"validVertex1\":{\"direction\":\"NE\",\"x\":-3,\"y\":2},\"validVertex2\":{\"direction\":\"E\",\"x\":-3,\"y\":2},\"orientation\":\"NE\",\"location\":{\"x\":-3,\"y\":2}},{\"ratio\":3,\"validVertex1\":{\"direction\":\"SE\",\"x\":-1,\"y\":-2},\"validVertex2\":{\"direction\":\"SW\",\"x\":-1,\"y\":-2},\"orientation\":\"S\",\"location\":{\"x\":-1,\"y\":-2}},{\"ratio\":2,\"inputResource\":\"Sheep\",\"validVertex1\":{\"direction\":\"SW\",\"x\":3,\"y\":-3},\"validVertex2\":{\"direction\":\"W\",\"x\":3,\"y\":-3},\"orientation\":\"SW\",\"location\":{\"x\":3,\"y\":-3}}],\"robber\":{\"x\":2,\"y\":0}},\"players\":[{\"MAX_GAME_POINTS\":10,\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":0},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"longestRoad\":false,\"largestArmy\":false,\"playedDevCard\":false,\"discarded\":false,\"playerID\":12,\"orderNumber\":0,\"name\":\"Reed\",\"color\":\"red\"},null,null,null],\"log\":{\"lines\":[]},\"chat\":{\"lines\":[]},\"bank\":{\"brick\":24,\"wood\":24,\"sheep\":24,\"wheat\":24,\"ore\":24},\"turnTracker\":{\"status\":\"FirstRound\",\"currentTurn\":0},\"biggestArmy\":2,\"longestRoad\":-1,\"winner\":-1,\"revision\":0}";
	private final String gamesList = "[{\"title\":\"Default Game\",\"id\":0,\"players\":[{\"color\":\"orange\",\"name\":\"Sam\",\"id\":0},{\"color\":\"blue\",\"name\":\"Brooke\",\"id\":1},{\"color\":\"red\",\"name\":\"Pete\",\"id\":10},{\"color\":\"green\",\"name\":\"Mark\",\"id\":11}]},{\"title\":\"AI Game\",\"id\":1,\"players\":[{\"color\":\"orange\",\"name\":\"Pete\",\"id\":10},{\"color\":\"green\",\"name\":\"Squall\",\"id\":-2},{\"color\":\"red\",\"name\":\"Steve\",\"id\":-2},{\"color\":\"white\",\"name\":\"Ken\",\"id\":-2}]},{\"title\":\"Empty Game\",\"id\":2,\"players\":[{\"color\":\"orange\",\"name\":\"Sam\",\"id\":0},{\"color\":\"blue\",\"name\":\"Brooke\",\"id\":1},{\"color\":\"red\",\"name\":\"Pete\",\"id\":10},{\"color\":\"green\",\"name\":\"Mark\",\"id\":11}]}]";
	
	private HttpHandler loginHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			// XStream xStream = new XStream(new DomDriver());
			// InputStreamReader stream = new InputStreamReader(exchange.getRequestBody());
			// ValidateUser_Params params = (ValidateUser_Params)xStream.fromXML(stream);

			try {
				// get user specified by username and password
				// User user = dataContext.getUserRepository().getSpecific(params.getUsername(),
				// params.getPassword());

				// serialize object, get the bytes and write them out
				// Gson gson = new GsonBuilder().create();
				// User user = gson.fromJson(json, User.class);

				// byte[] bytes = xStream.toXML(new
				// ValidateUser_Result(user)).getBytes();
				byte[] bytes = "Success".getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler registerHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			try {
				byte[] bytes = "Success".getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler listGamesHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = gamesList.getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler createGameHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = "{\"title\":\"gobble\",\"id\":3,\"players\":[{},{},{},{}]}".getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler joinGameHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = "Success! You have joined the game.".getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler getModelHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = model.getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler resetGameHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = model.getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler getCommandsHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = model.getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler postCommandsHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = "Success".getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler listAiHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = "[ \"LARGEST_ARMY\" ]".getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler addAiHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = "Success".getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler changeLogLevelHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = "Not an allowed log level.".getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler sendChatHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = model.getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler acceptTradeHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = model.getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler discardCardsHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = model.getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler rollNumberHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = model.getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler buildRoadHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = model.getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler buildSettlementHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = model.getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler buildCityHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = model.getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler offerTradeHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = model.getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler maritimeTradeHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = model.getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler finishTurnHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = model.getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler buyDevCardHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = model.getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler yearOfPlentyHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = model.getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler roadBuildingHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = model.getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler soldierHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = model.getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler monopolyHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = model.getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	private HttpHandler monumentHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			try {
				byte[] bytes = model.getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
						bytes.length);
				exchange.getResponseBody().write(bytes);

			} catch (Exception e) {

				// send server exception response header
				System.out.println("ERROR 500. Internal Server Error"
						+ e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}
			exchange.close();
		}
	};
	
	private HttpHandler downloadFileHandler = new HttpHandler() {

		@SuppressWarnings("resource")
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			
			File file = null;
			URI uri = exchange.getRequestURI();
			String path = uri.getPath().substring(1);
			
			if (path.isEmpty()) { 			 	 // default to index.html
				file = new File("./gameplay/index.html");
			} else if (path.startsWith("docs")){ // go to docs dir
				file = new File(path);
			}else { 							 // go to gameplay
				file = new File("./gameplay/" + exchange.getRequestURI().getPath());
			}
			
			InputStream stream = null;
			try {
				if (file.exists() && file.isFile()) {
					// read in file and send it
					byte[] bytes = new byte[(int)file.length()];
					stream = new BufferedInputStream(new FileInputStream(file));
					stream.read(bytes, 0, bytes.length);
					
					exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, bytes.length);	
					exchange.getResponseBody().write(bytes, 0, bytes.length);
				} else if (file.isDirectory()) {
					
					// this is somewhat of a hack (and it still doesn't work for swagger)
					File[] files = file.listFiles();
					ArrayList<StreamBytes> streamBytes = new ArrayList<>();
					int length = 0;
					try {
						for (File f : files) {
							byte[] b = new byte[(int)f.length()];
							length += b.length;
							streamBytes.add(new StreamBytes(new BufferedInputStream(new FileInputStream(f)), b));
						}
					
						// send response headers including the length to expect
						exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, length);
					
						for (StreamBytes s : streamBytes) {
							s.get_stream().read(s.getBytes(), 0, s.getBytes().length);
							exchange.getResponseBody().write(s.getBytes(), 0, s.getBytes().length);
						}	
					} catch (Exception e) {
						System.out.println("ERROR 500. Internal Server Error" + e.getMessage());
						e.printStackTrace();
						exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
					} finally {
						for (StreamBytes s : streamBytes) {
							s.get_stream().close();
						}
					}
				}else {
					exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, -1);
				}
			} catch (Exception e) {
				System.out.println("ERROR 500. Internal Server Error" + e.getMessage());
				e.printStackTrace();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			} finally {
				exchange.close();
				if (stream!= null)
					stream.close();
			}
		}
		class StreamBytes {
			private InputStream _stream;
			private byte[] bytes;
			
			public InputStream get_stream() {
				return _stream;
			}
			@SuppressWarnings("unused")
			public void set_stream(InputStream _stream) {
				this._stream = _stream;
			}
			public byte[] getBytes() {
				return bytes;
			}
			@SuppressWarnings("unused")
			public void setBytes(byte[] bytes) {
				this.bytes = bytes;
			}
			
			public StreamBytes(InputStream stream, byte[] bytes) {
				this.bytes = bytes;
				this._stream = stream;
			}
		}

	};
}