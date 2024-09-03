package me.zhengjie.modules.tc.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.Job;
import me.zhengjie.modules.system.domain.Role;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="tc_merchant")
public class Merchant extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id")
    @NotNull(groups = BaseEntity.Update.class)
    @ApiModelProperty(value = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "商家名称不能为空")
    @ApiModelProperty(value = "商家名称")
    private String name;
    @NotBlank(message = "商家电话不能为空")
    @ApiModelProperty(value = "商家电话")
    private String phone;
    @ApiModelProperty(value = "商家图片")
    private String url;
    @ApiModelProperty(value = "商家描述")
    private String description;
    @ApiModelProperty(value = "商家对应用户")
    private Long userId;
    @NotBlank(message = "商家地址不能为空")
    @ApiModelProperty(value = "省id")
    private String provinceId;
    @ApiModelProperty(value = "省")
    private String province;
    @NotBlank(message = "商家地址不能为空")
    @ApiModelProperty(value = "市id")
    private String cityId;
    @ApiModelProperty(value = "市")
    private String city;
    @ApiModelProperty(value = "区id")
    private String districtId;
    @ApiModelProperty(value = "区")
    private String district;
    @NotBlank(message = "商家地址不能为空")
    private String address;
    @ApiModelProperty(value = "商家状态1.待审核2.正常3.审核失败")
    private String state;
    @ApiModelProperty(value = "审核失败原因")
    private String cause;
    private String userName;
    @Transient
    private Set<Role> roles;
    @Transient
    private Set<Job> jobs;
    @Transient
    private Dept dept;
}
