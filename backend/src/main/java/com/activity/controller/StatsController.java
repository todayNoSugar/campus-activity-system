package com.activity.controller;

import com.activity.common.Result;
import com.activity.service.StatsService;
import com.activity.vo.StatsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端：数据概览
 */
@RestController
@RequestMapping("/api/admin/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping
    public Result<StatsVO> overview() {
        return Result.ok(statsService.overview());
    }
}
