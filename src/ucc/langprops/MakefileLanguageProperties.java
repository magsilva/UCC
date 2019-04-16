package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * MaklefileLanguageProperties class stores properties of Maklefile programming
 * language that will be used to count different types of code metrics for
 * source code written in Maklefile.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class MakefileLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize Maklefile language properties
    */
   public MakefileLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("Unversioned");
      SetLangName("MAKEFILE");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".mak", ".make", ".gmk", ".am", ".in", ".win")));
      SetCaseSensitive(true);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList()));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("#")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList()));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList("include", "-include", "sinclude")));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "\'")));
      SetLineContChars(new ArrayList<String>(Arrays.asList("\\")));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList()));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("for", "while")));
      SetCondKeywords(new ArrayList<String>(Arrays.asList("ifeq", "ifneq", "ifdef", "ifndef", "else", "for", "while")));
      SetLogicalOps(new ArrayList<String>(Arrays.asList()));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=", "?=", ":=")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList()));
      SetDataKeywords(new ArrayList<String>(Arrays.asList()));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("+", "-", "/", "*")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList()));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList()));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList()));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("endif", "else", "done")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList()));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList("if", "for", "while")));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList("if", "for", "while")));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList()));
   }

}
