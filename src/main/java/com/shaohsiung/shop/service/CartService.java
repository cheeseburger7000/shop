package com.shaohsiung.shop.service;

import com.shaohsiung.shop.dto.CartItem;
import com.shaohsiung.shop.error.ServiceException;
import com.shaohsiung.shop.mapper.CartMapper;
import com.shaohsiung.shop.dto.Cart;
import com.shaohsiung.shop.mapper.GoodsMapper;
import com.shaohsiung.shop.model.Goods;
import com.shaohsiung.shop.model.enums.GoodsStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 购物车模块
 */
@Slf4j
@Service
public class CartService {
    // TODO 抽取到配置类中
    private final static String SHOP_CART = "shop-cart";
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private RedisTemplate<String, Cart> redisTemplate;

    /**
     * 根据用户id获取购物车
     * @param userId
     * @return
     */
    public Cart getCart(Long userId) {
        Cart result;
        HashOperations<String, Long, Cart> hashOperations = redisTemplate.opsForHash();

        // reids中存在购物车
        if (redisTemplate.hasKey(SHOP_CART ) && hashOperations.hasKey(SHOP_CART , userId)) {
            result = Optional.of(hashOperations.get(SHOP_CART, userId)).get();
            log.info("【购物车模块】从redis获取购物车 {}", result);
            return result;
        }

        // 用户还没有购物车
        result = Cart.builder().userId(userId).content(new HashMap<>()).total(new BigDecimal(0)).build();
        hashOperations.put(SHOP_CART, userId, result);
        log.info("【购物车模块】保存购物车 {} 到redis", result);
        //redisTemplate.expire(SHOP_CART_PREFIX, 1, TimeUnit.MINUTES); TODO 购物车不需要设置过期时间
        return result;
    }

    /**
     * 添加商品到购物车
     * TODO 商品库存判断
     * @param goodsId
     * @param userId
     * @return
     */
    public Cart addGoods2Cart(Long goodsId, Long userId) {
        HashOperations<String, Long, Cart> hashOperations = redisTemplate.opsForHash();
        // 根据goodsId获取商品， 判断商品是否上架
        Goods goods = goodsMapper.getById(goodsId);
        if (goods.getStatus().equals(GoodsStatus.HAS_BEEN_REMOVED)) {
            log.error("购物车存在过期商品 goodsId：{}", goods.getGoodsId());
            throw new ServiceException("购物车存在过期商品");
        }

        // 根据userId获取购物车，判断购物车中是否已经存在该商品
        Cart cart = getCart(userId);
        hashOperations.delete(SHOP_CART, userId); // 处理反序列化异常

        Map<Long, CartItem> content = cart.getContent();
        for (Map.Entry<Long, CartItem> cartItemEntry: content.entrySet()) {
            Long currentGoodsId = cartItemEntry.getKey();
            if (goodsId != null && goodsId.equals(currentGoodsId)) {
                CartItem cartItem = cartItemEntry.getValue();
                cart.getContent().remove(cartItem);
                // 若存在 数量+1 计算购物车总价
                cartItem.setCount(cartItem.getCount()+1);
                cartItem.setAmount(cartItem.getAmount().add(goods.getPrice()));

                cart.getContent().put(goodsId, cartItem);
                cart.setTotal(cart.getTotal().add(goods.getPrice()));

                // 将购物车更改到redis
                hashOperations.put(SHOP_CART, userId, cart);
                log.info("【购物车模块】添加商品 {} 到购物车 {} 到redis", goodsId, cart);
                return  cart;
            }
        }

        // 若不存在 创建购物车项目 计算购物车总价
        CartItem newCartItem = CartItem.builder().goodsId(goodsId)
                .amount(goods.getPrice())
                .count(1)
                .status(goods.getStatus())
                .name(goods.getName())
                .build();
        cart.getContent().put(goodsId, newCartItem);
        cart.setTotal(cart.getTotal().add(goods.getPrice()));

        // 将购物车更改到redis
        hashOperations.put(SHOP_CART, userId, cart);
        log.info("【购物车模块】添加商品 {} 到购物车 {} 到redis", goodsId, cart);
        return cart;
    }

    /**
     * 购物车商品-1
     * @param goodsId
     * @param userId
     * @return
     */
    public Cart decreaseGoodsFromCart(Long goodsId, Long userId) {
        HashOperations<String, Long, Cart> hashOperations = redisTemplate.opsForHash();
        Goods goods = goodsMapper.getById(goodsId);

        // 获取购物车
        Cart cart = getCart(userId);
        hashOperations.delete(SHOP_CART, userId);

        // 判断购物车是否存在该商品，不存在就抛异常
        Map<Long, CartItem> content = cart.getContent();
        for (Map.Entry<Long, CartItem> cartItemEntry: content.entrySet()) {
            Long currentGoodsId = cartItemEntry.getKey();
            if (goodsId != null && goodsId.equals(currentGoodsId)) {
                // 删除目标商品 -1
                CartItem cartItem = cartItemEntry.getValue();
                if (cartItem.getCount() > 0) {
                    cartItem.setCount(cartItem.getCount() - 1);
                    cartItem.setAmount(cartItem.getAmount().subtract(goods.getPrice()));
                    if (cartItem.getCount() == 0) {
                        content.remove(goodsId);
                    }
                    cart.setTotal(cart.getTotal().subtract(goods.getPrice()));
                    // 存储到redis
                    hashOperations.put(SHOP_CART, userId, cart);
                    log.info("【购物车模块】购物车商品-1 goodsId:{}， cart:{}", goodsId, cart);
                    return cart;
                }
            }
        }
        hashOperations.put(SHOP_CART, userId, cart);
        log.error("【购物车模块】购物车商品-1失败 goodsId:{}，userId:{}", goodsId, userId);
        throw new ServiceException("购物车中不存在该商品");
    }

    /**
     * 删除购物车商品项
     * @param goodsId
     * @param userId
     * @return
     */
    public Cart deleteGoodsFromCart(Long goodsId, Long userId) {
        HashOperations<String, Long, Cart> hashOperations = redisTemplate.opsForHash();

        // 根据userId获取购物车，判断购物车中是否已经存在该商品
        Cart cart = getCart(userId);
        hashOperations.delete(SHOP_CART, userId);

        Map<Long, CartItem> content = cart.getContent();
        for (Map.Entry<Long, CartItem> cartItemEntry: content.entrySet()) {
            Long currentGoodsId = cartItemEntry.getKey();
            if (goodsId != null && goodsId.equals(currentGoodsId)) {
                CartItem cartItem = cartItemEntry.getValue();
                cart.getContent().remove(goodsId);
                cart.setTotal(cart.getTotal().subtract(cartItem.getAmount()));

                hashOperations.put(SHOP_CART, userId, cart);
                log.info("【购物车模块】删除购物车商品 {} 购物车 {}", goodsId, cart);
                return cart;
            }
        }
        // 将购物车放回redis
        hashOperations.put(SHOP_CART, userId, cart);
        log.error("【购物车模块】删除购物车商品失败 goodsId:{}，userId:{}", goodsId, userId);
        throw new ServiceException("购物车中不存在该商品");
    }

    /**
     * 清空购物车
     * @param userId
     * @return
     */
    public  Cart clearCart(Long userId) {
        Cart result;
        HashOperations<String, Long, Cart> hashOperations = redisTemplate.opsForHash();

        // 清空redis中的购物车
        if (redisTemplate.hasKey(SHOP_CART ) && hashOperations.hasKey(SHOP_CART , userId)) {
            hashOperations.delete(SHOP_CART, userId);
        }

        result = Cart.builder().userId(userId)
                .total(new BigDecimal(0))
                .content(new HashMap<>())
                .build();
        hashOperations.put(SHOP_CART, userId, result);
        return result;
    }

    /**
     * 更改购物车商品数量
     * TODO 商品库存判断
     * @param userId
     * @param goodsId
     * @param count
     * @return
     */
    public Cart updateCartGoodsCount(Long userId, Long goodsId, Integer count) {
        return null;
    }
}
