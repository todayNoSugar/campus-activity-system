package com.activity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ActivityDTO {

    @NotNull(message = "请选择活动分类")
    private Long categoryId;

    @NotBlank(message = "活动名称不能为空")
    private String title;

    @NotBlank(message = "活动地点不能为空")
    private String location;

    @NotNull(message = "开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @NotNull(message = "报名截止时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signupDeadline;

    @NotNull(message = "人数上限不能为空")
    @Min(value = 1, message = "人数上限至少为1")
    private Integer maxPeople;

    private String intro;

    private String detail;

    /** 发布状态：1 已发布 0 已下架（默认发布） */
    private Integer status;
}
