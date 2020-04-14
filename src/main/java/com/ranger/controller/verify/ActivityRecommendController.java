package com.ranger.controller.verify;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.activity.contract.ActivityBuyGoodCheckContract;
import com.ranger.activity.contract.ActivityContract;
import com.ranger.activity.contract.ActivityRecommendContract;
import com.ranger.activity.dto.*;
import com.ranger.activity.vo.ActivityRecommendVO;
import com.ranger.activity.vo.ActivityRegistrationExportVO;
import com.ranger.activity.vo.ActivityVO;
import com.ranger.activity.vo.ResultVO;
import com.ranger.advert.contract.ClubActivityTypeContract;
import com.ranger.audit.contract.ActivityAuditContract;
import com.ranger.photo.contract.PhotoContract;
import com.ranger.statistics.contract.QiniuContract;
import com.ranger.utils.ExcelExportUtil;
import com.ranger.utils.IdGen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述:活动推荐(现只用于iAcro小程序)
 *
 * @author ssd
 * @create 2019-07-26 2:33 PM
 */
@RestController
@RequestMapping("/verify/activityRecommend")
public class ActivityRecommendController {

    @Reference(interfaceClass = ActivityRecommendContract.class, timeout = 1200000)
    private ActivityRecommendContract activityRecommendContract;


    /**
     * 活动推荐新增
     *
     * @return
     */
    @PostMapping("")
    public ResultVO addActivityRecommend(@RequestBody ActivityRecommendDTO activityRecommendVO) {
        return activityRecommendContract.insert(activityRecommendVO);
    }

    /**
     * 获取活动推荐列表
     *
     * @param page
     * @param size
     * @param clubId
     * @return
     */
    @GetMapping("")
    public ResultVO getActivityRecommend(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                         @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                         @RequestParam Long clubId) {
        return activityRecommendContract.findList(page, size, clubId);
    }


    /**
     * 删除活动推荐
     *
     * @param recommendId
     * @return
     */
    @DeleteMapping("")
    public ResultVO deleteActivityRecommend(@RequestParam Long recommendId) {
        return activityRecommendContract.delete(recommendId);
    }

}


