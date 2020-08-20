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
      <v-subheader class="mr-3">My Workspace</v-subheader>
      <v-btn class="ma-4" tile large color="teal" icon @click="getNoteList($store.state.selectedBandInfo)">
        <v-icon>refresh</v-icon>
      </v-btn>
      
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
                    v-model="newBandName"
                    label="workspace *"
                    @keyup.enter="createWorkspace(newBandName)">
                  </v-text-field>
                </v-col>
              </v-row>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="cancelCreateWorkspace()">Cancel</v-btn>
            <v-btn color="blue darken-1" text @click="createWorkspace(newBandName)">Create</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
      <v-row>
        <v-select
          class="ma-2"
          v-model="bandInfo"
          :items="userInfo.group"
          item-text="name"
          @input="changeWorkspace(bandInfo)"
          @mousedown.right="$refs.bandMenu.open($event, bandInfo)"
          prepend-icon="folder"
          return-object
          hide-details
        ></v-select>
      </v-row>
      <v-row>
        <v-spacer></v-spacer>
        <v-btn text color="grey darken-1" v-if="!!bandInfo" right tile @click="showInviteModal(); $store.state.noSuchMemberAlert=false; $store.state.alreadyMemberAlert=false">
          <v-icon left>mdi-plus</v-icon>
          invite
        </v-btn>
      </v-row>
      <v-list subheader flat>
        <!-- <v-subheader >
          <v-toolbar-title>FILES</v-toolbar-title>
        </v-subheader> -->
        <v-list-item
          v-for="note in noteList"
          :key="note._id"
          @click="getNote(note)"
          @mousedown.right="$refs.fileMenu.open($event, note), setRightNoteInfo(note)"
        >
          <v-list-item-content>
            <v-list-item-title>{{ note.subject }}</v-list-item-title>
          </v-list-item-content>
          <v-list-item-icon>
            <v-icon v-if="note.occupiedNo != 0 && note.occupiedNo != userInfo.no" right>lock</v-icon>
            <v-icon v-if="note.occupiedNo != 0 && note.occupiedNo == userInfo.no" right color="blue">lock</v-icon>
          </v-list-item-icon>
        </v-list-item>
      </v-list>
      <v-row justify="end">
        <CreateFileModal v-if="bandInfo" />
      </v-row>
    </v-container>

    <ContextMenu ref="bandMenu">
      <template>
        <ContextMenuItem v-if="bandInfo" @click.native="deleteWorkspace()">
          <v-icon>delete</v-icon>delete
        </ContextMenuItem>
        <ContextMenuItem v-if="bandInfo" @click.native="showRenameDialog()">
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

    <InviteModal :workspaceNo="bandInfo.no" />
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
                <v-text-field label="rename *" required v-model="bandInfo.name"></v-text-field>
              </v-col>
            </v-row>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="workspaceRenameDialog=false">Cancel</v-btn>
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

import { mapState, mapActions, mapGetters } from 'vuex'

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

  computed: {
    ...mapState(['userInfo']),
    ...mapGetters(['noteList']),
  },

  data() {
    return {
      bandInfo: '',
      workspaceDialog: false,
      workspaceRenameDialog: false,
      newBandName: "",
      // memberEmail: "",
      // memberDialog: false,
    };
  },

  methods: {
    ...mapActions(["getNote","initUserInfo", "getNoteList"]),

    cancelCreateWorkspace() {
      this.$refs.form_workspace.reset();
      this.workspaceDialog = false;
    },

    // 워크스페이스 새로 추가하는 함수.
    async createWorkspace(newBandName) {
      // event.preventDefault();
      if (!!newBandName) {
        await this.$store.dispatch("createWorkspace", newBandName);
        this.bandInfo = this.$store.state.selectedBandInfo;
        this.$store.dispatch('getNoteList', this.bandInfo)
      }
      this.$refs.form_workspace.reset();
      this.workspaceDialog = false;

      document.getElementById("serverFileName").style.display = "none";
      const win = remote.BrowserWindow.getFocusedWindow();
      win.webContents.send("contentReset", "");
      this.$store.state.selectedNoteInfo = {};
    },

    changeWorkspace(bandInfo) {
      const win = remote.BrowserWindow.getFocusedWindow();
      win.webContents.send("contentReset", "");
      this.$store.state.selectedNoteInfo = {};

      if(this.$store.state.shareGroupSocket.connected)
        this.$store.state.shareGroupSocket.disconnect();
      console.log("bandInfo: ", bandInfo)
      this.$store.commit('SELECTED_WORKSPACE', bandInfo)
      this.$store.dispatch('getNoteList', bandInfo)
      // const serverURL = "http://localhost:8080/noteAPI/ws";
      const serverURL = "http://i3b104.p.ssafy.io:80/noteAPI/ws";
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);

      this.stompClient.connect(
        { Authorization: this.$store.state.authorization },
        frame => {
          this.connected = true;
          this.$store.commit('setShareGroupSocket',this.stompClient);

          // 워크스페이스에서의 현재 파일 점유 유무를 받고 있음.
          this.stompClient.subscribe("/send/groupSend/occupy/" + this.$store.state.selectedBandInfo.no,
            res => {
              const receivedMsg = JSON.parse(res.body);
              
              console.log(receivedMsg)
              console.log(receivedMsg.noteNo)
              console.log(receivedMsg.occupiedNo)
              console.log(receivedMsg.content)


              // fileList를 돌며, res.body.file_id를 통해 row를 찾아, 해당 row의 account_no와 account_name을 바꿔준다.
              //      >> 누가 점유했다는 메세지를 받으면 -> fileList에서 해당 file을 찾아, accountNo와 accountName을 각각 점유자의 accountNo, accountName으로 덮어씌울거고,
              //      >> 누가 점유를 포기했다는 메세지를 받으면 -> fileList에서 해당 file을 찾아, accountNo와 accountName을 각각 0, ""으로 덮어씌울 것임.
              console.log("this.$store.state.noteList", this.$store.state.noteList)
              console.log("너 어딨음 ?? " + receivedMsg.noteNo);
              console.log("???" + receivedMsg.noteNo);
              console.log("???" + receivedMsg.occupiedNo);
              console.log("???" + receivedMsg.occupiedName);
              const idx = this.$store.state.noteList.findIndex(element => element._id == receivedMsg.noteNo);
              
              // this.$store.state.noteList[idx].occupiedNo = receivedMsg.occupiedNo;
              // this.$store.state.noteList[idx].occupiedName = receivedMsg.occupiedName;
              this.$store.state.selectedNoteInfo.occupiedNo = receivedMsg.occupiedNo;
              this.$store.state.selectedNoteInfo.occupiedName = receivedMsg.occupiedName;

              this.$store.dispatch('getNoteList', this.bandInfo)
            } 
          )

          this.stompClient.subscribe("/send/groupSend/vacate/" + this.$store.state.selectedBandInfo.no,
            res => {
              const receivedMsg = JSON.parse(res.body);
              
              console.log(receivedMsg)
              console.log(receivedMsg.noteNo)
              console.log(receivedMsg.occupiedNo)
              console.log(receivedMsg.content)


              // fileList를 돌며, res.body.file_id를 통해 row를 찾아, 해당 row의 account_no와 account_name을 바꿔준다.
              //      >> 누가 점유했다는 메세지를 받으면 -> fileList에서 해당 file을 찾아, accountNo와 accountName을 각각 점유자의 accountNo, accountName으로 덮어씌울거고,
              //      >> 누가 점유를 포기했다는 메세지를 받으면 -> fileList에서 해당 file을 찾아, accountNo와 accountName을 각각 0, ""으로 덮어씌울 것임.
              console.log("this.$store.state.noteList", this.$store.state.noteList)
              console.log("너 어딨음 ?? " + receivedMsg.noteNo);
              console.log("???" + receivedMsg.noteNo);
              console.log("???" + receivedMsg.occupiedNo);
              console.log("???" + receivedMsg.occupiedName);
              var idx = this.$store.state.noteList.findIndex(element => element._id == receivedMsg.noteNo);
              
              // this.$store.state.noteList[idx].occupiedNo = receivedMsg.occupiedNo;
              // this.$store.state.noteList[idx].occupiedName = receivedMsg.occupiedName;
              this.$store.state.selectedNoteInfo.occupiedNo = 0;
              this.$store.state.selectedNoteInfo.occupiedName = "";

              this.$store.dispatch('getNoteList', this.bandInfo)
            } 
          )

          this.stompClient.subscribe("/send/groupSend/content/" + this.$store.state.selectedBandInfo.no,
            res => {
              const receiveMsg = JSON.parse(res.body);
              
              var idx = this.$store.state.noteList.findIndex(element => element._id == receiveMsg.noteNo);
              this.$store.state.selectedNoteInfo.content = receiveMsg.content;

              if(this.$store.state.userInfo.no != receiveMsg.occupiedNo)                         {
                win.webContents.send("test", receiveMsg.content);
              }
                  const info = {
                    accountNo: receiveMsg.accountNo,
                    bandNo: receiveMsg.bandNo,
                    noteNo: receiveMsg.noteNo,
                    subject: receiveMsg.subject,
                    content: receiveMsg.content,
                    occupiedNo: receiveMsg.occupiedNo, // 점유자의 account_no
                    occupiedName: receiveMsg.occupiedName // 점유자의 account_name
                // console.log(info);
              }
                this.$store.commit('SET_NOTE_CONTENT', info);
            }
          )
          
        }
      );
      document.getElementById("serverFileName").style.display = "none";
    },

    // changeNote(note, _id){
    //   if(this.$store.state.savedTime != '')
    //     this.$store.state.savedTime = '';
    //   if(this.$store.state.shareNoteSocket.connected)
    //     this.$store.state.shareNoteSocket.disconnect();
    //   // console.log(this.$store.state.selectedBandInfo.no);
    //   // console.log(_id);
    //   const serverURL = "http://i3b104.p.ssafy.io:80/noteAPI/ws";
    //   let socket = new SockJS(serverURL);
    //   this.stompClient = Stomp.over(socket);
    //   const win = remote.BrowserWindow.getFocusedWindow();
    //   this.stompClient.connect(
    //     { Authorization: this.$store.state.authorization },
    //     frame => {
    //       this.connected = true;
    //       console.log(this.stompClient);
    //       this.$store.commit('setShareNoteSocket',this.stompClient);
    //       this.stompClient.subscribe("/send/groupSend/content/" + this.$store.state.selectedBandInfo.no + "/" + _id,
    //         res => {
    //           const receiveMsg = JSON.parse(res.body);

    //           var idx = this.$store.state.noteList.findIndex(element => element._id == receiveMsg.noteNo);
    //           this.$store.state.selectedNoteInfo.content = receiveMsg.content;

    //           if(this.$store.state.userInfo.no != receiveMsg.occupiedNo)                         {
    //             win.webContents.send("test", receiveMsg.content);
    //           }
    //               const info = {
    //                 accountNo: receiveMsg.accountNo,
    //                 bandNo: receiveMsg.bandNo,
    //                 noteNo: receiveMsg.noteNo,
    //                 subject: receiveMsg.subject,
    //                 content: receiveMsg.content,
    //                 occupiedNo: receiveMsg.occupiedNo, // 점유자의 account_no
    //                 occupiedName: receiveMsg.occupiedName // 점유자의 account_name
    //             // console.log(info);
    //           }
    //             this.$store.commit('SET_NOTE_CONTENT', info);
    //         }
    //       )
    //     }
    //   );
    // },


    deleteWorkspace() {
      this.$refs.bandMenu.close();

      const deleteWorkspace = {
        bandNo: this.bandInfo.no,
        accountNo: this.$store.state.userInfo.no,
      };

      this.$store.dispatch("deleteWorkspace", deleteWorkspace);
      this.$store.commit('SELECTED_WORKSPACE', '')
      this.bandInfo = this.$store.state.selectedBandInfo;
      this.$store.dispatch('getNoteList', this.bandInfo)
    },

    showRenameDialog() {
      this.workspaceRenameDialog = true;
    },

    renameWorkspace() {
      const renameWorkspace = {
        bandNo: this.bandInfo.no,
        accountNo: this.$store.state.userInfo.no,
        newBandName: this.bandInfo.name,
      };

      this.$store.dispatch("renameWorkspace", renameWorkspace)
      this.workspaceRenameDialog = false;
    },

    showInviteModal() {
      // console.log("전체 워크스페이스", this.$store.state.userInfo.group)
      // var workspaceNo = this.$store.state.userInfo.group.find(element => element.name == this.$store.state.workspace).no;
      // this.workspaceNo = workspaceNo;

      // console.log("여기workspaceNo : " + workspaceNo);

      const showGroupMembers = {
        bandNo: this.bandInfo.no,
        email: this.$store.state.userInfo.email
      };

      this.$store.dispatch("showGroupMembers", showGroupMembers);
    },

    openRenameFileModal() {
      this.$store.commit("SET_IS_RENAME_FILE_MODAL", true);
    },

    setRightNoteInfo(noteInfo) {
      this.$store.commit("RIGHT_SELECTED_NOTE", noteInfo);
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
  /* border-bottom: solid 3px rgb(100, 93, 102); */
  display: inline-block;
  margin: 1em 0 0 0;
  padding: 0 0.75em 0.5em 0;
}

.v-subheader:not(.sidesubheader) {
  font-size: 1.25em;
  /* color: rgb(95, 90, 97) !important; */
  /* border-bottom: solid 2px rgb(83, 81, 83); */
  display: inline-block;
  margin: 0.5em;
  padding: 0.75em 0;
}

.theme--light.v-list-item:not(.v-list-item--active) {
  /* color: #615f75 !important; */
  font-size: 0.5em;
}

.theme--light.v-list-item:not(.v-list-item--active):not(.v-list-item--disabled):hover {
  color: rgb(255, 179, 102) !important;
  /* color: rgb(214, 198, 219) !important; */
}

.theme--dark.v-list-item:not(.v-list-item--active) {
  /* color: #615f75 !important; */
  font-size: 0.5em;
}

.theme--dark.v-list-item:not(.v-list-item--active):not(.v-list-item--disabled):hover {
  color: rgb(255, 179, 102) !important;
  /* color: rgb(214, 198, 219) !important; */
}
</style>