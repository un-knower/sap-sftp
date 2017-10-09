package com.hcicloud.sap.sftp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hcicloud.sap.common.CommonMethod;
import com.hcicloud.sap.common.HTTPMethod;
import com.hcicloud.sap.common.StringUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EsUtilTest {

    private static String HOST = "http://10.0.1.227:9200/";

    private static String SUCCESS_INDEX = "pa_success";
    private static String ANNOY_INDEX = "pa_annoy";
    private static String PRODUCT_INDEX = "pa_product";
    private static String UPLOAD_INDEX = "pa_upload";


    private static String TYPE_SUCCESS = "success";
    private static String TYPE_CONTENT = "content";
    private static String TYPE_ANNOY = "annoy";
    private static String TYPE_PRODUCT = "product";

    //pa_success content 索引结构
    private static String contentMap = "{\"mappings\":{\"content\":{\"_all\":{\"analyzer\":\"ik\",\"search_analyzer\":\"ik\",\"term_vector\":\"no\"},\"properties\":{\"VOICE_ID\":{\"type\":\"string\",\"index\":\"not_analyzed\"},\"ORDER_ID\":{\"type\":\"string\",\"index\":\"not_analyzed\"},\"PLAT_FORM\":{\"type\":\"string\",\"index\":\"not_analyzed\"},\"USER_PHONE\":{\"type\":\"string\",\"index\":\"not_analyzed\"},\"CALL_LENGTH\":{\"type\":\"integer\",\"index\":\"not_analyzed\"},\"xmlData\":{\"type\":\"string\",\"ignore_above\":10000,\"analyzer\":\"ik\",\"search_analyzer\":\"ik\"},\"CALL_CONTENT\":{\"type\":\"string\",\"ignore_above\":10000,\"analyzer\":\"ik\",\"search_analyzer\":\"ik\",\"term_vector\":\"with_positions_offsets\"}}}},\"settings\":{\"index\":{\"max_result_window\":100000000}}}";
    //pa_annoy 索引结构
    private static String annoyMap="{\"mappings\": {\"annoy\": {\"_all\": {\"analyzer\": \"ik\",\"search_analyzer\": \"ik\",\"term_vector\": \"no\"},\"properties\": {\"VOICE_ID\": {\"type\": \"string\",\"index\": \"not_analyzed\"},\"STAFF_ID\": {\"type\": \"string\",\"index\": \"not_analyzed\"},\"VOICE_PATH\": {\"type\": \"string\",\"index\": \"not_analyzed\"},\"PLAT_FORM\": {\"type\": \"string\",\"index\": \"not_analyzed\"},\"ANNOY_TYPE\": {\"type\": \"string\",\"index\": \"not_analyzed\"},\"ANNOY_WORD\": {\"type\": \"string\",\"index\": \"not_analyzed\"},				 \"ANNOY_NUM\" : {					\"type\" : \"long\",					\"index\": \"not_analyzed\"				},\"CALL_CONTENT\": {\"type\": \"string\",\"analyzer\": \"ik\",\"search_analyzer\": \"ik\",\"term_vector\": \"with_positions_offsets\",					\"ignore_above\":10000},				\"TRANS_CONTENT\": {\"type\": \"string\",\"analyzer\": \"ik\",\"search_analyzer\": \"ik\",\"term_vector\": \"with_positions_offsets\",\"ignore_above\":10000},\"CALL_START_TIME\": {\"type\": \"date\",\"format\": \"yyyy-MM-dd HH:mm:ss\"},\"CALL_END_TIME\": {\"type\": \"date\",\"format\": \"yyyy-MM-dd HH:mm:ss\"},\"CREATE_TIME\":{\"type\": \"date\",\"format\": \"yyyy-MM-dd HH:mm:ss\"},\"CALL_PHONE\":{\"type\": \"string\",\"index\": \"not_analyzed\"},\"ANNOY_HISTORY_TYPE\":{\"type\": \"string\",\"index\": \"not_analyzed\"},\"SEAT_ATTITUDE\":{\"type\": \"string\",\"index\": \"not_analyzed\"}}}},\"settings\":{\"index\" : { \"max_result_window\" : 100000000}}}";



//    public static void main(String[] args){
//
//        //2017年9月23日 15:35:57
//        Calendar nowTime = Calendar.getInstance();
//        System.out.println(nowTime.get(Calendar.YEAR)); //2017
//        System.out.println(nowTime.get(Calendar.MONTH));  //8
//        System.out.println(nowTime.get(Calendar.DATE));  //23
//        System.out.println(nowTime.get(Calendar.HOUR_OF_DAY));  //15
//        System.out.println(successIndexPostfix()); //2017-10
//
//        //TODO *************************创建索引****************************
//
//        //TODO 1.成功单content
////        String contentIndex = createIndex(HOST+SUCCESS_INDEX+"2017-09"+"/",contentMap);
//        //TODO 2.成功单success  错误的，建立的索引为   pa_success2017-09success ，入坑了
////        String successIndex = createIndex(HOST+SUCCESS_INDEX+"2017-09"+TYPE_SUCCESS,"{}");
//        //TODO 3.防骚扰
////        String annoyMapIndex = createIndex(HOST+ANNOY_INDEX+"2017-09"+"/",annoyMap);
//
//
//
//
//        //TODO *************************添加数据****************************
//
//        String type = "2017-09";
//        //TODO 1.成功单content
//        List<String>  voiceList = new ArrayList<String>();
//        JSONObject contentObj = new JSONObject();
////        contentObj.put("ORDER_ID", "");
////        contentObj.put("CALL_START_TIME", "");
////        contentObj.put("CREATE_TIME", "");
////        contentObj.put("CALL_END_TIME", "");
////        contentObj.put("USER_PHONE", "");
////        contentObj.put("xmlData", "");
////        contentObj.put("CALL_CONTENT",  "");
////        contentObj.put("CALL_LENGTH", );
////        contentObj.put("PLAT_FORM", "");
////        contentObj.put("PATH", "");
////        contentObj.put("VOICE_ID", "");
//
//
//        contentObj.put("ORDER_ID", "1691123196");
//        contentObj.put("CALL_START_TIME", "2017-09-23 14:55:56");
//        contentObj.put("CREATE_TIME", "2017-09-23 15:11:44");
//        contentObj.put("CALL_END_TIME", "2017-09-23 14:55:56");
//        contentObj.put("USER_PHONE", "550-4320-C,4510-5060-A,5310-12760-C,13010-14840-A,15250-16100-C,16530-19660-A,19890-21540-C,21730-27040-A,27250-28620-C,28890-31280-A,31970-32300-C,33090-34160-A,34310-34640-C,60010-60760-C,84410-85540-C,160390-161080-C,161730-164520-C,165890-166450-C,166450-172860-A,173310-173520-C,173930-178860-A,179190-180000-C,180650-184540-A,185150-186740-C");
//        contentObj.put("xmlData", "<?xml version=\"1.0\" encoding=\"utf-8\"?><result><duration>185</duration><sentence_list><sentence end=\"4320\" index=\"0\" role=\"USER\" score=\"2677\" speed=\"4.8951\" start=\"550\"><text>哎喂哎你好卢先生哎你好</text></sentence><sentence end=\"5060\" index=\"1\" role=\"AGENT\" score=\"2677\" speed=\"4.8951\" start=\"4510\"><text>你好</text></sentence><sentence end=\"12760\" index=\"2\" role=\"USER\" score=\"2677\" speed=\"4.8951\" start=\"5310\"><text>呃这边的话之前给您支付的时候看到您这个单当天的这个交易限额超限了您需要改成这个</text></sentence><sentence end=\"14840\" index=\"3\" role=\"AGENT\" score=\"2677\" speed=\"4.8951\" start=\"13010\"><text>建行的信用卡来缴费是吧</text></sentence><sentence end=\"16100\" index=\"4\" role=\"USER\" score=\"2677\" speed=\"4.8951\" start=\"15250\"><text>嗯对是</text></sentence><sentence end=\"19660\" index=\"5\" role=\"AGENT\" score=\"2677\" speed=\"4.8951\" start=\"16530\"><text>好行那您稍等一下我帮您切入到这个信用卡</text></sentence><sentence end=\"21540\" index=\"6\" role=\"USER\" score=\"2677\" speed=\"4.8951\" start=\"19890\"><text>这个支付系统里面</text></sentence><sentence end=\"27040\" index=\"7\" role=\"AGENT\" score=\"2677\" speed=\"4.8951\" start=\"21730\"><text>那的话是需要您把信用卡带着身边输入信用卡卡号有效期和安全验证码的</text></sentence><sentence end=\"28620\" index=\"8\" role=\"USER\" score=\"2677\" speed=\"4.8951\" start=\"27250\"><text>总共有五个操作步骤</text></sentence><sentence end=\"31280\" index=\"9\" role=\"AGENT\" score=\"2677\" speed=\"4.8951\" start=\"28890\"><text>稍后呢您根据系统提示来完成好吗</text></sentence><sentence end=\"32300\" index=\"10\" role=\"USER\" score=\"2677\" speed=\"4.8951\" start=\"31970\"><text>好的</text></sentence><sentence end=\"34160\" index=\"11\" role=\"AGENT\" score=\"2677\" speed=\"4.8951\" start=\"33090\"><text>好的那您稍等</text></sentence><sentence end=\"34640\" index=\"12\" role=\"USER\" score=\"2677\" speed=\"4.8951\" start=\"34310\"><text>嗯</text></sentence><sentence end=\"60760\" index=\"13\" role=\"USER\" score=\"2677\" speed=\"4.8951\" start=\"60010\"><text>嗯</text></sentence><sentence end=\"85540\" index=\"14\" role=\"USER\" score=\"2677\" speed=\"4.8951\" start=\"84410\"><text>嗯</text></sentence><sentence end=\"161080\" index=\"15\" role=\"USER\" score=\"2677\" speed=\"4.8951\" start=\"160390\"><text>喂你好</text></sentence><sentence end=\"164520\" index=\"16\" role=\"USER\" score=\"2677\" speed=\"4.8951\" start=\"161730\"><text>嗯你好嗯帮您确认一下您稍等</text></sentence><sentence end=\"166450\" index=\"17\" role=\"USER\" score=\"2677\" speed=\"4.8951\" start=\"165890\"><text>好的更</text></sentence><sentence end=\"172860\" index=\"18\" role=\"AGENT\" score=\"2677\" speed=\"4.8951\" start=\"166450\"><text>你你已经缴费成功了稍后的话呢扣费短信和同样的承保信息也会发到您手机上的刘先生</text></sentence><sentence end=\"173520\" index=\"19\" role=\"USER\" score=\"2677\" speed=\"4.8951\" start=\"173310\"><text>嗯</text></sentence><sentence end=\"178860\" index=\"20\" role=\"AGENT\" score=\"2677\" speed=\"4.8951\" start=\"173930\"><text>好行那这边也就不多耽误您的时间了有问题您联系畅通就可以了</text></sentence><sentence end=\"180000\" index=\"21\" role=\"USER\" score=\"2677\" speed=\"4.8951\" start=\"179190\"><text>嗯好的</text></sentence><sentence end=\"184540\" index=\"22\" role=\"AGENT\" score=\"2677\" speed=\"4.8951\" start=\"180650\"><text>好祝您平安您需要我们的保障给宝贝送学生幸福再见</text></sentence><sentence end=\"186740\" index=\"23\" role=\"USER\" score=\"2677\" speed=\"4.8951\" start=\"185150\"><text>嗯再见嗯好</text></sentence></sentence_list></result>");
//        contentObj.put("CALL_CONTENT",  "哎喂哎你好卢先生哎你好☆你好☆呃这边的话之前给您支付的时候看到您这个单当天的这个交易限额超限了您需要改成这个☆建行的信用卡来缴费是吧☆嗯对是☆好行那您稍等一下我帮您切入到这个信用卡☆这个支付系统里面☆那的话是需要您把信用卡带着身边输入信用卡卡号有效期和安全验证码的☆总共有五个操作步骤☆稍后呢您根据系统提示来完成好吗☆好的☆好的那您稍等☆嗯☆嗯☆嗯☆喂你好☆嗯你好嗯帮您确认一下您稍等☆好的更☆你你已经缴费成功了稍后的话呢扣费短信和同样的承保信息也会发到您手机上的刘先生☆嗯☆好行那这边也就不多耽误您的时间了有问题您联系畅通就可以了☆嗯好的☆好祝您平安您需要我们的保障给宝贝送学生幸福再见☆嗯再见嗯好☆");
//        contentObj.put("CALL_LENGTH", 185);
//        contentObj.put("PLAT_FORM", "XQD-CCOD");
//        contentObj.put("PATH", "/home/sftp_pingan/RecordShare/1691123196/record93/0000050326/Agent/20170923/TEL-61743303_251094_20170923145232.pcm");
//        contentObj.put("VOICE_ID", "1659c604b00583400000050326251094");
//
//        voiceList.add(contentObj.toJSONString());
////        indexBatchContent(type,voiceList);
//        //TODO 2.成功单success
//
//
//
//        //TODO 3.防骚扰
//        List<String> annoyList = new ArrayList<String>();
//        JSONObject esJsonAnnoy = new JSONObject();
//
//
////        esJsonAnnoy.put("CALL_PHONE","018263342022");
////        esJsonAnnoy.put("CREATE_TIME", "2017-09-23 16:06:18");
////        esJsonAnnoy.put("SEAT_ATTITUDE", "");
////        esJsonAnnoy.put("ANNOY_TYPE",  "正常");
////        esJsonAnnoy.put("ANNOY_HISTORY_TYPE",  "正常");//历史骚扰详细
////        esJsonAnnoy.put("VOICE_ID", "ZX20170923201709231611220013756225");
////        esJsonAnnoy.put("STAFF_ID", "756225");
////        esJsonAnnoy.put("ANNOY_WORD", "");
////        esJsonAnnoy.put("CALL_START_TIME", "2017-09-23 16:11:22");
////        esJsonAnnoy.put("CALL_END_TIME", "2017-09-23 16:11:22");
////        esJsonAnnoy.put("CALL_CONTENT", "喂你好哎你好请问是任服务信任先生是吗☆喂你好任先生☆嗯对☆哎你好我这边是中国平安您的专属客户经理壁柜分工号呢是幺九八幺七三呃是这样的那个关注到您在我们这个平安保险呀一四年的时候购买了一个万能险智胜人生那每年的交费是六千四百三十八块钱交费四年了☆哦现在就是您交费是六九四☆那就是欠是嗯☆一千五嘛☆六千四百三十八块钱☆在你查一查这个记录交费记录☆哦您这个主险的话是六千然后呢他有一个附加险有一个住院费用的是四千三百呃所有四百三十八一共是六千四百三十八块钱☆啊对就是你们都清楚就是但是之前我跟你发到吗之前是六千二百五十我挂断之后是理解哦☆之前是六千二百多了现在是六千四百多是这个意思是吗咯☆对☆嗯☆那您这个就是在网上查询过是吧相关的详情☆哎对☆嗯对哦☆咱那个就是您这个网上有这个平安尽管家吗我看您下载了还有这个软件吗☆喂☆喂☆喂☆听到您那边信号不太好我是说让您现在手机上还有咱这个app软件吗我看您当时下载☆啊没有了之后我没想☆嗯☆就是换手机之后在名下是吧那之后的话呢就需要说下载一下因为最重要的呢就是说万一发生理赔的话咱会通过咱这个软件买的伤残资料三到五个工作日做理赔的是这样的咯☆您到时候☆啊如如果如果我不想☆哦您这个也是必须要下载这个软件就的微信是吧挺好下的为什么不下来☆您是先麻烦吗还是怎么不会像呀☆没关哎能听到吗☆喂☆");
////        esJsonAnnoy.put("PLAT_FORM","XQD-ZX");
////        esJsonAnnoy.put("VOICE_PATH", "/home/szzxsftp/fsrnotify/20170923161452/voice/20170923161122635");
////        esJsonAnnoy.put("TRANS_CONTENT", "[{\"content\":\"喂你好哎你好请问是任服务信任先生是吗;time=230 3760\",\"talkertype\":\"1\"},{\"content\":\"喂你好任先生;time=6890 7920\",\"talkertype\":\"1\"},{\"content\":\"嗯对;time=8590 9020\",\"talkertype\":\"2\"},{\"content\":\"哎你好我这边是中国平安您的专属客户经理壁柜分工号呢是幺九八幺七三呃是这样的那个关注到您在我们这个平安保险呀一四年的时候购买了一个万能险智胜人生那每年的交费是六千四百三十八块钱交费四年了;time=9390 25020\",\"talkertype\":\"1\"},{\"content\":\"哦现在就是您交费是六九四;time=25130 32440\",\"talkertype\":\"2\"},{\"content\":\"那就是欠是嗯;time=32550 33740\",\"talkertype\":\"1\"},{\"content\":\"一千五嘛;time=34470 35420\",\"talkertype\":\"2\"},{\"content\":\"六千四百三十八块钱;time=35910 37840\",\"talkertype\":\"1\"},{\"content\":\"在你查一查这个记录交费记录;time=39670 42120\",\"talkertype\":\"2\"},{\"content\":\"哦您这个主险的话是六千然后呢他有一个附加险有一个住院费用的是四千三百呃所有四百三十八一共是六千四百三十八块钱;time=43170 52990\",\"talkertype\":\"1\"},{\"content\":\"啊对就是你们都清楚就是但是之前我跟你发到吗之前是六千二百五十我挂断之后是理解哦;time=52990 65000\",\"talkertype\":\"2\"},{\"content\":\"之前是六千二百多了现在是六千四百多是这个意思是吗咯;time=65150 69060\",\"talkertype\":\"1\"},{\"content\":\"对;time=69310 69780\",\"talkertype\":\"2\"},{\"content\":\"嗯;time=70290 70580\",\"talkertype\":\"2\"},{\"content\":\"那您这个就是在网上查询过是吧相关的详情;time=70730 74340\",\"talkertype\":\"1\"},{\"content\":\"哎对;time=78290 78880\",\"talkertype\":\"1\"},{\"content\":\"嗯对哦;time=79930 81200\",\"talkertype\":\"2\"},{\"content\":\"咱那个就是您这个网上有这个平安尽管家吗我看您下载了还有这个软件吗;time=81430 87060\",\"talkertype\":\"1\"},{\"content\":\"喂;time=92550 92960\",\"talkertype\":\"1\"},{\"content\":\"喂;time=95170 95800\",\"talkertype\":\"1\"},{\"content\":\"喂;time=96430 97940\",\"talkertype\":\"2\"},{\"content\":\"听到您那边信号不太好我是说让您现在手机上还有咱这个app软件吗我看您当时下载;time=98130 105180\",\"talkertype\":\"1\"},{\"content\":\"啊没有了之后我没想;time=106290 108860\",\"talkertype\":\"2\"},{\"content\":\"嗯;time=109550 109780\",\"talkertype\":\"2\"},{\"content\":\"就是换手机之后在名下是吧那之后的话呢就需要说下载一下因为最重要的呢就是说万一发生理赔的话咱会通过咱这个软件买的伤残资料三到五个工作日做理赔的是这样的咯;time=109890 122620\",\"talkertype\":\"1\"},{\"content\":\"您到时候;time=123130 123640\",\"talkertype\":\"1\"},{\"content\":\"啊如如果如果我不想;time=123930 127900\",\"talkertype\":\"2\"},{\"content\":\"哦您这个也是必须要下载这个软件就的微信是吧挺好下的为什么不下来;time=129150 133880\",\"talkertype\":\"1\"},{\"content\":\"您是先麻烦吗还是怎么不会像呀;time=135070 137420\",\"talkertype\":\"1\"},{\"content\":\"没关哎能听到吗;time=142410 143760\",\"talkertype\":\"1\"},{\"content\":\"喂;time=145890 146440\",\"talkertype\":\"1\"}]");
////        esJsonAnnoy.put("ANNOY_NUM", 0);//命中骚扰的次数
//
//
//        esJsonAnnoy.put("CALL_PHONE","018988924693");
//        esJsonAnnoy.put("CREATE_TIME", "2017-09-23 18:54:38");
//        esJsonAnnoy.put("SEAT_ATTITUDE", "");
//        esJsonAnnoy.put("ANNOY_TYPE",  "正常");
//        esJsonAnnoy.put("ANNOY_HISTORY_TYPE",  "正常");//历史骚扰详细
//        esJsonAnnoy.put("VOICE_ID", "hwc1a11675d20170922t1842242m0v1@I");
//        esJsonAnnoy.put("STAFF_ID", "11675");
//        esJsonAnnoy.put("ANNOY_WORD", "");
//        esJsonAnnoy.put("CALL_START_TIME", "2017-09-23 18:38:27");
//        esJsonAnnoy.put("CALL_END_TIME", "2017-09-23 18:38:27");
//        esJsonAnnoy.put("CALL_CONTENT", "那 行☆那 我☆喂☆啊 你好 这边 是 陈先生 对 吧☆是 的☆嗯☆先☆嗯☆啊 这里 是 平安 这边 李 经理 啊 上午 给 您 联系 过 了 但是 我 是 跟 你 说到 公司 安排 呢 有 三点 服务 给 您 做 个 通知 嘛 那 上午 是 给 您 讲 了 第 一点 第 一点 呢 就 关于 贷款 这 方面 做 一个 个人 额度 最高 五十万 无 抵押 免 担保 小额 信用 贷款 长期有效 服务 到 您 本人 五十五 周岁 也 费用☆的 嘛 这个 你 自己 都 还记得 对 吧☆嗯☆嗯☆生活☆嗯☆嗯☆嗯 那 行 那 这边 接 给 你 通知 啊 那 第二点 给 您 通知 我们 平时 肯定 会 帮 自己 和 家人 攒钱 的 但是 我们 客户 说 就是 和 上 大钱 呢 我们 拿去 投资 做生意 花掉 这些 我 本身 配 啊 但是 小钱 可能 放到 哪里 都 没有 太 好 的 服务 也 发 不 出 价值 只能 官方 的 所以 针对 这些 到 呢 是 给 处于☆通知 的 客户 邀请 到 可以 将 自己 生活 上 用不着 了 一个 小钱 零 头 在 通过 一个 代扣 代 攒 方式 统一 划 扣☆划 扣 到 平安 给 自己做 一个 小额 的 财富 积攒☆然后☆a☆啊☆啊☆那 这个 呢 是 小额 但是 不是 说白 攒 啊 那 攒 一千 拿回去 有 一千一百 八 攒 两千 拿回去 一共 是 两千 三百六 以此类推 多 攒 多 的 不是 放心 这个 不是 叫 你 做 任何 的 投资 或者 理财 不是 的 攒 在 平安 这个 毕竟 只是 零 头 而已 不 多 那 客户 说 要 转 多少 钱 发家致富 这些 呢 也 不可能☆他 给 到 我们 作用 啊 性质 呢 就 生活 上 小钱 票 保 乘坐 同时 确保 你 这个 资金 有一个 稳定 的 正 增长 那 这 是 第二点 通知 的 可以 理解 吗 陈先生☆那 这☆嗯 能 听得到吗 陈先生 这边☆嗯 听☆啊 你好☆哦 喂☆");
//        esJsonAnnoy.put("PLAT_FORM","XQD-ZHW");
//        esJsonAnnoy.put("VOICE_PATH", "1506076927-991850");
//        esJsonAnnoy.put("TRANS_CONTENT", "[{\"id\":\"018988924693\",\"talkertype\":\"2\",\"content\":\"那 行;time=2230 4830\",\"timestamp\":1506076707887,\"time\":\"18:38:27\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"250c32969f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"018988924693\",\"talkertype\":\"2\",\"content\":\"那 我;time=8640 15010\",\"timestamp\":1506076717175,\"time\":\"18:38:37\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"250c32969f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"018988924693\",\"talkertype\":\"2\",\"content\":\"喂;time=16090 16420\",\"timestamp\":1506076717487,\"time\":\"18:38:37\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"250c32969f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"11675\",\"talkertype\":\"1\",\"content\":\"啊 你好 这边 是 陈先生 对 吧;time=17580 20050\",\"timestamp\":1506076720079,\"time\":\"18:38:40\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"252d800e9f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"018988924693\",\"talkertype\":\"2\",\"content\":\"是 的;time=20740 21140\",\"timestamp\":1506076721206,\"time\":\"18:38:41\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"250c32969f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"018988924693\",\"talkertype\":\"2\",\"content\":\"嗯;time=25460 25710\",\"timestamp\":1506076726941,\"time\":\"18:38:46\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"250c32969f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"018988924693\",\"talkertype\":\"2\",\"content\":\"先;time=40400 40580\",\"timestamp\":1506076742918,\"time\":\"18:39:02\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"250c32969f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"018988924693\",\"talkertype\":\"2\",\"content\":\"嗯;time=42220 42440\",\"timestamp\":1506076743101,\"time\":\"18:39:03\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"250c32969f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"11675\",\"talkertype\":\"1\",\"content\":\"啊 这里 是 平安 这边 李 经理 啊 上午 给 您 联系 过 了 但是 我 是 跟 你 说到 公司 安排 呢 有 三点 服务 给 您 做 个 通知 嘛 那 上午 是 给 您 讲 了 第 一点 第 一点 呢 就 关于 贷款 这 方面 做 一个 个人 额度 最高 五十万 无 抵押 免 担保 小额 信用 贷款 长期有效 服务 到 您 本人 五十五 周岁 也 费用;time=21580 41130\",\"timestamp\":1506076744150,\"time\":\"18:39:04\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"252d800e9f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"11675\",\"talkertype\":\"1\",\"content\":\"的 嘛 这个 你 自己 都 还记得 对 吧;time=41130 43280\",\"timestamp\":1506076744192,\"time\":\"18:39:04\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"252d800e9f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"018988924693\",\"talkertype\":\"2\",\"content\":\"嗯;time=43950 44300\",\"timestamp\":1506076745879,\"time\":\"18:39:05\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"250c32969f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"018988924693\",\"talkertype\":\"2\",\"content\":\"嗯;time=48520 48660\",\"timestamp\":1506076749909,\"time\":\"18:39:09\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"250c32969f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"018988924693\",\"talkertype\":\"2\",\"content\":\"生活;time=64760 65070\",\"timestamp\":1506076767206,\"time\":\"18:39:27\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"250c32969f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"018988924693\",\"talkertype\":\"2\",\"content\":\"嗯;time=65860 66110\",\"timestamp\":1506076767220,\"time\":\"18:39:27\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"250c32969f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"018988924693\",\"talkertype\":\"2\",\"content\":\"嗯;time=71480 71670\",\"timestamp\":1506076774614,\"time\":\"18:39:34\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"250c32969f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"11675\",\"talkertype\":\"1\",\"content\":\"嗯 那 行 那 这边 接 给 你 通知 啊 那 第二点 给 您 通知 我们 平时 肯定 会 帮 自己 和 家人 攒钱 的 但是 我们 客户 说 就是 和 上 大钱 呢 我们 拿去 投资 做生意 花掉 这些 我 本身 配 啊 但是 小钱 可能 放到 哪里 都 没有 太 好 的 服务 也 发 不 出 价值 只能 官方 的 所以 针对 这些 到 呢 是 给 处于;time=44800 66290\",\"timestamp\":1506076777286,\"time\":\"18:39:37\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"252d800e9f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"11675\",\"talkertype\":\"1\",\"content\":\"通知 的 客户 邀请 到 可以 将 自己 生活 上 用不着 了 一个 小钱 零 头 在 通过 一个 代扣 代 攒 方式 统一 划 扣;time=66290 74780\",\"timestamp\":1506076777300,\"time\":\"18:39:37\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"252d800e9f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"11675\",\"talkertype\":\"1\",\"content\":\"划 扣 到 平安 给 自己做 一个 小额 的 财富 积攒;time=74400 78160\",\"timestamp\":1506076778357,\"time\":\"18:39:38\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"252d800e9f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"018988924693\",\"talkertype\":\"2\",\"content\":\"然后;time=81040 81260\",\"timestamp\":1506076781452,\"time\":\"18:39:41\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"250c32969f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"018988924693\",\"talkertype\":\"2\",\"content\":\"a;time=87780 87810\",\"timestamp\":1506076788795,\"time\":\"18:39:48\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"250c32969f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"018988924693\",\"talkertype\":\"2\",\"content\":\"啊;time=89160 89340\",\"timestamp\":1506076792219,\"time\":\"18:39:52\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"250c32969f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"018988924693\",\"talkertype\":\"2\",\"content\":\"啊;time=97240 97460\",\"timestamp\":1506076800115,\"time\":\"18:40:00\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"250c32969f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"11675\",\"talkertype\":\"1\",\"content\":\"那 这个 呢 是 小额 但是 不是 说白 攒 啊 那 攒 一千 拿回去 有 一千一百 八 攒 两千 拿回去 一共 是 两千 三百六 以此类推 多 攒 多 的 不是 放心 这个 不是 叫 你 做 任何 的 投资 或者 理财 不是 的 攒 在 平安 这个 毕竟 只是 零 头 而已 不 多 那 客户 说 要 转 多少 钱 发家致富 这些 呢 也 不可能;time=78820 101830\",\"timestamp\":1506076803967,\"time\":\"18:40:03\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"252d800e9f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"11675\",\"talkertype\":\"1\",\"content\":\"他 给 到 我们 作用 啊 性质 呢 就 生活 上 小钱 票 保 乘坐 同时 确保 你 这个 资金 有一个 稳定 的 正 增长 那 这 是 第二点 通知 的 可以 理解 吗 陈先生;time=102320 113410\",\"timestamp\":1506076815834,\"time\":\"18:40:15\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"252d800e9f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"11675\",\"talkertype\":\"1\",\"content\":\"那 这;time=114000 114240\",\"timestamp\":1506076815865,\"time\":\"18:40:15\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"252d800e9f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"11675\",\"talkertype\":\"1\",\"content\":\"嗯 能 听得到吗 陈先生 这边;time=117600 119810\",\"timestamp\":1506076822721,\"time\":\"18:40:22\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"252d800e9f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"11675\",\"talkertype\":\"1\",\"content\":\"嗯 听;time=121920 122380\",\"timestamp\":1506076823715,\"time\":\"18:40:23\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"252d800e9f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"11675\",\"talkertype\":\"1\",\"content\":\"啊 你好;time=123740 124420\",\"timestamp\":1506076824714,\"time\":\"18:40:24\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"252d800e9f8211e7\",\"answer\":\"\",\"answerType\":\"0\"},{\"id\":\"11675\",\"talkertype\":\"1\",\"content\":\"哦 喂;time=129480 130060\",\"timestamp\":1506076831001,\"time\":\"18:40:31\",\"cmd\":\"\",\"userphone\":\"018988924693\",\"channelId\":\"252d800e9f8211e7\",\"answer\":\"\",\"answerType\":\"0\"}]");
//        esJsonAnnoy.put("ANNOY_NUM", 0);//命中骚扰的次数
//
//        annoyList.add(esJsonAnnoy.toJSONString());
//        indexBatchAnnoy(type,annoyList);
//    }

    public static String createIndex(String url, String query) {
        return HTTPMethod.doPostQuery(url, query, 3000);
    }

    private static String successIndexPostfix() {
        Calendar cl = Calendar.getInstance();
        cl.setTime(new Date());
        cl.add(Calendar.MONTH, 1);
        return StringUtil.dateToString(cl.getTime(), "yyyy-MM");
    }

    /**
     * @Title: indexBatchSuccess
     * @Description: 插入成功件
     * @param type
     * @param dataList
     * @return: void
     */
    public static void indexBatchSuccess(String type,List<String> dataList){
        try {
            String query ="";
            if(dataList!=null&&dataList.size()>0){
                for(String data:dataList){
                    String uuid = JSON.parseObject(data).getString("ORDER_ID");
                    query+="{ \"index\": {\"_id\":\""+uuid+"\"}}\n";
                    query+=data+"\n";
                }
                String indexUrl = HOST;
                String result = HTTPMethod.doPutQuery(indexUrl+"pa_success"+type+"/success/"+"_bulk", query, 5000);
                //改为日志
                System.out.println(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Title: indexBatchContent
     * @Description: 插入成功件文本内容
     * @param type
     * @param dataList
     * @return: void
     */
    public static void indexBatchContent(String type,List<String> dataList){
        try {
            String query ="";
            if(dataList!=null&&dataList.size()>0){
                for(String data:dataList){
                    String uuid = JSON.parseObject(data).getString("VOICE_ID");
                    query+="{ \"index\": {\"_id\":\""+uuid+"\"}}\n";
                    query+=data+"\n";
                }
                String indexUrl = HOST;
                String result = HTTPMethod.doPutQuery(indexUrl+"pa_success"+type+"/content/"+"_bulk", query, 5000);
                //改为日志
                System.out.println(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Title: indexBatchAnnoy
     * @Description: 插入防骚扰数据
     * @param type
     * @param dataList
     * @return: void
     */
    public static void indexBatchAnnoy(String type,List<String> dataList){
        try {
            String query ="";
            if(dataList!=null&&dataList.size()>0){
                for(String data:dataList){
                    String uuid = JSON.parseObject(data).getString("VOICE_ID");
                    query+="{ \"index\": {\"_id\":\""+uuid+"\"}}\n";
                    query+=data+"\n";
                }
                String indexUrl = HOST;
                String result = HTTPMethod.doPutQuery(indexUrl+"pa_annoy"+type+"/annoy/"+"_bulk", query, 5000);
                //改为日志
                System.out.println("CheckAnnoyResultThread插入es返回结果"+result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
