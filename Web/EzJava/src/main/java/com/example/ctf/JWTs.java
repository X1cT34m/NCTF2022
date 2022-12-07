package com.example.ctf;


import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;


public class JWTs {

    private static final String SIGNATURE_ALGORITHM = "SHA-1";
    private static final String ALGORITHM = "DSA";
    private  static final BigInteger zero=BigInteger.valueOf(0);
    private  static final BigInteger one=BigInteger.valueOf(1);
    private static final int KEY_SIZE = 512;
    private final static int accuracy = 100;//素数的准确率为1-(2^(-accuracy))
    BigInteger p,q,h,g;
    BigInteger y,x,r,s;

    private static BigInteger expMode(BigInteger base, BigInteger exp, BigInteger mod) {
        BigInteger res = BigInteger.ONE;
        //拷贝一份防止修改原引用
        BigInteger tempBase = new BigInteger(base.toString());
        for (int i = 0; i < exp.bitLength(); i++) {
            if (exp.testBit(i)) {//判断对应二进制位是否为1
                res = (res.multiply(tempBase)).mod(mod);
            }
            tempBase = tempBase.multiply(tempBase).mod(mod);
        }
        return res;
    }

    public JWTs(){}

    //    该方法取一个随机x，x小于q并且大于0，
    private static BigInteger randbint(BigInteger n) {
        Random rnd = new Random();
        int maxNumBitLength = n.bitLength();
        BigInteger aRandomBigInt;
        do {
            aRandomBigInt = new BigInteger(maxNumBitLength, rnd);
        } while (aRandomBigInt.compareTo(n) > 0);
        return aRandomBigInt;
    }

    public static String getSha1(String str) {
        char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] digest = md.digest();

            StringBuffer hexstr = new StringBuffer();
            String shaHex = "";
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            String mySignature = hexstr.toString();
            return mySignature;
        } catch (NoSuchAlgorithmException e) {
            return "签名验证错误";
        }
    }

    public static void init(JWTs dsa){

        Random random = new Random();
        dsa.q = BigInteger.probablePrime(160, new Random());
        while (!dsa.q.isProbablePrime(accuracy)) {
            dsa.q = BigInteger.probablePrime(160, new Random());
        }

        int L=random.nextInt(8) * 64 + 352;
        BigInteger mul=new BigInteger(L, new Random());
        dsa.p = dsa.q.multiply(mul).add(one);
        while (!dsa.p.isProbablePrime(accuracy)) {
            L=random.nextInt(8) * 64 + 352;
            mul= new BigInteger(L, new Random());
            dsa.p = dsa.q.multiply(mul).add(one);
        }

        BigInteger a=dsa.p.subtract(one).divide(dsa.q);
        dsa.h= new BigInteger(300, new Random());
        dsa.g=expMode(dsa.h,a,dsa.p);
        while (dsa.g.compareTo(one)!=1) {
            dsa.h= new BigInteger(300, new Random());
            dsa.g=expMode(dsa.h,a,dsa.p);
        }

    }

    public static void getKey(JWTs dsa){
        dsa.x = randbint(dsa.q);
        dsa.y=expMode(dsa.g,dsa.x,dsa.p);

    }

//    public static String sign(BigInteger M, JWTs dsa){
//        BigInteger k = randbint(dsa.q);
//        dsa.r=(expMode(dsa.g,k,dsa.p)).mod(dsa.q);
//        BigInteger two=BigInteger.valueOf(2);
//        BigInteger ink=expMode(k,(dsa.q.subtract(two)),dsa.q);
//        BigInteger m=M.add(dsa.x.multiply(dsa.r));
//        dsa.s=(ink.multiply(m)).mod(dsa.q);
//        String content = String.format("%s.%s", dsa.r, dsa.s);
//        return content;
//    }

    public static boolean verify(BigInteger M, BigInteger r, BigInteger s, JWTs dsa){
        BigInteger two=BigInteger.valueOf(2);
        BigInteger w=expMode(s,(dsa.q.subtract(two)),dsa.q);
        BigInteger u1=(M.multiply(w)).mod(dsa.q);
        BigInteger u2=(r.multiply(w)).mod(dsa.q);
        BigInteger v1=expMode(dsa.g,u1,dsa.p);
        BigInteger v2=expMode(dsa.y,u2,dsa.p);
        BigInteger v=((v1.multiply(v2)).mod(dsa.p)).mod(dsa.q);


        if(v.equals(r))
        {
            return true;
        }
        else {
            return false;
        }

    }
//    public static String generateToken() throws Exception {
//        JWTs dsa=new JWTs();
//        init(dsa);
//        getKey(dsa);
//       // JSONObject payload = new JSONObject();
//        ObjectMapper objectMapper = new ObjectMapper();
//        UserBean userBean = new UserBean();
//        userBean.setiss("Pupi1");
//        userBean.setadmin(false);
//        String json = objectMapper.writeValueAsString(userBean);
//        String payloadB64 = Base64.getUrlEncoder().encodeToString(json.getBytes());
//        String content = getSha1(payloadB64);
//        BigInteger Hm=new BigInteger(content,16);
//        String contents = String.format("%s.%s", payloadB64,sign(Hm,dsa));
//        return contents;
//
//    }
public static String sign(BigInteger M, JWTs dsa) {
    BigInteger k = randbint(dsa.q);
    dsa.r = expMode(dsa.g, k, dsa.p).mod(dsa.q);
    BigInteger two = BigInteger.valueOf(2L);
    BigInteger ink = expMode(k, dsa.q.subtract(two), dsa.q);
    BigInteger m = M.add(dsa.x.multiply(dsa.r));
    dsa.s = ink.multiply(m).mod(dsa.q);
    String content = String.format("%s.%s", dsa.r, dsa.s);
    return content;
}
public static String generateToken(String user,JWTs dsa) throws Exception {

    JSONObject payload = new JSONObject();
    payload.put("iss", "Pupi1");
    payload.put("Alibanana", Base64.getUrlEncoder().encodeToString(user.getBytes()));
    String payloadB64 = Base64.getUrlEncoder().encodeToString(payload.toJSONString().getBytes());
    String content = getSha1(payloadB64);
    BigInteger Hm = new BigInteger(content, 16);
    String contents = String.format("%s.%s", payloadB64, sign(Hm, dsa));
    return contents;
}
    public static Object verifyToken(String token,JWTs dsa) throws IOException {
//        JWTs dsa=new JWTs();
//        init(dsa);
//        getKey(dsa);
//        JWTs d=new JWTs();
        String payloadB64 = token.split("\\.")[0];
        String sign_r = token.split("\\.")[1];
        String sign_s = token.split("\\.")[2];
        String content = getSha1(payloadB64);
        BigInteger Hm=new BigInteger(content,16);
        String payloadDecodeString = new String(Base64.getUrlDecoder().decode(payloadB64));
       JSONObject payload = JSON.parseObject(payloadDecodeString);
        if(verify(Hm, new BigInteger(sign_r), new BigInteger(sign_s), dsa)){

            String s = payload.getString("Alibanana");
            if (s.hashCode()=="WelComeToNCTF2022".hashCode()&&!(s.equals("WelComeToNCTF2022"))){
                return true;
            }
        }
        return false;
    }



}

