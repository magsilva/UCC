package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * CCPPLanguageProperties class stores properties of C/C++ programming language
 * that will be used to count different types of code metrics for source code
 * written in C or C++.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class CCPPLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize C/C++ language properties
    */
   public CCPPLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("C11 - ISO/IEC 9899:2011 and C++14 - ISO/IEC 14882:2014");
      SetLangName("C_CPP");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".c", ".cc", ".cpp", ".h", ".hpp", ".hh", ".ipp")));
      SetCaseSensitive(true);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList(";")));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("//")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList("/*")));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList("*/")));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList("#")));
      SetCompilerDirKeywords(new ArrayList<String>(
               Arrays.asList("#define", "#dictionary", "#elif", "#else", "#endif", "#error", "#if", "#ifdef", "#ifndef",
                        "#import", "#include", "#line", "#module", "#pragma", "#undef", "#using")));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "'")));
      SetLineContChars(new ArrayList<String>(Arrays.asList("\\")));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList()));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("for", "while")));
      SetCondKeywords(
               new ArrayList<String>(Arrays.asList("else if", "if", "switch", "case", "for", "while", "else", "do")));
      SetLogicalOps(new ArrayList<String>(Arrays.asList("==", "!=", "<=", ">=", "!", "<", ">", "&&", "||")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("break", "case", "catch", "cerr", "cin", "class", "clog",
               "const", "const_cast", "continue", "cout", "default", "delete", "do", "dynamic_cast", "else", "enum",
               "entry", "explicit", "export", "extern", "file", "float", "for", "friend", "goto", "if", "new",
               "operator", "private", "protected", "public", "register", "reinterpret_cast", "return", "short",
               "short int", "signed", "sizeof", "size_t", "static", "static_cast", "stderr", "stdin", "stdout",
               "switch", "template", "throw", "try", "typedef", "typeid", "while")));
      SetDataKeywords(new ArrayList<String>(Arrays.asList("abstract", "asm", "auto", "bool", "char", "char16_t",
               "char32_t", "char64_t", "char8_t", "class", "const", "double", "enum", "explicit", "extern", "float",
               "friend", "inline", "int", "int16_t", "int32_t", "int64_t", "int8_t", "long", "long double", "long int",
               "long long", "long long int", "mutable", "namespace", "operator", "private", "protected", "public",
               "register", "short", "short int", "signed", "size_t", "static", "string", "struct", "template",
               "typedef", "typename", "uint16_t", "uint32_t", "uint64_t", "uint8_t", "union", "unsigned", "using",
               "virtual", "void", "volatile", "wchar_t")));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("%", "^", "++", "+", "--", "-", "*", "/", ">>", "<<")));// ,
                                                                                                                  // "~",
                                                                                                                  // "&",
                                                                                                                  // "|")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("log", "log10", "ilogb", "log1p", "log2", "logb")));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList("cos", "sin", "tan", "acos", "asin", "atan", "atan2",
               "cosh", "sinh", "tanh", "acosh", "asinh", "atanh")));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList("exp", "frexp", "ldexp", "modf", "exp2", "expm1",
               "scalbn", "scalbln", "pow", "sqrt", "cbrt", "hypot", "erf", "erfc", "tgamma", "lgamma", "ceil", "floor",
               "fmod", "trunc", "round", "lround", "llround", "rint", "lrint", "llrint", "nearbyint", "remainder",
               "remquo", "copysign", "nan", "nextafter", "nexttoward", "fdim", "fmax", "fmin", "fabs", "abs", "fma")));
      SetExcludeKeywords(new ArrayList<String>(
               Arrays.asList("public", "private", "protected", "default", "else", "do", "try", "EXPORT")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList("\\{", "\\}", "\\(", "\\)")));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList("if", "else if", "case", "while", "for", "catch")));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList("if", "catch", "switch", "for", "while", "else if")));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList("->")));
   }
}
