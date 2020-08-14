<template>
  <!-- rename file dialog -->
  <v-dialog v-model="workspaceRenameDialog" max-width="600px">
    <v-card>
      <v-card-title>
        <span>Rename workspace</span>
      </v-card-title>
      <v-card-text>
        <v-form ref="form_workspace_rename">
          <v-row>
            <v-col cols="12" sm="20" md="20">
              <v-text-field label="rename *" required v-model="workspaceRename"></v-text-field>
            </v-col>
          </v-row>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue darken-1" text @click="cancelCreateWorkspace">Cancel</v-btn>
        <v-btn color="blue darken-1" text @click="renameWorkspace">Rename</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {

}
</script>

<style>

</style>