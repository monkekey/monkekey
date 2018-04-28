export default {
  path: 'services',
  component: resolve => require(['@/app/Services'], resolve),
  children: [
    {
      path: '/',
      component: resolve => require(['@/app/Services/List'], resolve)
    },
    {
      path: 'form/:id?',
      component: resolve => require(['@/app/Services/Form'], resolve)
    }
  ]
}
