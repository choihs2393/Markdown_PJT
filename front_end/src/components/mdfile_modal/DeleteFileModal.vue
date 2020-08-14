<template>
  <!-- rename file dialog -->
  <v-dialog v-model="$store.state.isDeleteFileModal" persistent max-width="600px">
    <v-card>
      <v-card-title>
        <span>Delete File</span>
      </v-card-title>
      <v-card-text>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue darken-1" text @click="deleteFile()">Delete</v-btn>
        <v-btn color="blue darken-1" text @click="close()">Close</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: 'DeleteFileModal',

  methods: {
    ...mapActions(['deleteFile']),

    close() {
      this.$store.commit('SET_IS_DELETE_FILE_MODAL', false)
    },
  }

}
</script>

<style>

</style>