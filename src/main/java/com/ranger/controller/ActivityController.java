package com.ranger.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.activity.contract.ActivityContract;
import com.ranger.activity.dto.ActivityDTO;
import com.ranger.activity.dto.ActivityStageDTO;
import com.ranger.activity.vo.ResultVO;
import com.ranger.audit.contract.ActivityAuditContract;
import org.springframework.web.bind.annotation.*;

/**
 * 描述:
 *
 * @author ssd
 * @create 2019-07-26 2:33 PM
 */
@RestController
@RequestMapping("/exemption/activity")
public class ActivityController {

    @Reference(interfaceClass = ActivityContract.class, timeout = 1200000)
    private ActivityContract activityContract;

    @Reference(interfaceClass = ActivityAuditContract.class, timeout = 1200000)
    private ActivityAuditContract activityAuditContract;

    /**
     * 俱乐部创建活动
     * @param activityDTO     活动对象
     * @param draftOrFormal
     * @return
     */
    @PostMapping
    public ResultVO saveActivity(@RequestBody ActivityDTO activityDTO, boolean draftOrFormal) {
        if (draftOrFormal){
            return  activityContract.addActivity(activityDTO);
        }else{
            return activityContract.addActivityDraft(activityDTO);
        }
    }

    /**
     * 俱乐部修改活动
     * @param activityDTO     活动对象
     * @param draftOrFormal
     * @return
     */
    @PutMapping
    public ResultVO updateActivity(@RequestBody ActivityDTO activityDTO, boolean draftOrFormal) {
        if (draftOrFormal){
            return  activityContract.editActivity(activityDTO);
        }else{
            return activityContract.editActivityDraft(activityDTO);
        }
    }

    /**
     * 俱乐部活动详情
     * @param activityId
     * @param draftOrFormal
     * @return
     */
    @GetMapping("{activityId}")
    public ResultVO selectActivity(@PathVariable Long activityId, boolean draftOrFormal) {
        if (draftOrFormal){
            return  activityContract.selectById(activityId);
        }else{
            return activityContract.selectDraftById(activityId);
        }
    }

    /**
     * 俱乐部活动详情
     * @param activityId
     * @param draftOrFormal
     * @return
     */
    @GetMapping("/activityList")
    public ResultVO selectActivityList(@PathVariable Long activityId,
                                       @RequestParam(value = "draftOrFormal", required = true)boolean draftOrFormal,
                                       String Keyword, Long clubId,
                                       @RequestParam(value = "page", defaultValue = "0")Integer page,
                                       @RequestParam(value = "size", defaultValue = "12")Integer size) {
        if (draftOrFormal){
            return  activityContract.selectActivityByKey(clubId, Keyword, page, size);
        }else{
            return activityContract.selectActivityDraft(null, clubId, Keyword, null, page, size);
        }
    }


    /**
     * 活动草稿详情页面发布成为正式活动
     * @param activityDTO
     * @param activityId
     * @return
     */
    @PostMapping("{activityId}/draftInfoPublish")
    public ResultVO draftInfoPublish(@RequestBody ActivityDTO activityDTO,
                                     @PathVariable Long activityId) {
        return activityContract.draftInfoPublish(activityDTO, activityId);
    }

    /**
     * 活动草稿列表发布成为正式活动
     * @param activityId
     * @return
     */
    @PostMapping("{activityId}/draftListPublish")
    public ResultVO draftListPublish(@PathVariable Long activityId) {
        return activityContract.draftListPublish(activityId);
    }

    /**
     * 正式活动撤销成为草稿活动
     * @param activityId
     * @return
     */
    @PostMapping("{activityId}/revokePublish")
    public ResultVO revokePublish(@PathVariable Long activityId) {
        return activityContract.revokePublish(activityId);
    }

    /**
     *  活动添加阶段
     * @param activityStageDTO
     * @return
     */
    @PostMapping("/addActivityStage")
    public ResultVO addActivityStage(@RequestBody ActivityStageDTO activityStageDTO) {
        return activityContract.addActivityStage(activityStageDTO);
    }

    /**
     *  活动修改阶段
     * @param activityStageDTO
     * @return
     */
    @PutMapping("/updateActivityStage")
    public ResultVO updateActivityStage(@RequestBody ActivityStageDTO activityStageDTO) {
        return activityContract.editActivityStage(activityStageDTO);
    }

    /**
     * 正式活动撤销成为草稿活动
     * @param activityId
     * @return
     */
    @GetMapping("{activityId}/getActivityStage")
    public ResultVO getActivityStage(@PathVariable Long activityId) {
        return activityContract.getActivityStage(activityId);
    }


}
