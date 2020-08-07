<template>
    <v-app-bar app elevate-on-scroll>
        <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
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
        <LogoutModal />
      </template>

      <v-switch
        v-model="$vuetify.theme.dark"
        hide-details
        label="Theme Dark"
      ></v-switch>

    </v-app-bar>
</template>

<script>
import { mapGetters } from 'vuex';

import LoginModal from "./LoginModal.vue"
import SignupModal from "./SignupModal.vue"
import LogoutModal from "./LogoutModal.vue"

export default {
    components: {
        LoginModal,
        SignupModal,
        LogoutModal
    },
    computed: {
        ...mapGetters(['isLoggedIn']),
    }
}
</script>

<style>

</style>