package me.zhengjie.modules.tc.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

@Data
public class MerchandiseQueryCriteria {

    @Query(type = Query.Type.INNER_LIKE)
    private String name;
    @Query
    private Long categoryId;
    @Query
    private Long merchantId;
    @Query(type = Query.Type.BETWEEN)
    private String[] createTime;
    @Query
    private Integer status;
    @Query
    private String openId;

    @Query(blurry = "name,description")
    private String value;

    @Query(type = Query.Type.INNER_LIKE)
    private String diQu;

}
