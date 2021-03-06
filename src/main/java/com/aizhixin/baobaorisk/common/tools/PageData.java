package com.aizhixin.baobaorisk.common.tools;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页对象
 * Created by zhen.pan on 2017/6/29.
 */
@ApiModel(description="分页数据对象")
@ToString
@NoArgsConstructor
public class PageData<T> {
    @ApiModelProperty(value = "分页数据")
    @Getter
    @Setter
    private PageDomain page = new PageDomain();
    @ApiModelProperty(value = "数据内容")
    @Getter
    @Setter
    private List<T> data = new ArrayList<>();
}
