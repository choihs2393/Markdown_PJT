<template>
  <!-- rename file dialog -->
  <v-dialog v-model="$store.state.isRenameFileModal" persistent max-width="500">
    <v-card>
      <v-card-title class="headline">Rename Markdown</v-card-title>
      <v-card-text>
        <v-form>
          <v-text-field
            v-model="fileTitle"
            label="Title"
            name="title"
            prepend-icon="far fa-file-alt"
            required
            type="text">
          </v-text-field>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="green darken-1" text @click="renameFile(fileTitle)">Rename</v-btn>
        <v-btn color="green darken-1" text @click="close()">Close</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'RenameFileModal',

  data() {
    return {
      fileTitle: this.$store.state.fileList.title,
    }
  },

  methods: {
    ...mapActions(['renameFile']),

    close() {
      this.$store.commit('SET_IS_RENAME_FILE_MODAL', false)
    },
  }

}
</script>

<style>

</style>