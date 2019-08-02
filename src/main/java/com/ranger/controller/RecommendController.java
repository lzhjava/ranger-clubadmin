package com.ranger.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.activity.contract.ActivityContract;
import com.ranger.activity.contract.WechatLiteRecommendContract;
import com.ranger.activity.enums.WechatLiteRecommendType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 描述:
 *
 * @author ssd
 * @create 2019-08-02 2:14 PM
 */
@RestController
@RequestMapping("/exemption/recommend")
public class RecommendController {

    @Reference(interfaceClass = WechatLiteRecommendContract.class, timeout = 1200000)
    private WechatLiteRecommendContract wechatLiteRecommendContract;

    @RequestMapping(value = "/{clubId}/recommend/top", method = RequestMethod.POST)
    public Object topRecommend(@PathVariable String clubId, Long relateId, WechatLiteRecommendType wechatLiteRecommendType, boolean top) {

        return wechatLiteRecommendContract.topData(relateId, wechatLiteRecommendType, top);

    }

    @RequestMapping(value = "/{clubId}/recommend/top", method = RequestMethod.GET)
    public Object getRecommendTop(@PathVariable Long clubId) {


        return wechatLiteRecommendContract.selectTopData(clubId);
    }


    @RequestMapping(value = "/{clubId}/recommend/top/position", method = RequestMethod.POST)
    public Object positionTopRecommend(@RequestBody Map<String, Integer> positions) {

        return wechatLiteRecommendContract.topDataPosition(positions);
    }
}
