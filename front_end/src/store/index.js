import Vue from "vue";
import Vuex from "vuex";

import axios from "axios";
import SERVER from "@/api/spring";
import { ipcRenderer, remote } from "electron";
import serverStartInput from "../markdown/serverStartInput.js";

Vue.use(Vuex);

export default new Vuex.Store({
  // data의 집합(중앙 관리할 모든 데이터 === 상태)
  state: {
    // auth_token
    authorization: localStorage.getItem("authorization"),
    accessTokenExpiraionDate: localStorage.getItem(
      "access-token-expiraion-date"
    ),
    refreshToken: localStorage.getItem("refresh-token"),
    refreshTokenExpiraionDate: localStorage.getItem(
      "refresh-token-expiraion-date"
    ),

    // user info
    userInfo: {
      email: "",
      name: "",
      group: [],
      status: [],
      no: ""
    },

    // selected workspace in server mode
    selectedBandInfo: {},
    workspaceMemberList: [],
    newMemberInfo: {
      email: "",
      name: "",
      status: "",
      no: ""
    },

    // note_file
    noteList: [],
    selectedNoteInfo: {},
    rightSelectedNoteInfo: {},

    // 파싱되는 데이터 저장.
    parseData: "",
    inputData: "",
    tempData: "",
    syncCheck: false,
    timer: "",

    // invite 데이터 저장
    inviteTempData: "",

    // 자동 저장되는 데이터 저장.
    storeTempData: "",
    storeSyncCheck: false,
    storeTimer: "",

    // 소켓 데이터 저장

    shareNoteSocket: "",
    shareGroupSocket: "",

    canInvite: true,

    // modal
    drawer: false,
    drawerShare: false,
    isLogoutModal: false,
    isMypageModal: false,
    isDeleteModal: false,
    isInviteModal: false,
    noSuchMemberAlert: false,
    alreadyMemberAlert: false,
    isRenameFileModal: false,
    isDeleteFileModal: false,

    // auth_check
    isDuplicateChecked: false,
    isAuthNumChecked: false,
    isPasswordChecked: false,
    isModifyChecked: false,

    // server_check
    isShareMode: false,

    // theme
    theme: "",

    // add Readme.md in file list
    addReadme: "",

    savedTime: ""
  },

  // state를 (가공해서)가져올 함수들. === computed
  getters: {
    isLoggedIn: state => !!state.authorization,
    status: state => state.userInfo.status,
    // occupiedName: state => state.noteList[state.noteList.findIndex(item => item._id===state.selectedNoteInfo._id)].occupiedName,
    selectedNoteInfo: state => state.selectedNoteInfo,
    isShareMode: state => state.isShareMode,
    selectedBandInfo: state => state.selectedBandInfo,
    savedTime: state => state.savedTime,
    noteList: state => state.noteList
    // getWorkspaceMemberList: state => {
    //   return state.workspaceMemberList
    // },

    // inputData: state=> state.inputData
  },

  // state를 변경하는 함수들(mutations에 작성되지 않은 state 변경 코드는 모두 동작하지 않음.)
  // 모든 mutation 함수들은 동기적으로 동작하는 코드.all
  // commit 을 통해 실행함.
  // mutations은 첫 번째 인자로 state를 받아야함.
  mutations: {
    //invite 처리를 위한 데이터
    setInviteTempData(state, param) {
      state.inviteTempData = param;
    },

    //소켓처리를 위한 데이터
    setShareNoteSocket(state, param) {
      state.shareNoteSocket = param;
    },
    setShareGroupSocket(state, param) {
      state.shareGroupSocket = param;
    },
    //자동처리를 위한 스케쥴링 데이터
    setStoreTempData(state, param) {
      state.storeTempData = param;
    },
    setStoreSyncCheck(state, param) {
      state.storeSyncCheck = param;
    },
    setStoreTimer(state, param) {
      state.storeTimer = param;
    },
    //모아서처리를 위한 스케쥴링 데이터
    setTempData(state, param) {
      state.tempData = param;
    },
    setSyncCheck(state, param) {
      state.syncCheck = param;
    },
    setTimer(state, param) {
      state.timer = param;
    },
    //파싱되는 데이터 저장
    setParseData(state, param) {
      state.parseData = param;
    },
    setInputData(state, param) {
      state.inputData = param;
    },
    // 토큰 저장
    SET_TOKEN(state, token) {
      if (token.authorization) {
        state.authorization = token.authorization;
        localStorage.setItem("authorization", state.authorization);
      }
      if (token.accesstokenexpiraiondate) {
        state.accessTokenExpiraionDate = token.accesstokenexpiraiondate;
        localStorage.setItem(
          "access-token-expiraion-date",
          state.accessTokenExpiraionDate
        );
      }
      if (token.refreshtoken) {
        state.refreshToken = token.refreshtoken;
        localStorage.setItem("refresh-token", state.refreshToken);
      }
      if (token.refreshtokenexpiraiondate) {
        state.refreshTokenExpiraionDate = token.refreshtokenexpiraiondate;
        localStorage.setItem(
          "refresh-token-expiraion-date",
          state.refreshTokenExpiraionDate
        );
      }
    },

    // 토큰 삭제
    DELETE_TOKEN(state) {
      state.authorization = null;
      state.accessTokenExpiraionDate = null;
      state.refreshToken = null;
      state.refreshTokenExpiraionDate = null;
      state.userInfo.name = null;
    },

    // email 중복체크 결과 저장
    SET_DUPLICATE_CHECKED(state, result) {
      state.isDuplicateChecked = result;
    },

    // 인증번호 확인 결과 저장
    SET_AUTHNUM_CHECKED(state, result) {
      result === "success"
        ? (state.isAuthNumChecked = true)
        : (state.isAuthNumChecked = false);
    },

    // 비밀번호 확인 결과 저장
    SET_PASSWORD_CHECKED(state, result) {
      state.isPasswordChecked = result;
    },

    // 회원정보 수정 결과 저장
    SET_MODIFY_RESULT(state, result) {
      state.isModifyChecked = result;
    },

    // 초기 회원정보 저장
    SET_INIT_USER_INFO(state, info) {
      state.userInfo = info;
    },

    // 워크스페이스 저장
    SET_WORKSPACES(state, result) {
      state.userInfo.group.push(result);
    },

    // 선택한 워크스페이스
    SELECTED_WORKSPACE(state, bandInfo) {
      state.selectedBandInfo = bandInfo;
    },

    // 워크스페이스 삭제
    DELETE_WORKSPACE(state, param) {
      const idx = state.userInfo.group.findIndex(
        item => item.no === param.bandNo
      );
      state.userInfo.group.splice(idx, 1);
    },

    // 워크스페이스 이름 변경
    UPDATE_WORKSPACE(state, param) {
      const idx = state.userInfo.group.findIndex(
        item => item.no == param.bandNo
      );
      state.userInfo.group[idx].name = param.newBandName;
    },

    //현재 WORKSPACE 내의 MEMBER LIST 가져오기
    SHOW_GROUP_MEMBERS(state, result) {
      state.workspaceMemberList = [];
      for (let i = 0; i < result.length; i += 1) {
        if (typeof result[i] === "object") {
          try {
            state.workspaceMemberList[i] = JSON.parse(
              JSON.stringify(result[i])
            );
          } catch (e) {
            console.error(e);
          }
        }
      }
    },

    GET_NEW_MEMBER_INFO(state, result) {
      state.workspaceMemberList.push(result);
    },

    REMOVE_DELETE_MEMBER_INFO(state, result) {
      const idx = state.workspaceMemberList.findIndex(function(item) {
        return item.no === result.accountNo;
      }); // findIndex = find + indexOf
      state.workspaceMemberList.splice(idx, 1);
    },

    SET_IS_RENAME_FILE_MODAL(state, payload) {
      state.isRenameFileModal = payload;
    },

    SET_IS_DELETE_FILE_MODAL(state, payload) {
      state.isDeleteFileModal = payload;
    },

    // 초기 noteList 정보 저장
    INIT_NOTE_LIST(state, noteList) {
      state.noteList = noteList;
    },

    // FileList 에 File 추가하기
    SET_NOTE(state, noteInfo) {
      state.noteList.push(noteInfo);
    },

    SELECTED_NOTE(state, noteInfo) {
      state.selectedNoteInfo = noteInfo;
    },

    RIGHT_SELECTED_NOTE(state, noteInfo) {
      state.rightSelectedNoteInfo = noteInfo;
    },

    // File 이름 변경
    RENAME_NOTE_SUBJECT(state, noteInfo) {
      const idx = state.noteList.findIndex(item => item._id == noteInfo.noteNo);
      state.noteList[idx].subject = noteInfo.subject;
    },

    // FileList 에 File 삭제하기
    DELETE_NOTE(state, noteNo) {
      const idx = state.noteList.findIndex(item => item._id === noteNo);
      state.noteList.splice(idx, 1);
    },

    // File 내용 추가
    SET_NOTE_CONTENT(state, noteInfo) {
      const idx = state.noteList.findIndex(item => item._id == noteInfo.noteNo);
      state.noteList[idx].content = noteInfo.content;
    },

    // modal 관련 로직
    SET_IS_SHARE(state, result) {
      state.isShareMode = result;
      // ipcRenderer.send("isShareMode", result);
    },
    SET_IS_DRAWER(state, result) {
      state.drawer = result;
    },
    SET_IS_DRAWER_SHARE(state, result) {
      state.drawerShare = result;
    },
    SET_IS_INVITE_MODAL(state, result) {
      (state.isInviteModal = result),
        (state.noSuchMemberAlert = false),
        (state.alreadyMemberAlert = false);
    },
    SET_IS_MYPAGE_MODAL(state, result) {
      state.isMypageModal = result;
    }
  },

  // 범용적인 함수들. mutations에 정의한 함수를 actions에서 실행 가능.
  // 비동기 로직은 actions에서 정의.
  // dispatch를 통해 실행함.
  actions: {
    // 로그인
    login({ state, commit, dispatch }, loginData) {
      axios
        .post(SERVER.URL + SERVER.ROUTES.login, loginData)
        .then(res => {
          commit("SET_TOKEN", res.headers); // 토큰 저장
          commit("SET_PASSWORD_CHECKED", false);

          /* 무성 추가 부분 -> 로그인을 한 후에도 서버요청해서 이름 정보 가져와야 할 듯 싶어서 추가 */
          /* 로그인을 해도 소망이님이 뜨고, 로그인상태로 앱을 껏다 켜야 yb님이라고 뜸. 애초에 로그인 후에 바로 가져오는 게 맞을 것 같습니다. */
          dispatch("initUserInfo");
          setTimeout(function() {
            commit("SET_IS_SHARE", true);
          }, 70);

          state.noteList = [];
          state.selectedNoteInfo = {};
          let win = remote.BrowserWindow.getFocusedWindow();
          win.webContents.send("serverInit", serverStartInput);
          let fileDataObject = { openedFileData: "", absoluteFilePath: "" };
          ipcRenderer.send("mainping", fileDataObject);
        })
        .catch(err => {
          console.error(err.response.data);
          if (err.response.status === 401) {
            commit("SET_PASSWORD_CHECKED", true);
          }
        });
    },

    // 회원가입
    signup({ dispatch }, signupData) {
      axios
        .post(SERVER.URL + SERVER.ROUTES.signup, signupData)
        .then(() => {
          const loginData = {
            email: signupData.email,
            password: signupData.password
          };
          dispatch("login", loginData); // 회원가입 성공 시, 자동 로그인
        })
        .catch(err => console.error(err.response.data));
    },

    // 로그아웃
    logout({ commit }) {
      axios
        .post(SERVER.URL + SERVER.ROUTES.logout)
        .then(() => {
          commit("DELETE_TOKEN"); // state 에서도 삭제

          // Local Storage 에서도 삭제
          localStorage.removeItem("authorization");
          localStorage.removeItem("access-token-expiraion-date");
          localStorage.removeItem("refresh-token");
          localStorage.removeItem("refresh-token-expiraion-date");
          // localStorage.removeItem('email')

          /* 서버모드로 켜놓고, 로그아웃 하면 서버모드가 유지됩니다. */
          /* 로그아웃시 로컬모드만 사용할 수 있도록 false로 고정해놨습니다. */
          commit("SET_IS_SHARE", false);
          commit("INIT_NOTE_LIST", []);

          let win = remote.BrowserWindow.getFocusedWindow();
          win.webContents.send("contentReset", "msg");
        })
        .catch(err => console.error(err.response.data));
    },

    // 이메일 중복확인
    checkEmailDuplicate({ commit, dispatch }, signupData) {
      axios
        .post(SERVER.URL + SERVER.ROUTES.email, signupData)
        .then(res => {
          // 사용 가능한 이메일 일 때,
          if (res.data["result"] === "success") {
            commit("SET_DUPLICATE_CHECKED", true);
            dispatch("sendAuthNum", signupData); // 사용 가능한 이메일이면, 바로 인증번호 이메일로 전송
          }

          // 이미 가입된 이메일 일때,
          else {
            commit("SET_DUPLICATE_CHECKED", false);
          }
        })
        .catch(err => console.error(err.response.data));
    },

    // 이메일로 인증번호 보내기
    sendAuthNum({ state }, signupData) {
      // 이메일 중복 검사가 확인되었을 때만 실행
      if (state.isDuplicateChecked) {
        axios
          .post(SERVER.URL + SERVER.ROUTES.authSend, signupData)
          .then()
          .catch(err => console.error(err.response.data));
      }
    },

    // 인증번호 확인
    checkAuthNum({ commit }, signupData) {
      axios
        .post(SERVER.URL + SERVER.ROUTES.authCheck, signupData)
        .then(res => {
          commit("SET_AUTHNUM_CHECKED", res.data["result"]); // 인정번호 확인 결과 저장
        })
        .catch(err => console.error(err.response.data));
    },

    // 회원정보 수정
    updateUserInfo({ commit, dispatch }, userInfo) {
      axios
        .post(SERVER.URL + SERVER.ROUTES.modify, userInfo, {
          headers: { email: userInfo.email }
        })
        .then(res => {
          if (res.data["result"] === "success") {
            commit("SET_MODIFY_RESULT", false);
            dispatch("initUserInfo");
          } else {
            commit("SET_MODIFY_RESULT", true);
          }
        })
        .catch(err => console.error(err.response.data));
    },

    // 회원탈퇴
    deleteAccount({ commit, dispatch }, userInfo) {
      const map = {
        accountNo: userInfo.no,
        email: userInfo.email,
        password: userInfo.password
      };

      axios
        .post(SERVER.URL + SERVER.ROUTES.delete, map, {
          headers: { email: userInfo.email }
        })
        .then(res => {
          if (res.data["result"] === "success") {
            commit("SET_MODIFY_RESULT", false);
            commit("SET_IS_MYPAGE_MODAL", false);
            dispatch("logout");
          } else {
            commit("SET_MODIFY_RESULT", true);
          }
        })
        .catch(err => console.error(err.response.data));
    },

    // 초기 회원정보 세팅
    initUserInfo({ commit }) {
      axios
        .post(SERVER.URL + SERVER.ROUTES.onServerInit)
        .then(res => {
          if (res.data["result"] === "success") {
            commit("SET_INIT_USER_INFO", res.data.map);
          }
        })
        .catch(err => console.error(err.response.data));
    },

    // 워크스페이스 생성
    async createWorkspace({ state, commit }, newBandName) {
      const info = {
        bandName: newBandName,
        accountNo: state.userInfo.no,
        bandMasterName: state.userInfo.name
      };

      await axios
        .post(SERVER.URL + SERVER.ROUTES.createWorkspace, info, {
          headers: { email: state.userInfo.email }
        })
        .then(res => {
          commit("SET_WORKSPACES", res.data.map.band);
          commit("SELECTED_WORKSPACE", res.data.map.band);
        })
        .catch(err => {});
    },

    // 워크스페이스 제거
    deleteWorkspace({ state, commit }, deleteWorkspace) {
      axios
        .post(SERVER.URL + SERVER.ROUTES.deleteWorkspace, deleteWorkspace, {
          headers: { email: state.userInfo.email }
        })
        .then(res => {
          if (res.data.result == "success") {
            commit("DELETE_WORKSPACE", deleteWorkspace);
          } else if (res.data.result == "fail") {
          }
        })
        .catch(err => {});
    },

    // 워크스페이스명 변경
    renameWorkspace({ state, commit }, renameWorkspace) {
      axios
        .post(SERVER.URL + SERVER.ROUTES.renameWorkspace, renameWorkspace, {
          headers: { email: state.userInfo.email }
        })
        .then(res => {
          if (res.data.result == "success") {
            commit("UPDATE_WORKSPACE", renameWorkspace);
          }
        });
    },
    // 워크스페이스 멤버 불러오기
    showGroupMembers({ state, commit }, showGroupMembers) {
      axios
        .post(SERVER.URL + SERVER.ROUTES.getBandMember, showGroupMembers, {
          headers: { email: state.userInfo.email }
        })
        .then(res => {
          commit("SHOW_GROUP_MEMBERS", res.data.map.bandMemberList);
          state.isInviteModal = !state.isInviteModal;
        });
    },

    // 가입된 회원인지 확인
    findAccountList({ state, dispatch }, email) {
      state.alreadyMemberAlert = false;
      state.noSuchMemberAlert = false;
      state.canInvite = true;
      var map = {
        email: email
      };
      axios
        .post(SERVER.URL + SERVER.ROUTES.findAccountList, map, {
          headers: { email: state.userInfo.email }
        })
        .then(res => {
          if (res.data.result === "success") {
            state.newMemberInfo.no = res.data.map.primitiveAccountList[0].no; // 초대받을 사람의 account_no를 보관.

            const inviteBandMember = {
              bandNo: state.selectedBandInfo.no,
              email: email,
              masterNo: state.selectedBandInfo.master
            };
            dispatch("inviteBandMember", inviteBandMember);
          } else {
            state.noSuchMemberAlert = true;
            state.canInvite = false;
          }
        });
    },

    // 워크스페이스에 멤버 초대하기
    inviteBandMember({ state, commit }, inviteBandMember) {
      axios
        .post(SERVER.URL + SERVER.ROUTES.inviteBandMember, inviteBandMember, {
          headers: { email: state.userInfo.email }
        })
        .then(res => {
          if (res.data.result == "success") {
            commit("GET_NEW_MEMBER_INFO", res.data.map.bandMember);
          } else {
            state.alreadyMemberAlert = true;
            state.canInvite = false;
          }
        });
    },

    // 워크스페이스 초대 수락
    acceptInvite({ state }, info) {
      axios
        .post(SERVER.URL + SERVER.ROUTES.acceptInvite, info, {
          headers: { email: state.userInfo.email }
        })
        .then(res => {});
    },

    // 워크스페이스 초대 거절
    declineInvite({ state }, info) {
      axios
        .post(SERVER.URL + SERVER.ROUTES.declineInvite, info, {
          headers: { email: state.userInfo.email }
        })
        .then(res => {});
    },

    //워크스페이스 멤버 내보내기
    kickOutBandMember({ state, commit }, accountNo) {
      const kickOutBandMember = {
        accountNo: accountNo,
        bandNo: state.selectedBandInfo.no,
        masterNo: state.userInfo.no
      };
      axios
        .post(SERVER.URL + SERVER.ROUTES.deleteMember, kickOutBandMember, {
          headers: { email: state.userInfo.email }
        })
        .then(res => {
          if (res.data.result == "success") {
            commit("REMOVE_DELETE_MEMBER_INFO", kickOutBandMember);
          }
        });
    },

    // noteList 조회
    getNoteList({ state, commit }, bandInfo) {
      if (bandInfo.no) {
        const info = {
          accountNo: state.userInfo.no,
          bandNo: bandInfo.no
        };
        axios
          .post(SERVER.URL + SERVER.ROUTES.noteList, info)
          .then(res => {
            if (res.data.result === "success") {
              commit("INIT_NOTE_LIST", res.data.map.noteDetailDTOList);
            } else if (res.data.result === "empty") {
              commit("INIT_NOTE_LIST", []);
            }
          })
          .catch(err => console.error(err.response.data));
      } else {
        commit("INIT_NOTE_LIST", []);
      }
    },

    // note 추가
    createNote({ state, commit, dispatch }, subject) {
      if (subject) {
        const info = {
          accountNo: state.userInfo.no,
          bandNo: state.selectedBandInfo.no,
          subject: subject
        };
        axios
          .post(SERVER.URL + SERVER.ROUTES.createNote, info)
          .then(res => {
            info.no = res.data.map.no;
            info.content = "";
            commit("SET_NOTE", info);
            dispatch("getNoteList", state.selectedBandInfo);
          })
          .catch(err => console.error(err.response.data));
      }
    },

    // note 삭제
    deleteNote({ state, commit, dispatch }, noteInfo) {
      const info = {
        accountNo: state.userInfo.no,
        bandNo: state.selectedBandInfo.no,
        noteNo: noteInfo._id
      };
      axios
        .post(SERVER.URL + SERVER.ROUTES.deleteNote, info)
        .then(() => {
          commit("DELETE_NOTE", info.noteNo);
          // commit('SELECTED_WORKSPACE', {})
          dispatch("getNoteList", state.selectedBandInfo);
        })
        .catch(err => console.error(err.response.data));
    },

    // note 이름 변경
    renameNote({ state, commit, dispatch }, newSubject) {
      const info = {
        accountNo: state.userInfo.no,
        bandNo: state.selectedBandInfo.no,
        subject: newSubject
      };
      if (state.rightSelectedNoteInfo === state.selectedNoteInfo) {
        info.noteNo = state.selectedNoteInfo._id;
      } else {
        info.noteNo = state.rightSelectedNoteInfo._id;
      }
      axios
        .post(SERVER.URL + SERVER.ROUTES.renameNote, info)
        .then(() => {
          commit("RENAME_NOTE_SUBJECT", info);
          dispatch("getNoteList", state.selectedBandInfo);
        })
        .catch(err => console.error(err.response.data));
    },

    // note 열기
    getNote({ state, commit }, noteInfo) {
      const info = {
        accountNo: state.userInfo.no,
        bandNo: state.selectedBandInfo.no,
        noteNo: noteInfo._id
      };
      axios
        .post(SERVER.URL + SERVER.ROUTES.getNote, info)
        .then(res => {
          commit("SELECTED_NOTE", res.data.map.content);
          const win = remote.BrowserWindow.getFocusedWindow();
          if (res.data.result === "success") {
            win.webContents.send(
              "getNote",
              res.data.map.content,
              info.accountNo
            );
            // win.webContents.send('getNote', res.data.map.content, state.noteList.find(item => item._id === info.noteNo).accountNo)
            state.storeTimer = "";
            document.getElementById("serverFileName").style.display = "inline";
          } else if (res.data.result === "empty") {
            win.webContents.send("getNote", "");
          }
        })
        .catch(err => console.error(err.response.data));
    },

    // note 저장
    saveNote({ state, commit }, content) {
      const info = {
        accountNo: state.userInfo.no,
        bandNo: state.selectedBandInfo.no,
        noteNo: state.selectedNoteInfo._id,
        subject: state.selectedNoteInfo.subject,
        content: content,
        occupiedNo:
          state.noteList[
            state.noteList.findIndex(
              item => item._id === state.selectedNoteInfo._id
            )
          ].occupiedNo, // 점유자의 account_no
        occupiedName: state.userInfo.name // 점유자의 account_name
      };
      axios
        .post(SERVER.URL + SERVER.ROUTES.saveNote, info)
        .then(() => {
          commit("SET_NOTE_CONTENT", info);
        })
        .catch(err => console.error(err.response.data));

      var d = new Date(),
        month = "" + (d.getMonth() + 1),
        day = "" + d.getDate(),
        year = d.getFullYear(),
        hour = ("0" + d.getHours()).slice(-2),
        min = ("0" + d.getMinutes()).slice(-2),
        sec = ("0" + d.getSeconds()).slice(-2);

      if (month.length < 2) month = "0" + month;
      if (day.length < 2) day = "0" + day;

      state.savedTime =
        "저장 일시 : " +
        [year, month, day].join("-") +
        " " +
        [hour, min, sec].join(":");
    }
  },
  modules: {}
});
