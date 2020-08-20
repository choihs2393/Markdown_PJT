import Vue from "vue";
import App from "./App.vue";
import "./markdown/registerServiceWorker";
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
    config.headers.Authorization = localStorage.getItem('authorization');
    
    return config;
  },
  function (error) {
    // 오류 요청을 보내기전 수행할 일
    return Promise.reject(error);
  });
  
  
  axios.interceptors.response.use(
    function(response) {
      const nowTime = Number(new Date().YYYYMMDDHHMMSS())
      const accessTokenExpiraionTime = Number(localStorage.getItem('access-token-expiraion-date'))
      const refreshTokenExpiraionTime = Number(localStorage.getItem('refresh-token-expiraion-date'))

      if (!!localStorage.getItem('authorization')) {
        if (nowTime > accessTokenExpiraionTime - 1000 && nowTime < accessTokenExpiraionTime) {
          
          axios.post(SERVER.URL + SERVER.ROUTES.newATBA)
          .then(res => {
            store.commit('SET_TOKEN', res.headers)
          })
          .catch(err => console.error(err.response.data))
        }
  
        if (nowTime > refreshTokenExpiraionTime - 100000 && nowTime < refreshTokenExpiraionTime) {
          
          axios.post(SERVER.URL + SERVER.ROUTES.newATBA)
          .then(res => {
            store.commit('SET_TOKEN', res.headers)
          })
          .catch(err => console.error(err.response.data))
        }
      }

      return response
    },

    function(err) {
      const errorAPI = err.config

      // if (err.response.status===401 && errorAPI.retry===undefined) {
      if (err.response.status===406 && errorAPI.retry===undefined) {
        errorAPI.retry = true

        if (!!localStorage.getItem('authorization')) {
          return axios.post(SERVER.URL + SERVER.ROUTES.newATBR, null, { headers: { RefreshToken: localStorage.getItem('refresh-token') } })
            .then(res => {
              
              store.commit('SET_TOKEN', res.headers)
              return axios(errorAPI)
            })
            .catch(err => {
              console.error(err.response.data)
              store.dispatch('logout')
            })
        } else {
        }
      } else if (err.response.status===401 && !!localStorage.getItem('authorization')) {
        store.dispatch('logout')
      }
      return Promise.reject(err)
    })

new Vue({
  // router,
  store,
  vuetify,
  render: h => h(App)
}).$mount("#app");
