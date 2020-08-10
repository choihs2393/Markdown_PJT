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
<<<<<<< HEAD
=======
        <v-spacer></v-spacer>
>>>>>>> 1fa6931b47d6c9cc576611b8af6c8a1cc2047065
        <v-btn icon @click="showInviteModal()">
          <v-icon>mdi-plus</v-icon>
        </v-btn>
        <InviteModal />
      </v-toolbar>
<<<<<<< HEAD

        <v-btn icon
              @click="dialog = !dialog"
        >
        <v-dialog v-model="dialog" max-width="600px">
          <template v-slot:activator="{ on, attrs }">
            <v-btn icon v-bind="attrs" v-on="on">
              <v-icon>mdi-plus</v-icon>
            </v-btn>
          </template>

          <v-card>
            <v-card-title>
              <span class="headline">Create a group</span>
            </v-card-title>
            <v-card-text>
              <v-container>
                <v-row>
                  <v-col cols="12" sm="20" md="20">
                    <v-text-field v-model="groupName" label="group name" required></v-text-field>
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="dialog = false">Cancel</v-btn>
              <v-btn color="blue darken-1" text @click="addGroup()">Add</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
        </v-btn>
      <v-list subheader flat>
        <v-subheader>GROUPS</v-subheader>

        <v-list-item v-for="group in groups" :key="group.groupName" @click="updateGroupName(group.groupName)">
          <!-- <v-list-item-avatar>
                <v-icon :class="[folder.iconClass]">{{ folder.icon }}</v-icon>
          </v-list-item-avatar>-->
          <v-list-item-content>
            <v-list-item-title :id="group.groupName">{{ group.groupName }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-divider></v-divider>

        <!-- <v-subheader >who is in this group?</v-subheader> -->

        <!-- <v-list-item v-for="file in files" :key="file.title" @click="openFile(file.fileFullPath)">
              <v-list-item-content>
                <v-list-item-title>{{ file.title }}</v-list-item-title>

                <v-list-item-subtitle>{{ file.subtitle }}</v-list-item-subtitle>
              </v-list-item-content>
        </v-list-item>-->
      </v-list>
    </v-card>

    <!-- <v-dialog v-model="memberDialog" persistent max-width="600px">
      <template v-slot:activator="{ on }">
        <v-btn color="rgb(255, 170, 128)" bottom block v-on="on">Add group member</v-btn>
=======
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
>>>>>>> 1fa6931b47d6c9cc576611b8af6c8a1cc2047065
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
<<<<<<< HEAD
          <v-btn @click="closeMemberDialog">Close</v-btn>
          <v-btn :id=group.groupName @click="addMember(group.groupName)">Add</v-btn>
          <v-btn :id="groupName" :value="$attrs" @click="addMember(group.groupName)">Add</v-btn>
=======
          <v-btn color="blue darken-1" text @click="cancelCreateWorkspace">Cancel</v-btn>
          <v-btn color="blue darken-1" text @click="renameWorkspace">Rename</v-btn>
>>>>>>> 1fa6931b47d6c9cc576611b8af6c8a1cc2047065
        </v-card-actions>
      </v-card>
    </v-dialog> -->
  </v-navigation-drawer>
</template>

<script>
<<<<<<< HEAD
import { remote } from "electron";
import fs from "fs";
=======
import "material-design-icons-iconfont/dist/material-design-icons.css";
import ContextMenu from './ContextMenu';
import ContextMenuItem from './ContextMenuItem';
>>>>>>> 1fa6931b47d6c9cc576611b8af6c8a1cc2047065
import InviteModal from "./InviteModal.vue";

export default {
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
  data() {
    return {
      workspaceDialog: false,
      workspaceRenameDialog: false,
      memberDialog: false,
      workspaceDialog: false,
      selected: "",
      workspaceName: "",
      workspaceRename: "",
      memberEmail: "",
      workspaces: [
        { name: "Add workspace first" },
      ]
    };
  },
<<<<<<< HEAD
  components: {
    InviteModal,
  },
  methods: {
    // closeMemberDialog() {
    //   this.$refs.form.reset();
    //   this.memberDialog = false;
    // },
    addGroup() {
      this.dialog = false;
      this.groups.push({icon: "assignment", groupName: this.groupName, groupMembers: []});
      this.$store.state.groupName = this.groupName;
    },
    showInviteModal () {
      // console.log('여기', !!(this.$store.state.groupName))
      if(!!(this.$store.state.groupName)){
=======
  updated(){
    console.log("updated() 호출됨.")
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

    // 워크스페이스 새로 추가하는 함수.
    createWorkspace() {
      // this.workspaces
      // this.workspaces.push({ name: this.workspaceName });
      this.selected = this.workspaceName;
      
      this.$store.dispatch("createWorkspace", this.workspaceName);

      this.$refs.form_workspace.reset();
      
      this.workspaceDialog = false;
      this.$store.state.workspace = this.selected;
      this.workspaces = this.$store.state.userInfo.group

    },
    changeWorkspace(select) {
      if (select == "Add workspace first") {
        this.workspaceDialog = true;
      }else {
        this.$store.state.workspace = select;
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
      // console.log('여기', this.$store.state.workspace)
      // console.log('여기', !!(this.$store.state.workspace))
      if(!!(this.$store.state.workspace)){
>>>>>>> 1fa6931b47d6c9cc576611b8af6c8a1cc2047065
        this.$store.state.isInviteModal = !this.$store.state.isInviteModal;
        }else{
        alert('workspace부터 골라')
      }
    },
<<<<<<< HEAD
    updateGroupName (groupName) {
      this.$store.state.groupName = groupName;
      console.log(this.$store.state.groupName)
    }
    // addMember(param) {
    //   this.memberDialog = false;

    //   var memberEmail = this.email;
    //   this.email = "";

    //   console.log("누를 그룹의 이름 : " + param);

    //   var idx = this.groups.findIndex(element => element.groupName == param);
    //   console.log("idx : " + idx);
    //   console.log("찾은 그룹의 이름 : " + this.groups[idx].groupName);
    //   console.log(
    //     "찾은 그룹의 그룹원들 (추가 전) : " + this.groups[idx].groupMembers
    //   );
    //   this.groups[idx].groupMembers.push(memberEmail);
    //   console.log(
    //     "찾은 그룹의 그룹원들 (추가 후) : " + this.groups[idx].groupMembers
    //   );
    // }
=======
>>>>>>> 1fa6931b47d6c9cc576611b8af6c8a1cc2047065
  }
};
</script>

<style>
</style>