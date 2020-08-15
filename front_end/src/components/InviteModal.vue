<template>
  <v-dialog v-model="$store.state.isInviteModal" max-width="400px">
    <v-card class="mx-auto">
      <v-toolbar color="rgb(255, 179, 102)" dark>
        <v-text-field
          autofocus
          v-model.lazy="search"
          append-icon="mdi-plus"
          label="Search Email"
          hide-details
          @click:append="inviteBandMember"
          @keyup="$store.state.noSuchMemberAlert=false"
        ></v-text-field>
      </v-toolbar>
      <v-alert
        class="ma-2"
        dense
        outlined
        persistent
        type="error"
        icon="mdi-cloud-alert"
        v-model="$store.state.noSuchMemberAlert"
      >가입된 회원이 아닙니다.</v-alert>
      <v-list>
        <v-list-item v-for="item in $store.state.workspaceMemberList" :key="item.no">
          <v-list-item-icon>
            <v-icon v-if="!(item.status)" color="pink">mdi-star</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title v-text="item.name"></v-list-item-title>
          </v-list-item-content>

          <v-list-item-group v-model="isOwner">
            <v-chip
              small
              color="orange"
              rounded
              filter
              dark
              v-if="item.status===2"
              style="margin-right: 1em"
            >수락 대기 중</v-chip>

            <v-btn 
            small 
            color="orange" 
            @click="kickOutBandMember(item.no)" 
            rounded 
            outlined 
            v-if="item.status!=0">내보내기</v-btn>
          </v-list-item-group>
        </v-list-item>
      </v-list>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapGetters } from "vuex";

// 소켓 관련 모듈
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";
import { integer } from 'vee-validate/dist/rules';

export default {
  name: "InviteModal",
  data() {
    return {
      connected: false,
      search: "",
      isOwner: false
    };
  },
  props: {
    workspaceNo: integer
  },
  mounted() {
    if (this.$store.state.isInviteModal) {
      if (
        this.$store.state.workspaceMemberList.find(
          element => element.no === this.$store.state.userInfo.no
        ).status === 0
      ) {
        isOwner = true;
      }
    }
  },
  methods: {
    inviteBandMember() {
      const findAccountList = {
        email: this.search
      };
      this.$store.dispatch("findAccountList", findAccountList);
      this.search = "";

      const serverURL = "http://localhost:8080/noteAPI/ws";
      // const serverURL = "http://i3b104.p.ssafy.io:80/noteAPI/ws";
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);

      // if (!this.connected) {
        this.stompClient.connect(
          { Authorization: this.$store.state.authorization },
          frame => {
            // 소켓 연결 성공
            this.connected = true;
            // console.log("[InviteModal] 소켓 연결 성공", frame);
            // console.log("[Send 내용]")

            // console.log("workspaceNo : " + this.workspaceNo)

            var map = {
              fromNo: this.$store.state.userInfo.no,
              fromEmail: this.$store.state.userInfo.email,
              fromName: this.$store.state.userInfo.name,
              toEmail: findAccountList.email,
              toNo: this.$store.state.newMemberInfo.no,
              groupName: this.$store.state.workspace,
              groupNo: this.workspaceNo
            }

            this.stompClient.send("/receive/" + this.$store.state.newMemberInfo.no, JSON.stringify(map));
            // console.log("========== 알림 송신 완료 ==========")
            // this.stompClient.send("/receive/" + this.$store.state.newMemberInfo.no, {
            //   'fromEmail': this.$store.state.userInfo.email,
            //   'fromName': this.$store.state.userInfo.name,
            //   'toEmail': findAccountList.email,
            //   'toNo': this.$store.state.newMemberInfo.no,
            //   'groupName': this.$store.state.workspace
            // });
          },
          error => {
            // 소켓 연결 실패
            // console.log("[InviteModal] 소켓 연결 실패", error);
            this.connected = false;
          }
        );
      // }
    },
    kickOutBandMember(no) {
      const kickOutBandMemberNo = {
        accountNo: no
      };
      this.$store.dispatch("kickOutBandMember", kickOutBandMemberNo);
    }
  }
};
</script>