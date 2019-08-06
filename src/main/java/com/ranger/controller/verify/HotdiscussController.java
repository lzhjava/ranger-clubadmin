package com.ranger.controller.verify;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.activity.contract.ActivityContract;
import com.ranger.activity.contract.PostVotePartyContract;
import com.ranger.activity.enums.PostVotePartyType;
import com.ranger.activity.vo.PostVotePartyVO;
import com.ranger.advert.enums.FeedType;
import com.ranger.post.contract.PostContract;
import com.ranger.utils.Pager;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *
 * @author ssd
 * @create 2019-08-03 3:12 PM
 */
@RestController
@RequestMapping("/verify/hotdiscuss")
public class HotdiscussController {

    @Reference(timeout = 1200000)
    private PostVotePartyContract postVotePartyContract;

    @Reference(interfaceClass = ActivityContract.class, timeout = 1200000)
    private ActivityContract activityContract;

    @Reference(interfaceClass = PostContract.class, timeout = 1200000)
    private PostContract postContract;

    /**
     * 获取热议列表
     * @param clubId
     * @param title
     * @param top
     * @param type
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/{clubId}")
    public Object getClubHotDiscuss(@PathVariable Long clubId, String title, Boolean top, PostVotePartyType type,
                                    @RequestParam(value = "page", defaultValue = "0") Integer page,
                                    @RequestParam(value = "size", defaultValue = "12") Integer size) {


        com.ranger.vo.ResultVO<Pager> pagerResultVO = new com.ranger.vo.ResultVO<>();

        com.ranger.vo.ResultVO<Long> resultCountVO = postVotePartyContract.countData(clubId, title, top, type);

        if (resultCountVO.getCode() != 0) {
            pagerResultVO.setCode(resultCountVO.getCode());
            pagerResultVO.setMessage(resultCountVO.getMessage());
            return pagerResultVO;
        }
        if (resultCountVO.getBody() == 0) {
            pagerResultVO.setCode(resultCountVO.getCode());
            pagerResultVO.setMessage(resultCountVO.getMessage());
            pagerResultVO.setBody(new Pager(page, size, 0, new ArrayList()));
            return pagerResultVO;
        }

        com.ranger.vo.ResultVO<List<PostVotePartyVO>> listResultVO = postVotePartyContract.selectData(clubId, title, top, type, page, size);
        if (listResultVO.getCode() != 0) {
            pagerResultVO.setCode(listResultVO.getCode());
            pagerResultVO.setMessage(listResultVO.getMessage());
            pagerResultVO.setBody(new Pager(page, size, 0, new ArrayList()));
            return pagerResultVO;
        }

        pagerResultVO.setCode(0);
        pagerResultVO.setBody(new Pager(page, size, resultCountVO.getBody(), listResultVO.getBody()));
        return pagerResultVO;

    }

    @GetMapping("/{relationId}/getContent")
    public Object getClubHotDiscuss(@PathVariable Long relationId, FeedType feedType) {
        if (feedType == FeedType.POST){
          return  postContract.selectPostContentById(relationId);
        }else if (feedType == FeedType.EVENT){
            return activityContract.queryActivityContent(relationId);
        }
        return null;
    }

}
