package com.aizhixin.baobaorisk.redpackage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="用户提现金额统计")
@NoArgsConstructor
@ToString
public class WithDrawCountDTO {
    @ApiModelProperty(value = "金额数")
    @Getter @Setter private long fee;

    @ApiModelProperty(value = "红包数")
    @Getter @Setter private long discount;

    public WithDrawCountDTO(Long fee, Long discount) {
        this.fee = (null == fee ? 0L : fee);
        this.discount = (null == discount ? 0L : discount);
    }
}
