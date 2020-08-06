<template>
  <v-app id="inspire">

    <v-app-bar app elevate-on-scroll>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
      <v-toolbar-title>Markdown</v-toolbar-title>
      <v-spacer></v-spacer>
      
      <!-- 로그인 전 화면의 상단바 -->
      <template v-if="!isLoggedIn">
        <LoginModal />
        <SignupModal />
      </template>

      <!-- 로그인 후 화면의 상단바 -->
      <template v-if="isLoggedIn">
        <div>
          <span>{{ $store.state.userEmail }}님</span>
        </div>
        <LogoutModal />
      </template>

      <v-switch
        v-model="$vuetify.theme.dark"
        hide-details
        label="Theme Dark"
      ></v-switch>

    </v-app-bar>

    <v-navigation-drawer
      v-model="drawer"
      app>
      <v-card>
        <v-toolbar
            prominent
            src="https://images.unsplash.com/photo-1489781879256-fa824b56f24f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80"
            >
            <!-- <v-app-bar-nav-icon></v-app-bar-nav-icon> -->
            <v-subheader class="sidesubheader">
            <v-toolbar-title>MY FILES</v-toolbar-title>
            </v-subheader>
            <v-spacer></v-spacer>
            <!-- <v-btn icon
              @click="dialog = !dialog"
            > -->
            <v-btn icon @click="showOpenDialog()">
              <v-icon>mdi-plus</v-icon>
            </v-btn>
            <v-btn icon>
              <v-icon>mdi-magnify</v-icon>
            </v-btn>
        </v-toolbar>

        <v-list subheader flat>
          <v-subheader >FOLDERS</v-subheader>

            <v-list-item 
              v-for="folder in folders" 
              :key="folder.title"
              disabled>
              <!-- <v-list-item-avatar>
                <v-icon :class="[folder.iconClass]">{{ folder.icon }}</v-icon>
              </v-list-item-avatar> -->
              <v-list-item-content>
                <v-list-item-title>{{ folder.title }}</v-list-item-title>

                <v-list-item-subtitle>{{ folder.subtitle }}</v-list-item-subtitle>
              </v-list-item-content>

              <!-- <v-list-item-action>
                <v-btn icon>
                  <v-icon color="grey lighten-1">mdi-information</v-icon>
                </v-btn>
              </v-list-item-action> -->
            </v-list-item>

            <v-divider ></v-divider>

            <v-subheader >FILES</v-subheader>

            <v-list-item v-for="file in files" :key="file.title" @click="openFile(file.fileFullPath)">
                <!-- <v-list-item-avatar>
                <v-icon :class="[file.iconClass]">{{ file.icon }}</v-icon>
              </v-list-item-avatar> -->
              <v-list-item-content>
                <v-list-item-title>{{ file.title }}</v-list-item-title>

                <v-list-item-subtitle>{{ file.subtitle }}</v-list-item-subtitle>
              </v-list-item-content>

              <!-- <v-list-item-action>
                <v-btn icon ripple @click="openFile(file.fileFullPath)">
                  <v-icon color="grey lighten-1">mdi-information</v-icon>
                </v-btn>
              </v-list-item-action> -->
            </v-list-item>
          </v-list>

        <v-dialog v-model="dialog" max-width="500px">
          <v-card>
            <v-card-text>
              <v-text-field label="File name"></v-text-field>
              <small class="grey--text">* This doesn't actually save.</small>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>

              <v-btn text color="primary" @click="dialog = false">Submit</v-btn>
            </v-card-actions>
          </v-card>
      </v-dialog>

      </v-card>
    </v-navigation-drawer>

    <router-view />
  </v-app>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import { remote } from "electron";

const fs = require("fs");
import LoginModal from "./components/LoginModal.vue"
import SignupModal from "./components/SignupModal.vue"
import LogoutModal from "./components/LogoutModal.vue"

export default {
  name: "App",
  created() {
  },
  updated(){
    var div = document.getElementById("compiledMarkdown");
    if(this.$vuetify.theme.dark == true)
      div.style.color = "white";
    else
      div.style.color = "black";
  },
  computed: {
    ...mapGetters(['isLoggedIn']),
  },
  components: {
    LoginModal,
    SignupModal,
    LogoutModal
  },
  props: {
    source: String,
  },
  data () {
    return {
      dialog: false,
      drawer: false,
      theme: this.$vuetify.theme.dark,
      folders: [
        // { icon: 'folder', iconClass: 'grey lighten-1 white--text', title: 'Photos', subtitle: 'Jan 9, 2014' },
        // { icon: 'folder', iconClass: 'grey lighten-1 white--text', title: 'Recipes', subtitle: 'Jan 17, 2014' },
        // { icon: 'folder', iconClass: 'grey lighten-1 white--text', title: 'Work', subtitle: 'Jan 28, 2014' },
      ],
      files: [
        // { icon: 'assignment', iconClass: 'blue white--text', title: 'Vacation itinerary', subtitle: 'Jan 20, 2014' },
        // { icon: 'call_to_action', iconClass: 'amber white--text', title: 'Kitchen remodel', subtitle: 'Jan 10, 2014' },
      ],
    }
  },
  methods: {
    showOpenDialog() {
      console.log("showOpenDialog() 호출됨.");

      remote.dialog.showOpenDialog(remote.BrowserWindow.getFocusedWindow(),
        {
          properties: ["openDirectory"]
        }
      )
      .then(result => {
        var folderFullPath = result.filePaths[0];

        var folderName = folderFullPath.substring(folderFullPath.lastIndexOf("\\") + 1);

        console.log("folderFullPath : " + folderFullPath);
        console.log("folderName : " + folderName);

        this.folders = [];
        this.folders.push({icon: 'folder',  iconClass: 'grey lighten-1 white--text', title: folderName});

        fs.readdir(folderFullPath, (err, fileList) => {
          this.files = [];

          console.log(fileList);

          for(var i = 0; i < fileList.length; i++) {
            console.log(folderFullPath + "\\" + fileList[i]);

            this.files.push({ icon: 'assignment', iconClass: 'blue white--text', title: fileList[i], fileFullPath: folderFullPath + "\\" + fileList[i]});

          }
        })
      });
    },

    openFile(absoluteFilePath) {
      fs.readFile(absoluteFilePath, 'utf8', (err, data) => {
        if(err) throw err;
        // console.log(data);
        // fileData = data;
        let openedFileData = data;
        // console.log(openedFileData);

        let win = remote.BrowserWindow.getFocusedWindow();
        win.webContents.send("ping", openedFileData);
      });
    }
  },
  watch: {
  }
};
</script>

<style scoped>
html, body {
  margin: 0;
  height: 100%;
  font-family: 'Helvetica Neue', Arial, sans-serif;
  color: #333;
}
.sidesubheader {
    border-bottom: solid 3px rgb(100, 93, 102);
    display: inline-block;
    margin: 2.5em 0 0 0;
    padding: 0 0.75em 0.5em 0;
}
.theme--light.v-list-item:not(.v-list-item--active) {
    color: #615f75 !important;
    font-size:0.5em;
}
.theme--light.v-list-item:not(.v-list-item--active):not(.v-list-item--disabled):hover {
    color: rgb(214, 198, 219) !important;
}
.v-list-item {
    margin: 0.5em 2em 0;
    padding: 1em 0;
    height: 1em;
}
.v-list-item:not(:last-child) {
    font-family: 'Roboto Slab', sans-serif;
    border-bottom: solid 1px rgba(210, 215, 217, 0.75);
}
.v-subheader:not(.sidesubheader) {
    font-size: 1.25em;
    color: rgb(95, 90, 97) !important;
    border-bottom: solid 2px rgb(83, 81, 83);
    display: inline-block;
    margin: 1em 0.5em 0 0.5em;
    padding: 0.75em 0;
}
</style>
