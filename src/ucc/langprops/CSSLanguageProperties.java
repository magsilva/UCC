package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * CSSLanguageProperties class stores properties of CSS programming language
 * that will be used to count different types of code metrics for source code
 * written in CSS.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class CSSLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize CSS language properties
    */
   public CSSLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("CSS 2.1");
      SetLangName("CSS");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".css")));
      SetCaseSensitive(false);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList(";")));
      SetSingleLineCmntStartChars(new ArrayList<String>());
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList("/*")));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList("*/")));
      SetCompilerDirChars(new ArrayList<String>());
      SetCompilerDirKeywords(new ArrayList<String>());
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "'")));
      SetLineContChars(new ArrayList<String>(Arrays.asList(",")));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList()));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList()));
      SetCondKeywords(new ArrayList<String>(Arrays.asList()));
      SetLogicalOps(new ArrayList<String>(Arrays.asList()));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList()));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("azimuth", "background", "background-attachment",
               "background-color", "background-image", "background-repeat", "background-position", "border",
               "border-bottom", "border-collapse", "border-bottom-color", "border-bottom-style", "border-bottom-width",
               "border-left", "border-left-color", "border-left-style", "border-left-width", "border-right",
               "border-right-color", "border-right-style", "border-right-width", "border-spacing", "border-style",
               "border-top", "border-top-color", "border-top-style", "border-top-width", "border-width", "bottom",
               "caption-side", "clear", "clip", "content", "counter-decrement", "counter-increment", "counter-reset",
               "cue", "cue-after", "cue-before", "cursor", "direction", "empty-cells", "float", "font", "font-family",
               "font-size", "font-style", "font-variant", "font-weight", "height", "left", "letter-spacing",
               "line-height", "list-style", "list-style-image", "list-style-position", "list-style-type", "line-width",
               "margin", "margin-bottom", "margin-left", "margin-right", "margin-top", "max-height", "max-width",
               "min-height", "min-width", "orphans", "outline", "outline-color", "outline-style", "outline-width",
               "padding", "padding-bottom", "padding-left", "padding-right", "padding-top", "page-break-after",
               "page-break-before", "pause", "pause-after", "pitch", "play", "play-during", "pitch-range", "position",
               "quptes", "richness", "right", "speak", "speak-header", "speak-numeral", "speak-punctuation",
               "speech-rate", "stress", "table-layout", "text", "text-transform", "text-indent", "text-decoration",
               "text-align", "top", "unicode-bidi", "vertical-align", "visibility", "voice-family", "volume",
               "white-space", "width", "word-spacing", "z-index")));
      SetDataKeywords(new ArrayList<String>(Arrays.asList()));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList()));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList()));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList()));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList()));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList()));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList("\\{", "\\}", "\\(", "\\)")));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList()));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList()));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList()));
   }
}
