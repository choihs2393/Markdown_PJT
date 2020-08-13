import { emojiArray } from './emoji.js';
import convertImg from './img.js';
export const referenceLinkArray = [];
export default class Replacer {

    static video(fullMatch, group1, group2, group3, group4) {
		const tmpRegex = /watch\?v=/g;
		return '<iframe src="https://www.youtube.com/embed/' + group4.replace(tmpRegex,'') + '" width="100%" height="400" frameborder="0" allowfullscreen="";"></iframe>';	
    }
    static unorderList(fullMatch, group1, group2, group3) {
        if(group1 == "  " || group1 == "   "){
            return '<ul><ul><li>' + group3 + '</li></ul></ul>';
        } else if (group1 == "    ") {
            return '<ul><ul><ul><li>' + group3 + '</li></ul></ul></ul>';
        }
        return '<ul><li>' + group3 + '</li></ul>';
    }
    // static orderList(fullMatch, group1, group2, group3, group4) {
    //     const tempRegex = /(\n){1}/g;
    //     const tapRegex = /(\t){1}/g;
    //     const exitRegex = /(\n\n)/g;
    //     //const exitRegex = /<p><\/p>/g;
    //     const tapReplacer = function() {
    //         console.log('check');
    //         return '</li><ol><li>';
    //     }
    //     const tempReplacer = function(group4) {
    //         return '</li>' + group4 + '<li>\n';
    //     }
    //     const replaceRegex = function(regex, replacement){
    //         return function(group4){
    //             return group4.replace(regex, replacement);
    //         }
    //     }
    //     const replacetemp = replaceRegex(tempRegex, tempReplacer);
    //     const replaceTap = replaceRegex(tapRegex, tapReplacer);
    //     const tmp = replaceTap(replacetemp(group4));
    //     return '<ol><li>' + tmp.replace(exitRegex,'</li></ol><li>') + '</li></ol>';
    // }
    static heading(fullMatch, tagStart, tagContents){
        return '<h' + tagStart.trim().length + '>' + tagContents + '</h' + tagStart.trim().length + '>\n';
    }
    static horizontal() {
        return '<hr />';
    }
    static blockquoteDepth1(fullMatch, tagStart, tagContents) {
        return '<blockquote><p>' + tagContents + '</p></blockquote>\n';
    }
    static blockquoteDepth2(fullMatch, tagStart, tagContents) {
        return '<blockquote><blockquote><p>' + tagContents + '</p></blockquote></blockquote>\n';
    }
    static blockquoteDepth3(fullMatch, tagStart, tagContents) {
        return '<blockquote><blockquote><blockquote><p>' + tagContents + '</p></blockquote></blockquote></blockquote>\n';
    }
    static codeblock(fullMatch, group1, group2) {

        if(typeof group2 == 'undefined')
            return '<pre><code>' + '</code></pre>';
        else
            return '<pre><code>' + group2 + '</code></pre>';
    }
    static inlineCodeblock(fullMatch, group1, group2) {
        return '<code>' + group2 + '</code>';
    }
    static italicAsterisk(fullMatch, group1, group2) {
        return '<em>' + group2 + '</em>';
    }
    static italicUnderbar(fullMatch, group1, group2) {
        return '<em>' + group2 + '</em>';
    }
    static boldAsterisk(fullMatch, group1, group2) {
        return '<strong>' + group2 + '</strong>';
    }
    static boldUnderbar(fullMatch, group1, group2) {
    // static boldUnderbar(fullMatch, group1, group2, group3) {

        return '<strong>' + group2 + '</strong>';
    }
    static paragraph(fullMatch) {
        return '<p>' + fullMatch + '</p>\n';
    }
    static async img(fullMatch, tagStart, tagContents) {
        var src = await convertImg.convertImgToDataURLviaCanvas(tagContents.substring(1,tagContents.length-1));
        return '<img src="' + src + '"/>';
        // convertImg.convertImgToDataURLviaCanvas(tagContents.substring(1,tagContents.length-1), function (result) {
        //         asdf = result;
        //     })
        // let src = function(asdf) {
        //     convertImg.convertImgToDataURLviaCanvas(tagContents.substring(1,tagContents.length-1), function (result) {
        //         asdf = result;
        //     })
        // }
        // src = new Promise(function(resolve, reject) {
            // convertImg.convertImgToDataURLviaCanvas(tagContents.substring(1,tagContents.length-1), function (result) {
            //     resolve(result);
            // })
        // })
        
        // .then(function(resolvedData) {
        //     console.log(resolvedData);
        // })
        
        //await convertImg.convertImgToDataURLviaCanvas(tagContents.substring(1,tagContents.length-1), function(resolvedData) {
        //    base64data = resolvedData;
            //return '<img src="data:image/png;base64, ' + resolvedData + '">';
        //});

        
        // let src = '';
        // let base64data = '';
        // function getBase64() {
        //     return new Promise(function(resolve, reject) {
        //         let src = '';
        //         convertImg.convertImgToDataURLviaCanvas(tagContents.substring(1,tagContents.length-1), function (result) {
        //             console.log('result : ' +result);
        //             src = result;
        //             callback(src);
        //         });
        //         resolve(src);
        //     });
        // }
        // getBase64().then(function(resolvedData) {
        //     console.log('result : ' + resolvedData);
        //     base64data = resolvedData;
        // });
        //return '<img src="' + base64data + '">';
    //    return '<img src="' + tagContents.substring(1,tagContents.length-1) + '">';
    }
    static externalLink(fullMatch, tagContents, link, linkTitle) {
        if(typeof linkTitle == 'undefined')
            return '<a href="' + link + '" target="_blank">' + tagContents + '</a>'; 
        return '<a href="' + link + '" title="' + linkTitle + '" target="_blank">' + tagContents + '</a>';
    }
    static autoLink(fullMatch, group1) {
        return '<a href="' + group1 +'" target="_blank">' + group1 + '</a>';
    }
    static autoLinkWithTag(fullMatch, group1) {
        return group1;
    }
    static emailLink(fullMatch, group1) {
        return '<a href="' + group1 +'" target="_blank">' + group1 + '</a>';
    }
    static addReferenceLink(fullMatch, group1, group2, group3) {
        for (const i in referenceLinkArray){
            if(referenceLinkArray[i].id == group1){
                referenceLinkArray[i].link = group2;
                referenceLinkArray[i].linkTitle = group3;
                return '';
            }
        }
        referenceLinkArray.push({id : group1, link : group2, linkTitle : group3});
        return '';
    }
    static referenceLink(fullMatch, group1, group2) {
        for (const i in referenceLinkArray){
            if(referenceLinkArray[i].id == group2)
                return '<a href="' + referenceLinkArray[i].link + '" title="' + referenceLinkArray[i].linkTitle + '" target="_blank">' + group1 + '</a>';
        }
        return fullMatch;
    }
    static emoji(fullMatch, group1) {
        for (const i in emojiArray){
            if(emojiArray[i][group1] != undefined)
                return emojiArray[i][group1];
        }
        return fullMatch;
    }
};