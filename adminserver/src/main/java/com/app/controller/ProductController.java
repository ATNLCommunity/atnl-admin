package com.app.controller;

import java.util.HashMap;
import java.util.List;

import com.app.model.Log;
import com.app.model.Order;
import com.app.model.Product;

import org.apache.commons.lang3.StringUtils;

import n.fw.base.BaseController;

public class ProductController extends BaseController
{
    public void list()
    {
        if (!checkLevel(1))
        {
            errorInvalidOper();
            return;
        }

        String name = getPara("name", "");
        Integer page = getParaToInt("page", 0);
        
        HashMap<String, Object> map = new HashMap<String,Object>();
        List<Product> arr = Product.dao.list(name, page);
        map.put("arr", arr);
        map.put("count", Product.dao.count(name));
        success(map);
    }

    public void add()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        String name = getPara("name", "");
        if (StringUtils.isBlank(name))
        {
            error("产品名字不能为空");
            return;
        }

        Float oprice = Float.parseFloat(getPara("oprice", "0"));
        Float price = Float.parseFloat(getPara("price", "0"));
        Float eth = Float.parseFloat(getPara("eth", "0"));
        Float atnl = Float.parseFloat(getPara("atnl", "0"));
        Float gift = Float.parseFloat(getPara("gift", "0"));
        Float lp = Float.parseFloat(getPara("lp", "0"));
        Float yf = Float.parseFloat(getPara("yf", "0"));
        Integer count = getParaToInt("count", -1);
        Integer type = getParaToInt("type", 0);
        Integer usequan = getParaToInt("usequan", 0);
        String sendDate = getPara("senddate", "");
        String expireTime = getPara("exporetime", "");
        String logo = getPara("logo", "");
        String detail = getPara("detail", "");
        String url = getPara("url", "");
        String send_date_desc = getPara("send_date_desc", "");
        String sendby = getPara("sendby", "");
        Integer from_type = getParaToInt("from_type", 0);

        Product product = Product.dao.create(oprice, price, eth, atnl, gift, lp, yf, count, type, usequan, name, logo, detail, url, sendDate, expireTime, send_date_desc, sendby,from_type);
        if (product != null)
        {
            Log.dao.add(getUid(), "product/create", "create product - id=" + product.getLong(Product.ID) + " type=" + type + " price=" + price + " eth=" + eth + " atnl=" + atnl + " count=" + count + " logo=" + logo + " detail=" + detail + " senddate" + sendDate + " expire_time=" + expireTime);
        }
        success(product);
    }

    public void edit()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long id = getParaToLong("id", 0L);
        if (id == 0)
        {
            error("找不到该产品");
            return;
        }
        Product product = Product.dao.findProduct(id);
        if (product == null)
        {
            error("找不到该产品");
            return;
        }

        String name = getPara("name", "");
        if (StringUtils.isNotBlank(name))
        {
            product.set(Order.NAME, name);
        }

        Float oprice = Float.parseFloat(getPara("oprice", "0"));
        product.set(Product.OPRICE, oprice);
        Float price = Float.parseFloat(getPara("price", "0"));
        product.set(Product.PRICE, price);
        Float eth = Float.parseFloat(getPara("eth", "0"));
        product.set(Product.ETH, eth);
        Float atnl = Float.parseFloat(getPara("atnl", "0"));
        product.set(Product.ATNL, atnl);
        Float gift = Float.parseFloat(getPara("gift", "0"));
        product.set(Product.GIFT, gift);
        Float lp = Float.parseFloat(getPara("lp", "0"));
        product.set(Product.LP, lp);
        Float yf = Float.parseFloat(getPara("yf", "0"));
        product.set(Product.YF, yf);
        Integer count = getParaToInt("count", -1);
        product.set(Product.COUNT, count);
        Integer type = getParaToInt("type", 0);
        product.set(Product.TYPE, type);
        Integer usequan = getParaToInt("usequan", 0);
        product.set(Product.USEQUAN, usequan);
        String sendDate = getPara("senddate", "");
        String send_date_desc = getPara("send_date_desc", "");
        String sendby = getPara("sendby", "");
        Integer from_type = getParaToInt("from_type", 0);
        product.set(Product.FROM_TYPE, from_type);

        if (StringUtils.isNotBlank(sendDate))
        {
            product.set(Product.SEND_DATE, sendDate);
        }
        String expireTime = getPara("exporetime", "");
        if (StringUtils.isNotBlank(expireTime))
        {
            product.set(Product.EXPIRE_TIME, expireTime);
        }
        String logo = getPara("logo", "");
        if (StringUtils.isNotBlank(logo))
        {
            product.set(Product.LOGO, logo);
        }
        String detail = getPara("detail", "");
        if (StringUtils.isNotBlank(detail))
        {
            product.set(Product.DETAIL, detail);
        }
        String url = getPara("url", "");
        if (StringUtils.isNotBlank(url))
        {
            product.set(Product.URL, url);
        }

        if (StringUtils.isNotBlank(send_date_desc))
        {
            product.set(Product.SEND_DATE_DESC, send_date_desc);
        }

        if (StringUtils.isNotBlank(sendby))
        {
            product.set(Product.SENDBY, sendby);
        }

        product.update();
        Log.dao.add(getUid(), "product/edit",
        "create product - id=" + id + " oprice=" + oprice + " price=" + price + " eth=" + eth + " count=" + count + " logo=" + logo + " detail=" + detail + " senddate" + sendDate + " expire_time=" + expireTime);
        success(product);
    }

    public void delete()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long id = getParaToLong("id", 0L);
        if (id != 0)
        {
            Product.dao.delete(id);
            Log.dao.add(getUid(), "product/delete", "delete product - id=" + id);
        }
        success();
    }

    public void get()
    {
        if (!checkLevel(1))
        {
            errorInvalidOper();
            return;
        }

        Long pid = getParaToLong("pid", 0L);
        if (pid != 0)
        {
            success(Product.dao.findProduct(pid));
        }
        else
        {
            error("订单id不能为空");
        }
    }
}
