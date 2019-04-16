package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * XSLLanguageProperties class stores properties of XSL programming language
 * that will be used to count different types of code metrics for source code
 * written in XSL.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class XSLLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize XSL language properties
    */
   public XSLLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("XSL 1.0");
      SetLangName("XSL");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".xsl")));
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