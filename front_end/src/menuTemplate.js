import { app, BrowserWindow, dialog } from "electron";
import { createWindow } from "./background";
const fs = require("fs");
const isMac = process.platform === "darwin"

const {ipcMain} = require('electron');

export let openedFileData = "test";

const template = [
    // { role: 'appMenu' }
    ...(isMac ? [{
        label: app.name,
        submenu: [
        { role: 'about' },
        { type: 'separator' },
        { role: 'services' },
        { type: 'separator' },
        { role: 'hide' },
        { role: 'hideothers' },
        { role: 'unhide' },
        { type: 'separator' },
        { role: 'quit' }
        ]
    }] : []),
    // { role: 'fileMenu' }
    {
        label: 'File',
        submenu: [
            isMac ? { role: 'close' } : { role: 'quit' },
            {
                label: 'New Window',
                accelerator: 'CommandOrControl+N',
                click() {
                    openNewWindow();
                }
            },
            {
                label: 'Save As ...',
                accelerator: 'CommandOrControl+S',
                click() {
                    saveFile();
                }
            },
            {
                label: 'Open',
                accelerator: 'CommandOrControl+O',
                click() {
                    openFile();
                }
            },
        ]
    },
    // { role: 'editMenu' }
    {
        label: 'Edit',
        submenu: [
            { role: 'undo' },
            { role: 'redo' },
            { type: 'separator' },
            { role: 'cut' },
            { role: 'copy' },
            { role: 'paste' },
            ...(isMac ? [
                { role: 'pasteAndMatchStyle' },
                { role: 'delete' },
                { role: 'selectAll' },
                { type: 'separator' },
                {
                label: 'Speech',
                submenu: [
                    { role: 'startspeaking' },
                    { role: 'stopspeaking' }
                ]
                }
            ] : [
                { role: 'delete' },
                { type: 'separator' },
                { role: 'selectAll' }
            ])
        ]
    },
    // { role: 'viewMenu' }
    {
        label: 'View',
        submenu: [
            { role: 'reload' },
            { role: 'forcereload' },
            { role: 'toggledevtools' },
            { type: 'separator' },
            { role: 'resetzoom' },
            { role: 'zoomin' },
            { role: 'zoomout' },
            { type: 'separator' },
            { role: 'togglefullscreen' },
            { type: 'separator' },
            { role: 'toggleDevTools'}
        ]
    },
    // { role: 'windowMenu' }
    {
        label: 'Window',
        submenu: [
            { role: 'minimize' },
            { role: 'zoom' },
            ...(isMac ? [
                { type: 'separator' },
                { role: 'front' },
                { type: 'separator' },
                { role: 'window' }
            ] : [
                { role: 'close' }
            ])
        ]
    },
    {
      role: 'help',
      submenu: [
        {
            label: 'Markdown Help',
        accelerator: 'F1',
        click() {
            createHelpWindow();
            }
        },
        {
          label: 'Learn More',
          click () { require('electron').shell.openExternal('https://electronjs.org') }
        }
      ]
    }
];
  
function openNewWindow() {
    createWindow();
}

function saveFile() {
    var fileData = '';

    BrowserWindow.getFocusedWindow().webContents.executeJavaScript(`document.getElementById("editor_textarea").value`)
    .then(result => {
        fileData = result;
    });

    var fileName = dialog.showSaveDialog(BrowserWindow.getFocusedWindow(),
        {
            title: "파일 저장하기!!!",
            filters: [
                { name: 'Markdown', extensions: ['md'] },
            ],
            message: "TEST"
        }
    )
    .then(result => {
        fileName = result.filePath;
        fs.writeFile(fileName, fileData, (err) => {

        })
    });
}

function openFile() {
    var fileData = '';

    dialog.showOpenDialog(BrowserWindow.getFocusedWindow(),
        {
            filters: [
                { name: 'Markdown', extensions: ['md'] }
            ]
        }
    )
    .then(result => {
        let absoluteFilePath = JSON.stringify(result.filePaths[0]);
        absoluteFilePath = absoluteFilePath.substr(1, absoluteFilePath.length-2)

        fs.readFile(absoluteFilePath, 'utf8', (err, data) => {
            // console.log(absoluteFilePath);
            if(err) throw err;
            // console.log(data);
            // fileData = data;
            openedFileData = data;
            console.log(openedFileData);

            let win = BrowserWindow.getFocusedWindow();
            win.webContents.send("ping", openedFileData);
        });
    });
}

function createHelpWindow() {
    console.log(`file://${__dirname}/app/help.html`)
    // Create the browser window.
    let win = new BrowserWindow({
      width: 600,
      height: 400,
      webPreferences: {
        nodeIntegration: true
      }
    });
    win.loadURL(`file://${__dirname}/../src/help.html`)
    win.setTitle("도움말");
    win.on('page-title-updated', function(e) {
    e.preventDefault()
  });
  }

export default template
