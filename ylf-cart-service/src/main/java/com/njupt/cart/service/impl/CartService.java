package com.njupt.cart.service.impl;

import com.njupt.cart.service.CartInterface;
import com.njupt.mapper.TbItemMapper;
import com.njupt.pojo.JsonUtils;
import com.njupt.pojo.TbItem;
import com.njupt.pojo.TbUser;
import com.njupt.pojo.YlfResult;
import com.njupt.redis.JedisClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Title:
 * description:
 * Create Time: 2017/12/24 20:30 星期日
 *
 * @author: WJZ
 **/
@Service
public class CartService implements CartInterface {

    @Value("${REDIS_USER_PRE}")
    private String REDIS_USER_PRE;
    @Resource
    private JedisClient jedisClient;
    @Resource
    private TbItemMapper itemMapper;

    //向 redis中添加购物车信息的接口的实现类
    @Override
    public YlfResult addCart(long userId, long itemId, int num) {
        Boolean hexists = jedisClient.hexists(REDIS_USER_PRE + ":" + userId, itemId + "");
        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        if (hexists)
        {
            //如果存在的话直接在num上加
            String hget = jedisClient.hget(REDIS_USER_PRE + ":" + userId, itemId + "");
            TbItem tbItem1 = JsonUtils.jsonToPojo(hget, TbItem.class);
            tbItem1.setNum(tbItem1.getNum()+num);
            jedisClient.hset(REDIS_USER_PRE + ":" + userId, itemId + "",JsonUtils.objectToJson(tbItem1));
            return YlfResult.ok();
        }


        //如果不存在将商品信息添加到redis
        tbItem.setNum(num);
        tbItem.setImage(tbItem.getImage().split(",")[0]);
        jedisClient.hset(REDIS_USER_PRE + ":" + userId, itemId + "",JsonUtils.objectToJson(tbItem));

        return YlfResult.ok();
    }

    //合并购物车
    @Override
    public List<TbItem> mergerCart(long userId, List<TbItem> itemList) {

        List<TbItem> list=new ArrayList<>();

        for(TbItem item:itemList)
        {

            YlfResult result = addCart(userId, item.getId(), item.getNum());

        }

         List<String> string = jedisClient.hvals(REDIS_USER_PRE + ":" + userId);


        for(String s:string)
        {


            TbItem item = JsonUtils.jsonToPojo(s, TbItem.class);
            list.add(item);

        }

        return list;



    }

    //更新购物车中商品的数量
    @Override
    public YlfResult updateNum(long userId, long itemId, int num) {


        String s = jedisClient.hget(REDIS_USER_PRE + ":" + userId, itemId + "");
        TbItem item = JsonUtils.jsonToPojo(s, TbItem.class);
        item.setNum(num);

        jedisClient.hset(REDIS_USER_PRE + ":" + userId, itemId + "",JsonUtils.objectToJson(item));

        return YlfResult.ok();
    }


    //删除购物车中某个商品
    @Override
    public YlfResult deleteItem(long userId, long itemId) {
        jedisClient.hdel(REDIS_USER_PRE + ":" + userId, itemId + "");
        return YlfResult.ok();
    }

    //查询购物车中商品列表
    @Override
    public List<TbItem> getCartList(long userId) {

         List<String> hvals = jedisClient.hvals(REDIS_USER_PRE + ":" + userId);
         List<TbItem> tbItems=new ArrayList<>();
         for (String str:hvals)
         {
             TbItem item = JsonUtils.jsonToPojo(str, TbItem.class);
             tbItems.add(item);

         }


        return tbItems;
    }

    //清除购物车
    @Override
    public YlfResult clearCart(long userId) {

        jedisClient.del(REDIS_USER_PRE + ":" + userId);

        return YlfResult.ok();
    }
}
