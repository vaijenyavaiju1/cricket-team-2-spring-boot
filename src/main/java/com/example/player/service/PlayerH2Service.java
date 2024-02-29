package com.example.player.service;

import com.example.player.model.PlayerRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import com.example.player.model.Player;
import com.example.player.repository.PlayerRepository;
import java.util.*;

// Write your code here

@Service
public class PlayerH2Service implements PlayerRepository {
    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Player> getPlayers() {
        List<Player> playerList = db.query("select * from team", new PlayerRowMapper());
        ArrayList<Player> players = new ArrayList<>(playerList);
        return players;
    }

    @Override
    public Player getPlayerById(int playerId) {
        try {
            Player player = db.queryForObject("select * from team where playerId = ?", new PlayerRowMapper(),
                    playerId);
            return player;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Player addPlayer(Player player) {
        db.update("insert into team(playerName, jerseyNumber, role) values(?, ?, ?)", player.getPlayerName(),
            player.getJerseyNumber(), player.getRole());
        Player savedPlayer = db.queryForObject("select * from team where playerName=? and jerseyNumber=?",
                new PlayerRowMapper(), player.getPlayerName(), player.getJerseyNumber());
        return savedPlayer;
    }

    @Override
    public Player updatePlayer(int playerId, Player player) {
        if (player.getPlayerName() != null) {
            db.update("update team Set playerName=? where playerId=?", player.getPlayerName(), playerId);
        }
        if (player.getJerseyNumber() != 0) {
            db.update("update team Set jerseyNumber=? where playerId=?", player.getJerseyNumber(), playerId);
        }
        if (player.getRole() != null) {
            db.update("update team Set role=? where playerId=?", player.getRole(), playerId);
        }
        return getPlayerById(playerId);
    }

    @Override
    public void deletePlayer(int playerId) {
        db.update("delete from team where playerId=?", playerId);
    }

}