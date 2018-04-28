const lavandeAPI = require('../../api/LavandeAPI.js')
const Request = require('../../utils/RequestUtil.js')
const tools = require('../../utils/tools.js')

Page({
  data: {
    isShow: false,//控制emoji表情是否显示
    isLoad: true,//解决初试加载时emoji动画执行一次
    cfBg: false,
    emojiChar: "😊-😋-😌-😍-😏-😚-😛-😝-😞-😟-😪-😬-😮-😺-😻-😼-😽-😾-😿-🙊-🙋-🙏-🚊-🚋-🚌-🍄-🍅-🍆-🍇-sendMsg🍈-🍉-🍑-🍒-🍓-🐔-🐕-🐖-👦-👧-👨-👩-👰-👱-👲-👳-💃-💄-💅-💆-💇-💐-💑-💓-💘-😂-😃-😅-😆-😈-😒-😓-😔-😕-😖-😘-😙-😠-😡-😣-😤-😥-😧-😩-😳-😵-😷-🚁",
    emoji: [
      "0x1f60a", "0x1f60b", "0x1f60c", "0x1f60d", "0x1f60f", "0x1f61a", "0x1f61b", "0x1f61d", "0x1f61e", "0x1f61f",
      "0x1f62a", "0x1f62c", "0x1f62e", "0x1f63a", "0x1f63b", "0x1f63c", "0x1f63d", "0x1f63e", "0x1f63f", "0x1f64a",
      "0x1f64b", "0x1f64f", "0x1f68a", "0x1f68b", "0x1f68c", "0x1f344", "0x1f345", "0x1f346", "0x1f347", "0x1f348",
      "0x1f349", "0x1f351", "0x1f352", "0x1f353", "0x1f414", "0x1f415", "0x1f416", "0x1f466", "0x1f467", "0x1f468",
      "0x1f469", "0x1f470", "0x1f471", "0x1f472", "0x1f473", "0x1f483", "0x1f484", "0x1f485", "0x1f486", "0x1f487",
      "0x1f490", "0x1f491", "0x1f493", "0x1f498", "0x1f602", "0x1f603", "0x1f605", "0x1f606", "0x1f608", "0x1f612",
      "0x1f613", "0x1f614", "0x1f615", "0x1f616", "0x1f618", "0x1f619", "0x1f620", "0x1f621", "0x1f623", "0x1f624",
      "0x1f625", "0x1f627", "0x1f629", "0x1f633", "0x1f635", "0x1f637", "0x1f681"
    ],
    emojis: [],//qq、微信原始表情

    placeholderText: "连接服务器中...",
    web_base_url: Request.BASE_URL,
    websocket_url: Request.WEBSOCKET_URL,
    notices: [
    ],
    voiceFiles: {},
    messageArray: [],
    msgids: [],
    selected: -1,
    socketOpen: false,
    inputValue: "",
    isVoiceType: false,
    voicing: false,
    writing: false,
    scrollTop: 120,
    height: 0,
    width: 0,
    trHeight: 40,
    animation: {},
    selfAvatarUrl: "../../images/yumi-logo.png",
    nickName: "",
    checkinInn: "222110",
    checkinRoom: "8304",
    customer: {},
    butlerList: [],
    butlerInfo: { account: 'zengqiuyan', userName: "加载中", age: 22, nature: "活泼、开朗", specialty: "美食家（吃货）", favoriteQty: 510, favorited: false, bulterHead: "http://aploproducts.oss-cn-shenzhen.aliyuncs.com/xiaoer.png" },
    innInfo: { innName: '寓米公寓|琶洲店' },
    carArray: [{ "price": "6.40", "num": 1, "name": "夹心饼干", "subPrice": "6.40", "index": 3, "mark": "a3" }],
    categories: [{ id: "TAP1", name: "小二" }],
    activeCategoryId: "TAP1",
    comment: false,
    award: false,
    subtractBtn: true,
    stars: 5,
    commentTag: [],
    textValue: '',
    scenesInit: [],
    WiFiPassword: '',
    WiFiName: '',
    starComment: [],
    starVal: "5.0",
    imgUrl: '',
    chgIndex: false
  },
  checkSettingStatu: function (cb) {
    var that = this;
    // 判断是否是第一次授权，非第一次授权且授权失败则进行提醒
    wx.getSetting({
      success: function success(res) {
        var authSetting = res.authSetting;
        if (tools.isEmptyObject(authSetting)) {
          console.log('首次授权');
        } else {
          console.log('不是第一次授权', authSetting);
          // 没有授权的提醒
          if (authSetting['scope.userInfo'] === false || authSetting['scope.userLocation'] === false) {
            wx.showModal({
              title: '用户未授权',
              content: '如需正常使用，请按确定并在授权管理中选中相关权限，然后点按确定。最后再重新进入小程序即可正常使用。',
              showCancel: false,
              success: function (res) {
                if (res.confirm) {
                  console.log('用户点击确定')
                  wx.openSetting({
                    success: function success(res) {
                      console.log('openSetting success', res.authSetting);
                    }
                  });
                }
              }
            })
          }
        }
      }
    });
  },
  onReady: function () {
    this.animation = wx.createAnimation();
    this.checkSettingStatu();
  },
  onLoad: function (options) {
    // wx.closeSocket()
    wx.showLoading({
      title: '小二赶来中...',
    })
    var em = {}, that = this, emChar = that.data.emojiChar.split("-");
    var emojis = []
    that.data.emoji.forEach(function (v, i) {
      em = {
        char: emChar[i],
        emoji: v
      };
      emojis.push(em)
    });

    that.setData({
      emojis: emojis
    });

    var scenes = decodeURIComponent(options.scene).split("_");
    console.log('scenes', scenes)
    console.log('scenes',scenes.length)
    console.log('openid', getApp().globalData.openid)
    if (scenes.length >= 2) {
      that.setData({
        chgIndex: true,
        placeholderText: "正在连接服务器...",
        checkinInn: scenes[0],
        checkinRoom: scenes[1],
        scenesInit: scenes
      });
      if (getApp().globalData.openid == '') {
        getApp().getUserInfo(function (openid) {
          //console.log("openid => " + openid);
          that.initWebSocket();
        });
      } else {
        that.initWebSocket();
      }
    } else if (options.scene && scenes.length == 1){
      console.log('scenes', scenes)
      //华美国际店
      // wx.reLaunch({
      //   url: '../roomChange/roomChange',
      // })
      wx.redirectTo({
        url: '../roomChange/roomChange',
      })
    } else {
      that.setData({ chgIndex: true,})
      wx.getLocation({
        type: 'gcj02',
        success: function (res) {
          //var latitude = res.latitude
          //var longitude = res.longitude
          //console.log(latitude + "," + longitude)
          lavandeAPI.getNearestInn({ longitude: res.longitude, latitude: res.latitude }, function (innRes) {
            console.log('innRes', innRes)
            if (innRes.success && innRes.data.length > 0) {
              that.setData({
                placeholderText: "正在连接服务器...",
                checkinInn: innRes.data[0].code,
                checkinRoom: "",
              });

              if (getApp().globalData.openid == '') {
                getApp().getUserInfo(function (openid) {
                  //console.log("openid => " + openid);
                  that.initWebSocket();
                });
              }else{
                that.initWebSocket();
              }
            }
          })
        },
        fail: function () {
          console.log('定位失败')
          wx.showModal({
            title: '提示',
            content: '无法获取门店信息，请打开定位！',
          })
          if (getApp().globalData.openid == '') {
            getApp().getUserInfo(function (openid) {
              //console.log("openid => " + openid);
              that.initWebSocket();
            });
          }
        }
      })
    }

    var categories = [{ id: "TAP1", name: "小二" }];
    categories.push({ name: "严选", id: "TAP2" });
    // categories.push({ name: "Trips", id: "TAP3" });
    that.getWiFi(scenes[0], scenes[1])

    that.setData({
      categories: categories,
      activeCategoryId: "TAP1"
    });
    wx.hideLoading()
  },
  onUnload: function () {
    // wx.closeSocket();
  },

  onShow: function () {
    
    var self = this;
    self.setData({
      activeCategoryId: "TAP1"
    })
    console.log("onShow onShow!!!")
    if (wx.getStorageSync('OrderNo')) {
      var msg = { "type": "msgTo" };
      msg.to = self.data.customer.account;//.checkinInn;
      msg.checkinInn = self.data.checkinInn;
      msg.info = "你有新的订单:" + wx.getStorageSync('OrderNo');
      msg.from = getApp().globalData.openid;
      self.sendSocketMessage(JSON.stringify(msg));
      self.activeCategoryId = "TAP1",
        self.setData({
          inputValue: "",
          writing: false,
          isShow: false,

        });
    }
    
  },

  initWebSocket: function () {
    var self = this;
    wx.showLoading({
      title: '小二赶来中...',
    })
    var reqParam = {};
    reqParam.inncode = self.data.checkinInn;
    reqParam.openid = getApp().globalData.openid;

    lavandeAPI.getButlerInfo(reqParam, function (respon) {
      if (respon.success) {
        respon.data.butler.age = Math.ceil((Date.now() - Date.parse(respon.data.butler.birthDay)) / 31536000000)
        let tempNotices = respon.data.notices;
        for (var i = 0; i < tempNotices.length; i++) {
          tempNotices[i].url = self.data.web_base_url + '/resource/notice/' + tempNotices[i].url
        }
        let images = [{ url: 'http://aploproducts.oss-cn-shenzhen.aliyuncs.com/bg-auth1.jpg' },
        { url: 'http://aploproducts.oss-cn-shenzhen.aliyuncs.com/main_7.png' },
        { url: 'http://aploproducts.oss-cn-shenzhen.aliyuncs.com/main_8.png' },]
        self.setData({
          selfAvatarUrl: getApp().globalData.userInfo.avatarUrl,
          nickName: getApp().globalData.userInfo.nickName,
          openId: getApp().globalData.openid,
          butlerList: respon.data.butler,
          innInfo: respon.data.inn,
          notices: images
        });

        if (respon.data.butler.length > 0) {
          var binfo = respon.data.butler[0];
          //binfo.favoriteQty = binfo.favoriteQty < 200 ? Math.ceil((Math.random() * (30 - 20) + 20) * 10) : binfo.favoriteQty;
          binfo.favorited = (binfo.favorited == 0)
          binfo.age = Math.ceil((Date.now() - new Date(binfo.birthDay)) / 1000 / 60 / 60 / 24 / 365)
          binfo.bulterHead = binfo.faces
          if (binfo.bulterHead == null || binfo.bulterHead == '') {
            binfo.bulterHead = 'http://aploproducts.oss-cn-shenzhen.aliyuncs.com/xiaoer.png'
          } else {
            binfo.bulterHead = self.data.web_base_url + '/resource/userHead/' + binfo.faces + "?" + Math.random() / 9999;
          }
          //binfo.innCode + '/' + binfo.account + '.png'
          self.setData({
            butlerInfo: binfo,
            imgUrl: binfo.bulterHead
          });

          lavandeAPI.getCommetCount({ innCode: self.data.scenesInit[0], name: binfo.userName }, function (respon) {
            if (respon.success == true) {
              self.setData({
                starVal: respon.data,
              })
            }
          })
          //console.log("将要连接服务器。");
          if (!self.data.SocketTask){
            var SocketTask = wx.connectSocket({
              url: self.data.websocket_url,
              complete:function(res){
                console.log('将要连接服务器。',res)
              }
            });
            self.setData({ SocketTask})
          

            SocketTask.onOpen(function(res){
              console.log('SocketTask',res)
              self.setData({
                placeholderText: "连接服务器成功。",
                socketOpen: true,
                selfAvatarUrl: getApp().globalData.userInfo.avatarUrl,
                nickName: getApp().globalData.userInfo.nickName,
                openId: getApp().globalData.openid
              });
            });

            SocketTask.onClose(function (res) {
              console.log("socket close ");

              self.initWebSocket();
            });

            SocketTask.onMessage(function (res) {
              console.log('收到服务器内容：' + res.data);
              var data = JSON.parse(res.data);
              if (data.type == 'create' && data.result == 1006) {
                var msg = { "type": "join", "info": {} };
                msg.info.checkinInn = self.data.checkinInn;
                msg.info.checkinRoom = self.data.checkinRoom;
                msg.info.userId = getApp().globalData.openid;
                msg.info.userHead = getApp().globalData.userInfo.avatarUrl;
                msg.info.nickName = getApp().globalData.userInfo.nickName;

                self.sendSocketMessage(JSON.stringify(msg));
                self.setData({
                  placeholderText: "请输入信息"
                });
                return;
              }

              if (data.type == 'join' && data.result == 1014) {
                var newMessage = {
                  type: 'other',
                  name: '系统',
                  time: '',
                  message: data.note,
                  msgtype: 'text',
                  carArray: []
                };

                if (newMessage.message.endsWith(".mp3")) {
                  newMessage.msgtype = 'voice';
                } else if (newMessage.message.endsWith(".png")) {
                  newMessage.msgtype = 'img';
                }

                var newArray = self.data.messageArray.concat(newMessage);
                self.setData({
                  messageArray: newArray
                });

                return;
              }

              if (data.hasOwnProperty('customer')) {
                //获取门店管家资料
                //inncode=222048&roomno=2201&account=zhanglou&openid=23423dfsgdsa23
                self.setData({
                  customer: data.customer
                });

                for (var i = 0; i < self.data.butlerList.length; i++) {
                  if (self.data.butlerList[i].account == data.customer.account) {
                    var binfo = self.data.butlerList[i];
                    //binfo.favoriteQty = binfo.favoriteQty < 200 ? Math.ceil((Math.random() * (30 - 20) + 20) * 10) : binfo.favoriteQty;
                    binfo.favorited = (binfo.favorited == 0)
                    binfo.age = Math.ceil((Date.now() - new Date(binfo.birthDay)) / 1000 / 60 / 60 / 24 / 365)
                    // binfo.bulterHead = binfo.faces
                    if (binfo.bulterHead == null || binfo.bulterHead == '') {
                      binfo.bulterHead = 'http://aploproducts.oss-cn-shenzhen.aliyuncs.com/xiaoer.png'
                    } else {
                      binfo.bulterHead = self.data.web_base_url + '/resource/userHead/' + binfo.faces + "?" + Math.random() / 9999;
                    }//binfo.innCode + '/' + binfo.account + '.png';
                    self.setData({
                      butlerInfo: binfo,
                      imgUrl: binfo.faces
                    });
                    break;
                  }
                }

                // wx.showToast({title: '当前客服已上线',icon: 'success',duration: 2000})
                var newMessage = {
                  type: 'other',
                  name: '系统',
                  time: '',
                  message: '您的管家已上线，请问有什么可以帮您？',
                  msgtype: 'text',
                  playing: false,
                  carArray: []
                };

                if (newMessage.message.endsWith(".mp3")) {
                  newMessage.msgtype = 'voice';
                } else if (newMessage.message.endsWith(".png")) {
                  newMessage.msgtype = 'img';
                }

                var newArray = self.data.messageArray.concat(newMessage);
                self.setData({
                  messageArray: newArray,
                });

                return;
              }

              if (data.type == 'msgTo') {
                var newMessage = {
                  type: 'other',
                  name: 'system',
                  userHead: self.data.selfAvatarUrl,
                  msgtype: 'text',
                  playing: false,
                  carArray: []
                };

                if (data.result == 1006) {
                  newMessage.type = 'self';
                  newMessage.message = data.originalMsg.info;
                  newMessage.time = data.originalMsg.sendtime;
                  newMessage.id = data.originalMsg.id || '';
                } else if (data.result == 1018) {
                  newMessage.message = '您的管家还未上线，您的消息管家上线后将立即为您处理。';
                } else {
                  newMessage.message = '您的管家还未上线，稍候为您服务。';
                }

                if ((data.result == 1006 && undefined != newMessage.id && !self.data.msgids.includes(newMessage.id)) || data.result != 1006) {
                  if (newMessage.message.endsWith(".mp3")) {
                    newMessage.msgtype = 'voice';
                  } else if (newMessage.message.endsWith(".png")) {
                    newMessage.msgtype = 'img';
                    newMessage.message = self.data.web_base_url + '/resource/msg/' + newMessage.message;
                  } else if (data.originalMsg && data.originalMsg.info.startsWith("orderNo:") && wx.getStorageSync('ShopDetail')) {
                    newMessage.msgtype = 'shop';
                    // debugger
                    // newMessage.carArray = JSON.parse(wx.getStorageSync('ShopDetail'));
                  } else {
                    let tmp_content = newMessage.message;

                    var patt = /\[0x1f[a-z0-9]{3}\]/g; // 检测utf16字符正则  
                    let tmpChar = self.data.emojiChar.split("-");
                    tmp_content = tmp_content.replace(patt, function (char) {
                      let hexchar = char.substr(1, 7);
                      if (self.data.emoji.indexOf(hexchar) >= 0) {
                        return tmpChar[self.data.emoji.indexOf(hexchar)];
                      } else {
                        return char;
                      }
                    });
                    newMessage.message = tmp_content;
                  }

                  var newArray = self.data.messageArray.concat(newMessage);
                  self.setData({
                    messageArray: newArray,
                    scrollTop: self.data.scrollTop + (self.data.messageArray.length > 2 ? 50 : 0)
                  });
                }

                return;
              }

              if (data.type == 'msgFrom') {
                var newMessage = {
                  type: 'other',
                  id: data.id,
                  name: data.fromName,
                  time: data.sendtime,
                  message: data.info,
                  msgtype: 'text',
                  playing: false
                };

                if (undefined != newMessage.id && !self.data.msgids.includes(newMessage.id)) {
                  if (newMessage.message.endsWith(".mp3")) {
                    newMessage.msgtype = 'voice';
                  } else if (newMessage.message.endsWith(".png")) {
                    newMessage.msgtype = 'img';
                    newMessage.message = self.data.web_base_url + '/resource/msg/' + newMessage.message;
                  } else {
                    let tmp_content = newMessage.message;

                    var patt = /\[0x1f[a-z0-9]{3}\]/g; // 检测utf16字符正则  
                    let tmpChar = self.data.emojiChar.split("-");
                    tmp_content = tmp_content.replace(patt, function (char) {
                      let hexchar = char.substr(1, 7);
                      if (self.data.emoji.indexOf(hexchar) >= 0) {
                        return tmpChar[self.data.emoji.indexOf(hexchar)];
                      } else {
                        return char;
                      }
                    });
                    newMessage.message = tmp_content;
                  }

                  var newArray = self.data.messageArray.concat(newMessage);
                  self.setData({
                    messageArray: newArray,
                    scrollTop: self.data.scrollTop + (self.data.messageArray.length > 2 ? 50 : 0)
                  });

                }

                return;
              }
            });
          }

        } else {
          wx.showToast({
            title: '您的管家还未上线，稍候为您服务!',
          })
        }
      }
    })

    wx.getSystemInfo({
      success: (res) => {
        this.setData({
          height: res.windowHeight / 3,
          width: res.windowWidth / 2
        })
      }
    });
    wx.hideLoading()
  },

  sendMsg: function () {
    if (this.data.inputValue != "") {
      var msg = { "type": "msgTo" };
      msg.to = this.data.customer.account;//.checkinInn;
      msg.checkinInn = this.data.checkinInn;
      msg.info = this.utf16toEntities(this.data.inputValue);
      msg.from = getApp().globalData.openid;
      this.sendSocketMessage(JSON.stringify(msg));

      this.setData({
        inputValue: "",
        writing: false,
        isShow: false
      });
    }
  },

  utf16toEntities: function (str) {
    var patt = /[\ud800-\udbff][\udc00-\udfff]/g; // 检测utf16字符正则  
    let entstr = str.replace(patt, function (char) {
      if (char.length === 2) {
        return "[0x" + char.codePointAt(0).toString(16) + "]";
      } else {
        return char;
      }
    });
    return entstr;
  },

  playVoice: function (e) {
    if (e.currentTarget.dataset.msg.endsWith(".mp3")) {
      let _this = this;
      let idx = e.currentTarget.dataset.idx;
      let key = e.currentTarget.dataset.msg.slice(0, e.currentTarget.dataset.msg.lastIndexOf(".mp3"));
      if (_this.data.voiceFiles.hasOwnProperty(key)) {

        var newArray = _this.data.messageArray;
        newArray[idx].playing = !newArray[idx].playing;
        _this.setData({
          messageArray: newArray,
          selected: newArray[idx].playing ? idx : -1,
        });


        wx.playVoice({
          filePath: _this.data.voiceFiles[key],
          complete: function () {
            //console.log('play end');
            var newArray = _this.data.messageArray;
            newArray[e.currentTarget.dataset.idx].playing = false;

            _this.setData({
              messageArray: newArray,
              selected: -1,
            });
          }
        })
      }
    } else if (e.currentTarget.dataset.msg.endsWith(".png")) {
      let imgList = [];
      imgList.push(e.currentTarget.dataset.msg.replace("YS.png", ".png"));

      wx.previewImage({
        current: e.currentTarget.dataset.msg, // 当前显示图片的http链接  
        urls: imgList // 需要预览的图片http链接列表  
      })
    }
  },

  sendSocketMessage: function (msg) {
    if (this.data.socketOpen) {
      // wx.sendSocketMessage({
      //   data: msg
      // });
      this.data.SocketTask.send({
        data: msg
      })
    }
  },

  toggleMsg: function () {
    this.setData({
      isVoiceType: !this.data.isVoiceType
    })
  },

  mytouchstart: function (e) {
    var _this = this;
    //console.log(e.timeStamp + '- touch-start')

    this.setData({
      voicing: true
    })
    //console.log("开始录音")
    wx.startRecord({
      success: function (res) {
        //console.log("录音成功")
        var tempFilePath = res.tempFilePath
        //console.log('voice:' + tempFilePath)

        wx.uploadFile({
          url: Request.WEB_URL + '/file/uploadFile', //仅为示例，非真实的接口地址
          filePath: tempFilePath,
          name: 'uploadingFile',
          formData: {
            'fileType': 'msg'
          },
          success: function (res) {
            //{statusCode: 200, data: "{"success":true,"errMsg":"","data":"171015073338443205982.silk"}", errMsg: "uploadFile:ok"}
            if (res.statusCode == 200) {
              if (JSON.parse(res.data).success) {
                // console.log(JSON.parse(res.data).data);
                var fileName = JSON.parse(res.data).data;
                var filekey = fileName.replace(".mp3", "");

                _this.setData({
                  inputValue: fileName
                });

                _this.sendMsg();

                //保存
                wx.saveFile({
                  tempFilePath: tempFilePath,
                  success: function (res) {
                    let voiceFiles = _this.data.voiceFiles;
                    voiceFiles[filekey] = res.savedFilePath;

                    _this.setData({
                      voiceFiles: voiceFiles
                    })
                  }
                })
              } else {
                //console.log(JSON.parse(res.data).errMsg);
              }
            }
            //do something
          }
        })

      },
      complete: function (res) {
        //console.log("complete" + res)
      },
      fail: function (res) {
        //录音失败
        //console.log("fail" + res)
      }
    })
  },
  //按下事件结束  
  mytouchend: function (e) {
    //console.log(e.timeStamp + '- touch-end')

    if (this.data.voicing) {
      wx.stopRecord()
      this.setData({
        voicing: false
      })
      return
    }
  },

  chooseImg() {
    var _this = this;
    wx.chooseImage({
      count: 1, // 默认9
      sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success: function (res) {
        // 返回选定照片的本地文件路径列表，tempFilePath可以作为img标签的src属性显示图片
        var tempFilePaths = res.tempFilePaths;
        wx.uploadFile({
          url: Request.WEB_URL + '/file/uploadFile', //仅为示例，非真实的接口地址
          filePath: tempFilePaths[0],
          name: 'uploadingFile',
          formData: {
            'fileType': 'msg'
          },
          success: function (res) {
            //{ "success":true, "errMsg":"", "data":"171025074604651805451.png" }
            var data = res.data
            if (JSON.parse(res.data).success) {
              _this.setData({
                inputValue: JSON.parse(res.data).data
              });

              _this.sendMsg();
            }
          }
        })
      }
    })
  },

  bindStar: function (e) {
    var reqParam = {};
    let id = e.currentTarget.id;
    let self = this;
    if (id == 'comment') {
      this.setData({
        comment: !self.data.comment
      });
    } else if (id == 'award') {
      self.award = true
    }
    // reqParam.innCode = self.data.checkinInn;
    // reqParam.openid = getApp().globalData.openid;
    // reqParam.account = self.data.butlerInfo.account;
    // reqParam.flag = self.data.butlerInfo.favorited ? 0 : 1;

    // lavandeAPI.postStar(JSON.stringify(reqParam), function (respon) {
    //   if (respon.success) {
    //     var binfo = self.data.butlerInfo;
    //     binfo.favoriteQty = binfo.favorited ? (binfo.favoriteQty - 1) : (binfo.favoriteQty + 1),
    //       binfo.favorited = binfo.favorited ? 0 : 1

    //     self.setData({
    //       butlerInfo: binfo
    //     });
    //   }
    // })
  },


  //解决滑动穿透问题
  emojiScroll: function (e) {
    console.log(e)
  },
  // textAreaInput: function (e) {
  //   if (this.data.inputValue.length > e.detail.value.length && this.data.inputValue.charAt(this.data.inputValue.length - 1) == "]") {
  //     this.setData({
  //       inputValue: this.data.inputValue.substr(0, this.data.inputValue.lastIndexOf("["))
  //     })
  //   } else {
  //     this.setData({
  //       inputValue: e.detail.value
  //     })
  //   }
  // },
  //文本域失去焦点时 事件处理
  textAreaBlur: function (e) {
    //获取此时文本域值
    this.setData({
      inputValue: e.detail.value,
      writing: e.detail.value.length > 0
    })

  },
  //文本域获得焦点事件处理
  textAreaFocus: function () {
    let that = this;
    this.setData({
      isShow: false,
      cfBg: false
    })

    setTimeout(function () {
      that.setData({ writing: true })
    }, 500)

  },
  //点击表情显示隐藏表情盒子
  emojiShowHide: function () {
    this.setData({
      isShow: !this.data.isShow,
      isLoad: false,
      cfBg: !this.data.false
    })
  },
  //表情选择
  emojiChoose: function (e) {
    //当前输入内容和表情合并"[" + e.currentTarget.dataset.oxf + "]"
    this.setData({
      inputValue: this.data.inputValue + e.currentTarget.dataset.emoji,
      writing: true
    })
  },
  //点击emoji背景遮罩隐藏emoji盒子
  cemojiCfBg: function () {
    this.setData({
      isShow: false,
      cfBg: false
    })
  },

  //服务选项
  bindSelectService: function (event) {
    var targetId = event.currentTarget.id;
    var itemList = [];
    var showSheet = false;
    var self = this;
    var pass = ''
    if (null != self.data.WiFiName || null != self.data.WiFiPassword || '' != self.data.WiFiName || '' != self.data.WiFiPassword) {
      pass = '账号:' + self.data.WiFiName + '\r\n密码:' + self.data.WiFiPassword
    } else {

      pass = '未设置WIFI'
    }

    console.log(pass)
    switch (targetId) {
      case 'tapWIFI':
        wx.showToast({
          title: pass,
          icon: 'none',
          // image: '../../images/Wifi_Icon1.png',
          duration: 4000
        })
        break;
      case 'tapBattery':
        self.setData({
          inputValue: "管家你好，请帮我办理续住",
          writing: true
        });
        break;
      case 'tapClean':
        self.setData({
          inputValue: "管家你好，请帮我安排房间清洁服务",
          writing: true
        });
        //self.sendMsg();
        break;
      case 'tapBorrow':
        showSheet = true;
        itemList = ['被子', '枕头', '拖鞋', '吹风机'];
        break;
      case 'tapWake':
        self.setData({
          inputValue: "管家你好，请为我预定叫醒服务",
          writing: true
        });
        //self.sendMsg();
        break;
      case 'tapWechat':
        self.setData({
          inputValue: "管家你好，请为我安排维修服务",
          writing: true
        });
      //self.sendMsg();
    }

    wx.showActionSheet({
      itemList: itemList,
      success: function (res) {
        if (!res.hasOwnProperty('cancel')) {
          //console.log(targetId + '-' +res.tapIndex)
          // if (targetId == 'tapBattery') {
          //   self.setData({
          //     inputValue: "管家你好，请为我送一条" + itemList[res.tapIndex],
          //     writing: true
          //   });
          // } else
          if (targetId == 'tapBorrow') {
            let units = ['1床', '1个', '1双', '1台'];
            self.setData({
              inputValue: "管家你好，请为我送来" + itemList[res.tapIndex] + units[res.tapIndex],
              writing: true
            });
          }

          //self.sendMsg();
        }
      },
      fail: function (res) {
        console.log(res.errMsg)
      }
    })
  },

  tabClick: function (e) {
    this.setData({
      activeCategoryId: e.currentTarget.id
    });
    if (e.currentTarget.id != 'TAP1') {
      var msg = { "type": "msgTo" };
      msg.to = this.data.customer.account;//.checkinInn;
      msg.checkinInn = this.data.checkinInn;
      msg.checkinRoom = this.data.checkinRoom
      msg.from = getApp().globalData.openid;
      console.log(msg)

      wx.navigateTo({
        url: "/pages/shopping/shopping?account=" + msg.to + "&hotelId=" + msg.checkinInn + "&room=" + msg.checkinRoom + "&from=" + msg.from
      })
    }

  },

  bindGoShop: function (event) {
    wx.navigateTo({
      url: "/pages/shop/index"
    })
  },

  commentDialog() {
    // console.log(this.data.butlerInfo)
    let userInfo = this.data.butlerInfo
    wx.navigateTo({
      url: "/pages/playTour/index?account=" + userInfo.account + "&username=" + userInfo.userName + "&scenes=" + this.data.scenesInit[0] + "_" + this.data.scenesInit[1] + "&imgUrl=" + this.data.imgUrl
    })
  },

  awardtDialog() {
    let self = this
    self.getCommentTag()
    this.setData({
      award: !self.data.award
    });

  },

  getCommentTag: function () {
    let _this = this;
    lavandeAPI.getCommentTagAll("", function (res) {
      if (res.success == true) {
        let data = res.data;
        let startComment = []
        let tag = [];
        for (let i in data) {
          if (i < 4) {
            startComment.push(data[i])
          }
          tag.push(data[i])
        }
        _this.setData({
          commentTag: tag,
          starComment: startComment,
          stars: 5,
          textValue: ''
        })
      }
    })
  },

  commentStar: function (event) {
    let _this = this
    let star = parseInt(event.currentTarget.dataset.startnum)
    let starComment = _this.data.commentTag
    let new_starComment = []
    for (let i in starComment) {
      new_starComment.push(starComment[i])
    }
    if (star > 4) {
      new_starComment = new_starComment.splice(0, 4)
      console.log(new_starComment)
    } else {
      new_starComment = new_starComment.splice(4, new_starComment.length)
      console.log(new_starComment)
    }
    _this.setData({
      stars: star,
      starComment: new_starComment
    })
  },

  commentClick: function (event) {
    let self = this
    let idx = parseInt(event.currentTarget.dataset.index);
    let data = self.data.starComment
    data[idx].flag == 0 ? data[idx].flag = 1 : data[idx].flag = 0
    self.setData({
      starComment: data
    })
  },

  bindKeyText: function (e) {
    this.setData({
      textValue: e.detail.value
    })
  },

  commentSub: function () {
    let self = this
    let data = {
      "commentTags": [],
      "creatTime": new Date(),
      "customerServices": self.data.butlerInfo.userName,
      "innCode": self.data.scenesInit[0],
      "remark": self.data.textValue,
      "roomNo": self.data.scenesInit[1],
      "starLavel": self.data.stars
    }
    let list = self.data.starComment
    for (let i in list) {
      if (list[i].flag == 0) {
        data.commentTags.push(list[i])
      }
    }
    console.log(data)
    lavandeAPI.postComment(JSON.stringify(data), function (respon) {
      if (respon.success == true) {
        self.setData({
          award: !self.data.award
        });
        wx.showToast({
          title: '评价成功。',
          icon: 'success',
          duration: 2000
        })
      } else {
        wx.showToast({
          title: '提交失败。',
          icon: 'success',
          duration: 2000
        })
      }
    })
  },

  getWiFi: function (innCode, roomNo) {
    let self = this
    lavandeAPI.getWiFi({ innCode: innCode, roomNo: roomNo }, function (respon) {
      if (respon.success == true) {
        console.log(respon.data)
        self.setData({
          WiFiPassword: respon.data.wifiPassword,
          WiFiName: respon.data.wifiName
        })
      }
    })
  },

  getCommnt: function (innCode, name) {
    let self = this
    lavandeAPI.getCommetCount({ innCode: innCode, name: name }, function (respon) {
      if (respon.success == true) {
        self.setData({
          starVal: respon.data,
        })
      }
    })
  },

  closeAward: function () {
    let self = this
    self.setData({
      award: !self.data.award
    });
  }

})