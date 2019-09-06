package com.ranger.controller.verify;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.activity.contract.ActivityContract;
import com.ranger.activity.contract.PartyContract;
import com.ranger.activity.contract.VoteContract;
import com.ranger.activity.vo.ActivityVO;
import com.ranger.activity.vo.PartyVO;
import com.ranger.activity.vo.VoteVO;
import com.ranger.advert.contract.BannerContract;
import com.ranger.advert.enums.FeedType;
import com.ranger.advert.po.BannerPO;
import com.ranger.club.contract.ClubContract;
import com.ranger.club.dto.ClubBaseDTO;
import com.ranger.enums.FeedEnum;
import com.ranger.enums.PostType;
import com.ranger.post.contract.PostContract;
import com.ranger.post.vo.PostVO;
import com.ranger.utils.Pager;
import com.ranger.vo.DailyItemVO;
import com.ranger.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:banner
 *
 * @author ssd
 * @create 2019-07-26 11:17 AM
 */
@RestController
@RequestMapping("/verify/banner")
public class BannerController {

    @Reference(interfaceClass = BannerContract.class, timeout = 1200000)
    private BannerContract bannerContract;

    @Reference(timeout = 1200000)
    private PartyContract partyContract;

    @Reference(timeout = 1200000)
    private VoteContract voteContract;

    @Reference(interfaceClass = ClubContract.class, timeout = 1200000)
    private ClubContract clubContract;

    @Reference(interfaceClass = ActivityContract.class, timeout = 1200000)
    private ActivityContract activityContract;

    @Reference(interfaceClass = PostContract.class, timeout = 1200000)
    private PostContract postContract;


    /**
     * banner查询
     *
     * @param pageNo     当前页面
     * @param pageSize   展示条数
     * @param sortString 排序字段
     * @param present    是否显示
     * @return
     */
    @GetMapping("")
    public com.ranger.advert.vo.ResultVO queryAdvert(@RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                     @RequestParam(required = false, defaultValue = "sort") String sortString,
                                                     @RequestParam(required = false, defaultValue = "true") Boolean present,
                                                     BannerPO bannerPO) {
        return bannerContract.queryAdvert(pageNo, pageSize, sortString, present, bannerPO);
    }


    /**
     * 设置banner
     *
     * @param bannerPO banner前台交互po
     * @return
     */
    @PostMapping
    public com.ranger.advert.vo.ResultVO saveBanner(@RequestBody BannerPO bannerPO) {
        System.out.println(FeedType.ACTIVITY);
        return bannerContract.saveBanner(bannerPO);
    }


    /**
     * 删除banner
     *
     * @param bannerId bannerid
     * @return
     */
    @DeleteMapping("/{bannerId}")
    public com.ranger.advert.vo.ResultVO delBanner(@PathVariable Long bannerId) {
        return bannerContract.delBanner(bannerId);

    }


    /**
     * banner修改
     *
     * @param bannerId 要修改的bannerid
     * @param bannerPO 要修改的banner内容
     * @return
     */
    @PutMapping("/{bannerId}")
    public com.ranger.advert.vo.ResultVO modifyBanner(@PathVariable Long bannerId, @RequestBody BannerPO bannerPO) {
        return bannerContract.modifyBanner(bannerId, bannerPO);

    }


    /**
     * @param page
     * @param size
     * @param feedEnum
     * @param relateName
     * @param clubId
     * @return
     */
    @GetMapping("/dailyItemList")
    public Object list(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                       @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                       @RequestParam(value = "feedEnum", required = false) FeedEnum feedEnum,
                       @RequestParam(value = "relateName", required = false) String relateName,
                       @RequestParam(value = "clubId", required = false) Long clubId) {
        List resultList = new ArrayList();
        if (feedEnum == FeedEnum.POST) {
            com.ranger.vo.ResultVO<Long> countVO = postContract.countPostByKey(clubId, relateName);
            com.ranger.vo.ResultVO<List<PostVO>> resultVO = postContract.selectPostByKey(clubId, relateName, page, size);

            Long count = 0L;
            if (countVO.getCode() == 0) {
                count = countVO.getBody();
            }
            if (resultVO != null && resultVO.getCode() == 0) {
                List<PostVO> partyVOS = (List<PostVO>) resultVO.getBody();
                System.out.println("这里是查出来的帖子的条数=======" + count);
                if (partyVOS != null && partyVOS.size() > 0) {
                    for (PostVO postVO : partyVOS) {
                        DailyItemVO dailyItemVO = new DailyItemVO();
                        dailyItemVO.setFeedEnum(feedEnum);
                        dailyItemVO.setUserName(postVO.getUserInfo().getUserName());
                        dailyItemVO.setUserId(postVO.getUserInfo().getUserId());
                        dailyItemVO.setRelateId(postVO.getPostId());
                        if (postVO.getPostType() == PostType.LONGTEXT) {
                            if (postVO.getPostLongTextInfo().getCover() != null) {
                                dailyItemVO.setCover(postVO.getPostLongTextInfo().getCover().getUrl());
                                dailyItemVO.setTitle(postVO.getPostLongTextInfo().getTitle());
                            }
                        } else if (postVO.getPostType() == PostType.TEXT) {
                            if (postVO.getPostTextInfo() != null) {
                                if (!postVO.getPostTextInfo().getImages().isEmpty()) {
                                    dailyItemVO.setCover(postVO.getPostTextInfo().getImages().get(0).getUrl());
                                }
                                dailyItemVO.setTitle(postVO.getPostTextInfo().getText());
                            }

                        } else if (postVO.getPostType() == PostType.VIDEO) {
                            if (postVO.getPostVideoInfo() != null) {
                                if (postVO.getPostVideoInfo().getVideo() != null) {
                                    dailyItemVO.setCover(postVO.getPostVideoInfo().getVideo().getCover());
                                }
                                dailyItemVO.setTitle(postVO.getPostVideoInfo().getText());
                            }
                        }
                        resultList.add(dailyItemVO);
                    }
                }
            }
            return new ResultVO<>("", 0, new com.ranger.utils.Pager(page, size, count, resultList));
        } else if (feedEnum == FeedEnum.PARTY) {
            com.ranger.vo.ResultVO resultVO = partyContract.selectPartyByKey(clubId, relateName, page, size);

            com.ranger.vo.ResultVO<Long> countVO = partyContract.countPartyByKey(clubId, relateName);
            Long count = 0L;
            if (countVO.getCode() == 0) {
                count = countVO.getBody();
            }
            if (resultVO != null && resultVO.getCode() == 0) {
                List<PartyVO> partyVOS = (List<PartyVO>) resultVO.getBody();
                System.out.println("这里是查出来的攒局的条数=======" + partyVOS.size());
                if (partyVOS != null && partyVOS.size() > 0) {
                    for (PartyVO partyVO : partyVOS) {
                        DailyItemVO dailyItemVO = new DailyItemVO();
                        dailyItemVO.setFeedEnum(feedEnum);
                        dailyItemVO.setUserName(partyVO.getUserName());
                        dailyItemVO.setUserId(partyVO.getUserId());
                        dailyItemVO.setRelateId(partyVO.getPartyId());
                        dailyItemVO.setCover(partyVO.getBackground());
                        dailyItemVO.setTitle(partyVO.getName());
                        resultList.add(dailyItemVO);
                    }
                }
            }
            return new ResultVO<>("", 0, new com.ranger.utils.Pager(page, size, count, resultList));
        } else if (feedEnum == FeedEnum.VOTE) {
            com.ranger.vo.ResultVO resultVO = voteContract.selectVoteByKey(clubId, relateName, page, size);
            Long count = 0L;
            com.ranger.vo.ResultVO<Long> countVO = voteContract.countVoteByKey(clubId, relateName);
            if (countVO.getCode() == 0) {
                count = countVO.getBody();
            }
            if (resultVO != null && resultVO.getCode() == 0) {
                List<VoteVO> voteVOS = (List<VoteVO>) resultVO.getBody();
                System.out.println("这里是查出来的投票的总条数=======" + count);
                if (voteVOS != null && voteVOS.size() > 0) {
                    for (VoteVO voteVO : voteVOS) {
                        DailyItemVO dailyItemVO = new DailyItemVO();
                        dailyItemVO.setFeedEnum(feedEnum);
                        dailyItemVO.setUserName(voteVO.getUserName());
                        dailyItemVO.setUserId(voteVO.getUserId());
                        dailyItemVO.setRelateId(voteVO.getVoteId());
                        dailyItemVO.setCover(voteVO.getBackground());
                        dailyItemVO.setTitle(voteVO.getName());
                        resultList.add(dailyItemVO);
                    }
                }
            }
            return new ResultVO<>("", 0, new com.ranger.utils.Pager(page, size, count, resultList));
        } else if (feedEnum == FeedEnum.CLUB) {
            Pager<ClubBaseDTO> clubBaseDTOPager = clubContract.getClubPage(relateName, page, size);
            Long count = 0L;
            if (clubBaseDTOPager != null) {
                count = clubBaseDTOPager.getTotal().longValue();
                System.out.println("这里是查出来的俱乐部的总条数=======" + count);
                List<ClubBaseDTO> clubBaseDTOs = clubBaseDTOPager.getContent();
                if (clubBaseDTOs != null && clubBaseDTOs.size() > 0) {
                    for (ClubBaseDTO clubBaseDTO : clubBaseDTOs) {
                        DailyItemVO dailyItemVO = new DailyItemVO();
                        dailyItemVO.setFeedEnum(feedEnum);
                        /*dailyItemVO.setUserName(clubBaseDTO.);
                        dailyItemVO.setUserId(voteVO.getUserId());*/
                        dailyItemVO.setRelateId(clubBaseDTO.getClubId());
                        dailyItemVO.setCover(clubBaseDTO.getLogo());
                        dailyItemVO.setTitle(clubBaseDTO.getClubName());
                        resultList.add(dailyItemVO);
                    }
                }
            }
            return new ResultVO<>("", 0, new com.ranger.utils.Pager(page, size, count, resultList));
        } else if (feedEnum == FeedEnum.ACTIVITY) {
            com.ranger.activity.vo.ResultVO resultVO = activityContract.selectActivityByKey(clubId, relateName, page, size);
            Long count = 0L;
            com.ranger.activity.vo.ResultVO<Long> countVO = activityContract.countActivityByKey(clubId, relateName);
            if (countVO.getCode() == 0) {
                count = countVO.getBody();
            }
            if (resultVO != null && resultVO.getCode() == 0) {
                List<ActivityVO> activityVOS = (List<ActivityVO>) resultVO.getBody();
                System.out.println("这里是查出来的活动的总条数=======" + count);
                if (activityVOS != null && activityVOS.size() > 0) {
                    for (ActivityVO activityVO : activityVOS) {
                        DailyItemVO dailyItemVO = new DailyItemVO();
                        dailyItemVO.setFeedEnum(feedEnum);
                        /*dailyItemVO.setUserName(activityVO.);
                        dailyItemVO.setUserId(activityVO.getUserId());*/
                        dailyItemVO.setRelateId(activityVO.getActivityId());
                        if (activityVO.getCoverImg() != null) {
                            dailyItemVO.setCover(activityVO.getCoverImg().getUrl());
                        }
                        dailyItemVO.setTitle(activityVO.getActivityName());
                        resultList.add(dailyItemVO);
                    }
                }
            }
            return new ResultVO<>("", 0, new com.ranger.utils.Pager(page, size, count, resultList));
        } else {
            return new ResultVO<>("无此类型数据", 0, null);
        }
    }
}
