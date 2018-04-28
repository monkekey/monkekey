export default {
  path: 'accounts',
  component: resolve => require(['@/app/Accounts'], resolve),
  children: [
    {
      path: '/',
      component: resolve => require(['@/app/Accounts/List'], resolve)
    },
    {
      path: 'form/:id?',
      component: resolve => require(['@/app/Accounts/Form'], resolve)
    }
  ]
}
