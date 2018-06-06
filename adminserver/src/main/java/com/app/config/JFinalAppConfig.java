package com.app.config;

import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.wall.WallFilter;
import com.app.controller.AdminController;
import com.app.controller.DeviceController;
import com.app.controller.InviteController;
import com.app.controller.LogController;
import com.app.controller.OrderController;
import com.app.controller.ProductController;
import com.app.controller.QuanController;
import com.app.controller.SheepController;
import com.app.controller.UploadController;
import com.app.controller.UserController;
import com.app.controller.VoteController;
import com.app.model.Admin;
import com.app.model.Device;
import com.app.model.Invite;
import com.app.model.Log;
import com.app.model.Order;
import com.app.model.Product;
import com.app.model.Quan;
import com.app.model.Sheep;
import com.app.model.User;
import com.app.model.Vote;
import com.app.model.VoteSeed;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;

/**
 * Created by Nijie on 2016/5/18.
 */
public class JFinalAppConfig extends JFinalConfig {

    @Override
    public void configConstant(Constants me) {
        loadPropertyFile("config.properties");
        me.setDevMode(getPropertyToBoolean("devMode", false));
        me.setEncoding("UTF-8");
        me.setBaseUploadPath(getProperty("upload.dir", "/data/www/mnfs/upload/"));
		me.setMaxPostSize(getPropertyToInt("upload.maxSize", 20480000));
    }

    @Override
    public void configRoute(Routes routes) {
        routes.add("/admin", AdminController.class);
        routes.add("/device", DeviceController.class);
        routes.add("/order", OrderController.class);
        routes.add("/quan", QuanController.class);
        routes.add("/sheep", SheepController.class);
        routes.add("/user", UserController.class);
        routes.add("/product", ProductController.class);
        routes.add("/upload", UploadController.class);
        routes.add("/invite", InviteController.class);
        routes.add("/log", LogController.class);
        routes.add("/vote", VoteController.class);
    }

    @Override
    public void configPlugin(Plugins me) {
        int initialSize = getPropertyToInt("initialSize");
        int minIdle = getPropertyToInt("minIdle");
        int maxActive = getPropertyToInt("maxActive");

        DruidPlugin druidPlugin = new DruidPlugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password").trim());
        druidPlugin.set(initialSize, minIdle, maxActive);

        WallFilter wallDefault = new WallFilter();
        wallDefault.setDbType(JdbcConstants.MYSQL);
        druidPlugin.addFilter(wallDefault);
        druidPlugin.setFilters("stat,wall");
        druidPlugin.start();

        me.add(druidPlugin);

        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        arp.setShowSql(getPropertyToBoolean("showSql", true));
        me.add(arp);
        arp.addMapping("user", User.class);	// 映射user 表到 User模型
        arp.addMapping("device", Device.class);	
        arp.addMapping("admin", Admin.class);
        arp.addMapping("order", Order.class);
        arp.addMapping("product", Product.class);
        arp.addMapping("quan", Quan.class);
        arp.addMapping("sheep", Sheep.class);
        arp.addMapping("invite", Invite.class);
        arp.addMapping("log", Log.class);
        arp.addMapping("vote", Vote.class);
        arp.addMapping("voteseed", VoteSeed.class);
    }

    @Override
    public void configInterceptor(Interceptors me) {
    	//me.addGlobalActionInterceptor(new CommonInterceptor());
    }

    @Override
    public void configHandler(Handlers me) {
    	me.add(new UrlSkipHandler(".*/services.*",false));  
    }
    
    @Override
    public void afterJFinalStart()
    {
    	//MessageManager.Init();
    	//new QuartzCronJob(new QuartzKey(1, "test", "test"), "0 */12 * * * ?", MessageManager.class).addParam("name", "quartz").start();
    }

    public static void main(String[] args) {
        JFinal.start("src/main/webapp", 80, "/", 5);
    }
}
