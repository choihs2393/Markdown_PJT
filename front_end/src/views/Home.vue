<template>
  <v-main>
    <v-container class="md" fluid>
      <v-btn v-if="isLoggedIn" @click="saveNote(input)">없어질 버튼</v-btn>
      <!-- <v-btn v-if="isLoggedIn && !isOccupied" @click="occupy(this.$store.state.selectedNoteNo)">점유 하기</v-btn>
      <v-btn v-if="isLoggedIn && isOccupied" @click="vacate(this.$store.state.selectedNoteNo)">점유 끊기</v-btn> -->
      <div id="editor_div">
        <v-textarea
          solo
          flat
          auto-grow
          ref="textarea"
          v-model="input"
          id="editor_textarea"
          @keyup.native="whenKeyUp"
          onkeydown="
            if(event.keyCode === 9) {
              var v=this.value, s=this.selectionStart, e=this.selectionEnd;
              this.value=v.substring(0, s)+'\t'+v.substring(e);
              this.selectionStart=this.selectionEnd=s+1;
              
              return false;
            }"
        />
        <div id="compiledMarkdown" class="compiledMarkdown" v-html="compiledMarkdown"></div>
      </div>

      <!-- <v-row
        align="center"
        justify="center"
      >
      </v-row>-->
    </v-container>
  </v-main>
</template>

<script>
// @ is an alias to /src
import parse from "../markdown/parse";
import sampleData from "../markdown/sampleData.js";
import readmeTemplate from "../markdown/readmeTemplate.js";

import { mapActions, mapGetters } from "vuex";

import { remote, ipcRenderer } from "electron";
import fs from "fs";
import path from "path";

var data = sampleData;

// var data = new Promise(function(resolve, reject) {
//   resolve(sampleData);
// });
ipcRenderer.on("template", (event, message) => {
  // console.log(message);
  if (message){
    data.input = readmeTemplate.input;
  }
});

ipcRenderer.on("ping", (event, message) => {
  // console.log(message);
  data.input = message["openedFileData"];
});

ipcRenderer.on("getNote", (event, message, accountNo) => {
  // console.log(message);
  data.input = message;
  // if(accountNo != 0) {
  //   this.isOccupied = true;
  //   document.getElementById("editor_div").setAttribute("disabled", true);
  // } else {
  //   this.isOccupied = false;
  //   document.getElementById("editor_div").setAttribute("disabled", false);
  // }
});

// 드래그 후 드랍을 하면,
document.addEventListener("drop", event => {
  var openedFileData = "";
  var textingFileData = "";

  event.preventDefault();
  event.stopPropagation();

  remote.BrowserWindow.getFocusedWindow().webContents.executeJavaScript(`document.getElementById("editor_textarea").value`)
    .then(result => {
      textingFileData = result;

      // console.log(typeof fileData);
      // console.log(fileData);

      // 작성중인 텍스트가 있다면, 저장할건지 먼저 물어본다.
      if (textingFileData.length > 0) {
        const options = {
          type: "question",
          title: "Question",
          message: "Are you sure you want to quit without saving?",
          detail: "Click the save button if you want to save this text to your md file",
          buttons: ["Cancel", "Save"],
          defaultId: 1
        };

        // 동기식으로 처리해야 할듯?
        if (remote.dialog.showMessageBoxSync(options) == 1) {
          remote.dialog.showSaveDialog(
            {
              title: "파일 저장하기!!!",
              filters: [
                { name: "Markdown", extensions: ["md"] }
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

    fs.readFile(f.path, "utf8", (err, data) => {
      if (err) throw err;
      // console.log('f.path', f.path)
      // console.log(data);
      // fileData = data;
      let openedFileData = data;
      // console.log("openedFileData : " + openedFileData);

      let fileDataObject = {
        openedFileData: openedFileData,
        absoluteFilePath: f.path
      };
      let win = remote.BrowserWindow.getFocusedWindow();
      win.webContents.send("ping", fileDataObject);
    });
  }
});

export default {
  name: "Home",
  components: {},
  created() {
    //data.then(function(tmp) {
    //return this.input = tmp;
    // })
    //  data.then((tmp) => {
    //     console.log(tmp);
    //     this.input = tmp;
    //}).then((tmp) => {
    //   console.log(this.input);
    // return this.$store.state.parseData;
    //return parse(this.input);
    // });
  },
  updated() {
    //this.$store.commit('setParseData', parse(this.input));
  },
  mounted() {
    // console.log(data);
    // this.$store.state.inputData = data.input;
    // this.$store.state.parseData = parse(data);
    this.$store.commit("setParseData", parse(this.input));
    // console.log(this.$store.state.parseData);
  },

  computed: {
    compiledMarkdown: function() {
      // data.then((tmp) => {
      // console.log(tmp);
      //  this.input = tmp;
      //}).then((tmp) => {
      //   console.log(this.input);
      // this.$store.state.parseData = parse(tmp);

      //  this.$store.commit('setParseData', parse(this.input));
      return this.$store.state.parseData;
      //return parse(this.input);
      // });
    },
    ...mapGetters(["isLoggedIn"]),
    // isOccupied() {
    //   return this.isOccupied;
    // }

    //  ...mapGetters(['inputData']),
  },
  // data: {
  //   input: data
  // },
  data() {
    return data;
  },
  // data: async () => {
  //   console.log(data);
  //   return await data;
  // }
  methods: {
    ...mapActions(["saveNote"]),

    whenKeyUp() {
      var tmp = event.target.value;
      var getTempData = this.$store.state.tempData;
      var getStore = this.$store;

      //server mode 인지 아닌지 판단하기 위한 variable
      //local mode 라면 timeout 200 
      //server mode 라면 tiemout * 

      if(this.$store.state.syncCheck === false) { 
        this.$store.commit('setSyncCheck',true);
        var timer = setTimeout(() => {
          // clearTimeout(getTempData);
          parse(tmp);
          console.log("ㅋㅋㅋㅋㅋ" + tmp);
          getStore.commit('setSyncCheck',false);
          
        }, 800);
        this.$store.commit('setTimer',timer)
      }
      clearTimeout(this.$store.state.tempData);
      //parse(event.target.value);
      var timeOut = setTimeout(() => {
        parse(tmp);
        this.$store.commit('setSyncCheck', false);
        clearTimeout(this.$store.state.timer);
      }, 200);
      this.$store.commit('setTempData',timeOut);

      let bandNo = this.$store.state.selectedBandInfo.no;
      let noteNo = this.$store.state.selectedNoteInfo._id;
      let occupyAccountNo = 1;
      let loginAccountNo = this.$store.state.userInfo.no;
      console.log("영복이의 로그추적"  + bandNo);
      console.log("영복이의 로그추적 " + noteNo);
      console.log("영복이의 로그추적 " + occupyAccountNo);
      console.log("영복이의 로그추적 " + loginAccountNo);
      console.log("영복이의 로그추적 " + tmp);
      if(noteNo !== '' && bandNo !== '' && occupyAccountNo !== '' && occupyAccountNo === loginAccountNo){
        if(this.$store.state.storeSyncCheck === false){
          this.$store.commit('setStoreSyncCheck', true);
          let storeTimer = setTimeout(() => {
            // clearTimeout(this.$store.state.storeTempData);
            console.log('로직 안 this22222', this);
            console.log("영복이의 로그추적 " + tmp);
            this.$store.dispatch('saveNote', tmp);
            this.$store.commit('setStoreSyncCheck', false);
          }, 1000 * 60 * 5);
           this.$store.commit('setStoreTimer',storeTimer)
        }
        clearTimeout(this.$store.state.storeTempData);
        let timeOut_ = setTimeout(() => {
            console.log('로직 안 this', this);
            console.log("영복이의 로그추적 " + tmp);
            this.$store.dispatch('saveNote', tmp);
            this.$store.commit('setStoreSyncCheck', false);
            // console.log(this.$store.state.stroeTimer);
            clearTimeout(this.$store.state.storeTimer);
        }, 1000 * 60 * 3);
        this.$store.commit('setStoreTempData', timeOut_);
      }
    },
    
    // 해당 파일 점유하기.
    // occupy(selectedNoteNo) {
    //   // 1. 소켓 뚫기
    //   const serverURL = "http://localhost:8080/noteAPI/ws";
    //   // const serverURL = "http://i3b104.p.ssafy.io:80/noteAPI/ws";
    //   let socket = new SockJS(serverURL);
    //   this.stompClient = Stomp.over(socket);

    //   // 2. 소켓 연결하기
    //   this.stompClient.connect({ Authorization: this.$store.state.authorization },
    //     frame => {
    //       var map = {
    //           noteId: selectedNoteNo,
    //           accountNo: this.$store.state.userInfo.no,
    //           accountName: this.$store.state.userInfo.name
    //       }
                   
    //       // 3. 소켓을 통해 다른 그룹원들에게 '내가 점유하고 있다'고 send하기
    //       this.stompClient.send("/groupReceive/occupy/" + this.$store.state.workspaceNo, JSON.stringify(map));
    //     }
    //   )

    //   // 4. send 했으면, 소켓 disconnect를 진행해준다.
    //   this.stompClient.disconnect();
    // },

    // 해당 파일 점유 포기하기.
    // vacate(selectedNoteNo) {
    //   // 1. 소켓 뚫기
    //   const serverURL = "http://localhost:8080/noteAPI/ws";
    //   // const serverURL = "http://i3b104.p.ssafy.io:80/noteAPI/ws";
    //   let socket = new SockJS(serverURL);
    //   this.stompClient = Stomp.over(socket);

    //   // 2. 소켓 연결하기
    //   this.stompClient.connect({ Authorization: this.$store.state.authorization },
    //     frame => {
    //       var map = {
    //           noteId: this.$store.state.selectedNoteNo,
    //           accountNo: this.$store.state.userInfo.no,
    //           accountName: this.$store.state.userInfo.name
    //       }

    //       // 3. 소켓을 통해 다른 그룹원들에게 '내가 점유하고 있다'고 send하기
    //       this.stompClient.send("/groupReceive/vacate/" + this.$store.state.workspaceNo, JSON.stringify(map));
    //     }
    //   )

    //   // 4. send 했으면, 소켓 disconnect를 진행해준다.
    //   this.stompClient.disconnect();
    // }
  }
};
</script>

<style scoped>
.v-container {
  background-color: #333;
}

#editor_div {
  margin: 0;
  height: 100%;
  font-family: "Helvetica Neue", Arial, sans-serif;
  color: #333;
}

#editor_div div {
  display: inline-block;
  width: 49%;
  height: 100%;
  vertical-align: top;
  box-sizing: border-box;
  padding: 0 20px;
}

.v-textarea {
  border: none;
  border-right: 1px solid #ccc;
  resize: none;
  outline: none;
  background-color: #f6f6f6;
  font-size: 14px;
  font-family: "Monaco", courier, monospace;
  padding: 20px;
}

code {
  color: #f66;
}

.compiledMarkdown >>> h1 {
  display: block;
  font-size: 2em;
  margin-top: 0.67em;
  margin-bottom: 0.67em;
  margin-left: 0;
  margin-right: 0;
  font-weight: bold;
}

.compiledMarkdown >>> h2 {
  display: block;
  font-size: 1.5em;
  margin-top: 0.83em;
  margin-bottom: 0.83em;
  margin-left: 0;
  margin-right: 0;
  font-weight: bold;
}

.compiledMarkdown >>> h3 {
  display: block;
  font-size: 1.17em;
  margin-top: 1em;
  margin-bottom: 1em;
  margin-left: 0;
  margin-right: 0;
  font-weight: bold;
}

.compiledMarkdown >>> h4 {
  display: block;
  font-size: 1em;
  margin-top: 1.33em;
  margin-bottom: 1.33em;
  margin-left: 0;
  margin-right: 0;
  font-weight: bold;
}

.compiledMarkdown >>> h5 {
  display: block;
  font-size: 0.83em;
  margin-top: 1.67em;
  margin-bottom: 1.67em;
  margin-left: 0;
  margin-right: 0;
  font-weight: bold;
}

.compiledMarkdown >>> h6 {
  display: block;
  font-size: 0.67em;
  margin-top: 2.33em;
  margin-bottom: 2.33em;
  margin-left: 0;
  margin-right: 0;
  font-weight: bold;
}

.compiledMarkdown >>> blockquote {
  border-left: 5px solid #ccc;
  padding: 5px;
}

.compiledMarkdown >>> hr {
  display: block;
  unicode-bidi: isolate;
  margin-block-start: 0.5em;
  margin-block-end: 0.5em;
  margin-inline-start: auto;
  margin-inline-end: auto;
  overflow: hidden;
  border-style: inset;
  border-width: 1px;
}
</style>

<style>
/*

Style with support for rainbow parens

*/

.hljs {
  display: block;
  overflow-x: auto;
  padding: 0.5em;
  background: #474949 !important;
  color: #d1d9e1 !important;
}

.hljs-comment,
.hljs-quote {
  color: #969896;
  font-style: italic;
}

.hljs-keyword,
.hljs-selector-tag,
.hljs-literal,
.hljs-type,
.hljs-addition {
  color: #cc99cc;
}

.hljs-number,
.hljs-selector-attr,
.hljs-selector-pseudo {
  color: #f99157;
}

.hljs-string,
.hljs-doctag,
.hljs-regexp {
  color: #8abeb7;
}

.hljs-title,
.hljs-name,
.hljs-section,
.hljs-built_in {
  color: #b5bd68;
}

.hljs-variable,
.hljs-template-variable,
.hljs-selector-id,
.hljs-class .hljs-title {
  color: #ffcc66;
}

.hljs-section,
.hljs-name,
.hljs-strong {
  font-weight: bold;
}

.hljs-symbol,
.hljs-bullet,
.hljs-subst,
.hljs-meta,
.hljs-link {
  color: #f99157;
}

.hljs-deletion {
  color: #dc322f;
}

.hljs-formula {
  background: #eee8d5;
}

.hljs-attr,
.hljs-attribute {
  color: #81a2be;
}

.hljs-emphasis {
  font-style: italic;
}
</style>
