import Vue from "vue";
import App from "./App.vue";
import "./markdown/registerServiceWorker";
// import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
import axios from 'axios'
import SERVER from '@/api/spring'

Vue.config.productionTip = false;

Object.defineProperty(Date.prototype, 'YYYYMMDDHHMMSS', {
  value: function() {
    function pad2(n) {  // always returns a string
      return (n < 10 ? '0' : '') + n;
    }
    
    return this.getFullYear() +
    pad2(this.getMonth() + 1) + 
    pad2(this.getDate()) +
    pad2(this.getHours()) +
    pad2(this.getMinutes()) +
    pad2(this.getSeconds());
  }
});

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

      const nowTime = Number(new Date().YYYYMMDDHHMMSS())
      const expiraionTime = Number(localStorage.getItem('access-token-expiraion-date'))

      // console.log('nowTime:', nowTime)
      // console.log('expiraionTime:', expiraionTime)
      // console.log('expiraionTime - 5분:', expiraionTime - 500)
      // console.log(nowTime > expiraionTime - 500 && nowTime < expiraionTime)

      
      
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
            
            console.log('토큰 갱신!!')
            // console.log(res.headers)
            store.commit('SET_TOKEN', res.headers)

            // store.state.authorization = res.headers['authorization']
            // localStorage.setItem('authorization', store.state.authorization)
            // console.log('errorAPI', errorAPI)
            return axios(errorAPI)
          })
      }
    }
    return Promise.reject(err)
  })

new Vue({
  // router,
  store,
  vuetify,
  render: h => h(App)
}).$mount("#app");
