package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * PerlLanguageProperties class stores properties of Perl programming language
 * that will be used to count different types of code metrics for source code
 * written in Perl.
 * 
 * @author Integrity Applications Incorporated
 * 
 *         Much of this data was taken from: TutorialsPoint: Source:
 *         http://www.tutorialspoint.com/perl/
 * 
 *         OR XAV: Source: http://www.xav.com/perl/lib/Pod/perlfunc.html
 */
public class PerlLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize Perl language properties
    */
   public PerlLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("Perl 5");
      SetLangName("PERL");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".pl", ".pm")));
      SetCaseSensitive(true);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList(";")));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("#")));
      SetMultiLineCmntStartChars(new ArrayList<String>());
      SetMultiLineCmntEndChars(new ArrayList<String>());
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList("#!", "import", "no", "package", "require", "use")));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "'")));
      SetLineContChars(new ArrayList<String>());
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList("sub")));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("while", "until", "for", "foreach")));
      SetCondKeywords(new ArrayList<String>(
               Arrays.asList("if", "else", "elsif", "while", "until", "for", "foreach", "unless")));
      SetLogicalOps(new ArrayList<String>(Arrays.asList("eq", "ne", "lt", "gt", "le", "ge", "==", "!=", "<", ">", "<=",
               ">=", "and", "or", "not", "cmp", "&&", "||", "!", "<=>")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=")));
      SetOtherExecKeywords(new ArrayList<String>(
               Arrays.asList("break", "case", "catch", "default", "do", "else", "elsif", "for", "foreach", "goto", "if",
                        "last", "new", "next", "return", "self", "switch", "try", "unless", "until", "while")));
      SetDataKeywords(new ArrayList<String>(
               Arrays.asList("AUTOLOAD", "BEGIN", "CHECK", "CORE", "DESTROY", "END", "INIT", "NULL")));
      SetCalcKeywords(new ArrayList<String>(
               Arrays.asList("++", "+", "--", "-", "//", "*", "**", ">>", "<<", "%", "&", "|", "^", "~")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("log")));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList("atan2", "cos", "sin", "tan")));
      SetOtherMathKeywords(new ArrayList<String>(
               Arrays.asList("abs", "exp", "sqrt", "rand", "srand", "time", "oct", "hex", "int")));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("else", "do", "try", "switch")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList("\\{", "\\}", "\\(", "\\)")));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList("case", "catch", "elsif", "for", "foreach", "if",
               "switch", "try", "unless", "while", "until")));
      SetLslocKeywords(new ArrayList<String>(
               Arrays.asList("if", "elsif", "unless", "until", "foreach", "catch", "switch", "for", "while")));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList()));
   }
}
