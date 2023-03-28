package com.example.redisdemo.commentgenerate;

import com.example.redisdemo.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.SQLException;
import java.util.*;

@Component
@Profile("dev")
public class GenerateOracleComment implements CommandLineRunner {

    @Value("#{entityManagerFactory.properties['hibernate.hbm2ddl.auto']}")
    private String ddlAuto;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        try{
            if(!CommonUtil.isBlank(ddlAuto) && Arrays.asList("update", "create", "create-drop").contains(ddlAuto.toLowerCase())){
                generateComments();
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    private void generateComments(){

        //从静态变量中获取与entity绑定的表信息
        List<TableComment> tableCommentList = CommentIntegrator.getTableComments();
        if(tableCommentList.isEmpty()){
            return;
        }

        String defaultSchema = getDefaultSchema();
        for (TableComment tableComment : tableCommentList) {
            if(CommonUtil.isBlank(tableComment.getSchema())){
                tableComment.setSchema(defaultSchema);
            }
        }

        Map<String, List<TableComment>> schema_tableComments_map = new HashMap<>();
        for(TableComment tableComment : tableCommentList){
            String schema = tableComment.getSchema().toUpperCase();
            if(!schema_tableComments_map.containsKey(schema)){
                schema_tableComments_map.put(schema, new ArrayList<>());
            }
            schema_tableComments_map.get(schema).add(tableComment);
        }

        for(Map.Entry<String, List<TableComment>> entry : schema_tableComments_map.entrySet()){
            updateASchema(entry.getKey(), entry.getValue());
        }
    }

    private String getDefaultSchema(){
        Query query = entityManager.createNativeQuery("SELECT SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA') FROM DUAL");
        return query.getSingleResult().toString();
    }


    private void updateASchema(String schema, List<TableComment> tableComments){

        Map<String, String> table_comment_map = getTableCommentMap(schema);
        Map<String, Map<String, String>> table_columns_map = getTableColumnsMap(schema);

        for(TableComment tableComment : tableComments){

            String table = tableComment.getTableName().toUpperCase();
            //现存有这个表
            if(table_comment_map.containsKey(table) && table_columns_map.containsKey(table)){
                updateATable(schema, table, tableComment, table_comment_map.get(table), table_columns_map.get(table));
            }
        }
    }

    /**
     *
     * @param schema
     * @param table
     * @param tableComment 新的表及列的注释
     * @param oldTableComment 旧的表注释
     * @param oldColumnComments 旧的列注释
     */
    private void updateATable(String schema, String table, TableComment tableComment, String oldTableComment, Map<String, String> oldColumnComments){

        String newTableComment = tableComment.getComment();
        if(!CommonUtil.isBlank(newTableComment) && !newTableComment.equals(oldTableComment)){
            updateTableComment(schema, table, newTableComment);
        }

        List<ColumnComment> newColumnComments = tableComment.getColumnComments();

        for(ColumnComment columnComment : newColumnComments){
            String column = columnComment.getColumnName().toUpperCase();
            String newColumnComment = columnComment.getComment();
            //若已有此列
            if(oldColumnComments.containsKey(column)){
                String oldColumnComment = oldColumnComments.get(column);
                if(!CommonUtil.isBlank(newColumnComment) && !newColumnComment.equals(oldColumnComment)){
                    updateColumnComment(schema, table, column, newColumnComment);
                }
            }
        }
    }

    /**
     *
     * @param schema
     * @return 返回 现有的 (大写)表名_表注释map
     * @throws SQLException
     */
    private Map<String, String> getTableCommentMap(String schema) {
        String sql =
                "SELECT TABLE_NAME, COMMENTS \n" +
                "FROM ALL_TAB_COMMENTS \n" +
                "WHERE OWNER = ?1 \n" +
                "AND TABLE_TYPE = 'TABLE'";

        List tableAndComments = entityManager
                                    .createNativeQuery(sql)
                                    .setParameter(1, schema.toUpperCase())
                                    .getResultList();

        Map<String, String> table_comment_map = new HashMap<>();

        for(Object o : tableAndComments){
            Object[] row = (Object[]) o;
            table_comment_map.put(row[0].toString().toUpperCase(), row[1] == null ? "" : row[1].toString());
        }
        return table_comment_map;
    }

    /**
     *
     * @param schema
     * @return 返回 现有的 (大写)表名_列集合map，列集合是 (大写)列名_列注释map
     * @throws SQLException
     */
    private Map<String, Map<String, String>> getTableColumnsMap(String schema) {
        String sql =
                "SELECT TABLE_NAME, COLUMN_NAME, COMMENTS \n" +
                "FROM ALL_COL_COMMENTS \n" +
                "WHERE OWNER = ?1 ";

        List columnAndComments = entityManager
                                    .createNativeQuery(sql)
                                    .setParameter(1, schema.toUpperCase())
                                    .getResultList();

        Map<String, Map<String, String>> table_columns_map = new HashMap<>();

        for(Object o : columnAndComments){
            Object[] row = (Object[]) o;

            String table = row[0].toString().toUpperCase();
            String column = row[1].toString().toUpperCase();
            String comment = row[2] == null ? "" : row[2].toString();

            if(!table_columns_map.containsKey(table)){
                table_columns_map.put(table, new HashMap<>());
            }
            Map<String, String> column_comment_map = table_columns_map.get(table);
            column_comment_map.put(column, comment);
        }

        return table_columns_map;
    }

    private void updateTableComment(String schema, String table, String tableComment) {
        String sql = String.format("COMMENT ON TABLE %s.%s IS '%s' ", schema, table, tableComment);
        entityManager.createNativeQuery(sql).executeUpdate();
    }


    private void updateColumnComment(String schema, String table, String column, String columnComment) {
        String sql = String.format("COMMENT ON COLUMN %s.%s.%s IS '%s' ", schema, table, column, columnComment);
        entityManager.createNativeQuery(sql).executeUpdate();
    }

}
