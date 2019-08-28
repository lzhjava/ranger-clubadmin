package com.ranger.controller.verify;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.activity.contract.ActivityContract;
import com.ranger.activity.dto.*;
import com.ranger.activity.vo.ActivityVO;
import com.ranger.activity.vo.ResultVO;
import com.ranger.advert.contract.ClubActivityTypeContract;
import com.ranger.audit.contract.ActivityAuditContract;
import com.ranger.photo.contract.PhotoContract;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *
 * @author ssd
 * @create 2019-07-26 2:33 PM
 */
@RestController
@RequestMapping("/verify/activity")
public class ActivityController {

    @Reference(interfaceClass = ActivityContract.class, timeout = 1200000)
    private ActivityContract activityContract;

    @Reference(interfaceClass = ActivityAuditContract.class, timeout = 1200000)
    private ActivityAuditContract activityAuditContract;

    @Reference(interfaceClass = ClubActivityTypeContract.class, timeout = 1200000)
    private ClubActivityTypeContract activityTypeContract;

    @Reference(interfaceClass = PhotoContract.class, timeout = 1200000)
    private PhotoContract photoContract;

    /**
     * 获取活动类别列表
     *
     * @return
     */
    @GetMapping("/activityType")
    public ResultVO getActivityType() {
        return activityContract.selectActivityTypeList();
    }

    /**
     * 俱乐部创建活动
     *
     * @param activityDTO   活动对象
     * @param draftOrFormal
     * @return
     */
    @PostMapping("")
    public ResultVO saveActivity(@RequestBody ActivityDTO activityDTO, boolean draftOrFormal) {

        ResultVO<ActivityVO> activityVOResultVO = null;
        if (draftOrFormal) {
            activityVOResultVO = activityContract.addActivity(activityDTO);

        } else {
            activityVOResultVO = activityContract.addActivityDraft(activityDTO);
        }

        if (activityVOResultVO != null) {
            if (activityVOResultVO.getCode().equals(0)) {
                ActivityVO activityVO = activityVOResultVO.getBody();
                photoContract.createAlbm(activityVO.getActivityId(), null, "默认相册", 3);
                System.out.println("活动相册创建成功关联id为+++" + activityVO.getActivityId());
            }
        }
        return activityVOResultVO;
    }

    /**
     * 俱乐部修改活动
     *
     * @param activityDTO   活动对象
     * @param draftOrFormal
     * @return
     */
    @PutMapping("")
    public ResultVO updateActivity(@RequestBody ActivityDTO activityDTO, boolean draftOrFormal) {

        return activityContract.editActivityDraft(activityDTO, draftOrFormal);
    }

    /**
     * 俱乐部活动详情
     *
     * @param activityId
     * @param draftOrFormal
     * @return
     */
    @GetMapping("/{activityId}")
    public ResultVO selectActivity(@PathVariable Long activityId, boolean draftOrFormal) {
        if (draftOrFormal) {
            return activityContract.selectById(activityId);
        } else {
            return activityContract.selectDraftById(activityId);
        }
    }

    /**
     * 活动删除
     *
     * @param activityId
     * @return
     */
    @DeleteMapping("/{activityId}")
    public ResultVO selectActivity(@PathVariable Long activityId) {
        return activityContract.deleteActivityDraft(activityId);
    }

    /**
     * 俱乐部活动列表
     *
     * @param draftOrFormal
     * @return
     */
    @GetMapping("/activityList")
    public ResultVO selectActivityList(@RequestParam(value = "draftOrFormal", required = true) boolean draftOrFormal,
                                       Long activityId, Long clubId, String keyword, Long startTime,
                                       Long endTime, String sortBy, String order,
                                       @RequestParam(value = "page", defaultValue = "0") Integer page,
                                       @RequestParam(value = "size", defaultValue = "12") Integer size) {
        Long count = 0L;
        List resultList = new ArrayList();
        if (draftOrFormal) {
            ResultVO<Long> resultVO = activityContract.countActivity(activityId, clubId, keyword, null, startTime, endTime);
            if (resultVO != null) {
                if (resultVO.getBody() != null) {
                    count = resultVO.getBody();
                }
            }
            ResultVO<List<ActivityVO>> activityVOS = activityContract.selectActivity(activityId, clubId, keyword, null, page, size, startTime, endTime, sortBy, order);
            if (activityVOS != null) {
                resultList = activityVOS.getBody();
            }
        } else {
            ResultVO<Long> resultVO = activityContract.countActivityDraft(activityId, clubId, keyword, null, startTime, endTime);
            if (resultVO != null) {
                if (resultVO.getBody() != null) {
                    count = resultVO.getBody();
                }
            }
            ResultVO<List<ActivityVO>> activityVOS = activityContract.selectActivityDraft(activityId, clubId, keyword, null, page, size, startTime, endTime, sortBy, order);
            if (activityVOS != null) {
                resultList = activityVOS.getBody();
            }
        }
        return new ResultVO("", 0, new com.ranger.utils.Pager(page, size, count, resultList));
    }


    /**
     * 活动草稿列表发布成为正式活动
     *
     * @param activityId
     * @return
     */
    @PostMapping("{activityId}/draftListPublish")
    public ResultVO draftListPublish(@PathVariable Long activityId) {
        return activityContract.draftListPublish(activityId);
    }

    /**
     * 正式活动撤销成为草稿活动
     *
     * @param activityId
     * @return
     */
    @PostMapping("{activityId}/revokePublish")
    public ResultVO revokePublish(@PathVariable Long activityId) {
        return activityContract.revokePublish(activityId);
    }

    /**
     * 活动添加阶段
     *
     * @param activityStageDTO
     * @return
     */
    @PostMapping("/addActivityStage")
    public ResultVO addActivityStage(@RequestBody ActivityStageDTO activityStageDTO) {
        return activityContract.addActivityStage(activityStageDTO);
    }

    /**
     * 活动修改阶段
     *
     * @param activityStageDTO
     * @return
     */
    @PutMapping("/updateActivityStage")
    public ResultVO updateActivityStage(@RequestBody ActivityStageDTO activityStageDTO) {
        return activityContract.editActivityStage(activityStageDTO);
    }

    /**
     * 删除活动阶段
     *
     * @param activityStageId
     * @return
     */
    @DeleteMapping("/{activityStageId}/deleteActivityStage")
    public ResultVO updateActivityStage(@PathVariable Long activityStageId) {
        return activityContract.deleteActivityStage(activityStageId);
    }

    /**
     * 获取活动阶段
     *
     * @param activityId
     * @return
     */
    @GetMapping("/{activityId}/getActivityStage")
    public ResultVO getActivityStage(@PathVariable Long activityId) {
        return activityContract.getActivityStage(activityId);
    }


    /**
     * 活动添加分组
     *
     * @param activityGroupDTO
     * @return
     */
    @PostMapping("/addActivityGroup")
    public ResultVO addActivityGroup(@RequestBody ActivityGroupDTO activityGroupDTO) {
        return activityContract.addActivityGroup(activityGroupDTO);
    }

    /**
     * 活动修改分组
     *
     * @param activityGroupDTO
     * @return
     */
    @PutMapping("/editActivityGroup")
    public ResultVO editActivityGroup(@RequestBody ActivityGroupDTO activityGroupDTO) {
        return activityContract.editActivityGroup(activityGroupDTO);
    }

    /**
     * 获取活动分组
     *
     * @param activityId
     * @param activityStageId
     * @return
     */
    @GetMapping("/{activityId}/{activityStageId}")
    public ResultVO editActivityGroup(@PathVariable Long activityId,
                                      @PathVariable Long activityStageId) {
        return activityContract.getActivityGroup(activityId, activityStageId);
    }

    /**
     * 删除活动分组
     *
     * @param activityGroupId
     * @return
     */
    @DeleteMapping("/{activityGroupId}/deleteActivityGroup")
    public ResultVO deleteActivityStage(@PathVariable Long activityGroupId) {
        return activityContract.deleteActivityGroup(activityGroupId);
    }

    /**
     * 活动项目新增
     *
     * @param activityGoodDTO
     * @return
     */
    @PostMapping("/addActivityGood")
    public ResultVO addActivityGood(@RequestBody ActivityGoodDTO activityGoodDTO) {
        return activityContract.addActivityGood(activityGoodDTO);
    }

    /**
     * 活动项目修改
     *
     * @param activityGoodDTO
     * @return
     */
    @PutMapping("/editActivityGood")
    public ResultVO editActivityGood(@RequestBody ActivityGoodDTO activityGoodDTO) {
        return activityContract.editActivityGood(activityGoodDTO);
    }

    /**
     * 获取所有活动项目
     *
     * @param activityId
     * @param activityStageId
     * @param activityGroupId
     * @return
     */
    @GetMapping("/{activityId}/{activityStageId}/{activityGroupId}/getActivityGood")
    public ResultVO editActivityGood(@PathVariable Long activityId,
                                     @PathVariable Long activityStageId,
                                     @PathVariable Long activityGroupId) {
        return activityContract.getActivityGood(activityId, activityStageId, activityGroupId);
    }

    /**
     * 活动项目删除
     *
     * @param goodId
     * @return
     */
    @DeleteMapping("/{goodId}/deleteActivityGood")
    public ResultVO editActivityGood(@PathVariable Long goodId) {
        return activityContract.deleteActivityGood(goodId);
    }

    /**
     * 获取活动所有的阶段,分组,项目
     *
     * @param activityId
     * @return
     */
    @GetMapping("/{activityId}/getStageAndGroupAndGood")
    public ResultVO getStageAndGroupAndGood(@PathVariable Long activityId) {
        return activityContract.getStageAndGroupAndGood(activityId, null);
    }

    /**
     * 活动问答新增
     *
     * @param activityQADTO
     * @return
     */
    @PostMapping("/addQA")
    public ResultVO addActivityQA(@RequestBody ActivityQADTO activityQADTO) {
        return activityContract.addActivityQA(activityQADTO);
    }

    /**
     * 活动问答修改
     *
     * @param activityQADTO
     * @return
     */
    @PutMapping("/{activityQAId}/updateQA")
    public ResultVO updateActivityQA(@PathVariable Long activityQAId,
                                     @RequestBody ActivityQADTO activityQADTO) {
        return activityContract.updateActivityQA(activityQAId, activityQADTO);
    }

    /**
     * 活动问答删除
     *
     * @param activityQAId
     * @return
     */
    @DeleteMapping("/{activityQAId}/deleteQA")
    public ResultVO addActivityQA(@PathVariable Long activityQAId) {
        return activityContract.deleteActivityQA(activityQAId);
    }

    /**
     * 活动问答删除
     *
     * @param activityId
     * @return
     */
    @GetMapping("/{activityId}/selectQAs")
    public ResultVO selectActivityQAS(@PathVariable Long activityId) {
        return activityContract.selectActivityQAS(activityId);
    }

    /**
     * 活动置顶
     *
     * @param activityId
     * @return
     */
    @PutMapping("/{activityId}/topActivity")
    public ResultVO topActivity(@PathVariable Long activityId) {
        return activityContract.topActivity(activityId);
    }

    /**
     * 活动取消置顶
     *
     * @param activityId
     * @return
     */
    @PutMapping("/{activityId}/untopActivity")
    public ResultVO untopActivity(@PathVariable Long activityId) {
        return activityContract.untopActivity(activityId);
    }
}
