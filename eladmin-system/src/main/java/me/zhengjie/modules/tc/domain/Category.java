package me.zhengjie.modules.tc.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import me.zhengjie.modules.system.domain.Menu;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name="tc_category")
public class Category  extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id")
    @NotNull(groups = Update.class)
    @ApiModelProperty(value = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "分类描述")
    private String description;

    @ApiModelProperty(value = "分类顺序")
    private Integer sort;

    @ApiModelProperty(value = "上级分类id")
    private Long pid;
    @ApiModelProperty(value = "图标url")
    private String url;

    private Boolean hidden;
    private Integer  subCount=0;
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Category menu = (Category) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
