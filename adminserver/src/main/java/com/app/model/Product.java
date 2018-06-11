package com.app.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

import org.apache.commons.lang3.StringUtils;

// 产品表
public class Product extends Model<Product>
{
    private static final long serialVersionUID = 3554755307155224113L;

    public static final Product dao = new Product();

    public static final String ID = "id";           // 产品id
    public static final String TYPE = "type";       // 产品类型，支付、兑换等
    public static final String USEQUAN = "usequan"; // 是否可以使用券
    public static final String OPRICE = "oprice";   // 原价
    public static final String PRICE = "price";     // 现价
    public static final String ETH = "eth";         // eth支付价格
    public static final String ATNL = "atnl";       // atnl兑换价格
    public static final String GIFT = "gift";       // 购买后赠送atnl
    public static final String LP = "lp";           // 购买后赠送令牌
    public static final String COUNT = "count";     // 剩余数量，-1表示无限
    public static final String SEND_DATE = "send_date"; // 发货日期
    public static final String NAME = "name";       // 产品名称
    public static final String LOGO = "logo";       // 产品logo图片地址
    public static final String DETAIL = "detail";   // 产品描述
    public static final String URL = "url";         // 产品图片详情
    public static final String EXPIRE_TIME = "expire_time"; // 过期时间

    public Product create(Float oprice, Float price, Float eth, Float atnl, Float gift, Float lp, Integer count, Integer type, Integer usequan, String name, String logo, String detail, String url, String sendDate, String expireTime)
    {
        Product product = new Product();
        product.set(TYPE, type);
        product.set(PRICE, price);
        product.set(OPRICE, oprice);
        product.set(ETH, eth);
        product.set(ATNL, atnl);
        product.set(GIFT, gift);
        product.set(LP, lp);
        product.set(COUNT, count);
        product.set(TYPE, type);
        product.set(USEQUAN, usequan);
        product.set(LOGO, logo);
        product.set(DETAIL, detail);
        product.set(URL, url);
        if (StringUtils.isBlank(sendDate))
        {
            sendDate = "2100-12-31";
        }
        product.set(SEND_DATE, sendDate);
        product.set(NAME, name);
        if (StringUtils.isBlank(expireTime))
        {
            expireTime = "2100-12-31";
        }
        product.set(EXPIRE_TIME, expireTime);

        if (product.save())
        {
            return product;
        }
        return null;
    }

    public List<Product> getProducts()
    {
        return find("SELECT * FROM product WHERE expire_time>=NOW()");
    }

    public Product findProduct(Long pid)
    {
        return findFirst("SELECT * FROM product WHERE id=?", pid);
    }

    public List<Product> list(String name, int page)
    {
        if (StringUtils.isBlank(name))
        {
            return find("SELECT * FROM product ORDER BY id DESC LIMIT ?,20", page * 20);
        }
        else
        {
            return find("SELECT * FROM product WHERE name LIKE '%" + name + "%' ORDER BY id DESC LIMIT ?,20", page * 20);
        }
    }

    public Long count(String name)
    {
        if (StringUtils.isBlank(name))
        {
            return Db.queryLong("SELECT COUNT(1) FROM product");
        }
        else
        {
            return Db.queryLong("SELECT COUNT(1) FROM product WHERE sid LIKE '%" + name + "%'");
        }
    }
    
    public void delete(Long id)
    {
        Db.update("DELETE FROM product WHERE id=?", id);
    }
}
