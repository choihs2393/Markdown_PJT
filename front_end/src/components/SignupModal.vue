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
                    <v-btn outlined small rounded @click="checkEmail(signupData)">Check</v-btn>  
                  </template>
                </v-text-field>
              </ValidationProvider>

              <ValidationProvider v-show="isEmailChecked" mode="eager" v-slot="{ errors }" name="CodeNumber" rules="required">
                <v-text-field
                  v-model="signupData.authNum"
                  :error-messages="errors"
                  label="CodeNumber"
                  required>
                  <template v-slot:append-outer>
                    <v-btn outlined small rounded class="mr-1" @click="confirmAuthNum(signupData)">Confirm</v-btn>  
                    <v-btn outlined small rounded @click="sendEmail(signupData)">Re-Send</v-btn>
                  </template>
                </v-text-field>
              </ValidationProvider>

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
                :rules="{ required: true, regex: /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$/ }">
                <v-text-field
                  v-model="signupData.password"
                  :error-messages="errors"
                  :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'" @click:append="show1 = !show1"
                  label="Password"
                  name="password"
                  :type="show1 ? 'text' : 'password'"
                ></v-text-field>
              </ValidationProvider>
              <ValidationProvider mode="eager" v-slot="{ errors }" name="PasswordConfirm" rules="required|confirmed:confirmation">
                <v-text-field
                  v-model="signupData.passwordConfirm"
                  :error-messages="errors"
                  :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'" @click:append="show1 = !show1"
                  label="Password Confirm"
                  name="passwordConfirm"
                  :type="show1 ? 'text' : 'password'"
                ></v-text-field>
              </ValidationProvider>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn class="mb-4 mr-1" color="primary" :disabled="invalid" @click="signup(signupData), submit(), close">SignUp</v-btn>
            <v-btn class="mb-4 mr-4" @click="close">close</v-btn>
          </v-card-actions>
        </v-card>
      </ValidationObserver>
    </v-dialog>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';

// import { required, email, max } from 'vee-validate/dist/rules';
import { ValidationObserver, setInteractionMode } from 'vee-validate'
// import { extend, ValidationObserver, setInteractionMode } from 'vee-validate'
import { ValidationProvider } from 'vee-validate/dist/vee-validate.full.esm';
// import { localize } from 'vee-validate/dist/vee-validate.full';


setInteractionMode('eager')

// extend('emailCheck', value => {
//   if (value) {

//   }
//   message: '{_field_} can not be empty',
//   return true
// })

// extend('required', {
//   ...required,
//   message: '{_field_} can not be empty',
// })

// extend('max', {
//   ...max,
//   message: '{_field_} may not be greater than {length} characters',
// })

// extend('email', {
//   ...email,
//   message: 'Email must be valid',
// })

export default {
  name: 'SignupModal',

  components: {
    ValidationProvider,
    ValidationObserver,
  },

  computed: {
    ...mapGetters(['isEmailChecked'])
  },

  data() {
    return {
      isSignupModal: false,
      show1: false,
      emailCheckColor: '',

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
    ...mapActions(['signup', 'checkEmail', 'sendEmail', 'confirmAuthNum']),

    submit() {
      this.$refs.observer.validate()
      this.isSignupModal = false
    },
    close() {
      this.signupData = {
        email: '',
        authNum: '',
        name: '',
        password: '',
        passwordConfirm: '',
      }
      this.$store.state.emailResult = false
      this.$refs.observer.reset()
      this.isSignupModal = false
    },
  }
}
</script>

<style>

</style>