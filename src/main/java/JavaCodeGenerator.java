
import dependency.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class JavaCodeGenerator {

    private static final String ip = "192.168.1.16";
    private static final String port = "3306";
    private static final String schema = "tz_tplan";
    private static final String userName = "tbj";
    private static final String password = "tbj900900";

    public static void main(String[] args) {
        String[] tables = new String[]{
                "advance_transfer_plan"
        };
        for (String tableName : tables) {
            buildZipFile(tableName);
        }
    }

    private static void buildZipFile(String table) {
        try {
            String beanName = StringUtils.getJavaName(table);
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/"
                    + schema + "?useUnicode=true&characterEncoding=utf-8", userName, password);
            TableConfig tableConfig = getTable(connection, table);
            connection.close();

            tableConfig.setBeanName(beanName);
            tableConfig.setInjectName(StringUtils.getFistLowName(beanName));

            File file = new File("/Users/zhangwei/Downloads/" + beanName + ".zip");
            if (file.exists() && !file.delete()) {
                throw new RuntimeException("delete fail");
            }
            if (!file.createNewFile()) {
                throw new RuntimeException("create fail");
            }
            FileOutputStream fis = new FileOutputStream(file);
            CheckedOutputStream cos = new CheckedOutputStream(fis, new CRC32());
            ZipOutputStream out = new ZipOutputStream(cos);

            printMapper(tableConfig, out);

            out.close();
            System.out.println(beanName + ">>>>>>SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printMapper(TableConfig table, ZipOutputStream out) throws IOException {

        String buffer = TemplateBuilder.build(table, TemplateBuilder.MAPPER);
        printFile(buffer, table.getTableName() + ".xml", out);

        buffer = TemplateBuilder.build(table, TemplateBuilder.model);
        printFile(buffer,  table.getBeanName() + ".java", out);

        buffer = TemplateBuilder.build(table, TemplateBuilder.Service);
        printFile(buffer,  table.getBeanName() + "Service.java", out);

        buffer = TemplateBuilder.build(table, TemplateBuilder.ServiceImpl);
        printFile(buffer,  table.getBeanName() + "ServiceImpl.java", out);

        buffer = TemplateBuilder.build(table, TemplateBuilder.DAO);
        printFile(buffer,  table.getBeanName() + "DAO.java", out);

        buffer = TemplateBuilder.build(table, TemplateBuilder.DAOImpl);
        printFile(buffer,  table.getBeanName() + "DAOImpl.java", out);

        buffer = TemplateBuilder.build(table, TemplateBuilder.DOConverter);
        printFile(buffer,  table.getBeanName() + "DOConverter.java", out);

        buffer = TemplateBuilder.build(table, TemplateBuilder.Query);
        printFile(buffer,  table.getBeanName() + "Query.java", out);

        buffer = TemplateBuilder.build(table, TemplateBuilder.DO);
        printFile(buffer,  table.getBeanName() + "DO.java", out);

        buffer = TemplateBuilder.build(table, TemplateBuilder.dtoModel);
        printFile(buffer,  table.getBeanName() + "DTO.java", out);

        buffer = TemplateBuilder.build(table, TemplateBuilder.DTOConverter);
        printFile(buffer,  table.getBeanName() + "DTOConverter.java", out);

        buffer = TemplateBuilder.build(table, TemplateBuilder.ManageFacade);
        printFile(buffer,  table.getBeanName() + "ManageFacade.java", out);

        buffer = TemplateBuilder.build(table, TemplateBuilder.ManageFacadeImpl);
        printFile(buffer,  table.getBeanName() + "ManageFacadeImpl.java", out);

        buffer = TemplateBuilder.build(table, TemplateBuilder.QueryFacade);
        printFile(buffer,  table.getBeanName() + "QueryFacade.java", out);

        buffer = TemplateBuilder.build(table, TemplateBuilder.QueryFacadeImpl);
        printFile(buffer,  table.getBeanName() + "QueryFacadeImpl.java", out);

        out.flush();
    }

    private static void printFile(String buffer, String name, ZipOutputStream out) throws IOException {
        ZipEntry entry = new ZipEntry(name);
        out.putNextEntry(entry);
        out.write(buffer.getBytes());
    }


    private static TableConfig getTable(Connection conn, String table) throws SQLException, UnsupportedEncodingException {
        TableConfig tableConfig = new TableConfig();
        DatabaseMetaData dbMetData = conn.getMetaData();
        List<ColumnConfig> columns = getColumns(dbMetData, table);
        String beanTypeName = StringUtils.getJavaName(table);
        tableConfig.setPackageName(beanTypeName);
        tableConfig.setBeanName(beanTypeName);
        tableConfig.setInjectName(StringUtils.getFistLowName(beanTypeName));
        tableConfig.setTableName(table);
        tableConfig.setNamespace(beanTypeName);
        tableConfig.setColumns(columns);
        if (columns != null) {
            for (ColumnConfig columnConfig : columns) {
                if (columnConfig.isPrimaryKey()) {
                    tableConfig.setPrimaryKey(columnConfig);
                    break;
                }
            }
        }
        return tableConfig;
    }

    private static List<ColumnConfig> getColumns(DatabaseMetaData metaData, String table) throws SQLException, UnsupportedEncodingException {
        List<ColumnConfig> columns = new LinkedList<ColumnConfig>();
        // 根据表名提前表里面信息：
        ResultSet primaryKeySet = metaData.getPrimaryKeys(null, null, table);
        String primaryKey = null;
        while (primaryKeySet.next()) {
            primaryKey = primaryKeySet.getString("COLUMN_NAME");
        }
        ResultSet colRet = metaData.getColumns(null, "%", table, "%");
        while (colRet.next()) {
            String columnName = colRet.getString("COLUMN_NAME");
            Integer columnType = colRet.getInt("DATA_TYPE");
            String typeName = colRet.getString("TYPE_NAME");
            String comment = colRet.getString("REMARKS");
            comment = new String(comment.getBytes(), "utf-8");
            boolean isPrimaryKey = false;
            if (columnName.equals(primaryKey)) {
                isPrimaryKey = true;
            }
            try {
                ColumnConfig columnConfig = buildConlumnConfig(columnName, columnType, isPrimaryKey);
                columnConfig.setComment(comment);
                columns.add(columnConfig);
            } catch (Exception e) {
                throw new RuntimeException("ConlumnConfig构建失败:" + table + " " + columnName + " " + columnType + "" + typeName);
            }
        }
        return columns;
    }

    private static ColumnConfig buildConlumnConfig(String columnName, Integer columnType, boolean isPrimaryKey) {
        ColumnConfig column = new ColumnConfig(isPrimaryKey, columnName, StringUtils.getFistLowName(StringUtils.getJavaName(columnName)));
        JdbcTypeService.JdbcType type = JdbcTypeService.getInstances().getJavaType(columnType);
        column.setColumnType(type.getJdbcType());
        column.setJavaType(type.getJavaType());
        column.setSimpleJavaType(StringUtils.getSimpleName(type.getJavaType()));
        return column;
    }
}
