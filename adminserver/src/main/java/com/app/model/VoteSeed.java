package com.app.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

// 投票选项表
public class VoteSeed extends Model<VoteSeed>
{
    private static final long serialVersionUID = 3554755307155224125L;

    public static final VoteSeed dao = new VoteSeed();

    public static final String ID = "id";       // 选项id
    public static final String NAME = "name";   // 选项名称
    public static final String RATE = "rate";   // 胜率
    public static final String LOGO = "logo";   // logo图片地址

    public List<VoteSeed> getAll()
    {
        return find("SELECT * FROM voteseed");
    }

    public VoteSeed findBySid(Long id)
    {
        return findFirst("SELECT * FROM voteseed WHERE id=?", id);
    }
}