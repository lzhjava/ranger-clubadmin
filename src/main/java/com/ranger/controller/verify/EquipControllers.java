package com.ranger.controller.verify;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.equip.contract.*;
import com.ranger.equip.enums.EquipAuditStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by caiyanying on 17/5/23.
 */
@RestController
@RequestMapping("/verify/equip")
public class EquipControllers {

    /**
     * 品牌
     */
    @Reference(interfaceClass = BrandContract.class, timeout = 1200000)
    private BrandContract brandContract;

    /**
     * 系列
     */
    @Reference(interfaceClass = SeriesContract.class, timeout = 1200000)
    private SeriesContract seriesContract;

    /**
     * 车型
     */
    @Reference(interfaceClass = CarTypeContract.class, timeout = 1200000)
    private CarTypeContract carTypeContract;


    @Reference(interfaceClass = EquipContract.class, timeout = 1200000)
    private EquipContract equipContract;

    @Reference(interfaceClass = EquipAuditContract.class, timeout = 1200000)
    private EquipAuditContract equipAuditContract;

    /**
     * 品牌查询列表
     *
     * @param
     * @param
     * @return
     */
    @GetMapping("/brand/{carTypeId}")
    public com.ranger.equip.vo.ResultVO getBrandList(@PathVariable Long carTypeId) {
        return brandContract.selectList(carTypeId);
    }

    /**
     * 根据品牌id获取系列列表
     *
     * @param brandId
     * @return
     */
    @GetMapping("/series/{brandId}")
    public com.ranger.equip.vo.ResultVO getSeriesList(@PathVariable Long brandId) {
        return seriesContract.selectList(brandId);
    }

    /**
     * 获取车型列表
     *
     * @return
     */
    @GetMapping("/type/getList")
    public com.ranger.equip.vo.ResultVO getTypeList() {
        return carTypeContract.selectList();
    }

    @RequestMapping(value = "/equipAudit", method = RequestMethod.GET)
    public com.ranger.equip.vo.ResultVO getEquipAudit(Long clubId, Long carTypeId, Long brandId, Long seriesId, EquipAuditStatus equipAuditStatus, Integer page, Integer count, String orderBy) {

        return equipAuditContract.selectClubEquipData(clubId, carTypeId, brandId, seriesId, equipAuditStatus, page, count, orderBy);
    }

    @RequestMapping(value = "/equipAudit/{equipId}", method = RequestMethod.GET)
    public com.ranger.equip.vo.ResultVO getEquipInfo(@PathVariable Long equipId) {

        return equipContract.selectEquipInfoById(equipId, null, null);
    }

    @RequestMapping(value = "/equipAudit/{equipId}", method = RequestMethod.POST)
    public com.ranger.equip.vo.ResultVO getEquipAudit(@PathVariable Long equipId, Long clubId, Boolean result) {

        return equipAuditContract.handleAudit(clubId, equipId, result);
    }

    @RequestMapping(value = "/equipAudit/{equipId}", method = RequestMethod.DELETE)
    public com.ranger.equip.vo.ResultVO removeEquipAudit(@PathVariable Long equipId, Long clubId) {

        return equipAuditContract.removeEquipByClub(clubId, equipId);
    }

}
