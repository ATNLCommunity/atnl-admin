package com.app.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

import org.apache.commons.lang3.StringUtils;

public class Product extends Model<Product>
{
    private static final long serialVersionUID = 3554755307155224113L;

    public static final Product dao = new Product();

    public static final String ID = "id";
    public static final String TYPE = "type";
    public static final String USEQUAN = "usequan";
    public static final String OPRICE = "oprice";
    public static final String PRICE = "price";
    public static final String ETH = "eth";
    public static final String ATNL = "atnl";
    public static final String GIFT = "gift";
    public static final String LP = "lp";
    public static final String COUNT = "count";
    public static final String SEND_DATE = "send_date";
    public static final String NAME = "name";
    public static final String LOGO = "logo";
    public static final String DETAIL = "detail";
    public static final String URL = "url";
    public static final String EXPIRE_TIME = "expire_time";

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
