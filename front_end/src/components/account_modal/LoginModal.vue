<template>
  <div>
    <v-btn text @click="isLoginModal = !isLoginModal">LogIn</v-btn>

    <v-dialog v-model="isLoginModal" persistent max-width="500px">
      <ValidationObserver ref="observer">
        <v-card class="elevation-12">
          <v-toolbar dark flat>
            <v-toolbar-title>LogIn</v-toolbar-title>
          </v-toolbar>
          <v-card-text>
            <v-form>
              <ValidationProvider
                mode="eager" v-slot="{ errors }" name="Email" rules="required|email">
                <v-text-field
                  v-model="loginData.email"
                  :error-messages="errors"
                  label="E-mail"
                  required
                  type="email"
                  prepend-icon="mdi-account"
                ></v-text-field>
              </ValidationProvider>
              <ValidationProvider
                mode="eager" v-slot="{ errors }" name="Password" rules="required">
                <v-text-field
                  v-model="loginData.password"
                  :error-messages="errors"
                  label="Password"
                  name="password"
                  type="password"
                  prepend-icon="mdi-lock"
                  @keyup.enter="login(loginData), submit()"
                ></v-text-field>
              </ValidationProvider>
              <v-alert dense outlined type="error" v-if="isPasswordChecked">
                이메일과 비밀번호가 맞지 않습니다.
              </v-alert>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="primary" @click="login(loginData), submit()">Login</v-btn>
            <v-btn @click="close()">Close</v-btn>
          </v-card-actions>
        </v-card>
      </ValidationObserver>
    </v-dialog>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex';

import { ValidationObserver, ValidationProvider, setInteractionMode, extend } from 'vee-validate'
import { required, email } from 'vee-validate/dist/rules';
import { remote, ipcRenderer } from "electron";
import serverStartInput from "../../markdown/serverStartInput.js";

setInteractionMode('eager')

extend('required', {
  ...required,
  message: '{_field_} 값은 반드시 입력해야 합니다.',
})

extend('email', {
  ...email,
  message: '{_field_} 형식이 아닙니다.',
})

export default {
  name: "LoginModal",

  components: {
    ValidationProvider,
    ValidationObserver,
  },

  computed: {
    ...mapState([
      'isPasswordChecked',
    ]),
  },

  data() {
    return {
      isLoginModal: false,
      // isPasswordAlert: false,

      loginData: {
        email: '',
        password: '',
      },
    }
  },

  methods: {
    ...mapActions(['login']),
    changeMode() {
      let win = remote.BrowserWindow.getFocusedWindow();
      console.log('test');
      let fileDataObject = {'openedFileData': '', 'absoluteFilePath': ''};
        if (!!this.$store.state.isShareMode == false) {
        this.$store.commit("SET_IS_DRAWER_SHARE", false);
        this.$store.commit("SET_IS_DRAWER", true);
        fileDataObject = {'openedFileData': serverStartInput, 'absoluteFilePath': ''};
        
      } else if (!!this.$store.state.isShareMode == true) {
        this.$store.commit("SET_IS_DRAWER_SHARE", true);
        this.$store.commit("SET_IS_DRAWER", false);
        fileDataObject = {'openedFileData': '', 'absoluteFilePath': ''};
        //data.input = serverStartInput.data;
      }
        win.webContents.send("ping", fileDataObject);
        ipcRenderer.send("mainping", fileDataObject);
    },
    close() {
      this.loginData = {
        email: '',
        password: '',
      }
      this.$refs.observer.reset()
      this.isLoginModal = false
      this.$store.state.isPasswordChecked = false
    },

    submit() {
      this.$refs.observer.validate()
    },
  }
}
</script>

<style>

</style>