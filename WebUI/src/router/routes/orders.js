export default {
  path: 'orders',
  component: resolve => require(['@/app/Orders'], resolve),
  children: [
    {
      path: '/',
      component: resolve => require(['@/app/Orders/List'], resolve)
    },
    {
      path: 'form/:id?',
      component: resolve => require(['@/app/Orders/Form'], resolve)
    }
  ]
}
