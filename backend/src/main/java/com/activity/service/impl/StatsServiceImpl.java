package com.activity.service.impl;

import com.activity.mapper.StatsMapper;
import com.activity.service.StatsService;
import com.activity.vo.StatsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatsServiceImpl implements StatsService {

    @Autowired
    private StatsMapper statsMapper;

    @Override
    public StatsVO overview() {



        // 完整业务实现代码请获取完整版源码
        return null;
    }
}
