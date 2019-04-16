package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * PythonLanguageProperties class stores properties of Python programming
 * language that will be used to count different types of code metrics for
 * source code written in Python.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class PythonLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize Python language properties
    */
   public PythonLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("Python 3.5.1");
      SetLangName("PYTHON");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".py")));
      SetCaseSensitive(true);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList(";")));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("#")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList("'''", "\"\"\"")));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList("'''", "\"\"\"")));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirKeywords(
               new ArrayList<String>(Arrays.asList("#!", "do", "from", "no", "use", "require", "package", "import")));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "'")));
      SetLineContChars(new ArrayList<String>(Arrays.asList("\\")));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList("def")));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("for", "while")));
      SetCondKeywords(new ArrayList<String>(Arrays.asList("if", "else", "except", "for", "elif", "try", "while")));
      SetLogicalOps(new ArrayList<String>(Arrays.asList("==", "!=", "<", ">", "<=", ">=")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("and", "as", "assert", "break", "continue", "def", "del",
               "except", "exec", "exit", "finally", "for", "global", "in", "is", "lambda", "not", "or", "pass", "print",
               "raise", "return", "try", "while", "with", "yield")));
      SetDataKeywords(new ArrayList<String>(Arrays.asList()));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("+", "-", "*", "/", "%", "**")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("math.log", "math.log10", "math.log1p", "math.log2")));
      SetTrigOpKeywords(new ArrayList<String>(
               Arrays.asList("math.cos", "math.sin", "math.tan", "math.acos", "math.asin", "math.atan", "math.atan2",
                        "math.cosh", "math.sinh", "math.tanh", "math.acosh", "math.asinh", "math.atanh")));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList("math.ceil", "math.copysign", "math.degrees", "math.e",
               "math.exp", "math.fabs", "math.factorial", "math.floor", "math.fmod", "math.frexp", "math.fsum",
               "math.hypot", "math.ldexp", "math.modf", "math.pi", "math.pow", "math.radians", "math.sqrt",
               "math.trunc", "cmath.phase", "cmath.polar", "cmath.rect")));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("try", "else", "do")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList("\\{", "\\}", "\\(", "\\)", "\\[", "\\]")));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList("if", "elif", "case", "while", "for", "catch")));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList("if", "catch", "switch", "for", "while")));
   }
}
