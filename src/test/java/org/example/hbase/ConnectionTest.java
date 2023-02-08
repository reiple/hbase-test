package org.example.hbase;

import org.apache.hadoop.hbase.client.Admin;
import org.example.hbase.util.ConnectionManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ConnectionTest {

    @DisplayName("HBase 접속 테스트")
    @Test
    public void normalConnectionTest() {

        ConnectionManager connectionManager = new ConnectionManager();

        Assertions.assertDoesNotThrow(() -> {
            Admin admin = connectionManager.connect();
            Assertions.assertNotNull(admin);
        });
    }

    @DisplayName("HBase에서 테스트 대상 테이블의 Column Family 개수 체크 테스트")
    @Test
    public void checkColumnFamilyTest() {
        ConnectionManager connectionManager = new ConnectionManager();
        int count = connectionManager.getTableDescription("test01");

        Assertions.assertEquals(1, count);
    }

    @DisplayName("HBase에서 테스트 대상 테이블의 값을 가져오는 테스트")
    @Test
    public void getValueNormalTest() throws IOException {
        ConnectionManager connectionManager = new ConnectionManager();
        connectionManager.connect();

        String result = connectionManager.getValue("test01", "row1", "test_cf", "col01");
        Assertions.assertEquals("value1", result);
    }
}
