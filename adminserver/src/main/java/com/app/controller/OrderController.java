package com.app.controller;

import java.util.HashMap;
import java.util.List;

import com.app.model.Log;
import com.app.model.Order;
import com.app.model.Product;
import com.app.model.Sheep;

import org.apache.commons.lang3.StringUtils;

import n.fw.base.BaseController;
import n.fw.utils.DateUtils;

public class OrderController extends BaseController
{
    public void list()
    {
        if (!checkLevel(1))
        {
            errorInvalidOper();
            return;
        }

        Long uid = getParaToLong("uid", 0L);
        Long pid = getParaToLong("pid", 0L);
        Integer payType = getParaToInt("paytype", 0);
        Integer payState = getParaToInt("paystate", -1);
        Integer state = getParaToInt("state", 0);
        String addr = getPara("addr", "");
        String name = getPara("name", "");
        String trackno = getPara("trackno", "");
        String sheep = getPara("sheep", "");
        String contacts = getPara("contacts", "");
        Integer page = getParaToInt("page", 0);
        
        String where = Order.dao.buildWhere(uid, pid, payType, payState, state, addr, name, sheep, trackno, contacts);
        HashMap<String, Object> map = new HashMap<String,Object>();
        List<Order> arr = Order.dao.list(where, page);
        map.put("arr", arr);
        map.put("count", Order.dao.count(where));
        success(map);
    } 

    public void payed()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long oid = getParaToLong("oid", 0L);
        if (oid == 0)
        {
            error("找不到该订单");
            return;
        }

        String strPayment = getPara("payment", "0");
        Float payment = Float.parseFloat(strPayment);
        Order order = Order.dao.findByOid(oid);
        if (order == null)
        {
            error("找不到该订单");
            return;
        }

        if (order.getInt(Order.STATE) != 1)
        {
            error("订单状态不正确");
            return;
        }

        List<Sheep> sheeps = Sheep.dao.getSheep(order.getInt(Order.COUNT));
        StringBuffer extra = new StringBuffer();
        for (Sheep sheep : sheeps) {
            if (extra.length() > 0) {
                extra.append('|');
            }
            extra.append(sheep.getStr(Sheep.SID));
            sheep.set(Sheep.STATE, 1);
            sheep.update();
        }
        order.set(Order.PAYMENT, payment);
        order.set(Order.EXTRA, extra.toString());
        order.set(Order.STATE, 2);
        order.set(Order.PAYSTATE, 1);
        order.set(Order.PAY_TIME, DateUtils.getDateTime());
        order.update();

        Product product = Product.dao.findProduct(order.getLong(Order.PID));
        if (product != null)
        {
            Integer productCount = product.getInt(Product.COUNT);
            System.out.println("product count = " + productCount);
            if (productCount > 0) {
                System.out.println("product count new = " + (productCount - order.getInt(Order.COUNT)));
                product.set(Product.COUNT, productCount - order.getInt(Order.COUNT));
                product.update();
            }
        }

        Log.dao.add(getUid(), "order/payed", "payed order - oid=" + oid + " payment=" + payment + " extra=" + extra);
        success(order);
    }

    public void send()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long oid = getParaToLong("oid", 0L);
        if (oid == 0)
        {
            error("找不到该订单");
            return;
        }

        String trackno = getPara("trackno", "");
        if (StringUtils.isBlank(trackno))
        {
            error("请填写单号");
            return;
        }
        Order order = Order.dao.findByOid(oid);
        if (order == null)
        {
            error("找不到该订单");
            return;
        }

        if (order.getInt(Order.STATE) != 2)
        {
            error("订单状态不正确");
            return;
        }
        order.set(Order.STATE, 3);
        order.set(Order.TRACHNO, trackno);
        order.set(Order.SEND_TIME, DateUtils.getDateTime());
        order.update();

        Log.dao.add(getUid(), "order/send", "send order - oid=" + oid + " trackno=" + trackno);
        success(order);
    }

    public void finish()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long oid = getParaToLong("oid", 0L);
        if (oid == 0)
        {
            error("找不到该订单");
            return;
        }

        Order order = Order.dao.findByOid(oid);
        if (order == null)
        {
            error("找不到该订单");
            return;
        }

        if (order.getInt(Order.STATE) != 3)
        {
            error("订单状态不正确");
            return;
        }

        order.set(Order.STATE, 4);
        order.update();

        Log.dao.add(getUid(), "order/finish", "finish order - oid=" + oid);
        success(order);
    }

    public void close()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long oid = getParaToLong("oid", 0L);
        if (oid == 0)
        {
            error("找不到该订单");
            return;
        }

        Order order = Order.dao.findByOid(oid);
        if (order == null)
        {
            error("找不到该订单");
            return;
        }

        /*
        if (order.getInt(Order.STATE) > 1)
        {
            error("订单状态不正确");
            return;
        }
        */
        order.set(Order.STATE, 0);
        order.update();

        Log.dao.add(getUid(), "order/close", "close order - oid=" + oid);
        success(order);
    }

    public void get()
    {
        if (!checkLevel(1))
        {
            errorInvalidOper();
            return;
        }
        Long oid = getParaToLong("oid", 0L);
        success(Order.dao.findByOid(oid));
    }
}