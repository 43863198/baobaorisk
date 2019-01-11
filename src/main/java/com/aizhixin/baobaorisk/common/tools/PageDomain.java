package com.aizhixin.baobaorisk.common.tools;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="分页描述对象")
@ToString
@NoArgsConstructor
public class PageDomain {
	@ApiModelProperty(value = "总条数")
	@Getter
    @Setter
    private Long totalElements;//总条数
	@ApiModelProperty(value = "总页数")
	@Getter
    @Setter
    private Integer totalPages;//总页数
	@ApiModelProperty(value = "第几页")
	@Getter
    @Setter
    private Integer pageNumber;//第几页
	@ApiModelProperty(value = "每页多少条")
	@Getter
    @Setter
    private Integer pageSize;//每页多少条
}
