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
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ucc.datatypes.CmplxDataType;
import ucc.datatypes.Constants;
import ucc.datatypes.UCCFile;
import ucc.langprops.LanguageProperties;
import ucc.utils.FileUtils;

/**
 * PythonCounter class performs various code counting operations on baseline(s)
 * identified by the user. It contains algorithms/methods to count Python
 * programming language files.
 *
 * @author Integrity Applications Incorporated
 *
 */
public class PythonCounter extends CodeCounter
{
   /** Instantiate the Log4j2 logger for this class */
   private static final Logger logger = LogManager.getLogger(PythonCounter.class);

   /** LSLOC keywords count */
   private int lslocKeywordsCount;

   /**
    * Default constructor to instantiate a JavaCounter object
    * 
    * @param langProps
    *           Language properties for this counter
    */
   public PythonCounter(LanguageProperties langProps)
   {
      super(langProps); // Call super class's constructor
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

      int psloc = 0; // Physical SLOC

      // Pattern finder variables
      int openPos = 0;
      int closePos = 0;

      int parenPos = 0;
      int bracketPos = 0;
      int curlyBracketPos = 0;
      char openPattern = 0;
      char closePattern = 0;
      boolean multiLineFlag = false;
      String multiLine = "";
      String regEx = "";
      Matcher matcher;

      boolean continued = false;

      BufferedWriter bw = null;

      boolean isCompilerDirective = false;
      boolean cdNewLine = false;

      try
      {
         // Create file for PSLOC storage
         File file = new File(FileUtils.BuildTempOutFileName_PSLOC(RtParams, cntrResult));

         // Initialize BufferedWriter
         bw = new BufferedWriter(new FileWriter(file));

         // Initialize reader
         reader = new LineNumberReader(
                  new InputStreamReader(new FileInputStream(cntrResult.FileName), Constants.CHARSET_NAME));

         // Read first line
         String line = reader.readLine();

         // Delete UTF-8 BOM instance
         line = DeleteUTF8BOM(line);

         // Reset truncated lines count
         truncateLinesCount = 0;

         // While we have lines in the file...
         while (line != null) // While we have lines in the file, read line
         {
            // Ignore line data beyond the single line threshold value
            if (RtParams.TruncThreshold != 0 && line.length() > RtParams.TruncThreshold)
            {
               line = line.substring(0, RtParams.TruncThreshold);
               truncateLinesCount++;
            }

            logger.debug("line " + reader.getLineNumber() + " in:  " + line);

            /* PREPROCESSING START */

            if (!line.trim().isEmpty())
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

               // Delete all styles of comments inside quotes
               line = DeleteUnwantedStringsFromQuotes(line);
            }

            /* PREPROCESSING FINISH */

            // Count Comments
            line = CommentHandler.HandleComments(line, CompilerDir);

            // Count PSLOC
            if (!line.trim().isEmpty())
            {
               logger.debug("line " + reader.getLineNumber() + " out: " + line);
               psloc++;
            }

            /* POSTPROCESSING START */

            if (!line.trim().isEmpty())
            {
               // Undo multi-line ()'s, []'s, {}'s
               if (multiLineFlag == true)
               {
                  line = multiLine + " " + line.trim();
               }

               // Find which open pattern is first
               parenPos = line.indexOf("(");
               bracketPos = line.indexOf("[");
               curlyBracketPos = line.indexOf("{");

               // If any of the patterns aren't found, set their index to a
               // large value
               if (parenPos == -1)
               {
                  parenPos = 99999;
               }

               if (bracketPos == -1)
               {
                  bracketPos = 99999;
               }

               if (curlyBracketPos == -1)
               {
                  curlyBracketPos = 99999;
               }

               // Find the first pattern found with Math.min
               openPos = -1;
               if (parenPos == Math.min(parenPos, Math.min(bracketPos, curlyBracketPos)) && parenPos != 99999)
               {
                  openPos = line.indexOf("(");
                  openPattern = '(';
                  closePattern = ')';
               }
               else if (bracketPos == Math.min(parenPos, Math.min(bracketPos, curlyBracketPos)) && bracketPos != 99999)
               {
                  openPos = line.indexOf("[");
                  openPattern = '[';
                  closePattern = ']';
               }
               else if (curlyBracketPos == Math.min(parenPos, Math.min(bracketPos, curlyBracketPos))
                        && curlyBracketPos != 99999)
               {
                  openPos = line.indexOf("{");
                  openPattern = '{';
                  closePattern = '}';
               }

               // Undo multi-line ()/[]/{}'s
               if (openPos != -1)
               {
                  closePos = FindClose(line, openPos, openPattern, closePattern);
                  if (closePos != -1)
                  {
                     multiLineFlag = false;
                     multiLine = "";
                  }
                  else
                  {
                     multiLineFlag = true;
                     multiLine = line;
                     line = "";
                  }
               }

               // Assume lines are not continued by default
               continued = false;

               // Undo official line continuations
               for (int lcc = 0; lcc < LineContChars.size(); lcc++)
               {
                  if (line.trim().endsWith(LineContChars.get(lcc)))
                  {
                     line += " ";
                     continued = true;
                     break;
                  }
               }

               // Undo unofficial line continuations

               // Case 1: lines ending in a calculation
               for (int ck = 0; ck < CalcKeywords.size(); ck++)
               {
                  if (line.trim().endsWith(CalcKeywords.get(ck)))
                  {
                     line += " ";
                     continued = true;
                     break;
                  }
               }

               // Case 2: lines ending in an assignment
               for (int ak = 0; ak < AssignKeywords.size(); ak++)
               {
                  if (line.trim().endsWith(AssignKeywords.get(ak)))
                  {
                     line += " ";
                     continued = true;
                     break;
                  }
               }

               // Case 3: lines ending in a '({[,'
               if (line.trim().endsWith("(") || line.trim().endsWith("{") || line.trim().endsWith("[")
                        || line.trim().endsWith(","))
               {
                  line += " ";
                  continued = true;
               }

               if (continued == false)
               {
                  line += "\n";
               }
            }

            // if ":" found in line w/ LSLOC keyword...
            for (int lk = 0; lk < LslocKeywords.size(); lk++)
            {
               regEx = "\\b" + LslocKeywords.get(lk) + "\\b";
               matcher = Pattern.compile(regEx).matcher(line.trim());

               if (matcher.find() && matcher.start() == 0)
               {
                  line = line.replaceFirst(":", ":\n");
               }

            }

            // replace double newlines with single newline
            while (line.contains("\n\n"))
            {
               line = line.replaceAll("\n\n", "\n");
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

            // write the line
            if (!line.trim().isEmpty())
            {
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
    * directives - Delete everything between quotes - Delete everything in loops
    * (i.e. for(...) turns into for()) - Count data keywords - Count executable
    * keywords - Count complexity keywords - Increment complexity loop level
    * based on keyword - Increment cyclomatic complexity loop level based on
    * keyword - Count LSLOC keywords - Count data declarations - Delete exclude
    * keywords and characters
    * 
    * If the line is not empty, increment the LSLOC counter to catch leftover
    * LSLOC
    * 
    * Read next line }
    */
   protected void CountFileLSLOC(UCCFile cntrResult)
   {
      LineNumberReader reader = null;
      int lsloc = 0;
      lslocKeywordsCount = 0;
      int lineIndex = 0;
      String tempLine = "";

      lslocLineValue = 0;
      File file = null;

      // Zero out loop level variables for complexity metrics
      ComplexityObj.loopLevelCount.clear();

      // Zero out loop level variables for complx metrics
      TempLoopObj.indentationSize.clear();
      TempLoopObj.loopKeyword.clear();

      // Zero out function variables for cyclomatic complexity metrics
      CyclomaticComplexityObj.functionFlag = false;
      CyclomaticComplexityObj.functionLevel = 0;
      CyclomaticComplexityObj.indentation.clear();
      CyclomaticComplexityObj.keyword.clear();

      // Add base CC level for the cyclomatic complexity counts
      cntrResult.CyclCmplxCnts.add(new CmplxDataType());

      // Set the base CC level's function name to blank since Python can contain
      // scripts with no functions
      cntrResult.CyclCmplxCnts.get(CyclomaticComplexityObj.functionLevel).Keyword = " ";

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

               // Delete all exclude keywords
               tempLine = DeleteExcludeKeywords(tempLine);

               // Delete all exclude characters
               tempLine = DeleteExcludeCharacters(tempLine);

               // Delete all line terminators
               for (int lt = 0; lt < LineTerminator.size(); lt++)
               {
                  tempLine = tempLine.replaceAll(LineTerminator.get(lt), "");
               }
            }

            // Delete everything between quotes
            line = DeleteQuoteContents(line);

            // Count executable keyword occurences
            CountKeywords(cntrResult.ExecKeywordCnts, line);

            // Count data keyword occurrences
            CountKeywords(cntrResult.DataKeywordCnts, line);

            // Count complexity keywords
            if (RtParams.CountCmplxMetrics && !line.trim().isEmpty())
            {
               CountComplexity(cntrResult, line);
            }

            // Delete all compiler directive lines left over from PSLOC
            line = DeleteCompilerDirectives(cntrResult, line);

            if (RtParams.CountCmplxMetrics)
            {
               // Get loop level
               GetLoopLevel(line);

               // Get function name
               GetFunctionName(cntrResult, line);

               // Get cyclomatic complexity
               GetCyclomaticComplexity(cntrResult, line);
            }

            // Count data declarations
            line = CountDataDeclarations(cntrResult, line);

            // Delete exclude keywords
            line = DeleteExcludeKeywords(line);

            // Delete exclude characters
            line = DeleteExcludeCharacters(line);

            // Delete "case...:" statements
            line = DeleteCaseStatements(line);

            // Delete leftover :'s
            line = line.replaceAll(":", "");

            // Increment lsloc_counter
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
         // If we're counting complexity...
         if (RtParams.CountCmplxMetrics)
         {
            // Get true indentation size
            GetTrueIndentationSize();

            // Scale complexity loop levels
            ScaleComplexityLoopLevels(cntrResult);

            // Get true cyclomatic complexity
            GetTrueCyclomaticComplexity(cntrResult);

            // Calculate cyclomatic complexity
            CalculateCyclomaticComplexity(cntrResult);
         }

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
      }
   }

   /**
    * Class for storing loop level variables needed for complexity metrics.
    */
   static class ComplexityObj
   {
      // Loop level counting variables for complexity metrics
      static ArrayList<Integer> loopLevelCount = new ArrayList<Integer>();
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
      static ArrayList<Integer> indentation = new ArrayList<Integer>();
      static ArrayList<String> keyword = new ArrayList<String>();
   }

   /**
    * Class for storing temporary loop level variables which we will adjust to
    * get true loop level complexity
    */
   static class TempLoopObj
   {
      static ArrayList<Integer> indentationSize = new ArrayList<Integer>();
      static ArrayList<String> loopKeyword = new ArrayList<String>();
   }

   /**
    * Function for getting the loop level for a particular line based on
    * occurrences of the loop keywords list.
    * 
    * @param line
    *           String input
    */
   protected void GetLoopLevel(String line)
   {
      String regEx;
      Matcher matcher;
      int ind = line.length() - line.replaceAll("^\\s+", "").length();

      // Add special case for 'class'
      regEx = "\\bclass\\b";
      matcher = Pattern.compile(regEx).matcher(line);

      // If we find any of the condition keywords on the given line...
      if (matcher.find())
      {
         regEx = ":";
         matcher = Pattern.compile(regEx).matcher(line);

         // If the line ends with a :...
         if (matcher.find() && matcher.end() == line.replaceFirst("\\s+$", "").length())
         {
            ind = line.length() - line.replaceAll("^\\s+", "").length();

            // Add the loop level and loop keyword to our arraylists
            TempLoopObj.indentationSize.add(ind);
            TempLoopObj.loopKeyword.add("class");
         }
      }

      // Add special case for 'def'
      regEx = "\\bdef\\b";
      matcher = Pattern.compile(regEx).matcher(line);

      // If we find any of the condition keywords on the given line...
      if (matcher.find())
      {
         regEx = ":";
         matcher = Pattern.compile(regEx).matcher(line);

         // If the line ends with a :...
         if (matcher.find() && matcher.end() == line.replaceFirst("\\s+$", "").length())
         {
            ind = line.length() - line.replaceAll("^\\s+", "").length();

            // Add the loop level and loop keyword to our arraylists
            TempLoopObj.indentationSize.add(ind);
            TempLoopObj.loopKeyword.add("def");
         }
      }

      // Loop through all the condition keywords
      for (int ck = 0; ck < CondKeywords.size(); ck++)
      {
         regEx = "\\b" + CondKeywords.get(ck) + "\\b";
         matcher = Pattern.compile(regEx).matcher(line);

         // If we find any of the condition keywords on the given line...
         if (matcher.find())
         {
            regEx = ":";
            matcher = Pattern.compile(regEx).matcher(line);

            // If the line ends with a :...
            if (matcher.find() && matcher.end() == line.replaceFirst("\\s+$", "").length())
            {
               ind = line.length() - line.replaceAll("^\\s+", "").length();

               // Add the loop level and loop keyword to our arraylists
               TempLoopObj.indentationSize.add(ind);
               TempLoopObj.loopKeyword.add(CondKeywords.get(ck));
            }
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
   protected void GetFunctionName(UCCFile cntrResult, String line)
   {
      String regEx;
      Matcher matcher;

      line = line.trim();

      // Loop through all the function keywords...
      for (int fk = 0; fk < FunctionKeywords.size() && !line.isEmpty(); fk++)
      {
         regEx = ".*\\b" + FunctionKeywords.get(fk) + "\\b.*";
         matcher = Pattern.compile(regEx).matcher(line);
         if (matcher.find() && matcher.start() == 0)
         {
            // Increment the number of functions
            CyclomaticComplexityObj.functionLevel++;

            // Add a new element to the cyclomatic complexity count arrayList
            cntrResult.CyclCmplxCnts.add(new CmplxDataType());

            // Take substring to derive function name
            line = line.substring(line.indexOf(FunctionKeywords.get(fk)) + FunctionKeywords.get(fk).length(),
                     line.length()).trim();

            // Handles cases with arguments on function name line
            if (line.indexOf("(") >= 0)
            {
               line = line.substring(0, line.indexOf("(")).trim();
            }

            // Save function name
            cntrResult.CyclCmplxCnts.get(CyclomaticComplexityObj.functionLevel).Keyword = line;
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
   protected void GetCyclomaticComplexity(UCCFile cntrResult, String line)
   {
      String regEx;
      Matcher matcher;
      int ind = line.length() - line.replaceAll("^\\s+", "").length();

      // Add special case for 'def'
      regEx = "\\bdef\\b";
      matcher = Pattern.compile(regEx).matcher(line);

      // If we find any of the condition keywords on the given line...
      if (matcher.find())
      {
         regEx = ":";
         matcher = Pattern.compile(regEx).matcher(line);

         // If the line ends with a :...
         if (matcher.find() && matcher.end() == line.replaceFirst("\\s+$", "").length())
         {
            ind = line.length() - line.replaceAll("^\\s+", "").length();

            // Add the loop level and loop keyword to our arraylists
            CyclomaticComplexityObj.indentation.add(ind);
            CyclomaticComplexityObj.keyword.add(line);
         }
      }

      // Loop through all the condition keywords
      for (int cck = 0; cck < CyclCmplexKeywords.size() && !line.trim().isEmpty(); cck++)
      {
         regEx = "\\b" + CyclCmplexKeywords.get(cck) + "\\b";
         matcher = Pattern.compile(regEx).matcher(line);

         // If we find any of the condition keywords on the given line...
         if (matcher.find())
         {
            regEx = ":";
            matcher = Pattern.compile(regEx).matcher(line);

            // If the line ends with a :...
            if (matcher.find() && matcher.end() == line.replaceFirst("\\s+$", "").length())
            {
               ind = line.length() - line.replaceAll("^\\s+", "").length();

               // Add the loop level and loop keyword to our arraylists
               CyclomaticComplexityObj.indentation.add(ind);
               CyclomaticComplexityObj.keyword.add(CyclCmplexKeywords.get(cck));
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
      // If the first level count is 0, but there are other levels, remove it
      if (cntrResult.CyclCmplxCnts.size() > 1 && cntrResult.CyclCmplxCnts.get(0).Count == 0)
      {
         cntrResult.CyclCmplxCnts.remove(0);
      }

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
    * Function for looping through all of the conditional + { occurrences we
    * found in the file, decrementing where needed, and adding the corrected
    * loop levels and counts to the complexity object's loop level counter.
    */
   protected void GetTrueIndentationSize()
   {
      boolean isLoopKeyword;
      int index;
      int newIndentSize;
      int loopCounter = 0;
      int indentSizeDiff = 0;
      int size1 = 0;
      int size2 = 0;

      ArrayList<String> loopKeyword2 = new ArrayList<String>();
      ArrayList<Integer> indentationSize2 = new ArrayList<Integer>();
      ArrayList<Integer> uniqueIndSizes = new ArrayList<Integer>();

      // Loop through all the conditional keyword instances we found...
      for (int i = 0; i < TempLoopObj.loopKeyword.size(); i++)
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

            if (i < TempLoopObj.loopKeyword.size() - 1)
            {
               // Move to the next entry in the list
               i++;

               // Save what we may need to scale back by (difference in
               // indentation between two keywords)
               indentSizeDiff = TempLoopObj.indentationSize.get(i) - TempLoopObj.indentationSize.get(index);

               // Scale all the indentation sizes back until we hit the
               // indentation size of the conditional keyword
               // we're dealing with.
               // This will "un-nest" iterative loops from conditional loops
               while (TempLoopObj.indentationSize.get(i) > TempLoopObj.indentationSize.get(index)
                        && i < TempLoopObj.indentationSize.size() - 1)
               {
                  newIndentSize = TempLoopObj.indentationSize.get(i) - indentSizeDiff;
                  TempLoopObj.indentationSize.set(i, newIndentSize);
                  i++;
               }

               // Handle last entry if it meets the criteria
               if (TempLoopObj.indentationSize.get(i) > TempLoopObj.indentationSize.get(index))
               {
                  newIndentSize = TempLoopObj.indentationSize.get(i) - indentSizeDiff;
                  TempLoopObj.indentationSize.set(i, newIndentSize);
               }
            }

            // Go back to the conditional keyword's index and move on
            i = index;
         }
      }

      // Loop through all the conditional keyword instances we found... AGAIN
      for (int i = 0; i < TempLoopObj.loopKeyword.size(); i++)
      {
         // Assume the keyword we found was not a loop keyword AGAIN
         isLoopKeyword = false;

         // Loop through all the loop keywords... AGAIN
         for (int lk = 0; lk < LoopKeywords.size(); lk++)
         {
            // If the conditional keyword we found is a loop keyword... AGAIN
            if (TempLoopObj.loopKeyword.get(i).equals(LoopKeywords.get(lk)))
            {
               // Add to list of ONLY loop keywords
               loopKeyword2.add(TempLoopObj.loopKeyword.get(i));

               // Add to list of indentation sizes of ONLY loop keywords
               // System.out.println(TempLoopObj.loopKeyword.get(i) +
               // TempLoopObj.indentationSize.get(i));
               indentationSize2.add(TempLoopObj.indentationSize.get(i));
            }
         }
      }

      // Remove duplicates from indentationSize2 and save in a new unique list
      int value;
      for (int i = 0; i < indentationSize2.size(); i++)
      {
         value = indentationSize2.get(i);
         uniqueIndSizes.add(value);
      }
      uniqueIndSizes = RemoveDuplicates(uniqueIndSizes);

      // Sort elements of the unique list from least to greatest
      Collections.sort(uniqueIndSizes);

      // Allocate the loop level count to the size of the unique indentation
      // size list
      for (int i = 0; i < uniqueIndSizes.size(); i++)
      {
         // Add another level to the level counter arrayList
         ComplexityObj.loopLevelCount.add(0);
      }

      // Loop through all of the patterns we found for loop levels
      for (int i = 0; i < indentationSize2.size(); i++)
      {
         size1 = indentationSize2.get(i);
         // Tally up the number of loops found at each level
         for (int j = 0; j < uniqueIndSizes.size(); j++)
         {
            size2 = uniqueIndSizes.get(j);
            if (size1 == size2)
            {
               // Set the loop counter to the integer value of the current loop
               // level counter array list. This gets the
               // current loop count per the level we're on
               loopCounter = ComplexityObj.loopLevelCount.get(j).intValue();

               // Increment the loop counter
               loopCounter++;

               // Set the loop level counter arrayList level <loopLevel> to the
               // count <loopCounter>
               ComplexityObj.loopLevelCount.set(j, loopCounter);
            }
         }
      }
   }

   /**
    * Function for removing duplicates from an ArrayList.
    * 
    * @param uniqueIndSizes
    *           Input ArrayList containing indentation sizes
    * @return Input ArrayList with any duplicate entires removed
    */
   public static ArrayList<Integer> RemoveDuplicates(ArrayList<Integer> uniqueIndSizes)
   {
      Set<Integer> lhs = new LinkedHashSet<>(uniqueIndSizes);
      lhs.addAll(uniqueIndSizes);
      uniqueIndSizes.clear();
      uniqueIndSizes.addAll(lhs);

      return uniqueIndSizes;
   }

   /**
    * Function for getting the true cyclomatic complexity per function/file
    * based on keywords collected earlier.
    * 
    * @param cntrResult
    *           A UCCFile object to store results of code counter
    */
   protected void GetTrueCyclomaticComplexity(UCCFile cntrResult)
   {
      int kndx = 0;
      int tempCount = 0;
      String defName = "";

      for (int k = 0; k < CyclomaticComplexityObj.keyword.size(); k++)
      {
         if (CyclomaticComplexityObj.keyword.get(k).trim().startsWith("def"))
         {
            // Save the def index
            kndx = k;

            if (k < CyclomaticComplexityObj.keyword.size() - 1)
            {
               // Go to the next item in the list
               k++;

               // Add 1 for the def itself
               // tempCount++;

               // While the indentation is greater than that of the def
               while (CyclomaticComplexityObj.indentation.get(kndx) < CyclomaticComplexityObj.indentation.get(k)
                        && k < CyclomaticComplexityObj.indentation.size() - 1)
               {
                  // Increment the count for the def
                  tempCount++;

                  // Set the indentation to -999 as we go so we know which ones
                  // we've counted
                  CyclomaticComplexityObj.indentation.set(k, -999);

                  // Go to the next item in the list
                  k++;
               }

               // Handle last entry if it meets the criteria
               if (CyclomaticComplexityObj.indentation.get(kndx) < CyclomaticComplexityObj.indentation.get(k))
               {
                  // Increment the count for the def
                  tempCount++;

                  // Set the indentation to -999 as we go so we know which ones
                  // we've counted
                  CyclomaticComplexityObj.indentation.set(k, -999);
               }
            }
            else if (CyclomaticComplexityObj.keyword.size() == 1)
            {
               // Increment the count for the def
               tempCount++;

               // Set the indentation to -999 as we go so we know which ones
               // we've counted
               CyclomaticComplexityObj.indentation.set(kndx, -999);
            }

            // Get the defName the same way we got the function name
            for (int fk = 0; fk < FunctionKeywords.size(); fk++)
            {
               defName = CyclomaticComplexityObj.keyword.get(kndx);
               defName = defName.substring(
                        defName.indexOf(FunctionKeywords.get(fk)) + FunctionKeywords.get(fk).length(),
                        defName.length());

               if (defName.indexOf("(") >= 0)
               {
                  defName = defName.substring(0, defName.indexOf("(")).trim();
               }
            }

            // Match up the cyclomatic complexity function name with the current
            // list defName
            for (int cc = 0; cc < cntrResult.CyclCmplxCnts.size(); cc++)
            {
               // if the names match, set the count to tempCount
               if (cntrResult.CyclCmplxCnts.get(cc).Keyword.equals(defName))
               {
                  cntrResult.CyclCmplxCnts.get(cc).Count = tempCount;
                  break;
               }
            }

            // Reset our count
            tempCount = 0;

            // Reset our index
            k = kndx;

            // Set the indentation to -999 as we go so we know which ones we've
            // counted
            CyclomaticComplexityObj.indentation.set(k, -999);
         }
      }

      // Reset our count
      tempCount = 0;

      // Count all of our "level 0" instances (i.e. CC outside a function)
      for (int k = 0; k < CyclomaticComplexityObj.keyword.size(); k++)
      {
         if (CyclomaticComplexityObj.indentation.get(k) != -999)
         {
            tempCount++;
         }
      }

      // If we have any file level CC, save the tempCount
      if (cntrResult.CyclCmplxCnts.get(0).Keyword.trim().isEmpty())
      {
         if (cntrResult.CyclCmplxCnts.size() > 1)
         {
            if (tempCount >= 1)
            {
               cntrResult.CyclCmplxCnts.get(0).Count = tempCount;
            }
            else // Otherwise delete the file level item
            {
               cntrResult.CyclCmplxCnts.remove(0);
            }
         }
         else if (cntrResult.CyclCmplxCnts.size() == 1)
         {
            cntrResult.CyclCmplxCnts.get(0).Count = tempCount;
         }
      }
   }
}
