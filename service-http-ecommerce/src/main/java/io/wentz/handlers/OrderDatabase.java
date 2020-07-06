package io.wentz.handlers;

import io.wentz.LocalDatabase;
import io.wentz.models.Order;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;

public class OrderDatabase implements Closeable {
    private final LocalDatabase db;

    public OrderDatabase(String name) throws SQLException {
        this.db = new LocalDatabase(name);

        // Maybe you want to save all the data
        boolean created = this.db.createIfNotExists("create table Orders (" +
                "uuid varchar(200) primary key)");
        System.out.println("DB created? " + created);
    }

    public boolean saveOrder(Order order) throws SQLException {
        if (wasProcessed(order)) {
            return false;
        }
        db.update("insert into Orders (uuid) values (?)", order.getOrderId());
        return true;
    }

    private boolean wasProcessed(Order order) throws SQLException {
        var result = db.query("select uuid from Orders where uuid = ? limit 1", order.getOrderId());
        return result.next();
    }

    @Override
    public void close() throws IOException {
        try {
            this.db.close();
        } catch (SQLException throwables) {
            throw new IOException(throwables);
        }
    }
}
