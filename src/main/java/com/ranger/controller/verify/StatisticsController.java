package com.ranger.controller.verify;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.statistics.contract.ClubVisitStatisticsContract;
import com.ranger.statistics.contract.CodeContract;
import com.ranger.statistics.contract.MemberEnterStatisticsContract;
import com.ranger.statistics.enums.FeedType;
import com.ranger.statistics.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 *
 * @author ssd
 * @create 2019-07-25 2:44 PM
 */
@RestController
@RequestMapping("/verify/statistics")
public class StatisticsController {

    /**
     * 统计服务(俱乐部访问统计)
     */
    @Reference(interfaceClass = ClubVisitStatisticsContract.class, timeout = 1200000)
    private ClubVisitStatisticsContract clubVisitStatisticsContract;

    /**
     * 统计服务(会员入驻统计)
     */
    @Reference(interfaceClass = MemberEnterStatisticsContract.class, timeout = 1200000)
    private MemberEnterStatisticsContract memberEnterStatisticsContract;

    @Reference(interfaceClass = CodeContract.class, timeout = 1200000)
    private CodeContract codeContract;


    /**
     * 根据俱乐部id获取30天内俱乐部访问数量和会员入驻情况,本月会员入驻数,近7天的访问总数
     *
     * @param clubId
     * @return
     */
    @GetMapping("/{clubId}/Statistics")
    public ResultVO selectNumber(@PathVariable Long clubId) {
        /**
         * 一个月的访问情况
         */
        List visitList = (List) clubVisitStatisticsContract.selectThirtyStatistics(clubId).getBody();
        /**
         * 一个月的会员入驻情况
         */
        List enterList = (List) memberEnterStatisticsContract.selectThirtyStatistics(clubId).getBody();
        /**
         * 7天的会员入驻情况
         */
        List enterSevenList = (List) memberEnterStatisticsContract.selectSevenStatistics(clubId).getBody();
        /**
         * 7天的访问总数
         */
        Integer visitNum = (Integer) clubVisitStatisticsContract.selectSevenStatistics(clubId).getBody();
        /**
         * 本月的会员入驻总数
         */
        Integer enterNum = (Integer) memberEnterStatisticsContract.selectThisMonthStatistics(clubId).getBody();
        /**
         * 7天会员入驻总数
         */
        Integer enterSevenNum = (Integer) memberEnterStatisticsContract.selectSevenSumStatistics(clubId).getBody();

        Map resultMap = new HashMap();
        resultMap.put("visitStatistics", visitList);
        resultMap.put("enterStatistics", enterList);
        resultMap.put("visitNumber", visitNum);
        resultMap.put("enterNumber", enterNum);
        resultMap.put("enterSevenList", enterSevenList);
        resultMap.put("enterSevenNum", enterSevenNum);

        return new ResultVO(resultMap);
    }

    /**
     * 获取活动二维码
     *
     * @param
     * @return
     */
    @GetMapping("/getCode")
    public com.ranger.statistics.vo.ResultVO selectByGoodsId(@RequestParam(value = "feedType", required = true) FeedType feedType,
                                                             @RequestParam(value = "relateId", required = true) Long relateId,
                                                             @RequestParam(value = "url", required = false, defaultValue = "index") String url,
                                                             @RequestParam(value = "appType", required = false, defaultValue = "1") Integer appType,
                                                             @RequestParam(value = "childrenId", required = false) Integer childrenId
    ) {
        return codeContract.makeCode(feedType, relateId, url, appType, childrenId);

    }
}
