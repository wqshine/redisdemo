package com.example.redisdemo.commentgenerate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TableComment {
    private String schema;
    private String tableName;
    private String comment;

    private List<ColumnComment> columnComments;

    public void addColumnComment(ColumnComment columnComment){
        columnComment.setTableComment(this);
        columnComments.add(columnComment);
    }

}
