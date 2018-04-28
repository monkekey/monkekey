export default {
  path: 'rooms',
  component: resolve => require(['@/app/Rooms'], resolve),
  children: [
    {
      path: '/',
      component: resolve => require(['@/app/Rooms/List'], resolve)
    },
    {
      path: 'form/:id?',
      component: resolve => require(['@/app/Rooms/Form'], resolve)
    }
  ]
}
