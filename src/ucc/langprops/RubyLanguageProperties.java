package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * RubyLanguageProperties class stores properties of Ruby programming language
 * that will be used to count different types of code metrics for source code
 * written in Ruby.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class RubyLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize Ruby language properties
    */
   public RubyLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("Ruby 2.3.1");
      SetLangName("RUBY");
      // Source: http://rubylearning.com/satishtalim/first_ruby_program.html
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".rb", ".rbw")));
      SetCaseSensitive(true);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList(";")));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("#")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList("=begin")));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList("=end")));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList()));
      // Source:
      // http://commandercoriander.net/blog/2012/11/08/include-require-and-modules/
      // Source: http://www.tutorialspoint.com/ruby/ruby_modules.htm
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList("#!", "$LOAD_PATH", "require_relative")));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "'")));
      SetLineContChars(new ArrayList<String>(Arrays.asList("\\")));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList("def")));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("for", "while", "until")));
      SetCondKeywords(new ArrayList<String>(
               Arrays.asList("elsif", "if", "else", "case", "when", "for", "while", "until", "unless")));
      SetLogicalOps(new ArrayList<String>(
               Arrays.asList("&&", "||", "==", "!=", "<=>", "!", "and", "not", "or", ">", "<", ">=", "<=", "===")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("alias", "begin", "BEGIN", "break", "case", "catch",
               "collect", "do", "each", "else", "elsif", "end", "END", "ensure", "exception", "exit", "for", "if",
               "module", "new", "next", "puts", "print", "redo", "rescue", "retry", "return", "throw", "undef",
               "unless", "until", "when", "while", "yield")));
      SetDataKeywords(new ArrayList<String>(Arrays.asList()));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("%", "+", "-", "*", "**", "/", ">>", "<<", "~", "&", "|")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("log", "log10")));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList("atan2", "cos", "sin", "tan")));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList("exp", "frexp", "ldexp", "rand", "sqrt", "srand")));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("else", "do", "when", "end")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList("\\(", "\\)", "\\{", "\\}")));
      SetCyclCmplexKeywords(new ArrayList<String>(
               Arrays.asList("case", "catch", "elsif", "for", "if", "try", "until", "unless", "while")));
      // IMPORTANT NOTE: Despite the attempt to maintain alphabetical order,
      // the keyword, "do", must remain the LAST WORD for which WE SEARCH.
      // The keyword "do" can be preceded by a number of other words in this
      // list
      // and we do not want to over count. The algorithm can be made more
      // efficient
      // by not having to account for "do"'s location in the last, being allowed
      // to
      // assume that "do" not will not precede any other word.
      SetLslocKeywords(new ArrayList<String>(Arrays.asList("alias", "begin", "BEGIN", "break", "case", "catch",
               "collect", "def", "each", "elsif", "END", "ensure", "exception", "exit", "for", "if", "module", "new",
               "next", "puts", "print", "redo", "rescue", "retry", "return", "throw", "undef", "unless", "until",
               "when", "while", "yield")));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList()));
   }
}