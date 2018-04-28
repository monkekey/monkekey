<template>
    <div class="content">
        <div class="user-text">
            <div style="display:flex;flex-direction: row;">
                <Upload :action="uploadurl" accept="image/*" 
                        name="uploadingFile"
                        :data="uploadparam"
                        :max-size="10240" 
                        :on-success="handleSuccess" 
                        :show-upload-list="false" style="width:60px">
                    <div style="width: 20px;height:20px;">
                        <Icon type="camera" size="20"></Icon>
                    </div>
                </Upload>
                <div style="width: 20px;height:20px;">
                    <Poptip placement="top" width="410">
                        <Icon type="happy-outline" size="20"></Icon>
                        <div class="emoji" slot="content">
                            <ul class="">
                                <li v-for="em in emojis" v-bind:key="em.emoji" v-bind:id="em.emoji" v-on:click="emojiChoose(em)">
                                    <img v-bind:src="em.emojiurl" v-bind:alt="em.emoji">
                                </li>
                            </ul>
                        </div>
                    </Poptip>
                </div>
            </div>
            <textarea name="" id="" placeholder="Press enter to send" v-model="content" v-on:keyup.enter="send"></textarea>
        </div>
        <div class="text-footer">
            
            <Button  v-on:click="send">发送</Button >
        </div>
    </div>
</template>


<script>
    import { mapState } from 'vuex'
    import consts from '@/utils/consts'
    import twemoji from 'twemoji'
    
    export default {
        data () {
            return{
                content : '',
                uploadurl : consts.API_URL + '/file/uploadFile',
                uploadparam: {'fileType': 'msg'},
                emojiChar: consts.EMOJI_CHAR,
                emoji: consts.EMOJI,
                emojis: [],
            }
        },
        created: function () {
            var em = {}, _this = this, emChar = _this.emojiChar.split("-");
            var emojis = []
            _this.emoji.forEach(function (v, i) {
              em = {
                char: emChar[i],
                emoji: v,
                emojiurl: 'static/emoji/' + v + '.png'
              };
              emojis.push(em)
            });

            _this.emojis = emojis;
        },
        computed: {
            ...mapState([
                'chat'
            ])
        },
        methods:{
            send: function(e){
                let _this = this;

                //_this.currentUser.cId,
                //_this.currentSession.cId
                let msg = {
                        type: "msgTo",
                        from : _this.chat.currentUser.account,
                        to : _this.chat.currentSession.account,
                        info : _this.utf16toEntities(_this.content)
                    };

                if (_this.content !== '' ) {
                    _this.chat.connection.send(JSON.stringify(msg));
                    _this.content = '';

                    //msg.is_self = 1;
                    //_this.addMessage(msg); 返回成功再显示

                    msg.date = '2018-01-02 12:12:35';
                    msg.is_self = 1;
                    msg.msgtype = 'text';

                    if (msg.info.endsWith(".mp3")){
                      msg.msgtype = 'voice';
                      msg.info = consts.STATIC_URL + '/msg/' + msg.info;
                    } else if (msg.info.endsWith(".png")) {
                      msg.msgtype = 'img';
                      msg.info = consts.STATIC_URL + '/msg/' + msg.info;
                    } else{
                        let tmp_content = msg.info;

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
                        msg.info = tmp_content
                    }

                    _this.$store.dispatch('addMessage', msg);
                }else{
                    _this.$store.dispatch('showNotice',' 消息不能为空!','warning');
                }
                
            },

            before: function(file) {
                this.files.push({
                    file: file,
                    data: {
                        fileType: 'msg'
                    }
                });

                return true;
            },
            handleSuccess: function(res, file){
                //res: {success: true, errMsg: "", data: "171026071357355856910.png"}
                if (res.success) {
                    this.content = res.data;
                    this.send();
                }
            },
            addService: function (servicetype){
                this.content = servicetype
            },
            emojiChoose: function (emoji){
                this.content = this.content + emoji.char;//"[" + emoji.emoji + "]"
            },

            utf16toEntities: function (str) {
                var patt=/[\ud800-\udbff][\udc00-\udfff]/g; // 检测utf16字符正则  
                let entstr = str.replace(patt, function(char){  
                    if (char.length===2) {  
                        return "[0x" + char.codePointAt(0).toString(16) + "]"; 
                    } else {  
                        return char;  
                    }  
                });  
                return entstr;
            }
        }
    }

    
</script>


<style lang="less">
    .content{
        display: flex;
        flex-direction: column;
    
        .user-text{
            height: 120px;
            border-top: 1px solid #ddd;

            textarea{
                padding: 10px;
                resize: none;
                width: 100%;
                height: 100%;
                border: none;
                outline: none;
            }
        }
        .text-footer{
            display: flex;
            padding: 10px 20px;
            background: #fff;
            justify-content: flex-end;
            align-items: flex-end;
            button{
                background: #fff;
                padding: 3px 20px;
                color: #222;
                border: 1px solid #c1c1c1;
                border-radius: 3px;
            }
            span{
                font-size: 14px;
                color: #999;
                margin-right: 10px;
            }
        }

        .emoji{
            width: 100%;

            ul {  
                width: 100%;  
            }  

            li {  
                width: 33px;
                height: 33px;
                float: left; 
                padding: 2px;
                display: inline-block;

            }
        }
    }
</style>