<template>
  <div>
    <v-dialog v-model="$store.state.isMypageModal" persistent max-width="500px">
      <ValidationObserver ref="observer">
        <v-card class="elevation-12">
          <v-toolbar dark flat>
            <v-toolbar-title class="ml-4">MyPage</v-toolbar-title>
          </v-toolbar>
          <v-card-text>
            <v-form class="ma-4">
              <!-- <ValidationProvider
                mode="eager" v-slot="{ errors }" name="Email"
                rules="required|email">
                <v-text-field
                  v-model="userInfo.email"
                  :error-messages="errors"
                  label="E-mail"
                  required
                  type="email">
                </v-text-field>
              </ValidationProvider> -->

              <ValidationProvider mode="eager" v-slot="{ errors }" name="Name" rules="required|max:10">
                <v-text-field
                  v-model="userInfo.newName"
                  :counter="10"
                  :error-messages="errors"
                  label="Name"
                  required
                ></v-text-field>
              </ValidationProvider>

              <ValidationProvider
                mode="eager" v-slot="{ errors }" name="Password" rules="required">
                <v-text-field
                  v-model="userInfo.password"
                  :error-messages="errors"
                  label="Password"
                  name="password"
                  type="password"
                ></v-text-field>
              </ValidationProvider>

              <ValidationProvider
                mode="eager" v-slot="{ errors }" name="NewPassword" vid="confirmation"
                :rules="{ required: true, min: 8, regex: /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]/ }">
                <v-text-field
                  v-model="userInfo.newPassword"
                  :error-messages="errors"
                  label="New Password"
                  name="newPassword"
                  type="password"
                ></v-text-field>
              </ValidationProvider>

              <ValidationProvider mode="eager" v-slot="{ errors }" name="NewPasswordConfirm" rules="required|confirmed:confirmation">
                <v-text-field
                  v-model="userInfo.newPasswordConfirm"
                  :error-messages="errors"
                  label="New Password Confirm"
                  name="newPasswordConfirm"
                  type="password"
                ></v-text-field>
              </ValidationProvider>
              <v-alert dense outlined type="error" v-if="$store.state.isModifyChecked">비밀번호를 다시 확인해주세요.</v-alert>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-btn class="mb-4 ml-4" color="error" @click="$store.state.isDeleteModal=!$store.state.isDeleteModal">Delete Account</v-btn>
            <v-spacer></v-spacer>
            <v-btn
            class="mb-4 mr-1"
            color="primary"
            @click="updateUserInfo(userInfo), submit()"
            >Update</v-btn>
            <v-btn class="mb-4 mr-4" @click="close()">close</v-btn>
          </v-card-actions>
        </v-card>
      </ValidationObserver>
    </v-dialog>
    
    <DeleteAccountModal :userInfo="userInfo" />
  </div>
</template>

<script>
import { mapActions } from 'vuex'

import { required, max, min, regex, confirmed } from 'vee-validate/dist/rules';
import { extend, ValidationObserver, setInteractionMode, ValidationProvider } from 'vee-validate'

import DeleteAccountModal from './DeleteAccountModal.vue'

setInteractionMode('eager')

extend('required', {
  ...required,
  message: '{_field_} 값은 반드시 입력해야 합니다.',
})

// extend('email', {
//   ...email,
//   message: '{_field_} 형식이 아닙니다.',
// })

extend('regex', {
  ...regex,
  message: '비밀번호는 영문, 숫자, 특수기호를 모두 포함하여야 합니다.',
})

extend('max', {
  ...max,
  message: '{_field_} 값은 {length}자리 이하로 입력해주세요.',
})

extend('min', {
  ...min,
  message: '{_field_} 값은 최소 {length}자리 이상이어야 합니다.',
})

extend('confirmed', {
  ...confirmed,
  message: '비밀번호가 같지 않습니다.',
})

export default {
  name: 'MypageModal',

  components: {
    ValidationProvider,
    ValidationObserver,
    DeleteAccountModal,
  },
  data() {
    return {
      userInfo: {
        email: this.$store.state.userInfo.email,
        newName: '',
        password: '',
        newPassword: '',
        newPasswordConfirm: '',
        no: this.$store.state.userInfo.no
      },
    }
  },

  methods: {
    ...mapActions(['updateUserInfo']),

    submit() {
      this.$refs.observer.validate()
      if (!this.$store.state.isModifyChecked) {
        this.$store.state.isMypageModal = false
      }
    },

    close() {
      this.userInfo = {
        email: this.$store.state.userInfo.email,
        newName: '',
        password: '',
        newPassword: '',
        newPasswordConfirm: '',
        no: this.$store.state.userInfo.no
      },
      this.$refs.observer.reset()
      this.$store.commit('SET_MODIFY_RESULT', false)
      this.$store.state.isMypageModal = false
    },
  }
}
</script>

<style>

</style>