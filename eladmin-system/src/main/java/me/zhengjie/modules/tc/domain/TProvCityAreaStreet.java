package me.zhengjie.modules.tc.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_prov_city_area_street")
public class TProvCityAreaStreet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @ApiModelProperty(value = "id,自增长")
    private Integer id;
    @Column(name = "code")
    private String code;
    @Column(name = "parent_id")
    private String parentId;
    @Column(name = "point_name")
    private String pointName;
    @Column(name = "level")
    private Integer level;
    @Column(name = "state")
    private Integer state;
}
