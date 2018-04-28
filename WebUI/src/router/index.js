import Vue from 'vue'
import Router from 'vue-router'
import auth from '@/utils/auth'
import storage from '@/utils/storage'
import Root from '@/app/Root'
import Layout from '@/app/Layout'
import Chat from '@/app/Chat'
import notFound from './routes/notFound'
import home from './routes/home'
import inns from './routes/inns'
import rooms from './routes/rooms'
import accounts from './routes/accounts'
import services from './routes/services'
import dicttypes from './routes/dicttypes'
import dicts from './routes/dicts'
// import commoditys from './routes/commoditys'
import goodses from './routes/goodses'
import orders from './routes/orders'
import login from './routes/login'
import logout from './routes/logout'

import iView from 'iview'

Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/chat',
      component: Chat,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/',
      component: Root,
      children: [
        {
          path: '/',
          component: Layout,
          children: [
            home,
            inns,
            rooms,
            accounts,
            services,
            dicttypes,
            dicts,
            // commoditys,
            goodses,
            orders
            
          ],
          meta: {
            requiresAuth: true
          }
        },
        login,
        logout,
        notFound
      ]
    },
    
  ]
})

router.beforeEach((to, from, next) => {
  iView.LoadingBar.start()

  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!auth.loggedIn()) {
      next({
        path: 'login',
        query: {redirect: to.fullPath}
      })
    } else {
      var roleList = (_.map(auth.get().lavande_butler_userinfo.sysroleList, 'role')).toString();

      if (to.path.indexOf("chat") >=0 ) {
        if(roleList.indexOf("kefu") >= 0 || roleList.length == 0){
          next()
        }else{
          auth.logout()
          next({
            path: 'login'
          })
        }
      }else{
        if (roleList.length > 0 && roleList!='kefu') {
          next() 
        }else{
          auth.logout()
          next({
            path: 'login'
          })
        } 
      }
    }
  } else {
    next()
  }
})

router.afterEach((to, from, next) => {
  iView.LoadingBar.finish()
})

export default router