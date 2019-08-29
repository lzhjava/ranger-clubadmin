package com.ranger.controller.verify;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.activity.contract.PostVotePartyContract;
import com.ranger.activity.dto.PostVotePartyDTO;
import com.ranger.activity.enums.PostVotePartyType;
import com.ranger.model.UserInfo;
import com.ranger.post.contract.PostContract;
import com.ranger.post.dto.PostDTO;
import com.ranger.post.dto.PostSearchDTO;
import com.ranger.post.vo.PostVO;
import com.ranger.utils.Pager;
import com.ranger.vo.ResultVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *
 * @author ssd
 * @create 2019-08-03 2:30 PM
 */
@RestController
@RequestMapping("/verify/post")
public class PostController {

    @Reference(interfaceClass = PostContract.class, timeout = 1200000)
    private PostContract postContract;

    @Reference(interfaceClass = PostVotePartyContract.class, timeout = 1200000)
    private PostVotePartyContract postVotePartyContract;


    /**
     * 新增帖子
     *
     * @param postDTO
     * @return
     */
    @PostMapping("")
    public ResultVO addPost(@RequestBody PostDTO postDTO,
                            @RequestHeader(value = "userId", required = false) Long userId) {

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        postDTO.setUserInfo(userInfo);
        ResultVO resultVO = postContract.addPost(postDTO);
        saveHotdiscuss(resultVO);
        return resultVO;

    }

    /**
     * 将新增或者修改的帖子存入到热议表中
     * @param resultVO
     */
    public void saveHotdiscuss(ResultVO resultVO) {
        if (resultVO != null) {
            if (resultVO.getCode().equals(0)) {
                PostVO postVO = (PostVO) resultVO.getBody();
                PostVotePartyDTO postVotePartyDTO = new PostVotePartyDTO();
                postVotePartyDTO.setClubInfo(postVO.getClubInfo());
                postVotePartyDTO.setPostLongTextInfo(postVO.getPostLongTextInfo());
                postVotePartyDTO.setPostTextInfo(postVO.getPostTextInfo());
                postVotePartyDTO.setPostType(postVO.getPostType());
                postVotePartyDTO.setPostVideoInfo(postVO.getPostVideoInfo());
                postVotePartyDTO.setCreateAt(postVO.getCreateAt());
                postVotePartyDTO.setRelateId(postVO.getPostId());
                postVotePartyDTO.setUserInfo(postVO.getUserInfo());
                postVotePartyDTO.setRelateType(PostVotePartyType.POST);
                postVotePartyDTO.setTags(postVO.getTags());
                postVotePartyContract.saveData(postVotePartyDTO);
            }
        }
    }

    /**
     * 帖子修改
     *
     * @param postDTO
     * @return
     */
    @PutMapping("")
    public ResultVO editPost(@RequestBody PostDTO postDTO) {

        ResultVO resultVO = postContract.editPost(postDTO);
        saveHotdiscuss(resultVO);
        return resultVO;

    }

    /**
     * 删除帖子
     *
     * @param postId
     * @param reason
     * @return
     */
    @DeleteMapping("/{postId}")
    public ResultVO deletePost(@PathVariable Long postId, String reason) {

        ResultVO resultVO = postContract.deletePost(postId, reason);

        return resultVO;

    }


//    @PostMapping("/{postId}/restore")
//    public ResultVO restorePost(@PathVariable Long postId) {
//
//        ResultVO resultVO = postContract.restorePost(postId);
//
//        return resultVO;
//
//    }

    @GetMapping("")
    public ResultVO searchList(@Validated PostSearchDTO postSearchDTO, Integer page, Integer size) {

        ResultVO<Long> resultVO = postContract.countPost(postSearchDTO);
        if (resultVO.getCode() != 0) {
            return resultVO;
        }
        if (resultVO.getBody() == 0) {
            // 如果一条数据没有。。。就直接返回空数据
            return new ResultVO<>(new Pager(page, size, 0, new ArrayList<>()));
        }

        ResultVO<List<PostVO>> listResultVO = postContract.selectPost(postSearchDTO, page, size);
        if (listResultVO.getCode() != 0) {
            return listResultVO;
        }

        com.ranger.vo.ResultVO<Pager> pagerResultVO = new com.ranger.vo.ResultVO<>();
        pagerResultVO.setCode(0);
        pagerResultVO.setBody(new Pager(page, size, resultVO.getBody(), listResultVO.getBody()));
        return pagerResultVO;

    }


    @GetMapping("/{postId}")
    public ResultVO searchPost(@PathVariable Long postId) {

        ResultVO resultVO = postContract.selectPostById(postId);

        return resultVO;
    }
}
