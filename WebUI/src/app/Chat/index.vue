


<template>
  <div class="chat" style="">
    <password-modal v-on:onChangePwd="onChangePwd"/>
    <reply-modal v-on:saveReply="saveReply"/>
    <view-image-modal />
    <div class="slide-bar">
    <!-- 
    v-on:onShowModl="onShowChangePwd"表示如果触发子组件中的onShowModl事件
    则连带触发父组件中的onShowChangePwd事件
     -->
      <user v-on:onShowModl="onShowChangePwd"></user>
      <list></list>
    </div>
    <div class="main">
      <notice></notice>
      <message ref="msg" v-on:onShowSourceImg="onShowImgModal"></message>
      <user-text ref="txt"></user-text>
    </div>
    <!-- <div class="right-bar">
        <div style="position: absolute;bottom: 0;height: 180px">
            <ul class="reply">
                <li>
                    <div style="width: 100%;display: flex;">
                        <font style="font-size:16px; color:#fd9b26; font-weight:bold">自定义回复语</font>
                        <div style="flex:1;text-align:right" @click="addReply">
                            <font style="text-decoration: underline;color: #ffffff;font-weight:bold">添 加</font>
                        </div>
                    </div>
                </li>
                <li v-for="rp in replys" v-bind:key="rp.id" v-bind:id="rp.id" >
                    <Row>
                        <Col span="20" style="text-overflow: ellipsis;overflow: hidden;" >
                            <div v-on:click="replyChoose(rp)" style="width:100%">
                                {{rp.title}}
                            </div>
                        </Col>
                        <Col span="4" style="text-align: right;" v-if="rp.title != '挂机'">
                            <font style="color: red;font-weight:bold;" v-on:click="delReply(rp)" >删除</font>
                        </Col>
                    </Row>
                </li>
            </ul>
        </div>
    </div> -->
    
        
    <div style="height: 40px;width: 100%;background-color: #ddd;position: fixed;bottom: 0;z-index:999;line-height:40px;left: 0;background:#31496d;display: flex;justify-content: space-between;color: #fff;font-size:15px;">
        
        <div style="display: flex;justify-content: space-around;width: 20%;">
            <div style="width: 100%;cursor: pointer;display: flex;justify-content: center;align-items: center;" @click="tabClick(0)">
                <Badge dot v-if="awaitListData>0">
                    <div>待派送({{awaitListData}})</div>
                </Badge>
                <div v-if="awaitListData == 0">待派送({{awaitListData}})</div>
            </div>    
            <div style="width: 100%;cursor:pointer;margin-left:10px;display: flex;justify-content: center;align-items: center;" @click="tabClick(1)">
                
                <Badge dot v-if="conveyData>0">
                    <div>配送中({{conveyData}})</div>
                </Badge>
                <div v-if="conveyData == 0">配送中({{conveyData}})</div>
            </div>
            <div style="width: 100%;cursor:pointer;margin-left:10px;display: flex;justify-content: center;align-items: center;" @click="tabClick(2)">
                
                <Badge dot v-if="arriveData>0">
                    <div>已配送({{arriveData}})</div>
                </Badge>
                <div v-if="arriveData < 1">已配送({{arriveData}})</div>
            </div>
        </div>
        <div style="margin-right: 40px;cursor: pointer;" @click="clickCommodity">
            <div>门店商品列表</div>
        </div>
    </div>

    <!-- 订单列表 -->
    <Modal v-model="orderListShow" width="900">
        <p slot="header" style="text-align:center">
            <span>{{tabName == 0 ? '待派送' : tabName == 1 ? '配送中' : tabName == 2 ? '已配送' : ''}}</span>
        </p>
        <div style="text-align:center">
            <div style="display: flex;justify-content: space-between;margin-bottom: 15px;">
                <div style="display: flex;">
                    <div>
                        <Input v-model="orderNo" placeholder="请输入订单编号查询" style="width: 300px"></Input>
                    </div>
                    <div>
                        <Button type="primary" @click="queryOrderNoSbm">查询</Button>
                    </div>
                </div>
                <div>
                    <div>
                        <Button type="warning" @click="refurbish">刷新</Button>
                    </div>
                    
                </div>
            </div>
            <Row>
                <Table stripe :loading="load" :columns="orderInfoColumns" :data="orderInfoData"></Table>
            </Row>
            <Row style="margin-top:5px">
                <Page :total="pageCount" :current="initPage" size="small" @on-change="changePage"></Page>    
            </Row>
        </div>
    </Modal>

    <!-- 订单详情 -->
    <Modal v-model="orderInfoShow" width="900">
        <p slot="header" style="text-align:center">
            <span>订单详情</span>
        </p>
        <div style="text-align:center">
            <Row v-show="LoadingVal">
              <Col class="demo-spin-col" span="24">
                  <Spin fix>
                      <Icon type="load-c" size=18 class="demo-spin-icon-load"></Icon>
                      <div>Loading</div>
                  </Spin>
              </Col>
            </Row>
            <Row v-show="!LoadingVal" style="margin-bottom:15px;text-align: left;">
              <Col span="8">门店：{{orderInfo.hotelName}}</Col>
              <Col span="8">房间：{{orderInfo.orderRoom}}</Col>
            </Row>
            <Row v-show="!LoadingVal" style="margin-bottom:15px;text-align: left;">
              <Col span="8">订单编号：{{orderInfo.orderNumber}}</Col>
              <Col span="8">支付编号：{{orderInfo.orderPaymentNumber}}</Col>
              <Col span="4">订单总额：{{orderInfo.orderSumPrice}}</Col>
              <Col span="4">订单状态：{{orderInfo.orderStatus === 0 ? '已支付' : (orderInfo.orderStatus === 1 ? '配送中' : (orderInfo.orderStatus === 2 ? '已送达' : '确认收货'))}}</Col>
            </Row>
            <Row v-show="!LoadingVal" style="margin-bottom:15px;text-align: left;">
              <Col span="8">下单人：{{orderInfo.orderCreateby}}</Col>
              <Col span="8">下单时间：{{orderInfo.orderCreatetime | formatTime}}</Col>
            </Row>
            <Row v-show="!LoadingVal" style="margin-bottom:15px;text-align: left;">
              <Col span="8">送货员：{{orderInfo.orderDeliveryby}}</Col>
              <Col span="8">送达时间：{{orderInfo.orderDeliverytime | formatTime}}</Col>
            </Row>
            <Row v-show="!LoadingVal">
              <Table stripe :columns="orderDetail" :data="orderDatailData"></Table>
            </Row>
        </div>
        <div slot="footer">
            <Button v-if="orderInfo.orderStatus === 0 || orderInfo.orderStatus === 1" type="primary" size="large"  @click="orderTaking(orderInfo.orderNumber)">
                {{orderInfo.orderStatus === 0 ? '接单配送': orderInfo.orderStatus === 1 ? '已送达' : ''}}
            </Button>
            <Button v-if="orderInfo.orderStatus === 1" type="error" size="large"  @click="salesClick(orderInfo.orderNumber)">
               退单
            </Button>
        </div>
    </Modal>

    <!-- 退单备注 -->
    <Modal v-model="sales" width="360">
        <p slot="header" style="color:#f60;text-align:center">
            <Icon type="information-circled"></Icon>
            <span>退单备注</span>
        </p>
        <div style="text-align:center">
            <Input v-model="salesRemark" type="textarea" :rows="4" placeholder="请填写退单备注"></Input>
        </div>
        <div slot="footer">
            <Button type="error" size="large" long @click="salesConfirm">确认退单</Button>
        </div>
    </Modal>

    <!-- 门店商品 -->
    <Modal v-model="commodityListView" width="900">
        <p slot="header" style="text-align:center">
            <span>本店商品</span>
        </p>
        <div style="text-align:center">
            <Row style="margin-top:10px">
                <Col>
                    <Table :loading="loadingCommodity" border :columns="CommodityColumns" :data="CommodityData"></Table>
                </Col>
            </Row>
            <Row style="margin-top:10px">
                <Col>
                    <Page :total="CommodityPageCount" :current="CommodityInitPage" @on-change="CommodityPage"></Page>
                </Col>
            </Row>
        </div>
        <div slot="footer">
        </div>
    </Modal>


  </div>
</template>



<script>
    import { mapState } from 'vuex'

    import User from './components/User.vue'
    import List from './components/List.vue';
    import Notice from './components/Notice.vue';
    import Message from './components/Message.vue';
    import UserText from './components/Text.vue';
    import PasswordModal from './components/PasswordModal.vue'
    import ReplyModal from './components/Reply.vue'
    import ViewImageModal from './components/ViewImageModal.vue'
    import PwdModel from '@/models/mdypwd'
    import Reply from '@/models/reply'
    import consts from '@/utils/consts'
    import twemoji from 'twemoji'
    import order from '@/models/order'
    import {formatDate} from '@/utils/date.js';
    import HotelCommodity from '@/models/hotelCommodity';

    export default {
        name: 'chat',
        computed: mapState([
          'chat',
          'service',
          'reply'
        ]),
        watch: {
          'chat.topService': {
            handler (newVal) {
                for (var i = newVal.length - 1; i >= 0; i--) {
                    newVal[i].icon_url = consts.STATIC_URL + '/service/' + newVal[i].serviceUrl
                }
               this.$set(this.chat, 'topService', newVal)
            }
          },
          'reply.replys': {
            handler (newVal) {
                this.replys = newVal.data
            }
          }
        }, 
        components:{
            User,List,Notice,Message,UserText,PasswordModal,ReplyModal,ViewImageModal
        },
        destroyed: function(){
            this.chat.connection.close();
        },
        created : function(){
            let _this = this;
            _this.get ()

            if(Notification.permission != 'granted'){
                Notification.requestPermission().then(function(permission) {
                    if(permission === 'granted'){
                        console.log('用户允许通知');
                    }else if(permission === 'denied'){
                        console.log('用户拒绝通知');
                    }
                });
            }

            _this.$store.dispatch('getTopService', {
              params: {
                pageIdx: 0,
                pageSize: 10
              }
            })

            var conn;//websocket实例
            var lockReconnect = false;//避免重复连接
            var wsUrl = consts.WEBSOCKET_URL; //'wss://butler.lavandehotels.club/ws';//'ws://localhost:5544/ws';//
            
            function createWebSocket(url) {
                try {
                    conn = new WebSocket(url);
                    initEventHandle(conn);

                } catch (e) {
                    reconnect(url);
                }     
            }

            function initEventHandle(conn) {
                conn.keepalive = function() { 
                    console.log(' HeartBeat ')
                };
                conn.onclose = function () {
                    _this.$store.dispatch('showNotice', ' 已断开连接！','error');
                    _this.$store.dispatch('changeStatus', false);

                    reconnect(wsUrl);
                };
                conn.onerror = function () {
                    _this.$store.dispatch('showNotice', ' 已断开连接！','error');
                    _this.$store.dispatch('changeStatus', false);

                    reconnect(wsUrl);
                };
                conn.onopen = function () {
                    //心跳检测重置
                    heartCheck.reset().start();

                    _this.$store.dispatch('showNotice', ' 连接成功！','success');
                    if(conn.readyState == WebSocket.OPEN){  
                        _this.$store.dispatch('setConn', conn);
                        let userinfo = JSON.parse(localStorage.getItem('lavande_butler_userinfo'));
                        let joinInfo = {type:"join", 
                            info: {
                                account: userinfo.account, 
                                password: "pwdmd5", 
                                checkinInn: userinfo.innCode
                            }
                        };

                       conn.send(JSON.stringify(joinInfo));
                    }else{  
                       alert("WebSocket 连接没有建立成功！");  
                    } 

                    _this.$store.dispatch('changeStatus', true);
                };
                conn.onmessage = function (evt) {
                    //如果获取到消息，心跳检测重置
                    //拿到任何消息都说明当前连接是正常的
                    heartCheck.reset().start();

                    let msg = JSON.parse(evt.data);
                    //console.log(msg)

                    switch(msg.type){
                        case 'join':
                            let userinfo = JSON.parse(localStorage.getItem('lavande_butler_userinfo'));

                            msg.info.userId = userinfo.id
                            msg.info.userName = userinfo.name
                            msg.info.account = userinfo.account
                            msg.info.innCode = userinfo.innCode
                            msg.info.userHead = 'static/images/yumi_logo.png'
                            //msg.userHead = userinfo.faces
                            _this.$store.dispatch('setUser', msg.info);
                            break;
                        case 'userComing':
                            //console.log(msg.user);
                            if(!msg.user.hasOwnProperty("userHead")){
                                msg.user.userHead = 'static/images/user_logo.png'
                            }
                            _this.$store.dispatch('addUser', msg.user);
                            _this.$store.dispatch('setCount',1);
                            break;
                        case 'msgFrom':
                            console.log("from:" + JSON.stringify(msg))
                            let sourceinfo = {
                                type: "msgFrom",
                                id: msg.id,
                                from: msg.from,
                                to: _this.$store.state.chat.currentUser.account,
                                info: msg.info,
                                date: msg.sendtime,
                                is_self: 0,
                                is_read: msg.is_read||false,
                                msgtype: 'text'
                            };


                            if (sourceinfo.info.endsWith(".mp3")){
                              sourceinfo.msgtype = 'voice';
                              sourceinfo.info = consts.STATIC_URL + '/msg/' + sourceinfo.info;
                            } else if (sourceinfo.info.endsWith(".png")) {
                              sourceinfo.msgtype = 'img';
                              sourceinfo.info = consts.STATIC_URL + '/msg/' + sourceinfo.info;
                            } else{
                                let tmp_content = sourceinfo.info;
                                
                                var patt=/\[0x1f[a-z0-9]{3}\]/g; // 检测utf16字符正则  
                                let tmpChar = _this.emojiChar.split("-");
                                tmp_content = tmp_content.replace(patt, function(char){  
                                    let hexchar = char.substr(1,7);
                                    if (_this.emoji.indexOf(hexchar)>=0) {
                                        return twemoji.parse(twemoji.convert.fromCodePoint(hexchar),
                                              function(icon, options, variant) {
                                                // return '/assets/' + options.size + '/' + icon + '.gif';
                                                return '/static/emoji/0x' + icon + '.png';
                                              })
                                        // return  tmpChar[_this.emoji.indexOf(hexchar)];
                                    }else{
                                        return char;
                                    }
                                    
                                });
                                sourceinfo.info = tmp_content
                            }

                            //管家挂起
                            if (_this.$store.state.chat.suspended) {

                                if (sourceinfo.hasOwnProperty("id") && sourceinfo.id != undefined && sourceinfo.id != 'undefined') {
                                    if (_this.$store.state.chat.msgids.includes(sourceinfo.id)) {
                                        return
                                    }
                                }

                                let reply_msg = {
                                    type: "msgTo",
                                    from : _this.$store.state.chat.currentUser.account,
                                    to : msg.from,
                                    info : "管家暂时离开，稍后为您服务!"
                                };

                                if (_this.replys && _this.replys.length>0) {
                                    reply_msg.info = _this.replys[_this.replys.length-1].content;
                                }

                                conn.send(JSON.stringify(reply_msg));
      
                                reply_msg.date = _this.$moment(new Date(2016,6,6)).format('YYYY-MM-DD HH:mm:ss');
                                reply_msg.is_self = 1;
                                reply_msg.msgtype = 'text';


                                _this.$store.dispatch('addMessage', sourceinfo);
                                _this.$store.dispatch('addMessage', reply_msg);
                            }

                            _this.$store.dispatch('addMessage', sourceinfo);
                            break;
                        case 'msgTo':
                            if (msg.result == 1002) {
                                _this.$store.dispatch('showNotice', ' 客人已离线，重新上线后将可收到您的消息！','error');
                            }else{
                                let sourceinfo = {
                                    type: "msgTo",
                                    id: msg.originalMsg.id,
                                    from: _this.$store.state.chat.currentUser.account,
                                    to: msg.originalMsg.to,
                                    info: msg.originalMsg.info,
                                    date: msg.originalMsg.sendtime,
                                    is_self: 1,
                                    msgtype: 'text'
                                };

                                if (sourceinfo.info.endsWith(".mp3")){
                                  sourceinfo.msgtype = 'voice';
                                  sourceinfo.info = consts.STATIC_URL + '/msg/' + sourceinfo.info;
                                } else if (sourceinfo.info.endsWith(".png")) {
                                  sourceinfo.msgtype = 'img';
                                  sourceinfo.info = consts.STATIC_URL + '/msg/' + sourceinfo.info;
                                } else{
                                    let tmp_content = sourceinfo.info;

                                    var patt=/\[0x1f[a-z0-9]{3}\]/g; // 检测utf16字符正则  
                                    let tmpChar = _this.emojiChar.split("-");
                                    tmp_content = tmp_content.replace(patt, function(char){  
                                        let hexchar = char.substr(1,7);
                                        if (_this.emoji.indexOf(hexchar)>=0) {
                                            return twemoji.parse(twemoji.convert.fromCodePoint(hexchar),
                                              function(icon, options, variant) {
                                                // return '/assets/' + options.size + '/' + icon + '.gif';
                                                return '/static/emoji/0x' + icon + '.png';
                                              })

                                            // return  tmpChar[_this.emoji.indexOf(hexchar)];
                                        }else{
                                            return char;
                                        }
                                        
                                    });
                                    sourceinfo.info = tmp_content
                                }

                                _this.$store.dispatch('addMessage', sourceinfo);}
                            break;
                        case 'disconnect':
                            _this.$store.dispatch('removeUser', msg.data.id);
                            _this.$store.dispatch('setCount', msg.data.count);
                            break;
                        case 'self_init':
                            _this.$store.dispatch('setUser', msg.data);
                            _this.$store.dispatch('setCount', msg.data.count);
                            break;
                        case 'other_init':
                            _this.$store.dispatch('addUser', msg.data);
                            break;
                        case 'message':   
                            _this.$store.dispatch('addMessage', msg.data);
                            break;
                    }
                }
            }

            function reconnect(url) {
                if(lockReconnect) return;
                lockReconnect = true;
                //没连接上会一直重连，设置延迟避免请求过多
                setTimeout(function () {
                    createWebSocket(url);
                    lockReconnect = false;
                }, 2000);
            }

            
            //心跳检测
            var heartCheck = {
                timeout: 60000,//60秒
                timeoutObj: null,
                serverTimeoutObj: null,
                reset: function(){
                    clearTimeout(this.timeoutObj);
                    clearTimeout(this.serverTimeoutObj);
                    return this;
                },
                start: function(){
                    var self = this;
                    this.timeoutObj = setTimeout(function(){
                        //这里发送一个心跳，后端收到后，返回一个心跳消息，
                        //onmessage拿到返回的心跳就说明连接正常
                        //conn.send("HeartBeat");
                        self.serverTimeoutObj = setTimeout(function(){//如果超过一定时间还没重置，说明后端主动断开了
                            conn.close();//如果onclose会执行reconnect，我们执行conn.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
                        }, self.timeout)
                    }, this.timeout)
                }
            }

            createWebSocket(wsUrl);
        },
        data () {
          return {
            emojiChar: consts.EMOJI_CHAR,
            emoji: consts.EMOJI,
            replys: this.$store.state.reply.replys,
            new_reply: {content:'', isDefault:0},
            replayModal: false,
            orderInfoColumns: [
                {
                    title: '订单编号',
                    key: 'orderNumber'
                },
                {
                    title: '房号',
                    key: 'orderRoom'
                },
                {
                    title: '订单时间',
                    key: 'orderCreatetime',
                    render:(h,params) =>{
                        let time = params.row.orderCreatetime == null ? '' : formatDate(new Date(params.row.orderCreatetime),'yyyy-MM-dd hh:mm')
                        return h('div',time)
                    }
                },
                {
                    title: '订单金额',
                    key: 'orderSumPrice'
                },
                {
                    title: '操作',
                    key: 'action',
                    width: 100,
                    render: (h, params) => {
                    return h('div', [
                        h('Button', {
                        props: {
                            type: 'primary',
                            size: 'small'
                        },
                        style: {
                            marginRight: '5px'
                        },
                        on: {
                            click: () => {
                                this.handleQuery(params.row.orderNumber)
                            }
                        }
                        }, this.tabName === 0 ? '详情/接单' : this.tabName === 1 ? '详情/送达' : '详情')
                    ])
                    }
                }
            ],
            orderInfoData:[],
            pageCount:0,
            pageDate:{
                pageIdx:0,
                pageSize:10
            },
            initPage:1, 
            tabName:0,
            orderDetail:[
                {
                    title: '类型',
                    key: 'categoryName'
                },
                {
                    title: '商品',
                    key: 'commodityName',
                    render:(h,params) =>{
                        return h('Tooltip',{
                            props:{
                            placement:'top'
                            }
                        },[
                            h('a', params.row.commodityName),[
                            h('div',{
                                slot:'content'
                            },[
                                h('img',{
                                attrs:{
                                    src: params.row.commodityImg 
                                },
                                style:{
                                    width: '120px',
                                    height: '120px'
                                }
                                },''),
                            ])
                            ]
                        ])
                    }
                },
                {
                    title: '数量',
                    key: 'commodityAmount'
                },
                {
                    title: '单价',
                    key: 'commodityPrice'
                },
                {
                    title: '总价',
                    key: 'commoditySumPrice'
                }
                
                ],
                orderDatailData:[],
                orderInfoShow:false,
                orderInfo:{},
                LoadingVal:true,
                orderListShow:false,
                awaitListData:0,
                conveyData:0,
                arriveData:0,
                load:false,
                orderNo:"",
                sales:false,
                salesRemark:"",
                salesOrderNo:"",
                commodityListView:false,
                loadingCommodity:false,
                CommodityColumns: [
                    {
                        title: '类型',
                        key: 'categoryName'
                    },
                    {
                        title: '商品名称',
                        key: 'commodityName',
                        render:(h,params) =>{
                        return h('Tooltip',{
                            props:{
                            placement:'top'
                            }
                        },[
                            h('a', params.row.commodityName),[
                            h('div',{
                                slot:'content'
                            },[
                                h('img',{
                                attrs:{
                                    src: params.row.commodityImg 
                                },
                                style:{
                                    width: '120px',
                                    height: '120px'
                                }
                                },''),
                            ])
                            ]
                        ])
                        }
                    },
                    {
                        title: '商品概要',
                        key: 'commoditySynopsis'
                    },
                    {
                        title: '商品售价',
                        key: 'commodityPrice'
                    },
                    {
                        title: '库存',
                        key: 'commodityInventory',
                    }
                ],
                CommodityData:[],
                CommodityPageCount:0,
                CommodityInitPage:1,
                CommodityPageDate:{
                    pageIdx:0,
                    pageSize:10
                },
          }
        },
        filters:{
            formatTime(time){
                if (time == null || time == '') return ''; 
                let date = new Date(time);
                return formatDate(date,'yyyy-MM-dd hh:mm');
                //此处formatDate是一个函数，将其封装在common/js/date.js里面，便于全局使用
            },
            format(time){
                let date = new Date(time);
                return formatDate(date,'yyyy-MM-dd');
                //此处formatDate是一个函数，将其封装在common/js/date.js里面，便于全局使用
            }
        },
        methods:{
            get () {
                this.$store.dispatch('getReplys', {})
            },
            onShowChangePwd: function (event){
                //console.log("event");

                this.$modal.show('password-modal', { show: true })
            },
            onShowImgModal: function(url){
                //console.log("url:" + url);

                this.$modal.show('view-image-modal', { show: true , resourceurl: url})
            },
            onChangePwd: function(data){
                //console.log(data);
                var _this = this;

                let userinfo = JSON.parse(localStorage.getItem('lavande_butler_userinfo'));
                let token = JSON.parse(localStorage.getItem('lavande_butler_token'));
                var requestParam = {newPassword:data};
                requestParam.account = userinfo.account;

                new PwdModel()
                    .addHeaders({"Authorization": "Bearer " + token})
                    .POST({
                      params: requestParam
                    })
                    .then((response) => {
                        if(response.data.success){
                            _this.$modal.hide('password-modal');
                            _this.$Message.success("修改密码成功!");

                        }else{
                            _this.$Message.error('修改密码失败!');
                        }
                    })
            },
            onSelectService: function(service){
                this.$refs.txt.addService(service);
            },
            replyChoose: function (reply){
                this.$refs.txt.addService(reply.content);
            },
            addReply: function (){
                this.$modal.show('reply-modal', { show: true })
            },
            saveReply: function (data){
                let _this = this

                new Reply()
                    .POST({
                      data: data
                    })
                    .then((response) => {
                        if(response.data.success){
                            _this.$modal.hide('reply-modal');
                            _this.$Message.success("添加成功!");
                            _this.get();

                        }else{
                            _this.$Message.error('修改密码失败!');
                        }
                    })
            },
            delReply: function (reply){
                let _this = this
                this.$Modal.confirm({
                    title: '提示',
                    content: '确认删除此自定义回复？',
                    onOk: () => {
                        _this.$store.dispatch('deleteReply', {
                              params: {
                                id: reply.id
                              }
                            }).then(() => {
                              this.$Message.success('删除成功！')
                              _this.get()
                            })
                    },
                    onCancel: () => {
                            
                        }
                    });
            },
            tabClick(e){
                this.tabName = e
                this.initPage = 1
                this.pageDate.pageIdx = 0 
                this.orderInfoData = []
                this.orderListShow = true
                this.load = true
                this.orderNo = ""
                this.queryStatus()
            },
            changePage(e){
                let _this = this
                _this.initPage = e;
                _this.pageDate.pageIdx = e
                if(this.orderNo == null || this.orderNo == ""){
                    this.queryStatus()
                }else{
                    this.queryOrderNo()
                }
                
            },
            queryStatus(){
                this.queryCount();
                let status = this.tabName
                let userinfo = JSON.parse(localStorage.getItem('lavande_butler_userinfo'));
                let token = JSON.parse(localStorage.getItem('lavande_butler_token'));
                var requestParam = {status:status};
                requestParam.account = userinfo.account;
                requestParam.pageIdx = this.pageDate.pageIdx;
                requestParam.pageSize = this.pageDate.pageSize;
                new order().addPath('orderStatus').GET({params: requestParam}).then((res) =>{
                    if(res.data.success == true){
                        this.pageCount = res.data.data.totalPages
                        this.orderInfoData = res.data.data.content
                        this.load = false
                    }else{
                        this.$Message.error('无法获取数据')
                    }
                })
            },
            queryCount(){
                let userinfo = JSON.parse(localStorage.getItem('lavande_butler_userinfo'));
                var requestParam = {account:userinfo.innCode};
                new order().addPath('countOrder').GET({params: requestParam}).then((res) =>{
                    if(res.data.success == true){
                        let dataList = res.data.data
                        this.awaitListData = dataList[0] != null && dataList[0] != "" ? dataList[0] : 0
                        this.conveyData = dataList[1] != null && dataList[1] != "" ? dataList[1] : 0
                        this.arriveData = dataList[2] != null && dataList[2] != "" ? dataList[2] : 0
                    }else{
                        this.$Message.error('无法获取数据')
                    }
                })
            },
            handleQuery(val){
                this.orderInfoShow = true
                this.orderInfo = []
                this.orderDatailData = []
                // this.orderListShow = false
                new order().addPath(val).GET().then((res) =>{
                    if(res.data.success == true){
                        let info = res.data.data
                        this.orderInfo = info
                        this.orderDatailData = info.sOrderDetails
                        this.LoadingVal = false
                    }else{
                        this.$Message.error('查看详情失败，'+res.data.data)
                        this.orderInfoShow = false
                    }
                })
            },
            orderTaking(val){
                let params = {
                    orderNo:val,
                    status:this.tabName
                }
                // params = JSON.stringify(params)
                new order().addPath('OrderStatus').PUT({params}).then((res) =>{
                    if(res.data.success == true){
                        this.$Message.info('操作成功')
                        this.orderInfoShow = false
                        this.queryStatus()
                        this.queryCount()
                    }else{
                        this.$Message.error('操作失败，'+res.data.data)
                        
                    }
                })
            },
            queryOrderNoSbm(){
                this.pageCount=0
                this.pageDate={
                    pageIdx:0,
                    pageSize:10
                }
                this.initPage=1
                this.queryOrderNo()
            },
            queryOrderNo(){
                this.load = true
                let status = this.tabName
                let orderNo = this.orderNo
                var requestParam = {status:status};
                requestParam.orderNo = orderNo;
                requestParam.pageIdx = this.pageDate.pageIdx;
                requestParam.pageSize = this.pageDate.pageSize;
                new order().addPath('queryOrderNo').GET({params: requestParam}).then((res) =>{
                    if(res.data.success == true){
                        this.pageCount = res.data.data.totalPages
                        this.orderInfoData = res.data.data.content
                        this.load = false
                    }else{
                        this.$Message.error('无法获取数据')
                    }
                })
            },
            refurbish(){
                this.orderNo = ""
                this.pageCount=0
                this.pageDate={
                    pageIdx:0,
                    pageSize:10
                }
                this.initPage=1
                this.queryStatus()
            },
            salesClick(e){
                this.salesOrderNo = e
                this.sales = true
            },
            salesConfirm(){
                let orderNo = this.salesOrderNo
                let salesRemark = this.salesRemark
                if(salesRemark == null || salesRemark == ""){
                    this.$Message.error("请输入退单备注")
                    return
                }
                let params = {
                    orderNo:orderNo,
                    salesRemark:salesRemark
                }
                // params = JSON.stringify(params)
                new order().addPath('salesOrder').PUT({params}).then((res) =>{
                    if(res.data.success == true){
                        this.$Message.info('退单成功')
                        this.sales = false
                        this.orderInfoShow = false
                        this.queryStatus()
                        this.queryCount()
                    }else{
                        this.$Message.error('退单失败，'+res.data.data)
                        
                    }
                })
            },
            clickCommodity(){
                let _this = this
                _this.commodityListView = true
                _this.CommodityInitPage = 1;
                _this.CommodityPageDate.pageIdx = 0
                _this.queryCommodity()
            },
            queryCommodity(){
                this.loadingCommodity = true
                let userinfo = JSON.parse(localStorage.getItem('lavande_butler_userinfo'));
                let hotelID = userinfo.innCode
                let keyword = ""
                let pageIdx = this.CommodityPageDate.pageIdx
                let pageSize = this.CommodityPageDate.pageSize
                let params = {
                    hotelID:hotelID,
                    keyword:keyword,
                    pageIdx:pageIdx,
                    pageSize:pageSize
                }
                new HotelCommodity().addPath('queryHotelAndCommodityName').GET({params}).then((res) =>{
                    if(res.data.success == true){
                        this.CommodityData = res.data.data
                        this.CommodityPageCount = res.data.data[0].totalCount
                        this.hotelShow = true
                        this.loadingCommodity = false
                    }else{
                        this.$Message.error('获取门店商品列表失败！')
                        this.loadingCommodity = false
                    }
                })
            },
            CommodityPage(e){//翻页
                let _this = this
                _this.CommodityInitPage = e;
                _this.CommodityPageDate.pageIdx = (e-1)*10
                _this.queryCommodity()
            },
        },
        mounted(){
            this.queryCount()
            
        },
    }
</script>

<style lang="less">
    .chat{
        width: 950px;
        height: 600px;
        position: absolute;
        top: 50%;
        left: 50%;
        margin-left: -400px;
        margin-top: -300px;

        display: flex;
        flex-wrap: nowrap;
        flex-direction: row;
        border-radius: 3px;
        
        .slide-bar,.main{
          height:100%;
        }

        .slide-bar{
          width: 280px;
          background:#2e3238;
          color: #f4f4f4;
        }
        .main{
          width: 600px;
          background: #eee;
          display: flex;
          flex-direction: column;
        }
        .right-bar{
          width: 200px;
          background: transparent;
          color: #f4f4f4;
          padding-left: 20px;

        
          .service-container {
              ul {  
                width: 200px;  
              }  

              li {  
                width: 80px;
                float: left; 
                padding: 10px;
                display: block;  
              } 
          } 

          .service {
            width: 50px; 
            height: 50px;
            border-radius: 4px; 
            margin-top: 10px
          }

          .reply{
            width: 190px;
            height: 180px;
            overflow-y: auto;

            ul {  
                width: 170px;  
                list-style: none;
            }  

            li {  
                height: 24px;
                width: 170px;
                text-align: left;
            }
        }
        }
    }
</style>