package com.app.controller;

import java.io.File;
import java.util.Date;

import com.app.config.Constants;
import com.jfinal.upload.UploadFile;

import n.fw.base.BaseController;

public class UploadController extends BaseController
{
    public void file()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        UploadFile uploadFile = getFile();
        if (uploadFile == null) {
			errorInvalid();
			return;
        }
        
        String filename = "_" + new Date().getTime() + "_" + uploadFile.getFileName();

        String realpath = Constants.UPLOAD_DIR + "/";
        realpath += filename;
        
        File file = uploadFile.getFile();
        File dest = new File(realpath);
        if (dest.exists()) {
            dest.delete();
		}
        
        file.renameTo(dest);
        //file.delete();
        
        realpath = Constants.IMAGE_URL + filename; 
        
        success(realpath);
    }
}