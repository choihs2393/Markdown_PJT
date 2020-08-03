<template>
  <div>
    <v-btn text @click="isLoginModal = !isLoginModal">LogIn</v-btn>

    <v-dialog v-model="isLoginModal" max-width="500px">
      <v-card class="elevation-12">
        <v-toolbar dark flat>
          <v-toolbar-title>LogIn</v-toolbar-title>
        </v-toolbar>
        <v-card-text>
          <v-form>
            <v-text-field
              label="ID"
              name="login"
              prepend-icon="mdi-account"
              type="text"
              v-model="loginData.email"
            ></v-text-field>

            <v-text-field
              label="Password"
              name="password"
              prepend-icon="mdi-lock"
              type="password"
              v-model="loginData.password"
            ></v-text-field>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="login(loginData)">Login</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { mapActions } from 'vuex';

export default {
  name: "LoginModal",

  data() {
    return {
      isLoginModal: false,
      loginData: {
        email: "",
        password: ""
      },
    }
  },

  methods: {
    ...mapActions(['login'])
  }
}
</script>

<style>

</style>