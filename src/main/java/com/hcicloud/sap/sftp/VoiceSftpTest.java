package com.hcicloud.sap.sftp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hcicloud.sap.common.DateConversion;
import com.hcicloud.sap.common.FileUtils;
import com.hcicloud.sap.common.RedisUtil;
import com.hcicloud.sap.common.SftpClient;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.util.Calendar;

public class VoiceSftpTest {



    private static int MAX_NUM = 10;
    private static int numTotal = 100000;

    private static int numFailureTotal = 0;

    //本地的目录前后必须有 "/"
    private static String successFtpVoicePath = "/home/mhn/pinganftp/";
    //磁盘的根目录下的子目录   /nfsc/gcc……/下的   同时前面不能加 "/"  后面必须有 "/"
    private static String remoteVoicePath = "";
    private static String localTextName = successFtpVoicePath + File.separator + "success/" + "success.txt";
    private static String localFailureName = successFtpVoicePath + File.separator + "success/" + "failure.txt";

//    public static void main(String[] args){
//        // TODO 1.连接redis，读取数据   2.本地文件记录推送的文件名  3.推送到指定的目录
//
//        int num = 0;
//        long time1 = System.currentTimeMillis();
//
//        SftpClient sftpClient = new SftpClient();
//
//        while (true) {
//            String sendPostGreKey = "testKey";
//            Jedis jedis = null;
//
//            //TODO 记得设置根目录文件夹
//
//            JSONArray array = new JSONArray();
//            String localName = "";
//            String remoteName = "";
//
//            try {
//                jedis = RedisUtil.getJedis();
//                System.out.println("Redis连接成功！");
//
//                for (int i = 0; i < MAX_NUM; i++) {
//                    String successMsg = jedis.lpop(sendPostGreKey);
//                    if (successMsg != null && successMsg.trim().length() > 0) {
//                        array.add(JSONObject.parseObject(successMsg));
//                    } else {
//                        break;
//                    }
//                }
//
//                if(array.size()>0) {
//                    //这里是推送到平安方的FTP服务器中
//                    for (int i = 0; i < array.size(); i++) {
//                        JSONObject successJsonObject = array.getJSONObject(i);
////                        JSONObject successJsonObject =  new JSONObject(array.getJSONObject(i));
//                        String content = successJsonObject.getString("content");//内容
//                        String callTime = successJsonObject.getString("callTime");//录音开始时间
//                        String voiceId = successJsonObject.getString("voiceId");//录音流水号
//
//                        System.out.println("当前录音流水号为："+voiceId);
//
//                        String callContentReal = "";
//                        boolean flagWirteText = false;
//
//                        //TODO 1.生成本地的文件，同时写入文本   2.推送ftp  3.删除文件
//
//                        //SpeechAnalytics/sinovoice/text/
//
//                        //这里转化为  年月日时分
//                        callTime = DateConversion.DateToStr(DateConversion.StrToDate(callTime,"yyyy-MM-dd HH:mm:ss"),"yyyyMMddHHmm");
//
//                        //开始文件的创建与校验
//                        if(!successFtpVoicePath.endsWith(File.separator)){
//                            successFtpVoicePath += File.separator;
//                        }
//
//                        localName = successFtpVoicePath + callTime + "_" + voiceId + ".txt";
//
//                        //因为有  年/月/日  文件夹的限制
//                        Calendar nowTime = Calendar.getInstance();
//                        remoteName = remoteVoicePath + nowTime.get(Calendar.YEAR) + File.separator + (nowTime.get(Calendar.MONTH) + 1) + File.separator + nowTime.get(Calendar.DAY_OF_MONTH) + File.separator + callTime + "_" + voiceId + ".txt";
//                        if(!FileUtils.isFileExist(localName)){
//                            JSONArray contentArray = JSONObject.parseArray(content);
//                            if (contentArray != null) {
//                                for (int k = 0; k < contentArray.size(); k++) {
//                                    JSONObject jsonObject = contentArray.getJSONObject(k);
//                                    String result = jsonObject.getString("content");
//                                    if (result != null) {
//                                        String resultNew = result.substring(0, result.indexOf(";time="));
//                                        if (resultNew != null && resultNew.trim().length() > 0 && resultNew != "\"\"") {
//                                            callContentReal += result.substring(0, result.indexOf(";time=")) + "☆";
//                                        }
//                                    }
//                                }
//                            }
//                            flagWirteText = FileUtils.writeFile(localName,callContentReal,false);
//                            String textMeg = callTime + "_" + voiceId + ".txt" +"\r\n";
//                            FileUtils.writeFile(localTextName,textMeg,true);
//                        }else{
//                            flagWirteText = true;
//                        }
//
//                        //推送FTP服务器，创建文件成功的时候
//                        boolean flag = false;
//                        if (flagWirteText) {
//                            System.out.println("本地文件：" + localName);
//                            System.out.println("本地文件的大小为：" + FileUtils.getFileSize(localName));
//                            System.out.println("远程文件：" + remoteName);
//                            flag = sftpClient.uploadFile(localName,remoteName);
//                        }
//
//                        jedis.rpush(sendPostGreKey, array.get(i).toString());
//
//                        if (!flagWirteText || !flag) {
//                            numFailureTotal++;
//                            System.out.println("当前的失败条数为" + numFailureTotal);
//                            String textMeg = callTime + "_" + voiceId + ".txt" +"\r\n";
//                            FileUtils.writeFile(localFailureName,textMeg,true);
//
////                            FileUtils.deleteFile(localName);
//                            if (!flag) {
//                                System.out.println("文件传输失败"+localName);
//                            }
//                            sftpClient.removeFile(remoteName);
//                        }
//
//                        num++;
////                        if (num/100 == 0 ) {
//                            System.out.println("=========================");
//                            System.out.println("已经写入文本个数为" + num);
//                            System.out.println("当前的失败条数为" + numFailureTotal);
//                            long time2 = System.currentTimeMillis();
//                            System.out.println("当前耗时为：" + DateConversion.formatTime(time2 - time1));
//                            System.out.println("=========================");
////                        }
//
//                        if (num == numTotal) {
//                            long time3 = System.currentTimeMillis();
//                            System.out.println("一共耗时为：" + DateConversion.formatTime(time3 - time1));
//                            break;
//                        }
//
////                        Thread.sleep(2000);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                if (num == numTotal) {
//                    long time3 = System.currentTimeMillis();
//                    System.out.println("一共耗时为：" + DateConversion.formatTime(time3 - time1));
//                    break;
//                }
////                FileUtils.deleteFile(localName);
//            } finally {
//                RedisUtil.returnResource(jedis);
//                //关闭sftp连接
//                try {
//                    sftpClient.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}
