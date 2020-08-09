<template>

  <v-navigation-drawer v-model="$store.state.drawerShare" app>
    <v-card>
      <v-toolbar
        prominent
        src="https://images.unsplash.com/photo-1529156069898-49953e39b3ac?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2089&q=80"
      >
        <v-subheader class="sidesubheader">
          <v-toolbar-title>SHARES</v-toolbar-title>
        </v-subheader>
        <v-spacer></v-spacer>
        <v-spacer></v-spacer>
        <v-btn icon @click="showInviteModal()">
          <v-icon>mdi-plus</v-icon>
        </v-btn>
        <InviteModal />

        <!-- SHARES 옆에 + 버튼을 눌렀을 때 보이는, 그룹원 추가하는 다이얼로그
        <v-dialog v-model="memberDialog" max-width="600px">
          <template v-slot:activator="{ on, attrs }">
            <v-btn icon v-bind="attrs" v-on="on">
              <v-icon>mdi-plus</v-icon>
            </v-btn>
          </template>

          <v-card>
            <v-card-title>
              <span class="headline">Add member</span>
            </v-card-title>
            <v-card-text>
              <v-container>
                <v-row>
                  <v-col cols="12" sm="20" md="20">
                    <v-text-field v-model="memberEmail" label="member email *" required></v-text-field>
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="dialog = false">Cancel</v-btn>
              <v-btn color="blue darken-1" text @click="addMember()">Add</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog> -->
      </v-toolbar>
    </v-card>

    <v-container>
      <v-subheader>My Workspace</v-subheader>
      <v-row>
        <v-col cols="20">
          <v-select
            v-model="selected"
            :items="workspaces"
            item-text="name"
            @change="changeWorkspace"
            prepend-icon="folder"
            @mousedown.right="$refs.menu.open($event, selected)"
          >
          </v-select>
        </v-col>
      </v-row>
    </v-container>

    <ContextMenu ref="menu">
      <template slot-scope="{ contextData }">
        <ContextMenuItem v-if="selected" @click.native="deleteWorkspace">
          <v-icon>delete</v-icon>{{ contextData }}
        </ContextMenuItem>
      </template>
    </ContextMenu>

    <!-- Add a workspace를 눌렀을 때 보이는 다이얼로그 -->
    <v-dialog v-model="workspaceDialog" max-width="600px">
      <v-card>
        <v-card-title>
          <span>New workspace</span>
        </v-card-title>
        <v-card-text>
          <v-form ref="form_workspace">
            <v-row>
              <v-col cols="12" sm="20" md="20">
                <v-text-field label="workspace *" required v-model="workspaceName"></v-text-field>
              </v-col>
            </v-row>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="cancelCreateWorkspace">Cancel</v-btn>
          <v-btn color="blue darken-1" text @click="createWorkspace">Create</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-navigation-drawer>
</template>

<script>
import "material-design-icons-iconfont/dist/material-design-icons.css";
import ContextMenu from './ContextMenu';
import ContextMenuItem from './ContextMenuItem';
import InviteModal from "./InviteModal.vue";

export default {
  data() {
    return {
      memberDialog: false,
      workspaceDialog: false,
      selected: "",
      workspaceName: "",
      memberEmail: "",
      workspaces: [
        { name: "Add a workspace" },
        { name: "[Sample] Workspace1" },
        { name: "[Sample] Workspace2" },
        { name: "[Sample] Workspace3" }
      ]
    };
  },
  components: {
    ContextMenu,
    ContextMenuItem,
    InviteModal,
  },
  methods: {
    cancelCreateWorkspace() {
      this.selected = "";
      this.$refs.form_workspace.reset();
      this.workspaceDialog = false;
    },
    createWorkspace() {
      this.workspaces.push({ name: this.workspaceName });
      this.selected = this.workspaceName;
      this.$refs.form_workspace.reset();
      this.workspaceDialog = false;
      this.$store.state.workspace = this.selected;
    },
    changeWorkspace(select) {
      if (select == "Add a workspace") {
        this.workspaceDialog = true;
      }else {
        this.$store.state.workspace = select;
      }
    },
    deleteWorkspace() {
      this.$refs.menu.close();
    },
    showInviteModal() {
      // console.log('여기', this.$store.state.workspace)
      // console.log('여기', !!(this.$store.state.workspace))
      if(!!(this.$store.state.workspace)){
        this.$store.state.isInviteModal = !this.$store.state.isInviteModal;
        }else{
        alert('workspace부터 골라')
      }
    },
  }
};
</script>

<style>
</style>