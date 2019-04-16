package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ColdFusionScriptLanguageProperties class stores properties of
 * ColdFusionScript programming language that will be used to count different
 * types of code metrics for source code written in ColdFusionScript.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class ColdFusionScriptLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize ColdFusionScript language properties
    */
   public ColdFusionScriptLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("Cold Fusion Script 13");
      SetLangName("COLDFUSION_SCRIPT");
      // SetLangFileExts(new ArrayList<String>(Arrays.asList(".cfs")));
      SetCaseSensitive(true);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList(";")));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("//")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList("/*")));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList("*/")));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList()));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "'")));
      SetLineContChars(new ArrayList<String>(Arrays.asList()));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList("function")));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("while", "for")));
      SetCondKeywords(new ArrayList<String>(
               Arrays.asList("if", "else if", "else", "match", "switch", "case", "while", "for")));
      SetLogicalOps(new ArrayList<String>(
               Arrays.asList("==", "!=", ">", "<", ">=", "<=", "eq", "neq", "gt", "lt", "gte", "lte")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("abort", "break", "case", "catch", "continue",
               "component", "createobject", "default", "else", "else if", "exit", "finally", "for", "if", "location",
               "lock", "new", "param", "pageencoding", "rethrow", "return", "savecontent", "switch", "thread", "throw",
               "trace", "transaction", "try", "while", "writedump", "writelog", "writeoutput")));
      SetDataKeywords(
               new ArrayList<String>(Arrays.asList("function", "import", "include", "interface", "property", "var")));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("+", "-", "*", "/", "**")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("log", "log10")));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList("acos", "asin", "atn", "cos", "sin", "tan")));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList("abs", "arrayavg", "arraysum", "ceiling",
               "decrementvalue", "exp", "fix", "incrementvalue", "int", "max", "min", "mod", "pi", "precisionvaluate",
               "rand", "randomize", "randrange", "round", "sgn", "sqr")));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("try", "else", "do")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList("\\{", "\\}", "\\(", "\\)")));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList()));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList("if", "catch", "match", "loop", "while", "for")));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList()));
   }
}