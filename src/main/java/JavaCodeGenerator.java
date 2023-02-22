
import dependency.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author yongjian.zw
 * @date 2020/01/01
 */
public class JavaCodeGenerator {

    private static final String IP = "localhost";
    private static final String PORT = "3306";
    private static final String schema = "arch";
    private static final String userName = "root";
    private static final String password = "root";
    public static final String zipSavePath = "/Users/yongjian/Downloads/";
    private static BuildConfig config;

    public static void initConfig() {
        config = new BuildConfig();
        config.needDO(true);
        config.setNeedService(false);
        config.needFacade(false);
        // 文件头
        config.setFileHeader("" +
                "/**\n" +
                " * @author YongJian(249171)" + "\n" +
                " * @since " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())) + "\n" +
                " */");
    }

    public static void main(String[] args) {
        initConfig();
        String[] tables = new String[]{
                "business_capability",
                "capability"
        };
        for (String tableName : tables) {
            buildZipFile(tableName);
        }
    }

    private static void buildZipFile(String table) {
        try {
            String beanName = StringUtils.getJavaName(table);
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + PORT + "/"
                    + schema + "?useUnicode=true&characterEncoding=utf-8", userName, password);
            TableConfig tableConfig = getTable(connection, table);
            connection.close();
            tableConfig.setBeanName(beanName);
            tableConfig.setInjectName(StringUtils.getFistLowName(beanName));

            String filePath = zipSavePath + beanName + ".zip";
            File file = new File(filePath);
            if (file.exists() && !file.delete()) {
                System.out.println("删除已存在的文件："+filePath +" 成功");
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

        String buffer = TemplateBuilderV2.build(table, config, TemplateBuilderV2.MAPPER);
        printFile(buffer, table.getTableName() + ".xml", out);
        buffer = TemplateBuilderV2.build(table, config, TemplateBuilderV2.model);
        printFile(buffer, table.getBeanName() + ".java", out);
        buffer = TemplateBuilderV2.build(table, config, TemplateBuilderV2.DAO);
        printFile(buffer, table.getBeanName() + "DAO.java", out);
        buffer = TemplateBuilderV2.build(table, config, TemplateBuilderV2.DAOImpl);
        printFile(buffer, table.getBeanName() + "DAOImpl.java", out);

        buffer = TemplateBuilderV2.build(table, config, TemplateBuilderV2.DO);
        printFile(buffer, table.getBeanName() + "DO.java", out);
        buffer = TemplateBuilderV2.build(table, config, TemplateBuilderV2.DOConverter);
        printFile(buffer, table.getBeanName() + "Converter.java", out);

        buffer = TemplateBuilderV2.build(table, config, TemplateBuilderV2.Repository);
        printFile(buffer, table.getBeanName() + "Repository.java", out);
        buffer = TemplateBuilderV2.build(table, config, TemplateBuilderV2.RepositoryImpl);
        printFile(buffer, table.getBeanName() + "RepositoryImpl.java", out);

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
        for (ColumnConfig columnConfig : columns) {
            if (columnConfig.isPrimaryKey()) {
                tableConfig.setPrimaryKey(columnConfig);
                break;
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
            boolean isPrimaryKey = columnName.equals(primaryKey);
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
