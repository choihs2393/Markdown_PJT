<template>
  <v-app id="inspire">

    <NavBar />
    <router-view />
    
  </v-app>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';

import NavBar from "./components/NavBar.vue"
import SideBar from "./components/SideBar.vue"

// 드래그 후 드랍을 하면,
document.addEventListener('drop', (event) => {
    var openedFileData = '';
    var textingFileData = '';

    event.preventDefault();
    event.stopPropagation();
    
    remote.BrowserWindow.getFocusedWindow().webContents.executeJavaScript(`document.getElementById("editor_textarea").value`)
    .then(result => {
        textingFileData = result;

        // console.log(typeof fileData);
        // console.log(fileData);

        // 작성중인 텍스트가 있다면, 저장할건지 먼저 물어본다.
        if(textingFileData.length > 0) {
          const options = {
              type: "question",
              title: "Question",
              message: "Are you sure you want to quit without saving?",
              detail: "Click the save button if you want to save this text to your md file",
              buttons: ["Cancel", "Save"],
              defaultId: 1
          };
    
          // 동기식으로 처리해야 할듯?
          if(remote.dialog.showMessageBoxSync(options) == 1) {
            remote.dialog.showSaveDialog(
              {
                title: "파일 저장하기!!!",
                filters: [
                  { name: 'Markdown', extensions: ['md'] },
                ],
                message: "TEST"
              }
            )
            .then(result => {
              var fileName = result.filePath;
              fs.writeFile(fileName, textingFileData, (err) => {
  
              })
            });
          }
        } // 작성중인 텍스트가 있다는 조건문
    });

    // 저장한 후에 열기.
    for (const f of event.dataTransfer.files) { 
      // Using the path attribute to get absolute file path 
      // console.log('File Path of dragged files: ', f.path);
      
      fs.readFile(f.path, 'utf8', (err, data) => {
        if(err) throw err;
        // console.log(data);
        // fileData = data;
        let openedFileData = data;
        // console.log("openedFileData : " + openedFileData);

        let win = remote.BrowserWindow.getFocusedWindow();
        win.webContents.send("ping", openedFileData);
      });
    }
});

export default {
  name: "App",

    components: {
      NavBar,
      SideBar
    },

    props: {
      source: String,
    },

  created() {
    console.log("created()");
  },

  updated(){
    console.log("updated()");
    var div = document.getElementById("compiledMarkdown");
    if(div != null) {
      if(this.$vuetify.theme.dark == true)
        
        div.style.color = "white";
      else
        div.style.color = "black";
    }
  },

  computed: {
    ...mapGetters(['isLoggedIn']),
  },

  data () {
    return {
      theme: this.$vuetify.theme.dark,
    }
  },
  methods: {
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
.v-list {
  padding: 0;
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
    margin: 0.5em;
    padding: 0.75em 0;
}
</style>
