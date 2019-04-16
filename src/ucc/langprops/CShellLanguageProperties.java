package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * CShellLanguageProperties class stores properties of CShell programming
 * language that will be used to count different types of code metrics for
 * source code written in CShell.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class CShellLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize CShell language properties
    */
   public CShellLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("Unversioned");
      SetLangName("CSHELL");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".csh", ".tcsh")));
      SetCaseSensitive(true);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList(";")));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("#")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList()));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList("#!")));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "\'", "`")));
      SetLineContChars(new ArrayList<String>(Arrays.asList("\\")));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList()));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("foreach", "while")));
      SetCondKeywords(
               new ArrayList<String>(Arrays.asList("if", "else if", "foreach", "while", "switch", "case", "else")));
      SetLogicalOps(new ArrayList<String>(
               Arrays.asList("!", "~", ">", "<", ">=", "<=", "!=", "==", "&", "^", "|", "&&", "||", "=~", "!~")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("alias", "break", "breaksw", "builtins", "case", "cd",
               "chdir", "continue", "dirs", "echo", "eval", "exec", "exit", "foreach", "glob", "goto", "if", "onintr",
               "popd", "pushd", "rehash", "repeat", "set", "setenv", "shift", "source", "switch", "time", "umask",
               "unalias", "unhash", "unset", "unsetenv", "while")));
      SetDataKeywords(new ArrayList<String>(Arrays.asList()));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("+", "-", "/", "*", "%", ">>", "<<", "++", "--")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList()));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList()));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList()));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("endif", "else", "end", "endsw", "then")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList("\\{", "\\}", "\\(", "\\)")));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList("if", "foreach", "while")));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList("if", "foreach", "while")));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList()));
   }
}
