<template>
    <div class="card">
        <div class="header">
            <img class="avatar" v-bind:class="[ chat.suspended ? 'offline' : 'online' ]" src="static/images/yumi_logo.png" v-on:click="changeSuspended">
            <p class="name" v-on:click="onShowModl">{{ chat.currentUser.userName }} </p>
            <div class="dot" v-bind:class="[ chat.online ? 'dot-green' : 'dot-red' ]"></div>
            <img class="logout" src="static/images/logout.png" v-on:click="onLogout">
        </div>
        <div class="search">
            <input type="text" placeholder="Search" v-on:keyup="onKeyup">
        </div>
    </div>
</template>

<script>
    import { mapState } from 'vuex'

    export default {

        computed: mapState([
            'chat'
        ]),
        data () {
            return{
                visible : '',
            }
        },
        methods:{
            onKeyup: _.debounce(function(e) {
                this.$store.dispatch('searchUser', e.target.value);
            }, 300),

            onShowModl: function (event){
                this.$emit('onShowModl');
            },
            onLogout: function(){
                this.$router.push('/logout')
                this.$Message.success('退出成功')
            },
            changeSuspended: function(){
                if (this.$store.state.chat.suspended) {
                    this.$Message.success('当前用户取消挂起');
                }else{
                    this.$Message.success('当前用户挂起');
                }
                this.$store.dispatch('changeSuspended', !this.$store.state.chat.suspended);
            }
        }
    }
</script>

<style lang="less">
    .card{
        display: flex;
        flex-direction: column;
        padding: 20px 15px;
        border-bottom: 1px solid #24272c;

        .header{
            display: flex;
            flex-direction: row;
            justify-content: space-between;

            .avatar {
                width: 40px; 
                height: 40px;
                background: #ffffff;
                border-radius: 40px;
            }

            .online {
                border:3px solid #00ff01;
            }

            .offline {
                border:3px solid lightgray;
            }

            .logout {
                width: 20px; 
                height: 20px;
                align-self: center;
            }

            p{
                font-size: 16px;
                align-self:center;
                margin-left: 15px;
            }
            .dot{
                width: 8px;
                height: 8px;
                border-radius: 50%;
                align-self: center;
                margin-left: 10px;
                background: #eee;
            }
            .dot-green{
                background: #00ff00;
            }
            .dot-red{
                background: #ff0000;
            }
        }

        .search{
            input{
                margin-top: 10px;
                width: 100%;
                background: #26292e;
                border: 1px solid #3a3a3a;
                height: 30px;
                border-radius: 4px;
                color: #fff;
                padding: 10px;
            }
        }
    }
</style>