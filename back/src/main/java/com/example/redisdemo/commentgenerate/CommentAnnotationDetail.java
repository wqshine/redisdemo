package com.example.redisdemo.commentgenerate;

import java.lang.annotation.Annotation;
import java.util.function.Function;

/**
 * 用来确定用哪个注解类来注释
 * @param <A>
 */
public class CommentAnnotationDetail<A extends Annotation>{
    private final Class<A> annotationType;
    private final Function<A, String> getCommonFunc;

    /**
     *
     * @param clz 注解类
     * @param getCommonFunc 获取注解内注释的方式
     */
    public CommentAnnotationDetail(Class<A> clz, Function<A, String> getCommonFunc){
        this.annotationType = clz;
        this.getCommonFunc = getCommonFunc;
    }

    /**
     * 获取注解内的注释
     * @param annotation
     * @return
     */
    public String getCommonValue(Object annotation){
        return this.getCommonFunc.apply((A) annotation);
    }

    /**
     * 获取注解的类型
     * @return
     */
    public Class<A> getAnnotationType(){
        return this.annotationType;
    }
}
