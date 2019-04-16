package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * DOSBatchLanguageProperties class stores properties of DOS Batch programming
 * language that will be used to count different types of code metrics for
 * source code written in DOS Batch.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class DOSBatchLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize DOS Batch language properties
    */
   public DOSBatchLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      // Partial Source:
      // https://en.wikipedia.org/wiki/Batch_file#Batch_file_parameters
      // Partial Source: http://www.robvanderwoude.com/batchfiles.php
      // Partial Source:
      // http://academic.evergreen.edu/projects/biophysics/technotes/program/batch.htm#%digit
      // Partial Source: http://www.robvanderwoude.com/if.php
      // Arithmetic Exp. Source: http://ss64.com/nt/set.html
      SetLangVersion("DOS Batch");
      SetLangName("DOS_BATCH");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".bat", ".cmd", ".btm")));
      SetCaseSensitive(false);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList()));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("::", "rem ")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList()));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList("@")));
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList("#!")));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "'")));
      SetLineContChars(new ArrayList<String>(Arrays.asList("\\")));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList()));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("for")));
      SetCondKeywords(new ArrayList<String>(Arrays.asList("if", "for")));
      SetLogicalOps(new ArrayList<String>(Arrays.asList("==", "equ", "neq", "lss", "leq", "gtr", "geq")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("assoc", "call", "cd", "date", "del", "defined", "dir",
               "echo", "endlocal", "erase", "exit", "fdisk", "find", "findramd", "findstr", "format", "goto", "in",
               "mem", "nslookup", "path", "pause", "ping", "popd", "prompt", "pushd", "ras", "readline", "regedit",
               "ren", "rundll", "schtasks", "set", "setlocal", "shell", "shift", "start", "truename", "type", "win",
               "wmic", "wupdmgr", "xcopy")));
      SetDataKeywords(new ArrayList<String>(Arrays.asList()));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("+", "-", "*", "/", "%", "<<", ">>", "&", "|", "~")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList()));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList()));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList()));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("else")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList("\\{", "\\}", "\\(", "\\)")));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList("if", "else", "for")));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList("if", "for")));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList()));
   }
}