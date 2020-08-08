<template>
  <v-main>
    <SideBar />

    <v-container class="md" fluid>

      <div id="editor_div">
        <v-textarea
          solo
          flat
          auto-grow
          ref="textarea"
          v-model="input"
          id="editor_textarea"
          onkeydown=
            "if(event.keyCode === 9) {
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
      </v-row> -->
    </v-container>
  </v-main>
</template>

<script>
// @ is an alias to /src
import parse from "../parse";
import sampleData from "../sampleData.js";

import { remote, ipcRenderer } from "electron";
import fs from "fs";
import path from "path"

import SideBar from "@/components/SideBar.vue";

var data = sampleData;

require('electron').ipcRenderer.on('ping', (event, message) => {
  // console.log(message);
  data.input = message['openedFileData'];
});

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
        // console.log('f.path', f.path)
        // console.log(data);
        // fileData = data;
        let openedFileData = data;
        // console.log("openedFileData : " + openedFileData);

        let fileDataObject = {'openedFileData': openedFileData, 'absoluteFilePath': f.path};
        let win = remote.BrowserWindow.getFocusedWindow();
        win.webContents.send("ping", fileDataObject);
      });
    }
});

export default {
  name: "Home",
  components: {
    SideBar
  },
  created() {
  },
  updated() {
  },
  computed: {
    compiledMarkdown: function () {
      return parse(this.input);
    },
  },
  data () {
    return data
  }
};
</script>

<style scoped>
#editor_div {
  margin: 0;
  height: 100%;
  font-family: 'Helvetica Neue', Arial, sans-serif;
  color: #333;
}

v-textarea, #editor_div div {
  display: inline-block;
  width: 49%;
  height: 100%;
  vertical-align: top;
  box-sizing: border-box;
  padding: 0 20px;
}

v-textarea {
  border: none;
  border-right: 1px solid #ccc;
  resize: none;
  outline: none;
  background-color: #f6f6f6;
  font-size: 14px;
  font-family: 'Monaco', courier, monospace;
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
  font-size: .83em;
  margin-top: 1.67em;
  margin-bottom: 1.67em;
  margin-left: 0;
  margin-right: 0;
  font-weight: bold;
}

.compiledMarkdown >>> h6 {
  display: block;
  font-size: .67em;
  margin-top: 2.33em;
  margin-bottom: 2.33em;
  margin-left: 0;
  margin-right: 0;
  font-weight: bold;
}

.compiledMarkdown >>> blockquote{
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
