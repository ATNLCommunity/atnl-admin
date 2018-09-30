package com.app.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

import org.apache.commons.lang3.StringUtils;

// 用户信息表
public class User extends Model<User> {
    private static final long serialVersionUID = 3554755307155224112L;

    public static final User dao = new User();

    public static final String ID = "id";               // 用户id
    public static final String INVITEID = "inviteid";   // 邀请者id
    public static final String PHONE = "phone";         // 用户手机号
    public static final String PWD = "pwd";             // 用户密码
    public static final String NAME = "name";           // 昵称
    public static final String MAIL = "mail";           // 邮箱地址
    public static final String TOKENADDR = "tokenaddr"; // token钱包地址
    public static final String MONEY = "money";         // rmb积分
    public static final String ATNL = "atnl";           // 解锁atnl
    public static final String LP = "lp";               // 令牌
    public static final String STATE = "state";         // 状态
    public static final String LOCKATNL = "lockatnl";   // 锁定atnl
    public static final String NEEDATNL = "needatnl";   // 申请提现atnl
    public static final String ALLATNL = "allatnl";     // 已消费atnl
    public static final String M1 = "m1";               // m1级邀请数
    public static final String M2 = "m2";               // m2级邀请数
    public static final String M3 = "m3";               // m3级邀请数
    public static final String M4 = "m4";               // m4级邀请数
    public static final String M5 = "m5";               // m5级邀请数
    public static final String M6 = "m6";               // m6级邀请数
    public static final String M21 = "m21";
    public static final String M31 = "m31";
    public static final String UPDATE_TIME = "update_time"; // 更新时间
    public static final String CREATE_TIME = "create_time"; // 注册时间
    public static final String UTYPE = "utype";
    public static final String LOGINTOKEN = "login_token";

    public User findByPhone(String phone) {
        return findFirst("SELECT * FROM user WHERE phone=?", phone);
    }

    public User findByUid(Long uid) {
        return findFirst("SELECT * FROM user WHERE id=?", uid);
    }

    public List<User> findInvites(Long uid)
    {
        return find("SELECT id,phone,name,update_time FROM user WHERE inviteid=?", uid);
    }

    public String buildWhere(Long id, Long inviteid, String phone, String name, String mail, String ip, Integer state)
    {
        StringBuffer sb = new StringBuffer();
        if (id != null && id != 0)
        {
            sb.append("id=" + id);
        }
        if (inviteid != null && inviteid != 0)
        {
            if (sb.length() > 0)
            {
                sb.append(" AND ");
            }

            sb.append("inviteid=" + inviteid);
        }
        if (state != null && state != 0)
        {
            if (sb.length() > 0)
            {
                sb.append(" AND ");
            }

            sb.append("state=" + state);
        }
        if (StringUtils.isNotBlank(phone))
        {
            if (sb.length() > 0)
            {
                sb.append(" AND phone LIKE '%" + phone + "%'");
            }
            else
            {
                sb.append("phone LIKE '%" + phone + "%'");
            }
        }
        if (StringUtils.isNotBlank(name))
        {
            if (sb.length() > 0)
            {
                sb.append(" AND name LIKE '%" + name + "%'");
            }
            else
            {
                sb.append("name LIKE '%" + name + "%'");
            }
        }
        if (StringUtils.isNotBlank(mail))
        {
            if (sb.length() > 0)
            {
                sb.append(" AND mail LIKE '%" + mail + "%'");
            }
            else
            {
                sb.append("mail LIKE '%" + mail + "%'");
            }
        }
        
        if (StringUtils.isNotBlank(ip))
       {
            if (sb.length() > 0)
            {
                sb.append(" AND ip LIKE '%" + ip + "%'");
            }
            else
            {
                sb.append("mail LIKE '%" + mail + "%'");
            }
        }


        return sb.toString();
    }

    public List<User> list(String where, int page)
    {
        if (StringUtils.isBlank(where))
        {
            return find("SELECT * FROM user ORDER BY `id` DESC LIMIT ?,20", page * 20);
        }
        else
        {
            return find("SELECT * FROM user WHERE " + where + " ORDER BY `id` DESC LIMIT ?,20", page * 20);
        }
    }

    public Long count(String where)
    {
        if (StringUtils.isBlank(where))
        {
            return Db.queryLong("SELECT COUNT(1) FROM user");
        }
        else
        {
            return Db.queryLong("SELECT COUNT(1) FROM user WHERE " + where);
        }
    }

    public List<User> getNeedCash(Integer page)
    {
        return find("SELECT * FROM user WHERE needatnl>0 ORDER BY `id` DESC LIMIT ?,20", page * 20);
    }

    public List<User> getLocks()
    {
        return find("SELECT * FROM user WHERE lockatnl>0");
    }

    public List<User> getInvites()
    {
        return find("SELECT * FROM user WHERE inviteid>0");
    }
}