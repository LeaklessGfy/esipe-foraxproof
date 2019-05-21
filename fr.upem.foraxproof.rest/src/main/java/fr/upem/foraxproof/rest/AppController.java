package fr.upem.foraxproof.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class AppController {
    @CrossOrigin(origins = "*")
    @RequestMapping(method= GET, value = "/api/records", produces = "application/json")
    List<HashMap<String, String>> records(
            @RequestParam(value = "rule") Optional<String> rule,
            @RequestParam(value = "context") Optional<String> context,
            @RequestParam(value = "level") Optional<String> level,
            @RequestParam(value = "limit") Optional<Integer> limit
    ) throws IOException {
        String url = "jdbc:sqlite:result.db";

        try (Connection connection = DriverManager.getConnection(url)) {
            PreparedStatement statement = new StatementBuilder(rule, context, level, limit).build(connection);
            try (ResultSet rs = statement.executeQuery()) {
                return parse(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<HashMap<String, String>> parse(ResultSet rs) throws SQLException {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        ResultSetMetaData md = rs.getMetaData();
        populateMap(rs, md, list);
        return list;
    }

    private void populateMap(ResultSet rs, ResultSetMetaData md, ArrayList<HashMap<String, String>> list) throws SQLException {
        while (rs.next()) {
            HashMap<String, String> row = new HashMap<>(md.getColumnCount());
            for (int i = 1; i <= md.getColumnCount(); i++) {
                row.put(md.getColumnName(i), rs.getString(i));
            }
            list.add(row);
        }
    }
}
