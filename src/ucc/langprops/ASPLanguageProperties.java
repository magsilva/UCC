package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * VBScriptLanguageProperties class stores properties of VBScript programming
 * language that will be used to count different types of code metrics for
 * source code written in VBScript.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class ASPLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize VBScript language properties
    */
   public ASPLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("ASP");
      SetLangName("ASP");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".asp", ".aspx")));
      SetCaseSensitive(false);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList()));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("'")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList()));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList("#")));
      SetCompilerDirKeywords(new ArrayList<String>(
               Arrays.asList("#const", "#else", "#elseif", "#end", "#externalsource", "#if", "#region")));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"")));
      SetLineContChars(new ArrayList<String>(Arrays.asList("_")));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList("Function", "Sub")));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("for each", "for", "while", "until")));
      SetCondKeywords(new ArrayList<String>(
               Arrays.asList("elseif", "if", "select", "case", "for each", "for", "do while", "while", "do until")));
      SetLogicalOps(new ArrayList<String>(Arrays.asList("<>", ">", "<", ">=", "=<", "and", "not", "or", "xor",
               "andalso", "orelse", "isfalse", "istrue")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("add", "appactivate", "asc", "beep", "call", "cbool",
               "cbyte", "ccur", "cdate", "cdbl", "cdec", "cint", "cstr", "cvar", "chdir", "clear", "close", "command",
               "createobject", "curdir", "dir", "do", "doevents", "else", "end", "environ", "erase", "error", "exit",
               "fileattr", "filecopy", "filedatetime", "filelen", "fix", "for", "format", "freefile", "function", "get",
               "getattr", "getobject", "gosub", "goto", "if", "input", "instr", "iserror", "kill", "left", "len",
               "line", "loc", "lock", "lof", "loop", "mid", "mkdir", "name", "next", "now", "on", "open", "print",
               "put", "raise", "randomize", "remove", "reset", "resume", "return", "right", "rmdir", "rnd", "seek",
               "select", "sendkeys", "server", "setattr", "shell", "spc", "stop", "str", "switch", "sub", "tab",
               "timer", "unlock", "val", "wend", "while", "width", "with", "write")));
      SetDataKeywords(new ArrayList<String>(Arrays.asList("boolean", "byte", "collection", "const", "currency", "date",
               "dim", "double", "integer", "item", "long", "new", "object", "option", "private", "public", "redim",
               "single", "static", "string", "time", "variant")));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("+", "-", "*", "/", "\\", "^")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("log")));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList("atan", "cos", "sin", "tan")));
      SetOtherMathKeywords(
               new ArrayList<String>(Arrays.asList("abs", "exp", "round", "rnd", "randomize", "sign", "sqrt")));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("next", "loop", "do", "else")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList()));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList()));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList("elseif", "if", "select", "case", "for each", "for",
               "do while", "while", "do until", "until")));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList()));
   }
}