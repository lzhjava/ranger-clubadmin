package com.ranger.controller.verify;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.statistics.contract.AdvertInfoContract;
import com.ranger.statistics.dto.AdvertInfoDTO;
import com.ranger.statistics.vo.ResultVO;
import com.ranger.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 描述:问答
 *
 * @author: sunshuaidong
 * @create: 2018-12-06 18:08
 * @brief:
 */
@RestController
@RequestMapping("/verify/advertInfo")
public class AdvertInfoController {

    @Reference(interfaceClass = AdvertInfoContract.class, timeout = 1200000)
    private AdvertInfoContract advertInfoContract;

    @Autowired
    private TimeUtil timeUtil;

    /**
     * 用户预留广告信息
     *
     * @param advertInfoDTO
     * @return
     */
    @PostMapping("")
    public ResultVO addAdvertInfo(@RequestBody AdvertInfoDTO advertInfoDTO) {

        return advertInfoContract.insert(advertInfoDTO);
    }

    /**
     * 根据经销商id获取浏览总数
     *
     * @param distributorId
     * @return
     */
    @PostMapping("/addAdvertVisitNumber")
    public ResultVO selectByAdvertVisitNumber(@RequestParam(required = true) Long distributorId,
                                              @RequestParam(required = true) String time) {

        return advertInfoContract.addAdvertVisitNumber(distributorId, time);
    }

    /**
     * 用户预留广告信息
     *
     * @param advertInfoDTO
     * @return
     */
    @PutMapping("/{id}")
    public ResultVO updateAdvertInfo(@PathVariable Long id, @RequestBody AdvertInfoDTO advertInfoDTO) {

        return advertInfoContract.update(id, advertInfoDTO);
    }

    /**
     * 根据经销商id获取浏览总数
     *
     * @param distributorId
     * @return
     */
    @GetMapping("/getVisitNumbe")
    public ResultVO selectByAdvertVisitNumber(@RequestParam(required = true) Long distributorId,
                                              @RequestParam(required = false) Long startTime,
                                              @RequestParam(required = false) Long endTime) {
        String startTimeString = null;
        String endTimeTimeString = null;
        if (startTime != null) {
            startTimeString = timeUtil.timeStampDate(startTime);
        }

        if (endTime != null) {
            endTimeTimeString = timeUtil.timeStampDate(endTime);
        }

        return advertInfoContract.getAdvertVisitNumber(distributorId, startTimeString, endTimeTimeString);
    }

    /**
     * 根据经销商id和用户唯一编码查询用户是否已经预留了信息
     *
     * @param distributorId
     * @param hashId
     * @return
     */
    @GetMapping("/getWriteState")
    public ResultVO selectByDistributorIdAndHashId(@RequestParam(required = true) Long distributorId,
                                                   @RequestParam(required = true) String hashId) {
        return advertInfoContract.selectByDistributorIdAndHashId(distributorId, hashId);
    }

    /**
     * 根据经销商id查询用户预留信息列表
     *
     * @param distributorId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/getAdvertInfos")
    public ResultVO selectByDistributorId(@RequestParam(required = true) Long distributorId,
                                          @RequestParam(required = false, defaultValue = "0") Integer page,
                                          @RequestParam(required = false, defaultValue = "10") Integer size,
                                          @RequestParam(required = false) Long startTime,
                                          @RequestParam(required = false) Long endTime) {
        Long dayStartTime = null;
        Long dayEndTime = null;
        if (startTime != null) {
            dayStartTime = timeUtil.getDailyStartTime(startTime);
        }
        if (endTime != null) {
            dayEndTime = timeUtil.getDailyEndTime(endTime);
        }

        return advertInfoContract.selectByDistributorId(distributorId, page, size, dayStartTime, dayEndTime);
    }


}
