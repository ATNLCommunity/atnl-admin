package com.app.controller;

import java.util.List;

import com.app.model.Invite;
import com.app.model.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import n.fw.base.BaseController;

public class InviteController extends BaseController
{
    public void get()
    {
        if (!checkLevel(1))
        {
            errorInvalidOper();
            return;
        }
        success(Invite.dao.get());
    }

    public void edit()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Float reg = Float.valueOf(getPara("reg", "0"));
        Float invite = Float.valueOf(getPara("invite", "0"));
        Integer count = getParaToInt("count", 0);

        boolean bNew = false;
        Invite in = Invite.dao.get();
        if (invite == null)
        {
            bNew = true;
            in = new Invite();
        }

        in.set(Invite.REG, reg);
        in.set(Invite.INVITE, invite);
        in.set(Invite.COUNT, count);
        if (bNew)
        {
            in.save();
        }
        else 
        {
            in.update();
        }
        Log.dao.add(getUid(), "invite/edit", "invite edit - reg=" + reg + " invite=" + invite + " count=" + count);
        success(in);
    }

    public void rank()
    {
        if (!checkLevel(1))
        {
            errorInvalidOper();
            return;
        }

        Integer page = getParaToInt("page", 0);

        List<Record> list = Db.find("SELECt user.*,count FROM (SELECT COUNT(1) AS count,inviteid FROM user GROUP BY inviteid) u INNER JOIN user ON user.id=u.inviteid ORDER BY COUNT DESC LIMIT ?,20", page * 20);
        success(list);
    }
}