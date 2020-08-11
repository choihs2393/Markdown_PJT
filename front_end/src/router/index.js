import Vue from "vue";
import VueRouter from "vue-router";

import Home from "@/views/Home.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Home",
     component: () =>
       import("../views/Home.vue")
   // component: Home
  },
  {
    path: "/about",
    name: "About",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/About.vue")
  }
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
