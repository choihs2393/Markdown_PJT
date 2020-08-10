import Vue from "vue";
import App from "./App.vue";
import "./registerServiceWorker";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
import axios from 'axios'
import SERVER from '@/api/spring'
// import api from "@/api/api"

Vue.config.productionTip = false;
// Vue.prototype.$http = axios;
// axios.defaults.timeout = 10000;

axios.interceptors.request.use(
  function (config) {
    // 요청을 보내기 전에 수행할 일
    // console.log('request interceptor!!');
    config.headers.Authorization = localStorage.getItem('authorization');

    return config;
  },
  function (error) {
    // 오류 요청을 보내기전 수행할 일
    return Promise.reject(error);
  });


axios.interceptors.response.use(
  function(response) {
    // console.log('response interceptor success!!');
    return response
  },
  function(err) {
    // console.log('response interceptor error!!');

    const errorAPI = err.config

    if (err.response.status===401 && errorAPI.retry===undefined) {
      // console.log('response interceptor 401 error!!');

      errorAPI.retry = true

      if (!!localStorage.getItem('authorization')) {
        return axios.post(SERVER.URL + SERVER.ROUTES.newATBR, null, { headers: { RefreshToken: store.state.refreshToken } })
          .then(res => {
            // console.log('토큰 갱신!!')
            store.state.authorization = res.headers['authorization']
            localStorage.setItem('authorization', store.state.authorization)
            // console.log('errorAPI', errorAPI)
            return axios(errorAPI)
          })
      }
    }
    return Promise.reject(err)
  })

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount("#app");
