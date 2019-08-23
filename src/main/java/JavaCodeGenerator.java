
import dependency.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    private static final String schema = "tz_transfer";
    private static final String userName = "sbj";
    private static final String password = "sbj900900";
    private static final BuildConfig config
            = new BuildConfig()
            .needDO(false)
            .needDTO(false)
            .needFacade(true);

    public static void main(String[] args) {
        String[] tables = new String[]{
                "transfer_apply",
        };
        for (String tableName : tables) {
            buildZipFile(tableName);
        }
    }

    private static void buildZipFile(String table) {
        try {
            String beanName = StringUtils.getJavaName(table);
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/"
                    + schema + "?useUnicode=true&characterEncoding=utf-8", userName.replaceAll("s", "t"), password.replaceAll("s", "t"));
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

        String buffer = TemplateBuilder.build(table, config, TemplateBuilder.MAPPER);
        printFile(buffer, table.getTableName() + ".xml", out);
        buffer = TemplateBuilder.build(table, config, TemplateBuilder.model);
        printFile(buffer, table.getBeanName() + ".java", out);
        buffer = TemplateBuilder.build(table, config, TemplateBuilder.Service);
        printFile(buffer, table.getBeanName() + "Service.java", out);
        buffer = TemplateBuilder.build(table, config, TemplateBuilder.ServiceImpl);
        printFile(buffer, table.getBeanName() + "ServiceImpl.java", out);
        buffer = TemplateBuilder.build(table, config, TemplateBuilder.DAO);
        printFile(buffer, table.getBeanName() + "DAO.java", out);
        buffer = TemplateBuilder.build(table, config, TemplateBuilder.DAOImpl);
        printFile(buffer, table.getBeanName() + "DAOImpl.java", out);
        buffer = TemplateBuilder.build(table, config, TemplateBuilder.Query);
        printFile(buffer, table.getBeanName() + "Query.java", out);

        if (config.isNeedDO()) {
            buffer = TemplateBuilder.build(table, config, TemplateBuilder.DO);
            printFile(buffer, table.getBeanName() + "DO.java", out);
            buffer = TemplateBuilder.build(table, config, TemplateBuilder.DOConverter);
            printFile(buffer, table.getBeanName() + "DOConverter.java", out);
        }

        if (config.isNeedDTO()) {
            buffer = TemplateBuilder.build(table, config, TemplateBuilder.dtoModel);
            printFile(buffer, table.getBeanName() + "DTO.java", out);
            buffer = TemplateBuilder.build(table, config, TemplateBuilder.DTOConverter);
            printFile(buffer, table.getBeanName() + "DTOConverter.java", out);
        }

        if (config.isNeedFacade()) {
            buffer = TemplateBuilder.build(table, config, TemplateBuilder.QueryFacade);
            printFile(buffer, table.getBeanName() + "QueryFacade.java", out);
            buffer = TemplateBuilder.build(table, config, TemplateBuilder.QueryFacadeImpl);
            printFile(buffer, table.getBeanName() + "QueryFacadeImpl.java", out);
        }

        out.flush();
    }

    private static void printFile(String buffer, String name, ZipOutputStream out) throws IOException {
        ZipEntry entry = new ZipEntry(name);
        out.putNextEntry(entry);
        out.write(buffer.getBytes());
    }


    private static TableConfig getTable(Connection conn, String table) throws SQLException {
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

    private static List<ColumnConfig> getColumns(DatabaseMetaData metaData, String table) throws SQLException {
        List<ColumnConfig> columns = new LinkedList<>();
        // 根据表名提前表里面信息：
        ResultSet primaryKeySet = metaData.getPrimaryKeys(null, null, table);
        String primaryKey = null;
        while (primaryKeySet.next()) {
            primaryKey = primaryKeySet.getString("COLUMN_NAME");
        }
        ResultSet colRet = metaData.getColumns(null, "%", table, "%");
        while (colRet.next()) {
            String columnName = colRet.getString("COLUMN_NAME");
            int columnType = colRet.getInt("DATA_TYPE");
            String typeName = colRet.getString("TYPE_NAME");
            String comment = colRet.getString("REMARKS");
            comment = new String(comment.getBytes(), StandardCharsets.UTF_8);
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
        ColumnConfig column = new ColumnConfig(isPrimaryKey, columnName,
                StringUtils.getFistLowName(StringUtils.getJavaName(columnName)));
        JdbcTypeService.JdbcType type = JdbcTypeService.getInstances().getJavaType(columnType);
        column.setColumnType(type.getJdbcType());
        column.setJavaType(type.getJavaType());
        column.setSimpleJavaType(StringUtils.getSimpleName(type.getJavaType()));
        return column;
    }
}
