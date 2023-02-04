package org.example.hbase;

import org.apache.hadoop.hbase.client.Admin;
import org.example.hbase.util.ConnectionManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ConnectionTest {

    @Test
    public void normalConnectionTest() {

        ConnectionManager connectionManager = new ConnectionManager();

        Assertions.assertDoesNotThrow(() -> {
            Admin admin = connectionManager.connect();
            Assertions.assertNotNull(admin);
        });
    }

    @Test
    public void checkColumnFamilyTest() {
        ConnectionManager connectionManager = new ConnectionManager();
        int count = connectionManager.getTableDescription("test01");

        Assertions.assertEquals(1, count);
    }

    @Test
    public void getValueNormalTest() throws IOException {
        ConnectionManager connectionManager = new ConnectionManager();
        connectionManager.connect();

        String result = connectionManager.getValue("test01", "row1", "test_cf", "col01");
        Assertions.assertEquals("value1", result);
    }
}
