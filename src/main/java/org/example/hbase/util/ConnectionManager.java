package org.example.hbase.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * HBase에 접속하고, 값을 가져오는 기능
 */
public class ConnectionManager {

    private static Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
    private Configuration config;
    private Connection connection;
    private Admin admin;

    /**
     * 생성자. HBase에 접속하기 위한 설정을 가져온다.
     */
    public ConnectionManager() {
        config = HBaseConfiguration.create();
        String path = this.getClass().getClassLoader().getResource("hbase-site.xml").getPath();
        config.addResource(new Path(path));
    }

    /**
     * HBase에 접속하여 HBase Admin 객체를 반환한다.
     * @return HBase Admin 객체. HBase 정보를 가져올 수 있음
     * @throws IOException 접속 불가 등의 예외
     */
    public Admin connect() throws IOException {

        try {
            HBaseAdmin.available(config);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw e;
        }

        try {
            this.connection = ConnectionFactory.createConnection(config);
            this.admin = connection.getAdmin();
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }


        return this.admin;
    }

    /**
     * HBase에서 입력한 이름의 테이블이 존재하는지를 체크하여 Column Family 개수를 반환한다.
     * @param tableName HBase의 테이블명
     * @return 찾아낸 테이블에 해당하는 Column Family 개수 (예: 1)
     */
    public int getTableDescription(String tableName) {
        TableDescriptor tableDescriptor = null;
        try {
            Admin admin = connect();
            tableDescriptor = admin.getDescriptor(TableName.valueOf(tableName));
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return tableDescriptor.getColumnFamilyCount();
    }

    /**
     * HBase에 접속하여 입력한 조건에 맞는 값을 반환한다.
     * @param tableName HBase 테이블명 (예: test01)
     * @param rowkey HBase에서 검색할 Row Key (예: row1)
     * @param columnFamily HBase에서 검색된 Row에서 값을 찾을 Column Family 이름 (예: test_cf, 테이블 생성 시 정함)
     * @param columnName HBase에서 검색된 Row에서 Column Family 하위의 Column 이름 (예: col1)
     * @return 조건에 맞는 값
     * @throws IOException HBase 접속 불가 등의 예외
     */
    public String getValue(String tableName, String rowkey, String columnFamily, String columnName) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Get getQuery = new Get(Bytes.toBytes(rowkey));
        Result getResult = table.get(getQuery);
        return Bytes.toString(getResult.getValue(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName)));
    }


}
