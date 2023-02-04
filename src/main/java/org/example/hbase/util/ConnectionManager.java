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

public class ConnectionManager {

    private static Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
    private Configuration config;
    private Connection connection;
    private Admin admin;

    public ConnectionManager() {
        config = HBaseConfiguration.create();
        String path = this.getClass().getClassLoader().getResource("hbase-site.xml").getPath();
        config.addResource(new Path(path));
    }

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

    public String getValue(String tableName, String rowkey, String columnFamily, String columnName) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Get getQuery = new Get(Bytes.toBytes(rowkey));
        Result getResult = table.get(getQuery);
        return Bytes.toString(getResult.getValue(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName)));
    }


}
