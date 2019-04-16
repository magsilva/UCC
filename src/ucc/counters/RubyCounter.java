package ucc.counters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
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
 * RubyCounter class performs various code counting operations on baseline(s)
 * identified by the user. It contains algorithms/methods to count Ruby
 * programming language files.
 *
 * @author Integrity Applications Incorporated
 *
 */
public class RubyCounter extends CodeCounter
{
   /** Instantiate the Log4j2 logger for this class */
   private static final Logger logger = LogManager.getLogger(RubyCounter.class);

   /**
    * Default constructor to instantiate a RubyCounter object
    * 
    * @param langProps
    *           Language properties for this counter
    */
   public RubyCounter(LanguageProperties langProps)
   {
      super(langProps);
   }

   /**
    * Counts Physical SLOC. Current algorithm:
    * 
    * Read first line
    * 
    * Delete the UTF-8 BOM, if found
    * 
    * Count #! compiler directive
    * 
    * While we have lines in the file... { - Do some pre-processing - Count
    * comments with handler (also counts blank lines) - Count compiler
    * directives with handler - Count non-blank lines - Do some post-processing
    * - Calculate PSLOC as non-blank lines + compiler directives + number of
    * continued compiler directives (these were erased in the compiler handler)
    * - Save post-processed line in new file for LSLOC counting - Read next line
    * }
    */
   protected void CountFilePSLOC(ArrayList<UCCFile> cntrResults, int i)
   {
      UCCFile cntrResult = cntrResults.get(i);

      LineNumberReader reader = null;

      int psloc = 0; // Physical SLOC counter

      // Buffered writer for _PSLOC file saving
      BufferedWriter bw = null;

      boolean isCompilerDirective = false;
      boolean cdNewLine = false;

      String regEx;
      Pattern pattern;
      Matcher matcher;

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
               // Undo line continuations
               for (int lcc = 0; lcc < LineContChars.size(); lcc++)
               {
                  if (line.endsWith(LineContChars.get(lcc)))
                  {
                     line += " ";
                  }
                  else if (line.endsWith(","))
                  {
                     line += " ";
                  }
                  else
                  {
                     line += "\n";
                  }
               }

               // Replace all line terminators with new lines
               for (int lt = 0; lt < LineTerminator.size(); lt++)
               {
                  line = line.replaceAll(LineTerminator.get(lt), LineTerminator.get(lt) + "\n");
               }

               // Insert a newline before each condition keyword
               for (int ck = 0; ck < CondKeywords.size(); ck++)
               {
                  regEx = "\\b" + CondKeywords.get(ck) + "\\b";
                  pattern = Pattern.compile(regEx);
                  matcher = pattern.matcher(line);

                  // If we find a condition keyword on the line...
                  if (matcher.find())
                  {
                     // Put a newline in front of it
                     line = line.replaceAll(regEx, "\n" + CondKeywords.get(ck));
                  }
               }
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

            // Delete all lines that are just a newline
            if (line.trim().equals("\n"))
            {
               line = "";
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
         logger.error("The reader or writer failed!");
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

            try
            {
               reader.close();
            }
            catch (IOException e)
            {
               logger.error("The reader failed to close!");
            }
            reader = null;
         }

         if (bw != null)
         {
            // Add compiler directives to end of PSLOC file
            try
            {
               bw.close();
            }
            catch (IOException e)
            {
               logger.error("The writer failed to write the string to the file: "
                        + FileUtils.BuildTempOutFileName_PSLOC(RtParams, cntrResult));
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
    * directives - Delete everything between quotes - Count executable keywords
    * - Count complexity keywords - Increment complexity loop level based on
    * keyword - Increment cyclomatic complexity loop level based on keyword -
    * Delete exclude keywords and characters
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
      String tempLine = ""; // String for storing a temporary version of the
                            // line
      int lineIndex = 0; // Index of the line used for checksumming lines in
                         // sequence

      lslocLineValue = 0;
      File file = null;

      // Zero out loop level variables for complexity metrics
      ComplexityObj.loopLevelCount.clear();
      ComplexityObj.loopLevel = 0;

      // Zero out function variables for cyclomatic complexity metrics
      CyclomaticComplexityObj.functionFlag = false;
      CyclomaticComplexityObj.functionLevel = 0;

      // Add base loop level to get started
      ComplexityObj.loopLevelCount.add(0);

      // Add base CC level for the cyclomatic complexity counts
      cntrResult.CyclCmplxCnts.add(new CmplxDataType());

      // Set the base CC level's function name to blank since Ruby can contain
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

            logger.debug("Line " + reader.getLineNumber() + " in:  " + line);

            // If we're baseline differencing or searching for duplicates...
            if (RtParams.DiffCode || RtParams.SearchForDups)
            {
               // Save line into a temporary line for LSLOC writing
               tempLine = line;

               // Delete all exclude keywords
               tempLine = DeleteExcludeKeywords(tempLine);

               // Delete all exclude characters
               tempLine = DeleteExcludeCharacters(tempLine);
            }

            // Delete all compiler directive lines left over from PSLOC
            line = DeleteCompilerDirectives(cntrResult, line);

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

            if (RtParams.CountCmplxMetrics && !line.isEmpty())
            {
               // Determine complexity loop level
               getLoopLevel(line);

               // Determine function name
               getFunctionName(cntrResult, line);

               // Increment cyclomatic complexity loop level based on keyword
               getCyclomaticComplexity(cntrResult, line);
            }

            // Delete exclude keywords
            line = DeleteExcludeKeywords(line);

            // Delete exclude characters
            line = DeleteExcludeCharacters(line);

            // Delete "when" statements (Ruby version of "case")
            line = DeleteWhenStatements(line);

            // Delete other lines ending in a colon
            line = DeleteLinesEndingWithAColon(line);

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
         logger.error("The reader or writer failed!");
      }
      finally
      {
         // If we're counting complexity...
         if (RtParams.CountCmplxMetrics)
         {
            // Scale complexity loop keyword counts to proper loop levels
            ScaleComplexityLoopLevels(cntrResult);

            // Calculate cyclomatic complexity
            CalculateCyclomaticComplexity(cntrResult);
         }

         if (reader != null)
         {
            // Save LSLOC metrics counted
            cntrResult.NumLSLOC = cntrResult.NumCompilerDirectives + lsloc;
            cntrResult.NumExecInstrLog =
                     cntrResult.NumLSLOC - cntrResult.NumDataDeclLog - cntrResult.NumCompilerDirectives;
            cntrResult.NumDataDeclPhys = cntrResult.NumDataDeclLog;
            cntrResult.NumExecInstrPhys = cntrResult.NumPSLOC - cntrResult.NumDataDeclPhys - cntrResult.NumCompilerDirectives;
            cntrResult.LangVersion = LangProps.GetLangVersion();
            try
            {
               reader.close();
            }
            catch (IOException e)
            {
               logger.error("The reader faile to close!");
            }
            reader = null;

            // Delete PSLOC file
            FileUtils.DeleteFile(FileUtils.BuildTempOutFileName_PSLOC(RtParams, cntrResult));
         }

         if (bw != null)
         {
            try
            {
               bw.close();
            }
            catch (IOException e)
            {
               logger.error("The writer failed to close.");
            }
            bw = null;
         }
      }
   }

   /**
    * Function for deleting when statements.
    *
    * @param line
    *           Line coming in from file
    * @return The modified line coming in
    */
   protected String DeleteWhenStatements(String line)
   {
      String regEx;
      Matcher matcher;

      regEx = "\\b" + "when" + "\\b";
      matcher = Pattern.compile(regEx).matcher(line);
      if (matcher.find())
      {
         line = "";
      }

      return line;
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
    * Function for getting the loop level for a particular line based on
    * occurrences of the loop keywords list.
    * 
    * @param line
    *           Line coming in from file
    */
   protected void getLoopLevel(String line)
   {
      String regEx;
      Matcher matcher;

      int loopCounter = 0;

      // Loop through the loop keywords list
      for (int lk = 0; lk < LoopKeywords.size() && !line.isEmpty(); lk++)
      {
         regEx = "\\b" + LoopKeywords.get(lk) + "\\b";
         matcher = Pattern.compile(regEx).matcher(line);

         // If we find any of the loop keywords on the given line
         if (matcher.find() && !line.isEmpty() && line.length() >= LoopKeywords.get(lk).length()
                  && !line.contains("end"))
         {
            // Increment the loop level
            ComplexityObj.loopLevel++;

            // If the loop level counter arrayList size is less than or equal to
            // the loop level integer
            if (ComplexityObj.loopLevelCount.size() <= ComplexityObj.loopLevel)
            {
               // Add another level to the level counter arrayList
               ComplexityObj.loopLevelCount.add(0);
            }

            // Set the loop counter to the integer value of the current loop
            // level counter array list. This gets the
            // current loop count per the level we're on
            loopCounter = ComplexityObj.loopLevelCount.get(ComplexityObj.loopLevel).intValue();

            // Increment the loop counter
            loopCounter++;

            // Set the loop level counter arrayList level <loopLevel> to the
            // count <loopCounter>
            ComplexityObj.loopLevelCount.set(ComplexityObj.loopLevel, loopCounter);

            // Delete the pattern we found
            line = line.replaceFirst(regEx, "");
         }
         else if (line.contains("end")) // If the line contains the loop end
                                        // keyword
         {
            // If the loop level is greater than 0
            if (ComplexityObj.loopLevel > 0)
            {
               // Decrement the loop level
               ComplexityObj.loopLevel--;
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
   protected void getFunctionName(UCCFile cntrResult, String line)
   {
      String regEx;
      Matcher matcher;

      line = line.trim();

      // Loop through all the function keywords...
      for (int fk = 0; fk < FunctionKeywords.size() && !line.isEmpty(); fk++)
      {
         regEx = "\\b" + FunctionKeywords.get(fk) + "\\b";
         matcher = Pattern.compile(regEx).matcher(line);
         if (matcher.find() && matcher.start() == 0 && !line.contains("end"))
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
   protected void getCyclomaticComplexity(UCCFile cntrResult, String line)
   {
      String regEx;
      Matcher matcher;

      // Loop through all the cyclomatic complexity keywords...
      for (int cck = 0; cck < CyclCmplexKeywords.size() && !line.isEmpty(); cck++)
      {
         regEx = "\\b" + CyclCmplexKeywords.get(cck) + "\\b";
         matcher = Pattern.compile(regEx).matcher(line);
         StringBuffer sbLine = new StringBuffer();

         // While the line isn't empty, the line is longer than keyword, and we
         // find the pattern...
         while (!line.isEmpty() && line.length() >= CyclCmplexKeywords.get(cck).length() && !line.contains("end")
                  && matcher.find())
         {
            matcher.appendReplacement(sbLine, " ");

            // Increment the function level's CC count
            cntrResult.CyclCmplxCnts.get(CyclomaticComplexityObj.functionLevel).Count++;
         }

         // Update the line
         matcher.appendTail(sbLine);
         line = sbLine.toString();
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
}
