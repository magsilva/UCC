package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * JavaScriptLanguageProperties class stores properties of JavaScript
 * programming language that will be used to count different types of code
 * metrics for source code written in JavaScript.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class JavaScriptLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize JavaScript language properties
    */
   public JavaScriptLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("Version 8 Update 91 - 19 April 2016 ");
      SetLangName("JAVASCRIPT");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".js")));
      SetCaseSensitive(true);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList(";")));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("//")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList("/*")));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList("*/")));
      SetCompilerDirChars(new ArrayList<String>());
      SetCompilerDirKeywords(new ArrayList<String>());
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "'")));
      SetLineContChars(new ArrayList<String>(Arrays.asList("...")));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList("function")));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("for", "while")));
      SetCondKeywords(new ArrayList<String>(Arrays.asList("case", "else", "else if", "for", "if", "switch", "while")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("alert", "arguments", "assign", "break", "case", "catch",
               "close", "comment", "constructor", "continue", "default", "debugger", "delete", "do", "else", "escape",
               "eval", "export", "extends", "FALSE", "find", "final", "finally", "focus", "for", "function", "if",
               "import", "label", "length", "location", "native", "new", "null", "open", "package", "print", "prompt",
               "prototype", "ref", "return", "self", "status", "stop", "super", "switch", "synchronized", "taint",
               "this", "throw", "throws", "transient", "TRUE", "try", "typeof", "untaint", "var", "watch", "while",
               "with")));
      SetDataKeywords(new ArrayList<String>(Arrays.asList("abstract", "boolean", "byte", "char", "class", "double",
               "enum", "float", "implements", "instanceOf", "int", "interface", "long", "private", "protected",
               "public", "short", "static", "void")));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("+", "-", "*", "/", "%", "++", "--")));
      SetLogicalOps(new ArrayList<String>(Arrays.asList("==", "===", "!=", "!==", ">", "<", ">=", "=<")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("log")));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList("acos", "asin", "atan", "atan2", "cos", "sin", "tan")));
      SetOtherMathKeywords(new ArrayList<String>(
               Arrays.asList("abs", "ceil", "exp", "floor", "max", "min", "pow", "random", "round", "sqrt")));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("else", "do")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList("\\{", "\\}", "\\(", "\\)")));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList()));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList("if", "switch", "for", "while")));
      SetPointerKeywords(new ArrayList<String>());
   }
}