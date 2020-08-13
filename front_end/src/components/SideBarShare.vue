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
      </v-toolbar>
    </v-card>

    <v-container>
      <v-subheader>My Workspace</v-subheader>
      <!-- 워크스페이스 추가 다이얼로그 -->
          <!-- Add a workspace를 눌렀을 때 보이는 다이얼로그 -->
    <v-dialog v-model="workspaceDialog" max-width="600px">
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          color="rgb(255, 179, 102)"
          dark
          v-bind="attrs"
          v-on="on"
          block
        >
          Add workspace
        </v-btn>
      </template>
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
      <v-row>
        <v-col cols="20" style="padding: 0">
          <v-select
            v-model="selected"
            :items="workspaces"
            item-text="name"
            @change="changeWorkspace"
            prepend-icon="folder"
            @mousedown.right="$refs.menu.open($event, selected)"
            hide-details
          >
          </v-select>
        </v-col>
      </v-row>
    </v-container>

    <ContextMenu ref="menu">
      <template>
        <ContextMenuItem v-if="selected" @click.native="deleteWorkspace">
          <v-icon>delete</v-icon>delete
        </ContextMenuItem>
        <ContextMenuItem v-if="selected" @click.native="showRenameDialog">
          <v-icon>autorenew</v-icon>rename
        </ContextMenuItem>
      </template>
    </ContextMenu>

    <!-- workspace rename dialog -->
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
    <v-btn text small style="padding: 0" color="grey darken-1" v-if="!!(this.$store.state.workspace)" absolute right tile @click="showInviteModal()">
      <v-icon left>mdi-plus</v-icon>
      invite
    </v-btn>
    <InviteModal />

    <v-list subheader flat>
      <v-subheader >FILES</v-subheader>

      <v-list-item v-for="file in this.$store.state.fileList" :key="file.no" @click="openFile(file.fileFullPath)">
          <!-- <v-list-item-avatar>
          <v-icon :class="[file.iconClass]">{{ file.icon }}</v-icon>
        </v-list-item-avatar> -->
        <v-list-item-content>
          <v-list-item-title>{{ file.title }}</v-list-item-title>

          <!-- <v-list-item-subtitle>{{ file.subtitle }}</v-list-item-subtitle> -->
        </v-list-item-content>

        <!-- <v-list-item-action>
          <v-btn icon ripple @click="openFile(file.fileFullPath)">
            <v-icon color="grey lighten-1">mdi-information</v-icon>
          </v-btn>
        </v-list-item-action> -->
      </v-list-item>
    </v-list>

  </v-navigation-drawer>
</template>

<script>
import "material-design-icons-iconfont/dist/material-design-icons.css";
import ContextMenu from './ContextMenu';
import ContextMenuItem from './ContextMenuItem';
import InviteModal from "./InviteModal.vue";

export default {
  name: "SideBarShare",

  components: {
    ContextMenu,
    ContextMenuItem,
    InviteModal,
  },

  computed: {
    
  },

  mounted() {
    console.log("SideBarShare.vue -> mounted() 호출됨.")

    if(this.$store.state.userInfo.group.length == 0) {
      
    } else {
      this.workspaces = this.$store.state.userInfo.group;
    }
    // for(var i = 0; i < this.$store.state.userInfo.group.length; i++) {
    //   this.workspaces.push(this.$store.state.userInfo.group[i]);
    // }
  },

    updated(){
      console.log("SideBarShare.vue -> mounted() 호출됨.")
    },
  
  data() {
    return {
      workspaceDialog: false,
      workspaceRenameDialog: false,
      memberDialog: false,
      // workspaceDialog: false,
      selected: "",
      workspaceName: "",
      workspaceRename: "",
      memberEmail: "",
      workspaces: [
        { name: "Add workspace first" },
      ],
      // files: [
      //   {
      //     title: '낄낄.md',
      //   },
      //   {
      //     title: '깔깔.md'
      //   }
      // ],
    };
  },



  methods: {
    cancelCreateWorkspace() {
      this.selected = "";
      this.$refs.form_workspace.reset();
      this.workspaceDialog = false;
    },

    // 워크스페이스 새로 추가하는 함수.
    createWorkspace() {
      // this.workspaces
      // this.workspaces.push({ name: this.workspaceName });
      this.selected = this.workspaceName;
      
      this.$store.dispatch("createWorkspace", this.workspaceName);

      this.$refs.form_workspace.reset();
      
      this.workspaceDialog = false;
      this.$store.commit('SELET_WORKSPACE', this.selected)
      this.workspaces = this.$store.state.userInfo.group

    },
    changeWorkspace(select) {
      if (select == "Add workspace first") {
        this.workspaceDialog = true;
      }else {
        this.$store.commit('SELET_WORKSPACE', select)
      }
    },
    deleteWorkspace() {
      this.$refs.menu.close();

      var workspace = this.$store.state.userInfo.group.find(element => element.name == this.$store.state.workspace);
      var idx = this.$store.state.userInfo.group.findIndex(element => element.name == this.$store.state.workspace);
      var workspaceNo = workspace.no

      console.log("workspaceNo : " + workspaceNo)
      console.log("workspaceName : " + this.$store.state.workspace)
      console.log("workspaceIdx : " + idx)

      const deleteWorkspace = {
        bandNo: workspaceNo,
        accountNo: this.$store.state.userInfo.no,
        workspaceIdx: idx
      }

      this.$store.dispatch("deleteWorkspace", deleteWorkspace);
      this.selected = "";
      this.workspaces = this.$store.state.userInfo.group;
    },
    showRenameDialog() {
      this.workspaceRenameDialog = true;
    },
    renameWorkspace() {
      this.workspaceRenameDialog = false;

      var workspace = this.$store.state.userInfo.group.find(element => element.name == this.$store.state.workspace)
      var workspaceNo = workspace.no
      var idx = this.$store.state.userInfo.group.findIndex(element => element.name == this.$store.state.workspace);

      console.log("selectedWorkspaceName : " + workspace.name)
      console.log("workspaceNo : " + workspaceNo)
      console.log("workspaceRename : " + this.workspaceRename)

      const renameWorkspace = {
        bandNo: workspaceNo,
        accountNo: this.$store.state.userInfo.no,
        newBandName: this.workspaceRename,
        workspaceIdx: idx,
      }

      this.$store.dispatch("renameWorkspace", renameWorkspace)
      this.workspaces = this.$store.state.userInfo.group;
      // this.selected = this.workspaceRename
      this.workspaceRename = ""
      
    },
    showInviteModal() {
      // console.log("전체 워크스페이스", this.$store.state.userInfo.group)
      var workspaceNo = this.$store.state.userInfo.group.find(element => element.name == this.$store.state.workspace).no;
      
      // console.log("여기workspaceNo : " + workspaceNo);

      const showGroupMembers = {
        bandNo: workspaceNo,
        email: this.$store.state.userInfo.email,
      }

      this.$store.dispatch("showGroupMembers", showGroupMembers)
    },
  }
};
</script>

<style>
</style>