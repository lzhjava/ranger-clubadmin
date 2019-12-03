package com.ranger.controller.verify;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.club.contract.CarOwnerContract;
import com.ranger.club.contract.ClubContract;
import com.ranger.club.contract.ClubManageContract;
import com.ranger.club.enums.ClubJoinVerifyType;
import com.ranger.club.enums.ClubMemberType;
import com.ranger.club.po.ClubOwnerJoinVerifyPO;
import com.ranger.club.po.ClubOwnerPO;
import com.ranger.club.po.ClubPO;
import com.ranger.club.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

/**
 * 车主认证信息管理
 *
 * @author huipeng
 * @Title: ClubController
 * @ProjectName ranger-clubadmin
 * @Description: TODO
 * @date 2019/7/25下午2:42
 */
@RestController
@RequestMapping("/verify/car")
public class CarOwnerController {


    @Reference(interfaceClass = CarOwnerContract.class, timeout = 1200000)
    private CarOwnerContract carOwnerContract;


    @PostMapping("")
    public  ResultVO saveCarOwner(@RequestBody ClubOwnerPO clubOwnerPO) {
        System.out.println(clubOwnerPO.getChecks());
        return carOwnerContract.saveCarOwner(clubOwnerPO);
    }   //存储俱乐部车主信息



    @DeleteMapping("/{id}")
    public  ResultVO delCarOwner(@PathVariable Long id){
        return carOwnerContract.delCarOwner(id);
    }//删除车主信息


    @PutMapping("/{id}")
    public ResultVO modifyCarOwner(@PathVariable Long id,
                                   @RequestBody ClubOwnerPO clubOwnerPO){
        return carOwnerContract.modifyCarOwner(id, clubOwnerPO);
    } //修改车主信息


    @GetMapping("/{clubId}")
    public ResultVO searchCarOwners(@PathVariable Long clubId,Long id){
        return carOwnerContract.searchCarOwners(clubId,id);
    } //查询俱乐部车主信息列表


    @GetMapping("/auditPage")
    public  ResultVO clubOwnerJoinVerifPage (@RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                             @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                             Long clubId,Integer status){
        return carOwnerContract.clubOwnerJoinVerifPage(pageNo, pageSize, clubId, status,null);
    }        //车主认证审核列表


    @PutMapping("/audit/{id}")
    public  ResultVO reviewTheOwner (@PathVariable  Long id,
                                     @RequestParam Integer status,
                                     @RequestParam String content){
        return carOwnerContract.reviewTheOwner(id, status, content);
    }      //车主审核



}
