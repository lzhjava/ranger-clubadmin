package com.ranger.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.equip.contract.SeniorMemberContrant;
import com.ranger.equip.dto.MemberApplicationDTO;
import com.ranger.equip.dto.SeniorMemberDTO;
import com.ranger.equip.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

/**    高级会员
 * @author huipeng
 * @Title: SeniorMemberContrant
 * @ProjectName ranger-equip-server
 * @Description: TODO
 * @date 2019/6/10下午5:07
 */
@RestController
@RequestMapping("/seniorMember")
public class SeniorMemberController {



    @Reference(interfaceClass = SeniorMemberContrant.class, timeout = 1200000)
    private SeniorMemberContrant seniorMemberContrant;

    /**  创建高级会员卡片
     * @param seniorMemberDTO    高级会员卡片交互DTO
     * @return
     */
    @PostMapping("")
    public ResultVO createSeniorMember(@RequestBody SeniorMemberDTO seniorMemberDTO){
             seniorMemberDTO.getStatus();
           return seniorMemberContrant.createSeniorMember(seniorMemberDTO);
    }


    /**  高级会员卡片获取
     * @param type
     * @param relationId
     * @return
     */
    @GetMapping("/{type}/{relationId}")
    public ResultVO SeniorMemberList(@PathVariable Integer type, @PathVariable Long relationId){
           return seniorMemberContrant.SeniorMemberList(type, relationId);

    }




    /**  会员卡申请列表
     * @param type
     * @param relationId
     * @return
     *//*
    @GetMapping("/memberList/{type}/{relationId}")
    public ResultVO applicationMemberList(@PathVariable Integer type, @PathVariable Long relationId){
        return seniorMemberContrant.applicationMemberList(type,relationId);

    }*/


    /**   会员审核
     * @param id
     * @param memberApplicationDTO
     * @return
     */
    @PutMapping("/verify/{id}")
    public ResultVO verifyApplication(@PathVariable Long id, @RequestBody MemberApplicationDTO memberApplicationDTO){
        return  seniorMemberContrant.verifyApplication(id, memberApplicationDTO);

    }


    /**   用户高级会员卡详情
     * @param id
     * @return
     */
    @GetMapping("/cardDetail/{id}")
    public ResultVO searchUserCard(@PathVariable Long id){
        return seniorMemberContrant.searchUserCard(id);

    }




    /**  高级会员卡片修改
     * @param seniorMemberDTO    高级会员卡片交互DTO
     * @return
     */
    @PutMapping("/{id}")
    public ResultVO updateSeniorMember(@PathVariable Long id, @RequestBody SeniorMemberDTO seniorMemberDTO){
        return seniorMemberContrant.updateSeniorMember(id, seniorMemberDTO);

    }

    /**  高级会员卡片删除
     * @param
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultVO deleteSeniorMember(@PathVariable Long id){
        return  seniorMemberContrant.deleteSeniorMember(id);
    }
}
