package com.aizhixin.baobaorisk.redpackage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="发包数量及金额统计")
@NoArgsConstructor
@ToString
public class PublishPackageCountDTO {
    @ApiModelProperty(value = "任务数")
    @Getter @Setter private long tasks;

    @ApiModelProperty(value = "金额数")
    @Getter @Setter private long fees;

    @ApiModelProperty(value = "红包数")
    @Getter @Setter private long reds;

    public PublishPackageCountDTO(Long tasks, Long fees, Long reds) {
        this.tasks = (null == tasks ? 0L : tasks);
        this.fees = (null == fees ? 0L : fees);
        this.reds = (null == reds ? 0L : reds);
    }
}
