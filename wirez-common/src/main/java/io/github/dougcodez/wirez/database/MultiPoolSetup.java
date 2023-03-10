/**
 * MIT License
 *
 * Copyright (c) 2022-2023 Douglas (dougcodez)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.dougcodez.wirez.database;

import com.zaxxer.hikari.HikariDataSource;
import io.github.dougcodez.wirez.commands.user.CommandUser;
import io.github.dougcodez.wirez.database.api.DatabaseAuthentication;
import io.github.dougcodez.wirez.database.api.HikariClientSetup;
import io.github.dougcodez.wirez.database.api.SQLTypes;
import io.github.dougcodez.wirez.database.api.StatementAPI;
import io.github.dougcodez.wirez.files.types.lang.Lang;
import io.github.dougcodez.wirez.files.types.settings.WireZSettingsFile;
import lombok.Getter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class MultiPoolSetup implements HikariClientSetup {

    private static MultiPoolSetup instance = null;

    @Getter
    private final Map<String, Map<String, HikariDataSource>> playersCurrentDbs = Collections.synchronizedMap(new LinkedHashMap<>());

    public void init(CommandUser player, WireZSettingsFile settingsFile, DatabaseAuthentication authentication, int timeOut, int maxConnections) {

        String playerName = player.getNameOrThrow();
        if (!settingsFile.getConfigFile().getBoolean("database-module.enable")) {
            return;
        }

        List<String> whitelist = settingsFile.getConfigFile().getStringList("database-module.player-whitelist");
        if (!whitelist.contains(playerName)) {
            player.sendMessage(Lang.PREFIX.toConfigString() + Lang.CONNECT_NOT_WHITELISTED.toConfigString());
            return;
        }

        if (playersCurrentDbs.values().stream().map(Map::keySet).anyMatch(c -> c.contains(authentication.database()))) {
            player.sendMessage(Lang.PREFIX.toConfigString() + Lang.DATABASE_ALREADY_CONNECTED.toConfigString());

        } else {
            Map<String, HikariDataSource> playerDataSource = playersCurrentDbs.getOrDefault(playerName, Collections.synchronizedMap(new LinkedHashMap<>()));
            final String connectMessage = Lang.PREFIX.toConfigString() + Lang.CONNECTED_TO_DB_SUCCESSFULLY.toConfigString();
            switch (settingsFile.getConfigFile().getString("database-module.driver")) {

                case "MYSQL" -> {
                    playerDataSource.put(authentication.database(), new HikariDataSource(getDataProperties(SQLTypes.MYSQL, authentication, timeOut, maxConnections)));
                    playersCurrentDbs.put(playerName, playerDataSource);
                    player.sendMessage(connectMessage);
                }


                case "POSTGRES" -> {
                    playerDataSource.put(authentication.database(), new HikariDataSource(getDataProperties(SQLTypes.POSTGRES, authentication, timeOut, maxConnections)));
                    playersCurrentDbs.put(playerName, playerDataSource);
                    player.sendMessage(connectMessage);
                }

                default ->
                        throw new IllegalStateException("Unexpected value: " + settingsFile.getConfigFile().getString("database-module.driver"));
            }
        }

        StatementAPI.getStatementAPI().setType(this);
    }

    public boolean isNotEstablished(String name, String database) {
        return playersCurrentDbs.isEmpty() || getDataSource(name, database) == null || isClosed(name, database);
    }


    public HikariDataSource getDataSource(String player, String database) {
        return playersCurrentDbs.get(player).get(database);
    }

    public void close(String name, String db) {
        getDataSource(name, db).close();
        playersCurrentDbs.get(name).remove(db);
        //System.out.println("Disconnected");
    }

    public void closeForAll() {
        for (Collection<HikariDataSource> dataSources : playersCurrentDbs.values().stream().map(Map::values).toList()) {
            if (dataSources.isEmpty()) return;
            dataSources.forEach(HikariDataSource::close);
        }
    }

    public boolean isClosed(String player, String db) {
        return getDataSource(player, db).isClosed();
    }


    public Connection getConnections(String player, String database) {
        try {
            return playersCurrentDbs.get(player).get(database).getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    //Not needed
    @Override
    public Connection getConnection() {
        return null;
    }

    public static MultiPoolSetup grabInstance() {
        if (instance == null) {
            instance = new MultiPoolSetup();
        }

        return instance;
    }
}