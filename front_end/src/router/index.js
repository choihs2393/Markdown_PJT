import Vue from "vue";
import VueRouter from "vue-router";

import Home from "@/views/Home.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
});

// router.beforeEach((to, from, next) => {
//   const publicPages = ['Home']  // Login 안해도 됨
//   const authPages = ['Login', 'Signup']  // Login 되어있으면 안됨
  
//   const authRequired = !publicPages.includes(to.name)  // 로그인 해야 함.
//   const unauthRequired = authPages.includes(to.name)  // 로그인 해서는 안됨
//   const isLoggedIn = !!Vue.$cookies.isKey('auth-token')

//   if(unauthRequired && isLoggedIn) {
//     next('/')
//   }
//   authRequired && !isLoggedIn ? next({ name: 'Login'}) : next()
// })

export default router;
