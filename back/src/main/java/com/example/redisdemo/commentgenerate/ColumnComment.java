package com.example.redisdemo.commentgenerate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ColumnComment {
    private String columnName;
    private String comment;

    private TableComment tableComment;
}
