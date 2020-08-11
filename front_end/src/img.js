// export function imageEncodeToBase64 (file, maxWidth, maxHeight) {
//     let src = ''
  
//     // 전달된 이미지 파일의 객체 타입에 따라서 호출할 메소드 선택 및 파일 타입 추출
//     if (typeof file === 'string') {
//       src = file
//     } else {
//       src = URL.createObjectURL(file)
//     }
  
//     return new Promise((resolve, reject) => {
//       let image = new Image()
//       image.onload = function (event) {
//         let canvas = document.createElement('canvas')
  
//         // set image size
//         canvas.width = (image.naturalWidth > maxWidth) ? maxWidth : image.naturalWidth
//         canvas.height = (image.naturalWidth > maxHeight) ? maxHeight : image.naturalHeight
  
//         // draw canvas
//         canvas.getContext('2d').drawImage(image, 0, 0, image.width, image.height)
//         resolve(canvas)
//       }
//       image.src = src
//     })
//   }
export default class convertImg {
    static convertImgToDataURLviaCanvas(url, callback, outputFormat) {
      return new Promise((resolve, reject) => {
        var img = new Image();
        img.crossOrigin = 'Anonymous';
        img.onload = function() {
          var canvas = document.createElement('CANVAS');
          var ctx = canvas.getContext('2d');
          var dataURL;
          canvas.height = this.height;
          canvas.width = this.width;
          ctx.drawImage(this, 0, 0);
          dataURL = canvas.toDataURL(outputFormat);
          //callback(dataURL);
          canvas = null;
          return resolve(dataURL);
        };
        img.src = url;
      })
      }
      static convertFileToDataURLviaFileReader(url, callback) {
        var xhr = new XMLHttpRequest();
        xhr.onload = function() {
          var reader = new FileReader();
          reader.onloadend = function() {
            callback(reader.result);
          }
          reader.readAsDataURL(xhr.response);
        };
        xhr.open('GET', url);
        xhr.responseType = 'blob';
        xhr.send();
      }
}
