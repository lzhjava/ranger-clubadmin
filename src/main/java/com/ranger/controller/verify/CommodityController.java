package com.ranger.controller.verify;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ranger.commodity.contract.CommodityContract;
import com.ranger.commodity.po.CommodityPo;
import com.ranger.commodity.po.ShelvesPo;
import com.ranger.commodity.vo.ResultVO;
import org.springframework.web.bind.annotation.*;


/**  商品服务
 * @author huipeng
 * @Title: CommodityController
 * @ProjectName ranger-clubadmin
 * @Description: TODO
 * @date 2019/8/1下午2:28
 */
@RestController
@RequestMapping("/verify/commodity")
public class CommodityController {


    @Reference(interfaceClass = CommodityContract.class, timeout = 1200000)
    private CommodityContract commodityContract;




    /**   存储商品
     * @param commodityPo
     * @return
     */
    @PostMapping
   public ResultVO saveCommodity(@RequestBody CommodityPo commodityPo){
        ResultVO resultVO = commodityContract.saveCommodity(commodityPo);
        return resultVO;
    }

    /**   删除商品
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
   public ResultVO delCommodity(@PathVariable Long id){
       return  commodityContract.delCommodity(id);
   }

    /**   修改商品信息
     * @param id
     * @param commodityPo
     * @return
     */
    @PutMapping("/{id}")
   public ResultVO modifyCommodity(@PathVariable Long id,@RequestBody CommodityPo commodityPo){
       return  commodityContract.modifyCommodity(id, commodityPo);
   }

    /**   查询商品信息
     * @param pageNo
     * @param pageSize
     * @param clubId
     * @return
     */
    @GetMapping
   public ResultVO searchCommodityPage( @RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                        @RequestParam(required = false, defaultValue = "10")Integer pageSize,
                                        Long clubId){
       return  commodityContract.searchCommodityPage(pageNo, pageSize, clubId);
   }


    /**   商品信息主键查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResultVO searchCommodity(@PathVariable Long id){
        return  commodityContract.searchCommodity(id);
    }


    /**   存储货架
     * @param shelvesPo
     * @return
     */
    @PostMapping("/shelves")
    public ResultVO saveShelves(@RequestBody ShelvesPo shelvesPo){
        return  commodityContract.saveShelves(shelvesPo);
    }


    /**   关闭货架
     * @param id
     * @return
     */
    @DeleteMapping("/shelves")
    public  ResultVO delShelves(Long id){
        return  commodityContract.delShelves(id);
    }


    /**   修改货架信息
     * @param id
     * @param shelvesPo
     * @return
     */
    @PutMapping("/shelves/{id}")
    public ResultVO modifyShelves(@PathVariable Long id,@RequestBody ShelvesPo shelvesPo){
        return  commodityContract.modifyShelves(id, shelvesPo);
    }


    /**   查询货架分页
     * @param id
     * @return
     */
    @GetMapping("/shelves")
    public ResultVO searchShelvesPage(@RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                      @RequestParam(required = false, defaultValue = "10")Integer pageSize,
                                      Long id,Long clubId){
        return  commodityContract.searchShelvesPage(pageNo,pageSize,id,clubId);
    }


    /**   货架信息查询（携带商品信息）
     * @param id
     * @return
     */
    @GetMapping("/shelves/{id}")
    public ResultVO searchShelves(@PathVariable Long id){
        return  commodityContract.searchShelves(id);
    }


    /**  上架商品
     * @param shelvesId
     * @param commodityId
     * @return
     */
    @PostMapping(("/shelves/put"))
    public ResultVO putGoods(@RequestParam Long shelvesId,@RequestParam Long commodityId){
        return  commodityContract.putGoods(shelvesId, commodityId);
    }


    /**  下架商品
     * @param shelvesId
     * @param commodityId
     * @return
     */
    @PostMapping("/shelves/remove")
    public ResultVO removeGoods(@RequestParam Long shelvesId,@RequestParam Long commodityId){
        return  commodityContract.removeGoods(shelvesId, commodityId);
    }
}
