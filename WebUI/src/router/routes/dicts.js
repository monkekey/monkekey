export default {
  path: 'dicts',
  component: resolve => require(['@/app/Dicts'], resolve),
  children: [
    {
      path: '/',
      component: resolve => require(['@/app/Dicts/List'], resolve)
    },
    {
      path: 'form/:id?',
      component: resolve => require(['@/app/Dicts/Form'], resolve)
    }
  ]
}
