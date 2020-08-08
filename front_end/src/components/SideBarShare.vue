<template>
  <v-navigation-drawer v-model="$store.state.drawerShare" app>
    <v-card>
      <v-toolbar prominent src="https://images.unsplash.com/photo-1529156069898-49953e39b3ac?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2089&q=80">
        <!-- <v-app-bar-nav-icon></v-app-bar-nav-icon> -->
        <v-subheader class="sidesubheader">
          <v-toolbar-title>SHARES</v-toolbar-title>
        </v-subheader>
        <v-spacer></v-spacer>
        <v-btn icon @click="showInviteModal()">
          <v-icon>mdi-plus</v-icon>
        </v-btn>
        <InviteModal />
      </v-toolbar>

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
      </template>

      <v-card>
        <v-card-title>
          <span>Add a group member</span>
        </v-card-title>
        <v-card-text>
          <v-form ref="form">
            <v-row>
              <v-col cols="12" sm="20">
                <v-select :items="groups" item-text="groupName" label="Select a group"></v-select>
              </v-col>
              <v-col cols="12" sm="20" md="20">
                <v-text-field label="email *" required v-model="email"></v-text-field>
              </v-col>
            </v-row>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn @click="closeMemberDialog">Close</v-btn>
          <v-btn :id=group.groupName @click="addMember(group.groupName)">Add</v-btn>
          <v-btn :id="groupName" :value="$attrs" @click="addMember(group.groupName)">Add</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog> -->
  </v-navigation-drawer>
</template>

<script>
import { remote } from "electron";
import fs from "fs";
import InviteModal from "./InviteModal.vue";

export default {
  data() {
    return {
      dialog: false,
      memberDialog: false,
      groupName: "",
      email: "",
      groups: []
    };
  },
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
        this.$store.state.isInviteModal = !this.$store.state.isInviteModal;
        }else{
        alert('workspace부터 골라')
      }
    },
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
  }
};
</script>

<style>
</style>