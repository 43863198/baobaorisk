package com.aizhixin.baobaorisk.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="状态通用表示")
@NoArgsConstructor
@ToString
public class StatusVO {
    @ApiModelProperty(value = "code")
    @Getter @Setter private String code = "0";

    @ApiModelProperty(value = "name")
    @Getter @Setter private String name = "success";
}
