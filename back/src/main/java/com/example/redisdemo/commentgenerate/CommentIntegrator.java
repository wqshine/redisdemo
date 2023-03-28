package com.example.redisdemo.commentgenerate;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.mapping.*;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 负责获取hibernate生成的entity映射，从而用于生成表、字段注释
 *
 * {@link org.hibernate.integrator.spi.IntegratorService}负责收集和注册所有的integrator,
 * 在实现类{@link org.hibernate.integrator.internal.IntegratorServiceImpl#IntegratorServiceImpl(java.util.LinkedHashSet, org.hibernate.boot.registry.classloading.spi.ClassLoaderService)}的构造函数可以看到
 * 里面使用了{@link org.hibernate.boot.registry.classloading.spi.ClassLoaderService},会把通过SPI机制注册的{@link Integrator}实现类注册进去
 * (在resources/META-INF/service下的org.hibernate.integrator.spi.Integrator文件里写上了实现类的全类名)
 *
 * @see org.hibernate.integrator.spi.IntegratorService
 * @see org.hibernate.integrator.internal.IntegratorServiceImpl
 * @see <a href="https://docs.jboss.org/hibernate/orm/5.0/integrationsGuide/en-US/html_single"/>
 */
public class CommentIntegrator implements Integrator {

    /**
     * 在类上使用的是{@link Schema}注解
     */
    private static final CommentAnnotationDetail CLASS_ANNOTATION =
            new CommentAnnotationDetail<>(Schema.class, schema -> schema.description());

    /**
     * 在成员变量上使用的是{@link Schema}注解
     */
    private static final CommentAnnotationDetail FIELD_ANNOTATION =
            new CommentAnnotationDetail<>(Schema.class, schema -> schema.description());




    private static List<TableComment> tableComments = new ArrayList<>();

    public static List<TableComment> getTableComments(){
        return tableComments;
    }

    /**
     * 因为在此阶段还没有执行ddl语句（如果有配置），因此先把结果存到一个静态变量中，到后面再更改注释
     * @param metadata
     * @param sessionFactory
     * @param serviceRegistry
     */
    @Override
    public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
        //"update".equals(sessionFactory.getProperties().get("hibernate.hbm2ddl.auto"))
        tableComments = getTableComments(metadata);
    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactoryImplementor, SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
    }

    private List<TableComment> getTableComments(Metadata metadata){
        List<TableComment> tableComments = new ArrayList<>();
        for (PersistentClass persistentClass : metadata.getEntityBindings()){
            tableComments.add(getTableComment(persistentClass));
        }
        return tableComments;
    }

    private TableComment getTableComment(PersistentClass persistentClass){

        Class<?> clz = persistentClass.getMappedClass();
        Table table = persistentClass.getTable();
        TableComment tableComment = TableComment.builder()
                                        .schema(table.getSchema())
                                        .tableName(table.getName())
                                        .comment("")
                                        .columnComments(new ArrayList<>())
                                        .build();
        if (clz.isAnnotationPresent(CLASS_ANNOTATION.getAnnotationType())) {
            Annotation annotation = clz.getAnnotation(CLASS_ANNOTATION.getAnnotationType());
            tableComment.setComment(CLASS_ANNOTATION.getCommonValue(annotation));
        }

        Property identifierProperty = persistentClass.getIdentifierProperty();
        if (identifierProperty != null) {
            addColumnComment(tableComment, clz, identifierProperty);
        } else {
            Component component = persistentClass.getIdentifierMapper();
            if (component != null) {
                Iterator<Property> idPropertyIterator = component.getPropertyIterator();
                while (idPropertyIterator.hasNext()) {
                    addColumnComment(tableComment, clz, idPropertyIterator.next());
                }
            }
        }

        Iterator<Property> propertyIterator = persistentClass.getPropertyIterator();
        while (propertyIterator.hasNext()){
            addColumnComment(tableComment, clz, propertyIterator.next());
        }

        return tableComment;
    }

    private void addColumnComment(TableComment tableComment, Class<?> entityClass, Property property){

        String fieldComment = getCommentFromField(entityClass, property.getName());

        Iterator<Selectable> selectableIterator = property.getValue().getColumnIterator();
        while (selectableIterator.hasNext()){
            Selectable selectable = selectableIterator.next();
            String text = selectable.getText();

            if(!selectable.isFormula()){
                ColumnComment columnComment = ColumnComment.builder()
                                                .columnName(text)
                                                .comment(fieldComment)
                                                .build();
                tableComment.addColumnComment(columnComment);
            }
        }
    }

    /**
     *
     * @param clz
     * @param fieldName
     * @return 如果没有注解，返回空字符串
     */
    private static String getCommentFromField(Class<?> clz, String fieldName){
        if(clz == null){
            throw new IllegalStateException("不能找到字段"+fieldName);
            //return "";
        }

        try{
            Field field = clz.getDeclaredField(fieldName);
            if(field.isAnnotationPresent(FIELD_ANNOTATION.getAnnotationType())){
                Annotation annotation = field.getAnnotation(FIELD_ANNOTATION.getAnnotationType());
                return FIELD_ANNOTATION.getCommonValue(annotation);
            }else{
                return "";
            }
        }catch (NoSuchFieldException e){
            return getCommentFromField(clz.getSuperclass(), fieldName);
        }
    }






}
