<template>
    <div class="content">
        <div class="title">
            <p>{{ chat.currentSession.userName }}</p>
        </div>
        <div class="message" ref="msg">
            <audio style="display:none" ref="musicBox" preload="metadata" controls autoplay="false" @ended="onStopPlaying()" @pause="onStopPlaying()"></audio>
            <ul ref="msgul" v-if="chat.broadcast[chat.currentSession.userId]">
                <li v-for="(msg, index) in chat.broadcast[chat.currentSession.userId].data">
                    <p>{{ msg.time }}</p>
                    <div class="msg" v-bind:class="msg.user.account == chat.currentUser.account ? 'self' : ''">
                        <img v-bind:src="msg.user.account == chat.currentUser.account ? msg.user.userHead : chat.currentSession.userHead" v-bind:alt="msg.user.userName" style="background: #ffffff">
                        <div class="text" v-if="msg.msgtype=='text'"><div v-html="msg.msg"></div></div>
                        <div v-if="msg.msgtype=='voice'" style="background:yellowgreen;border-radius:8px; padding-right:60px; padding-left:6px;display:flex;align-items:center" v-on:click="onPlayVoice(msg.msg, index, $event)">
                             <div class="cricleplay">
                                 <div class="small"></div>
                                 <div class="middle" v-bind:style="{'animation': (msg.playing ? 'show2 2s ease-in-out infinite' : 'none')}"></div>
                                 <div class="large" v-bind:style="{'animation': (msg.playing ? 'show3 2s ease-in-out infinite' : 'none')}"></div>
                             </div>
                        </div>
                        <div v-if="msg.msgtype=='img'">
                            <img v-bind:src="msg.msg" style="width:200px; height: auto;" v-on:click="onShowSourceImg(msg.msg)">
                        </div>
                    </div>
                </li>
            </ul>
        </div>
    </div>

</template>

<script>
    import { mapState } from 'vuex'

    export default {
      computed: mapState([
        'chat'
      ]),
      updated: function () {
        this.$refs.msg.scrollTop = this.$refs.msg.scrollHeight - 40;//this.$refs.msg.offsetHeight + 70;//
      },
      methods: {
        scrollview: function () {
            // this.$refs.msg.scrollTop = this.$refs.msgul.offsetHeight + 70;//this.$refs.msg.scrollHeight - 40;
            // console.log(1111);
        },
        onPlayVoice: function (url, index, event) {
            let _this = this;

            let tt = _this.chat.broadcast[_this.chat.currentSession.userId];//.data[index];
            if(tt.hasOwnProperty("data")){
                let currentMsg = _this.chat.broadcast[_this.chat.currentSession.userId].data[index];
                if(currentMsg.hasOwnProperty("playing")){

                }
                currentMsg.playing = !currentMsg.playing;

                _this.$store.dispatch('updateMessage',{msg:currentMsg, idx:index});      
            }

            if (!_this.$refs.musicBox.paused) {
                _this.$refs.musicBox.pause();

                if (index == _this.selected) {
                    return;
                }
            }

            _this.$refs.musicBox.src = url;//'https://butler.lavandehotels.club//resource//msg/171027020207330033287.mp3';//
            _this.$refs.musicBox.play();
            _this.selected = index;

        },
        onStopPlaying: function(){
            let _this = this;
            let currentMsg = _this.chat.broadcast[_this.chat.currentSession.userId].data[_this.selected];
            if(currentMsg.hasOwnProperty("playing")){
                currentMsg.playing = false;
                _this.$store.dispatch('updateMessage',{msg:currentMsg, idx:_this.selected});      
            }
        },
        onShowSourceImg: function(url){
            this.$emit('onShowSourceImg', url);
        }
      },
      data () {
        return{
            selected : -1
        }
      }
    }
</script>

<style lang="less">
    .small{
      width: 10px;
      height: 10px;
      border-style: solid;
      border-top-color: transparent;
      border-left-color: transparent;
      border-bottom-color: transparent;
      border-radius: 50%;
      box-sizing: border-box;
      vertical-align: middle;
      display: inline-block;
    }

    .middle{
      width: 18px;
      height: 18px;
      border-style: solid;
      border-top-color: transparent;
      border-left-color: transparent;
      border-bottom-color: transparent;
      border-radius: 50%;
      box-sizing: border-box;
      vertical-align: middle;
      display: inline-block;
      margin-left: -10px;
      opacity: 1;
    }
    @keyframes show2 {
      0% { opacity: 0;}
      30% { opacity: 1;}
      100% { opacity: 0;}
    }
    .large{
      width: 24px;
      height: 24px;
      border-style: solid;
      border-top-color: transparent;
      border-left-color: transparent;
      border-bottom-color: transparent;
      border-radius: 50%;
      box-sizing: border-box;
      vertical-align: middle;
      display: inline-block;
      margin-left: -16px;
      opacity: 1;
    }
    @keyframes show3 {
      0% { opacity: 0;}
      60% { opacity: 1;}
      100% { opacity: 0;}
    }

    .content{
        display: flex;
        flex-direction: column;

        .title{
            height: 50px;
            display: flex;
            justify-content: center;
            align-items: center;
            border-bottom: 1px solid #d6d6d6;

            p{
                text-align: center;
                font-size: 16px;
                color: #666;
            }
        }

        .message{
            overflow-y: scroll;
            padding: 10px 15px;
            height: 400px;
            &::-webkit-scrollbar-button{
                display: none;
            }
            &::-webkit-scrollbar{  
                width: 8px;  
                background-color: #eee;  
            }  
              
            /*定义滚动条轨道 内阴影+圆角*/  
            &::-webkit-scrollbar-track{   
                background-color: #eee;  
            }  
              
            /*定义滑块 内阴影+圆角*/  
            &::-webkit-scrollbar-thumb{  
                border-radius: 10px;  
                background: #ccc;
            }  

            li{
                display: flex;
                flex-direction: column;
                margin-bottom: 15px;

                p{
                    align-self: center;
                    padding: 0 18px;
                    font-size: 12px;
                    color: #fff;
                    border-radius: 2px;
                    background-color: #dcdcdc;
                    margin: 5px 0px;
                }

                .msg{
                    display: flex;
                    flex-direction: row;

                    img{
                        width: 30px;
                        height: 30px;
                        margin-right: 10px;
                        flex-shrink: 0;
                    }
                    .emoji {
                        margin-right: 2px;
                        width: 20px;
                        height: 20px;
                    }
                    .text{
                        font-family: "Apple Color Emoji","Segoe UI Emoji","NotoColorEmoji","Segoe UI Symbol","Android Emoji","EmojiSymbols";
                        padding: 10px;
                        min-height: 30px;
                        font-size: 14px;
                        text-align: left;
                        word-break: break-all;
                        background: #fafafa;
                        border-radius: 4px;
                        line-height: 1.4;
                    }
                }
                .self{
                    flex-direction: row-reverse;
                    .text{
                        background: #b2e281;
                    }
                    img{
                        margin-right: 0px; margin-left: 10px;
                    }
                    .emoji {
                        margin-left: 2px;
                        width: 20px;
                        height: 20px;
                    }
                }
            }
        }
    }


</style>