package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * HTMLLanguageProperties class stores properties of HTML programming language
 * that will be used to count different types of code metrics for source code
 * written in HTML.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class HTMLLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize HTML language properties
    */
   public HTMLLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("HTML 5");
      SetLangName("HTML");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".htm", ".html")));
      SetCaseSensitive(false);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList()));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList()));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList("<!--")));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList("-->")));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList()));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "'")));
      SetLineContChars(new ArrayList<String>(Arrays.asList()));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList()));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList()));
      SetCondKeywords(new ArrayList<String>(Arrays.asList()));
      SetLogicalOps(new ArrayList<String>(Arrays.asList()));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList()));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("address", "applet", "area", "a", "base", "basefont",
               "big", "blockquote", "body", "br", "b", "caption", "center", "cite", "CODE", "dd", "dfn", "dir", "div",
               "dl", "dt", "em", "font", "form", "h1", "h2", "h3", "h4", "h5", "h6", "head", "hr", "html", "img",
               "input", "isindex", "i", "jsp", "kbd", "link", "li", "map", "menu", "meta", "ol", "option", "param",
               "pre", "p", "samp", "script", "select", "small", "span", "strike", "strong", "style", "sub", "sup",
               "table", "td", "textarea", "th", "title", "tr", "tt", "ul", "u", "var")));
      SetDataKeywords(new ArrayList<String>(Arrays.asList()));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList()));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList()));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList()));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList()));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList()));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList()));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList()));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList()));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList()));
   }
}