package com.ranger.controller.verify;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.equip.contract.EquipRecommendContract;
import com.ranger.equip.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

/**
 * 描述:装备推荐(现只用于iAcro小程序)
 *
 * @author ssd
 * @create 2019-07-26 2:33 PM
 */
@RestController
@RequestMapping("/verify/EquipRecommend")
public class EquipRecommendController {

    @Reference(interfaceClass = EquipRecommendContract.class, timeout = 1200000)
    private EquipRecommendContract equipRecommendContract;


    /**
     * 装备推荐新增
     *
     * @return
     */
    @PostMapping("")
    public ResultVO addActivityRecommend(@RequestParam Long equipId,
                                         @RequestParam Long clubId,
                                         @RequestParam(value = "position", required = false, defaultValue = "0") Double position) {
        return equipRecommendContract.insert(equipId, clubId, position);
    }

    /**
     * 获取装备推荐列表
     * @param page
     * @param size
     * @param clubId
     * @return
     */
    @GetMapping("")
    public ResultVO getActivityRecommend(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                         @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                         @RequestParam Long clubId) {
        return equipRecommendContract.selectPage(clubId, page, size);
    }

    /**
     * 删除装备推荐
     * @param recommendId
     * @return
     */
    @DeleteMapping("")
    public ResultVO deleteActivityRecommend(@RequestParam Long recommendId) {
        return equipRecommendContract.delete(recommendId);
    }

}


