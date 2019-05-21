package fr.upem.foraxproof.rest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

class StatementBuilder {
    private final String rule;
    private final String context;
    private final String level;
    private final int limit;
    private final ArrayList<String> args;
    private String sql = "SELECT * FROM records";
    private int index;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    StatementBuilder(Optional<String> rule, Optional<String> context, Optional<String> level, Optional<Integer> limit) {
        this.rule = rule.orElse(null);
        this.context = context.orElse(null);
        this.level = level.orElse(null);
        this.limit = limit.orElse(-1);
        this.args = new ArrayList<>();
    }

    PreparedStatement build(Connection connection) throws SQLException {
        handle(rule, " rule == ?");
        handle(context, " context == ?");
        handle(level, " level == ?");
        int indexLimit = handleLimit();
        return buildStatement(connection, indexLimit);
    }

    private PreparedStatement buildStatement(Connection connection, int indexLimit) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 0; i < index; i++) {
            statement.setString(i + 1, args.get(i));
        }
        if (limit > -1) {
            statement.setInt(indexLimit, limit);
        }
        return statement;
    }

    private void handle(String element, String statement) {
        if (element != null) {
            String w = index < 1 ? "WHERE" : "AND";
            args.add(element);
            index++;
            sql += " " + w + statement;
        }
    }

    private int handleLimit() {
        if (limit > -1) {
            sql += " LIMIT ?";
            return index + 1;
        }
        return -1;
    }
}
