<template>
  <v-navigation-drawer v-model="$store.state.drawer" app>
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
          <v-icon>fas fa-file-import</v-icon>
        </v-btn>
      </v-toolbar>
      <v-container>
        <v-list subheader flat>
          <v-subheader>FOLDERS</v-subheader>

          <v-list-item v-for="folder in folders" :key="folder.title">
            <v-list-item-content>
              <v-list-item-title>{{ folder.title }}</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <v-divider></v-divider>

          <v-row style="margin-left:0px" align="center">
            <v-subheader>FILES</v-subheader>
            <!-- <v-btn v-if="isNewFile" style="margin-left: 60px;" text color="grey darken-1" tile @click="addNewFile()"> -->
            <v-spacer></v-spacer>
            <v-btn
              class="mr-3 mt-3"
              icon
              v-if="isNewFile"
              @click="addNewFile()"
            >
              <v-icon>mdi-plus</v-icon>
            </v-btn>
          </v-row>
          <v-list-item
            v-for="file in files"
            :key="file.title"
            @click="openFile(file.fileFullPath, file)"
          >
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
      </v-container>
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
</template>

<script>
import { remote, ipcRenderer } from "electron";
import fs from "fs";
import path from "path";

export default {
  mounted() {
    var isWindow = navigator.platform.indexOf("Win") > -1;
    var isMac = navigator.platform.indexOf("Mac") > -1;

    ipcRenderer.on("ping", (event, message) => {
      var absoluteFilePath = message["absoluteFilePath"];
      var folderFullPath = path.dirname(absoluteFilePath);
      var folderName = path.basename(path.dirname(absoluteFilePath));

      this.folders = [];
      this.folders.push({
        icon: "folder",
        iconClass: "grey lighten-1 white--text",
        title: folderName
      });

      fs.readdir(folderFullPath, (err, fileList) => {
        this.files = [];

        for (var i = 0; i < fileList.length; i++) {
          if (
            fileList[i].substring(
              fileList[i].length - 3,
              fileList[i].length
            ) === ".md"
          ) {
            if (isWindow) {
              this.files.push({
                icon: "assignment",
                iconClass: "blue white--text",
                title: fileList[i],
                fileFullPath: folderFullPath + "\\" + fileList[i]
              });
            }
            if (isMac) {
              this.files.push({
                icon: "assignment",
                iconClass: "blue white--text",
                title: fileList[i],
                fileFullPath: folderFullPath + "/" + fileList[i]
              });
            }
          }
        }
      });
      ipcRenderer.send("mainping", message);
    });

    ipcRenderer.on("addFileInList", (event, folderFullPath) => {
      fs.readdir(folderFullPath, (err, fileList) => {
        this.files = [];
        if (fileList) {
          for (var i = 0; i < fileList.length; i++) {
            if (
              fileList[i].substring(
                fileList[i].length - 3,
                fileList[i].length
              ) === ".md"
            )
              if (isWindow) {
                this.files.push({
                  icon: "assignment",
                  iconClass: "blue white--text",
                  title: fileList[i],
                  fileFullPath: folderFullPath + "\\" + fileList[i]
                });
              }
            if (isMac) {
              this.files.push({
                icon: "assignment",
                iconClass: "blue white--text",
                title: fileList[i],
                fileFullPath: folderFullPath + "/" + fileList[i]
              });
            }
          }
        }
      });
    });
  },
  data() {
    return {
      dialog: false,
      isNewFile: false,
      folders: [],
      files: [],
      folderFullPath: ""
    };
  },
  methods: {
    addNewFile() {
      if (this.folderFullPath != "") {
        let win = remote.BrowserWindow.getFocusedWindow();
        win.webContents.send("newFile");
      }
    },

    showOpenDialog() {
      remote.dialog
        .showOpenDialog(remote.BrowserWindow.getFocusedWindow(), {
          properties: ["openDirectory"]
        })
        .then(result => {
          var folderFullPath = result.filePaths[0];

          var folderName = folderFullPath.substring(
            folderFullPath.lastIndexOf("\\") + 1
          );

          var isWindow = navigator.platform.indexOf("Win") > -1;
          var isMac = navigator.platform.indexOf("Mac") > -1;

          // 맥과 윈도우는 폴더 경로 들어갈때 다름.
          // 예)
          // 맥 : /Users/kimmuseong/Desktop/폴더1
          // 윈도우 : C:\kimmuseong\Desktop\폴더1
          if (isWindow) {
            folderName = folderFullPath.substring(
              folderFullPath.lastIndexOf("\\") + 1
            );
          }
          if (isMac) {
            folderName = folderFullPath.substring(
              folderFullPath.lastIndexOf("/") + 1
            );
          }

          this.folders = [];
          this.folders.push({
            icon: "folder",
            iconClass: "grey lighten-1 white--text",
            title: folderName
          });

          fs.readdir(folderFullPath, (err, fileList) => {
            this.files = [];

            for (var i = 0; i < fileList.length; i++) {
              if (
                fileList[i].substring(
                  fileList[i].length - 3,
                  fileList[i].length
                ) === ".md"
              ) {
                if (isWindow)
                  this.files.push({
                    icon: "assignment",
                    iconClass: "blue white--text",
                    title: fileList[i],
                    fileFullPath: folderFullPath + "\\" + fileList[i]
                  });
                if (isMac)
                  this.files.push({
                    icon: "assignment",
                    iconClass: "blue white--text",
                    title: fileList[i],
                    fileFullPath: folderFullPath + "/" + fileList[i]
                  });
              }
            }
          });
          let win = remote.BrowserWindow.getFocusedWindow();
          this.folderFullPath = folderFullPath;
          win.webContents.send("pong", folderFullPath);
          win.webContents.send("contentReset", "msg");

          document.getElementById("localFileName").innerHTML = "";
          this.isNewFile = true;
          // document.getElementById("serverFileName").innerHTML(file.title);
        });
    },

    openFile(absoluteFilePath, file) {
      var folderFullPath = path.dirname(absoluteFilePath);
      var isWindow = navigator.platform.indexOf("Win") > -1;
      var isMac = navigator.platform.indexOf("Mac") > -1;
      fs.readdir(folderFullPath, (err, fileList) => {
        this.files = [];

        for (var i = 0; i < fileList.length; i++) {
          if (
            fileList[i].substring(
              fileList[i].length - 3,
              fileList[i].length
            ) === ".md"
          ) {
            if (isWindow) {
              this.files.push({
                icon: "assignment",
                iconClass: "blue white--text",
                title: fileList[i],
                fileFullPath: folderFullPath + "\\" + fileList[i]
              });
            }
            if (isMac) {
              this.files.push({
                icon: "assignment",
                iconClass: "blue white--text",
                title: fileList[i],
                fileFullPath: folderFullPath + "/" + fileList[i]
              });
            }
          }
        }
      });
      fs.readFile(absoluteFilePath, "utf8", (err, data) => {
        // if(err) throw err;
        // fileData = data;
        let openedFileData = data;

        let fileDataObject = {
          openedFileData: openedFileData,
          absoluteFilePath: absoluteFilePath
        };
        let win = remote.BrowserWindow.getFocusedWindow();
        win.webContents.send("ping", fileDataObject);
        ipcRenderer.send("mainping", fileDataObject);

        document.getElementById("localFileName").innerHTML = file.title;
      });
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
  margin: 2.5em 0 0 0;
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
  /* color: rgb(214, 198, 219) !important; */
  color: rgb(255, 179, 102) !important;
}

.theme--dark.v-list-item:not(.v-list-item--active) {
  /* color: #615f75 !important; */
  font-size: 0.5em;
}

.theme--dark.v-list-item:not(.v-list-item--active):not(.v-list-item--disabled):hover {
  /* color: rgb(214, 198, 219) !important; */
  color: rgb(255, 179, 102) !important;
}
</style>
