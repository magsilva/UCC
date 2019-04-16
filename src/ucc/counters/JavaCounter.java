package ucc.counters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ucc.counters.handlers.QuoteHandler;
import ucc.datatypes.CmplxDataType;
import ucc.datatypes.Constants;
import ucc.datatypes.UCCFile;
import ucc.langprops.LanguageProperties;
import ucc.main.ReleaseInfo;
import ucc.utils.FileUtils;

/**
 * JavaCounter class performs various code counting operations on baseline(s)
 * identified by the user. It contains algorithms/methods to count Java
 * programming language files.
 *
 * @author Integrity Applications Incorporated
 *
 */
public class JavaCounter extends CodeCounter
{
   /** Instantiate the Log4j2 logger for this class */
   private static final Logger logger = LogManager.getLogger(JavaCounter.class);

   /** LSLOC keywords count */
   private int lslocKeywordsCount;

   /** Stores current level */
   private int level;

   /**
    * An Arraylist to hold all operator count for each file
    */
   protected ArrayList<ArrayList<CmplxDataType>> UniqueOperatorList;

   /**
    * An Arraylist to hold all operand count for each file
    */
   protected ArrayList<TreeMap> UniqueOperandList;

   /**
    * Default constructor to instantiate a JavaCounter object
    * 
    * @param langProps
    *           Language properties for this counter
    */
   public JavaCounter(LanguageProperties langProps)
   {
      // Call super class's constructor
      super(langProps);

      // Set unique data keywords specific to this language
      SetUniqueDataKeywords();

      // Create the quote handler
      QuoteHandler = new QuoteHandler(LangProps);
   }

   public void CountSLOC(ArrayList<UCCFile> cntrResults, int i)
   {

      // CountHalstead(cntrResults.get(i));

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
    */
   protected void CountFilePSLOC(ArrayList<UCCFile> cntrResults, int i)
   {
      UCCFile cntrResult = cntrResults.get(i);

      LineNumberReader reader = null;

      int psloc = 0; // Physical SLOC counter

      // Pattern finder variables
      int openPos = 0;
      int closePos = 0;

      String tempLine = "";
      boolean multiLineFlag = false;
      String multiLine = "";

      String regEx;
      Matcher matcher;

      boolean undoNewlines = false;
      boolean skipLine = false;

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

               // count char and string operands if 3rd argument is true
               CountCharAndStringOperands(cntrResult.charMap, line, true);
               // line = DeleteQuoteContents(line,cntrResult.charMap);

               // Delete apostraphes from words
               line = DeleteApostraphesFromWords(line);

               // Handle string literals
               line = QuoteHandler.HandleQuotes(line);

               // If the line has become empty from handling quotes, skip this
               // line for comment handling
               if (line.isEmpty())
               {
                  skipLine = true;
                  psloc++;
               }
               else
               {
                  skipLine = false;
               }
            }

            /* PREPROCESSING FINISH */

            line = line.trim();

            // Count Comments
            if (skipLine == false)
            {
               line = CommentHandler.HandleComments(line, CompilerDir);
            }

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

               // Special case
               if (line.equals("EXPORT"))
               {
                  line += "\n";
               }
               
               //Special case
               if (line.contains("else") && !line.contains("else if")) {
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
                        break;
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
         // If the original file was opened...
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

         // If the _PSLOC file was opened...
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
         QuoteHandler.Reset();
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
    * Count executable keywords - Count complexity keywords - Increment
    * complexity loop level based on keyword - Increment cyclomatic complexity
    * loop level based on keyword - Count LSLOC keywords - Count data
    * declarations - Delete exclude keywords and characters
    * 
    * If the line is not empty, increment the LSLOC counter to catch leftover
    * LSLOC
    * 
    * Read next line }
    */
   protected void CountFileLSLOC(UCCFile cntrResult)
   {
      LineNumberReader reader = null;

      int lsloc = 0; // Logical SLOC counter
      lslocKeywordsCount = 0; // LSLOC keyword counter
      level = 0; // Complexity loop level counter

      String tempLine = ""; // String for storing a temporary version of the
                            // line
      int lineIndex = 0; // Index of the line used for checksumming lines in
                         // sequence

      lslocLineValue = 0;
      File file = null;

      // Zero out loop level variables for complexity metrics
      ComplexityObj.loopLevelCount.clear();
      ComplexityObj.loopLevel = 0;

      // Zero out temporary loop level variables (catches all conditionals, not
      // just loop keyword info)
      TempLoopObj.loopLevel.clear();
      TempLoopObj.loopKeyword.clear();

      // Zero out function variables for cyclomatic complexity metrics
      CyclomaticComplexityObj.functionFlag = false;
      CyclomaticComplexityObj.functionLevel = 0;

      // Add base loop level to get started
      ComplexityObj.loopLevelCount.add(0);

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

            logger.debug("line " + reader.getLineNumber() + " in:  " + line);

            // If we're baseline differencing or searching for duplicates...
            if (RtParams.DiffCode || RtParams.SearchForDups)
            {
               // Save line into a temporary line for LSLOC writing
               tempLine = line;

               // Delete all {}'s except in compiler directives
               tempLine = DeleteAllCurlyBracketsNotInCompilerDirectives(tempLine);

               // Delete all exclude keywords
               tempLine = DeleteExcludeKeywords(tempLine);

               // Delete lines that end with :
               tempLine = DeleteLinesEndingWithAColon(tempLine);

               // Delete all line terminators (e.g. for instances where the line
               // ends up being ;)
               for (int lt = 0; lt < LineTerminator.size(); lt++)
               {
                  tempLine = tempLine.replaceAll(LineTerminator.get(lt), "");
               }
            }

            // // Delete everything between quotes
            // line = DeleteQuoteContents(line,cntrResult.charMap);

            // Delete everything between quotes
            line = DeleteQuoteContents(line);

            // Count executable keyword occurrences
            CountKeywords(cntrResult.ExecKeywordCnts, line);

            // Count data keyword occurrences
            CountKeywords(cntrResult.DataKeywordCnts, line);

            // Count bool operand occurances
            CountKeywords(cntrResult.BoolOperandCnts, line);

            line = line.trim();

            // Count complexity keywords
            if (RtParams.CountCmplxMetrics && !line.isEmpty())
            {
               CountComplexity(cntrResult, line);
            }

            // Delete all compiler directive lines left over from PSLOC
            line = DeleteCompilerDirectives(cntrResult, line);

            // Delete everything inside of loops
            for (int lk = 0; lk < LslocKeywords.size() && !line.isEmpty(); lk++)
            {
               line = DeleteLoopContents(line, LslocKeywords.get(lk));
            }

            // Assume we are not dealing with a function
            CyclomaticComplexityObj.functionFlag = false;

            // Count loop level complexity, determine function names, and
            // calculate cyclomatic complexity
            if (RtParams.CountCmplxMetrics && !line.isEmpty())
            {
               // Determine complexity loop level
               getLoopLevel(line);

               // Determine function name
               getFunctionName(cntrResult, line);

               // Increment cyclomatic complexity loop level based on keyword
               getCyclomaticComplexity(cntrResult, line);
            }

            // Count data declarations
            for (int lt = 0; lt < LineTerminator.size() && !line.isEmpty(); lt++)
            {
               // Count data declarations with line terminators
               if (line.endsWith(LineTerminator.get(lt)))
               {
                  // Count data declarations
                  line = CountDataDeclarations(cntrResult, line);
               }
            }

            // Count LSLOC keywords
            line = CountLSLOCKeywords(line);

            // Delete exclude keywords
            line = DeleteExcludeKeywords(line);

            // Delete exclude characters
            line = DeleteExcludeCharacters(line);

            // Delete lines ending in a comma
            line = DeleteLinesEndingInComma(line);

            // Delete "case ... :" statements
            line = DeleteCaseStatements(line);

            // Delete other lines ending in a colon
            line = DeleteLinesEndingWithAColon(line);

            CountNumandWordOperands(cntrResult.digitMap, cntrResult.wordMap, line);
            //
            // for (Entry<String, Integer> entry :
            // cntrResult.digitMap.entrySet())
            // {
            // String key = entry.getKey().toString();
            // Integer value = entry.getValue();
            // System.out.println(key +":"+value);
            // }

            // Count LSLOC
            if (!line.trim().isEmpty())
            {
               logger.debug("line " + reader.getLineNumber() + " out: " + line);
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
            cntrResult.NumExecInstrPhys = cntrResult.NumPSLOC - cntrResult.NumDataDeclPhys - cntrResult.NumCompilerDirectives;
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
            // Close the _LSLOC file
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

         // If we're counting complexity...
         if (RtParams.CountCmplxMetrics)
         {
            // Decrement needed loop levels to get correct result
            GetTrueLoopLevels();

            // Scale complexity loop keyword counts to proper loop levels
            ScaleComplexityLoopLevels(cntrResult);

            // Calculate cyclomatic complexity
            CalculateCyclomaticComplexity(cntrResult);

            // Calculate halstead metrics
            CalculateHalsteadMetrics(cntrResult);

            // Calculate Maintainability Index(MI)
            CalculateMaintainabilityIndex(cntrResult);
         }
      }

   }

   private void CountCharAndStringOperands(TreeMap<String, Integer> charMap, String line, Boolean add_to_count)
   {

      byte double_quote = (byte) '"';
      byte single_quote = (byte) '\'';

      String double_quote_escape = "\\\"";
      String single_quote_escape = "\\'";

      int next_double_quote_idx = -1;
      int next_single_quote_idx = -1;

      int temp = 0;

      int idx = line.indexOf(double_quote);

      while (idx != -1)
      {
         next_double_quote_idx = find_idx_of_next_quote(line, idx + 1, double_quote, double_quote_escape);

         if (next_double_quote_idx == -1)
         {
            break;
         }

         if (add_to_count)
         {
            if (charMap.containsKey(line.substring(idx, next_double_quote_idx + 1)))
            {
               temp = charMap.get(line.substring(idx, next_double_quote_idx + 1));
            }
            else
            {
               temp = 0;
            }
            temp = temp + 1;
            charMap.put(line.substring(idx, next_double_quote_idx + 1), temp);
         }
         line = (line.substring(0, idx) + "  " + line.substring(idx + next_double_quote_idx - idx + 1));
         idx = line.indexOf(double_quote, idx + 2);
      }

      idx = line.indexOf(single_quote);

      while (idx != -1)
      {

         next_single_quote_idx = find_idx_of_next_quote(line, idx + 1, single_quote, single_quote_escape);

         if (next_single_quote_idx == -1)
         {
            break;
         }

         if (add_to_count)
         {
            if (charMap.containsKey(line.substring(idx, next_single_quote_idx + 1)))
            {
               temp = charMap.get(line.substring(idx, next_single_quote_idx + 1));
            }
            else
            {
               temp = 0;
            }
            temp = temp + 1;
            charMap.put(line.substring(idx, next_single_quote_idx + 1), temp);
         }
         line = (line.substring(0, idx) + " " + line.substring(idx + next_single_quote_idx - idx + 1));
         idx = line.indexOf(single_quote, idx + 1);
      }

      return;

   }

   private int find_idx_of_next_quote(String line, int start_pos, byte quote, String quote_escape)
   {
      int idx = line.indexOf(quote, start_pos + 1);
      int next_escape_quote_pos = line.indexOf(quote_escape, start_pos + 1);

      while (idx - 1 == next_escape_quote_pos)
      {
         next_escape_quote_pos = line.indexOf(quote_escape, idx + 1);
         idx = line.indexOf(quote, idx + 1);
      }

      return idx;
   }

   private void CountNumandWordOperands(TreeMap<String, Integer> digitMap, TreeMap<String, Integer> wordMap,
            String line)
   {
      line.replaceAll("\\(", "  ");

      String in_new = line.replaceAll("\\)", " ");

      ArrayList<String> tokens = new ArrayList<String>();

      String[] iss = in_new.split("\\s+");

      for (int i = 0; i < iss.length; i++)
      {
         tokens.add(iss[i]);
         i++;
      }

      String token;
      // Parse tokens for variables, numbers
      for (String it : tokens)
      {
         token = it;

         if (token.length() == 0)
         {
            continue;
         }
         if (token.charAt(0) >= '0' && token.charAt(0) <= '9')
         {
            int temp = 0;
            if (digitMap.containsKey(token))
            {
               temp = digitMap.get(token);
            }
            else
            {
               temp = 0;
            }
            temp = temp + 1;
            digitMap.put(token, temp);
         }
         else if (IsAlphanumericOrUnderscore(token.charAt(0)))
         {
            int temp = 0;
            if (wordMap.containsKey(token))
            {
               temp = wordMap.get(token);
            }
            else
            {
               temp = 0;
            }
            temp = temp + 1;
            wordMap.put(token, temp);
         }

      }

   }

   private static boolean IsAlphanumericOrUnderscore(char c)
   {
      return Character.isLetterOrDigit(c) || c == (byte) '_';
   }

   /**
    * Class for storing loop level variables needed for complexity metrics.
    */
   static class ComplexityObj
   {
      // Loop level counting variables for complexity metrics
      static ArrayList<Integer> loopLevelCount = new ArrayList<Integer>();
      static int loopLevel;
   }

   /**
    * Class for storing cyclomatic complexity variables needed for cyclomatic
    * complexity metrics
    */
   static class CyclomaticComplexityObj
   {
      // Function variables used with cyclomatic complexity
      static boolean functionFlag;
      static int functionLevel;
   }

   /**
    * Class for storing temporary loop level variables which we will adjust to
    * get true loop level complexity
    */
   static class TempLoopObj
   {
      static ArrayList<Integer> loopLevel = new ArrayList<Integer>();
      static ArrayList<String> loopKeyword = new ArrayList<String>();
   }

   /**
    * Function for getting the loop level for a particular line based on
    * occurrences of the loop keywords list.
    * 
    * @param line
    *           String input
    */
   protected void getLoopLevel(String line)
   {
      String regEx;
      Matcher matcher;

      regEx = "\\}";
      matcher = Pattern.compile(regEx).matcher(line);

      // If we find }, decrement the level. We do this first so that it handles
      // "do { ... } while (...);" loops
      // correctly
      if (matcher.find() && level > 0)
      {
         level--;
      }

      // Loop through all the condition keywords
      for (int ck = 0; ck < CondKeywords.size(); ck++)
      {
         regEx = "\\b" + CondKeywords.get(ck) + "\\b";
         matcher = Pattern.compile(regEx).matcher(line);

         // If we find any of the condition keywords on the given line...
         if (matcher.find())
         {
            regEx = "\\{";
            matcher = Pattern.compile(regEx).matcher(line);

            // Add the loop level and loop keyword to our arraylists
            TempLoopObj.loopLevel.add(level);
            TempLoopObj.loopKeyword.add(CondKeywords.get(ck));

            // If the line ends with a {...
            if (matcher.find() && matcher.end() == line.trim().length())
            {
               // Increment the loop level
               level++;
            }

            // Kick out of the for loop
            break;
         }
      }

   }

   /**
    * Function for ruling out lines which are not functions and finally
    * extracting the function name if we determine that we are in a function.
    * 
    * @param cntrResult
    *           A UCCFile object to store results of code counter
    * @param line
    *           Line coming in from file
    */
   protected void getFunctionName(UCCFile cntrResult, String line)
   {
      String regEx;
      Matcher matcher;

      line = line.trim();

      if (line.endsWith("{"))
      {
         CyclomaticComplexityObj.functionFlag = true;
      }

      // Lines which match conditional keywords (but same pattern as a function)
      // are not functions
      for (int ck = 0; ck < CondKeywords.size() && !line.isEmpty(); ck++)
      {
         regEx = "\\b" + CondKeywords.get(ck) + "\\b";
         matcher = Pattern.compile(regEx).matcher(line);
         if (matcher.find() && matcher.start() == 0)
         {
            CyclomaticComplexityObj.functionFlag = false;
            break;
         }
      }

      // Lines which match unique data keywords (but same pattern as a function)
      // are not functions
      for (int udk = 0; udk < UniqueDataKeywords.size() && !line.isEmpty(); udk++)
      {
         regEx = "\\b" + UniqueDataKeywords.get(udk) + "\\b";
         matcher = Pattern.compile(regEx).matcher(line);
         if (matcher.find() && matcher.start() == 0)
         {
            CyclomaticComplexityObj.functionFlag = false;
            break;
         }
      }

      // Lines which match cyclomatic keywords (but same pattern as a function)
      // are not functions
      for (int ck = 0; ck < CyclCmplexKeywords.size() && !line.isEmpty(); ck++)
      {
         regEx = "\\b" + CyclCmplexKeywords.get(ck) + "\\b";
         matcher = Pattern.compile(regEx).matcher(line);
         if (matcher.find() && matcher.start() == 0)
         {
            CyclomaticComplexityObj.functionFlag = false;
            break;
         }
      }

      // Lines which contain the following specific characters are not functions
      if (line.equals("{") || line.contains("=") || line.equals("({"))
      {
         CyclomaticComplexityObj.functionFlag = false;
      }

      // If the function flag is still true, line contains a (, and the loop
      // level is less than or equal to the defined
      // function level, we have a function
      if (CyclomaticComplexityObj.functionFlag == true && line.indexOf("(") >= 0
               && ComplexityObj.loopLevel <= CyclomaticComplexityObj.functionLevel)
      {
         // Increment the number of functions (if we're past the first function)
         CyclomaticComplexityObj.functionLevel++;

         // Add a new element to the cyclomatic complexity count arrayList
         cntrResult.CyclCmplxCnts.add(new CmplxDataType());

         // Extract the function name
         if (CyclomaticComplexityObj.functionLevel > 0)
         {
            cntrResult.CyclCmplxCnts.get(CyclomaticComplexityObj.functionLevel - 1).Keyword =
                     line.substring(0, line.indexOf("("));
         }
      }
   }

   /**
    * Function for counting cyclomatic complexity based on cyclomatic complexity
    * keywords
    * 
    * @param cntrResult
    *           A UCCFile object to store results of code counter
    * @param line
    *           Line coming in from file
    */
   protected void getCyclomaticComplexity(UCCFile cntrResult, String line)
   {
      String regEx;
      Matcher matcher;

      for (int cck = 0; cck < CyclCmplexKeywords.size() && !line.isEmpty(); cck++)
      {
         regEx = "\\b" + CyclCmplexKeywords.get(cck) + "\\b";
         matcher = Pattern.compile(regEx).matcher(line);
         StringBuffer sbLine = new StringBuffer();

         // Count multiple LSLOC keywords on one line
         while (matcher.find() && !line.isEmpty() && line.length() >= CyclCmplexKeywords.get(cck).length())
         {
            matcher.appendReplacement(sbLine, " ");
            if (CyclomaticComplexityObj.functionLevel > 0)
            {
               cntrResult.CyclCmplxCnts.get(CyclomaticComplexityObj.functionLevel - 1).Count++;
            }
         }
         matcher.appendTail(sbLine);
         line = sbLine.toString();
      }

      // Handle ?: ternary if statements
      if (line.indexOf("?") >= 0 && line.indexOf(":") >= 0)
      {
         if (line.indexOf("?") < line.indexOf(":"))
         {
            if (CyclomaticComplexityObj.functionLevel > 0)
            {
               cntrResult.CyclCmplxCnts.get(CyclomaticComplexityObj.functionLevel - 1).Count++;
            }
         }
      }
   }

   /**
    * Function for counting and removing all LSLOC keywords and their (...)'s
    * 
    * @param line
    *           Line coming in from file
    * @return Line with any LSLOC keywords removed
    */
   protected String CountLSLOCKeywords(String line)
   {
      String regEx;
      Matcher matcher;
      int index = -1;

      line = line.trim();

      // Delete line terminators after while loops
      regEx = "\\bwhile\\b";
      matcher = Pattern.compile(regEx).matcher(line);

      // If we find a while loop...
      if (matcher.find())
      {
         for (int lt = 0; lt < LineTerminator.size(); lt++)
         {
            index = line.lastIndexOf(LineTerminator.get(lt));

            // If the line terminator is at the end of the line...
            if (index == line.length() - 1)
            {
               // Remove the line terminator from the end of the line
               line = line.substring(0, index);
            }
         }
      }

      // Loop through the LSLOC keywords list
      for (int lk = 0; lk < LslocKeywords.size() && !line.isEmpty(); lk++)
      {
         regEx = "\\b" + LslocKeywords.get(lk) + "\\b";
         matcher = Pattern.compile(regEx).matcher(line);
         StringBuffer sbLine = new StringBuffer();

         // While we find LSLOC keywords on the line...
         while (matcher.find() && !line.isEmpty() && line.length() >= LslocKeywords.get(lk).length())
         {
            matcher.appendReplacement(sbLine, " ");

            // Tally them up
            lslocKeywordsCount++;
            lslocLineValue++;
         }
         matcher.appendTail(sbLine);
         line = sbLine.toString();
      }

      return line;
   }

   /**
    * Function for deleting lines if they end in a comma
    * 
    * @param line
    *           Line coming in from file
    * @return Blank line if the line ends in a comma
    */
   protected String DeleteLinesEndingInComma(String line)
   {
      // If the line ends with a comma...
      if (line.trim().endsWith(","))
      {
         // Wipe the line
         line = "";
      }

      return line;
   }

   /**
    * Function for looping through all of the conditional + { occurrences we
    * found in the file, decrementing where needed, and adding the corrected
    * loop levels and counts to the complexity object's loop level counter.
    */
   protected void GetTrueLoopLevels()
   {
      boolean isLoopKeyword;
      int index;
      int newLevel;
      int maxLoopLevel = 0;
      int loopCounter = 0;

      // Loop through all the conditional keyword instances we found...
      for (int i = 0; i < TempLoopObj.loopLevel.size(); i++)
      {
         // Assume the keyword we found was not a loop keyword
         isLoopKeyword = false;

         // Loop through all the loop keywords...
         for (int lk = 0; lk < LoopKeywords.size(); lk++)
         {
            // If the conditional keyword we found is a loop keyword...
            if (TempLoopObj.loopKeyword.get(i).equals(LoopKeywords.get(lk)))
            {
               // Turn this boolean to true
               isLoopKeyword = true;
            }
         }

         // If the conditional keyword we found was not a loop keyword...
         if (isLoopKeyword == false)
         {
            // Save the index in the list
            index = i;

            if (i < TempLoopObj.loopLevel.size() - 1)
            {
               // Move to the next entry in the list
               i++;

               // Scale all the loop levels back 1 until we hit the level of the
               // conditional keyword we're dealing with.
               // This will "un-nest" iterative loops from conditional loops
               if (!TempLoopObj.loopKeyword.get(index).equals("do"))
               {
                  while (TempLoopObj.loopLevel.get(i) > TempLoopObj.loopLevel.get(index)
                           && i < TempLoopObj.loopLevel.size() - 1)
                  {
                     newLevel = TempLoopObj.loopLevel.get(i) - 1;
                     TempLoopObj.loopLevel.set(i, newLevel);
                     i++;
                  }

                  // Handle last entry if it meets the criteria
                  if (TempLoopObj.loopLevel.get(i) > TempLoopObj.loopLevel.get(index))
                  {
                     newLevel = TempLoopObj.loopLevel.get(i) - 1;
                     TempLoopObj.loopLevel.set(i, newLevel);
                  }
               }
            }

            // Go back to the conditional keyword's index and move on
            i = index;
         }
      }

      // Loop through all of the patterns we found for loop levels
      for (int i = 0; i < TempLoopObj.loopLevel.size(); i++)
      {
         // Get the max loop level of loop keywords
         for (int lk = 0; lk < LoopKeywords.size(); lk++)
         {
            // If the conditional keyword we found is a loop keyword...
            if (TempLoopObj.loopKeyword.get(i).equals(LoopKeywords.get(lk)))
            {
               // Get the maximum loop level we found for allocation of the loop
               // level count
               maxLoopLevel = Math.max(maxLoopLevel, TempLoopObj.loopLevel.get(i));
            }
         }
      }

      // Allocate the loop level count to the right size, discovered above
      for (int i = 0; i < maxLoopLevel; i++)
      {
         // Add another level to the level counter arrayList
         ComplexityObj.loopLevelCount.add(0);
      }

      // Loop through all of the patterns we found for loop levels
      for (int i = 0; i < TempLoopObj.loopLevel.size(); i++)
      {
         // Tally up the number of loops found at each level
         for (int lk = 0; lk < LoopKeywords.size(); lk++)
         {
            // If the conditional keyword we found is a loop keyword...
            if (TempLoopObj.loopKeyword.get(i).equals(LoopKeywords.get(lk)))
            {
               // Set the loop counter to the integer value of the current loop
               // level counter array list. This gets the
               // current loop count per the level we're on
               loopCounter = ComplexityObj.loopLevelCount.get(TempLoopObj.loopLevel.get(i)).intValue();

               // Increment the loop counter
               loopCounter++;

               // Set the loop level counter arrayList level <loopLevel> to the
               // count <loopCounter>
               ComplexityObj.loopLevelCount.set(TempLoopObj.loopLevel.get(i), loopCounter);
            }
         }
      }
   }

   /**
    * Function for adding all the loop level complexity counts up in a 0 to N-1
    * format and saving to the UCCFile's complexity loop level count metrics.
    * 
    * @param cntrResult
    *           A UCCFile object to store results of code counter
    */
   protected void ScaleComplexityLoopLevels(UCCFile cntrResult)
   {
      int finalLoopCounter = 0;

      // Loop through all the loop level counts
      for (int i = 0; i < ComplexityObj.loopLevelCount.size(); i++)
      {
         // If the count is non-zero
         if (ComplexityObj.loopLevelCount.get(i) > 0)
         {
            // Add the level to the loop level arraylist
            cntrResult.CmplxLoopLvlCnts.add(new CmplxDataType());

            // Set the count to the tallied value
            cntrResult.CmplxLoopLvlCnts.get(finalLoopCounter).Count = ComplexityObj.loopLevelCount.get(i);

            // Increment the loop counter
            finalLoopCounter++;
         }
      }
   }

   /**
    * Function for calculating the total cyclomatic complexity and average
    * cyclomatic complexity of the file.
    * 
    * @param cntrResult
    *           A UCCFile object to store results of code counter
    */
   protected void CalculateCyclomaticComplexity(UCCFile cntrResult)
   {
      // Calculate cyclomatic complexity
      for (int i = 0; i < cntrResult.CyclCmplxCnts.size(); i++)
      {
         // Add 1 for the file/function itself
         cntrResult.CyclCmplxCnts.get(i).Count++;

         // Sum up total cyclomatic complexity
         cntrResult.CyclCmplxTotal += cntrResult.CyclCmplxCnts.get(i).Count;
      }

      // Get average of cyclomatic complexity
      cntrResult.CyclCmplxAvg = (double) cntrResult.CyclCmplxTotal / (double) cntrResult.CyclCmplxCnts.size();
   }

   /**
    * Function for calculating the Halstead volume of the file.
    * 
    * @param cntrResult
    *           A UCCFile object to store results of code counter
    */

   private void CalculateHalsteadMetrics(UCCFile cntrResults)
   {

      int uniqueOperatorCnt = 0;
      int totalOperatorCnt = 0;
      int uniqueOperandCnt = 0;
      int totalOperandCnt = 0;

      int N = 0;
      int n = 0;

      UniqueOperatorList = new ArrayList<>();
      UniqueOperatorList.add(cntrResults.CmplxTrigCnts);
      UniqueOperatorList.add(cntrResults.CmplxLogCnts);
      UniqueOperatorList.add(cntrResults.CmplxCalcCnts);
      UniqueOperatorList.add(cntrResults.CmplxMathCnts);
      UniqueOperatorList.add(cntrResults.CmplxCondCnts);
      UniqueOperatorList.add(cntrResults.CmplxLogicCnts);
      UniqueOperatorList.add(cntrResults.CmplxPreprocCnts);
      UniqueOperatorList.add(cntrResults.CmplxAssignCnts);
      UniqueOperatorList.add(cntrResults.CmplxPntrCnts);
      UniqueOperatorList.add(cntrResults.DataKeywordCnts);
      UniqueOperatorList.add(cntrResults.ExecKeywordCnts);

      UniqueOperandList = new ArrayList<>();
      UniqueOperandList.add(cntrResults.charMap);
      UniqueOperandList.add(cntrResults.digitMap);
      UniqueOperandList.add(cntrResults.wordMap);

      int[] operatorCnt = GetUniqueAndTotalOperatorCount(UniqueOperatorList);
      uniqueOperatorCnt = operatorCnt[0];
      totalOperatorCnt = operatorCnt[1];

      logger.debug("uniqueOperatorCnt=" + uniqueOperatorCnt);
      logger.debug("totalOperatorCnt=" + totalOperatorCnt);

      int[] operandCnt = GetUniqueAndTotalOperandCount(UniqueOperandList, cntrResults.BoolOperandCnts);
      uniqueOperandCnt = operandCnt[0];
      totalOperandCnt = operandCnt[1];

      logger.debug("uniqueOperandCnt=" + uniqueOperatorCnt);
      logger.debug("totalOperandCnt=" + totalOperatorCnt);

      // Program Length(N) is the sum of the total number of operators and
      // operands in the program
      N = totalOperatorCnt + totalOperandCnt;

      // vocabulary size (n) is the sum of the number of unique operators and
      // operands
      n = uniqueOperatorCnt + uniqueOperandCnt;

      // Halstead Volume or Program volume (V) = N * log2(n)
      if (n > 0)
      {
         cntrResults.V = N * Math.log(n) / Math.log(2);
      }

      logger.debug("Halstead Volume=" + cntrResults.V);

   }

   /**
    * Function for calculating the 3-metric and 4-metric Maintainability Index
    * of the file.
    * 
    * @param cntrResult
    *           A UCCFile object to store results of code counter
    */
   private void CalculateMaintainabilityIndex(UCCFile cntrResults)
   {

      cntrResults.maintainabilityIndexNC = 171 - 5.2 * Math.log(cntrResults.V) - 0.23 * cntrResults.CyclCmplxTotal
               - 16.2 * Math.log(cntrResults.NumLSLOC);

      logger.debug("maintainabilityIndex_3_Metric=" + cntrResults.maintainabilityIndexNC);

      cntrResults.maintainabilityIndex = cntrResults.maintainabilityIndexNC
               + 50 * Math.sin((Math.sqrt(2.4 * (cntrResults.NumWholeComments + cntrResults.NumEmbeddedComments))));

      logger.debug("maintainabilityIndex_4_Metric=" + cntrResults.maintainabilityIndex);
   }

   /**
    * Computes total and unique number of operator counts
    * 
    * @param cmplxData
    *           Complexity data object containing counts to sum
    * @return Integer array containing total number of operator counts and
    *         unique number of operator counts
    */

   private int[] GetUniqueAndTotalOperatorCount(ArrayList<ArrayList<CmplxDataType>> cmplxData)
   {
      int uniqueOperators = 0;
      int totalOperators = 0;

      if (cmplxData != null)
      {
         for (int i = 0; i < cmplxData.size(); i++)
         {
            for (int j = 0; j < cmplxData.get(i).size(); j++)
            {
               totalOperators += cmplxData.get(i).get(j).Count;
               if (cmplxData.get(i).get(j).Count > 0)
               {
                  uniqueOperators++;
               }

            }
         }
      }

      return new int[] { uniqueOperators, totalOperators };
   }

   /**
    * Computes total and unique number of operaand counts
    * 
    * @param cmplxData
    *           Complexity data object containing counts to sum
    * @return Integer array containing total number of operand counts and unique
    *         number of operand counts
    */

   private int[] GetUniqueAndTotalOperandCount(ArrayList<TreeMap> map, ArrayList<CmplxDataType> cmplxData)
   {
      int uniqueOperands = 0;
      int totalOperands = 0;

      if (cmplxData != null)
      {
         for (int i = 0; i < cmplxData.size(); i++)
         {
            totalOperands += cmplxData.get(i).Count;
            if (cmplxData.get(i).Count > 0)
            {
               uniqueOperands++;
            }
         }
      }

      for (int i = 0; i < map.size(); i++)
      {
         TreeMap<String, Integer> iterMap = new TreeMap<>(map.get(i));
         uniqueOperands += iterMap.size();
         for (Entry<String, Integer> entry : iterMap.entrySet())
         {

            totalOperands += entry.getValue();
         }
      }

      return new int[] { uniqueOperands, totalOperands };
   }
}
