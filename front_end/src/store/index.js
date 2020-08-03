import Vue from "vue";
import Vuex from "vuex";

import axios from "axios"
// import cookies  from "vue-cookies"
// import qs from "qs"

// import router from "@/router"
import SERVER from "@/api/spring"

Vue.use(Vuex);

export default new Vuex.Store({
  // data의 집합(중앙 관리할 모든 데이터 === 상태)
  state: {
    theme: "",
    authorization: localStorage.getItem('authorization'),
    accesstokenexpiraiondate: localStorage.getItem('accesstokenexpiraiondate'),
    refreshtoken: localStorage.getItem('refreshtoken'),
    refreshtokenexpiraiondate: localStorage.getItem('refreshtokenexpiraiondate'),

    isLogin: !!localStorage.getItem('authorization'),
    userEmail: "",

    emailResult: false,
  },

  // state를 (가공해서)가져올 함수들. === computed
  getters: {
    isEmailChecked: state => !!state.emailResult,
  },

  // state를 변경하는 함수들(mutations에 작성되지 않은 state 변경 코드는 모두 동작하지 않음.)
  // 모든 mutation 함수들은 동기적으로 동작하는 코드.all
  // commit 을 통해 실행함.
  // mutations은 첫 번째 인자로 state를 받아야함.
  mutations: {
    //auth
    SET_TOKEN(state, token) {
      console.log("SET_TOKEN entered...");
      console.log(token);

      console.log(token.authorization);
      console.log(token.accesstokenexpiraiondate);
      console.log(token.refreshtoken);
      console.log(token.refreshtokenexpiraiondate);
      console.log(token.useremail);

      state.authorization = token.authorization;
      state.accesstokenexpiraiondate = token.accesstokenexpiraiondate;
      state.refreshtoken = token.refreshtoken;
      state.refreshtokenexpiraiondate = token.refreshtokenexpiraiondate;
      state.userEmail = token.useremail;

      console.log("state.authorization : " + state.authorization);
      console.log("state.accesstokenexpiraiondate : " + state.accesstokenexpiraiondate);
      console.log("state.refreshtoken : " + state.refreshtoken);
      console.log("state.refreshtokenexpiraiondate : " + state.refreshtokenexpiraiondate);
      console.log("state.userEmail : " + state.userEmail);

      localStorage.setItem("authorization", state.authorization);
      localStorage.setItem("accesstokenexpiraiondate", state.accesstokenexpiraiondate);
      localStorage.setItem("refreshtoken", state.refreshtoken);
      localStorage.setItem("refreshtokenexpiraiondate", state.refreshtokenexpiraiondate);
      state.isLogin = true;
    },

    // for logout
    toggleIsLogin(state) {
      state.isLogin = false;
    },

    setEmailResult(state, emailResult) {
      if (emailResult === "success") {
        state.emailResult = true
      } else {
        state.emailResult = false
      }
    }
  },

  // 범용적인 함수들. mutations에 정의한 함수를 actions에서 실행 가능.
  // 비동기 로직은 actions에서 정의.
  // dispatch를 통해 실행함.
  actions: {
    // auth
    postAuthData({ commit, dispatch }, info) {
        axios.post(SERVER.URL + info.location, info.data)
        .then(res => {
          if (info.location === SERVER.ROUTES.signup) {
            console.log(res.data["result"])
            commit("setEmailResult", res.data["result"])
          }
          else if(info.location === SERVER.ROUTES.login) {
            console.log(res);
            commit('SET_TOKEN', res.headers);
          }
          else if(info.location === SERVER.ROUTES.email) {
            console.log(res.data["result"])
            commit("setEmailResult", res.data["result"])
            info.location = SERVER.ROUTES.authSend
            dispatch('postAuthData', info)
          }
          else if(info.location === SERVER.ROUTES.authSend) {
            console.log(res.data)
          }
          else if(info.location === SERVER.ROUTES.authCheck) {
            console.log(res.data)
          }
        })
        .catch(err => console.error(err.response.data));
    },

    login({ dispatch }, loginData) {
      const info = {
        data: loginData,
        location: SERVER.ROUTES.login
      }
      dispatch('postAuthData', info)
    },
    signup({ dispatch }, signupData) {
      const info = {
        data: signupData,
        location: SERVER.ROUTES.signup
      }
      dispatch("postAuthData", info);
      
      const loginData = {
        data: {
          email: info.data.email,
          password: info.data.password
        },
        location: SERVER.ROUTES.login
      }
      dispatch("postAuthData", loginData);
    },
    logout({ commit }) {
      localStorage.removeItem("authorization");
      localStorage.removeItem("accesstokenexpiraiondate");
      localStorage.removeItem("refreshtoken");
      localStorage.removeItem("refreshtokenexpiraiondate");

      commit("toggleIsLogin");
    },
    checkEmail({ dispatch }, signupData) {
      const info = {
        data: signupData,
        location: SERVER.ROUTES.email
      }
      dispatch("postAuthData", info)
    },
    sendEmail({ dispatch }, signupData) {
      const info = {
        data: signupData,
        location: SERVER.ROUTES.authSend
      }
      dispatch("postAuthData", info)
    },
    confirmAuthNum({ dispatch }, signupData) {
      const info = {
        data: signupData,
        location: SERVER.ROUTES.authCheck
      }
      dispatch("postAuthData", info)
    },
  },
  modules: {}
});
