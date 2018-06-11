package com.app.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

import org.apache.commons.lang3.StringUtils;

// 订单表
public class Order extends Model<Order>
{
    private static final long serialVersionUID = 3554755307155224114L;

    public static final Order dao = new Order();

    public static final String ID = "id";       // 订单id
    public static final String UID = "uid";     // 用户id
    public static final String PID = "pid";     // 产品id
    public static final String QID = "qid";     // 优惠券id
    public static final String COUNT = "count"; // 购买数量
    public static final String PRICE = "price"; // 购买价格
    public static final String REALPRICE = "realprice"; // 真实价格
    public static final String ETH = "eth";     // ETH支付数量
    public static final String PAYMENT = "payment"; // 实际支付RMB
    public static final String PAYTYPE = "paytype"; // 支付类型
    public static final String PAYSTATE = "paystate";   // 支付状态
    public static final String STATE = "state";     // 订单状态
    public static final String CREATE_TIME = "create_time"; // 创建时间
    public static final String PAY_TIME = "pay_time";   // 支付时间
    public static final String SEND_TIME = "send_time"; // 发货时间
    public static final String ADDR = "addr";       // 发货地址
    public static final String NAME = "name";       // 收件人姓名
    public static final String MSG = "msg";         // 留言
    public static final String TRACHNO = "trackno"; // 联系人电话
    public static final String EXTRA = "extra";     // 额外信息,如羊的id等

    public Order create(Long uid, Long pid, Long qid, Integer count, Float price, Float realprice, Float eth, int payType, String addr, String name, String msg)
    {
        Order order = new Order();
        order.set(UID, uid);
        order.set(PID, pid);
        order.set(QID, qid);
        order.set(COUNT, count);
        order.set(PRICE, price);
        order.set(REALPRICE, realprice);
        order.set(ETH, eth);
        order.set(PAYTYPE, payType);
        order.set(ADDR, addr);
        order.set(NAME, name);
        order.set(MSG, msg);
        order.set(STATE, 1);

        if (order.save())
        {
            return order;
        }
        return null;
    }

    public List<Order> getOrders(Long uid)
    {
        return find("SELECT * FROM `order` WHERE uid=? AND state>0", uid);
    }

    public Order findByOid(Long oid)
    {
        return findFirst("SELECT * FROM `order` WHERE id=?", oid);
    }

    public String buildWhere(Long uid, Long pid, Integer payType, Integer payState, Integer state, String addr, String name, String extra, String trackNo, String contacts)
    {
        StringBuffer sb = new StringBuffer();
        if (uid != 0L)
        {
            sb.append("uid=" + uid);
        }
        if (pid != 0L)
        {
            if (sb.length() > 0)
            {
                sb.append(" AND ");
            }
            sb.append("pid=" + pid);
        }
        if (payState >= 0)
        {
            if (sb.length() > 0)
            {
                sb.append(" AND ");
            }
            sb.append("paystate=" + payState);
        }
        if (state != 0)
        {
            if (sb.length() > 0)
            {
                sb.append(" AND ");
            }
            sb.append("state=" + state);
        }
        if (payType != 0)
        {
            if (sb.length() > 0)
            {
                sb.append(" AND ");
            }
            sb.append("paytype=" + payType);
        }
        if (StringUtils.isNotBlank(addr))
        {
            if (sb.length() > 0)
            {
                sb.append(" AND ");
            }
            sb.append("addr LIKE '%" + addr + "%'");
        }
        if (StringUtils.isNotBlank(name))
        {
            if (sb.length() > 0)
            {
                sb.append(" AND ");
            }
            sb.append("name LIKE '%" + name + "%'");
        }
        if (StringUtils.isNotBlank(extra))
        {
            if (sb.length() > 0)
            {
                sb.append(" AND ");
            }
            sb.append("extra LIKE '%" + extra + "%'");
        }
        if (StringUtils.isNotBlank(trackNo))
        {
            if (sb.length() > 0)
            {
                sb.append(" AND ");
            }
            sb.append("trackno LIKE '%" + trackNo + "%'");
        }
        if (StringUtils.isNotBlank(contacts))
        {
            if (sb.length() > 0)
            {
                sb.append(" AND ");
            }
            sb.append("contacts LIKE '%" + contacts + "%'");
        }
        return sb.toString();
    }

    public List<Order> list(String where, int page)
    {
        if (StringUtils.isBlank(where))
        {
            return find("SELECT * FROM `order` ORDER BY id DESC LIMIT ?,20", page * 20);
        }
        else 
        {
            return find("SELECT * FROM `order` WHERE " + where + " ORDER BY id DESC LIMIT ?,20", page * 20);
        }
    }

    public Long count(String where)
    {
        if (StringUtils.isBlank(where))
        {
            return Db.queryLong("SELECT COUNT(1) FROM `order`");
        }
        else 
        {
            return Db.queryLong("SELECT COUNT(1) FROM `order` WHERE " + where);
        }
    }
}