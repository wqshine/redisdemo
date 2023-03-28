import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

// 配置路由路径
let routers;

/*这里只产生路由，菜单设计资源从数据库获取*/
routers = [
  {
    id: '1',
    path: '/',
    name: 'home',
    meta: {title: "主界面"},
    hidden: true,//判断是否显示在导航区
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import('./views/Home.vue')
  },
  {
    path: '/UserInfo',
    name: 'UserInfo',
    meta: {title: "用户信息"},
    hidden: false,//判断是否显示在导航区
    component: () => import('./views/UserInfo')
  },{
    path: '/UserList',
    name: 'UserList',
    meta: {title: "用户列表"},
    hidden: false,//判断是否显示在导航区
    component: () => import('./views/UserList')
  },
  {
    path: '/demo',
    name: 'demo',
    redirect: '/',
    hidden: true,
    meta: {title: "demo"},
    component: {render: (e) => e("router-view")},
    children: [
      {
        path: '/demo/for',
        name: 'for循环',
        meta: {title: "for循环"},
        component: () => import('./views/demo/for')
      },
      {
        path: '/demo/maps',
        name: '地图功能',
        meta: {title: "地图功能"},
        component: () => import('./views/demo/maps')
      }
    ]
  }
]


export default new Router({
    routes:routers
})


