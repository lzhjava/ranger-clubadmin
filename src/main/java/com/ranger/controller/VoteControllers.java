package com.ranger.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.activity.contract.PostVotePartyContract;
import com.ranger.activity.contract.VoteContract;
import com.ranger.activity.dto.VoteDTO;
import com.ranger.activity.vo.VoteVO;
import com.ranger.club.contract.ClubContract;
import com.ranger.club.dto.ClubBaseDTO;
import com.ranger.model.VoteItem;
import com.ranger.statistics.contract.VoteStatisticsContract;
import com.ranger.statistics.vo.ResultVO;
import com.ranger.statistics.vo.VoteStatisticsVO;
import com.ranger.user.contract.UserInfoApi;
import com.ranger.user.vo.UserInfoVO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caiyanying on 17/5/23.
 */
@RestController
@RequestMapping("/exemption/vote")
public class VoteControllers {


    @Reference(interfaceClass = UserInfoApi.class, timeout = 1200000)
    private UserInfoApi userInfoApi;

    @Reference(interfaceClass = ClubContract.class, timeout = 1200000)
    private ClubContract clubContract;

    @Reference(interfaceClass = VoteStatisticsContract.class, timeout = 1200000)
    private VoteStatisticsContract voteStatisticsContract;


    @Reference(timeout = 1200000)
    private VoteContract voteContract;

    @Reference(timeout = 1200000)
    private PostVotePartyContract postVotePartyContract;

    /**
     * 投票新增
     *
     * @param userId 用户id
     * @return
     */
    @PostMapping("")
    public Object insertVote(@RequestHeader(value = "X-Consumer-Username", required = false) Long userId,
                             @RequestBody VoteDTO voteDTO) {

        UserInfoVO userInfoVO = userInfoApi.searchUserInfo(userId);
        if (userInfoVO ==null) {
            return  com.ranger.user.vo.ResultVO.USER_NOT_FOUND;
        }

        voteDTO.setUserId(userInfoVO.getUserId());
        voteDTO.setUserName(userInfoVO.getNickname());
        voteDTO.setUserLogo(userInfoVO.getAvatar());

        if (voteDTO.getClubId() == null) {
            return new com.ranger.user.vo.ResultVO("俱乐部id为空");
        }

        ClubBaseDTO clubBaseDTO = clubContract.searchClubDTO(voteDTO.getClubId());
        if (clubBaseDTO !=null) {
            return com.ranger.club.vo.ResultVO.CLUB_NOT_FOUND;
        }

        voteDTO.setClubName(clubBaseDTO.getClubName());
        voteDTO.setClubLogo(clubBaseDTO.getLogo());

        return voteContract.addVote(voteDTO);
    }

    /**
     * 投票修改
     *
     * @param userId 用户id
     * @return
     */
    @PutMapping("")
    public Object updateVote(@RequestHeader(value = "X-Consumer-Username", required = false) Long userId,
                             @RequestBody VoteDTO voteDTO) {
        UserInfoVO userInfoVO = userInfoApi.searchUserInfo(userId);
        if (userInfoVO ==null) {
            return  com.ranger.user.vo.ResultVO.USER_NOT_FOUND;
        }

        voteDTO.setUserId(userInfoVO.getUserId());
        voteDTO.setUserName(userInfoVO.getNickname());
        voteDTO.setUserLogo(userInfoVO.getAvatar());

        if (voteDTO.getClubId() == null) {
            return new com.ranger.user.vo.ResultVO("俱乐部id为空");
        }

        ClubBaseDTO clubBaseDTO = clubContract.searchClubDTO(voteDTO.getClubId());
        if (clubBaseDTO !=null) {
            return com.ranger.club.vo.ResultVO.CLUB_NOT_FOUND;
        }

        voteDTO.setClubName(clubBaseDTO.getClubName());
        voteDTO.setClubLogo(clubBaseDTO.getLogo());

        return voteContract.editVote(voteDTO);
    }

    /**
     * 投票删除
     *
     * @return
     */
    @DeleteMapping("/{voteId}")
    public Object deleteVote(@PathVariable Long voteId) {

        return voteContract.deleteVote(voteId);
    }

    /**
     * 投票详情查询
     *
     * @return
     */
    @GetMapping("/{voteId}")
    public Object queryVoteInfo(@PathVariable Long voteId) {

        com.ranger.vo.ResultVO<VoteVO> resultVO = voteContract.selectById(voteId);


        return resultVO;
    }


    /**
     * 投票详情查询
     *
     * @return
     */
    @PostMapping("/{voteId}/item")
    public Object addVoteItem(@PathVariable Long voteId, @RequestBody VoteItem voteItem) {

        return voteContract.addVoteItem(voteId, voteItem);
    }

    /**
     * 根据投票id和用户查询是否可以投票以及投票状态
     *
     * @param voteId
     * @param userId
     * @return
     */
    @GetMapping("/{voteId}/item")
    public ResultVO getVoteItem(@PathVariable Long voteId,
                                @RequestHeader(value = "X-Consumer-Username", required = false) Long userId) {
        List resultList = new ArrayList();
        /**
         * 获取投票统计情况
         */
        VoteStatisticsVO voteStatisticsVO = voteStatisticsContract.selectVoteState(voteId, userId).getBody();
        /**
         * 获取投票详情
         */
        VoteVO voteVO =voteContract.selectById(voteId).getBody();

        /**
         * 创建一个最终返回的map
         */
        Map resultMap = new HashMap();
        if (voteVO!=null){

            /**
             * 根据投票选项去拼接统计传过来的数量
             */
            /**
             * 遍历投票详情的选项,取投票的类型(是否带图)和图片
             */
            for (VoteItem voteItem: voteVO.getVoteItem()){
                Map map = new HashMap();
                map.put("voteItemId",voteItem.getVoteItemId());
                map.put("voteItemImage",voteItem.getVoteItemImage());
                map.put("voteItemStr",voteItem.getVoteItemStr());
                if (voteStatisticsVO !=null){
                    Map voteStatisticsMap = voteStatisticsVO.getVoteinfo();
                    /**
                     * 判断投票的每个选项是否有人投,如果返回来的是空就是没有人投票,默认给前台投票人数:0
                     */
                    if (voteStatisticsMap !=null && voteStatisticsMap.size()>0){
                        if (voteStatisticsMap.get(voteItem.getVoteItemId()) !=null){
                            map.put("voteItemNumber",voteStatisticsMap.get(voteItem.getVoteItemId()));
                        }else{
                            map.put("voteItemNumber",0);
                        }
                    }else{
                        map.put("voteItemNumber",0);
                    }
                }else{
                    resultMap.put("voteItemNumber",0);
                }
                resultList.add(map);
            }
            resultMap.put("total",voteStatisticsVO.getTotal());
            resultMap.put("optionTotal",voteStatisticsVO.getOptionTotal());
            resultMap.put("voteItemType",voteVO.getVoteItemType());
            resultMap.put("voteStatistics",resultList);
        }
        return new ResultVO(resultMap) ;

    }

}
