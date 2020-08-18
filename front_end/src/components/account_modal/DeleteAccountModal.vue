<template>
  <v-dialog v-model="$store.state.isDeleteModal" persistent max-width="400">
    <v-card>
      <v-card-title class="headline">정말 계정을 탈퇴하시겠습니까?</v-card-title>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" @click="deleteAccount(userInfo), close()">Yes</v-btn>
        <v-btn @click="close()">No</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'DeleteAccountModal',

  props: {
    userInfo: Object,
  },

  methods: {
    ...mapActions(['deleteAccount']),

    close() {
      this.$store.state.isDeleteModal = false
    }
  }
}
</script>

<style>

</style>