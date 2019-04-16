package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * PHPLanguageProperties class stores properties of PHP programming language
 * that will be used to count different types of code metrics for source code
 * written in PHP.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class PHPLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize PHP language properties
    */
   public PHPLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("PHP 7");
      SetLangName("PHP");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".php")));
      SetCaseSensitive(true);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList(";")));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("//", "#")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList("/*")));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList("*/")));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirKeywords(new ArrayList<String>(
               Arrays.asList("declare", "define", "include", "include_once", "require", "require_once")));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "'")));
      SetLineContChars(new ArrayList<String>(Arrays.asList()));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList("function")));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("for", "while", "foreach")));
      SetCondKeywords(new ArrayList<String>(
               Arrays.asList("else if", "elseif", "if", "switch", "case", "for", "while", "else")));
      SetLogicalOps(new ArrayList<String>(Arrays.asList("==", "===", "!=", "<>", "!==", ">", "<", ">=", "<=", "and",
               "or", "xor", "not", "&&", "||", "!")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=", "=>")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("break", "case", "catch", "continue", "default", "die",
               "do", "echo", "else", "exception", "exit", "for", "foreach", "goto", "if", "isset", "new", "print",
               "return", "switch", "this", "throw", "try", "while")));
      SetDataKeywords(new ArrayList<String>(
               Arrays.asList("array", "bool", "class", "const", "declare", "extends", "float", "function", "global",
                        "int", "interface", "NULL", "object", "private", "protected", "public", "string", "var")));
      SetCalcKeywords(new ArrayList<String>(
               Arrays.asList("+", "++", "-", "--", "*", "/", "%", ">>", "<<", "~", "&", "|", "^")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("log", "log10", "log1p")));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList("acos", "acosh", "asin", "asinh", "atan", "atan2", "atanh",
               "cos", "cosh", "sin", "sinh", "tan", "tanh")));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList("abs", "base_convert", "bindec", "ceil", "decbin",
               "dechex", "decoct", "deg2rad", "exp", "expm1", "floor", "fmod", "getrandmax", "hexdec", "hypot",
               "is_finite", "is_infinite", "is_nan", "lcg_value", "max", "min", "mt_getrandmax", "mt_rand", "octdec",
               "pi", "pow", "rad2deg", "rand", "round", "sqrt", "srand")));
      SetExcludeKeywords(
               new ArrayList<String>(Arrays.asList("endif", "endfor", "endforeach", "endswitch", "endwhile")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList("\\{", "\\}", "\\(", "\\)")));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList()));
      SetLslocKeywords(new ArrayList<String>(
               Arrays.asList("else if", "elseif", "if", "switch", "for", "while", "foreach", "catch")));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList()));
   }
}