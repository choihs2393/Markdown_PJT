<template>
  <div>
    <v-btn class="mr-2" color="grey darken-1" text @click="isCreateFileModal=!isCreateFileModal">
      <v-icon>mdi-plus</v-icon>
      <v-spacer></v-spacer>
      <span>Create File</span>
    </v-btn>

    <v-row justify="center">
      <v-dialog v-model="isCreateFileModal" persistent max-width="500">
        <v-card>
          <v-card-title class="headline">Create Markdown</v-card-title>
          <v-card-text>
            <v-form>
              <v-text-field
                v-model="subject"
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
            <v-btn color="green darken-1" text @click="close()">Close</v-btn>
            <v-btn color="green darken-1" text @click="createNote(subject), close()">Create</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-row>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'CreateFileModal',

  data() {
    return {
      isCreateFileModal: false,
      subject: '',
    }
  },

  methods: {
    ...mapActions(['createNote']),

    close() {
      this.isCreateFileModal = false
      this.subject = ''
    }
  }

}
</script>

<style>

</style>