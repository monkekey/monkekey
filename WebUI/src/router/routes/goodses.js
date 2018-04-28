export default {
  path: 'goodses',
  component: resolve => require(['@/app/Goodses'], resolve),
  children: [
    {
      path: '/',
      component: resolve => require(['@/app/Goodses/List'], resolve)
    },
    {
      path: 'form/:id?',
      component: resolve => require(['@/app/Goodses/Form'], resolve)
    }
  ]
}
