<template>
  <v-app-bar app elevate-on-scroll>
    <!-- <v-app-bar-nav-icon @click="$store.state.drawer = !$store.state.drawer"></v-app-bar-nav-icon> -->
    <v-app-bar-nav-icon @click="decideSideBar()"></v-app-bar-nav-icon>
    <v-toolbar-title class="mr-3">소망이 노트</v-toolbar-title>
    <v-spacer></v-spacer>

    <!-- 로그인 전 화면의 상단바 -->
    <template v-if="!isLoggedIn">
      <LoginModal />
      <SignupModal />
      <v-switch v-model="$vuetify.theme.dark" hide-details label="Dark"></v-switch>
    </template>

    <!-- 로그인 후 화면의 상단바 -->
    <template v-if="isLoggedIn">
      <v-toolbar-title class="mr-2">{{ $store.state.userInfo.name }}님</v-toolbar-title>

      <v-dialog v-model="invitationDialog" max-width="600px">
        <template v-slot:activator="{ on, attrs }">
          <v-btn icon v-bind="attrs" v-on="on" @click="bellClicked">
            <v-icon>mdi-bell</v-icon>
          </v-btn>
        </template>
        <v-card>
          <v-card-title>
            <span>초대 현황</span>
          </v-card-title>

          <v-list-item>
            <v-list-item-group>
              <v-list-item v-for="group in status" :key="group.no">
                <v-list-item-avatar color="grey">
                  <v-img src="https://cdn.vuetifyjs.com/images/lists/1.jpg"></v-img>
                </v-list-item-avatar>
                <v-list-item-content>
                  <!-- <v-list-item-title class="headline">Our Changing Planet</v-list-item-title>
                  <v-list-item-subtitle>by Kurt Wagner</v-list-item-subtitle>-->
                  {{ group.bandMasterName }}님이 회원님을 {{ group.name}}에 초대했습니다.
                </v-list-item-content>
                <v-list-item-icon>
                  <v-btn @click="send(1, group.no)">수락</v-btn>
                  <v-btn @click="send(0, group.no)">거절</v-btn>
                </v-list-item-icon>
              </v-list-item>
            </v-list-item-group>
          </v-list-item>

          <v-card-text></v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="invitationDialog = false">Close</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <v-menu left bottom>
        <template v-slot:activator="{ on, attrs }">
          <v-btn icon v-bind="attrs" v-on="on">
            <v-icon>fas fa-ellipsis-h</v-icon>
          </v-btn>
        </template>

        <v-list>
          <v-list-item>
            <v-switch class="mt-0" v-model="$vuetify.theme.dark" hide-details label="Dark"></v-switch>
          </v-list-item>
          <v-list-item @click="$store.state.isMypageModal = !$store.state.isMypageModal">
            <v-list-item-title>MyPage</v-list-item-title>
          </v-list-item>
          <v-list-item @click="$store.state.isLogoutModal = !$store.state.isLogoutModal">
            <v-list-item-title>Logout</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
      <LogoutModal />
      <MypageModal />
      <v-switch class="mt-0" v-model="$store.state.isShareMode" hide-details label="Share"></v-switch>
    </template>
  </v-app-bar>
</template>

<script>
import { mapGetters } from "vuex";

import LoginModal from "./modal/LoginModal.vue";
import SignupModal from "./modal/SignupModal.vue";
import LogoutModal from "./modal/LogoutModal.vue";
import MypageModal from "./modal/MypageModal.vue";

// 소켓 관련 모듈
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";

export default {
  name: "NavBar",
  data() {
    return {
      invitationDialog: false,
      connected: false
      // status: this.$store.state.userInfo.status
      // theme: this.$vuetify.theme.dark,
    };
  },
  components: {
    LoginModal,
    SignupModal,
    LogoutModal,
    MypageModal
  },

  computed: {
    ...mapGetters(["isLoggedIn", "status"])
  },

  created() {
    console.log("NavBar.vue -> created() 호출됨.");
  },

  updated() {
    var div = document.getElementById("compiledMarkdown");
    if (this.$vuetify.theme.dark == true) div.style.color = "white";
    else div.style.color = "black";

    if (!!this.$store.state.isShareMode == false) {
      this.$store.commit("SET_IS_DRAWER_SHARE", false);
      this.$store.commit("SET_IS_DRAWER", true);
    } else if (!!this.$store.state.isShareMode == true) {
      this.$store.commit("SET_IS_DRAWER_SHARE", true);
      this.$store.commit("SET_IS_DRAWER", false);
    }
  },

  methods: {
    bellClicked() {
      console.log("bell누름!!!!!!!!!!!!!!!!!!!!!!");
      if (this.$store.getters.isLoggedIn) {
        console.log("로그인된 상태");
        this.socketConnect();
      } else {
        console.log("로그아웃된 상태");
        this.socketDisconnect();
      }
      console.log("@@@@초대 목록 : ", this.$store.state.userInfo.status);
    },
    decideSideBar() {
      // 폴더트리를 보여주는 사이드바를 열어준다.
      if (!!this.$store.state.isShareMode == false) {
        this.$store.commit("SET_IS_DRAWER", !this.$store.state.drawer);
      }
      // 그룹을 보여주는 사이드바를 열어준다.
      else if (!!this.$store.state.isShareMode == true) {
        this.$store.commit(
          "SET_IS_DRAWER_SHARE",
          !this.$store.state.drawerShare
        );
      }
    },
    socketConnect() {
      // const serverURL = "http://localhost:8080/noteAPI/ws";
      const serverURL = "http://i3b104.p.ssafy.io:80/noteAPI/ws";
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);

      if (!this.connected) {
        this.stompClient.connect(
          { Authorization: this.$store.state.authorization },
          frame => {
            // 소켓 연결 성공
            this.connected = true;
            console.log("소켓 연결 성공", frame);
            this.stompClient.subscribe(
              "/send/" + this.$store.state.userInfo.no,
              res => {
                console.log(">>> 소켓으로 받은 메세지", res);
                this.$store.userInfo.status.push({
                  no: "",
                  name: "groupName",
                  master: "",
                  bandMasterName: "fromName"
                });
              }
            );
          },
          error => {
            // 소켓 연결 실패
            console.log("소켓 연결 실패", error);
            this.connected = false;
          }
        );
      }
    },
    socketDisconnect() {
      this.stompClient.disconnect();
    },
    send(flag, groupNo) {
      // 수락
      if (flag == 1) {
        const info = {
          accountNo: this.$store.state.userInfo.no,
          bandNo: groupNo
        };
        console.log("accountNo : " + this.$store.state.userInfo.no);
        console.log("bandNo : " + groupNo);
        this.$store.dispatch("acceptInvite", info);

        var idx = this.$store.state.userInfo.status.findIndex(
          element => element.no == groupNo
        );
        this.$store.state.userInfo.status.splice(idx, 1);
      }
      // 거절
      else if (flag == 0) {
        const info = {
          accountNo: this.$store.state.userInfo.no,
          bandNo: groupNo
        };
        this.$store.dispatch("declineInvite", info);

        var idx = this.$store.state.userInfo.status.findIndex(
          element => element.no == groupNo
        );
        this.$store.state.userInfo.status.splice(idx, 1);
      }
    }
    // changeLocalShare() {
    //   if(!!this.$store.state.isShareMode == false) {
    //     this.$store.state.drawerShare = false
    //     this.$store.state.drawer = true
    //   }
    //   else if(!!this.$store.state.isShareMode == true) {
    //     this.$store.state.drawer = false
    //     this.$store.state.drawerShare = true
    //   }
    // }
  }
};
</script>

<style>
</style>