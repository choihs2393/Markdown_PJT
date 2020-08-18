<template>
  <v-main>
    <v-container class="md" fluid>
      <v-row>
        <v-btn color="primary" v-if="isLoggedIn && $store.state.selectedNoteInfo != null && $store.state.selectedNoteInfo.occupiedNo == 0" @click="occupy($store.state.selectedNoteInfo._id)">점유하기</v-btn>
        <v-btn color="primary" v-if="$store.state.selectedNoteInfo.occupiedNo == $store.state.userInfo.no" @click="saveNote(input)">저장하기</v-btn>
        <v-btn color="primary" v-if="$store.state.selectedNoteInfo.occupiedNo == $store.state.userInfo.no" @click="vacate($store.state.selectedNoteInfo._id)">점유권 놓기</v-btn>
        <span v-if="$store.state.isShareMode">{{ currentTime }}</span>
        <v-spacer></v-spacer>
        <v-btn class="mr-2" @click="isTextarea=!isTextarea">
          <!-- <v-icon left>mdi-pencil</v-icon> -->
          {{ showTextarea }}
        </v-btn>
      </v-row>
      <v-row>
        <v-col id="editor_div" v-if="isTextarea">
          <v-textarea
            solo
            flat
            auto-grow
            row-height="100%"
            full-width
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
        </v-col>
        <v-col id="markdown_div">
          <div id="compiledMarkdown" class="compiledMarkdown" v-html="compiledMarkdown"></div>
        </v-col>
      </v-row>
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
import { is } from 'vee-validate/dist/rules';

// 소켓 관련 모듈
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";
import { setInterval } from 'timers';

var data = sampleData;

// var data = new Promise(function(resolve, reject) {
  //   resolve(sampleData);
// });

let isPathExist = false;
let isTemplate = false;
let folderFullPath = ''

ipcRenderer.on("pong", (event, folderPath) => {
  // console.log('folderPath', folderPath)
  folderFullPath = folderPath;
  // console.log('absoluteFilePath', absoluteFilePath)
  if (folderFullPath != '') {
    isPathExist = true;
  }
  // console.log('absoluteee', folderFullPath);
})

ipcRenderer.on("template", (event, isThereTemplate) => {
  // console.log(message);
  isTemplate = isThereTemplate;
  data.input = readmeTemplate.input;
  if (isTemplate){
    let fileDataObject = {}
    if (isPathExist){
      
      fileDataObject = {'openedFileData': data.input, 'absoluteFilePath': folderFullPath+'\\README.md'};
      fs.exists(fileDataObject['absoluteFilePath'], (exists) => { 
        if (!exists){
          fs.writeFile(fileDataObject['absoluteFilePath'], readmeTemplate.input, (err) => {
            // console.log('파일경로', fileDataObject['absoluteFilePath'])
          })
        } else{
          fs.writeFile(folderFullPath+'\\somangReadme'+Math.floor(Math.random() * 5000)+'.md', readmeTemplate.input, (err) => {
          })
        }
      }); 
    }else {
      fileDataObject = {'openedFileData': data.input, 'absoluteFilePath': ''};
    }
    let win = remote.BrowserWindow.getFocusedWindow();
    // win.webContents.send("ping", fileDataObject);
    ipcRenderer.send("mainping", fileDataObject);
    ipcRenderer.send("template", isTemplate);
    win.webContents.send("addFileInList", folderFullPath);
    isTemplate = false;
    parse(data.input)
  }
})


ipcRenderer.on("ping", (event, message) => {
  // console.log(message);
  data.input = message["openedFileData"];
  parse(data.input)
});

ipcRenderer.on("getNote", (event, message, accountNo) => {
  // console.log(message);
  data.input = message.content;
  // if(accountNo != 0) {
  //   this.isOccupied = true;
  //   document.getElementById("editor_div").setAttribute("disabled", true);
  // } else {
  //   this.isOccupied = false;
  //   document.getElementById("editor_div").setAttribute("disabled", false);
  // }
  parse(data.input)
});

ipcRenderer.on("contentReset", (event, message) => {
  data.input = ""
  parse(data.input)
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
    ipcRenderer.on("template", (event, isThereTemplate) => {
      if(this.$store.state.isShareMode && isThereTemplate) {
        console.log('여깅ㅇㅇㅇㅇㅇ')
      }
    })
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
    ...mapGetters(["isLoggedIn", "isOccupied"]),
    currentTime() {
      return new Date();
    },
    showTextarea() {
      if (data.isTextarea) {
        return '입력창 숨기기'
      } else {
        return '입력창 열기'
      }
    }
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
      let occupyAccountNo = this.$store.state.selectedNoteInfo.occupiedNo;
      let loginAccountNo = this.$store.state.userInfo.no;
      // console.log("영복이의 로그추적"  + bandNo);
      // console.log("영복이의 로그추적 " + noteNo);
      // console.log("영복이의 로그추적 " + occupyAccountNo);
      // console.log("영복이의 로그추적 " + loginAccountNo);
      // console.log("영복이의 로그추적 " + tmp);
      if(noteNo !== '' && bandNo !== '' && occupyAccountNo !== '' && occupyAccountNo === loginAccountNo){
        if(this.$store.state.storeSyncCheck === false){
          this.$store.commit('setStoreSyncCheck', true);
          let storeTimer = setTimeout(() => {
            // clearTimeout(this.$store.state.storeTempData);
            // console.log('로직 안 this22222', this);
            // console.log("영복이의 로그추적 " + tmp);
            this.$store.dispatch('saveNote', tmp);
            this.$store.commit('setStoreSyncCheck', false);
          }, 1000 * 60 * 5);
           this.$store.commit('setStoreTimer',storeTimer)
        }
        clearTimeout(this.$store.state.storeTempData);
        let timeOut_ = setTimeout(() => {
            // console.log('로직 안 this', this);
            // console.log("영복이의 로그추적 " + tmp);
            // console.log(this.$store.state.stroeTimer);
            this.$store.dispatch('saveNote', tmp);
            this.$store.commit('setStoreSyncCheck', false);
            clearTimeout(this.$store.state.storeTimer);
        }, 1000 * 60 * 3);
        this.$store.commit('setStoreTempData', timeOut_);

        let shareTimeOut = setTimeout(() => {
            // console.log('로직 안 this', this);
            console.log("영복이의 Share 로그추적 " + tmp);
            // console.log(this.$store.state.stroeTimer);
            shareNote(tmp);
        }, 1000 * 3);
        this.$store.commit('setShareTempData', shareTimeOut);
      }
    },
    

    shareNote(inputContent){
      const serverURL = "http://i3b104.p.ssafy.io:80/noteAPI/ws";
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);

      this.stompClient.connect({ Authorization: this.$store.state.authorization },
        frame => {
          var map = {
              noteNo: this.$store.state.selectedNoteInfo._id,
              content: this.inputContent
          }
          this.stompClient.send("/groupReceive/content/" + this.$store.state.selectedBandInfo.no + "/" + this.$store.state.selectedNoteInfo._id, JSON.stringify(map));
        }
      )
    },


    // 해당 파일 점유하기.
    occupy(selectedNoteNo) {
      // 1. 소켓 뚫기
      // const serverURL = "http://localhost:8080/noteAPI/ws";
      const serverURL = "http://i3b104.p.ssafy.io:80/noteAPI/ws";
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);

      // 2. 소켓 연결하기
      this.stompClient.connect({ Authorization: this.$store.state.authorization },
        frame => {
          var map = {
              noteId: this.$store.state.selectedNoteInfo._id,
              accountNo: this.$store.state.userInfo.no,
              accountName: this.$store.state.userInfo.name,
              subject: this.$store.state.selectedNoteInfo.subject,
              content: this.$store.state.selectedNoteInfo.content
          }

          // 3. 소켓을 통해 다른 그룹원들에게 '내가 점유하고 있다'고 점유를 풀때까지 무한정 send하기
          // setInterval(() => {
            this.stompClient.send("/groupReceive/occupy/" + this.$store.state.selectedBandInfo.no, JSON.stringify(map)); 
          // }, 5000);
        }
      )

      // 4. send 했으면, 소켓 disconnect를 진행해준다.
      // this.stompClient.disconnect();
    },

    // 해당 파일 점유 포기하기.
    vacate(selectedNoteNo) {
      // 1. 소켓 뚫기
      // const serverURL = "http://localhost:8080/noteAPI/ws";
      const serverURL = "http://i3b104.p.ssafy.io:80/noteAPI/ws";
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);

      // 2. 소켓 연결하기
      this.stompClient.connect({ Authorization: this.$store.state.authorization },
        frame => {
          var map = {
              noteId: this.$store.state.selectedNoteInfo._id,
              accountNo: this.$store.state.userInfo.no,
              subject: this.$store.state.selectedNoteInfo.subject,
              content: this.$store.state.selectedNoteInfo.content
          }

          // 3. 소켓을 통해 다른 그룹원들에게 '내가 점유권을 놓겠다'고 send하기
          this.stompClient.send("/groupReceive/vacate/" + this.$store.state.selectedBandInfo.no, JSON.stringify(map));
        }
      )

      // 4. send 했으면, 소켓 disconnect를 진행해준다.
      this.stompClient.disconnect();
    }
  }
};
</script>

<style scoped>
#editor_div {
  margin: 0;
  /* height: 100%; */
  height: 85vh;
  font-family: "Helvetica Neue", Arial, sans-serif;
  /* color: #333; */
}

#editor_div div {
  height: 100%;
  vertical-align: top;
  box-sizing: border-box;
  /* padding: 0 20px; */
}

.v-textarea {
  border: none;
  resize: none;
  outline: none;
  /* height: 100%; */
  height: 100vh;
  /* background-color: #f6f6f6; */
  font-size: 14px;
  font-family: "Monaco", courier, monospace;
  padding: 10px;
  overflow-y: scroll;
}

code {
  color: #f66;
}

#markdown_div {
  margin: 0;
  height: 85vh;
  overflow-y: scroll;
  /* color: #333; */
}

#markdown_div div {
  height: 100%;
  vertical-align: top;
  box-sizing: border-box;
  /* padding: 0 20px; */
}

#compileMarkdown{
  border: none;
  resize: none;
  outline: none;
  padding: 10px;
  height: 100vh;
  /* background-color: #f6f6f6; */
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
