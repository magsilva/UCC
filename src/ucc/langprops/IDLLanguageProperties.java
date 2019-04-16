package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * IDLLanguageProperties class stores properties of IDL programming language
 * that will be used to count different types of code metrics for source code
 * written in IDL.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class IDLLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize IDL language properties
    */
   public IDLLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("IDL");
      SetLangName("IDL");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".pro", ".sav")));
      SetCaseSensitive(false);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList()));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList(";")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList()));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList("@")));
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList()));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "'")));
      SetLineContChars(new ArrayList<String>(Arrays.asList("$")));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList("function", "pro")));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("for", "foreach", "while")));
      SetCondKeywords(
               new ArrayList<String>(Arrays.asList("if", "else if", "switch", "case", "for", "foreach", "while")));
      SetLogicalOps(new ArrayList<String>(
               Arrays.asList("and", "or", "not", "xor", "lt", "gt", "ge", "le", "eq", "ne", "~", "&&", "||")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("begin", "break", "case", "common", "compile_opt",
               "continue", "else", "for", "foreach", "forward_function", "function", "goto", "if", "inherits",
               "on_ioerror", "pro", "repeat", "return", "switch", "while")));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("mod", "^", "++", "+", "--", "-", "*", "/", "#", "##")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("alog", "alog10")));
      SetTrigOpKeywords(new ArrayList<String>(
               Arrays.asList("acos", "asin", "atan", "cos", "cosh", "sin", "sinh", "tan", "tanh")));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList("abs", "exp", "factorial", "fft", "fix", "float", "max",
               "mean", "min", "primes", "randomu", "round", "sqrt", "total")));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("case", "switch", "else", "endcase", "endswitch", "endif",
               "endfor", "endforeach", "endwhile", "begin", "end", "repeat", "endelse")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList()));
      SetCyclCmplexKeywords(
               new ArrayList<String>(Arrays.asList("case", "switch", "else", "if", "while", "for", "catch", "repeat")));
      SetLslocKeywords(
               new ArrayList<String>(Arrays.asList("case", "switch", "else", "if", "while", "for", "catch", "repeat")));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList("->", ".")));
   }
}