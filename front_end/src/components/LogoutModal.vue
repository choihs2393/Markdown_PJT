<template>
  <div>
    <!-- <v-btn text @click="isLogoutModal = !isLogoutModal">logout</v-btn> -->

    <v-dialog v-model="$store.state.isLogoutModal" max-width="500px">
      <v-card class="elevation-12 custom-selector">
        <v-toolbar dark flat>
          <v-toolbar-title>Logout</v-toolbar-title>
        </v-toolbar>
        <v-card-text>
          Are you sure you want to log out?
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="logout(), $store.state.isLogoutModal = !$store.state.isLogoutModal">Logout</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { mapActions } from 'vuex';

export default {
  name: "LogoutModal",
  data() {
    return {
      isLogoutModal: false
    }
  },
  methods: {
    ...mapActions(['logout'])
  }
};
</script>

<style>
.v-card .v-card__text {
  margin-top: 20px;
  font-size: 20px;
  text-align: center;
}
</style>