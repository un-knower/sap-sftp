package com.hcicloud.sap.job;

import com.hcicloud.sap.common.*;
import com.hcicloud.sap.service.VoiceService;
import com.hcicloud.sap.vo.AnnoyModel;
import com.hcicloud.sap.vo.Grid;
import com.hcicloud.sap.vo.VoiceVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Calendar;
import java.util.List;

@Configuration
@Component // 此注解必加
@EnableScheduling // 此注解必加
@DisallowConcurrentExecution
public class CurrentVoiceJob implements Job {
    @Autowired
    private VoiceService voiceService;

    //本地的目录前后必须有 "/"
    private static String successFtpVoicePath = "/home/mhn/voiceftp/current/";
    private static String zipName = "zip";
    private static String unzipName = "unzip";


    //磁盘的根目录下的子目录   /nfsc/gcc……/下的   同时前面不能加 "/"  后面必须有 "/"
    private static String remoteVoicePath = "";
    private static int totalNum = 50000;  //一个包中的个数
    private static int singleNum = 10;  //一次查5000条数据

    private Logger logger = Logger.getLogger(CurrentVoiceJob.class);


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("******************实时任务开始************************");
        logger.info("******************实时任务开始************************");


        //TODO 1.读取ES中的数据，先按照  1-10000 取读取数据   2.压缩保存   3.推送


        //定义一个当前时间
        Calendar nowTime = Calendar.getInstance();
        //本地文件名
        String localName = "";
        //本地解压目录
        String localPathUnZip = "";
        //本地压缩目录
        String localPathZip = "";
        //远程sftp路径
        String remoteName = "";
        //记录一共耗时
        long time1 = System.currentTimeMillis();
        SftpClient sftpClient = new SftpClient();
        sftpClient.setDirectory("/current");

        String fileName = DateConversion.DateToStr(DateConversion.getDayBeforeBegin(nowTime.getTime(),1),"yyyyMMdd") + "_current_";
        String fileNameSend = "0";

        AnnoyModel model = new AnnoyModel();
        model.setStartTime(DateConversion.DateToStr(DateConversion.getDayBeforeBegin(nowTime.getTime(),1),"yyyy-MM-dd HH:mm:ss"));
        model.setEndTime(DateConversion.DateToStr(DateConversion.getDayBeforeEnd(nowTime.getTime(),1),"yyyy-MM-dd HH:mm:ss"));

        try {
            //计算一共导出多少个fsr包
            Grid suGrid = voiceService.getCurrentList(model, new PageFilter(0,10));
            long total =  suGrid.getiTotalDisplayRecords();
            int sheetNum =(int)total/totalNum+1;
            if (total == 0) {
                return;
            }
            System.out.println("一共导出zip包为：" + sheetNum +"**************current**************，一共的个数为" + total + "**************" + nowTime.getTime());
            logger.info("一共导出zip包为：" + sheetNum +"**************current**************，一共的个数为" + total + "**************" + nowTime.getTime());


            for (int i = 1;i <= sheetNum; i++){
                System.out.println("导出zip包：" + fileName + i + "_" + fileNameSend +"**************current**************"+ nowTime.getTime());
                logger.info("导出zip包：" + fileName + i + "_" + fileNameSend +"**************current**************"+ nowTime.getTime());

                //拼凑本地的路径
                if(!successFtpVoicePath.endsWith(File.separator)){
                    successFtpVoicePath += File.separator;
                }
                localPathUnZip = successFtpVoicePath + nowTime.get(Calendar.YEAR) + File.separator + ((nowTime.get(Calendar.MONTH)+1) < 10 ?  "0" + (nowTime.get(Calendar.MONTH)+1) :  (nowTime.get(Calendar.MONTH)+1)) + File.separator + (nowTime.get(Calendar.DAY_OF_MONTH)-1) + File.separator + unzipName + File.separator;
                localPathZip = successFtpVoicePath + nowTime.get(Calendar.YEAR) + File.separator + ((nowTime.get(Calendar.MONTH)+1) < 10 ?  "0" + (nowTime.get(Calendar.MONTH)+1) :  (nowTime.get(Calendar.MONTH)+1)) + File.separator + (nowTime.get(Calendar.DAY_OF_MONTH)-1) + File.separator + zipName + File.separator;
                remoteName = remoteVoicePath + nowTime.get(Calendar.YEAR) + File.separator + ((nowTime.get(Calendar.MONTH)+1) < 10 ?  "0" + (nowTime.get(Calendar.MONTH)+1) :  (nowTime.get(Calendar.MONTH)+1)) + File.separator + (nowTime.get(Calendar.DAY_OF_MONTH)-1) + File.separator;
                if (!FileUtils.isFolderExist(localPathUnZip)) {
                    FileUtils.makeDirs(localPathUnZip);
                }
                if (!FileUtils.isFolderExist(localPathZip)) {
                    FileUtils.makeDirs(localPathZip);
                }

                int j = 0;
                //计算j的值
                if(i == sheetNum){
                    j = (int)(total - (i-1) * totalNum)/singleNum + 1;
                }else{
                    j = totalNum/singleNum;
                }
                for(int z=0 ; z < j; z++){
                    //拼凑数据
                    Grid grid = voiceService.getCurrentList(model, new PageFilter((i-1)*totalNum + z*singleNum , singleNum));
                    //处理失败第二次调用
                    if ("false".equals(grid.getMessage())) {
                        grid = voiceService.getCurrentList(model, new PageFilter((i-1)*totalNum + z*singleNum , singleNum));
                        System.out.println("【实时超时，第二次调用】");
                        logger.info("【实时超时，第二次调用】");
                    }
                    List<VoiceVo> aaData = grid.getAaData();
                    for (VoiceVo voiceVo : aaData) {
                        String voiceId = voiceVo.getVoiceId();
                        String callContent = voiceVo.getCallContent();
                        String callTime = voiceVo.getCallStartTime();

                        //这里转化为  年月日时分
                        callTime = DateConversion.DateToStr(DateConversion.StrToDate(callTime,"yyyy-MM-dd HH:mm:ss"),"yyyyMMddHHmm");
                        localName = localPathUnZip + fileName + i + "_" + fileNameSend + File.separator + callTime + "_" + voiceId + ".txt";

                        //写入文件
                        if (FileUtils.writeFile(localName,callContent,false)) {
                            String textMeg = callTime + "_" + voiceId + ".txt" +"\r\n";
                            FileUtils.writeFile2(localPathZip + fileName + i + "_" + fileNameSend +".txt",textMeg,true);
                        }
                    }
//                    Thread.sleep(500);
                }
                //TODO 开始压缩zip,并且发送sftp
                System.out.println("压缩zip包：" + fileName + i + "_" + fileNameSend +"**************current**************"+ nowTime.getTime());
                logger.info("压缩zip包：" + fileName + i + "_" + fileNameSend +"**************current**************"+ nowTime.getTime());

                long time4 = System.currentTimeMillis();
                ZipUtils.writeByApacheZipOutputStream(localPathUnZip + fileName + i + "_" + fileNameSend + File.separator, localPathZip + fileName + i + "_" + fileNameSend + ".zip", "");
                long time5 = System.currentTimeMillis();

                System.out.println("压缩zip包：" + fileName + i + "_" + fileNameSend +"**************current**************"+ nowTime.getTime() + "***************耗时为：" + DateConversion.formatTime(time5 - time4));
                logger.info("压缩zip包：" + fileName + i + "_" + fileNameSend +"**************current**************"+ nowTime.getTime() + "***************耗时为：" + DateConversion.formatTime(time5 - time4));

                System.out.println("发送zip包：" + fileName + i + "_" + fileNameSend +"**************current**************"+ nowTime.getTime());
                logger.info("发送zip包：" + fileName + i + "_" + fileNameSend +"**************current**************"+ nowTime.getTime());

                sftpClient.uploadFile(localPathZip + fileName + i + "_" + fileNameSend + ".zip",remoteName + fileName + i + "_" + fileNameSend + ".zip");
                sftpClient.uploadFile(localPathZip + fileName + i + "_" + fileNameSend + ".txt",remoteName + fileName + i + "_" + fileNameSend + ".txt");

                //由于查询一万条数据耗时，当前线程先停2分钟后，再去查询
                Thread.sleep(1000*5);
            }
            long time3 = System.currentTimeMillis();
            System.out.println("***************************实时开始*******************************");
            System.out.println("一共耗时为：" + DateConversion.formatTime(time3 - time1));
            System.out.println("***************************实时结束*******************************");

            logger.info("***************************实时开始*******************************");
            logger.info("一共耗时为：" + DateConversion.formatTime(time3 - time1));
            logger.info("***************************实时结束*******************************");

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }finally{
            //关闭sftp连接
            try {
                sftpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}