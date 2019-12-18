package com.ranger.controller.verify;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.order.contract.PhysicalContract;
import com.ranger.order.po.UserAddressPO;
import com.ranger.order.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  实物商品
 *
 * @author huipeng
 * @Title: ClubController
 * @ProjectName ranger-clubadmin
 * @Description: TODO
 * @date 2019/7/25下午2:42
 */
@RestController
@RequestMapping("/verify/goods")
public class PhysicalController {


    @Reference(interfaceClass = PhysicalContract.class, timeout = 1200000)
    private PhysicalContract physicalContract;


    @GetMapping("/page")
    public ResultVO searchPhysicalGoodsPage(@RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                            String orderId,
                                            Long userId,
                                            Long clubId){
        return  physicalContract.searchPhysicalGoodsPage(pageNo, pageSize, orderId, userId,clubId);
    }        //实物商品分页


    @GetMapping("/aRefund/{oderId}")
    public ResultVO orderIdGetReimburse(String orderId){
        return  physicalContract.orderIdGetReimburse(orderId);
    }          //订单id查询退款信息


    @PutMapping("/aRefund/{id}")
    public ResultVO refundTheAudit(@PathVariable Long id,String reason,Integer status){
        return  physicalContract.refundTheAudit(id, reason, status);
    }     //退款审核

    @PostMapping("/logistics/{physicalId}")
    public  ResultVO deliverGoods(@PathVariable Long physicalId,String trackingNumber,String company){
        return  physicalContract.deliverGoods(physicalId, trackingNumber, company);
    }     //商品绑定快递单号

    @GetMapping("/aRefund/page")
    public ResultVO searchARefundPage(@RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                      @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                     Integer status,Long clubId,Long userId){
        return  physicalContract.searchARefundPage(pageNo, pageSize, status, clubId,userId);
    }    //待退款列表

}
