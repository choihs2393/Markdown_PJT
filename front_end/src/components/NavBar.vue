<template>
  <v-app-bar app elevate-on-scroll>
    <v-app-bar-nav-icon @click="$store.state.drawer = !$store.state.drawer"></v-app-bar-nav-icon>
    <v-toolbar-title>Markdown</v-toolbar-title>
    <v-spacer></v-spacer>
    
    <!-- 로그인 전 화면의 상단바 -->
    <template v-if="!isLoggedIn">
      <LoginModal />
      <SignupModal />
    </template>

    <!-- 로그인 후 화면의 상단바 -->
    <template v-if="isLoggedIn">
      <div>
        <span>{{ $store.state.userEmail }}님</span>
      </div>

      <v-menu
        left
        bottom
      >
        <template v-slot:activator="{ on, attrs }">
          <v-btn
            icon
            v-bind="attrs"
            v-on="on"
          >
            <v-icon>fas fa-ellipsis-h</v-icon>
          </v-btn>
        </template>

        <v-list>
          <v-list-item>
              <v-switch
                v-model="$vuetify.theme.dark"
                label="Dark"
              ></v-switch>
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
    </template>


  </v-app-bar>
</template>

<script>
import { mapGetters } from 'vuex';

import LoginModal from "./LoginModal.vue"
import SignupModal from "./SignupModal.vue"
import LogoutModal from "./LogoutModal.vue"
import MypageModal from "./MypageModal.vue"

export default {
  name: 'NavBar',

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
  },
  
  data() {
    return {
      theme: this.$vuetify.theme.dark,
    }
  }
}
</script>

<style>
  .theme--light.v-list-item:not(.v-list-item--active) {
      color: #615f75 !important;
      font-size:0.5em;
  }
  .theme--light.v-list-item:not(.v-list-item--active):not(.v-list-item--disabled):hover {
      color: rgb(214, 198, 219) !important;
  }
</style>