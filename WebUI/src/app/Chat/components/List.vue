<template>
    <div class="list">
        <ul>
            <li v-for="user in filteredUsers" v-bind:key="user.id" v-bind:id="user.userId" v-on:click="changeSession(user)">
                <img v-bind:src="user.userHead" v-bind:alt="user.userName">
                <p>{{ user.userName }} <span v-if="user.userId == 0">({{ currentCount }})</span></p>
                <!-- <div v-bind:class="[ user.has_message ? 'dot' : '' ]"></div> -->
                <Badge v-if="user.has_message" v-bind:count="chat.broadcast[user.userId].unreadCount"></Badge>
            </li>
        </ul>
    </div>
</template>

<script>
    import { mapState } from 'vuex'
    import Msg from '@/models/msg'

    export default {
        computed: {
            ...mapState([
              'chat'
            ]),
            filteredUsers: function () {
                var self = this.$store.state.chat;
                return self.users.filter(function (user) {

                    return user.userName.indexOf(self.filterUser) !== -1
                })
            },
            msgSize: function(uid){
                return 10
            }
        }, 
        methods : {
            changeSession: function (user){
                //if (typeof userId == 'number') {
                    this.$store.dispatch('selectSession',user.userId);
                    this.$store.dispatch('setHasMessageStatus',{userId:user.userId,status:false});
                //}

                let _this = this

                new Msg()
                    .PUT({
                      params: {checkinInn:'222110', openid:'oA4Ug0dmZXXmZYmBJWi2gr8--izg'}
                    })
                    .then((response) => {
                        if(response.data.success){
                                                        
                        }
                    })
            }
        }
    }
</script>

<style lang="less">
    .list{
        height: 479px;
        overflow-y: scroll;
        overflow-x: hidden;
        &::-webkit-scrollbar-button{
            display: none;
        }
        &::-webkit-scrollbar{  
            width: 8px;  
            background-color: #4d4d4d;  
        }  
          
        /*定义滚动条轨道 内阴影+圆角*/  
        &::-webkit-scrollbar-track{   
            background-color: #2e3238;  
        }  
          
        /*定义滑块 内阴影+圆角*/  
        &::-webkit-scrollbar-thumb{  
            border-radius: 10px;  
            background: rgba(255,255,255,0.1);
        }  

        ul{
            margin: 0; padding: 0;
        }
        li{
            display: flex;
            flex-direction: row;
            align-items: center;
            height: 60px;
            cursor: pointer;
            border-bottom: 1px solid #292c33;
            padding: 15px;
            &:hover{
                background: rgba(255,255,255,0.03);
            }
            &.active{
                background: rgba(255,255,255,0.1);
            }
            
            img{
                width: 30px; 
                height: 30px;
            }   
            p{
                margin-left: 5px;
                font-size: 16px;
                text-overflow:ellipsis;
                overflow:hidden;
                flex: 1;
            }
            .dot{
                width: 8px;
                height: 8px;
                border-radius: 50%;
                align-self: center;
                margin-left: 10px;
                background: #ff0000;
            }
        }
    }
</style>