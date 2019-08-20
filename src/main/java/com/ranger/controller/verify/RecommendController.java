package com.ranger.controller.verify;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.activity.contract.WechatLiteRecommendContract;
import com.ranger.activity.enums.WechatLiteRecommendType;
import com.ranger.activity.vo.WechatLiteRecommendVO;
import com.ranger.utils.Pager;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 描述:
 *
 * @author ssd
 * @create 2019-08-02 2:14 PM
 */
@RestController
@RequestMapping("/verify/recommend")
public class RecommendController {

    @Reference(interfaceClass = WechatLiteRecommendContract.class, timeout = 1200000)
    private WechatLiteRecommendContract wechatLiteRecommendContract;

    /**
     * 推荐列表置顶取消置顶
     * @param clubId
     * @param relateId
     * @param wechatLiteRecommendType
     * @param top
     * @return
     */
    @RequestMapping(value = "/{clubId}/recommend/top", method = RequestMethod.POST)
    public Object topRecommend(@PathVariable String clubId, Long relateId, WechatLiteRecommendType wechatLiteRecommendType, boolean top) {

        return wechatLiteRecommendContract.topData(relateId, wechatLiteRecommendType, top);

    }

    /**
     * 获取推荐置顶列表
     * @param clubId
     * @return
     */
    @RequestMapping(value = "/{clubId}/recommend/top", method = RequestMethod.GET)
    public Object getRecommendTop(@PathVariable Long clubId) {


        return wechatLiteRecommendContract.selectTopData(clubId);
    }

    /**
     * 置顶排序
     * @param positions
     * @return
     */
    @RequestMapping(value = "/{clubId}/recommend/top/position", method = RequestMethod.POST)
    public Object positionTopRecommend(@RequestBody Map<String, Integer> positions) {

        return wechatLiteRecommendContract.topDataPosition(positions);
    }

    /**
     * 获取推荐列表
     * @param clubId
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/{clubId}/recommend", method = RequestMethod.GET)
    public Object getRecomendList(@PathVariable Long clubId,
                                  @RequestParam(value = "page", defaultValue = "0") Integer page,
                                  @RequestParam(value = "size", defaultValue = "12") Integer size) {

        com.ranger.vo.ResultVO<Long> resultVO = wechatLiteRecommendContract.countData(clubId);
        if (resultVO.getCode() != 0) {
            return resultVO;
        }
        if (resultVO.getBody() == 0) {
            // 如果一条数据没有。。。就直接返回空数据
            resultVO.setBody(null);
            return resultVO;
        }

        com.ranger.vo.ResultVO<List<WechatLiteRecommendVO>> listResultVO = wechatLiteRecommendContract.selectData(clubId, page, size);
        if (listResultVO.getCode() != 0) {
            return listResultVO;
        }

        com.ranger.vo.ResultVO<Pager> pagerResultVO = new com.ranger.vo.ResultVO<>();
        pagerResultVO.setCode(0);
        pagerResultVO.setBody(new Pager(page, size, resultVO.getBody(), listResultVO.getBody()));
        return pagerResultVO;

    }
}
