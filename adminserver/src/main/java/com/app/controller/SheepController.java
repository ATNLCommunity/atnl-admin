package com.app.controller;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.spongycastle.util.encoders.Base64;

import com.app.model.BasicRecord;
import com.app.model.Device;
import com.app.model.DistanceRecord;
import com.app.model.EventRecord;
import com.app.model.GpsRecord;
import com.app.model.Log;
import com.app.model.Sheep;
import com.app.model.StepRecord;
import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.nebulas.account.AccountManager;
import io.nebulas.client.NebulasClient;
import io.nebulas.client.api.request.GetAccountStateRequest;
import io.nebulas.client.api.request.SendRawTransactionRequest;
import io.nebulas.client.api.response.AccountState;
import io.nebulas.client.api.response.RawTransaction;
import io.nebulas.client.api.response.Response;
import io.nebulas.client.impl.HttpNebulasClient;
import io.nebulas.core.Address;
import io.nebulas.core.Transaction;
import io.nebulas.core.TransactionBinaryPayload;
import io.nebulas.core.TransactionCallPayload;
import io.nebulas.core.TransactionDeployPayload;
import n.fw.base.BaseController;
import n.fw.utils.DateUtils;
import n.fw.utils.FileUtils;
import net.sf.json.JSONObject;
import rpcpb.ApiServiceGrpc;
import rpcpb.Rpc;

public class SheepController extends BaseController
{
    public void list()
    {
        if (!checkLevel(1))
        {
            errorInvalidOper();
            return;
        }

        String sid = getPara("sid", "");
        Integer page = getParaToInt("page", 0);
        
        HashMap<String, Object> map = new HashMap<String,Object>();
        List<Sheep> arr = Sheep.dao.list(sid, page);
        map.put("arr", arr);
        map.put("count", Sheep.dao.count(sid));
        success(map);
    }

    public void add()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }
        String sid = getPara("sid", "");
        if(sid.length() == 0)
        {
        	error("档案编号不能为空");
        	return;
        }
        float price = Float.parseFloat(getPara("price",""));
        if(price == 0f)
        {
        	error("售卖价格不能为空");
        	return;
        }
        float weight = Float.parseFloat(getPara("weight",""));
        float height = Float.parseFloat(getPara("height",""));
        if(weight == 0f || height == 0f)
        {
        	error("体重身长不能为空");
        	return;
        }
        String birthday = getPara("birthday", "");
        String prekilltime = getPara("prekilltime", "");
        if(birthday.trim().length() == 0 || prekilltime.trim().length() == 0)
        {
        	error("出生日期和预收获日期不能为空");
        	return;
        }       
        Sheep sheep = Sheep.dao.create(sid,price,birthday,weight,height,prekilltime);
        if (sheep != null)
        {
            Log.dao.add(getUid(), "sheep/create", "create shepp - id=" + sheep.getLong(Sheep.ID) + " sid=" + sid);
        }
        this.createAddress(sheep.getLong(Sheep.ID));
        success(sheep);
    }
    
    /**
     * 绑定设备
     */
    public void bindDevice()
    {
    	if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }
    	Long sheepid = getParaToLong("sheepid", 0L);
    	if(sheepid == 0L)
    	{
    		error("不存在的羊羔ID");
    		return;
    	}
    	Long did = getParaToLong("did", 0L);
    	if(did == 0L)
    	{
    		error("不存在的设备ID");
    		return;
    	}
    	Sheep sheep = Sheep.dao.getBySheepId(sheepid);
        if (sheep == null)
        {
        	error("该羊羔已经不存在了");
            return;
        }
        if(null != sheep.getLong(Sheep.DID) && sheep.getLong(Sheep.DID) > 0 && did != sheep.getLong(Sheep.DID))
        {
        	Device device = Device.dao.findById(sheep.getLong(Sheep.DID));
        	if(null != device)
        	{
        		device.set(Device.BINDSTATE, 0);
            	device.update();
        	}
        }
    	float weight = Float.parseFloat(getPara("weight",""));
    	if(weight <= 0f || sheep.getFloat(Sheep.WEIGHT) > weight)
    	{
    		error("错误的体重信息");
    		return;
    	}
        float height = Float.parseFloat(getPara("height",""));
        if(height <= 0f ||sheep.getFloat(Sheep.HEIGHT) > height)
    	{
    		error("错误的身长信息");
    		return;
    	}
    	sheep.set(Sheep.DID, did);
    	sheep.set(Sheep.ADDRESS, this.concatAddress(sheepid));
    	sheep.update();
    	Device device = Device.dao.findById(did);
    	if(null != device)
    	{
    		device.set(Device.BINDSTATE, 1);
        	device.update();
    	}
    	BasicRecord.dao.create(sheepid, weight, height, weight - sheep.getFloat(Sheep.WEIGHT), height - sheep.getFloat(Sheep.HEIGHT));
        
    	success(sheep);
    }
    /**
     * 修改羊的信息
     */
    public void updateSheep()
    {
    	if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }
        
        String sid = getPara("sid", "");
        if(sid.length() == 0)
        {
        	error("档案编号不能为空");
        	return;
        }
        float price = Float.parseFloat(getPara("price",""));
        if(price == 0f)
        {
        	error("售卖价格不能为空");
        	return;
        }
        float weight = Float.parseFloat(getPara("weight",""));
        float height = Float.parseFloat(getPara("height",""));
        if(weight == 0f || height == 0f)
        {
        	error("体重身长不能为空");
        	return;
        }
        String birthday = getPara("birthday", "");
        String prekilltime = getPara("prekilltime", "");
        if(birthday.trim().length() == 0 || prekilltime.trim().length() == 0)
        {
        	error("出生日期和预收获日期不能为空");
        	return;
        }        
        Long sheepid = getParaToLong("sheepid", 0L);
    	if(sheepid == 0L)
    	{
    		error("不存在的羊羔ID");
    		return;
    	}
    	
    	Sheep sheep = Sheep.dao.getBySheepId(sheepid);
        if (sheep == null)
        {
        	error("该羊羔已经不存在了");
            return;
        }
        
        sheep.set(Sheep.SID, sid);
        sheep.set(Sheep.BIRTHDAY, birthday);
        sheep.set(Sheep.PRICE, price);
        sheep.set(Sheep.WEIGHT, weight);
        sheep.set(Sheep.HEIGHT, height);
        sheep.set(Sheep.PREKILLTIME, prekilltime);
        sheep.update();
        success(sheep);
        
        
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
            Sheep.dao.delete(id);
            Log.dao.add(getUid(), "sheep/delete", "delete sheep - id=" + id);
        }
        success();
    }
    
    public void addBasicRecord()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long sheepid = getParaToLong("sheepid", 0l);
		if (sheepid <= 0)
        {
            error("该羊羔已经不存在了");
            return;
        }
        Sheep sheep = Sheep.dao.findById(sheepid);
        if (sheep == null)
        {
        	error("该羊羔已经不存在了");
            return;
        }
        float weight = Float.parseFloat(getPara("weight"));
        if(weight <= 0)
        {
        	error("错误的体重数据");
            return;
        }
        float height = Float.parseFloat(getPara("height"));
        if(height <= 0)
        {
        	error("错误的身长数据");
            return;
        }
        float diffw = 0f;
        float diffh = 0f;
        BasicRecord leastRecord = BasicRecord.dao.getLeastRecord(sheepid);
        if(null == leastRecord)
        {
        	diffw = weight - sheep.getFloat(Sheep.WEIGHT);
        	diffh = height - sheep.getFloat(Sheep.HEIGHT);
        }
        else
        {
        	diffw = weight - leastRecord.getFloat(Sheep.WEIGHT);
        	diffh = height - leastRecord.getFloat(Sheep.HEIGHT);
        }
        
        BasicRecord basicRecord = BasicRecord.dao.create(sheepid, weight, height, diffw, diffh);
        success(basicRecord);
    }
    
    public void addDistanceRecord()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long sheepid = getParaToLong("sheepid", 0l);
		if (sheepid <= 0)
        {
            error("该羊羔已经不存在了");
            return;
        }
        Sheep sheep = Sheep.dao.findById(sheepid);
        if (sheep == null)
        {
        	error("该羊羔已经不存在了");
            return;
        }
        float distance = Float.parseFloat(getPara("distance"));
        if(distance <= 0)
        {
        	error("错误的距离数据");
            return;
        }
        DistanceRecord distanceRecord = DistanceRecord.dao.create(sheepid, distance);
        success(distanceRecord);
    }
    
    public void addEventRecord()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long sheepid = getParaToLong("sheepid", 0l);
		if (sheepid <= 0)
        {
            error("该羊羔已经不存在了");
            return;
        }
        Sheep sheep = Sheep.dao.findById(sheepid);
        if (sheep == null)
        {
        	error("该羊羔已经不存在了");
            return;
        }
        String event = getPara("event","");
        if(event == "")
        {
        	error("事件消息不能为空");
            return;
        }
        EventRecord eventRecord = EventRecord.dao.create(sheepid, event);
        success(eventRecord);
    }
    
    
    public void addStepRecord()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long sheepid = getParaToLong("sheepid", 0l);
		if (sheepid <= 0)
        {
            error("该羊羔已经不存在了");
            return;
        }
        Sheep sheep = Sheep.dao.findById(sheepid);
        
        if (sheep == null)
        {
        	error("该羊羔已经不存在了");
            return;
        }
        int steps = getParaToInt("steps",0);
        if(steps == 0)
        {
        	error("错误的计步数据");
            return;
        }
        StepRecord stepRecord = StepRecord.dao.create(sheepid, steps);
        success(stepRecord);
    }
    
    public void addGpsRecord()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long sheepid = getParaToLong("sheepid", 0l);
		if (sheepid <= 0)
        {
            error("该羊羔已经不存在了");
            return;
        }
        Sheep sheep = Sheep.dao.findById(sheepid);
        
        if (sheep == null)
        {
        	error("该羊羔已经不存在了");
            return;
        }
        float lon = Float.parseFloat(getPara("lon"));

        float lat = Float.parseFloat(getPara("lat"));

        float high = Float.parseFloat(getPara("high"));

        GpsRecord gpsRecord = GpsRecord.dao.create(sheepid, lon, lat, high);
        success(gpsRecord);
    }
    
    private void createAddress(long sheepid)
    {
    	AccountManager manager;
		try {
			manager = new AccountManager();
			byte[] passphrase = "atnl123".getBytes();
	    	byte[] walletFile = "{\"version\":4,\"id\":\"56e8fc70-f676-4c8d-91a2-b19e49a6dc2b\",\"address\":\"n1bHcQ4UCeQb4JbsDDhKtHwwcXLKCCCWXDk\",\"crypto\":{\"ciphertext\":\"f12c2a7b41b40b63189f247e8b0b63932d2867f06eb1e758561ce597b0b2d2ae\",\"cipherparams\":{\"iv\":\"7a00c94de9adc98064baf78a4d451e2b\"},\"cipher\":\"aes-128-ctr\",\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"salt\":\"7f48eebf5d571c924ff0a2170a3125baefd023c1ba352f9bc4f66b11324e1182\",\"n\":4096,\"r\":8,\"p\":1},\"mac\":\"f5db617f67d74835b82508b7e6cacfa557f2409b012b4fae2f567ca7fad360c9\",\"machash\":\"sha3256\"}}".getBytes();
	        Address from = manager.load(walletFile, passphrase);
	        int chainID = 1001; //1 mainet,1001 testnet, 100 default private
	        Address to = manager.newAccount(passphrase);
	        byte[] walletFile1 = manager.export(to,passphrase);
	        //System.out.println(to.string());
	        //System.out.println("exported KeyJson data is: " + DatatypeConverter.printHexBinary(walletFile));
	        OutputStream out = new FileOutputStream("/data/sheep/"+sheepid+".json");
	        InputStream is = new ByteArrayInputStream(walletFile1);
	        byte[] buff = new byte[1024];
	        int len = 0;
	        while((len=is.read(buff))!=-1){
	            out.write(buff, 0, len);
	        }
	        is.close();
	        out.close();
	        BigInteger value = new BigInteger("100000");
	        NebulasClient nebulasClient = HttpNebulasClient.create("https://testnet.nebulas.io");
	        Response<AccountState> accountResponse = nebulasClient.getAccountState(new GetAccountStateRequest("n1bHcQ4UCeQb4JbsDDhKtHwwcXLKCCCWXDk"));        
	        long nonce = accountResponse.getResult().getNonce() + 1;
	        Transaction.PayloadType payloadType = Transaction.PayloadType.BINARY;
	        byte[] payload = new TransactionBinaryPayload(null).toBytes();

	        BigInteger gasPrice = new BigInteger("1000000"); // 0 < gasPrice < 10^12
	        BigInteger gasLimit = new BigInteger("2000000"); // 20000 < gasPrice < 50*10^9
	        Transaction tx = new Transaction(chainID, from, to, value, nonce, payloadType, payload, gasPrice, gasLimit);

	        manager.signTransaction(tx, passphrase);
	        byte[] rawData = tx.toProto();
	        String rawTransaction = Base64.toBase64String(rawData);

	        Response<RawTransaction> response = nebulasClient.sendRawTransaction(new SendRawTransactionRequest().setData(rawTransaction));
	        Log.dao.add(getUid(), "sheep/add", "block tx=" + response.getResult().getTxHash());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private String concatAddress(long sheepid)
    {
    	try {
			AccountManager manager = new AccountManager();
			byte[] passphrase = "atnl123".getBytes();
	        // binary tx
	        int chainID = 1001; //1 mainet,1001 testnet, 100 default private
	        byte[] walletFile = FileUtils.changeFileToByte("/data/sheep/"+sheepid+".json");
	        Address from = manager.load(walletFile, passphrase);
	        BigInteger value = new BigInteger("0");
	        NebulasClient nebulasClient = HttpNebulasClient.create("https://testnet.nebulas.io");
	        Response<AccountState> accountResponse = nebulasClient.getAccountState(new GetAccountStateRequest("n1YXiUTnLwVxN5WvJdagzTE8wxqNSuvAtG6"));        
	        long nonce = accountResponse.getResult().getNonce() + 1;
	        Transaction.PayloadType payloadType = Transaction.PayloadType.DEPLOY;
	        
	        byte[] payload = new TransactionDeployPayload("js", "'use strict';var ATNLContract = function() {};ATNLContract.prototype = {init: function() {},set: function(name, value) {LocalContractStorage.set(name, value);},get: function(name) {return LocalContractStorage.get(name);}};module.exports = ATNLContract;", "").toBytes();
	        BigInteger gasPrice = new BigInteger("1000000"); // 0 < gasPrice < 10^12
	        BigInteger gasLimit = new BigInteger("2000000"); // 20000 < gasPrice < 50*10^9
	        Transaction tx = new Transaction(chainID, from, from, value, nonce, payloadType, payload, gasPrice, gasLimit);

	        manager.signTransaction(tx, passphrase);
	        byte[] rawData = tx.toProto();
	        String rawTransaction = Base64.toBase64String(rawData);

	        Response<RawTransaction> response = nebulasClient.sendRawTransaction(new SendRawTransactionRequest().setData(rawTransaction));
	       
	        return response.getResult().getContractAddress();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    	return "";
    }
	/**
     * 区块链数据上传
     */
    public void dataUpload()
    {
    	Long sheepid = getParaToLong("sheepid", 0l);
		if (sheepid <= 0)
        {
            error("该羊羔已经不存在了");
            return;
        }
		Sheep sheep = Sheep.dao.findById(sheepid);
		if(null == sheep)
		{
			error("该羊羔已经不存在了");
            return;
		}
		//success(sheep);
    	AccountManager manager;
		try {
			manager = new AccountManager();
		
	    	byte[] passphrase = "atnl123".getBytes();
	        // binary tx
	        int chainID = 1001; //1 mainet,1001 testnet, 100 default private
	        Address from;
	        Address to;
	        byte[] walletFile = "{\"version\":4,\"id\":\"56e8fc70-f676-4c8d-91a2-b19e49a6dc2b\",\"address\":\"n1bHcQ4UCeQb4JbsDDhKtHwwcXLKCCCWXDk\",\"crypto\":{\"ciphertext\":\"f12c2a7b41b40b63189f247e8b0b63932d2867f06eb1e758561ce597b0b2d2ae\",\"cipherparams\":{\"iv\":\"7a00c94de9adc98064baf78a4d451e2b\"},\"cipher\":\"aes-128-ctr\",\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"salt\":\"7f48eebf5d571c924ff0a2170a3125baefd023c1ba352f9bc4f66b11324e1182\",\"n\":4096,\"r\":8,\"p\":1},\"mac\":\"f5db617f67d74835b82508b7e6cacfa557f2409b012b4fae2f567ca7fad360c9\",\"machash\":\"sha3256\"}}".getBytes();
	        from = manager.load(walletFile, passphrase);
	        if("".equals(sheep.getStr(Sheep.ADDRESS)) || null == sheep.getStr(Sheep.ADDRESS))
	    	{
	        	error("该羊羔区块链地址不存在了");
	            return;
	    	}
	        else
	        {
	        	to = Address.ParseFromString(sheep.getStr(Sheep.ADDRESS).toString());	
	        }
	        BigInteger value = new BigInteger("0");
	        NebulasClient nebulasClient = HttpNebulasClient.create("https://testnet.nebulas.io");
	        Response<AccountState> accountResponse = nebulasClient.getAccountState(new GetAccountStateRequest("n1bHcQ4UCeQb4JbsDDhKtHwwcXLKCCCWXDk"));        
	        long nonce = accountResponse.getResult().getNonce() + 1;
	        Transaction.PayloadType payloadType = Transaction.PayloadType.CALL;
	        StringBuffer sb = new StringBuffer();
	        sb.append("出生日期:"+sheep.getDate(Sheep.BIRTHDAY)+";");
	        sb.append("源羊编号:"+sheep.getStr(Sheep.SID)+";");
	        BasicRecord br = BasicRecord.dao.getLeastRecord(sheepid);
	        String height = "";
	        String weight = ""; 
	        String recordtime = "";
	        if(null != br)
	        {
	        	height = br.getFloat(BasicRecord.HEIGHT)+"";
	        	weight = br.getFloat(BasicRecord.WEIGHT)+"";
	        	recordtime = br.getDate(BasicRecord.RECORDTIME).toString();
	        }
	        Long sumSteps = StepRecord.dao.getSum(sheepid);
	        sb.append("身长:" + height + ";");
	        sb.append("体重:" + weight + ";"); 
	        sb.append("身长体重记录时间:" + recordtime + ";");
	        sb.append("上链时累计活动量："+sumSteps+";");
	        
	      
	        byte[] payload = new TransactionCallPayload("set","[\"sheepid:"+sheepid+"\",\""+sb.toString()+"\"]").toBytes();
	        BigInteger gasPrice = new BigInteger("1000000"); // 0 < gasPrice < 10^12
	        BigInteger gasLimit = new BigInteger("2000000"); // 20000 < gasPrice < 50*10^9
	        Transaction tx = new Transaction(chainID, from, to, value, nonce, payloadType, payload, gasPrice, gasLimit);
	
	        manager.signTransaction(tx, passphrase);
	        byte[] rawData = tx.toProto();
	        String rawTransaction = Base64.toBase64String(rawData);
	
	        Response<RawTransaction> response = nebulasClient.sendRawTransaction(new SendRawTransactionRequest().setData(rawTransaction));
	        success(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}