package me.zhengjie.modules.tc.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name="tc_merchandise")
public class Merchandise extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id")
    @NotNull(groups = Update.class)
    @ApiModelProperty(value = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ApiModelProperty(value = "商品名称")
    private String name;
    @ApiModelProperty(value = "商品描述")
    private String description;
    @ApiModelProperty(value = "商品图片")
    private String url;
    @ApiModelProperty(value = "商品联系手机")
    private String phone;
    @ApiModelProperty(value = "商品对应商家")
    private Long merchantId;
    @ApiModelProperty(value = "商品对应分类")
    private Long categoryId;
    @ApiModelProperty(value = "商品评价等级")
    private Integer level;
    @ApiModelProperty(value = "商品价格")
    private String price;
    @ApiModelProperty(value = "1.官方自营2.商家运营")
    private Integer type;
    @ApiModelProperty(value = "服务范围")
    private String diQu;

    private Integer status;
    @Transient
    private String[] urls;
    @Transient
    private String merchant;
    @Transient
    private String diQu2;
    @Transient
    private String categoryName;

    private String openId;
}
