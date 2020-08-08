import Vue from 'vue'
import Vuex from 'vuex'
import router from "@/router/index.js"

import axios from 'axios'
import SERVER from '@/api/spring'



Vue.use(Vuex);

export default new Vuex.Store({

  // data의 집합(중앙 관리할 모든 데이터 === 상태)
  state: {

    // auth_token
    authorization: localStorage.getItem('authorization'),
    accessTokenExpiraionDate: localStorage.getItem('access-token-expiraion-date'),
    refreshToken: localStorage.getItem('refresh-token'),
    refreshTokenExpiraionDate: localStorage.getItem('refresh-token-expiraion-date'),
    userEmail: '',

    // email_authentication
    isDuplicateChecked: false,
    isAuthNumChecked: false,

    // password_check
    isPasswordChecked: false,

    // modal
    drawer: false,
    drawerShare: false,
    isLogoutModal: false,

    theme: '',
  },

  // state를 (가공해서)가져올 함수들. === computed
  getters: {
    isLoggedIn: state => !!state.authorization,

    config: state => ({
      headers: {
        Authorization:  state.authorization,
        RefreshToken: state.refreshToken,
      }
    }),
  },

  // state를 변경하는 함수들(mutations에 작성되지 않은 state 변경 코드는 모두 동작하지 않음.)
  // 모든 mutation 함수들은 동기적으로 동작하는 코드.all
  // commit 을 통해 실행함.
  // mutations은 첫 번째 인자로 state를 받아야함.
  mutations: {

    // 토큰 저장
    SET_TOKEN(state, token) {      
      state.authorization = token.authorization
      state.accessTokenExpiraionDate = token.accesstokenexpiraiondate
      state.refreshToken = token.refreshtoken
      state.refreshTokenExpiraionDate = token.refreshtokenexpiraiondate
      state.userEmail = token.useremail

      localStorage.setItem('authorization', state.authorization)
      localStorage.setItem('access-token-expiraion-date', state.accessTokenExpiraionDate)
      localStorage.setItem('refresh-token', state.refreshToken)
      localStorage.setItem('refresh-token-expiraion-date', state.refreshTokenExpiraionDate)
    },

    // 토큰 삭제
    DELETE_TOKEN(state) {
      state.authorization = null
      state.accessTokenExpiraionDate = null
      state.refreshToken = null
      state.refreshTokenExpiraionDate = null
      state.userEmail = null
    },

    // email 중복체크 결과 저장
    SET_DUPLICATE_CHECKED(state, result) {
      state.isDuplicateChecked = result
    },

    // 인증번호 확인 결과 저장
    SET_AUTHNUM_CHECKED(state, result) {
      result==='success' ? state.isAuthNumChecked = true : state.isAuthNumChecked = false
    },

    // 비밀번호 확인 결과 저장
    SET_PASSWORD_CHECKED(state, result) {
      state.isPasswordChecked = result
    },
  },

  // 범용적인 함수들. mutations에 정의한 함수를 actions에서 실행 가능.
  // 비동기 로직은 actions에서 정의.
  // dispatch를 통해 실행함.
  actions: {

    // 로그인
    login({ commit }, loginData) {
      axios.post(SERVER.URL + SERVER.ROUTES.login, loginData)
        .then(res => {
          commit('SET_TOKEN', res.headers)  // 토큰 저장
          commit('SET_PASSWORD_CHECKED', false)

          router.push({name: "Share"});
        })
        .catch(err => {
          if (err.response.status===401) {
            commit('SET_PASSWORD_CHECKED', true)
          }
        })
    },
    
    // 회원가입
    signup({ dispatch }, signupData) {
      axios.post(SERVER.URL + SERVER.ROUTES.signup, signupData)
        .then(() => {
          const loginData = {
            email: signupData.email,
            password: signupData.password 
          }
          dispatch('login', loginData)  // 회원가입 성공 시, 자동 로그인
        })
        .catch(err => console.error(err.response.data))
    },

    // 로그아웃
    logout({ getters, commit }) {
      axios.post(SERVER.URL + SERVER.ROUTES.logout, null, getters.config)
        .then(() => {
          commit('DELETE_TOKEN')  // state 에서도 삭제

          // Local Storage 에서도 삭제
          localStorage.removeItem('authorization');
          localStorage.removeItem('access-token-expiraion-date')
          localStorage.removeItem('refresh-token')
          localStorage.removeItem('refresh-token-expiraion-date')
          
          router.push({name: "Home"});
        })
        .catch(err => console.error(err.response.data))
    },

    // 이메일 중복확인
    checkEmailDuplicate({ commit, dispatch }, signupData) {
      axios.post(SERVER.URL + SERVER.ROUTES.email, signupData)
        .then(res => {
          
          // 사용 가능한 이메일 일 때,
          if (res.data['result'] === 'success') {
            commit('SET_DUPLICATE_CHECKED', true)
            dispatch('sendAuthNum', signupData)  // 사용 가능한 이메일이면, 바로 인증번호 이메일로 전송
          }

          // 이미 가입된 이메일 일때,
          else {
            commit('SET_DUPLICATE_CHECKED', false)
          }
        })
        .catch(err => console.error(err.response.data))
    },

    // 이메일로 인증번호 보내기
    sendAuthNum({ state }, signupData) {

      // 이메일 중복 검사가 확인되었을 때만 실행
      if (state.isDuplicateChecked) {
        axios.post(SERVER.URL + SERVER.ROUTES.authSend, signupData)
          .then().catch(err => console.error(err.response.data))
      }
    },

    // 인증번호 확인
    checkAuthNum({ commit }, signupData) {
      axios.post(SERVER.URL + SERVER.ROUTES.authCheck, signupData)
        .then(res => {
          commit('SET_AUTHNUM_CHECKED', res.data['result'])  // 인정번호 확인 결과 저장
        })
        .catch(err => console.error(err.response.data))
    },
  },
  modules: {}
});
