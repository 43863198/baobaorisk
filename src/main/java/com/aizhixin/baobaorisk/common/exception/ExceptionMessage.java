/**
 * 
 */
package com.aizhixin.baobaorisk.common.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhen.pan
 *
 */
@ApiModel(description="微信用户信息")
@NoArgsConstructor
@ToString
public class ExceptionMessage {
	@ApiModelProperty(value = "错误编码")
	@Getter @Setter private Integer code;
	@ApiModelProperty(value = "错误详细信息")
	@Getter @Setter private String message;

public ExceptionMessage(Integer code, String message) {
	this.code = code;
	this.message = message;
}
}
