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
          <v-btn color="rgb(255, 179, 102)" dark v-bind="attrs" v-on="on" block>Add workspace</v-btn>
        </template>
        <v-card>
          <v-card-title>
            <span>New workspace</span>
          </v-card-title>
          <v-card-text>
            <v-form ref="form_workspace" @submit.prevent>
              <v-row>
                <v-col cols="12" sm="20" md="20">
                  <v-text-field
                    label="workspace *"
                    @keyup.enter="createWorkspace"
                    v-model="workspaceName"
                  ></v-text-field>
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
        <v-select
          class="ma-2"
          v-model="selected"
          :items="workspaces"
          item-text="name"
          @change="changeWorkspace"
          prepend-icon="folder"
          @mousedown.right="$refs.menu.open($event, selected)"
          hide-details
        ></v-select>
      </v-row>
      <v-row>
        <v-spacer></v-spacer>
        <v-btn
          text
          color="grey darken-1"
          v-if="!!(this.$store.state.workspace)"
          right
          tile
          @click="showInviteModal()"
        >
          <!-- <v-btn text color="grey darken-1" v-if="true" right tile @click="showInviteModal()"> -->
          <v-icon left>mdi-plus</v-icon>invite
        </v-btn>
      </v-row>
      <v-list subheader flat>
        <!-- <v-subheader >
          <v-toolbar-title>FILES</v-toolbar-title>
        </v-subheader>-->
        <v-list-item
          v-for="file in this.$store.state.fileList"
          :key="file._id"
          @click="openFile(file._id)"
          @mousedown.right="$refs.fileMenu.open($event, file), $store.commit('SELECTED_FILE_NO', file._id)"
        >
          <v-list-item-content>
            <v-list-item-title>{{ file.subject }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
      <v-row justify="end">
        <CreateFileModal />
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

    <ContextMenu ref="fileMenu">
      <template>
        <ContextMenuItem @click.native="openDeleteFileModal()">
          <v-icon>delete</v-icon>delete
        </ContextMenuItem>
        <ContextMenuItem @click.native="openRenameFileModal()">
          <v-icon>autorenew</v-icon>rename
        </ContextMenuItem>
      </template>
    </ContextMenu>

    <RenameFileModal />
    <DeleteFileModal />

    <InviteModal />
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
  </v-navigation-drawer>
</template>

<script>
import ContextMenu from "./ContextMenu";
import ContextMenuItem from "./ContextMenuItem";
import InviteModal from "./InviteModal.vue";
import CreateFileModal from "./mdfile_modal/CreateFileModal.vue";
import RenameFileModal from "./mdfile_modal/RenameFileModal.vue";
import DeleteFileModal from "./mdfile_modal/DeleteFileModal.vue";

import "material-design-icons-iconfont/dist/material-design-icons.css";
import fs from "fs";
import { remote } from "electron";

import { mapActions } from "vuex";

// 소켓 관련 모듈
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";

export default {
  name: "SideBarShare",

  components: {
    ContextMenu,
    ContextMenuItem,
    InviteModal,
    CreateFileModal,
    RenameFileModal,
    DeleteFileModal
  },

  computed: {},

  mounted() {
    // console.log("SideBarShare.vue -> mounted() 호출됨.")

    if (this.$store.state.userInfo.group.length == 0) {
    } else {
      this.workspaces = this.$store.state.userInfo.group;
    }
    // for(var i = 0; i < this.$store.state.userInfo.group.length; i++) {
    //   this.workspaces.push(this.$store.state.userInfo.group[i]);
    // }
  },

  updated() {
    // console.log("SideBarShare.vue -> mounted() 호출됨.")
  },

  data() {
    return {
      workspaceDialog: false,
      workspaceRenameDialog: false,
      memberDialog: false,
      // workspaceDialog: false,
      selected: "",
      workspaceNo: "",
      workspaceName: "",
      workspaceRename: "",
      memberEmail: "",
      workspaces: [{ name: "Add workspace first" }]
    };
  },

  methods: {
    ...mapActions(["openFile"]),

    cancelCreateWorkspace() {
      this.selected = "";
      this.$refs.form_workspace.reset();
      this.workspaceDialog = false;
    },

    // 워크스페이스 새로 추가하는 함수.
    createWorkspace() {
      event.preventDefault();
      // this.workspaces
      // this.workspaces.push({ name: this.workspaceName });
      this.selected = this.workspaceName;

      this.$store.dispatch("createWorkspace", this.workspaceName);

      this.$refs.form_workspace.reset();

      this.workspaceDialog = false;
      this.$store.commit("SELET_WORKSPACE", this.selected);
      this.workspaces = this.$store.state.userInfo.group;
    },
    changeWorkspace(select) {
      if (select == "Add workspace first") {
        this.workspaceDialog = true;
      } else {
        this.$store.commit("SELET_WORKSPACE", select);
        this.$store.dispatch("showFileList", select);

        // const serverURL = "http://localhost:8080/noteAPI/ws";
        // // const serverURL = "http://i3b104.p.ssafy.io:80/noteAPI/ws";
        // let socket = new SockJS(serverURL);
        // this.stompClient = Stomp.over(socket);

        // this.stompClient.connect(
        //   { Authorization: this.$store.state.authorization },
        //   frame => {
        //     this.connected = true;
        //     // 워크스페이스에서의 현재 파일 점유 유무를 받고 있음.
        //     this.stompClient.subscribe("/groupSend/" + this.$store.state.workspaceNo,
        //       res => {
        //         const receivedMsg = JSON.parse(res.body);

        //         // fileList를 돌며, res.body.file_id를 통해 row를 찾아, 해당 row의 account_no와 account_name을 바꿔준다.
        //         //      >> 누가 점유했다는 메세지를 받으면 -> fileList에서 해당 file을 찾아, accountNo와 accountName을 각각 점유자의 accountNo, accountName으로 덮어씌울거고,
        //         //      >> 누가 점유를 포기했다는 메세지를 받으면 -> fileList에서 해당 file을 찾아, accountNo와 accountName을 각각 0, ""으로 덮어씌울 것임.
        //         var idx = this.$store.state.userInfo.fileList.findIndex(element => element._id == res.body._id);
        //         this.$store.state.userInfo.fileList[idx].accountNo = res.body.accountNo;
        //         this.$store.state.userInfo.fileList[idx].accountName = res.body.accountName;
        //       }
        //     );
        //   }
        // );

        // this.stompClient.disconnect();
      }
    },
    deleteWorkspace() {
      this.$refs.menu.close();

      var workspace = this.$store.state.userInfo.group.find(element => element.name == this.$store.state.workspace);
      var idx = this.$store.state.userInfo.group.findIndex(element => element.name == this.$store.state.workspace);
      var workspaceNo = workspace.no;

      // console.log("workspaceNo : " + workspaceNo)
      // console.log("workspaceName : " + this.$store.state.workspace)
      // console.log("workspaceIdx : " + idx)

      const deleteWorkspace = {
        bandNo: workspaceNo,
        accountNo: this.$store.state.userInfo.no,
        workspaceIdx: idx
      };

      this.$store.dispatch("deleteWorkspace", deleteWorkspace);
      this.selected = "";
      this.workspaces = this.$store.state.userInfo.group;
    },
    showRenameDialog() {
      this.workspaceRenameDialog = true;
    },
    renameWorkspace() {
      this.workspaceRenameDialog = false;

      var workspace = this.$store.state.userInfo.group.find(element => element.name == this.$store.state.workspace);
      var workspaceNo = workspace.no;
      var idx = this.$store.state.userInfo.group.findIndex(element => element.name == this.$store.state.workspace);

      // console.log("selectedWorkspaceName : " + workspace.name)
      // console.log("workspaceNo : " + workspaceNo)
      // console.log("workspaceRename : " + this.workspaceRename)

      const renameWorkspace = {
        bandNo: workspaceNo,
        accountNo: this.$store.state.userInfo.no,
        newBandName: this.workspaceRename,
        workspaceIdx: idx
      };

      this.$store.dispatch("renameWorkspace", renameWorkspace);
      this.workspaces = this.$store.state.userInfo.group;
      // this.selected = this.workspaceRename
      this.workspaceRename = "";
    },
    showInviteModal() {
      // console.log("전체 워크스페이스", this.$store.state.userInfo.group)
      var workspaceNo = this.$store.state.userInfo.group.find(element => element.name == this.$store.state.workspace).no;
      this.workspaceNo = workspaceNo;

      // console.log("여기workspaceNo : " + workspaceNo);

      const showGroupMembers = {
        bandNo: workspaceNo,
        email: this.$store.state.userInfo.email
      };

      this.$store.dispatch("showGroupMembers", showGroupMembers);
    },

    openRenameFileModal(data) {
      this.$store.commit("SET_IS_RENAME_FILE_MODAL", true);
    },

    openDeleteFileModal() {
      this.$store.commit("SET_IS_DELETE_FILE_MODAL", true);
      // console.log(data)
      // this.$store.dispatch('deleteFile', data)
    }
  }
};
</script>

<style scoped>
.v-list {
  padding: 0;
}

.v-list-item {
  margin: 0.5em 2em 0;
  padding: 1em 0;
  height: 1em;
}
.v-list-item:not(:last-child) {
  font-family: "Roboto Slab", sans-serif;
  border-bottom: solid 1px rgba(210, 215, 217, 0.75);
}
.sidesubheader {
  border-bottom: solid 3px rgb(100, 93, 102);
  display: inline-block;
  margin: 2.5em 0 0 0;
  padding: 0 0.75em 0.5em 0;
}

.v-subheader:not(.sidesubheader) {
  font-size: 1.25em;
  color: rgb(95, 90, 97) !important;
  border-bottom: solid 2px rgb(83, 81, 83);
  display: inline-block;
  margin: 0.5em;
  padding: 0.75em 0;
}

.theme--light.v-list-item:not(.v-list-item--active) {
  color: #615f75 !important;
  font-size: 0.5em;
}

.theme--light.v-list-item:not(.v-list-item--active):not(.v-list-item--disabled):hover {
  color: rgb(214, 198, 219) !important;
}

.theme--dark.v-list-item:not(.v-list-item--active) {
  color: #615f75 !important;
  font-size: 0.5em;
}

.theme--dark.v-list-item:not(.v-list-item--active):not(.v-list-item--disabled):hover {
  color: rgb(214, 198, 219) !important;
}
</style>