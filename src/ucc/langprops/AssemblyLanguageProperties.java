package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * AssemblyLanguageProperties class stores properties of Assembly programming
 * language that will be used to count different types of code metrics for
 * source code written in Assembly.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class AssemblyLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize Assembly language properties
    */
   public AssemblyLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("Assembly");
      SetLangName("ASSEMBLY");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".asm", ".s", ".asm.ppc")));
      SetCaseSensitive(false);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList()));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("#", ";", "|", "//")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList("/*")));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList("*/")));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList()));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"")));
      SetLineContChars(new ArrayList<String>(Arrays.asList("\\")));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList()));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList()));
      SetCondKeywords(new ArrayList<String>(Arrays.asList()));
      SetLogicalOps(new ArrayList<String>(Arrays.asList()));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList()));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("section .text", ".code", ".text", "section .txt",
               ".section .text", ".init", ".fini", ".ktext")));
      SetDataKeywords(new ArrayList<String>(Arrays.asList("section .data", ".data", "section .bss", ".const", ".bss",
               ".rdata", ".section name, \"d\"", ".sdata", ".csect[RW]", ".section name, \"b\"", ".csect[TC0]", ".sbss",
               ".csect[TC]", ".lit", ".csect[TD]", ".csect[UA]", ".csect[DS]", ".csect[BS]", ".csect[UC]", ".csect[ER]",
               ".csect[SD]", ".csect[LD]", ".csect[CM]")));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList()));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList()));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList()));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList()));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList()));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList()));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList()));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList()));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList()));
   }
}