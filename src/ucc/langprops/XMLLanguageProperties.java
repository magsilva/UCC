package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * XMLLanguageProperties class stores properties of XML programming language
 * that will be used to count different types of code metrics for source code
 * written in XML.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class XMLLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize XML language properties
    */
   public XMLLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("XML 1.0");
      SetLangName("XML");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".xml")));
      SetCaseSensitive(true);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList()));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList()));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList("<!--")));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList("-->")));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList()));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "'")));
      SetLineContChars(new ArrayList<String>(Arrays.asList()));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList()));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList()));
      SetCondKeywords(new ArrayList<String>(Arrays.asList()));
      SetLogicalOps(new ArrayList<String>(Arrays.asList()));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList()));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList()));
      SetDataKeywords(new ArrayList<String>(Arrays.asList()));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList()));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList()));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList()));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList()));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList()));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList()));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList()));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList()));
   }
}