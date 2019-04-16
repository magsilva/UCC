package ucc.langprops;

import java.util.ArrayList;

/**
 * LanguageProperties class is an abstract class that provides a set of
 * properties common across different programming languages. Subclasses from
 * this class can be derived to form a language specific set of properties that
 * will be used to count different types of code metrics for source code written
 * in that programming language.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public abstract class LanguageProperties
{
   /**
    * Denotes version number for the language
    */
   private String LangVersion;

   /**
    * Name of the programming language
    */
   private String LangName;

   /**
    * Most similar known language. This property is only for custom language
    * types
    */
   private String SimilarTo;

   /**
    * File extensions supported by the programming language
    */
   private ArrayList<String> LangFileExts;

   /**
    * Boolean for whether the language is case sensitive
    */
   private boolean CaseSensitive;

   /**
    * Line termination character(s) for an executable instruction
    */
   private ArrayList<String> ExecLineTermChars;

   /**
    * Character(s) that denote beginning of a single line or embedded comment
    */
   private ArrayList<String> SingleLineCmntStartChars;

   /**
    * Character(s) that denote beginning of a multiple line comment
    */
   private ArrayList<String> MultiLineCmntStartChars;

   /**
    * Character(s) that denote end of a multiple line comment
    */
   private ArrayList<String> MultiLineCmntEndChars;

   /**
    * Character(s) for compiler directives
    */
   private ArrayList<String> CompilerDirChars;

   /**
    * Keyword(s) for compiler directives
    */
   private ArrayList<String> CompilerDirKeywords;

   /**
    * Character(s) that denote beginning of a string literal
    */
   private ArrayList<String> QuoteStartChars;

   /**
    * Character(s) that denote continuation of a line to the next line
    */
   private ArrayList<String> LineContChars;

   /**
    * Keyword(s) that denote beginning of a function/method
    */
   private ArrayList<String> FuncStartKeywords;

   /**
    * Keyword(s) that denote a loop
    */
   private ArrayList<String> LoopKeywords;

   /**
    * Keyword(s) that denote a conditional statement
    */
   private ArrayList<String> CondKeywords;

   /**
    * Keyword(s) that denote logical operators
    */
   private ArrayList<String> LogicalOps;

   /**
    * Keyword(s) that denote assignment operators
    */
   private ArrayList<String> AssignmentOps;

   /**
    * Additional executable keyword(s) not covered anywhere else
    */
   private ArrayList<String> OtherExecKeywords;

   /**
    * Keyword(s) that denote data declarations
    */
   private ArrayList<String> DataKeywords;

   /**
    * Keyword(s) that denote calculation operators
    */
   private ArrayList<String> CalcKeywords;

   /**
    * Keyword(s) that denote logarithmic operations
    */
   private ArrayList<String> LogOpKeywords;

   /**
    * Keyword(s) that denote trigonometric operators
    */
   private ArrayList<String> TrigOpKeywords;

   /**
    * Additional mathematical operations keyword(s) not covered in above
    * categories
    */
   private ArrayList<String> OtherMathKeywords;

   /**
    * List of keyword(s) to exclude from count
    */
   private ArrayList<String> ExcludeKeywords;

   /**
    * List of character(s) to exclude from count
    */
   private ArrayList<String> ExcludeCharacters;

   /**
    * Keyword(s) for cyclomatic complexity count
    */
   private ArrayList<String> CyclCmplexKeywords;

   /**
    * Keyword(s) for LSLOC count
    */
   private ArrayList<String> LslocKeywords;

   /**
    * Keyword(s) for pointer
    */
   private ArrayList<String> PointerKeywords;

   /**
    * Keyword(s) for bool operands
    */
   private ArrayList<String> BoolOperands;

   /**
    * Default constructor to initialize language properties
    */
   public LanguageProperties()
   {
      SetLangVersion("");
      SetLangName("UNKNOWN");
      SetCaseSensitive(true);
      SetSimilarTo("");

      LangFileExts = new ArrayList<String>();
      ExecLineTermChars = new ArrayList<String>();
      SingleLineCmntStartChars = new ArrayList<String>();
      MultiLineCmntStartChars = new ArrayList<String>();
      MultiLineCmntEndChars = new ArrayList<String>();
      CompilerDirChars = new ArrayList<String>();
      CompilerDirKeywords = new ArrayList<String>();
      QuoteStartChars = new ArrayList<String>();
      LineContChars = new ArrayList<String>();
      FuncStartKeywords = new ArrayList<String>();
      LoopKeywords = new ArrayList<String>();
      CondKeywords = new ArrayList<String>();
      LogicalOps = new ArrayList<String>();
      AssignmentOps = new ArrayList<String>();
      OtherExecKeywords = new ArrayList<String>();
      DataKeywords = new ArrayList<String>();
      CalcKeywords = new ArrayList<String>();
      LogOpKeywords = new ArrayList<String>();
      TrigOpKeywords = new ArrayList<String>();
      OtherMathKeywords = new ArrayList<String>();
      ExcludeKeywords = new ArrayList<String>();
      ExcludeCharacters = new ArrayList<String>();
      CyclCmplexKeywords = new ArrayList<String>();
      LslocKeywords = new ArrayList<String>();
      PointerKeywords = new ArrayList<String>();
      BoolOperands = new ArrayList<String>();
   }

   /**
    * @return the langPropVersion
    */
   public String GetLangVersion()
   {
      return LangVersion;
   }

   /**
    * @param langVersion
    *           the langPropVersion to set
    */
   public void SetLangVersion(String langVersion)
   {
      LangVersion = langVersion;
   }

   /**
    * @return the langName
    */
   public String GetLangName()
   {
      return LangName;
   }

   /**
    * @param langName
    *           the langName to set
    */
   public void SetLangName(String langName)
   {
      LangName = langName;
   }

   /**
    * 
    * @param similarLang
    *           the similarLang to set
    */
   public void SetSimilarTo(String similarLang)
   {
      SimilarTo = similarLang;
   }

   /**
    * 
    * @return the SimilarTo language
    */
   public String GetSimilarTo()
   {
      return SimilarTo;
   }

   /**
    * @return the langFileExts
    */
   public ArrayList<String> GetLangFileExts()
   {
      return LangFileExts;
   }

   /**
    * @param langFileExts
    *           the langFileExts to set
    */
   public void SetLangFileExts(ArrayList<String> langFileExts)
   {
      LangFileExts = langFileExts;
   }

   /**
    * @param caseSensitive
    *           the caseSensitive setting to set
    */
   public void SetCaseSensitive(boolean caseSensitive)
   {
      CaseSensitive = caseSensitive;
   }

   /**
    * @return the case sensitivity of the language
    */
   public boolean IsCaseSensitive()
   {
      return CaseSensitive;
   }

   /**
    * @return the execLineTermChars
    */
   public ArrayList<String> GetExecLineTermChars()
   {
      return ExecLineTermChars;
   }

   /**
    * @param execLineTermChars
    *           the execLineTermChars to set
    */
   public void SetExecLineTermChars(ArrayList<String> execLineTermChars)
   {
      ExecLineTermChars = execLineTermChars;
   }

   /**
    * @return the singleLineCmntStartChars
    */
   public ArrayList<String> GetSingleLineCmntStartChars()
   {
      return SingleLineCmntStartChars;
   }

   /**
    * @param singleLineCmntStartChars
    *           the singleLineCmntStartChars to set
    */
   public void SetSingleLineCmntStartChars(ArrayList<String> singleLineCmntStartChars)
   {
      SingleLineCmntStartChars = singleLineCmntStartChars;
   }

   /**
    * @return the multilineCmntStartChars
    */
   public ArrayList<String> GetMultiLineCmntStartChars()
   {
      return MultiLineCmntStartChars;
   }

   /**
    * @param multilineCmntStartChars
    *           the multilineCmntStartChars to set
    */
   public void SetMultiLineCmntStartChars(ArrayList<String> multilineCmntStartChars)
   {
      MultiLineCmntStartChars = multilineCmntStartChars;
   }

   /**
    * @return the multilineCmntEndChars
    */
   public ArrayList<String> GetMultiLineCmntEndChars()
   {
      return MultiLineCmntEndChars;
   }

   /**
    * @param multilineCmntEndChars
    *           the multilineCmntEndChars to set
    */
   public void SetMultiLineCmntEndChars(ArrayList<String> multilineCmntEndChars)
   {
      MultiLineCmntEndChars = multilineCmntEndChars;
   }

   /**
    * @return the compilerDirChars
    */
   public ArrayList<String> GetCompilerDirChars()
   {
      return CompilerDirChars;
   }

   /**
    * @param compilerDirChars
    *           the compilerDirChars to set
    */
   public void SetCompilerDirChars(ArrayList<String> compilerDirChars)
   {
      CompilerDirChars = compilerDirChars;
   }

   /**
    * @return the compilerDirKeywords
    */
   public ArrayList<String> GetCompilerDirKeywords()
   {
      return CompilerDirKeywords;
   }

   /**
    * @param compilerDirKeywords
    *           the compilerDirKeywords to set
    */
   public void SetCompilerDirKeywords(ArrayList<String> compilerDirKeywords)
   {
      CompilerDirKeywords = compilerDirKeywords;
   }

   /**
    * @return the quoteStartChars
    */
   public ArrayList<String> GetQuoteStartChars()
   {
      return QuoteStartChars;
   }

   /**
    * @param quoteStartChars
    *           the quoteStartChars to set
    */
   public void SetQuoteStartChars(ArrayList<String> quoteStartChars)
   {
      QuoteStartChars = quoteStartChars;
   }

   /**
    * @return the LineContChars
    */
   public ArrayList<String> GetLineContChars()
   {
      return LineContChars;
   }

   /**
    * @param lineContChars
    *           the LineContChars to set
    */
   public void SetLineContChars(ArrayList<String> lineContChars)
   {
      LineContChars = lineContChars;
   }

   /**
    * @return the funcStartKeywords
    */
   public ArrayList<String> GetFuncStartKeywords()
   {
      return FuncStartKeywords;
   }

   /**
    * @param funcStartKeywords
    *           the funcStartKeywords to set
    */
   public void SetFuncStartKeywords(ArrayList<String> funcStartKeywords)
   {
      FuncStartKeywords = funcStartKeywords;
   }

   /**
    * @return the LoopKeywords
    */
   public ArrayList<String> GetLoopKeywords()
   {
      return LoopKeywords;
   }

   /**
    * @param loopKeywords
    *           the LoopKeywords to set
    */
   public void SetLoopKeywords(ArrayList<String> loopKeywords)
   {
      LoopKeywords = loopKeywords;
   }

   /**
    * @return the CondKeywords
    */
   public ArrayList<String> GetCondKeywords()
   {
      return CondKeywords;
   }

   /**
    * @param condKeywords
    *           the CondKeywords to set
    */
   public void SetCondKeywords(ArrayList<String> condKeywords)
   {
      CondKeywords = condKeywords;
   }

   /**
    * @return the logicalOps
    */
   public ArrayList<String> GetLogicalOps()
   {
      return LogicalOps;
   }

   /**
    * @param logicalOps
    *           the logicalOps to set
    */
   public void SetLogicalOps(ArrayList<String> logicalOps)
   {
      LogicalOps = logicalOps;
   }

   /**
    * @return the assignmentOps
    */
   public ArrayList<String> GetAssignmentOps()
   {
      return AssignmentOps;
   }

   /**
    * @param assignmentOps
    *           the assignmentOps to set
    */
   public void SetAssignmentOps(ArrayList<String> assignmentOps)
   {
      AssignmentOps = assignmentOps;
   }

   /**
    * @return the otherExecKeywords
    */
   public ArrayList<String> GetOtherExecKeywords()
   {
      return OtherExecKeywords;
   }

   /**
    * @param otherExecKeywords
    *           the otherExecKeywords to set
    */
   public void SetOtherExecKeywords(ArrayList<String> otherExecKeywords)
   {
      OtherExecKeywords = otherExecKeywords;
   }

   /**
    * @return the dataKeywords
    */
   public ArrayList<String> GetDataKeywords()
   {
      return DataKeywords;
   }

   /**
    * @param dataKeywords
    *           the dataKeywords to set
    */
   public void SetDataKeywords(ArrayList<String> dataKeywords)
   {
      DataKeywords = dataKeywords;
   }

   /**
    * @return the CalcKeywords
    */
   public ArrayList<String> GetCalcKeywords()
   {
      return CalcKeywords;
   }

   /**
    * @param calcKeywords
    *           the CalcKeywords to set
    */
   public void SetCalcKeywords(ArrayList<String> calcKeywords)
   {
      CalcKeywords = calcKeywords;
   }

   /**
    * @return the logOpKeywords
    */
   public ArrayList<String> GetLogOpKeywords()
   {
      return LogOpKeywords;
   }

   /**
    * @param logOpKeywords
    *           the logOpKeywords to set
    */
   public void SetLogOpKeywords(ArrayList<String> logOpKeywords)
   {
      LogOpKeywords = logOpKeywords;
   }

   /**
    * @return the trigOpKeywords
    */
   public ArrayList<String> GetTrigOpKeywords()
   {
      return TrigOpKeywords;
   }

   /**
    * @param trigOpKeywords
    *           the trigOpKeywords to set
    */
   public void SetTrigOpKeywords(ArrayList<String> trigOpKeywords)
   {
      TrigOpKeywords = trigOpKeywords;
   }

   /**
    * @return the otherMathKeywords
    */
   public ArrayList<String> GetOtherMathKeywords()
   {
      return OtherMathKeywords;
   }

   /**
    * @param otherMathKeywords
    *           the otherMathKeywords to set
    */
   public void SetOtherMathKeywords(ArrayList<String> otherMathKeywords)
   {
      OtherMathKeywords = otherMathKeywords;
   }

   /**
    * @return the excludeKeywords
    */
   public ArrayList<String> GetExcludeKeywords()
   {
      return ExcludeKeywords;
   }

   /**
    * @param excludeKeywords
    *           the excludeKeywords to set
    */
   public void SetExcludeKeywords(ArrayList<String> excludeKeywords)
   {
      ExcludeKeywords = excludeKeywords;
   }

   /**
    * @return the ExcludeCharacters
    */
   public ArrayList<String> GetExcludeCharacters()
   {
      return ExcludeCharacters;
   }

   /**
    * @param excludeCharacters
    *           the excludeCharacters to set
    */
   public void SetExcludeCharacters(ArrayList<String> excludeCharacters)
   {
      ExcludeCharacters = excludeCharacters;
   }

   /**
    * @return the CyclCmplexKeywords
    */
   public ArrayList<String> GetCyclCmplexKeywords()
   {
      return CyclCmplexKeywords;
   }

   /**
    * @param cyclCmplexKeywords
    *           the CyclCmplexKeywords to set
    */
   public void SetCyclCmplexKeywords(ArrayList<String> cyclCmplexKeywords)
   {
      CyclCmplexKeywords = cyclCmplexKeywords;
   }

   /**
    * @return the CyclCmplexKeywords
    */
   public ArrayList<String> GetLslocKeywords()
   {
      return LslocKeywords;
   }

   /**
    * @param lslocKeywords
    *           the LslocKeywords to set
    */
   public void SetLslocKeywords(ArrayList<String> lslocKeywords)
   {
      LslocKeywords = lslocKeywords;
   }

   /**
    * @return the pointerKeywords
    */
   public ArrayList<String> GetPointerKeywords()
   {
      return PointerKeywords;
   }

   /**
    * @param pointerKeywords
    *           the pointerKeywords to set
    */
   public void SetPointerKeywords(ArrayList<String> pointerKeywords)
   {
      PointerKeywords = pointerKeywords;
   }

   /**
    * @return the BoolOperands
    */
   public ArrayList<String> GetBoolOperands()
   {
      return BoolOperands;
   }

   /**
    * @param BoolOperands
    *           the BoolOperands to set
    */
   public void SetBoolOperands(ArrayList<String> boolOperands)
   {
      BoolOperands = boolOperands;
   }
}
