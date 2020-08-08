<template>
  <v-navigation-drawer
      v-model="$store.state.drawer"
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
</template>

<script>
import { remote, ipcRenderer } from "electron";
import fs from "fs";
import path from "path";

export default {
   mounted() {
    ipcRenderer.on('ping', (event, message) => {
      var absoluteFilePath = message['absoluteFilePath'];
      // console.log('absolute', absoluteFilePath);
      var folderFullPath = path.dirname(absoluteFilePath);
      var folderName = path.basename(path.dirname(absoluteFilePath));

      // console.log("folderFullPath : " + folderFullPath);
      // console.log("folderName : " + folderName);
      this.folders = [];
      this.folders.push({icon: 'folder',  iconClass: 'grey lighten-1 white--text', title: folderName});

      fs.readdir(folderFullPath, (err, fileList) => {
        this.files = [];

        // console.log('filelist', fileList);

        for(var i = 0; i < fileList.length; i++) {
          // console.log(folderFullPath + "\\" + fileList[i]);
          if(fileList[i].substring(fileList[i].length-3, fileList[i].length) === '.md')
            this.files.push({ icon: 'assignment', iconClass: 'blue white--text', title: fileList[i], fileFullPath: folderFullPath + "\\" + fileList[i]});
        }
      })
    });
  },
    data() {
        return {
            dialog: false,
            folders: [

            ],
            files: [
            ]
        }
    },
    methods: {
        showOpenDialog() {
      // console.log("showOpenDialog() 호출됨.");

      remote.dialog.showOpenDialog(remote.BrowserWindow.getFocusedWindow(),
        {
          properties: ["openDirectory"]
        }
      )
      .then(result => {
        var folderFullPath = result.filePaths[0];

        var folderName = folderFullPath.substring(folderFullPath.lastIndexOf("\\") + 1);
        
        var isWindow = navigator.platform.indexOf('Win') > -1;
        var isMac = navigator.platform.indexOf('Mac') > -1;
        // console.log("folderFullPath : " + folderFullPath);
        // console.log("folderName : " + folderName);
    
    // 맥과 윈도우는 폴더 경로 들어갈때 다름.
    // 예)
    // 맥 : /Users/kimmuseong/Desktop/폴더1
    // 윈도우 : C:\kimmuseong\Desktop\폴더1
        if(isWindow) {
          folderName = folderFullPath.substring(folderFullPath.lastIndexOf("\\") + 1);
        }
        if(isMac) {
          folderName = folderFullPath.substring(folderFullPath.lastIndexOf("/") + 1);
        }

        this.folders = [];
        this.folders.push({icon: 'folder',  iconClass: 'grey lighten-1 white--text', title: folderName});

        fs.readdir(folderFullPath, (err, fileList) => {
          this.files = [];

          // console.log(fileList);

          for(var i = 0; i < fileList.length; i++) {
            // console.log(folderFullPath + "\\" + fileList[i]);
            if(fileList[i].substring(fileList[i].length-3, fileList[i].length) === '.md') {
                if(isWindow)
                    this.files.push({ icon: 'assignment', iconClass: 'blue white--text', title: fileList[i], fileFullPath: folderFullPath + "\\" + fileList[i]});
                if(isMac)
                    this.files.push({ icon: 'assignment', iconClass: 'blue white--text', title: fileList[i], fileFullPath: folderFullPath + "/" + fileList[i]});
            }

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

        let fileDataObject = {'openedFileData': openedFileData, 'absoluteFilePath': absoluteFilePath};
        let win = remote.BrowserWindow.getFocusedWindow();
        win.webContents.send("ping", fileDataObject);
        // console.log('absolutefilepath', absoluteFilePath);
      });
    }
    }

}
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
    font-family: 'Roboto Slab', sans-serif;
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
    font-size:0.5em;
  }

  .theme--light.v-list-item:not(.v-list-item--active):not(.v-list-item--disabled):hover {
    color: rgb(214, 198, 219) !important;
  }
</style>