// const Replacer = window.$ = window.jQuery = require('./Replacer.js');
import Replacer  from './replacer.js';
//import { emojiArray } from './emoji.js';
//import { replace } from 'core-js/fn/symbol';
import { hljs } from './highlight.pack.js';
hljs.initHighlightingOnLoad();

//참조링크 배열
// 참조링크가 지워지면 array에서도 지워지도록 수정해야함.
// input 값이 변화할때마다 parse 함수를 불러오도록 짜여져 있어 수정에 어려움이 있음.

// 정규식 표현.
const Regex = {
	heading : /\n{0,3}(#{1,6}) +([^\n]*)(?: +#+)? *(?:\n+|$)/g,
	horizontal : /((?:- *){3,}|(?:_ *){3,}|(?:\* *){3,})(?:\n+|$)/g,
	blockquoteDepth1 : /^(>{1}) +([^\n]*)(?: +>+)? *(?:\n+|$)/gm,
	blockquoteDepth2 : /^(>{2}) +([^\n]*)(?: +>+)? *(?:\n+|$)/gm,
	blockquoteDepth3 : /^(>{3}) +([^\n]*)(?: +>+)? *(?:\n+|$)/gm,
	img : /\n{0,3}(!\[.+\])+(\(.+\))/g, // 이미지 정규식 이름짓는 부분과 경로를 모든 문자열 처리를 했음. 예외 상황 생각 필요할지도?
	externalLink : /(?:\[([^[\]]+)\])(?:(?:\(([^), ]+))(?:,? *"([^"]+)")?\))/g,
	autoLink : /((?<![">])(?:(?:ftp|https?):\/\/)(?:[a-zA-Z0-9-]+\.?)+[^\s<]*)/g,
	autoLinkWithTag : /(?:<((?:(?:ftp|https?):\/\/)(?:[a-zA-Z0-9-]+\.?)+[^\s<]*)>)/g,
	emailLink : /([0-9a-zA-Z](?:[-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z](?:[-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3})/g,
	addReferenceLink : /(?:\[([^[\]]+)\])(?:(?::\s*(\w[^),"]+)\s*)(?:,? *"([^"]+)")?\n)/g,
	referenceLink : /(?:\[([^[\]]+)\])(?:\[([^[\]]+)\])/g,
	codeblock : /(`{3}(?=[^`\n]*\n)|~{3,})(?:[^\n]*)\n(?:|([\s\S]*?)\n)(?: {0,3}\1[~`]* *(?:\n+|$)|$)/g,
	inlineCodeblock : /(`)(.*?)\1/g,
	italicAsterisk : /(\*)([^\n*]+)(?:(\*))/g,
	italicUnderbar : /(_)([^\n]+)(?:(_))/g,
	boldAsterisk : /(\*{2})([^\n]+)(?:(\*{2}))/g,
	boldUnderbar : /(__)(.+)(?:(__))/g,
	paragraph : /^(?!(?:<h|<b|<u|<pre|<code))([^\n]+(?:\n[^\n]+)*)/gm, // 헤더 무시하는 정규식
	unorderList : /\n{0,3}( {0,4})(?:(-|\*|\+)) +([^\n]*)(?: +-+)? *(?:\n+|$)/g,
	orderList : /^(?!(?:<h|<b|<u|<pre|<code))( {0,4})(?:([1-9].) )([^\n]*?)(?: +#+)? *([^\n]+(?:\n[^\n]+)*)/gm,
	video : /(@\[.+\])+(\((?:https?:\/\/)?(\w*:\w*@)?[-\w.]+((:\d+)?(\/([\w/_.]*(\?\S+)?)?)?)+\))/g,
	emoji : /(?::(\w+):)/g
};

/*const replaceRegex = function(regex, replacement){
	return function(str){
		return str.replace(regex, replacement);
	}
}*/

/*function edit(str) {
	return str;
}*/
const replaceMarkdown = function(str) {
	str = str.replace(Regex.emoji, Replacer.emoji)
	.replace(Regex.codeblock, Replacer.codeblock)
	.replace(Regex.img, Replacer.img)
	.replace(Regex.video, Replacer.video)
	.replace(Regex.inlineCodeblock, Replacer.inlineCodeblock)
	.replace(Regex.addReferenceLink, Replacer.addReferenceLink)
	.replace(Regex.referenceLink, Replacer.referenceLink)
	.replace(Regex.externalLink, Replacer.externalLink)
	.replace(Regex.emailLink, Replacer.emailLink)
	.replace(Regex.autoLinkWithTag, Replacer.autoLinkWithTag)
	.replace(Regex.autoLink, Replacer.autoLink)
	.replace(Regex.heading, Replacer.heading)
	.replace(Regex.blockquoteDepth3, Replacer.blockquoteDepth3)
	.replace(Regex.blockquoteDepth2, Replacer.blockquoteDepth2)
	.replace(Regex.blockquoteDepth1, Replacer.blockquoteDepth1)
	.replace(Regex.boldAsterisk, Replacer.boldAsterisk)
	.replace(Regex.boldUnderbar, Replacer.boldUnderbar)
	.replace(Regex.italicAsterisk, Replacer.italicAsterisk)
	.replace(Regex.italicUnderbar, Replacer.italicUnderbar)
	.replace(Regex.horizontal, Replacer.horizontal)
	.replace(Regex.orderList, Replacer.orderList)
	.replace(Regex.unorderList, Replacer.unorderList)
	.replace(Regex.paragraph, Replacer.paragraph);
	return str;
} 
const parse = function(str) {
	return replaceMarkdown(str);
}

export default parse;
