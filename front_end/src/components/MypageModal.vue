<template>
  <div>
    <v-btn text @click="isMypageModal = !isMypageModal">MyPage</v-btn>

    <v-dialog v-model="isMypageModal" max-width="500px">
      <v-card class="elevation-12">
        <v-toolbar dark flat>
          <v-toolbar-title class="ml-4">MyPage</v-toolbar-title>
        </v-toolbar>
        <v-card-text>
          <v-form class="ma-4">
            <v-text-field
              label="Name"
              required
            ></v-text-field>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            class="mb-4 mr-1" color="primary">Update</v-btn>
          <v-btn class="mb-4 mr-4">close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
export default {
  name: 'MypageModal',

  data() {
    return {
      isMypageModal: false,
    }
  },
}
</script>

<style>

</style>