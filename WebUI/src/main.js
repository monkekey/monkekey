// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import lodash from 'lodash'
import VueLodash from 'vue-lodash'
import App from './App'
import router from './router'
import Axios from 'axios'
import vmodal from 'vue-js-modal'
import moment from 'moment';

import iView from 'iview';
import 'iview/dist/styles/iview.css';    // 使用 CSS

Vue.mixin({
  created() {
    this.$moment = moment;
  },
});

import store from '@/vuex'


Vue.use(VueLodash, lodash)
Vue.use(iView);
Vue.use(vmodal)

Vue.config.productionTip = false

// Axios.defaults.withCredentials = true
Vue.prototype.$http = Axios

// 全局的请求拦截器
Axios.interceptors.request.use(function (config) {
  // if (localStorage.hasOwnProperty("lavande_butler_token")) {  // 判断是否存在token，如果存在的话，则每个http header都加上token
  //   config.headers.Authorization = 'Bearer ' +localStorage.getItem('lavande_butler_token');
  // }
  return config;
}, function (error) {
  return Promise.reject(error);
});


/* eslint-disable no-new */
new Vue({
  el: '#app',
  store,
  router,
  template: '<App/>',
  components: { App }
})
