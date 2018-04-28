export default {
  path: 'inns',
  component: resolve => require(['@/app/Inns'], resolve),
  children: [
    {
      path: '/',
      component: resolve => require(['@/app/Inns/List'], resolve)
    },
    {
      path: 'form/:id?',
      component: resolve => require(['@/app/Inns/Form'], resolve)
    }
  ]
}
