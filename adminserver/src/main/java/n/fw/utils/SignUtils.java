package n.fw.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class SignUtils
{
    public static String sign(String str, String key)
    {
        String code = str + "#" + key;
        try {
            String encodeStr = DigestUtils.md5Hex(code);
            return encodeStr;
        } catch (Exception e) {
            //TODO: handle exception
            return "";
        }
    }
}