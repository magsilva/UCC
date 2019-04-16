package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * JavaLanguageProperties class stores properties of Java programming language
 * that will be used to count different types of code metrics for source code
 * written in Java.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class JavaLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize Java language properties
    */
   public JavaLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();
      SetLangVersion("Java 8");
      SetLangName("JAVA");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".java")));
      SetCaseSensitive(true);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList(";")));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("//")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList("/*")));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList("*/")));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList("package", "import")));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "'")));
      SetLineContChars(new ArrayList<String>(Arrays.asList("\\")));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList()));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("for", "while")));
      SetCondKeywords(new ArrayList<String>(Arrays.asList("if", "else", "else if", "switch", "case", "for", "while")));
      SetLogicalOps(new ArrayList<String>(Arrays.asList("==", "!=", "<", ">", "<=", ">=", "!", "&&", "||")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("break", "continue", "try", "throw", "throws", "catch",
               "finally", "new", "return", "super", "this", "default", "case", "do", "else", "for", "if", "switch", "while")));
      SetDataKeywords(new ArrayList<String>(Arrays.asList("abstract", "ArrayList", "boolean", "byte", "char", "class",
               "const", "double", "enum", "extends", "final", "float", "HashMap", "HashSet", "implements", "int",
               "interface", "LinkedHashMap", "LinkedList", "long", "native", "operator", "private", "protected",
               "public", "short", "static", "String", "template", "TreeMap", "Vector", "void", "volatile")));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("%", "^", "++", "+", "--", "-", "*", "//", ">>", "<<")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("Math.log", "Math.log10", "Math.log1p")));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList("Math.acos", "Math.asin", "Math.atan", "Math.atan2",
               "Math.cos", "Math.cosh", "Math.sin", "Math.sinh", "Math.tan", "Math.tanh")));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList("Math.abs", "Math.cbrt", "Math.ceil", "Math.copySign",
               "Math.E", "Math.exp", "Math.expm1", "Math.floor", "Math.getExponent", "Math.hypot", "Math.IEEEremainder",
               "Math.max", "Math.min", "Math.nextAfter", "Math.nextUp", "Math.PI", "Math.pow", "Math.random",
               "Math.rint", "Math.round", "Math.scalb", "Math.signum", "Math.sqrt", "Math.toRadians", "Math.toDegrees",
               "Math.ulp", "Math.addExact", "Math.decrementExact", "Math.floorDiv", "Math.floorMod",
               "Math.incrementExact", "Math.multiplyExact", "Math.negateExact", "Math.nextDown", "Math.subtractExact",
               "Math.toIntExact")));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("else", "do", "try")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList("\\{", "\\}", "\\(", "\\)")));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList("if", "else if", "case", "while", "for", "catch")));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList("if", "catch", "switch", "for", "while","else if")));
      SetBoolOperands(new ArrayList<String>(Arrays.asList("true", "false")));
   }
}
