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

      <v-btn icon>
        <v-icon>mdi-bell</v-icon>
      </v-btn>

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
import { mapGetters } from 'vuex';

import LoginModal from "./modal/LoginModal.vue"
import SignupModal from "./modal/SignupModal.vue"
import LogoutModal from "./modal/LogoutModal.vue"
import MypageModal from "./modal/MypageModal.vue"

export default {
  name: 'NavBar',
  data() {
    return {
      // theme: this.$vuetify.theme.dark,
    }
  },
  components: {
    LoginModal,
    SignupModal,
    LogoutModal,
    MypageModal,
  },

  computed: {
      ...mapGetters(['isLoggedIn']),
  },

  updated(){
    var div = document.getElementById("compiledMarkdown");
    if(this.$vuetify.theme.dark == true)
      div.style.color = "white";
    else
      div.style.color = "black";

    if(!!this.$store.state.isShareMode == false) {
      this.$store.commit('SET_IS_DRAWER_SHARE', false)
      this.$store.commit('SET_IS_DRAWER', true)
    }
    else if(!!this.$store.state.isShareMode == true) {
      this.$store.commit('SET_IS_DRAWER_SHARE', true)
      this.$store.commit('SET_IS_DRAWER', false)
    }
  },

  methods: {
      decideSideBar() {
        // 폴더트리를 보여주는 사이드바를 열어준다.
        if(!!this.$store.state.isShareMode == false) {
          this.$store.commit('SET_IS_DRAWER', !this.$store.state.drawer)

        }
        // 그룹을 보여주는 사이드바를 열어준다.
        else if(!!this.$store.state.isShareMode == true) {
          this.$store.commit('SET_IS_DRAWER_SHARE', !this.$store.state.drawerShare)
        }
      },
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
}
</script>

<style>

</style>