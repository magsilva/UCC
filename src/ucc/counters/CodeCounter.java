package ucc.counters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ucc.counters.handlers.CommentHandler;
import ucc.counters.handlers.MultiLanguageHandler;
import ucc.counters.handlers.QuoteHandler;
import ucc.datatypes.CmplxDataType;
import ucc.datatypes.Constants;
import ucc.datatypes.UCCFile;
import ucc.langprops.CCPPLanguageProperties;
import ucc.langprops.LanguageProperties;
import ucc.main.RuntimeParameters;
import ucc.utils.FileUtils;
import ucc.utils.StringUtils.StringLengthListSort;

/**
 * CodeCounter class performs various code counting operations on baseline(s)
 * identified by the user. CodeCounter is the base class to be used for deriving
 * language specific counters. It contains default code counter
 * algorithms/methods to be used when no language specific algorithm/method is
 * specified.
 *
 * @author Integrity Applications Incorporated
 *
 */
public class CodeCounter
{
   /** Instantiate the Log4j2 logger for this class */
   private static final Logger logger = LogManager.getLogger(CodeCounter.class);

   /**
    * Language properties to be used with this code counter
    */
   protected LanguageProperties LangProps;

   /**
    * A handle to the RuntimeParameters class's single instance
    */
   protected static RuntimeParameters RtParams;

   /** Array list containing keyword(s) for compiler directives */
   protected ArrayList<String> CompilerDir;

   /**
    * Array list containing line termination character(s) for an executable
    * instruction
    */
   protected ArrayList<String> LineTerminator;

   /**
    * Array list containing additional executable keyword(s) not covered
    * anywhere else
    */
   protected ArrayList<String> ExecKeywords;

   /** Array list containing keyword(s) that denote data declarations */
   protected ArrayList<String> DataKeyword;

   /**
    * Array list containing Additional mathematical operations keyword(s) not
    * covered in other math categories
    */
   protected ArrayList<String> MathKeywords;

   /** Array list containing keyword(s) that denote trigonometric operators */
   protected ArrayList<String> TrigKeywords;

   /** Array list containing keyword(s) that denote logarithmic operations */
   protected ArrayList<String> LogKeywords;

   /** Array list containing keyword(s) that denote calculation operations */
   protected ArrayList<String> CalcKeywords;

   /** Array list containing keyword(s) that denote a conditional statement */
   protected ArrayList<String> CondKeywords;

   /** Array list containing keyword(s) that denote logical operators */
   protected ArrayList<String> LogicKeywords;

   /** Array list containing keyword(s) that denote assignment operators */
   protected ArrayList<String> AssignKeywords;

   /** Array list containing keyword(s) for pointer */
   protected ArrayList<String> PntrKeywords;

   /** Array list containing keyword(s) for operand */
   protected ArrayList<String> BoolOperands;

   /** Array list containing keyword(s) for LSLOC count */
   protected ArrayList<String> LslocKeywords;

   /** Array list containing keyword(s) that denote a loop */
   protected ArrayList<String> LoopKeywords;

   /** Array list containing keyword(s) for cyclomatic complexity count */
   protected ArrayList<String> CyclCmplexKeywords;

   /** Array list containing unique data keyword(s) */
   protected ArrayList<String> UniqueDataKeywords;

   /**
    * Array list character(s) that denote beginning of a single line or embedded
    * comment
    */
   protected ArrayList<String> SingleLineComment;

   /**
    * Array list containing character(s) that denote beginning of a multiple
    * line comment
    */
   protected ArrayList<String> MultiLineCommentStart;

   /**
    * Array list containing character(s) that denote end of a multiple line
    * comment
    */
   protected ArrayList<String> MultiLineCommentEnd;

   /**
    * Array list containing character(s) that denote beginning of a string
    * literal
    */
   protected ArrayList<String> QuoteChar;

   /** Array list containing character(s) for compiler directives */
   protected ArrayList<String> CompilerChar;

   /**
    * Array list containing character(s) that denote continuation of a line to
    * the next line
    */
   protected ArrayList<String> LineContChars;

   /** Array list of keyword(s) to exclude from count */
   protected ArrayList<String> ExcludeKeyword;

   /** Array list of character(s) to exclude from count */
   protected ArrayList<String> ExcludeCharacter;

   /** Array list containing that denote beginning of a function/method */
   protected ArrayList<String> FunctionKeywords;

   /** Instantiated comment handler */
   protected CommentHandler CommentHandler;

   /** Instantiated quote handler */
   protected QuoteHandler QuoteHandler;

   /** Instantiated multi-language handler */
   protected MultiLanguageHandler MultiLanguageHandler;

   /**
    * Maintains whether a line in run-time processing should be written as LSLOC
    * for the Differencer
    */
   protected int lslocLineValue;
   protected static String lslocLineValueDelim = "|*||*|";
   protected int truncateLinesCount = 0; // counter for truncated lines

   public static String getLslocLineValueDelim()
   {
      return lslocLineValueDelim;
   }

   /**
    * Class for storing both the line and a required boolean flag to return to
    * counter function.
    */
   static class ParseResult
   {
      String returnLine;
      boolean returnFlag;
   }

   /**
    * Default constructor to instantiate a CodeCounter object
    */
   public CodeCounter()
   {
      // Initialize class variables
      RtParams = RuntimeParameters.GetInstance();

      // Default language property is for C/C++
      LangProps = new CCPPLanguageProperties();

      // Load language properties into class member variables
      LoadLangProperties();

      // Create the comment handler
      CommentHandler = new CommentHandler(LangProps);

      // Initialize value used in line by line processing loop
      lslocLineValue = 0;
   }

   /**
    * A constructor to instantiate a CodeCounter object with a given language
    * properties object
    *
    * @param langProps
    *           Language properties for this counter
    */
   public CodeCounter(LanguageProperties langProps)
   {
      // Initialize class variables
      RtParams = RuntimeParameters.GetInstance();

      // Uses the read in language properties
      LangProps = langProps;

      // Load language properties into class member variables
      LoadLangProperties();

      // Create the comment handler
      CommentHandler = new CommentHandler(LangProps);

      // Initialize value used in line by line processing loop
      lslocLineValue = 0;
   }

   /**
    * Load language properties to use with the counter algorithm
    */
   protected void LoadLangProperties()
   {
      CompilerDir = LangProps.GetCompilerDirKeywords();
      LineTerminator = LangProps.GetExecLineTermChars();
      ExecKeywords = LangProps.GetOtherExecKeywords();
      DataKeyword = LangProps.GetDataKeywords();

      // Initialize keywords for complexity
      MathKeywords = LangProps.GetOtherMathKeywords();
      TrigKeywords = LangProps.GetTrigOpKeywords();
      LogKeywords = LangProps.GetLogOpKeywords();
      CalcKeywords = LangProps.GetCalcKeywords();
      CondKeywords = LangProps.GetCondKeywords();
      LogicKeywords = LangProps.GetLogicalOps();
      AssignKeywords = LangProps.GetAssignmentOps();
      PntrKeywords = LangProps.GetPointerKeywords();
      BoolOperands = LangProps.GetBoolOperands();

      // Initialize keywords for cyclomatic complexity
      CyclCmplexKeywords = LangProps.GetCyclCmplexKeywords();

      // SLOC keywords
      LslocKeywords = LangProps.GetLslocKeywords();
      LoopKeywords = LangProps.GetLoopKeywords();
      CondKeywords = LangProps.GetCondKeywords();
      SingleLineComment = LangProps.GetSingleLineCmntStartChars();
      MultiLineCommentStart = LangProps.GetMultiLineCmntStartChars();
      MultiLineCommentEnd = LangProps.GetMultiLineCmntEndChars();
      QuoteChar = LangProps.GetQuoteStartChars();
      CompilerChar = LangProps.GetCompilerDirChars();
      LineContChars = LangProps.GetLineContChars();
      ExcludeKeyword = LangProps.GetExcludeKeywords();
      ExcludeCharacter = LangProps.GetExcludeCharacters();
      FunctionKeywords = LangProps.GetFuncStartKeywords();

      StringLengthListSort ss = new StringLengthListSort(); // Used to sort
                                                            // lists by length
                                                            // (large to small)

      // Sort all the needed lists
      Collections.sort(CompilerDir, ss);
      Collections.sort(LineTerminator, ss);
      Collections.sort(ExecKeywords, ss);
      Collections.sort(DataKeyword, ss);
      Collections.sort(MathKeywords, ss);
      Collections.sort(TrigKeywords, ss);
      Collections.sort(LogKeywords, ss);
      Collections.sort(CalcKeywords, ss);
      Collections.sort(CondKeywords, ss);
      Collections.sort(LogicKeywords, ss);
      Collections.sort(AssignKeywords, ss);
      Collections.sort(PntrKeywords, ss);
      Collections.sort(BoolOperands, ss);
      Collections.sort(CyclCmplexKeywords, ss);
      Collections.sort(LslocKeywords, ss);
      Collections.sort(LoopKeywords, ss);
      Collections.sort(CondKeywords, ss);
      Collections.sort(SingleLineComment, ss);
      Collections.sort(MultiLineCommentStart, ss);
      Collections.sort(MultiLineCommentEnd, ss);
      Collections.sort(QuoteChar, ss);
      Collections.sort(CompilerChar, ss);
      Collections.sort(LineContChars, ss);
      Collections.sort(ExcludeKeyword, ss);
      Collections.sort(ExcludeCharacter, ss);
      Collections.sort(FunctionKeywords, ss);
   }

   /**
    * Set unique data keywords for the language counter (if any)
    */
   protected void SetUniqueDataKeywords()
   {
      UniqueDataKeywords = new ArrayList<String>();
      UniqueDataKeywords.add("class");
      UniqueDataKeywords.add("struct");
      UniqueDataKeywords.add("union");
      UniqueDataKeywords.add("enum");
      UniqueDataKeywords.add("template");
      UniqueDataKeywords.add("typename");
      UniqueDataKeywords.add("namespace");

      StringLengthListSort ss = new StringLengthListSort(); // Used to sort
                                                            // lists by length
                                                            // (large to small)
      Collections.sort(UniqueDataKeywords, ss);
   }

   /**
    * Initializes complexity keyword names from a given list of keywords
    *
    * @param keywords
    *           A list of keywords
    * @param cmplxKeywords
    *           A list of keywords for complexity metrics counter
    */
   protected void InitCmplxKeywords(ArrayList<String> keywords, ArrayList<CmplxDataType> cmplxKeywords)
   {
      if (cmplxKeywords != null)
      {
         for (int i = 0; i < keywords.size(); i++)
         {
            cmplxKeywords.add(new CmplxDataType(keywords.get(i)));
         }
      }
   }

   /**
    * Initialize all complexity keywords for the file
    * 
    * @param cntrResult
    *           A results object to store counter results for a file
    */
   protected void InitAllCmplxKeywords(UCCFile cntrResult)
   {
      InitCmplxKeywords(DataKeyword, cntrResult.DataKeywordCnts);
      InitCmplxKeywords(ExecKeywords, cntrResult.ExecKeywordCnts);
      InitCmplxKeywords(MathKeywords, cntrResult.CmplxMathCnts);
      InitCmplxKeywords(TrigKeywords, cntrResult.CmplxTrigCnts);
      InitCmplxKeywords(LogKeywords, cntrResult.CmplxLogCnts);
      InitCmplxKeywords(CalcKeywords, cntrResult.CmplxCalcCnts);
      InitCmplxKeywords(CompilerDir, cntrResult.CmplxPreprocCnts);
      InitCmplxKeywords(CondKeywords, cntrResult.CmplxCondCnts);
      InitCmplxKeywords(LogicKeywords, cntrResult.CmplxLogicCnts);
      InitCmplxKeywords(AssignKeywords, cntrResult.CmplxAssignCnts);
      InitCmplxKeywords(PntrKeywords, cntrResult.CmplxPntrCnts);
      InitCmplxKeywords(BoolOperands, cntrResult.BoolOperandCnts);
   }

   /**
    * Computes Source Lines of Code metrics for given file. Metrics include:
    * Physical Source Line of Code (PSLOC) counts Logical Source Line of Code
    * (LSLOC) counts Complexity keyword counts Cyclomatic complexity counts
    * 
    * @param cntrResults
    *           A UCCFile object ArrayList to store results of code counters
    * @param i
    *           The index of the UCCFile we want to work on
    */
   public void CountSLOC(ArrayList<UCCFile> cntrResults, int i)
   {
      logger.debug("Counting PSLOC for " + cntrResults.get(i).FileName);
      // Count PSLOC and complexity
      CountFilePSLOC(cntrResults, i);
      logger.debug("Done\n");

      logger.debug("Counting LSLOC for " + cntrResults.get(i).FileName);
      // Count LSLOC and cyclomatic complexity
      CountFileLSLOC(cntrResults.get(i));
      logger.debug("Done\n");
   }

   /**
    * Function for finding the closing pattern on a given line.
    *
    * @param text
    *           String coming in from file.
    * @param openPos
    *           Open position of identifying opening character in text.
    * @param openPattern
    *           Character identifying opening pattern.
    * @param closePattern
    *           Character identifying closing pattern.
    * @return Returns closing position or -1 if no close is found.
    */
   protected int FindClose(String text, int openPos, char openPattern, char closePattern)
   {
      int occurrence = 1;
      int closePos = openPos;
      char c;
      while (occurrence > 0)
      {
         try
         {
            c = text.charAt(++closePos);
            if (c == openPattern)
            {
               occurrence++;
            }
            else if (c == closePattern)
            {
               occurrence--;
            }
         }
         catch (StringIndexOutOfBoundsException e)
         {
            closePos = -1;
            break;
         }
      }
      return closePos;
   }

   /**
    * Function for counting keywords on a line from a given list
    *
    * @param arr
    *           An array list containing keywords and counts
    * @param line
    *           Line coming in from file
    */
   protected void CountKeywords(ArrayList<CmplxDataType> arr, String line)
   {
      String regEx;
      Matcher matcher;

      if (!line.trim().isEmpty())
      {
         for (int kw = 0; kw < arr.size(); kw++)
         {
            regEx = "\\b" + arr.get(kw).Keyword + "\\b";
            if (regEx.contains("[")) regEx = regEx.replace("[", "\\[");
            matcher = Pattern.compile(regEx).matcher(line);
            StringBuffer sbLine = new StringBuffer();
            while (matcher.find() && !line.isEmpty() && line.length() >= arr.get(kw).Keyword.length())
            {
               matcher.appendReplacement(sbLine, "");
               arr.get(kw).Count++;
            }
            matcher.appendTail(sbLine);
            line = sbLine.toString().trim();
         }
      }
   }

   /**
    * Function for deleting unwanted strings (comments, line terminators) from
    * quote blocks
    * 
    * @param line
    *           Line coming in from file
    *
    * @return The modified line coming in
    */
   protected String DeleteUnwantedStringsFromQuotes(String line)
   {
      String front = "";
      String middle = "";
      String back = "";
      String quote = "";
      Pattern pattern;
      Matcher matcher;

      for (int qc = 0; qc < QuoteChar.size() && !line.isEmpty(); qc++)
      {
         pattern = Pattern.compile(QuoteChar.get(qc) + "([^" + QuoteChar.get(qc) + "]*)" + QuoteChar.get(qc));
         matcher = pattern.matcher(line);

         while (matcher.find())
         {
            quote = QuoteChar.get(qc) + matcher.group(1) + QuoteChar.get(qc);
            front = line.substring(0, line.indexOf(quote) + QuoteChar.get(qc).length());
            middle = matcher.group(1);
            back = line.substring(line.indexOf(quote) + quote.length() - QuoteChar.get(qc).length(), line.length());

            middle = middle.replaceAll("[^\\p{Alnum}\\s]", "").replaceAll("\\s+", "");

            line = front + middle + back;
         }
      }

      return line;
   }

   /**
    * Function for deleting unwanted strings (comments, line terminators) from
    * quote blocks
    * 
    * @param line
    *           Line coming in from file
    *
    * @return The modified line coming in
    */
   protected String DeleteUnwantedStringsFromBraces(String line)
   {
      String front = "";
      String middle = "";
      String back = "";
      Pattern pattern;
      Matcher matcher;

      pattern = Pattern.compile("\\$\\{([^}]*)\\}");
      matcher = pattern.matcher(line);

      while (matcher.find())
      {
         front = line.substring(0, line.indexOf(matcher.group(1)));
         middle = matcher.group(1);
         back = line.substring(line.indexOf(matcher.group(1)) + matcher.group(1).length(), line.length());

         // Delete all single line comments
         for (int slc = 0; slc < SingleLineComment.size() && !middle.isEmpty(); slc++)
         {
            middle = middle.replace(SingleLineComment.get(slc), "");
         }

         // Delete all multi line comment starts
         for (int mlcs = 0; mlcs < MultiLineCommentStart.size() && !middle.isEmpty(); mlcs++)
         {
            middle = middle.replace(MultiLineCommentStart.get(mlcs), "");
         }

         // Delete all multi line comment ends
         for (int mlce = 0; mlce < MultiLineCommentEnd.size() && !middle.isEmpty(); mlce++)
         {
            middle = middle.replace(MultiLineCommentEnd.get(mlce), "");
         }

         // Delete line terminators
         for (int lt = 0; lt < LineTerminator.size(); lt++)
         {
            middle = middle.replace(LineTerminator.get(lt), "");
         }

         // Delete exclude characters
         middle = DeleteExcludeCharacters(middle);

         line = front + middle + back;
      }

      return line;
   }

   /**
    * Function for removing white space around special characters, specifically
    * () and {}.
    *
    * @param line
    *           Line coming in from file
    * @return The modified line coming in
    */
   protected String DeleteSpacesAroundSpecialChars(String line)
   {
      // Replace all spaces around ( and )
      // line = line.replaceAll("\\s*\\(\\s*([^)]*?)\\s*\\)\\s*", "($1)");
      line = line.replaceAll("\\s*\\(\\s*([^)]*?)", "($1");
      line = line.replaceAll("([^)]*?)\\s*\\)\\s*", "$1)");

      // Replace all spaces around { and }
      line = line.replaceAll("\\s*\\{\\s*([^)]*?)\\s*\\}\\s*", " { $1 } ");

      return line;
   }

   /**
    * Function for replacing problematic characters with unique, unproblematic,
    * patterns.
    *
    * @param line
    *           Line coming in from file
    * @return The modified line coming in
    */
   protected String ReplaceSpecialChars(String line)
   {
      // Change instances of \\ (escaped \) to |||
      while (line.contains("\\\\"))
      {
         line = line.replace("\\\\", "|||");
      }

      return line;
   }

   /**
    * Function for deleting instances of empty double quotes.
    * 
    * @param line
    *           Line coming in from file
    *
    * @return The modified line coming in
    */
   protected String DeleteEmptyQuotes(String line)
   {
      for (int qc = 0; qc < QuoteChar.size() && !line.isEmpty(); qc++)
      {
         line = line.replace(QuoteChar.get(qc) + QuoteChar.get(qc), " ");
      }

      return line;
   }

   /**
    * Function for deleting escaped quotes.
    * 
    * @param line
    *           Line coming in from file
    *
    * @return The modified line coming in
    */
   protected String DeleteEscapedQuotes(String line)
   {
      for (int qc = 0; qc < QuoteChar.size() && !line.isEmpty(); qc++)
      {
          //If the quotes are matched, we don't need to delete them.
          int count = 0;
          Pattern p = Pattern.compile(QuoteChar.get(qc));
          Matcher m = p.matcher(line);
          while(m.find()) {
              count++;
          }
          if(count % 2 != 0) {
              line = line.replace("\\" + QuoteChar.get(qc), "");
          }
      }
      return line;
   }

   /**
    * Function for deleting instances of the UTF-8 BOM
    *
    * @param line
    *           Line coming in from file
    * @return The modified line coming in
    */
   protected String DeleteUTF8BOM(String line)
   {
      if (line != null && line.startsWith("\uFEFF"))
      {
         line = line.replace("\uFEFF", "");
      }

      return line;
   }

   /**
    * Function for deleting all curly brackets not in compiler directives.
    * 
    * @param line
    *           Line coming in from file
    *
    * @return The modified line coming in
    */
   protected String DeleteAllCurlyBracketsNotInCompilerDirectives(String line)
   {
      for (int cc = 0; cc < CompilerChar.size(); cc++)
      {
         if (!line.startsWith(CompilerChar.get(cc)))
         {
            line = line.replaceAll("\\{", "").replaceAll("\\}", "");
         }
      }

      return line;
   }

   /**
    * Function for deleting exclude keywords.
    * 
    * @param line
    *           Line coming in from file
    *
    * @return The modified line coming in
    */
   protected String DeleteExcludeKeywords(String line)
   {
      String regEx;

      for (int ek = 0; ek < ExcludeKeyword.size() && !line.trim().isEmpty(); ek++)
      {
         regEx = "\\b" + ExcludeKeyword.get(ek) + "\\b";
         line = line.replaceAll(regEx, " ");
      }

      return line;
   }

   /**
    * Function for deleting compiler directives.
    * 
    * @param cntrResult
    *           UCCFile object so the Compiler Directive Count can be
    *           incremented
    * @param line
    *           Line coming in from file
    *
    * @return The modified line coming in
    */
   protected String DeleteCompilerDirectives(UCCFile cntrResult, String line)
   {
      String regEx = "";
      Matcher matcher;

      if (!line.trim().isEmpty())
      {
         if (CompilerChar.size() > 0)
         {
            for (int cc = 0; cc < CompilerChar.size() && !line.trim().isEmpty(); cc++)
            {
               if (line.startsWith(CompilerChar.get(cc)))
               {
                  line = "";
                  cntrResult.NumCompilerDirectives++;
                  lslocLineValue++;
               }
            }
         }
         else
         {
            for (int cd = 0; cd < CompilerDir.size() && !line.trim().isEmpty(); cd++)
            {
               regEx = CompilerDir.get(cd);
               matcher = Pattern.compile(regEx).matcher(line.trim());

               // This ensures that we find whole word only and that it starts
               // at the beginning of the line
               if (matcher.find() && matcher.start() == 0)
               {
                  line = "";
                  cntrResult.NumCompilerDirectives++;
                  lslocLineValue++;
               }
            }
         }
      }

      return line;
   }

   /**
    * Function for deleting compiler directives.
    * 
    * @param cntrResult
    *           UCCFile object so the Compiler Directive Count can be
    *           incremented
    * @param line
    *           Line coming in from file
    *
    * @return The modified line coming in
    */
   protected String DeleteCompilerDirectivesFromPsloc(UCCFile cntrResult, String line)
   {
      String regEx = "";
      Matcher matcher;

      if (!line.trim().isEmpty())
      {
         if (CompilerChar.size() > 0)
         {
            for (int cc = 0; cc < CompilerChar.size() && !line.trim().isEmpty(); cc++)
            {
               if (line.startsWith(CompilerChar.get(cc)))
               {
                  line = line.replaceAll(CompilerChar.get(cc), "");
               }
            }
         }
         else
         {
            for (int cd = 0; cd < CompilerDir.size() && !line.trim().isEmpty(); cd++)
            {
               regEx = "\\b" + CompilerDir.get(cd) + "\\b";
               matcher = Pattern.compile(regEx).matcher(line.trim());

               // This ensures that we find whole word only and that it starts
               // at the beginning of the line
               if (matcher.find())
               {
                  line = line.replaceAll(CompilerChar.get(cd), "");
               }
            }
         }
      }

      return line;
   }

   /**
    * Function for deleting the contents of quotes.
    * 
    * @param line
    *           Line coming in from file
    *
    * @return The modified line coming in
    */
   protected String DeleteQuoteContents(String line)
   {
      for (int qc = 0; qc < QuoteChar.size() && !line.isEmpty(); qc++)
      {
         // Delete special cases of """..."""
         line = line.replaceAll(QuoteChar.get(qc) + QuoteChar.get(qc) + QuoteChar.get(qc) + ".*?" + QuoteChar.get(qc)
                  + QuoteChar.get(qc) + QuoteChar.get(qc), "");

         // Delete special cases of ""...""
         line = line.replaceAll(QuoteChar.get(qc) + QuoteChar.get(qc) + ".*?" + QuoteChar.get(qc) + QuoteChar.get(qc),
                  "");

         // Delete standard cases of "..."
         line = line.replaceAll(QuoteChar.get(qc) + ".*?" + QuoteChar.get(qc), QuoteChar.get(qc) + QuoteChar.get(qc));
      }

      return line;
   }

   /**
    * Function for deleting the contents of loops.
    * 
    * @param line
    *           Line coming in from file
    * @param keyword
    *           Loop Keyword to search
    *
    * @return The modified line coming in
    */
   protected String DeleteLoopContents(String line, String keyword)
   {
      String regEx;
      Pattern pattern;
      Matcher matcher;
      int openPos = 0;
      int closePos = 0;

      regEx = "\\b" + keyword + "\\b";
      pattern = Pattern.compile(regEx);
      matcher = pattern.matcher(line);
      if (matcher.find())
      {
         openPos = line.indexOf("(", matcher.start());
         if (openPos != -1)
         {
            closePos = FindClose(line, openPos, '(', ')');
            if (closePos != -1)
            {
               line = line.substring(0, openPos + "(".length()) + line.substring(closePos, line.length()).trim();
            }
         }
      }

      return line;
   }

   /**
    * Function for deleting case statements.
    *
    * @param line
    *           Line coming in from file
    * @return The modified line coming in
    */
   protected String DeleteCaseStatements(String line)
   {
      String regEx;
      Pattern pattern;
      Matcher matcher;

      if (line.equals("case:"))
      {
         line = line.replaceAll("case:", "").trim();
      }
      else
      {
         regEx = "\\b" + "case" + "\\b";
         pattern = Pattern.compile(regEx);
         matcher = pattern.matcher(line);
         if (matcher.find())
         {
            int caseIndex = matcher.start();
            int colonIndex = line.indexOf(":", caseIndex);
            if (caseIndex != -1 && colonIndex != -1 && (caseIndex < colonIndex))
            {
               line = line.substring(0, caseIndex) + line.substring(colonIndex + 1, line.length());
               line = line.trim();
            }
         }
      }

      return line;
   }

   /**
    * Function for deleting lines which end in colons. Primarily used by C-type
    * languages.
    *
    * @param line
    *           Line coming in from file
    * @return The modified line coming in
    */
   protected String DeleteLinesEndingWithAColon(String line)
   {
      if (line.trim().endsWith(":"))
      {
         line = "";
      }

      return line;
   }

   /**
    * Function for deleting exclude characters.
    * 
    * @param line
    *           Line coming in from file
    *
    * @return The modified line coming in
    */
   protected String DeleteExcludeCharacters(String line)
   {
      for (int ec = 0; ec < ExcludeCharacter.size() && !line.isEmpty(); ec++)
      {
         line = line.replaceAll(ExcludeCharacter.get(ec), " ");
      }

      return line;
   }

   /**
    * Function for counting data declarations.
    *
    * @param cntrResult
    *           A UCCFile object to store results of code counter
    * @param line
    *           Line coming in from file
    * @return The modified line coming in
    */
   protected String CountDataDeclarations(UCCFile cntrResult, String line)
   {
      String regEx;
      Pattern pattern;
      Matcher matcher;

      for (int dk = 0; dk < DataKeyword.size(); dk++)
      {
         regEx = "\\b" + DataKeyword.get(dk) + "\\b";
         pattern = Pattern.compile(regEx);
         matcher = pattern.matcher(line.trim());
         if (matcher.find() && matcher.start() == 0)
         {
            line = "";
            cntrResult.NumDataDeclLog++;
            lslocLineValue++;
         }
      }

      return line;
   }

   /**
    * Function for deleting empty parentheses of the form ().
    *
    * @param line
    *           Line coming in from file
    * @return The modified line coming in
    */
   protected String DeleteEmptyParentheses(String line)
   {
      line = line.replaceAll("\\(\\)", "");

      return line;
   }

   /**
    * Function for deleting problematic paths (directories and website URLs)
    *
    * @param line
    *           Line coming in from file
    * @return The modified line coming in
    */
   public static String DeletePathPatterns(String line)
   {
      // Delete instances of directory starts (c:/ or h:\) and website urls
      // (http(s)://)
      line = line.replaceAll("([a-zA-Z]:)?(\\\\[a-zA-Z0-9_.-]+)+\\\\?", "").replaceAll("(?im)[a-z]:(\\\\|/)|https?://",
               "");

      return line;
   }

   /**
    * Function for deleting words containing apostraphes
    * 
    * @param line
    *           String coming in from file
    * @return Input string with apostraphes removed
    */
   protected String DeleteApostraphesFromWords(String line)
   {
      line = line.replaceAll("(\\w+)s?'(?:s\\b)?", "$1");

      return line;
   }

   /**
    * Counts Physical SLOC. Current algorithm:
    * 
    * Read first line
    * 
    * Delete the UTF-8 BOM, if found
    * 
    * While we have lines in the file... { - Do some pre-processing - Check for
    * unclosed quotes - Count comments with handler (also counts blank lines) -
    * Count compiler directives with handler - Count non-blank lines - Do some
    * post-processing - Calculate PSLOC as non-blank lines + compiler directives
    * + number of continued compiler directives (these were erased in the
    * compiler handler) - Save post-processed line in new file for LSLOC
    * counting - Read next line }
    * 
    * @param cntrResults
    *           A UCCFile object ArrayList to store results of code counters
    * @param i
    *           The index of the UCCFile we want to work on
    */
   protected void CountFilePSLOC(ArrayList<UCCFile> cntrResults, int i)
   {
      UCCFile cntrResult = cntrResults.get(i);

      LineNumberReader reader = null;

      int psloc = 0; // Physical SLOC

      // Pattern finder variables
      int openPos = 0;
      int closePos = 0;

      String tempLine = "";
      boolean multiLineFlag = false;
      String multiLine = "";
      String regEx;
      Matcher matcher;

      boolean undoNewlines = false;

      // Buffered writer for _PSLOC file saving
      BufferedWriter bw = null;

      boolean isCompilerDirective = false;
      boolean cdNewLine = false;

      try
      {
         // Create file for PSLOC storage
         File file = new File(FileUtils.BuildTempOutFileName_PSLOC(RtParams, cntrResult));
         bw = new BufferedWriter(new FileWriter(file));

         reader = new LineNumberReader(
                  new InputStreamReader(new FileInputStream(cntrResult.FileName), Constants.CHARSET_NAME));

         // Read first line
         String line = reader.readLine();

         // Delete UTF-8 BOM instance
         line = DeleteUTF8BOM(line);

         // Reset truncated lines count
         truncateLinesCount = 0;

         // While we have lines in the file...
         while (line != null)
         {
            // Ignore line data beyond the single line threshold value
            if (RtParams.TruncThreshold != 0 && line.length() > RtParams.TruncThreshold)
            {
               line = line.substring(0, RtParams.TruncThreshold);
               truncateLinesCount++;
            }

            logger.debug("line " + reader.getLineNumber() + " in:  " + line);

            /* PREPROCESSING START */

            line = line.trim();

            if (!line.isEmpty())
            {
               // If the language is not case sensitive, lower-case line coming
               // in
               if (!LangProps.IsCaseSensitive())
               {
                  line = line.toLowerCase();
               }

               // Delete path patterns
               line = DeletePathPatterns(line);

               // Delete escaped quotes
               line = DeleteEscapedQuotes(line);

               // Delete double quotes
               line = DeleteEmptyQuotes(line);

               // Delete all styles of comments inside quotes
               line = DeleteUnwantedStringsFromQuotes(line);
            }

            /* PREPROCESSING FINISH */

            // Count Comments
            line = CommentHandler.HandleComments(line, CompilerDir);

            line = line.trim();

            // Count PSLOC
            if (!line.isEmpty())
            {
               logger.debug("line " + reader.getLineNumber() + " out: " + line);
               psloc++;
            }

            /* POSTPROCESSING START */

            if (!line.isEmpty())
            {
               // Undo multi-line logical loops
               if (multiLineFlag == true)
               {
                  line = multiLine + " " + line;
               }

               // Assume we're not going to undo any of the changes we will make
               undoNewlines = false;

               // If the line ends in a line terminator, we're not going to undo
               // all newlines we're about do put in.
               // This will undo changing things like "char k[5] = {'a', 'a',
               // 'a', 'f','c'};" to a multiple line
               // statement
               for (int lt = 0; lt < LineTerminator.size(); lt++)
               {
                  if (line.endsWith(LineTerminator.get(lt)))
                  {
                     undoNewlines = true;
                  }
               }

               for (int lk = 0; lk < LslocKeywords.size() && !line.isEmpty(); lk++)
               {
                  closePos = 0;
                  regEx = "\\b" + LslocKeywords.get(lk) + "\\b";
                  matcher = Pattern.compile(regEx).matcher(line);
                  while (closePos >= 0 && matcher.find(closePos))
                  {
                     openPos = line.indexOf("(", matcher.end());
                     closePos = FindClose(line, openPos, '(', ')');
                     if (closePos != -1)
                     {
                        multiLineFlag = false;
                        // Replace all line terminators within loop ()'s with
                        // @'s
                        if (LslocKeywords.get(lk).equals("for"))
                        {
                           for (int lt = 0; lt < LineTerminator.size(); lt++)
                           {
                              tempLine = line.substring(openPos + "(".length(), closePos).trim()
                                       .replaceAll(LineTerminator.get(lt), "@");
                           }
                           line = line.substring(0, openPos + "(".length()) + tempLine
                                    + line.substring(closePos, line.length()).trim();
                        }
                        multiLine = "";
                     }
                     else
                     {
                        multiLineFlag = true;
                        multiLine = line;
                        line = "";
                     }
                  }
               }

               // Add a newline after all line terminators
               for (int lt = 0; lt < LineTerminator.size(); lt++)
               {
                  line = line.replaceAll(LineTerminator.get(lt), LineTerminator.get(lt) + "\n");
               }

               // Add a newline after all open curly braces
               line = line.replaceAll("\\{", "\\{\n");

               // Add a newline before and after all close curly braces
               line = line.replaceAll("\\}", "\n\\}\n");

               // Special cases of "protected:", etc.
               if (line.endsWith(":"))
               {
                  line += "\n";
               }

               // Replace all double newlines with single newlines
               while (line.contains("\n\n"))
               {
                  line = line.replaceAll("\n\n", "\n");
               }

               // Delete all newlines at the beginning of a line
               if (line.startsWith("\n"))
               {
                  line = line.replaceFirst("\n", "");
               }

               // Put all LSLOC keywords to start on their own line
               for (int lk = 0; lk < LslocKeywords.size() && !line.isEmpty(); lk++)
               {
                  regEx = "\\b" + LslocKeywords.get(lk) + "\\b";
                  matcher = Pattern.compile(regEx).matcher(line);
                  isCompilerDirective = false;

                  if (matcher.find())
                  {
                     // Check to make sure the LSLOC keyword is also not part of
                     // a compiler directive
                     if (CompilerDir != null && CompilerDir.size() > 0)
                     {
                        for (int cd = 0; cd < CompilerDir.size(); cd++)
                        {
                           if (CompilerDir.get(cd).contains(LslocKeywords.get(lk)))
                           {
                              int lkp = CompilerDir.get(cd).indexOf(LslocKeywords.get(lk));
                              int lkl = line.indexOf(LslocKeywords.get(lk));

                              if (line.indexOf(CompilerDir.get(cd), (lkl - lkp)) > -1)
                              {
                                 isCompilerDirective = true;
                                 break;
                              }
                           }
                        }
                     }

                     if (!isCompilerDirective)
                     {
                        line = line.replaceAll(regEx, "\n" + LslocKeywords.get(lk));
                     }
                  }
               }

               // Special case for "do"
               regEx = "\\b" + "do" + "\\b";
               matcher = Pattern.compile(regEx).matcher(line);
               if (matcher.find())
               {
                  line = line.replaceAll(regEx, "\n" + "do ");
               }

               // Delete all lines that are just a newline
               if (line.trim().equals("\n"))
               {
                  line = "";
               }

               // Undo our newlines we put in
               if (undoNewlines)
               {
                  line = line.replaceAll("\n", "");

                  // Add a newline after all line terminators
                  for (int lt = 0; lt < LineTerminator.size(); lt++)
                  {
                     line = line.replaceAll(LineTerminator.get(lt), LineTerminator.get(lt) + "\n");
                  }
               }

               // Add a new line after all compiler directives
               cdNewLine = false;
               for (int cd = 0; cd < CompilerDir.size(); cd++)
               {
                  if (line.contains(CompilerDir.get(cd)))
                  {
                     line += "\n";
                     cdNewLine = true;
                  }
               }
               if (!cdNewLine)
               {
                  for (int cc = 0; cc < CompilerChar.size(); cc++)
                  {
                     if (line.contains(CompilerChar.get(cc)))
                     {
                        line += "\n";
                     }
                  }
               }

               // Replace leading whitespace for new lines
               while (line.contains("\n "))
               {
                  line = line.replaceAll("\n ", "\n");
               }
            }

            if (!line.trim().isEmpty())
            {
               // line += " ";
               bw.write(line);
            }

            /* POSTPROCESSING FINISH */

            // Read next line
            line = reader.readLine();
         }
      }
      catch (IOException e)
      {
         logger.error("The input reader failed to open.");
         logger.error(e);
      }
      finally
      {
         if (reader != null)
         {
            // Save PSLOC metrics counted
            cntrResult.IsCounted = true;
            cntrResult.NumTotalLines = reader.getLineNumber();
            cntrResult.NumPSLOC = psloc;
            cntrResult.NumBlankLines = CommentHandler.GetBlankLineTally();
            cntrResult.LangVersion = LangProps.GetLangVersion();
            cntrResult.NumEmbeddedComments = CommentHandler.GetEmbeddedCommentTally();
            cntrResult.NumWholeComments = CommentHandler.GetWholeCommentTally();

            // Close the original file
            try
            {
               reader.close();
            }
            catch (IOException e)
            {
               logger.error("The input reader failed to close.");
               logger.error(e);
            }
            reader = null;
         }

         if (bw != null)
         {
            try
            {
               bw.close();
            }
            catch (IOException e)
            {
               logger.error("The PSLOC writer failed to close.");
               logger.error(e);
            }
            bw = null;
         }

         // Log the number of lines in the file that were truncated
         if (truncateLinesCount > 0)
         {
            logger.warn("Truncated " + truncateLinesCount + " total lines in file " + cntrResult.FileName
                     + "  [-trunc set to " + Integer.toString(RtParams.TruncThreshold) + "]");
         }

         // Reset handlers
         CommentHandler.Reset();
      }
   }

   /**
    * Counts Logical SLOC. Current algorithm:
    * 
    * Read first line
    * 
    * Delete the UTF-8 BOM, if found
    * 
    * While we have lines in the file... { - Build true LSLOC and save to LSLOC
    * file - Search for duplicates based on threshold - Delete compiler
    * directives - Delete everything between quotes - Count data keywords -
    * Count executable keywords - Count complexity keywords - Count data
    * declarations (different cases based on whether language has a line
    * terminator) - Delete exclude keywords and characters
    * 
    * If the line is not empty, increment the LSLOC counter to catch leftover
    * LSLOC
    * 
    * Read next line }
    * 
    * @param cntrResult
    *           A UCCFile object to store results of code counter
    */
   protected void CountFileLSLOC(UCCFile cntrResult)
   {
      LineNumberReader reader = null;

      int lsloc = 0;
      int lslocKeywordsCount = 0;
      lslocLineValue = 0;

      String regEx;
      Pattern pattern;
      Matcher matcher;
      String tempLine = "";
      int lineIndex = 0;
      File file = null;

      // Initialize complexity keywords/counts for this file
      InitAllCmplxKeywords(cntrResult);

      // Buffered writer for _LSLOC file saving
      BufferedWriter bw = null;

      try
      {
         // If we're differencing baselines...
         if (RtParams.DiffCode)
         {
            // Create file for LSLOC storage
            file = new File(FileUtils.BuildTempOutFileName_LSLOC(RtParams, cntrResult));
            bw = new BufferedWriter(new FileWriter(file));
         }

         reader = new LineNumberReader(
                  new InputStreamReader(new FileInputStream(FileUtils.BuildTempOutFileName_PSLOC(RtParams, cntrResult)),
                           Constants.CHARSET_NAME));

         // Read first line
         String line = reader.readLine();

         // Delete UTF-8 BOM instance
         line = DeleteUTF8BOM(line);

         // While we have lines in the file...
         while (line != null)
         {
            // Reset conditional
            lslocLineValue = 0;

            // If we're baseline differencing or searching for duplicates...
            if (RtParams.DiffCode || RtParams.SearchForDups)
            {
               // Save line into a temporary line for LSLOC writing
               tempLine = line;

               // Delete all exclude keywords
               tempLine = DeleteExcludeKeywords(tempLine);

               // Delete all exclude characters
               tempLine = DeleteExcludeCharacters(tempLine);

               // Delete all line terminators (e.g. for instances where the line
               // ends up being ;)
               for (int lt = 0; lt < LineTerminator.size(); lt++)
               {
                  tempLine = tempLine.replaceAll(LineTerminator.get(lt), "");
               }
            }

            // Delete everything between quotes
            line = DeleteQuoteContents(line);

            // Count executable keyword occurrences
            CountKeywords(cntrResult.ExecKeywordCnts, line);

            // Count data keyword occurrences
            CountKeywords(cntrResult.DataKeywordCnts, line);

            line = line.trim();

            // Count complexity keywords
            if (RtParams.CountCmplxMetrics && !line.isEmpty())
            {
               CountComplexity(cntrResult, line);
            }

            // Delete all compiler directive lines left over from PSLOC
            line = DeleteCompilerDirectives(cntrResult, line);

            /* LOGICAL SLOC BLOCK START */

            // Count data declarations
            if (LineTerminator.size() > 0) // For languages with line
                                           // terminators
            {
               for (int lt = 0; lt < LineTerminator.size() && !line.isEmpty(); lt++)
               {
                  // Count data declarations with line terminators
                  if (line.endsWith(LineTerminator.get(lt)))
                  {
                     line = CountDataDeclarations(cntrResult, line);
                  }
               }
            }
            else // For languages without line terminators
            {
               line = CountDataDeclarations(cntrResult, line);
            }

            // Count LSLOC keywords
            for (int lk = 0; lk < LslocKeywords.size() && !line.isEmpty(); lk++)
            {
               if (line.equals(LslocKeywords.get(lk)))
               {
                  lslocKeywordsCount++;
                  line = "";
                  lslocLineValue++;
               }
               else
               {
                  regEx = "\\b" + LslocKeywords.get(lk) + "\\b";
                  pattern = Pattern.compile(regEx);
                  matcher = pattern.matcher(line);

                  // Count multiple LSLOC keywords on one line
                  if (matcher.find() && !line.isEmpty() && line.length() >= LslocKeywords.get(lk).length())
                  {
                     lslocKeywordsCount++;
                     line = "";
                     lslocLineValue++;
                  }
               }
            }

            if (LineTerminator.size() > 0) // For languages with line
                                           // terminators
            {
               for (int lt = 0; lt < LineTerminator.size() && !line.isEmpty(); lt++)
               {
                  // Count data declarations with line terminators
                  if (line.endsWith(LineTerminator.get(lt)))
                  {
                     lsloc++;
                     line = "";
                     lslocLineValue++;
                  }
               }
            }

            // Delete exclude keywords
            line = DeleteExcludeKeywords(line);

            // Delete exclude characters
            line = DeleteExcludeCharacters(line);

            // Delete "case ... :" statements
            line = DeleteCaseStatements(line);

            line = DeleteLinesEndingWithAColon(line);

            /* LOGICAL SLOC BLOCK FINISH */

            // Delete empty ()'s
            line = DeleteEmptyParentheses(line);

            if (!line.trim().isEmpty())
            {
               lsloc++;
               lslocLineValue++;
            }

            // Write the Raw LSLOC Line for Differencer
            if (lslocLineValue > 0)
            {
               // If we're baseline differencing, write the LSLOC line
               if (RtParams.DiffCode)
               {
                  bw.write(lslocLineValueDelim + Integer.toString(lslocLineValue) + lslocLineValueDelim + tempLine
                           + "\n");
               }

               // If we're searching for duplicates, checksum the LSLOC line
               if (RtParams.SearchForDups && !cntrResult.UniqueFileName)
               {
                  cntrResult.FileLineChecksum.add(lineIndex, tempLine.hashCode());
                  lineIndex++;
               }
            }

            // Read next line
            line = reader.readLine();
         }
      }
      catch (IOException e)
      {
         logger.error("The PSLOC reader failed to open.");
         logger.error(e);
      }
      finally
      {
         // If the _PSLOC file was opened...
         if (reader != null)
         {
            // Save LSLOC metrics counted
            cntrResult.NumLSLOC =
                     cntrResult.NumCompilerDirectives + cntrResult.NumDataDeclLog + lslocKeywordsCount + lsloc;
            cntrResult.NumExecInstrLog =
                     cntrResult.NumLSLOC - cntrResult.NumDataDeclLog - cntrResult.NumCompilerDirectives;
            cntrResult.NumDataDeclPhys = cntrResult.NumDataDeclLog;
            cntrResult.NumExecInstrPhys = cntrResult.NumExecInstrLog;
            cntrResult.LangVersion = LangProps.GetLangVersion();

            // Close the _PSLOC file
            try
            {
               reader.close();
            }
            catch (IOException e)
            {
               logger.error("The PSLOC reader failed to close.");
               logger.error(e);
            }
            reader = null;

            // Delete PSLOC file
            FileUtils.DeleteFile(FileUtils.BuildTempOutFileName_PSLOC(RtParams, cntrResult));
         }

         // If the _LSLOC file was opened...
         if (bw != null)
         {
            try
            {
               bw.close();
            }
            catch (IOException e)
            {
               logger.error("The LSLOC writer failed to close.");
               logger.error(e);
            }
            bw = null;
         }
      }
   }

   /**
    * Counts pre-processor complexity metrics of a line
    *
    * @param preProcCnts
    *           Contains pre-processor (compiler directive) keywords and counts
    * @param line
    *           Source line of code to perform complexity count on
    * @return Modified input line with complexity keywords removed
    */
   protected String CountCmplxPreProc(ArrayList<CmplxDataType> preProcCnts, String line)
   {
      // Count compiler directive keyword occurrences, only one per line
      for (int cd = 0; !line.isEmpty() && cd < preProcCnts.size(); cd++)
      {
         if (line.startsWith(preProcCnts.get(cd).Keyword))
         {
            preProcCnts.get(cd).Count++;

            // Don't count any other keywords on the compiler directive line
            line = "";
            break;
         }
      }

      return line;
   }

   /**
    * Counts data declaration complexity metrics of a line
    *
    * @param dataCnts
    *           Contains data keywords and counts
    * @param line
    *           Source line of code to perform complexity count on
    * @return Modified input line with complexity keywords removed
    */
   protected String CountCmplxDataDecl(ArrayList<CmplxDataType> dataCnts, String line)
   {
      Matcher matcher;
      String regEx;

      // Pre-process data declaration lines
      for (int i = 0; !line.isEmpty() && i < dataCnts.size(); i++)
      {
         // Pre-process any data declaration lines with template form <> and
         // with pointer declaration to not count
         // them as logical or multiplication operations, respectively
         regEx = "<\\s*" + dataCnts.get(i).Keyword + "\\s*>|\\b" + dataCnts.get(i).Keyword + "\\s*\\*";
         matcher = Pattern.compile(regEx).matcher(line);
         StringBuffer sbLine = new StringBuffer();
         while (matcher.find())
         {
            matcher.appendReplacement(sbLine, " ");
         }
         matcher.appendTail(sbLine);
         line = sbLine.toString();
      }

      return line;
   }

   /**
    * Counts pointer complexity metrics of a line
    *
    * @param pntrCnts
    *           Contains pointer keywords and counts
    * @param line
    *           Source line of code to perform complexity count on
    * @return Modified input line with complexity keywords removed
    */
   protected String CountCmplxPntr(ArrayList<CmplxDataType> pntrCnts, String line)
   {
      Matcher matcher;
      String regEx;

      for (int i = 0; !line.isEmpty() && i < pntrCnts.size(); i++)
      {
         // This tells the regEx engine that the string between \Q and \E must
         // be interpreted verbatim,
         // ignoring any metacharacters that it may contain
         regEx = "\\Q" + pntrCnts.get(i).Keyword + "\\E";
         matcher = Pattern.compile(regEx).matcher(line);
         StringBuffer sbLine = new StringBuffer();
         while (matcher.find())
         {
            matcher.appendReplacement(sbLine, " ");
            pntrCnts.get(i).Count++;
         }
         matcher.appendTail(sbLine);
         line = sbLine.toString();
      }

      return line;
   }

   /**
    * Counts trigonometric complexity metrics of a line
    *
    * @param trigCnts
    *           Contains trigonometric keywords and counts
    * @param line
    *           Source line of code to perform complexity count on
    * @return Modified input line with complexity keywords removed
    */
   protected String CountCmplxTrig(ArrayList<CmplxDataType> trigCnts, String line)
   {
      Matcher matcher;
      String regEx;

      for (int i = 0; !line.isEmpty() && i < trigCnts.size(); i++)
      {
         // Whole word only regular expression
         regEx = "\\b" + trigCnts.get(i).Keyword + "\\b";
         matcher = Pattern.compile(regEx).matcher(line);
         StringBuffer sbLine = new StringBuffer();
         while (matcher.find() && !line.isEmpty() && line.length() >= trigCnts.get(i).Keyword.length())
         {
            matcher.appendReplacement(sbLine, " ");
            trigCnts.get(i).Count++;
         }
         matcher.appendTail(sbLine);
         line = sbLine.toString();
      }

      return line;
   }

   /**
    * Counts logarithmic complexity metrics of a line
    *
    * @param logCnts
    *           Contains logarithmic keywords and counts
    * @param line
    *           Source line of code to perform complexity count on
    * @return Modified input line with complexity keywords removed
    */
   protected String CountCmplxLog(ArrayList<CmplxDataType> logCnts, String line)
   {
      Matcher matcher;
      String regEx;

      for (int i = 0; !line.isEmpty() && i < logCnts.size(); i++)
      {
         // Whole word only regular expression
         regEx = "\\b" + logCnts.get(i).Keyword + "\\b";
         matcher = Pattern.compile(regEx).matcher(line);
         StringBuffer sbLine = new StringBuffer();
         while (matcher.find() && !line.isEmpty() && line.length() >= logCnts.get(i).Keyword.length())
         {
            matcher.appendReplacement(sbLine, " ");
            logCnts.get(i).Count++;
         }
         matcher.appendTail(sbLine);
         line = sbLine.toString();
      }

      return line;
   }

   /**
    * Counts math operations complexity metrics of a line
    *
    * @param mathCnts
    *           Contains math keywords and counts
    * @param line
    *           Source line of code to perform complexity count on
    * @return Modified input line with complexity keywords removed
    */
   protected String CountCmplxMath(ArrayList<CmplxDataType> mathCnts, String line)
   {
      Matcher matcher;
      String regEx;

      for (int i = 0; !line.isEmpty() && i < mathCnts.size(); i++)
      {
         // Whole word only regular expression
         regEx = "\\b" + mathCnts.get(i).Keyword + "\\b";
         matcher = Pattern.compile(regEx).matcher(line);
         StringBuffer sbLine = new StringBuffer();
         while (matcher.find() && !line.isEmpty() && line.length() >= mathCnts.get(i).Keyword.length())
         {
            matcher.appendReplacement(sbLine, " ");
            mathCnts.get(i).Count++;
         }
         matcher.appendTail(sbLine);
         line = sbLine.toString();
      }

      return line;
   }

   /**
    * Counts conditional complexity metrics of a line
    *
    * @param condCnts
    *           Contains conditional keywords and counts
    * @param line
    *           Source line of code to perform complexity count on
    * @return Modified input line with complexity keywords removed
    */
   protected String CountCmplxCond(ArrayList<CmplxDataType> condCnts, String line)
   {
      Matcher matcher;
      String regEx;

      for (int i = 0; !line.isEmpty() && i < condCnts.size(); i++)
      {
         // Whole word only regular expression
         regEx = "\\b" + condCnts.get(i).Keyword + "\\b";
         matcher = Pattern.compile(regEx).matcher(line);
         StringBuffer sbLine = new StringBuffer();
         while (matcher.find() && !line.isEmpty() && line.length() >= condCnts.get(i).Keyword.length())
         {
            matcher.appendReplacement(sbLine, " ");
            condCnts.get(i).Count++;
         }
         matcher.appendTail(sbLine);
         line = sbLine.toString();
      }

      return line;
   }

   /**
    * Counts logical complexity metrics of a line
    *
    * @param logicCnts
    *           Contains logical keywords and counts
    * @param line
    *           Source line of code to perform complexity count on
    * @return Modified input line with complexity keywords removed
    */
   protected String CountCmplxLogic(ArrayList<CmplxDataType> logicCnts, String line)
   {
      boolean isWord = false;
      String logicPattern = "";
      String regEx;
      Matcher matcher;

      // Loop through all logical keywords
      for (int i = 0; !line.isEmpty() && i < logicCnts.size(); i++)
      {
         logicPattern = logicCnts.get(i).Keyword; // Save the keyword in a
                                                  // temporary string
         isWord = false; // Assume the keyword is a symbol by default

         // Check if the keyword starts with a letter, if so we're going to
         // assume it's a word
         if (logicPattern.toLowerCase().matches("^[a-z].*$"))
         {
            isWord = true;
         }

         // If the logical operation is a word, we use a regular expression to
         // find the whole word only
         if (isWord == true)
         {
            // Whole word only regular expression
            regEx = "\\b" + logicPattern + "\\b";
            matcher = Pattern.compile(regEx).matcher(line);
            StringBuffer sbLine = new StringBuffer();
            while (matcher.find() && !line.isEmpty() && line.length() >= logicPattern.length())
            {
               matcher.appendReplacement(sbLine, " ");
               logicCnts.get(i).Count++;
            }
            matcher.appendTail(sbLine);
            line = sbLine.toString();
         }
         else // If the logical operation is symbolic, a special regular
              // expression
         {
            // This tells the regEx engine that the string between \Q and \E
            // must be interpreted verbatim,
            // ignoring any metacharacters that it may contain
            regEx = "\\Q" + logicPattern + "\\E";
            matcher = Pattern.compile(regEx).matcher(line);
            StringBuffer sbLine = new StringBuffer();
            while (matcher.find() && !line.isEmpty() && line.length() >= logicPattern.length())
            {
               matcher.appendReplacement(sbLine, " ");
               logicCnts.get(i).Count++;
            }
            matcher.appendTail(sbLine);
            line = sbLine.toString();
         }
      }

      return line;
   }

   /**
    * Counts calculation operation complexity metrics of a line
    *
    * @param calcCnts
    *           Contains calculation keywords and counts
    * @param line
    *           Source line of code to perform complexity count on
    * @return Modified input line with complexity keywords removed
    */
   protected String CountCmplxCalc(ArrayList<CmplxDataType> calcCnts, String line)
   {
      Matcher matcher;
      String regEx = "";

      for (int i = 0; !line.isEmpty() && i < calcCnts.size(); i++)
      {
         // This tells the regEx engine that the string between \Q and \E must
         // be interpreted verbatim,
         // ignoring any metacharacters that it may contain
         regEx = "\\Q" + calcCnts.get(i).Keyword + "\\E";
         matcher = Pattern.compile(regEx).matcher(line);
         StringBuffer sbLine = new StringBuffer();
         while (matcher.find() && !line.isEmpty() && line.length() >= calcCnts.get(i).Keyword.length())
         {
            matcher.appendReplacement(sbLine, " ");
            calcCnts.get(i).Count++;
         }
         matcher.appendTail(sbLine);
         line = sbLine.toString();
      }

      return line;
   }

   /**
    * Counts assignment operation complexity metrics of a line
    *
    * @param assignCnts
    *           Contains assignment keywords and counts
    * @param line
    *           Source line of code to perform complexity count on
    * @return Modified input line with complexity keywords removed
    */
   protected String CountCmplxAssign(ArrayList<CmplxDataType> assignCnts, String line)
   {
      Matcher matcher;
      String regEx = "";

      for (int i = 0; !line.isEmpty() && i < assignCnts.size(); i++)
      {
         // This tells the regEx engine that the string between \Q and \E must
         // be interpreted verbatim,
         // ignoring any metacharacters that it may contain
         regEx = "\\Q" + assignCnts.get(i).Keyword + "\\E";
         matcher = Pattern.compile(regEx).matcher(line);
         StringBuffer sbLine = new StringBuffer();
         while (matcher.find() && !line.isEmpty() && line.length() >= assignCnts.get(i).Keyword.length())
         {
            matcher.appendReplacement(sbLine, " ");
            assignCnts.get(i).Count++;
         }
         matcher.appendTail(sbLine);
         line = sbLine.toString();
      }

      return line;
   }

   /**
    * Counts complexity metrics of a line
    *
    * @param cntrResult
    *           Contains the list of results found in UCCFile
    * @param line
    *           Source line of code to perform complexity count on
    */
   protected void CountComplexity(UCCFile cntrResult, String line)
   {
      // Count compiler directive keyword occurrences, only one per line
      line = CountCmplxPreProc(cntrResult.CmplxPreprocCnts, line);

      // Pre-process data declaration lines
      line = CountCmplxDataDecl(cntrResult.DataKeywordCnts, line);

      // Count number of loops - Done as part of LSLOC counting

      // Count pointer operations
      line = CountCmplxPntr(cntrResult.CmplxPntrCnts, line);

      // Count number of trigonometry operations
      line = CountCmplxTrig(cntrResult.CmplxTrigCnts, line);

      // Count number of logarithmic operations
      line = CountCmplxLog(cntrResult.CmplxLogCnts, line);

      // Count number of other math operations
      line = CountCmplxMath(cntrResult.CmplxMathCnts, line);

      // Count conditional statements
      line = CountCmplxCond(cntrResult.CmplxCondCnts, line);

      // Count logical operations
      line = CountCmplxLogic(cntrResult.CmplxLogicCnts, line);

      // Count calculation operations
      line = CountCmplxCalc(cntrResult.CmplxCalcCnts, line);

      // Count assignment operations
      line = CountCmplxAssign(cntrResult.CmplxAssignCnts, line);
   }
}
