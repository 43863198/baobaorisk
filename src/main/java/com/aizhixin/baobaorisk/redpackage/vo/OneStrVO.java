package com.aizhixin.baobaorisk.redpackage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;

@ApiModel(description="单值对象")
@NoArgsConstructor
@ToString
public class OneStrVO {
    @ApiModelProperty(value = "name")
    @Column(name = "name")
    @Getter @Setter private String name = "success";
}
