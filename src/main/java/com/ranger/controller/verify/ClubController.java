package com.ranger.controller.verify;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.club.contract.ClubContract;
import com.ranger.club.contract.ClubManageContract;
import com.ranger.club.enums.ClubJoinVerifyType;
import com.ranger.club.enums.ClubMemberType;
import com.ranger.club.po.ClubPO;
import com.ranger.club.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

/**
 * 俱乐部管理
 *
 * @author huipeng
 * @Title: ClubController
 * @ProjectName ranger-clubadmin
 * @Description: TODO
 * @date 2019/7/25下午2:42
 */
@RestController
@RequestMapping("/verify/club")
public class ClubController {


    @Reference(interfaceClass = ClubContract.class, timeout = 1200000)
    private ClubContract clubContract;

    @Reference(interfaceClass = ClubManageContract.class, timeout = 1200000)
    private ClubManageContract clubManageContract;

    /**
     * 查询俱乐部管理员  或者   俱乐部会员（默认查询会员）
     *
     * @param clubId   俱乐部id
     * @param type     俱乐部身份
     * @param pageNo   当前页码
     * @param pageSize 展示条数
     * @return
     */
    @GetMapping("/members/{clubId}")
    public ResultVO searchClubMember(@PathVariable Long clubId,
                                     @RequestParam ClubMemberType type,
                                     @RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                     String nickName) {
        return clubContract.searchClubManageOrMember(clubId, type, pageNo, pageSize, nickName);
    }


    /**
     * 后台查看加入俱乐部待审核用户 (ClubId有值查询聚体俱乐部，无值查询全部俱乐部)
     *
     * @param clubId             俱乐部ID
     * @param clubJoinVerifyType 加入俱乐部审核状态
     * @param pageNo             当前页码
     * @param pageSize           展示条数
     * @return
     */
    @GetMapping("/{clubId}/join")
    public ResultVO clubJoin(@PathVariable Long clubId,
                             @RequestParam ClubJoinVerifyType clubJoinVerifyType,
                             @RequestParam(required = false, defaultValue = "0") Integer pageNo,
                             @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return clubManageContract.clubJoinCheck(clubId, clubJoinVerifyType, pageNo, pageSize);
    }


    /**
     * 管理后台审核俱乐部会员加入
     *
     * @param clubId             俱乐部id
     * @param userId             用户id
     * @param clubJoinVerifyType 审批状态
     * @return
     */
    @PostMapping("/{clubId}/verify")
    public ResultVO clubMemberVerify(@PathVariable Long clubId,
                                     @RequestParam Long userId,
                                     @RequestParam ClubJoinVerifyType clubJoinVerifyType) {
        return clubManageContract.clubMemberReview(clubId, userId, clubJoinVerifyType);
    }


    /**
     * 退出俱乐部
     *
     * @param clubId 俱乐部ID
     * @param userId 用户ID
     * @return
     */
    @PostMapping("/{clubId}/quit/{userId}")
    public ResultVO quitClub(@PathVariable Long clubId, @PathVariable Long userId) {
        return clubContract.quitClub(clubId, userId);
    }


    /**
     * 俱乐部详情
     *
     * @param clubId 俱乐部ID
     * @return
     */
    @GetMapping("/{clubId}")
    public ResultVO clubInfo(@PathVariable Long clubId) {
        return clubContract.clubInfo(clubId, null);
    }


    /**
     * 修改俱乐部信息
     *
     * @param clubId 俱乐部id
     * @param clubPO 后台俱乐部信息维护po
     * @return
     */
    @PutMapping("/{clubId}")
    public ResultVO modifyClub(@PathVariable Long clubId, @RequestBody ClubPO clubPO) {
        clubPO.setClubId(clubId);
        return clubManageContract.modifyClub(clubId, clubPO);
    }
}
