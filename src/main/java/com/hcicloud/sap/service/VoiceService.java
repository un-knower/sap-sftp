package com.hcicloud.sap.service;

import com.hcicloud.sap.common.PageFilter;
import com.hcicloud.sap.vo.AnnoyModel;
import com.hcicloud.sap.vo.Grid;
import com.hcicloud.sap.vo.SuccessModal;

public interface VoiceService {

    //获取成功单的数据
    Grid getSuccessList(SuccessModal model, PageFilter ph);

    //获取防骚扰的数据
    Grid getFsrList(AnnoyModel model, PageFilter ph);

    //获取实时的数据
    Grid getCurrentList(AnnoyModel model, PageFilter ph);

    //************************************获取历史数据******************************************
    //************************************2017年9月27日 20:55:38******************************************
    //获取防骚扰历史的数据
    Grid getFsrHistList(AnnoyModel model, PageFilter ph);

}
