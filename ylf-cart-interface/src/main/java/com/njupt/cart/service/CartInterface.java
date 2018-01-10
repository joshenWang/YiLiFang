package com.njupt.cart.service;

import com.njupt.pojo.TbItem;
import com.njupt.pojo.YlfResult;

import java.util.List;

/**
 * Title:
 * description:向 redis中添加购物车信息的接口
 * Create Time: 2017/12/24 20:28 星期日
 *
 * @author: WJZ
 **/
public interface CartInterface {


    //向 redis中添加购物车信息的接口

    public YlfResult addCart(long userId,long itemId,int num);

    //合并购物车
    public List<TbItem> mergerCart(long userId, List<TbItem> itemList);

    //更新购物车中商品的数量
    public YlfResult updateNum(long userId,long itemId,int num);

    //删除购物车中某个商品
    public YlfResult deleteItem(long userId,long itemId);
    //删除购物车中某个商品
    public List<TbItem>getCartList(long userId);
    //删除购物车
    public YlfResult clearCart(long userId);


}
