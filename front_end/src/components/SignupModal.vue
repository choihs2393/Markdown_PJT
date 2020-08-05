<template>
  <div>
    <v-btn text @click="isSignupModal=!isSignupModal">SignUp</v-btn>

    <v-dialog v-model="isSignupModal" max-width="500px">
      <ValidationObserver ref="observer" v-slot="{ invalid }">
        <v-card class="elevation-12">
          <v-toolbar dark flat>
            <v-toolbar-title class="ml-4">SignUp</v-toolbar-title>
          </v-toolbar>
          <v-card-text>
            <v-form class="ma-4">
              <ValidationObserver v-slot="{ invalid }">
                <ValidationProvider
                  mode="eager" v-slot="{ errors }" name="Email"
                  rules="required|email">
                  <v-text-field
                    v-model="signupData.email"
                    :error-messages="errors"
                    label="E-mail"
                    required
                    type="email">
                    <template v-slot:append-outer>
                      <v-btn
                        :disabled="invalid" outlined small rounded
                        @click="checkEmailDuplicate(signupData), isDuplicateAlert=true">
                        CHECK
                      </v-btn>  
                    </template>
                  </v-text-field>
                </ValidationProvider>
              </ValidationObserver>
              <v-alert dense outlined type="error" v-if="isDuplicateAlert && !isDuplicateChecked">
                이미 가입된 Email 입니다.
              </v-alert>

              <ValidationProvider v-show="isDuplicateChecked" mode="eager" v-slot="{ errors }" name="CodeNumber" rules="required">
                <v-text-field
                  v-model="signupData.authNum"
                  :error-messages="errors"
                  label="CodeNumber"
                  required>
                  <template v-slot:append-outer>
                    <v-btn outlined small rounded class="mr-1" @click="checkAuthNum(signupData), isAuthNumAlert=true">Confirm</v-btn>  
                    <v-btn outlined small rounded @click="sendAuthNum(signupData)">Re-Send</v-btn>
                  </template>
                </v-text-field>
              </ValidationProvider>
              <v-alert dense outlined type="error" v-if="isAuthNumAlert && !isAuthNumChecked">
                인증코드를 다시 확인해주세요.
              </v-alert>

              <ValidationProvider mode="eager" v-slot="{ errors }" name="Name" rules="required|max:10">
                <v-text-field
                  v-model="signupData.name"
                  :counter="10"
                  :error-messages="errors"
                  label="Name"
                  required
                ></v-text-field>
              </ValidationProvider>

              <ValidationProvider
                mode="eager" v-slot="{ errors }" name="Password" vid="confirmation"
                :rules="{ required: true, min: 8, regex: /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]/ }">
                <v-text-field
                  v-model="signupData.password"
                  :error-messages="errors"
                  label="Password"
                  name="password"
                  type="password"
                ></v-text-field>
              </ValidationProvider>

              <ValidationProvider mode="eager" v-slot="{ errors }" name="PasswordConfirm" rules="required|confirmed:confirmation">
                <v-text-field
                  v-model="signupData.passwordConfirm"
                  :error-messages="errors"
                  label="Password Confirm"
                  name="passwordConfirm"
                  type="password"
                ></v-text-field>
              </ValidationProvider>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
              class="mb-4 mr-1" color="primary" @click="signup(signupData), close()"
              :disabled="invalid || !isDuplicateChecked || !isAuthNumChecked">SignUp</v-btn>
            <v-btn class="mb-4 mr-4" @click="close()">close</v-btn>
          </v-card-actions>
        </v-card>
      </ValidationObserver>
    </v-dialog>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex';

import { required, email, max, min, regex, confirmed } from 'vee-validate/dist/rules';
import { extend, ValidationObserver, setInteractionMode, ValidationProvider } from 'vee-validate'

setInteractionMode('eager')

extend('required', {
  ...required,
  message: '{_field_} 값은 반드시 입력해야 합니다.',
})

extend('email', {
  ...email,
  message: '{_field_} 형식이 아닙니다.',
})

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
  name: 'SignupModal',

  components: {
    ValidationProvider,
    ValidationObserver,
  },

  computed: {
    ...mapState([
      'isDuplicateChecked',
      'isAuthNumChecked',
    ]),
  },

  data() {
    return {
      isSignupModal: false,
      isDuplicateAlert: false,
      isAuthNumAlert: false,

      signupData: {
        email: '',
        authNum: '',
        name: '',
        password: '',
        passwordConfirm: '',
      },
    }
  },

  methods: {
    ...mapActions([
      'signup',
      'checkEmailDuplicate',
      'sendAuthNum',
      'checkAuthNum',
    ]),

    // submit() {
    //   this.$refs.observer.validate()
    //   this.isSignupModal = false
    // },
    close() {
      this.signupData = {
        email: '',
        authNum: '',
        name: '',
        password: '',
        passwordConfirm: '',
      }
      this.$refs.observer.reset()
      this.isSignupModal = false
      this.isDuplicateAlert = false
      this.isAuthNumAlert = false
    },
  }
}
</script>

<style>

</style>