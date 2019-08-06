package com.ranger.controller.verify;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.activity.contract.PartyContract;
import com.ranger.activity.contract.PostVotePartyContract;
import com.ranger.activity.dto.PartyDTO;
import com.ranger.activity.vo.PartyVO;
import com.ranger.club.contract.ClubContract;
import com.ranger.club.dto.ClubBaseDTO;
import com.ranger.enums.PartyType;
import com.ranger.statistics.contract.PartyStatisticsContract;
import com.ranger.statistics.contract.VoteStatisticsContract;
import com.ranger.statistics.vo.PartyPeopleListVO;
import com.ranger.statistics.vo.ResultVO;
import com.ranger.user.contract.UserInfoApi;
import com.ranger.user.vo.UserInfoVO;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by caiyanying on 17/5/23.
 */
@RestController
@RequestMapping("/verify/party")
public class PartyControllers {

    @Reference(interfaceClass = PartyStatisticsContract.class, timeout = 1200000)
    private PartyStatisticsContract partyStatisticsContract;

    @Reference(interfaceClass = VoteStatisticsContract.class, timeout = 1200000)
    private VoteStatisticsContract voteStatisticsContract;

    @Reference(interfaceClass = UserInfoApi.class, timeout = 1200000)
    private UserInfoApi userInfoApi;

    @Reference(interfaceClass = ClubContract.class, timeout = 1200000)
    private ClubContract clubContract;


    @Reference(timeout = 1200000)
    private PartyContract partyContract;

    @Reference(timeout = 1200000)
    private PostVotePartyContract postVotePartyContract;


    /**
     * 攒局新增
     *
     * @param userId 用户id
     * @return
     */
    @PostMapping("")
    public Object insertParty(@RequestHeader(value = "X-Consumer-Username", required = false,defaultValue ="38617") Long userId,
                              @RequestBody PartyDTO partyDTO) {
        UserInfoVO userInfoVO = userInfoApi.searchUserInfo(38617L);
        if (userInfoVO ==null) {
            return  com.ranger.user.vo.ResultVO.USER_NOT_FOUND;
        }

        partyDTO.setUserId(userInfoVO.getUserId());
        partyDTO.setUserName(userInfoVO.getNickname());
        partyDTO.setUserLogo(userInfoVO.getAvatar());

        if (partyDTO.getClubId() == null) {
            return new com.ranger.user.vo.ResultVO("俱乐部id为空");
        }

        ClubBaseDTO clubBaseDTO = clubContract.searchClubDTO(partyDTO.getClubId());
        if (clubBaseDTO ==null) {
            return com.ranger.club.vo.ResultVO.CLUB_NOT_FOUND;
        }

        partyDTO.setClubName(clubBaseDTO.getClubName());
        partyDTO.setClubLogo(clubBaseDTO.getLogo());

        return partyContract.addParty(partyDTO);
    }

    /**
     * 攒局修改
     *
     * @param userId 用户id
     * @return
     */
    @PutMapping("")
    public Object updateParty(@RequestHeader(value = "X-Consumer-Username", required = false) Long userId,
                              @RequestBody PartyDTO partyDTO) {
        UserInfoVO userInfoVO = userInfoApi.searchUserInfo(38617L);
        if (userInfoVO ==null) {
            return  com.ranger.user.vo.ResultVO.USER_NOT_FOUND;
        }

        partyDTO.setUserId(userInfoVO.getUserId());
        partyDTO.setUserName(userInfoVO.getNickname());
        partyDTO.setUserLogo(userInfoVO.getAvatar());

        if (partyDTO.getClubId() == null) {
            return new com.ranger.user.vo.ResultVO("俱乐部id为空");
        }

        ClubBaseDTO clubBaseDTO = clubContract.searchClubDTO(partyDTO.getClubId());
        if (clubBaseDTO ==null) {
            return com.ranger.club.vo.ResultVO.CLUB_NOT_FOUND;
        }

        partyDTO.setClubName(clubBaseDTO.getClubName());
        partyDTO.setClubLogo(clubBaseDTO.getLogo());

        return partyContract.editParty(partyDTO);
    }

    /**
     * 攒局删除
     *
     * @return
     */
    @DeleteMapping("/{partyId}")
    public Object deleteParty(@PathVariable Long partyId) {

        return partyContract.deleteParty(partyId);
    }

    /**
     * 攒局详情查询
     *
     * @return
     */
    @GetMapping("/{partyId}")
    public Object queryPartyInfo(@PathVariable Long partyId) {

        com.ranger.vo.ResultVO<PartyVO> resultVO = partyContract.selectById(partyId);


        return resultVO;
    }

    /**
     * 根据攒局id,用户id,返回攒局情况(选择时间)
     *
     * @param partyId 攒局id
     * @param userId  用户id
     * @return
     */
    @GetMapping("/{partyId}/discuss")
    public ResultVO selectPartyDiscuss(@PathVariable Long partyId,
                                       @RequestHeader(value = "X-Consumer-Username", required = false) Long userId) {
        return partyStatisticsContract.selectNumber(partyId, userId);

    }

    /**
     * 根据攒局id,日期,返回当天报名的人数列表
     *
     * @param partyId 攒局id
     * @param partyId 攒局id
     * @return date 日期
     */
    @GetMapping("/{partyId}/discuss/peopleList")
    public ResultVO selectDiscussPeopleList(@PathVariable Long partyId,
                                            @RequestParam(value = "date", required = true) String date) {
        List<Long> userIds = partyStatisticsContract.selectPeopleList(partyId, date).getBody();
        if (userIds != null && userIds.size() > 0) {
            List<UserInfoVO> userInfoVOS = userInfoApi.selectUserInfos(userIds);
            return new ResultVO(userInfoVOS);
        } else {
            return new ResultVO("无用户报名", 1, userIds);
        }
    }


    /**
     * 根据攒局id,攒局每天的报名人员列表
     *
     * @param partyId 攒局id
     * @param partyId 攒局id
     * @return date 日期
     */
    @GetMapping("/{partyId}/discuss/dateList")
    public ResultVO selectDiscussDateList(@PathVariable Long partyId,
                                          @RequestHeader(value = "X-Consumer-Username", required = false) Long userId) {

        /**
         * 先查询该攒局是立即报名的还是选择时间的
         */
        com.ranger.vo.ResultVO<PartyVO> resultVO = partyContract.selectById(partyId);
        if (resultVO != null) {
            PartyVO partyVO = resultVO.getBody();
            if (partyVO != null) {
                /**
                 * 如果是选择时间的就按照选择时间的指定格式返回给前台
                 */
                if (partyVO.getPartyType() == PartyType.DISCUSS){
                    Map resultMap = new HashMap();
                    Set userIds = new HashSet();
                    Map<String, List<Long>> map = new HashMap<String, List<Long>>();
                    /**
                     * 拿到每天的报名人员id,map格式为:key:日期,value:人员ids
                     */
                    map = partyStatisticsContract.selectDateAndPeopleList(partyId).getBody();
                    //map.values().stream().flatMap(longs -> )

                    /**
                     * 把每天的人员id拿到一起,组成一个list(这样可以保证只从用户表中拿一次数据)
                     */
                    if (map != null && map.size() > 0) {
                        for (List<Long> list : map.values()) {
                            for (Long i : list) {
                                userIds.add(i);
                            }
                        }
                    }
                    List<Long> userIdList = new ArrayList<>(userIds);
                    if (userIdList != null && userIdList.size() > 0) {
                        Map<Long, UserInfoVO> userInfoMap = new HashMap<>();
                        List<UserInfoVO> UserInfoVOList = userInfoApi.selectUserInfos(userIdList);
                        /**
                         * 拿到用户信息,与用户id对应, userInfoMap格式为:key:用户id,value:用户信息
                         */
                        for (UserInfoVO userInfoVO : UserInfoVOList) {
                            userInfoMap.put(userInfoVO.getUserId(), userInfoVO);
                        }

                        /**
                         * 这里是去组建一个key是日期,value是用户基本信息的List
                         */
                        if (map != null && map.size() > 0) {
                            for (String date : map.keySet()) {
                                List resultList = new ArrayList();
                                List<Long> list = map.get(date);
                                for (Long i : list) {
                                    Map hashMap = new HashMap();
                                    if (userInfoMap.get(i) != null) {
                                        hashMap.put("nickname", userInfoMap.get(i).getNickname());
                                        hashMap.put("background", userInfoMap.get(i).getBackground());
                                        hashMap.put("total", list.size());
                                    }
                                    resultList.add(hashMap);
                                }
                                resultMap.put(date, resultList);
                            }

                        }
                        /**
                         * 给最后的map进行排序,按照日期正序排序
                         */
                        LinkedHashMap endMap = new LinkedHashMap();;
                        if (resultMap != null && resultMap.size()>0){
                            Set<String> keys = resultMap.keySet();
                            List<String> list = new ArrayList<String>(keys);
                            Collections.sort(list);

                            for (String key: list){
                                endMap.put(key,resultMap.get(key));
                            }
                        }

                        return new ResultVO(endMap);
                    } else {
                        return new ResultVO("无用户报名", 0, resultMap);
                    }
                    /**
                     * 如果类型为立即报名,则按照指定格式返回给前台
                     */
                }else if(partyVO.getPartyType() == PartyType.JOIN){
                    PartyPeopleListVO partyPeopleListVO = partyStatisticsContract.selectState(partyId, userId).getBody();
                    List<Long> userIds = null;

                    if (partyPeopleListVO != null) {
                        if (partyPeopleListVO.getPeopleList() != null && partyPeopleListVO.getPeopleList().size() > 0) {
                            userIds = partyPeopleListVO.getPeopleList();
                        }
                    }
                    Map resultMap = new HashMap();
                    /**
                     * resultMap格式为:key:开始日期~结束日期,value:用户信息列表
                     */
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    List<UserInfoVO> userInfoVOS = userInfoApi.selectUserInfos(userIds);
                    if (userInfoVOS ==null){
                        userInfoVOS = new ArrayList<>();
                    }
                    resultMap.put(simpleDateFormat.format(partyVO.getStartTime()).toString()+" ~ "+simpleDateFormat.format(partyVO.getEndTime()).toString(),userInfoVOS);
                    return new ResultVO(resultMap);
                }
            }
        }

        return new ResultVO("无此攒局",190100,null);
    }

    /**
     * 根据攒局id,用户id,返回攒局情况(立即报名)
     *
     * @param partyId 攒局id
     * @param userId  用户id
     * @return
     */
    @GetMapping("/{partyId}/join")
    public ResultVO selectPartyjoin(@PathVariable Long partyId,
                                    @RequestHeader(value = "X-Consumer-Username", required = false) Long userId) {
        PartyPeopleListVO partyPeopleListVO = partyStatisticsContract.selectState(partyId, userId).getBody();
        List<Long> userIds = null;

        if (partyPeopleListVO != null) {
            if (partyPeopleListVO.getPeopleList() != null && partyPeopleListVO.getPeopleList().size() > 0) {
                userIds = partyPeopleListVO.getPeopleList();
            }
        }
        Map resultMap = new HashMap();
        resultMap.put("partyState", partyPeopleListVO.getPartyState());
        resultMap.put("peopleList", userInfoApi.selectUserInfos(userIds));
        return new ResultVO(resultMap);
    }


}
