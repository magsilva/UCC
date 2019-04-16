package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * PascalLanguageProperties class stores properties of Pascal programming
 * language that will be used to count different types of code metrics for
 * source code written in Pascal.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class PascalLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize Pascal language properties
    */
   public PascalLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("Pascal 3.0");
      SetLangName("PASCAL");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".pas", ".p", ".pp", ".pa3", ".pa4", ".pa5", ".pascal")));
      SetCaseSensitive(false);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList(";")));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("//")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList("(*", "{")));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList("*)", "}")));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList()));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("'")));
      SetLineContChars(new ArrayList<String>(Arrays.asList()));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList("function", "procedure")));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("for", "while", "repeat", "with")));
      SetCondKeywords(new ArrayList<String>(Arrays.asList("if", "case", "try", "for", "while", "repeat", "with")));
      SetLogicalOps(new ArrayList<String>(
               Arrays.asList("=", "<>", ">", "<", ">=", "=<", "not", "and", "or", "xor", "shl", "shr")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList(":=")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("absolute", "assembler", "case", "const", "constructor",
               "destructor", "dispose", "downto", "else", "exit", "far", "for", "forward", "freemem", "function",
               "getmem", "goto", "if", "implementation", "inline", "interrupt", "label", "mark", "near", "new", "nil",
               "packed", "private", "procedure", "program", "protected", "public", "repeat", "unit", "uses", "var",
               "virtual", "while", "with")));
      SetDataKeywords(new ArrayList<String>(Arrays.asList("ansistring", "array", "boolean", "byte", "bytebool",
               "cardinal", "char", "class", "comp", "complex", "const", "double", "extended", "file",
               "integer", "interface", "int64", "longbool", "longint", "longword", "object", "pchar", "qword", "real",
               "record", "set", "shortint", "shortstring", "single", "smallint", "string", "type", "widestring", "word",
               "wordbool")));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("+", "-", "*", "/", "**", "div", "mod")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("ln")));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList("arccos", "arcsin", "arctan", "cos", "sin")));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList("abs", "arg", "cmplx", "dec", "exp", "im", "inc", "min",
               "max", "polar", "pow", "re", "round", "sqr", "sqrt")));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("else", "begin", "end", "repeat", "try", "except", "uses",
               "public", "protected", "private", "type", "interface", "implementation", "resourcestring")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList()));
      SetCyclCmplexKeywords(
               new ArrayList<String>(Arrays.asList("if", "except", "finally", "while", "for", "repeat", "with")));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList("if", "for", "while", "repeat", "with", "case")));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList("^")));
   }
}
