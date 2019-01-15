package com.aizhixin.baobaorisk.redpackage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="抢包数量及金额统计")
@NoArgsConstructor
@ToString
public class GrapPackageCountDTO {

    @ApiModelProperty(value = "参与任务数")
    @Getter @Setter private long tasks;

    @ApiModelProperty(value = "金额数")
    @Getter @Setter private long fees;

    @ApiModelProperty(value = "红包数")
    @Getter @Setter private long reds;

    public GrapPackageCountDTO(Long fees, Long reds) {
        this.fees = (null == fees ? 0L : fees);
        this.reds = (null == reds ? 0L : reds);
    }
}
