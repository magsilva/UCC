package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ScalaLanguageProperties class stores properties of Scala programming language
 * that will be used to count different types of code metrics for source code
 * written in Scala.
 * 
 * Scala is VERY similar to Java and has a joint compiler to Java meaning that
 * Scala can be written with Java in the same file and be compiled.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class ScalaLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize Scala language properties
    */
   public ScalaLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("Scala 2");
      SetLangName("SCALA");
      // Source: https://en.wikipedia.org/wiki/Scala_(programming_language)
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".scala", ".sc")));
      SetCaseSensitive(true);
      // The ";" is optional
      // Source: http://www.tutorialspoint.com/scala/scala_basic_syntax.htm
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList(";")));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("//")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList("/*")));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList("*/")));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList("package", "import")));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "'")));
      SetLineContChars(new ArrayList<String>(Arrays.asList("\\")));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList("def")));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("for", "while")));
      SetCondKeywords(new ArrayList<String>(Arrays.asList("if", "else if", "match", "case", "for", "while")));
      SetLogicalOps(new ArrayList<String>(Arrays.asList("==", "!=", "<=", ">=", "<", ">", "!", "&&", "||")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("break", "continue", "try", "throw", "catch", "finally",
               "new", "return", "super", "override", "if", "else")));
      SetDataKeywords(new ArrayList<String>(
               Arrays.asList("private", "protected", "Byte", "Short", "Int", "Long", "Float", "Double", "Char",
                        "String", "Boolean", "Unit", "Null", "Nothing", "Any", "AnyRef", "var", "val")));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("+", "-", "*", "/", "%", ">>", "<<", ">>>", "&", "|", "^")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("math.log", "math.log10", "math.log1p")));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList("math.acos", "math.asin", "math.atan", "math.atan2",
               "math.cos", "math.cosh", "math.sin", "math.sinh", "math.tan", "math.tanh")));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList("math.abs", "math.cbrt", "math.ceil", "math.copySign",
               "math.E", "math.exp", "math.expm1", "math.floor", "math.getExponent", "math.hypot", "math.IEEEremainder",
               "math.max", "math.min", "math.nextAfter", "math.nextUp", "math.pow", "math.random", "math.rint",
               "math.round", "math.scalb", "math.signum", "math.sqrt", "math.toRadians", "math.toDegrees", "math.ulp",
               "math.addExact", "math.decrementExact", "math.floorDiv", "math.floorMod", "math.incrementExact",
               "math.multiplyExact", "math.negateExact", "math.nextDown", "math.subtractExact", "math.toIntExact")));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("else", "do", "try", "catch")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList("\\{", "\\}", "\\(", "\\)")));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList("if", "else if", "case", "while", "for", "catch")));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList("if", "else if", "match", "for", "while", "catch")));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList()));
   }
}
