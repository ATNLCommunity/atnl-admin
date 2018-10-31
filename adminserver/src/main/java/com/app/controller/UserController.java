package com.app.controller;

import java.util.HashMap;
import java.util.List;

import com.app.model.Admin;
import com.app.model.Log;
import com.app.model.User;

import org.apache.commons.lang3.StringUtils;

import n.fw.base.BaseController;

public class UserController extends BaseController
{
    public void list()
    {
        if (!checkLevel(1))
        {
            errorInvalidOper();
            return;
        }

        Long id = getParaToLong("id", 0l);
        Long inviteid = getParaToLong("inviteid", 0l);
        String phone = getPara("phone");
        String name = getPara("name");
        String mail = getPara("mail");
        String ip = getPara("mail");
        Integer state = getParaToInt("state", 0);
        Integer page = getParaToInt("page", 0);
        
        String where = User.dao.buildWhere(id, inviteid, phone, name, mail, ip, state);
        HashMap<String, Object> map = new HashMap<String,Object>();
        List<User> arr = User.dao.list(where, page);
        map.put("arr", arr);
        map.put("count", User.dao.count(where));
        success(map);
    }

    public void cashlist()
    {
        if (!checkLevel(1))
        {
            errorInvalidOper();
            return;
        }

        Integer page = getParaToInt("page", 0);
        success(User.dao.getNeedCash(page));
    }

    public void cash()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long uid = getParaToLong("uid", 0L);
        if (uid == 0)
        {
            errorInvalid();
            return;
        }

        Integer state = getParaToInt("state", 0);

        User user = User.dao.findByUid(uid);
        if (user == null)
        {
            error("找不到该用户");
            return;
        }

        Float needcash = user.getFloat(User.NEEDATNL);
        user.set(User.NEEDATNL, 0);
        if (state == 0)
        {
            user.set(User.ATNL, user.getFloat(User.ATNL) + needcash);
        }
        user.set(User.ALLATNL, user.getFloat(User.ALLATNL) + needcash);
        user.update();
        success(user);
    }

    public void unlock()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        String pwd = getPara("pwd", "");
        Admin admin = Admin.dao.findById(getUid());
        if (admin == null || !StringUtils.equals(pwd, admin.getStr(Admin.PWD)))
        {
            errorInvalidOper();
            return;
        }

        String strAtnl = getPara("atnl", "0");
        Float atnl = Float.parseFloat(strAtnl);
        if (atnl <= 0F)
        {
            errorInvalid();
            return;
        }
        
        List<User> lst = User.dao.getLocks();
        for (User u : lst) {
            Float lockAtnl = u.getFloat(User.LOCKATNL);
            Float moveAtnl = atnl;
            if (lockAtnl < atnl)
            {
                moveAtnl = lockAtnl;
            }

            u.set(User.LOCKATNL, lockAtnl - moveAtnl);
            u.set(User.ATNL, u.getFloat(User.ATNL) + moveAtnl);
            u.save();
        }
        Log.dao.add(getUid(), "user/unlock", "unlock atnl=" + atnl);
        success();
    }

    public void addatnl()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long uid = getParaToLong("uid", 0L);
        if (uid == 0)
        {
            error("uid 不能为空");
            return;
        }

        String strAtnl = getPara("atnl", "0");
        Float atnl = Float.parseFloat(strAtnl);
        if (atnl <= 0F)
        {
            errorInvalid();
            return;
        }
        if (atnl > 1000F)
        {
            error("每次最多操作0-1000个ATNL");
            return;
        }
        
        Integer type = getParaToInt("type", 0);
        User user = User.dao.findByUid(uid);
        if (user == null)
        {
            error("找不到用户");
            return;
        }

        if (type == 0)
        {
            user.set(User.LOCKATNL, user.getFloat(User.LOCKATNL) + atnl);
        }
        else
        {
            user.set(User.ATNL, user.getFloat(User.ATNL) + atnl);
        }
        user.update();

        Log.dao.add(getUid(), "user/addatnl", "add atnl=" + atnl + " uid=" + uid);
        success(user); 
    }

    public void delatnl()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long uid = getParaToLong("uid", 0L);
        if (uid == 0)
        {
            error("uid 不能为空");
            return;
        }

        String strAtnl = getPara("atnl", "0");
        Float atnl = Float.parseFloat(strAtnl);
        if (atnl <= 0F)
        {
            errorInvalid();
            return;
        }
        if (atnl > 1000F)
        {
            error("每次最多操作0-1000个ATNL");
            return;
        }
        
        Integer type = getParaToInt("type", 0);
        User user = User.dao.findByUid(uid);
        if (user == null)
        {
            error("找不到用户");
            return;
        }

        if (type == 0)
        {
            Float userAtnl = user.getFloat(User.LOCKATNL);
            if (userAtnl < atnl)
            {
                error("用户锁定atnl不足");
                return;
            }
            user.set(User.LOCKATNL, userAtnl - atnl);
        }
        else
        {
            Float userAtnl = user.getFloat(User.ATNL);
            if (userAtnl < atnl)
            {
                error("用户解锁atnl不足");
                return;
            }
            user.set(User.ATNL, userAtnl - atnl);
        }
        user.update();

        Log.dao.add(getUid(), "user/delatnl", "del atnl=" + atnl + " uid=" + uid);
        success(user); 
    }

    public void state()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        } 
 
        Long uid = getParaToLong("uid", 0L);
        if (uid == 0)
        {
            errorInvalid();
            return;
        }
        Integer state = getParaToInt("state", 0);

        User user = User.dao.findByUid(uid);
        if (user == null)
        {
            error("找不到该用户");
            return;
        }

        user.set(User.STATE, state);
        user.update();
        success(user);
    }

    // ---------------------------------------------------------------------------------
    class RewardThread implements Runnable
    {
        public void run()
        {
            List<User> allUser = User.dao.getInvites();
            for (User u : allUser) 
            {
                Long inviteid = u.getLong(User.INVITEID);
                reward(inviteid, 75, 1, 1, 6);    
            }
        }  
    }

    public void rewardAll()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        } 

        /*
        RewardThread rewardThread = new RewardThread();  
        Thread thread = new Thread(rewardThread);  
        thread.start();
        */

        Log.dao.add(getUid(), "user/rewardAll", "rewardAll");
        success(); 
    }

    private void reward(Long uid, Integer atnl, Integer idx, Integer start, Integer depth)
    {
        if (uid == 0 || atnl == 0)
        {
            return;
        }

        if (idx > depth)
        {
            return;
        }

        User user = User.dao.findByUid(uid);
        if (user == null)
        {
            return;
        }

        if (idx >= start)
        {
            user.set(User.LOCKATNL, user.getFloat(User.LOCKATNL) + atnl);
            user.update();
        }

        Long inviteid = user.getLong(User.INVITEID);
        if (inviteid != 0)
        {
            reward(inviteid, atnl / 2, idx + 1, start, depth);
        }
    }

    class RewardThread1000 implements Runnable
    {
        public void run()
        {
            List<User> allUser = User.dao.find("SELECT * FROM user WHERE id<=1018 AND inviteid>0");
            for (User u : allUser) 
            {
                User user = User.dao.findByUid(u.getLong(User.INVITEID));
                if (user != null)
                {
                    System.out.println("reward1000 " + user.getStr(User.PHONE) + " add 975");
                    user.set(User.LOCKATNL, user.getFloat(User.LOCKATNL) + 975);
                    user.update();
                }
            }

            System.out.println("reward1000 ok");
        }  
    }

    public void reward1000()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        } 
        //RewardThread1000 rewardThread = new RewardThread1000();  
        //Thread thread = new Thread(rewardThread);  
        //thread.start();

        Log.dao.add(getUid(), "user/reward1000", "rewardAll1000");
        success(); 
    }

    ///--------------------------------------------------------------------
    class MakeMThread implements Runnable
    {
        public void run()
        {
            List<User> allUser = User.dao.getInvites();
            for (User u : allUser) 
            {
                Long inviteid = u.getLong(User.INVITEID);
                makem(inviteid, 1, 6);    
            }
        }  
    }

    public void makem6()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        } 
        /*
        MakeMThread rewardThread = new MakeMThread();  
        Thread thread = new Thread(rewardThread);  
        thread.start();
        */
        
        Log.dao.add(getUid(), "user/makem6", "makem6");
        success(); 
    }

    private void makem(Long uid, Integer idx, Integer depth)
    {
        if (uid == 0)
        {
            return;
        }

        if (idx > depth)
        {
            return;
        }

        User user = User.dao.findByUid(uid);
        if (user == null)
        {
            return;
        }

        switch (idx)
        {
            case 1:
            user.set(User.M1, user.getLong(User.M1) + 1);
            break;
            case 2:
            user.set(User.M2, user.getLong(User.M2) + 1);
            break;
            case 3:
            user.set(User.M3, user.getLong(User.M3) + 1);
            break;
            case 4:
            user.set(User.M4, user.getLong(User.M4) + 1);
            break;
            case 5:
            user.set(User.M5, user.getLong(User.M5) + 1);
            break;
            case 6:
            user.set(User.M6, user.getLong(User.M6) + 1);
            break;
            default:
            break;
        }
        user.update();

        Long inviteid = user.getLong(User.INVITEID);
        if (inviteid != 0)
        {
            makem(inviteid, idx + 1, depth);
        }
    }
    
    /**
     * 直销用户列表
     */
    public void dUserList()
    {
    	if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        } 
    	int page = getParaToInt("page", 0);
		if (page < 0) {
			page = 0;
		}
		success(User.dao.getUserByType(page, 3));
    }
    /**
     * 撤销直销权限
     */
    public void revoke()
    {
    	if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        } 
 
        Long uid = getParaToLong("uid", 0L);
        if (uid == 0)
        {
            errorInvalid();
            return;
        }
        
        User user = User.dao.findByUid(uid);
        if (user == null)
        {
            error("找不到该用户");
            return;
        }

        user.set(User.UTYPE, 0);
        user.update();
        success(user);
    }
}