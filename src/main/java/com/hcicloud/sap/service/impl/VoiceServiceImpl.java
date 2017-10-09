package com.hcicloud.sap.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hcicloud.sap.common.*;
import com.hcicloud.sap.service.VoiceService;
import com.hcicloud.sap.vo.AnnoyModel;
import com.hcicloud.sap.vo.Grid;
import com.hcicloud.sap.vo.SuccessModal;
import com.hcicloud.sap.vo.VoiceVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VoiceServiceImpl implements VoiceService {

    private Logger logger = Logger.getLogger(VoiceServiceImpl.class);


    @Override
    public Grid getSuccessList(SuccessModal model, PageFilter ph) {
        String searchString = "{\"query\": {\"filtered\": {\"filter\": [{\"bool\": {\"must\": [";
        if (StringUtils.isNoneBlank(model.getStartTime())
                && StringUtils.isNoneBlank(model.getEndTime())) {
            searchString += "{\"range\":{\"CREATE_TIME\":{\"gte\":\""
                    + model.getStartTime() + "\",\"lte\":\""
                    + model.getEndTime() + "\"}}},";
        }
        if (StringUtils.isNoneBlank(model.getVoiceId())) {
            searchString += "{\"wildcard\":{\"VOICE_ID\": \"*"
                    + model.getVoiceId() + "*\"}},";
        }
        if (StringUtils.isNoneBlank(model.getOrderId())) {
            searchString += "{\"term\":{\"ORDER_ID\": \"" + model.getOrderId()
                    + "\"}},";
        }
        if (StringUtils.isNoneBlank(model.getPlatForm())) {
            searchString += "{\"term\":{\"PLAT_FORM\": \""
                    + model.getPlatForm() + "\"}},";
        }

        if (StringUtils.isNoneBlank(model.getQualityName())) {
            searchString += "{\"wildcard\":{\"QUALITY_NAME\": \"*"
                    + model.getQualityName() + "*\"}},";
        }

        if (StringUtils.isNoneBlank(model.getUserPhone())) {
            searchString += "{\"term\":{\"USER_PHONE\": \""
                    + model.getUserPhone() + "\"}},";
        }
        if (StringUtils.isNoneBlank(model.getQualityData())
                && !"全部".equals(model.getQualityData())) {
            searchString += "{\"term\":{\"QUALITY_DATA\": \""
                    + model.getQualityData() + "\"}},";
        }
        if (searchString.endsWith(",")) {
            searchString = searchString.substring(0, searchString.length() - 1);
        }
//        searchString += "]}}]}},\"_source\": [\"VOICE_ID\",\"CALL_START_TIME\",\"USER_PHONE\",\"CALL_CONTENT\"],\"sort\": {\"CREATE_TIME\": {\"order\": \"asc\",\"ignore_unmapped\":true}},"
        searchString += "]}}]}},\"_source\": [\"VOICE_ID\",\"CALL_START_TIME\",\"USER_PHONE\",\"CALL_CONTENT\"],"
                + "\"from\": "
                + ph.getiDisplayStart()
                + ",\"size\": "
                + ph.getiDisplayLength() + "}";

        List<VoiceVo> list = new ArrayList<VoiceVo>();
        long total = 0l;
        long time1 = System.currentTimeMillis();
        Grid grid = new Grid();
        try {
            String result = HTTPMethod.doPostQuery(EsUtil.getContentQueryUrl(), searchString, 30000*6);
            long time2 = System.currentTimeMillis();
            System.out.println("【成功单查询】一共耗时为：" + DateConversion.formatTime(time2 - time1));
            logger.info("【成功单查询】一共耗时为：" + DateConversion.formatTime(time2 - time1));
            System.out.println("这里是成功单查询url："+EsUtil.getContentQueryUrl());
            System.out.println("这里是输出的是成功单查询ES语句：" + searchString);
            logger.info("这里是成功单查询url："+EsUtil.getContentQueryUrl());
            logger.info("这里是输出的是成功单查询ES语句：" + searchString);
            JSONObject resultObject = JSON.parseObject(result);
            JSONObject hitsObject = resultObject.getJSONObject("hits");
            total = (Integer) hitsObject.get("total");
            JSONArray hitsArray = hitsObject.getJSONArray("hits");
            for (Object v : hitsArray) {
                JSONObject jsonObject = ((JSONObject) v)
                        .getJSONObject("_source");

                //逐条遍历获取vo
                VoiceVo sm = new VoiceVo();
                sm.setVoiceId(jsonObject.getString("VOICE_ID"));
                sm.setCallContent(jsonObject.getString("CALL_CONTENT"));
                sm.setCallStartTime(jsonObject.getString("CALL_START_TIME"));
                sm.setUserPhone(jsonObject.getString("USER_PHONE"));
                list.add(sm);
            }
        } catch (Exception e) {
            e.printStackTrace();
            grid.setMessage("false");
            logger.error("【成功单】从"+ ph.getiDisplayStart() +"到"+ ph.getiDisplayLength() + "出现错误：" + e.getMessage());
            long time3 = System.currentTimeMillis();
            logger.error("【成功单查询】一共耗时为：" + DateConversion.formatTime(time3 - time1));
            return grid;
        }
        grid.setAaData(list);
        grid.setiTotalDisplayRecords(total);
        grid.setiTotalRecords(list.size());
        return grid;
    }

    @Override
    public Grid getFsrList(AnnoyModel model, PageFilter ph) {
        String searchString = "{\"query\": {\"filtered\": {\"filter\": [{\"bool\": {\"must\": [";
        if(StringUtils.isNoneBlank(model.getStartTime()) && StringUtils.isNoneBlank(model.getEndTime()) ){
            searchString += "{\"range\":{\"CREATE_TIME\":{\"gte\":\""+model.getStartTime()+"\",\"lte\":\""+model.getEndTime()+"\"}}},";
        }
        if(StringUtils.isNoneBlank(model.getAnnoyType())){
            searchString+="{\"term\":{\"ANNOY_TYPE\": \""+model.getAnnoyType()+"\"}},";
        }
        if(StringUtils.isNoneBlank(model.getVoiceId())){
            searchString+="{\"term\":{\"VOICE_ID\": \""+model.getVoiceId()+"\"}},";
        }
        if(StringUtils.isNoneBlank(model.getStaffId())){
            searchString+="{\"term\":{\"STAFF_ID\": \""+model.getStaffId()+"\"}},";
        }
//        if(StringUtils.isNoneBlank(model.getPlatForm())){
//            searchString+="{\"term\":{\"PLAT_FORM\": \""+model.getPlatForm()+"\"}},";
//        }
        if(StringUtils.isNoneBlank(model.getCallPhone())){
            searchString+="{\"term\":{\"CALL_PHONE\": \""+model.getCallPhone()+"\"}},";
        }
        if(StringUtils.isNoneBlank(model.getSeatAttitude())){
            searchString+="{\"term\":{\"SEAT_ATTITUDE\": \""+model.getSeatAttitude()+"\"}},";
        }

        //去掉最后一个 ‘，’
        if(searchString.endsWith(",")){
            searchString = searchString.substring(0, searchString.length()-1);
        }
        searchString+="]" +
                ",\"should\": [" +
                "{" +
                "\"term\": {" +
                "\"PLAT_FORM\": \"XQD-CCOD\"" +
                "}" +
                "}," +
                "{" +
                "\"term\":{" +
                "\"PLAT_FORM\": \"XQD-ZX\"" +
                "}" +
                "}," +
                "{" +
                "\"term\":{" +
                "\"PLAT_FORM\": \"XQD-HW\"" +
                "}" +
                "}" +
                "]"+
                "}}]}},\"_source\": [\"VOICE_ID\",\"CALL_START_TIME\",\"TRANS_CONTENT\"],"
                + "\"from\": "+ph.getiDisplayStart()+",\"size\": "+ph.getiDisplayLength()+"}";

        List<VoiceVo> list = new ArrayList<VoiceVo>();
        long total = 0l;
        long time1 = System.currentTimeMillis();
        Grid grid = new Grid();
        try {
            String result = HTTPMethod.doPostQuery(EsUtil.getAnnoyQueryUrl(), searchString, 30000*6);
            long time2 = System.currentTimeMillis();
            System.out.println("【防骚扰查询】一共耗时为：" + DateConversion.formatTime(time2 - time1));
            logger.info("【防骚扰查询】一共耗时为：" + DateConversion.formatTime(time2 - time1));
            System.out.println("这里是防骚扰查询url："+EsUtil.getAnnoyQueryUrl());
            System.out.println("这里是输出的是防骚扰查询ES语句：" + searchString);
            logger.info("这里是防骚扰查询url："+EsUtil.getAnnoyQueryUrl());
            logger.info("这里是输出的是防骚扰查询ES语句：" + searchString);
            JSONObject resultObject = JSON.parseObject(result);
            JSONObject hitsObject = resultObject.getJSONObject("hits");
            total = (Integer) hitsObject.get("total");
            JSONArray hitsArray = hitsObject.getJSONArray("hits");
            for (Object v : hitsArray) {
                JSONObject jsonObject = ((JSONObject)v).getJSONObject("_source");
                VoiceVo sm = new VoiceVo();
                sm.setVoiceId(jsonObject.getString("VOICE_ID"));
                sm.setCallStartTime(jsonObject.getString("CALL_START_TIME"));
                //话者分离格式文本
                String transContent = jsonObject.getString("TRANS_CONTENT");
                if (StringUtils.isNotBlank(transContent)) {
                    String callContent = CommonMethod.transContent(transContent);
                    if (StringUtils.isNotBlank(callContent)) {
                        sm.setCallContent(callContent);
                    }else {
                        sm.setCallContent("");
                    }
                }else {
                    sm.setCallContent("");
                }
                list.add(sm);
            }
        } catch (Exception e) {
            e.printStackTrace();
            grid.setMessage("false");
            logger.error("【防骚扰】从"+ ph.getiDisplayStart() +"到"+ ph.getiDisplayLength() + "出现错误：" + e.getMessage());
            long time3 = System.currentTimeMillis();
            logger.error("【防骚扰查询】一共耗时为：" + DateConversion.formatTime(time3 - time1));
            return grid;
        }
        grid.setAaData(list);
        grid.setiTotalDisplayRecords(total);
        grid.setiTotalRecords(list.size());
        return grid;
    }

    @Override
    public Grid getCurrentList(AnnoyModel model, PageFilter ph) {
        String searchString = "{\"query\": {\"filtered\": {\"filter\": [{\"bool\": {\"must\": [";
        if(StringUtils.isNoneBlank(model.getStartTime()) && StringUtils.isNoneBlank(model.getEndTime()) ){
            searchString += "{\"range\":{\"CREATE_TIME\":{\"gte\":\""+model.getStartTime()+"\",\"lte\":\""+model.getEndTime()+"\"}}},";
        }
        if(StringUtils.isNoneBlank(model.getAnnoyType())){
            searchString+="{\"term\":{\"ANNOY_TYPE\": \""+model.getAnnoyType()+"\"}},";
        }
        if(StringUtils.isNoneBlank(model.getVoiceId())){
            searchString+="{\"term\":{\"VOICE_ID\": \""+model.getVoiceId()+"\"}},";
        }
        if(StringUtils.isNoneBlank(model.getStaffId())){
            searchString+="{\"term\":{\"STAFF_ID\": \""+model.getStaffId()+"\"}},";
        }
//        if(StringUtils.isNoneBlank(model.getPlatForm())){
//            searchString+="{\"term\":{\"PLAT_FORM\": \""+model.getPlatForm()+"\"}},";
//        }
        if(StringUtils.isNoneBlank(model.getCallPhone())){
            searchString+="{\"term\":{\"CALL_PHONE\": \""+model.getCallPhone()+"\"}},";
        }
        if(StringUtils.isNoneBlank(model.getSeatAttitude())){
            searchString+="{\"term\":{\"SEAT_ATTITUDE\": \""+model.getSeatAttitude()+"\"}},";
        }

        //去掉最后一个 ‘，’
        if(searchString.endsWith(",")){
            searchString = searchString.substring(0, searchString.length()-1);
        }
        searchString+="]" +
                ",\"should\": [" +
                "{" +
                "\"term\": {" +
                "\"PLAT_FORM\": \"XQD-ZHW\"" +
                "}" +
                "}," +
                "{" +
                "\"term\":{" +
                "\"PLAT_FORM\": \"XQD-SHW\"" +
                "}" +
                "}" +
                "]"+
                "}}]}},\"_source\": [\"VOICE_ID\",\"CALL_START_TIME\",\"TRANS_CONTENT\"],"
                + "\"from\": "+ph.getiDisplayStart()+",\"size\": "+ph.getiDisplayLength()+"}";
        List<VoiceVo> list = new ArrayList<VoiceVo>();
        long total = 0l;
        long time1 = System.currentTimeMillis();
        Grid grid = new Grid();
        try {
            String result = HTTPMethod.doPostQuery(EsUtil.getAnnoyQueryUrl(), searchString, 30000*6);
            long time2 = System.currentTimeMillis();
            System.out.println("【实时查询】一共耗时为：" + DateConversion.formatTime(time2 - time1));
            logger.info("【实时查询】一共耗时为：" + DateConversion.formatTime(time2 - time1));
            System.out.println("这里是实时查询url："+EsUtil.getAnnoyQueryUrl());
            System.out.println("这里是输出的是实时查询ES语句：" + searchString);
            logger.info("这里是实时查询url："+EsUtil.getAnnoyQueryUrl());
            logger.info("这里是输出的是实时查询ES语句：" + searchString);
            JSONObject resultObject = JSON.parseObject(result);
            JSONObject hitsObject = resultObject.getJSONObject("hits");
            total = (Integer) hitsObject.get("total");
            JSONArray hitsArray = hitsObject.getJSONArray("hits");
            for (Object v : hitsArray) {
                JSONObject jsonObject = ((JSONObject)v).getJSONObject("_source");
                VoiceVo sm = new VoiceVo();
                sm.setVoiceId(jsonObject.getString("VOICE_ID"));
                sm.setCallStartTime(jsonObject.getString("CALL_START_TIME"));
                //话者分离格式文本
                String transContent = jsonObject.getString("TRANS_CONTENT");
                if (StringUtils.isNotBlank(transContent)) {
                    String callContent = CommonMethod.transContent(transContent);
                    if (StringUtils.isNotBlank(callContent)) {
                        sm.setCallContent(callContent);
                    }else {
                        sm.setCallContent("");
                    }
                }else {
                    sm.setCallContent("");
                }
                list.add(sm);
            }
        } catch (Exception e) {
            e.printStackTrace();
            grid.setMessage("false");
            logger.error("【实时】从"+ ph.getiDisplayStart() +"到"+ ph.getiDisplayLength() + "出现错误：" + e.getMessage());
            long time3 = System.currentTimeMillis();
            logger.error("【实时查询】一共耗时为：" + DateConversion.formatTime(time3 - time1));
            return grid;
        }
        grid.setAaData(list);
        grid.setiTotalDisplayRecords(total);
        grid.setiTotalRecords(list.size());
        return grid;
    }

    @Override
    public Grid getFsrHistList(AnnoyModel model, PageFilter ph) {
        String searchString = "{\"query\": {\"filtered\": {\"filter\": [{\"bool\": {\"must\": [";
        if(StringUtils.isNoneBlank(model.getStartTime()) && StringUtils.isNoneBlank(model.getEndTime()) ){
            searchString += "{\"range\":{\"CALL_START_TIME\":{\"gte\":\""+model.getStartTime()+"\",\"lte\":\""+model.getEndTime()+"\"}}},";
        }
        if(StringUtils.isNoneBlank(model.getAnnoyType())){
            searchString+="{\"term\":{\"ANNOY_TYPE\": \""+model.getAnnoyType()+"\"}},";
        }
        if(StringUtils.isNoneBlank(model.getVoiceId())){
            searchString+="{\"term\":{\"VOICE_ID\": \""+model.getVoiceId()+"\"}},";
        }
        if(StringUtils.isNoneBlank(model.getStaffId())){
            searchString+="{\"term\":{\"STAFF_ID\": \""+model.getStaffId()+"\"}},";
        }
        if(StringUtils.isNoneBlank(model.getPlatForm())){
            searchString+="{\"term\":{\"PLAT_FORM\": \""+model.getPlatForm()+"\"}},";
        }
        if(StringUtils.isNoneBlank(model.getCallPhone())){
            searchString+="{\"term\":{\"CALL_PHONE\": \""+model.getCallPhone()+"\"}},";
        }
        if(StringUtils.isNoneBlank(model.getSeatAttitude())){
            searchString+="{\"term\":{\"SEAT_ATTITUDE\": \""+model.getSeatAttitude()+"\"}},";
        }

        //去掉最后一个 ‘，’
        if(searchString.endsWith(",")){
            searchString = searchString.substring(0, searchString.length()-1);
        }
        searchString+="]}}]}},\"_source\": [\"VOICE_ID\",\"CALL_START_TIME\",\"CALL_CONTENT\"],"
                + "\"from\": "+ph.getiDisplayStart()+",\"size\": "+ph.getiDisplayLength()+"}";

        List<VoiceVo> list = new ArrayList<VoiceVo>();
        long total = 0l;
        long time1 = System.currentTimeMillis();
        Grid grid = new Grid();
        try {
            String result = HTTPMethod.doPostQuery(EsUtil.getAnnoyQueryUrl(), searchString, 30000*6);
            long time2 = System.currentTimeMillis();
            System.out.println("【防骚扰历史查询】一共耗时为：" + DateConversion.formatTime(time2 - time1));
            logger.info("【防骚扰历史查询】一共耗时为：" + DateConversion.formatTime(time2 - time1));
            System.out.println("这里是防骚扰历史查询url："+EsUtil.getAnnoyQueryUrl());
            System.out.println("这里是输出的是防骚扰历史查询ES语句：" + searchString);
            logger.info("这里是防骚扰历史查询url："+EsUtil.getAnnoyQueryUrl());
            logger.info("这里是输出的是防骚扰历史查询ES语句：" + searchString);
            JSONObject resultObject = JSON.parseObject(result);
            JSONObject hitsObject = resultObject.getJSONObject("hits");
            total = (Integer) hitsObject.get("total");
            JSONArray hitsArray = hitsObject.getJSONArray("hits");
            for (Object v : hitsArray) {
                JSONObject jsonObject = ((JSONObject)v).getJSONObject("_source");
                VoiceVo sm = new VoiceVo();
                sm.setVoiceId(jsonObject.getString("VOICE_ID"));
                sm.setCallStartTime(jsonObject.getString("CALL_START_TIME"));
                sm.setCallContent(jsonObject.getString("CALL_CONTENT"));
                list.add(sm);
            }
        } catch (Exception e) {
            e.printStackTrace();
            grid.setMessage("false");
            logger.error("【防骚扰历史】从"+ ph.getiDisplayStart() +"到"+ ph.getiDisplayLength() + "出现错误：" + e.getMessage());
            long time3 = System.currentTimeMillis();
            logger.error("【防骚扰历史查询】一共耗时为：" + DateConversion.formatTime(time3 - time1));
            return grid;
        }
        grid.setAaData(list);
        grid.setiTotalDisplayRecords(total);
        grid.setiTotalRecords(list.size());
        return grid;
    }
}
