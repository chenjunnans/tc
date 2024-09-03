package me.zhengjie.modules.tc.service.dto;

import lombok.Data;
import me.zhengjie.base.BaseDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryDTO extends BaseDTO {

    private String name;
    private String description;
    private Long id;
    private Long pid;
    private String url;
    private Integer subCount;
    private Integer sort;
    private Boolean  hidden;
    private List<CategoryDTO> children;

    public Boolean getHasChildren() {
        return subCount > 0;
    }

    public Boolean getLeaf() {
        return subCount <= 0;
    }

    public String getLabel() {
        return name;
    }

    public Long getValue() {return id;}

    public List<CategoryDTO> getSubmenu(){
        return children;
    }
}
