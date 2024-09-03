package me.zhengjie.modules.tc.service.dto;

import lombok.Data;

import java.util.List;

/**
 * 省市区三级封装联动
 */
@Data
public class Region2Dto {
    private String value;
    private String name;
     private List<Region2Dto> submenu;

}
