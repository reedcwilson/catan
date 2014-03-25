/**
 *
 */
package com.catan.main.server;

import com.catan.main.datamodel.DataModel;

import com.catan.main.datamodel.User;

import com.catan.main.datamodel.commands.*;
import com.catan.main.datamodel.game.CreateGameRequest;
import com.catan.main.datamodel.game.Game;
import com.catan.main.datamodel.player.Color;
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

    private static final int SERVER_PORT_NUMBER = 8081;
    private static final int MAX_WAITING_CONNECTIONS = 10;
    private final String _serverErrorStr = "ERROR 500. Internal Server Error: ";
    private final String _successStr = "Success!";
    private final String _clientErrorStr = "ERROR 400. Client Error: ";
    private final String _textStr = "text/html";
    private final String _jsonStr = "application/json";
    private int _port;
    private HttpServer _server;

    private final String model = "{\"deck\":{\"yearOfPlenty\":2,\"monopoly\":2,\"soldier\":14,\"roadBuilding\":2,\"monument\":5},\"map\":{\"hexGrid\":{\"hexes\":[[{\"isLand\":false,\"location\":{\"x\":0,\"y\":-3},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":false,\"location\":{\"x\":1,\"y\":-3},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":false,\"location\":{\"x\":2,\"y\":-3},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":false,\"location\":{\"x\":3,\"y\":-3},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]}],[{\"isLand\":false,\"location\":{\"x\":-1,\"y\":-2},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landType\":\"Ore\",\"isLand\":true,\"location\":{\"x\":0,\"y\":-2},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landType\":\"Wheat\",\"isLand\":true,\"location\":{\"x\":1,\"y\":-2},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landType\":\"Sheep\",\"isLand\":true,\"location\":{\"x\":2,\"y\":-2},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":false,\"location\":{\"x\":3,\"y\":-2},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]}],[{\"isLand\":false,\"location\":{\"x\":-2,\"y\":-1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landType\":\"Ore\",\"isLand\":true,\"location\":{\"x\":-1,\"y\":-1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landType\":\"Wood\",\"isLand\":true,\"location\":{\"x\":0,\"y\":-1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landType\":\"Wood\",\"isLand\":true,\"location\":{\"x\":1,\"y\":-1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landType\":\"Sheep\",\"isLand\":true,\"location\":{\"x\":2,\"y\":-1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":false,\"location\":{\"x\":3,\"y\":-1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]}],[{\"isLand\":false,\"location\":{\"x\":-3,\"y\":0},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landType\":\"Wheat\",\"isLand\":true,\"location\":{\"x\":-2,\"y\":0},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landType\":\"Sheep\",\"isLand\":true,\"location\":{\"x\":-1,\"y\":0},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landType\":\"Wood\",\"isLand\":true,\"location\":{\"x\":0,\"y\":0},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landType\":\"Brick\",\"isLand\":true,\"location\":{\"x\":1,\"y\":0},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":true,\"location\":{\"x\":2,\"y\":0},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":false,\"location\":{\"x\":3,\"y\":0},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]}],[{\"isLand\":false,\"location\":{\"x\":-3,\"y\":1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landType\":\"Wood\",\"isLand\":true,\"location\":{\"x\":-2,\"y\":1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landType\":\"Sheep\",\"isLand\":true,\"location\":{\"x\":-1,\"y\":1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landType\":\"Ore\",\"isLand\":true,\"location\":{\"x\":0,\"y\":1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landType\":\"Wheat\",\"isLand\":true,\"location\":{\"x\":1,\"y\":1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":false,\"location\":{\"x\":2,\"y\":1},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]}],[{\"isLand\":false,\"location\":{\"x\":-3,\"y\":2},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landType\":\"Brick\",\"isLand\":true,\"location\":{\"x\":-2,\"y\":2},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landType\":\"Wheat\",\"isLand\":true,\"location\":{\"x\":-1,\"y\":2},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"landType\":\"Brick\",\"isLand\":true,\"location\":{\"x\":0,\"y\":2},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":false,\"location\":{\"x\":1,\"y\":2},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]}],[{\"isLand\":false,\"location\":{\"x\":-3,\"y\":3},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":false,\"location\":{\"x\":-2,\"y\":3},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":false,\"location\":{\"x\":-1,\"y\":3},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]},{\"isLand\":false,\"location\":{\"x\":0,\"y\":3},\"vertexes\":[{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}},{\"value\":{\"worth\":0,\"ownerID\":-1}}],\"edges\":[{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}},{\"value\":{\"ownerID\":-1}}]}]],\"offsets\":[3,2,1,0,0,0,0],\"radius\":4,\"x0\":3,\"y0\":3},\"radius\":4,\"numbers\":{\"2\":[{\"x\":1,\"y\":-2}],\"3\":[{\"x\":0,\"y\":-2},{\"x\":0,\"y\":-1}],\"4\":[{\"x\":0,\"y\":2},{\"x\":-2,\"y\":1}],\"5\":[{\"x\":-2,\"y\":2},{\"x\":-1,\"y\":-1}],\"6\":[{\"x\":-1,\"y\":2},{\"x\":0,\"y\":0}],\"8\":[{\"x\":1,\"y\":0},{\"x\":-2,\"y\":0}],\"9\":[{\"x\":2,\"y\":-2},{\"x\":0,\"y\":1}],\"10\":[{\"x\":2,\"y\":-1},{\"x\":-1,\"y\":1}],\"11\":[{\"x\":1,\"y\":-1},{\"x\":1,\"y\":1}],\"12\":[{\"x\":-1,\"y\":0}]},\"ports\":[{\"ratio\":2,\"inputResource\":\"Wood\",\"validVertex1\":{\"direction\":\"NE\",\"x\":-2,\"y\":3},\"validVertex2\":{\"direction\":\"E\",\"x\":-2,\"y\":3},\"orientation\":\"NE\",\"location\":{\"x\":-2,\"y\":3}},{\"ratio\":2,\"inputResource\":\"Wheat\",\"validVertex1\":{\"direction\":\"SE\",\"x\":1,\"y\":-3},\"validVertex2\":{\"direction\":\"SW\",\"x\":1,\"y\":-3},\"orientation\":\"S\",\"location\":{\"x\":1,\"y\":-3}},{\"ratio\":3,\"validVertex1\":{\"direction\":\"E\",\"x\":-3,\"y\":0},\"validVertex2\":{\"direction\":\"SE\",\"x\":-3,\"y\":0},\"orientation\":\"SE\",\"location\":{\"x\":-3,\"y\":0}},{\"ratio\":2,\"inputResource\":\"Ore\",\"validVertex1\":{\"direction\":\"W\",\"x\":3,\"y\":-1},\"validVertex2\":{\"direction\":\"NW\",\"x\":3,\"y\":-1},\"orientation\":\"NW\",\"location\":{\"x\":3,\"y\":-1}},{\"ratio\":3,\"validVertex1\":{\"direction\":\"NW\",\"x\":0,\"y\":3},\"validVertex2\":{\"direction\":\"NE\",\"x\":0,\"y\":3},\"orientation\":\"N\",\"location\":{\"x\":0,\"y\":3}},{\"ratio\":3,\"validVertex1\":{\"direction\":\"W\",\"x\":2,\"y\":1},\"validVertex2\":{\"direction\":\"NW\",\"x\":2,\"y\":1},\"orientation\":\"NW\",\"location\":{\"x\":2,\"y\":1}},{\"ratio\":2,\"inputResource\":\"Brick\",\"validVertex1\":{\"direction\":\"NE\",\"x\":-3,\"y\":2},\"validVertex2\":{\"direction\":\"E\",\"x\":-3,\"y\":2},\"orientation\":\"NE\",\"location\":{\"x\":-3,\"y\":2}},{\"ratio\":3,\"validVertex1\":{\"direction\":\"SE\",\"x\":-1,\"y\":-2},\"validVertex2\":{\"direction\":\"SW\",\"x\":-1,\"y\":-2},\"orientation\":\"S\",\"location\":{\"x\":-1,\"y\":-2}},{\"ratio\":2,\"inputResource\":\"Sheep\",\"validVertex1\":{\"direction\":\"SW\",\"x\":3,\"y\":-3},\"validVertex2\":{\"direction\":\"W\",\"x\":3,\"y\":-3},\"orientation\":\"SW\",\"location\":{\"x\":3,\"y\":-3}}],\"robber\":{\"x\":2,\"y\":0}},\"players\":[{\"MAX_GAME_POINTS\":10,\"resources\":{\"brick\":0,\"wood\":0,\"sheep\":0,\"wheat\":0,\"ore\":1},\"oldDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"newDevCards\":{\"yearOfPlenty\":0,\"monopoly\":0,\"soldier\":0,\"roadBuilding\":0,\"monument\":0},\"roads\":15,\"cities\":4,\"settlements\":5,\"soldiers\":0,\"victoryPoints\":0,\"monuments\":0,\"longestRoad\":false,\"largestArmy\":false,\"playedDevCard\":false,\"discarded\":false,\"playerID\":12,\"orderNumber\":0,\"name\":\"Reed\",\"color\":\"red\"},null,null,null],\"getLog\":{\"lines\":[]},\"chat\":{\"lines\":[]},\"bank\":{\"brick\":24,\"wood\":24,\"sheep\":24,\"wheat\":24,\"ore\":24},\"turnTracker\":{\"status\":\"FirstRound\",\"currentTurn\":0},\"biggestArmy\":2,\"longestRoad\":-1,\"winner\":-1,\"revision\":0}";
    private final String gamesList = "[{\"title\":\"Default Game\",\"id\":0,\"players\":[{\"color\":\"orange\",\"name\":\"Sam\",\"id\":0},{\"color\":\"blue\",\"name\":\"Brooke\",\"id\":1},{\"color\":\"red\",\"name\":\"Pete\",\"id\":10},{\"color\":\"green\",\"name\":\"Mark\",\"id\":11}]},{\"title\":\"AI Game\",\"id\":1,\"players\":[{\"color\":\"orange\",\"name\":\"Pete\",\"id\":10},{\"color\":\"green\",\"name\":\"Squall\",\"id\":-2},{\"color\":\"red\",\"name\":\"Steve\",\"id\":-2},{\"color\":\"white\",\"name\":\"Ken\",\"id\":-2}]},{\"title\":\"Empty Game\",\"id\":2,\"players\":[{\"color\":\"orange\",\"name\":\"Sam\",\"id\":0},{\"color\":\"blue\",\"name\":\"Brooke\",\"id\":1},{\"color\":\"red\",\"name\":\"Pete\",\"id\":10},{\"color\":\"green\",\"name\":\"Mark\",\"id\":11}]}]";
    private Gson _gson;

    private void respondWithString(HttpExchange exchange, String message, int code, String contentType) throws IOException {
        byte[] bytes = message.getBytes();
        exchange.getResponseHeaders().add("Content-Type", contentType);
        exchange.sendResponseHeaders(code, bytes.length);
        exchange.getResponseBody().write(bytes);
    }

    private void handleCommand(HttpExchange exchange, Class type) throws IOException {
        String json = ServerUtils.streamToString(exchange.getRequestBody());
        DataModel model = ServerUtils.getModel(exchange);
        Command command = (Command)_gson.fromJson(json, type);
        command.execute(model);
        String response = model.toJSON();
        respondWithString(exchange, response, 200, _jsonStr);
    }

    //region Handlers

	//region User

    //region Login
    private HttpHandler loginHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {

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
                    respondWithString(exchange, _clientErrorStr, 200, _textStr);
                }
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

    //region Register
    private HttpHandler registerHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
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
            } catch (Exception e) {
                // send server exception response header
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, 500, _textStr);
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
                String response = new Gson().toJson(ServerUtils.getGames());
                respondWithString(exchange, response, 200, _jsonStr);
            } catch (Exception e) {
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
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
                Map<String, String> values = ServerUtils.getValuesFromForm(exchange);

                CreateGameRequest request = new CreateGameRequest(
                        Boolean.parseBoolean(values.get("randomHexes")),
                        Boolean.parseBoolean(values.get("randomTiles")),
                        Boolean.parseBoolean(values.get("randomtile")),
                        values.get("name"));

                GsonGame game = new GsonGame(ServerUtils.createGame(request));
                String gameStr = new Gson().toJson(game);
                respondWithString(exchange, gameStr, 200, _jsonStr);
            } catch (Exception e) {
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
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
            } catch (Exception e) {
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
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

                Client client = ServerUtils.getClient(exchange);
                Game game = ServerUtils.getGame(client.getGameID());
                String versionString = ServerUtils.decodeUri(exchange.getRequestURI().getQuery()).get("revision");
                int version = versionString == null ? -1 : Integer.parseInt(versionString);
                if (game != null) {
                    String response;
                    if (game.getModel().getVersion() == version) {
                        response = "\"true\"";
                    } else {
                        response = ServerUtils.getGame(client.getGameID()).getModel().toJSON();
                    }
                    respondWithString(exchange, response, 200, _jsonStr);
                } else {
                    respondWithString(exchange, _clientErrorStr + "no game found", 400, _textStr);
                }
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

    //region Reset
    private HttpHandler resetGameHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {

                Client client = ServerUtils.getClient(exchange);
                Game game = ServerUtils.getGame(client.getGameID());
                if (!ServerUtils.resetGame(client) || game == null) {
                    respondWithString(exchange, _clientErrorStr, 400, _textStr);
                }
                String response = game.getModel().toJSON();
                exchange.getResponseHeaders().set("Content-Type", _jsonStr);
                respondWithString(exchange, response, 200, _jsonStr);
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

    //region Commands
    private HttpHandler getCommandsHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                Client client = ServerUtils.getClient(exchange);
                Game game = ServerUtils.getGame(client.getGameID());
                if (game == null) {
                    respondWithString(exchange, _clientErrorStr, 400, _textStr);
                } else {
                    String response = _gson.toJson(game.getHistory().getCommands());
                    respondWithString(exchange, response, 200, _jsonStr);
                }
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

    //region Commands - Post
    private HttpHandler postCommandsHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                // TODO: I am really unsure about this one
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

                // send server exception response header
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
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
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
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
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
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
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
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
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
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
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
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
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
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
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
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
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
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
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
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
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
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
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
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
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
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
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
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
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
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
                System.out.println(_serverErrorStr + e.getMessage());
                e.printStackTrace();
                respondWithString(exchange, _serverErrorStr, HttpURLConnection.HTTP_INTERNAL_ERROR, _textStr);
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
    //endregion

    //endregion

    private Server(int p) {
        this._port = p;
        _gson = new Gson();
    }

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
        this._server.createContext("/moves/maritimeTrade", maritimeTradeHandler);
        this._server.createContext("/moves/finishTurn", finishTurnHandler);
        this._server.createContext("/moves/buyDevCard", buyDevCardHandler);
        this._server.createContext("/moves/Year_of_Plent", yearOfPlentyHandler);
        this._server.createContext("/moves/Road_Building", roadBuildingHandler);
        this._server.createContext("/moves/Soldier", soldierHandler);
        this._server.createContext("/moves/Monopoly", monopolyHandler);
        this._server.createContext("/moves/Monument", monumentHandler);
        this._server.createContext("/docs/api/data", getSwaggerFilesHandler);
        this._server.createContext("/", downloadFileHandler);

        this._server.start();
    }
}