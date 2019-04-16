package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * BashLanguageProperties class stores properties of Bash programming language
 * that will be used to count different types of code metrics for source code
 * written in Bash.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class BashLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize Bash language properties
    */
   public BashLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("Unversioned");
      SetLangName("BASH");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".sh", ".ksh")));
      SetCaseSensitive(true);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList(";")));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("#")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList()));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList("#!")));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "\'")));
      SetLineContChars(new ArrayList<String>(Arrays.asList("\\")));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList("function")));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("for", "while", "until")));
      SetCondKeywords(new ArrayList<String>(Arrays.asList("if", "elif", "for", "while", "until", "case", "select")));
      SetLogicalOps(new ArrayList<String>(Arrays.asList("&&", "||", "==", "!", "~", ">", "<", ">=", "<=", "-lt", "-gt",
               "-ge", "-le", "-eq", "-ne")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("alias", "awk", "bind", "break", "builtin", "caller",
               "case", "cd", "command", "continue", "coproc", "dirs", "echo", "elif", "enable", "eval", "exec", "exit",
               "export", "for", "function", "getopts", "hash", "if", "let", "mapfile", "popd", "printf", "pushd", "pwd",
               "read", "readarray", "readonly", "return", "select", "set", "shift", "source", "test", "time", "times",
               "trap", "ulimit", "umask", "unalias", "unset", "until", "while")));
      SetDataKeywords(new ArrayList<String>(Arrays.asList("declare", "local", "type", "typeset")));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("+", "-", "/", "*", "%", "^", "++", "--")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList()));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList()));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList()));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("fi", "else", "done", "do", "then", "esac")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList("\\{", "\\}", "\\(", "\\)")));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList("if", "for", "while", "until")));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList("if", "for", "while", "until")));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList()));
   }

}
