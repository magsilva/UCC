package ucc.langprops;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ucc.datatypes.UCCFile;

/**
 * GoLanguageProperties class stores properties of GO programming language
 * that will be used to count different types of code metrics for source code
 * written in GO.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class GoLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize GO language properties
    */
   public GoLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("GO 1.9");
      SetLangName("GO");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".go")));
      SetCaseSensitive(true);
      SetCaseSensitive(true);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList(";")));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("//")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList("/*")));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList("*/")));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList("package", "import")));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "'")));
      /*Not sure what this is*/
      SetLineContChars(new ArrayList<String>(Arrays.asList("\\")));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList("func")));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("for")));
      SetCondKeywords(new ArrayList<String>(Arrays.asList("if", "else", "else if", "switch", "case", "for")));
      SetLogicalOps(new ArrayList<String>(Arrays.asList("==", "!=", "<", ">", "<=", ">=", "!", "&&", "||")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("break", "continue", "return", "default", "fallthrough", "defer", "go", "goto", "range","select","recover")));
      SetDataKeywords(new ArrayList<String>(Arrays.asList("uint8","uint16","uint32","uint64","int8","int16","int32","int64","float32","float64","complex64","complex128","byte","rune","uint","int","uintptr","chan","var","const","interface","string","struct","map","type")));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("%", "^", "++", "+", "--", "-", "*", "//", ">>", "<<","&","|","*")));
      
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("math.Log", "math.Log10", "math.Log1p", "math.Log2", "math.logb")));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList("math.Asin","math.Atan","math.Acosh","math.Asinh","math.Atan2","math.Atanh","math.Cos","math.Cosh","math.Sin","math.Sinh","math.Tan","math.Tanh","math.Acos")));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList("math.Abs", "math.Cbrt", "math.Ceil", "math.Copysign", "math.Dim",
               "math.Erf", "math.Erfc", "math.Exp","math.Exp2","math.Expm1","math.Float32bits","math.Float32frombits","math.Float64bits","math.Float64frombits","math.Floor", "math.Gamma", "math.Hypot", "math.Ilogb","math.Inf","math.J0","math.J1","math.Jn",
               "math.Max", "math.Min", "math.Mod","math.NaN","math.Pow","math.Pow10","math.Remainder","math.Sqrt","math.Trunc","math.Y0","math.Y1")));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("else","default","select")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList("\\{", "\\}", "\\(", "\\)")));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList("if", "else if", "case", "for")));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList("if", "switch", "for", "case")));
   }
}
