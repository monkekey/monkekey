import Vue from 'vue'

export default {
  FILTER_USER: (state, nickname) => {
        state.filterUser = nickname;
    },

    CHANGE_SESSION: (state, userId) => {
        for (var i = state.users.length - 1; i >= 0; i--) {
            if (state.users[i].userId != userId) {
                continue;
            }
            state.currentSession = state.users[i];
            break;
        }

        let newMsgs = state.broadcast[userId];
        if (newMsgs = undefined) {
            newMsgs.unreadCount = 0
            Vue.set(state.broadcast,  userId, newMsgs);
        }
    },

    SET_USER: (state, user) => {
        //user.userHead = 'http://tva3.sinaimg.cn/crop.0.0.200.200.50/701cac0cjw8ez3nd2wa7rj205k05kt8v.jpg';
        //user.userHead = consts.STATIC_URL + '/images/' + user.innCode + '/' + user.account + '.png'
        state.currentUser = user;
    },

    ADD_USER: (state, user) => {
        if (user instanceof Array) {

            for (var i = user.length - 1; i >= 0; i--) {
                if (user[i].userId != state.currentUser.userId) {
                    user[i].has_message = false;
                    state.users.push(user[i]);
                }
                
            }
        }else{
            let is_old = false;
            state.users.forEach((item,index) => {
                if (item.userId == user.userId) {
                    is_old = true
                    var index = state.users.indexOf(item); 
                    
                    user.has_message = item.has_message

                    state.users.splice(index, 1, user)
                }
            });
            if(!is_old){
                user.has_message = false;   
                state.users.push(user);
            }
        }
    
    },

    REMOVE_USER: (state, userId) => {
        state.users.forEach((item,index) => {
            if (item.id == userId) {
                var index = state.users.indexOf(item); 
                state.users.splice(index, 1)
            }
        });
    },

    SET_CONN: (state, conn) => {
        if (conn != null ) {
            //&& state.connection == null
            state.connection = conn;
        }
    },

    CHANGE_STATUS: (state, status) => {
        state.online = status;
    },

    CHANGE_SUSPENDED: (state, status) => {
        //是否挂起
        state.suspended = status;
    },

    ADD_MESSAGE: (state, message) => {
        if (message.hasOwnProperty("id") && message.id != undefined && message.id != 'undefined') {
            if (state.msgids.includes(message.id)) {
                return
            }
            
            if(message.is_self == 0){
                var n = new Notification('消息更新提醒',{
                    body: '您有新的回复，快去查看吧',
                    tag: 'guest_msg',
                    icon: '/static/images/yumi_logo.png',
                    sound: '/static/images/msgtip.mp3',
                    requireInteraction: true
                })

                var audio = new Audio('/static/images/msgtip.mp3');
                audio.play();

            }
            
            state.msgids.push(message.id);
        }

        let msg = {
            user : {},
            msg : message.info,
            time : message.date,
            msgtype : message.msgtype,
            is_read: message.is_read,
            playing: false,
            is_self: message.is_self
        };
        if (message.from == state.currentUser.account) {
            msg.user = state.currentUser;
        }else{
            for (var i = state.users.length - 1; i >= 0; i--) {
                if (state.users[i].userId == message.from) {
                    msg.user = state.users[i];
                    break;
                }
            }
        }

        if (message.is_self == 1) {
            message.from = message.to;
        }

        let bid = '';
        for (var i = state.users.length - 1; i >= 0; i--) {
            if (state.users[i].userId == message.from) {
                bid = state.users[i].userId;
                break;
            }
        }

        let newMsgs = state.broadcast[bid];

        if (newMsgs == undefined) {
            newMsgs = {data : []};
        }

        newMsgs.data.push(msg);

        var unreadCount = 0;
        newMsgs.data.forEach((item,index) => {
            if (!item.is_read && item.is_self==0) {
                unreadCount = unreadCount + 1;
            }
        });
        newMsgs.unreadCount = unreadCount;

        Vue.set(state.broadcast, message.from, newMsgs);
    },

    UPDATE_MESSAGE: (state, payload) => {
        debugger
        let bid = payload.msg.user.userId;
        let newMsgs = state.broadcast[bid];

        if (newMsgs == undefined) {
            newMsgs = {data : [], unreadCount:0};
        }

        if (newMsgs.data.length >0 ) {
            newMsgs.data[payload.idx] = payload.msg

            Vue.set(state.broadcast, bid, newMsgs);
        }
    },

    SET_HAS_MESSAGE : (state, payload) => {
        for (var i = state.users.length - 1; i >= 0; i--) {
            if(state.users[i].userId == payload.userId){
                if(payload.status == false || (!state.currentSession.hasOwnProperty("userId") || (state.currentSession.userId != payload.userId))){
                    state.users[i].has_message = payload.status;
                }
            }
        }
    },

    SET_COUNT : (state, count) => {
        state.currentCount = count;
    },

    SHOW_NOTICE : (state, msg, type) => {
        state.notice = {
            show : true, msg, type
        }

        setTimeout(function(){
            state.notice.show = false;
        },3000);
    },

    TOP_SERVICE : (state, payload) => {
        state.topService = payload.data;
    }
}
