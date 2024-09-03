package me.zhengjie.modules.tc.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

@Data
public class CategoryQueryCriteria {

    @Query(type = Query.Type.INNER_LIKE)
    private String name;
    @Query
    private Long pid;

    @Query(type = Query.Type.IS_NULL, propName = "pid")
    private Boolean pidIsNull;
}
