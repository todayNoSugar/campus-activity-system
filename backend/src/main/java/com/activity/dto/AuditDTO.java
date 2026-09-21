package com.activity.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class AuditDTO {

    @NotNull(message = "审核状态不能为空")
    @Min(value = 1, message = "审核状态取值不正确")
    @Max(value = 2, message = "审核状态取值不正确")
    private Integer status;

    private String remark;
}
