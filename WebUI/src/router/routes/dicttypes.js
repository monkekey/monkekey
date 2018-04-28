export default {
  path: 'dicttypes',
  component: resolve => require(['@/app/DictTypes'], resolve),
  children: [
    {
      path: '/',
      component: resolve => require(['@/app/DictTypes/List'], resolve)
    },
    {
      path: 'form/:id?',
      component: resolve => require(['@/app/DictTypes/Form'], resolve)
    }
  ]
}
