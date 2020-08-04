var data = {
    input: '# 마크다운 에디터 사용법\n'
    + '----------------------------------------------------------------------------------------------------\n'
    + '## 1. 헤더(headers : 1~6)\n'
    + '# heading1\n'
    + '## heading2\n'
    + '### heading3\n'
    + '#### heading4\n'
    + '##### heading5\n'
    + '###### heading6\n'
    + '----------------------------------------------------------------------------------------------------\n'
    + '## 2. 인용구(blockquote)\n'
    + '> This is a first blockqute.\n'
    + '>> This is a second blockqute.\n'
    + '>>> This is a third blockqute.\n'
    + '----------------------------------------------------------------------------------------------------\n'
    + '## 3. Unordered List(ul)\n'
    + '- 빨강\n'
    + '  - 녹색\n'
    + '    - 파랑\n'
    + '----------------------------------------------------------------------------------------------------\n'
    + '## 4. 코드 블럭\n'
    + '```\n'
    + 'public class BootSpringBootApplication {\n'
    + '  public static void main(String[] args) {\n'
    + '    System.out.println("Hello world!");\n'
    + '  }\n'
    + '}\n'
    + '```\n'
    + '----------------------------------------------------------------------------------------------------\n'
    + '# 5. 인라인 코드 블럭\n'
    + '프로그램 수행 중 `return a+b` 라는 문장을 만나면 결과값이 리턴됩니다.\n'
    + '----------------------------------------------------------------------------------------------------\n'
    + '## 6. 수평선\n'
    + '---\n'
    + '- - -\n'
    + '***\n'
    + '* * * * * *\n'
    + '----------------------------------------------------------------------------------------------------\n'
    + '## 7. 링크\n'
    + '[google](http://google.com)\n'
    + 'http://google.com\n'
    + '<http://google.com>\n'
    + 'email@gmail.com\n\n'
    + 'Link: [Google][googlelink]\n\n'
    + '[googlelink]: https://google.com "Go google"\n'
    + '----------------------------------------------------------------------------------------------------\n'
    + '## 8. 강조\n'
    + '### 7-1. italic체\n'
    + '*italic*\n\n'
    + '_italic_\n'
    + '### 7-2. bold체\n'
    + '**bold**\n\n'
    + '__bold__\n'
    + '----------------------------------------------------------------------------------------------------\n'
    + '## 9.이모지\n'
    + '### :smile:\n\n'
    + '##### :worried:\n'
    + '----------------------------------------------------------------------------------------------------\n'
    + '## 10. 동영상\n'
    + '@[video](https://www.youtube.com/watch?v=mgvZ0Gmo02k)'
};

export default data;