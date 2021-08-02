import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
// import Home from '../views/Home.vue'

import BookStore from '../pages/BookStore/BookStore.vue';
import UserCollection from '../pages/user/Collection/UserCollection.vue';
import UserProfile from '../pages/user/Profile/UserProfile.vue';
import UserWallent from '../pages/user/Wallent/UserWallent.vue';
import UserLogin from '../pages/user/Login/UserLogin.vue';
import UserNotLogin from '../pages/user/NotLogin/UserNotLogin.vue';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect: '/bookstore'
  },
  {
    path: '/bookstore',
    name: 'bookstore',
    component: BookStore
  },
  {
    path: '/user/profile',
    name: 'userProfile',
    component: UserProfile
  },
  {
    path: '/user/collection',
    name: 'userCollection',
    component: UserCollection
  },
  {
    path: '/user/wallent',
    name: 'userWallent',
    component: UserWallent
  },
  {
    path: '/user/login',
    name: 'userLogin',
    component: UserLogin
  },
  {
    path: '/user/notlogin',
    name: 'userNotLogin',
    component: UserNotLogin
  }
  // {
  //   path: '/about',
  //   name: 'About',
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  //   component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  // }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
