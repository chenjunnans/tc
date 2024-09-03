package me.zhengjie.modules.tc.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name="tc_user")
public class WxUser {
    @Id
    @Column(name = "id")
    @NotNull(groups = BaseEntity.Update.class)
    @ApiModelProperty(value = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ApiModelProperty(value = "用户名称")
    private String username;
    @ApiModelProperty(value = "微信用户id")
    private String openid;
    @ApiModelProperty(value = "用户头像")
    private String face;
    @ApiModelProperty(value = "最后一次登录时间")
    private String time;
    @ApiModelProperty(value = "发送消息次数")
    private Integer number;
}
