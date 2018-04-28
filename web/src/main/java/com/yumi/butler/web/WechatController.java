package com.yumi.butler.web;

import com.yumi.butler.common.RequestResult;
import com.yumi.butler.constant.CommonFlag;
import com.yumi.butler.domain.*;
import com.yumi.butler.service.*;
import com.yumi.butler.service.*;
import com.yumi.butler.utils.CoodinateCovertor;
import com.yumi.butler.utils.DESHelper;
import com.yumi.butler.utils.HeaderUtils;
import com.yumi.butler.utils.LngLat;
import com.yumi.butler.vo.*;
import com.yumi.netty.tools.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yumi.butler.domain.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Leo on 2017/10/9.
 */

@RestController
@RequestMapping("/api/wx")
@Api(value = "WechatController", description = "微信相关接口")
public class WechatController {
    @Value("${storepath.wxacode}")
    private String wxacodeUrl;

    @Autowired
    private IWechataccountRepository iWechataccountRepository;
    @Autowired
    private IWechattokenRepository iWechattokenRepository;
    @Autowired
    private IInnRepository iInnRepository;
    @Autowired
    private IRoomRepository iRoomRepository;
    @Autowired
    private IFavoriteRepository iFavoriteRepository;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private IServicesRepository iServicesRepository;
    @Autowired
    private InnService innService;
    @Autowired
    private AdmireInfoService admireInfoService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentTagService commentTagService;
    @Autowired
    private WxSerivice wxSerivice;
    @Autowired
    private ISOrderRepository isOrderRepository;
    @Autowired
    private ISOrderDetailRepository isOrderDetailRepository;
    @Autowired
    private HotelCommodityService hotelCommodityService;

    private String page = "pages/index/index";


    @GetMapping("/getOpenId")
    @ApiOperation(value = "WX|获取用户openid")
    public RequestResult getOpenId(@RequestParam(value = "platformCode", required = true, defaultValue = "") String platformCode,
                                   @RequestParam(value = "code", required = true, defaultValue = "") String code){

        Wechataccount wechataccount = iWechataccountRepository.findByPlatformAndFlag(platformCode, CommonFlag.VALID.getValue());
        if(null == wechataccount){
            return RequestResult.fail("没有找到对应的应用数据");
        }

        String appid = wechataccount.getAppId();
        String secret = DESHelper.decrypt(wechataccount.getSecret(), StringUtils.sortByChart(appid.concat("lavandeinn")).substring(0, 8));//wechataccount.getSecret()
        if(StringUtils.isNULL(appid)|| StringUtils.isNULL(secret)){
            return RequestResult.fail("应用数据错误");
        }
//        secret = DESHelper.decrypt(secret, appid.concat("wechat_account").substring(0, 8));

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={APPID}&secret={APPSECRET}&js_code={JSCODE}&grant_type=authorization_code";
        Map<String, String> params = new HashMap<String, String>();
        params.put("APPID",appid);//"wx47823b34812dcbf5");
        params.put("APPSECRET",secret);//"0a5ad3ae285b8673684c5279f8ef28a3");
        params.put("JSCODE",code);
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, params);


        if(null!=result) {
            JSONObject json = JSONObject.parseObject(result.getBody());

            if(null!=json && !json.containsKey("errmsg")){

                return RequestResult.success(json);
            }
        }

        return RequestResult.fail("获取openid失败!");
    }


    @GetMapping("/getButlerInfo")
    @ApiOperation(value = "WX|获取门店信息以及管家资料")
    public RequestResult getButlerInfo(@RequestParam(value = "inncode", required = true) String inncode,
                                       @RequestParam(value = "openid", required = true) String openid){

        if(StringUtils.isNULL(inncode)){
            return RequestResult.fail("参数有误!");
        }

        List<UserVo> userVoList = sysUserService.findAllUserByInncode(inncode, openid);
        Inn inn = iInnRepository.findByInnCode(inncode);

        //Integer favoriteQty = iFavoriteRepository.countByInnCodeAndAccountAndFlag(inncode, account, CommonFlag.VALID.getValue());
        //Favorite favorite = iFavoriteRepository.findByInnCodeAndAccountAndOpenid(inncode, account, openid);
        //map.put("favoriteQty", favoriteQty);
        //map.put("favorited", (null==favorite ||favorite.getFlag()==CommonFlag.INVALID.getValue()) ? 0 : 1);

        Map<String, Object> map = new HashMap<>();
        map.put("butler", userVoList);
        map.put("inn", inn);

        List<Map<String, String>> notices = new ArrayList<>();
        Map<String, String> noticeMap = new HashMap<>();

        File noticeDict = new File(wxacodeUrl + File.separator + "notice");
        if (noticeDict.isDirectory()){
            for (String noticeFileName : noticeDict.list()){
                if (noticeFileName.lastIndexOf(".") >0 && "PNG,JPG".indexOf(noticeFileName.substring(noticeFileName.lastIndexOf(".") + 1).toUpperCase())>0 ){
                    noticeMap = new HashMap<>();
                    noticeMap.put("url", noticeFileName);
                    noticeMap.put("desc", noticeFileName);
                    notices.add(noticeMap);
                }
            }
        }
        map.put("notices", notices);

        return  RequestResult.success(map);
    }

    @ApiOperation(value = "新增|Favorite")
    @PostMapping("/star")
    @Transactional
    public RequestResult add(@RequestBody Favorite favorite){

        if (StringUtils.isNULL(favorite.getAccount()) || StringUtils.isNULL(favorite.getInnCode()) || StringUtils.isNULL(favorite.getOpenid())){
            return RequestResult.fail("account、openid、inncode不能为空");
        }

        Favorite old_favorite = iFavoriteRepository.findByInnCodeAndAccountAndOpenid(favorite.getInnCode(), favorite.getAccount(), favorite.getOpenid());
        if(null != old_favorite){
            old_favorite.setFlag(favorite.getFlag());
            old_favorite.setCreateTime(new Date());
            iFavoriteRepository.saveAndFlush(old_favorite);
        }else{
            favorite.setCreateTime(new Date());
            iFavoriteRepository.saveAndFlush(favorite);
        }


        return RequestResult.success("ok");
    }


    @GetMapping("/getToken/{platform}")
    @ApiOperation(value = "WX|获取AccessToken")
    public RequestResult getAccessToken(@NotNull @PathVariable("platform") String platform) {
        String token = "";

        Wechataccount wechataccount = iWechataccountRepository.findByPlatformAndFlag(platform, 1);
        if(null == wechataccount){
            return RequestResult.fail("无法获取AppId配置信息");
        }

        Wechattoken wechattoken = iWechattokenRepository.findByAccountIdAndFlag(wechataccount.getId(),1);
        if(wechattoken == null || StringUtils.isNULL(wechattoken.getAccessToken()) || wechattoken.getAccessToken().equalsIgnoreCase("null") || new Date().getTime() > wechattoken.getExpiresTime().getTime()){
            //获取token
            String appid = wechataccount.getAppId();
            String secret = DESHelper.decrypt(wechataccount.getSecret(), StringUtils.sortByChart(appid.concat("lavandeinn")).substring(0, 8));
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={APPID}&secret={APPSECRET}";
            Map<String, String> params = new HashMap<String, String>();
            params.put("APPID",appid);//"wx47823b34812dcbf5");
            params.put("APPSECRET", secret);//wechataccount.getSecret()//"0a5ad3ae285b8673684c5279f8ef28a3");
            ResponseEntity<String> result = restTemplate.getForEntity(url,String.class, params);

            JSONObject json = JSONObject.parseObject(result.getBody());
            token = String.valueOf(json.get("access_token"));

            //判断接口是否正确返回
            if(token != null){
                if(wechattoken == null){
                    wechattoken = new Wechattoken();
                    wechattoken.setAccountId(wechataccount.getId());
                    wechattoken.setFlag(CommonFlag.VALID.getValue());
                }
                wechattoken.setExpiresTime(StringUtils.modifyDate(new Date(), 0, 0, 0, 0, 0, 7000)); //默认2个小时失效，
                wechattoken.setAccessToken(token);
                iWechattokenRepository.saveAndFlush(wechattoken);
            }
        } else {
            token = wechattoken.getAccessToken();
        }

        return RequestResult.success(token);
    }

    @ApiOperation(value = "下载|Room Qrcode")
    @GetMapping("/getQrcode")
    public void getRoomQrCode(@RequestParam(value = "id", required = true)  long roomId) throws IOException {
        Room room = iRoomRepository.findOne(roomId);
        if(null == room){
            return; //RequestResult.fail("无法房间信息");
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("path", "pages/index/index");
        params.put("scene", room.getInnCode() + "_" + room.getRoomNo());
        JSONObject json =  JSONObject.parseObject(JSON.toJSONString(params));

        RequestResult tokenResult = getAccessToken("Bee");
        if(null == tokenResult || !tokenResult.isSuccess()){
            return;// RequestResult.fail("无法获取获取token信息失败");
        }

        String token = tokenResult.getData().toString();
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + token;
        String result = null;

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");

        StringEntity se = new StringEntity(json.toString());
        se.setContentType("application/json");
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                "UTF-8"));
        httpPost.setEntity(se);

        HttpResponse resp = httpClient.execute(httpPost);

        if (resp != null) {
            HttpEntity resEntity = resp.getEntity();
            if (resEntity != null) {
                InputStream instreams = resEntity.getContent();

                HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
                response.reset();
                response.setHeader("Content-Disposition", "attachment;filename="+ (room.getInnCode() + "_" + room.getRoomNo()+".png"));
                response.setContentType("application/octet-stream");
                OutputStream out = response.getOutputStream();
                //写文件
                int b;
                byte[] bytes = new byte[1024];
                while ((b = instreams.read(bytes)) > 0){
                    out.write(bytes, 0, b);
                }
                instreams.close();
            }
        }
    }

    @GetMapping("/getQrCodeImg/{platform}/{innCode}/{roomNo}")
    @ApiOperation(value = "WX|获取并保存房间二维码")
    public RequestResult getQrCodeImg(@NotNull @PathVariable("platform") String platform,
                                      @NotNull @PathVariable("innCode") String innCode,
                                      @NotNull @PathVariable("roomNo") String roomNo) throws  Exception{
        String fileName = "";
        RequestResult tokenResult = getAccessToken(platform);
        if(null == tokenResult || !tokenResult.isSuccess()){
            return RequestResult.fail("无法获取获取token信息失败");
        }
        String token = tokenResult.getData().toString();

        Map<String, String> params = new HashMap<String, String>();
        //params.put("path", page + "?innCode=" + innCode + "&roomNo=" + roomNo);
        params.put("path", page);
        params.put("scene", innCode + "_" + roomNo);
        JSONObject json =  JSONObject.parseObject(JSON.toJSONString(params));

//        String url = "https://api.weixin.qq.com/wxa/getwxacode?access_token=" + token;
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + token;
        String result = null;

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");

        StringEntity se = new StringEntity(json.toString());
        se.setContentType("application/json");
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                "UTF-8"));
        httpPost.setEntity(se);

        HttpResponse response = httpClient.execute(httpPost);
        if (response != null) {
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                InputStream instreams = resEntity.getContent();
                fileName =  File.separator + "qrcode" + File.separator + innCode +File.separator + roomNo + ".png";

                File saveFile = new File(wxacodeUrl +  fileName);
                // 判断这个文件（saveFile）是否存在
                if (!saveFile.getParentFile().exists()) {
                    // 如果不存在就创建这个文件夹
                    saveFile.getParentFile().mkdirs();
                }
                saveToImgByInputStream(instreams, wxacodeUrl, fileName);
            }
        }
        return RequestResult.success(fileName);
    }

    @GetMapping("/getAllQrCodeImg")
    @ApiOperation(value = "WX|获取所有并保存房间二维码")
    public RequestResult getAllQrCodeImg(@RequestParam(value = "platform", required = true) String platform,
                                         @RequestParam(value = "innCode", required = true) String innCode) throws  Exception{
        RequestResult tokenResult = getAccessToken(platform);
        if(null == tokenResult || !tokenResult.isSuccess()){
            return RequestResult.fail("无法获取获取token信息失败");
        }
        String token = tokenResult.getData().toString();
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + token;

        Pageable pageable = new PageRequest(0, 5000, Sort.Direction.ASC, "innCode", "roomNo");

//        Page<Room> roomPage = iRoomRepository.findByInnCodeLikeAndFlag(pageable, "", 1);//.findByInnCodeOrderByRoomNo(innCode);
//        List<Room> roomList = roomPage.getContent();

        List<Room> roomList = iRoomRepository.findByInnCodeOrderByRoomNo(innCode);

        String fileName = "";
        DefaultHttpClient httpClient = null;
        HttpPost httpPost = null;
        StringEntity se = null;
        HttpEntity resEntity = null;

        Map<String, String> params = null;
        JSONObject json = null;
        InputStream instreams = null;

        for (Room room : roomList){
            //判断本地是否存在二维码图片
            if(isExistsFile(room.getInnCode(), room.getRoomNo())){
                continue;
            }
            System.out.println(room.getRoomNo());

            params = new HashMap<String, String>();
            params.put("path", page);
            params.put("scene", room.getInnCode() + "_" + room.getRoomNo());
            json =  JSONObject.parseObject(JSON.toJSONString(params));

            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(url);
            httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");

            se = new StringEntity(json.toString());
            se.setContentType("application/json");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                    "UTF-8"));
            httpPost.setEntity(se);

            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                resEntity = response.getEntity();
                if (resEntity != null) {
                    instreams = resEntity.getContent();
                    fileName =  File.separator + "qrcode" + File.separator + room.getInnCode() + File.separator + room.getRoomNo() + ".png";

                    File saveFile = new File(wxacodeUrl + fileName);
                    // 判断这个文件（saveFile）是否存在
                    if (!saveFile.getParentFile().exists()) {
                        // 如果不存在就创建这个文件夹
                        saveFile.getParentFile().mkdirs();
                    }
                    saveToImgByInputStream(instreams, wxacodeUrl, fileName);
                }
            }
        }

        return RequestResult.success("ok");
    }

    @GetMapping("/getQrCodeImgUrl/{platform}/{innCode}/{roomNo}")
    @ApiOperation(value = "WX|获取并保存房间二维码链接")
    public RequestResult getQrCodeImgUrl(@NotNull @PathVariable("platform") String platform,
                                      @NotNull @PathVariable("innCode") String innCode,
                                      @NotNull @PathVariable("roomNo") String roomNo) throws  Exception{
        String fileName =  wxacodeUrl + File.separator + "qrcode" + File.separator + innCode + File.separator + roomNo + ".png";
        if(!isExistsFile(innCode, roomNo)){
            //如果不存在文件，则生成一遍
            getQrCodeImg(platform, innCode, roomNo);
        }
        return RequestResult.success(fileName);
    }

    @ApiOperation(value = "Services|列表")
    @GetMapping("/getTopService")
    public RequestResult list(@RequestParam(value = "pageIdx", required = false, defaultValue = "0") Integer pageIdx,
                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        Pageable pageable = new PageRequest(pageIdx, pageSize, Sort.Direction.DESC, "id");
        return RequestResult.success(iServicesRepository.findByFlagOrderBySortDesc(pageable, 1));
    }

    @ApiOperation(value = "Nearest Inn|最近门店")
    @GetMapping("/getNearstInn")
    public RequestResult getNearestInn(@RequestParam(value = "longitude", required = true) String longitude,
                              @RequestParam(value = "latitude", required = true) String latitude) {
        LngLat currentPoint = new LngLat(Double.valueOf(longitude), Double.valueOf(latitude));
        currentPoint = CoodinateCovertor.bd_encrypt(currentPoint);

        List<InnVo> innList = innService.findValidInn();
        for (InnVo inn : innList){
            inn.setDistance(CoodinateCovertor.calculateDistance(new LngLat(inn.getLongitude().doubleValue(), inn.getLatitude().doubleValue()), currentPoint));
        }

        Collections.sort(innList);
        return RequestResult.success(innList.size()>0?innList.subList(0,1):new ArrayList<InnVo>());
    }

    @ApiOperation(value = "AdmireInfo|保存赞赏")
    @PostMapping("/saveAdmireInfo")
    public RequestResult saveAdmireInfo(@RequestBody AdmireInfo admireInfo){
        admireInfo.setCreateTime(new Date());
        admireInfoService.save(admireInfo);
        return RequestResult.success("ok");
    }

    @ApiOperation(value = "AdmireInfo|统计个人赞赏数")
    @GetMapping("getCount")
    public RequestResult getCount(@RequestParam(value = "innCode", required = true) String innCode,
                                  @RequestParam(value = "name", required = true) String name){
        int count = admireInfoService.count(innCode,name);
        return RequestResult.success(count);
    }

    @ApiOperation(value = "Comment|获取tag")
    @GetMapping("getTagAll")
    public RequestResult getTagAll(){
        List<CommentTag> commentTagList = commentTagService.tagList();
        return RequestResult.success(commentTagList);
    }

    @ApiOperation(value = "Comment|保存评价")
    @PostMapping("saveComment")
    public RequestResult saveComment(@RequestBody CommentVo commentVo){
        String status = "";
        try {
            status = commentService.save(commentVo);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if(status.equals("ok")){
            return RequestResult.success(status);
        }
        return RequestResult.fail("保存失败！");
    }

    @ApiOperation(value = "Comment|统计个人评价")
    @GetMapping("getCommetCount")
    public RequestResult getCommetCount(@RequestParam(value = "innCode", required = true) String innCode,
                                  @RequestParam(value = "name", required = true) String name){
        BigDecimal count = commentService.count(innCode,name);
        return RequestResult.success(count);
    }

    @ApiOperation(value = "WIFI|获取WiFi密码")
    @GetMapping("getWiFi")
    public RequestResult getWiFi(@RequestParam(value = "innCode", required = true) String inncode,
                                 @RequestParam(value = "roomNo", required = true) String roomNo){
        Room room = iRoomRepository.findByInnCodeAndRoomNo(inncode,roomNo);
        if(room == null){
            return RequestResult.fail("无此房间");
        }
        return RequestResult.success(room);
    }

    @ApiOperation(value = "Category|获取门店类别")
    @GetMapping("getCategory")
    public RequestResult getCategory(@RequestParam(value = "hotelId", required = true) String hotelId){
        List<SHotelCategory> hotelCategoryList = wxSerivice.hotelCategories(hotelId);

        if(hotelCategoryList.size() == 0){
            return RequestResult.fail("此房间暂无商品");
        }
        return RequestResult.success(hotelCategoryList);
    }

    @ApiOperation(value = "NewCommodity|获取新品商品")
    @GetMapping("getNewCommodity")
    public RequestResult getNewCommodity(@RequestParam(value = "hotelId", required = true) String hotelId){
        List<CommodityVO> commodityVOList = wxSerivice.queryNewCommodity(hotelId);

        if(commodityVOList.size() == 0){
            return RequestResult.fail("暂无新商品");
        }
        return RequestResult.success(commodityVOList);
    }

    @ApiOperation(value = "HotCommodity|获取热门商品")
    @GetMapping("getHotCommodity")
    public RequestResult getHotCommodity(@RequestParam(value = "hotelId", required = true) String hotelId){
        List<CommodityVO> commodityVOList = wxSerivice.queryHotCommodity(hotelId);

        if(commodityVOList.size() == 0){
            return RequestResult.fail("暂无新商品");
        }
        return RequestResult.success(commodityVOList);
    }

    @ApiOperation(value = "LikeCommodity|获取喜欢的商品")
    @GetMapping("getLikeCommodity")
    public RequestResult getLikeCommodity(@RequestParam(value = "hotelId", required = true) String hotelId){
        List<CommodityVO> commodityVOList = wxSerivice.queryLikeCommodity(hotelId);

        if(commodityVOList.size() == 0){
            return RequestResult.fail("暂无新商品");
        }
        return RequestResult.success(commodityVOList);
    }

    @ApiOperation(value = "KeywordCommodity|搜索商品")
    @GetMapping("getKeywordCommodity")
    public RequestResult getKeywordCommodity(@RequestParam(value = "hotelId", required = true) String hotelId,
                                             @RequestParam(value = "keyword", required = true) String keyword,
                                             @RequestParam(value = "pageIdx", required = false,defaultValue = "0") Integer pageIdx,
                                             @RequestParam(value = "pageSize", required = false,defaultValue = "10") Integer pageSize){
        List<CommodityVO> commodityVOList = hotelCommodityService.queryHotelCommodity(pageIdx,pageSize,hotelId,keyword);

        if(commodityVOList.size() == 0){
            return RequestResult.fail("暂无该商品");
        }
        return RequestResult.success(commodityVOList);
    }

    @ApiOperation(value = "Commodity|获取商品")
    @GetMapping("getCommodity")
    public RequestResult getCommodity(@RequestParam(value = "hotelId", required = true) String hotelId,
                                      @RequestParam(value = "categoryCode", required = true) String categoryCode,
                                      @RequestParam(value = "pageIdx", required = false,defaultValue = "0") Integer pageIdx,
                                      @RequestParam(value = "pageSize", required = false,defaultValue = "10") Integer pageSize){
        List<CommodityVO> commodityVOList = wxSerivice.queryCommodity(hotelId,categoryCode,pageIdx,pageSize);

        if(commodityVOList.size() == 0){
            return RequestResult.fail("暂无新商品");
        }
        return RequestResult.success(commodityVOList);
    }

    @ApiOperation(value = "Payment|保存支付信息")
    @PostMapping("savePayment")
    public RequestResult savePayment(@RequestBody WxOrderVO wxOrderVO){
        SOrder sOrder = wxSerivice.saveOrder(wxOrderVO);
        return RequestResult.success(sOrder);
    }

    @ApiOperation(value = "CheckStorage|检查库存")
    @PostMapping("checkStorage")
    public RequestResult checkStorage(@RequestBody WxOrderVO wxOrderVO){
        String status = wxSerivice.checkStorage(wxOrderVO);
        return RequestResult.success(status);
    }

    @ApiOperation(value = "cleanOrder|取消订单恢复库存")
    @PostMapping("cleanOrder")
    public RequestResult cleanOrder(@RequestBody WxOrderVO wxOrderVO){
        String status = wxSerivice.cleanOrder(wxOrderVO);
        return RequestResult.success(status);
    }

    public static int saveToImgByInputStream(InputStream instreams,String imgPath,String imgName){
        int stateInt = 1;
        if(instreams != null){
            try {
                File file=new File(imgPath+imgName);//可以是任何图片格式.jpg,.png等
                FileOutputStream fos=new FileOutputStream(file);

                byte[] b = new byte[1024];
                int nRead = 0;
                while ((nRead = instreams.read(b)) != -1) {
                    fos.write(b, 0, nRead);
                }
                fos.flush();
                fos.close();
            } catch (Exception e) {
                stateInt = 0;
                e.printStackTrace();
            } finally {
                try {
                    instreams.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return stateInt;
    }

    @ApiOperation(value = "WxOrder|获取订单列表")
    @GetMapping("getOrderListWx")
    public RequestResult getOrderListWx(@RequestParam(value = "openId", required = true) String openId,
                                      @RequestParam(value = "username", required = true) String username,
                                      @RequestParam(value = "status", required = true) Integer status,
                                      @RequestParam(value = "pageIdx", required = false,defaultValue = "0") Integer pageIdx,
                                      @RequestParam(value = "pageSize", required = false,defaultValue = "10") Integer pageSize){
        Page<SOrder> orderList = wxSerivice.queryOrderList(openId,username,status,pageIdx,pageSize);

        if(orderList == null){
            return RequestResult.fail("获取订单列表失败");
        }

        List<IOrderVO> iOrderVOS = new ArrayList<IOrderVO>();
        IOrderVO orderVO = new IOrderVO();
        for(SOrder sOrder : orderList){
            orderVO = wxSerivice.queryOrderDateil(sOrder.getOrderNumber());
            iOrderVOS.add(orderVO);
        }

        return RequestResult.success(iOrderVOS);
    }

    @ApiOperation(value = "WxOrder|获取订单详情")
    @GetMapping("getOrderInfoWx")
    public RequestResult getOrderInfoWx(@RequestParam(value = "orderNo", required = true) String orderNo){

        IOrderVO orderVO = wxSerivice.queryOrderDateil(orderNo);

        if(orderVO == null){
            return RequestResult.fail("获取订单详情失败");
        }
        return RequestResult.success(orderVO);
    }

    @ApiOperation(value = "更新|OrderStatusWx")
    @GetMapping(value = "OrderStatusWx")
    public RequestResult updateStatus(@RequestParam(value = "orderNo", required = true) String orderNo,
                                      @RequestParam(value = "status", required = true) Integer status) {

        SOrder sOrder = isOrderRepository.findByOrderNumberAndFlag(orderNo,CommonFlag.VALID.getValue());
        if(null == sOrder){
            return RequestResult.fail("无法获取纪录");
        }
        sOrder.setOrderStatus(status);
        sOrder = isOrderRepository.saveAndFlush(sOrder);
        return RequestResult.success(sOrder);
    }



    /**
     * 本地是否存在该房间二维码图片
     * @return
     */
    public Boolean isExistsFile(String innCode, String roomNo){
        Boolean isExistsFile = false;
        String fileName =  File.separator + "qrcode" + File.separator + innCode + File.separator + roomNo + ".png";
        File saveFile = new File(wxacodeUrl + fileName);
        // 判断这个文件（saveFile）是否存在
        if (saveFile.exists()) {
            isExistsFile = true;
        }

        return isExistsFile;
    }

}
