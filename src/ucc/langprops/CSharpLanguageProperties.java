package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * CSharpLanguageProperties class stores properties of C# programming language
 * that will be used to count different types of code metrics for source code
 * written in C#.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class CSharpLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize C# language properties
    */
   public CSharpLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("C# 6.0");
      SetLangName("CSHARP");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".cs")));
      SetCaseSensitive(true);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList(";")));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("//")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList("/*")));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList("*/")));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList("#")));
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList("#define", "#else", "#warning", "#undef", "#elif",
               "#line", "#if", "#endif", "#error", "#region", "#endregion")));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "'")));
      SetLineContChars(new ArrayList<String>(Arrays.asList("\\")));
      SetFuncStartKeywords(new ArrayList<String>());
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("for", "while", "do")));
      SetCondKeywords(new ArrayList<String>(Arrays.asList("if", "else", "else if", "switch", "case")));
      SetLogicalOps(new ArrayList<String>(Arrays.asList("==", "!=", "<", ">", "<=", ">=", "!", "&&", "||")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("break", "continue", "try", "throw", "throws", "catch",
               "finally", "new", "return", "super", "this", "default")));
      SetDataKeywords(new ArrayList<String>(Arrays.asList("abstract", "bool", "byte", "char", "class", "const",
               "decimal", "delegate", "double", "enum", "event", "explicit", "extern", "float", "implicit", "int",
               "interface", "internal", "List", "long", "namespace", "object", "operator", "override", "private",
               "protected", "public", "readonly", "sbyte", "sealed", "short", "static", "string", "struct", "uint",
               "ulong", "using", "unsafe", "ushort", "virtual", "void", "volatile")));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("%", "^", "++", "+", "--", "-", "*", "/", ">>", "<<")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("log", "log10", "log1p", "log2")));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList("cos", "sin", "tan", "acos", "asin", "atan", "atan2",
               "cosh", "sinh", "tanh", "acosh", "asinh", "atanh")));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList("exp", "frexp", "ldexp", "modf", "exp2", "expm1",
               "scalbn", "scalbln", "pow", "sqrt", "cbrt", "hypot", "erf", "erfc", "tgamma", "lgamma", "ceil", "floor",
               "fmod", "trunc", "round", "lround", "llround", "rint", "lrint", "llrint", "nearbyint", "remainder",
               "remquo", "copysign", "nan", "nextafter", "nexttoward", "fdim", "fmax", "fmin", "fabs", "abs", "fma",
               "ceil", "getExponent", "IEEEremainder", "max", "min", "PI", "random", "scalb", "signum", "toRadians",
               "toDegrees", "ulp")));
      SetExcludeKeywords(new ArrayList<String>(
               Arrays.asList("public:", "private:", "protected:", "default:", "else", "do", "try")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList("\\{", "\\}", "\\(", "\\)")));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList("if", "else if", "else", "switch", "case", "default",
               "for", "foreach", "while", "do", "try", "catch")));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList("if", "catch", "switch", "for", "while", "else if")));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList()));
   }
}
