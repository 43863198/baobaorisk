package com.aizhixin.baobaorisk.redpackage.entity;

import com.aizhixin.baobaorisk.common.core.DataValidity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@ApiModel(description="微信用户信息")
//@Entity(name = "T_WEIXIN_USER")
@NoArgsConstructor
@ToString
public class WeixinUser implements java.io.Serializable {
    @ApiModelProperty(value = "ID")
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID")
    @Getter @Setter private String id;

    @ApiModelProperty(value = "openId")
    @Column(name = "OPEN_ID")
    @Getter @Setter private String openId;

    @ApiModelProperty(value = "unionId")
    @Column(name = "UNIONID")
    @Getter @Setter private String unionId;

    @ApiModelProperty(value = "微信头像URL")
    @Column(name = "AVATAR")
    @Getter @Setter private String avatar;

    @ApiModelProperty(value = "昵称")
    @Column(name = "NICK")
    @Getter @Setter private String nick;

    @ApiModelProperty(value = "性别")
    @Column(name = "GENDER")
    @Getter @Setter private String gender;

    @ApiModelProperty(value = "城市")
    @Column(name = "CITY")
    @Getter @Setter private String city;

    @ApiModelProperty(value = "省")
    @Column(name = "PROVINCE")
    @Getter @Setter private String province;

    @ApiModelProperty(value = "国家")
    @Column(name = "COUNTRY")
    @Getter @Setter private String country;

    @ApiModelProperty(value = "语言")
    @Column(name = "LANGUAGE")
    @Getter @Setter private String language;

    @ApiModelProperty(value = "创建时间")
    @CreatedDate
    @Column(name = "CREATED_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date createdDate;

    @ApiModelProperty(value = "最后一次修改时间")
    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date lastModifiedDate;

    @ApiModelProperty(value = "是否删除标志")
    @Column(name = "DELETE_FLAG")
    @Getter @Setter private Integer deleteFlag = DataValidity.VALID.getState();
}
