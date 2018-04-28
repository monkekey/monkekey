export default {
  path: 'commoditys',
  component: resolve => require(['@/app/Commoditys'], resolve),
  children: [
    {
      path: '/',
      component: resolve => require(['@/app/Commoditys/List'], resolve)
    },
    {
      path: 'form/:id?',
      component: resolve => require(['@/app/Commoditys/Form'], resolve)
    }
  ]
}
