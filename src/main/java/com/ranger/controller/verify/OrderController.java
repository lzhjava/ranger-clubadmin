package com.ranger.controller.verify;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.order.contract.ExpressInfoContract;
import com.ranger.order.contract.OrderContract;
import com.ranger.order.vo.ResultVO;
import org.springframework.web.bind.annotation.*;


/**
 * 订单相关
 *
 * @author huipeng
 * @Title: OrderController
 * @ProjectName ranger-app
 * @Description: TODO
 * @date 2019/8/16下午1:47
 */
@RestController
@RequestMapping("/verify/order")
public class OrderController {

    @Reference(interfaceClass = OrderContract.class, timeout = 1200000)
    private OrderContract orderContract;

    @Reference(interfaceClass = ExpressInfoContract.class, timeout = 1200000)
    private ExpressInfoContract expressInfoContract;


    @PostMapping("/check/{orderId}")
    public ResultVO checkOrder(@PathVariable String orderId, @RequestParam Integer status) {
        return orderContract.checkOrder(orderId, status);
    }     //  审核订单


    @GetMapping("/page")
    public ResultVO orderPage(@RequestParam(required = false, defaultValue = "0") Integer pageNo,
                              @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                              Integer productType,
                              Long productId,
                              Integer orderState,
                              Long userId,
                              String orderId,
                              Long clubId) {
        return orderContract.orderPage(pageNo, pageSize, productType, productId, orderState, userId, orderId, clubId);
    }   //订单分页

    /**
     * 查询所有快递公司信息
     *
     * @param companyName
     * @param companyCode
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/expressInfo")
    public ResultVO selectexpressInfo(@RequestParam(required = false, defaultValue = "0") Integer page,
                                    @RequestParam(required = false, defaultValue = "10") Integer size,
                                    String companyName, String companyCode) {
        return expressInfoContract.findAll(companyName, companyCode, page, size);
    }


}
