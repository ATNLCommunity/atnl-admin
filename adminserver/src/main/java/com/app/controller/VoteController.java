package com.app.controller;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import n.fw.base.BaseController;

public class VoteController extends BaseController
{
    public void list()
    {
        if (!checkLevel(1))
        {
            errorInvalidOper();
            return;
        }

        List<Record> arr = Db.query("SELECT idx,COUNT(1) FROM vote GROUP BY idx");
        success(arr);
    }
}