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

import ucc.datatypes.Constants;
import ucc.datatypes.UCCFile;
import ucc.langprops.LanguageProperties;
import ucc.utils.FileUtils;

/**
 * SQLCounter class performs various code counting operations on baseline(s)
 * identified by the user. It contains algorithms/methods to count SQL
 * programming language files.
 *
 * @author Integrity Applications Incorporated
 *
 */
public class SQLCounter extends CodeCounter
{
   /** Instantiate the Log4j2 logger for this class */
   private static final Logger logger = LogManager.getLogger(SQLCounter.class);

   /** Count for data keywords */
   private int DataKeywrdCnts;

   /** Count for execution keywords */
   private int ExecKeywrdCnts;

   /**
    * Default constructor to instantiate a BashCounter object
    * 
    * @param langProps
    *           Language properties for this counter
    */
   public SQLCounter(LanguageProperties langProps)
   {
      // Call super class's constructor
      super(langProps);
   }

   /**
    * Computes Source Lines of Code metrics for given file. Metrics include:
    * Physical Source Line of Code (PSLOC) counts Logical Source Line of Code
    * (LSLOC) counts Complexity keyword counts Cyclomatic complexity counts
    */
   public void CountSLOC(ArrayList<UCCFile> cntrResults, int i)
   {
      logger.debug("Counting PSLOC for " + cntrResults.get(i).FileName);
      // Count PSLOC and complexity
      CountFilePSLOC(cntrResults, i);
      logger.debug("Done\n");

      logger.debug("Counting LSLOC for " + cntrResults.get(i).FileName);
      // Count LSLOC and cyclomatic complexity
      CountFileLSLOC(cntrResults, i);
      logger.debug("Done\n");
   }

   /**
    * Counts Physical SLOC. Current algorithm:
    * 
    * Read first line
    * 
    * Delete the UTF-8 BOM, if found
    * 
    * While we have lines in the file... { - Do some pre-processing - Count
    * comments with handler (also counts blank lines) - Count non-blank lines -
    * Do some post-processing - Calculate PSLOC as non-blank lines - Save
    * post-processed line in new file for LSLOC counting - Read next line }
    */
   protected void CountFilePSLOC(ArrayList<UCCFile> cntrResults, int i)
   {
      UCCFile cntrResult = cntrResults.get(i);

      LineNumberReader reader = null;

      int psloc = 0; // Physical SLOC
      int index = -1;
      int openPos = -1;
      int closePos = 0;

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
               // Delete everything in CREATE TABLE ( ... )
               index = line.indexOf("create table");
               while (index != -1)
               {
                  openPos = line.indexOf("(", index + "create table".length());
                  if (openPos != -1)
                  {
                     closePos = FindClose(line, openPos, '(', ')');
                     if (closePos != -1)
                     {
                        line = line.substring(0, openPos + "(".length())
                                 + line.substring(closePos, line.length()).trim();
                     }
                  }
                  else
                  {
                     break;
                  }

                  index = line.indexOf("create table", openPos);
               }

               // Put certain patterns on new lines
               line += " ";
               line = line.replaceAll("\\bcreate table\\b", "\ncreate table");
               line = line.replaceAll("\\bcreate procedure\\b", "\ncreate procedure");
               // line = line.replaceAll("\\balter table\\b", "\nalter table ");
               // line = line.replaceAll("\\binsert into\\b", "\ninsert into ");
               // line = line.replaceAll("\\bvalues\\b", "\nvalues ");
               // line = line.replaceAll("\\bdeclare\\b", "\ndeclare ");
               // line = line.replaceAll("\\bset\\b", "\nset ");
               // line = line.replaceAll("\\bupdate\\b", "\nupdate ");
               line = line.replaceAll("\\bend\\b", "\nend");
               line = line.replaceAll("\\bselect\\b", "\nselect");

               for (int lt = 0; lt < LineTerminator.size(); lt++)
               {
                  line = line.replaceAll(LineTerminator.get(lt), "\n");
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
            // Close it
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
    * file - Search for duplicates based on threshold - Delete everything
    * between quotes - Count data keywords - Count executable keywords - Count
    * complexity keywords - Delete some unwanted patterns in order to correctly
    * count LSLOC - Count data keywords - Count executable keywords
    *
    * Read next line }
    * 
    * @param cntrResults
    *           An array list of counter results objects
    * 
    * @param i
    *           An index into cntrResults list
    */
   protected void CountFileLSLOC(ArrayList<UCCFile> cntrResults, int i)
   {
      UCCFile cntrResult = cntrResults.get(i);

      LineNumberReader reader = null;

      String tempLine = ""; // String for storing a temporary version of the
                            // line
      int lineIndex = 0; // Index of the line used for checksumming lines in
                         // sequence

      lslocLineValue = 0;
      File file = null;

      int index = -1;
      int openPos = -1;
      int closePos = 0;

      // Zero out data and executable keyword counts
      DataKeywrdCnts = 0;
      ExecKeywrdCnts = 0;

      // Initialize complexity keywords/counts for this file
      InitAllCmplxKeywords(cntrResult);

      // Buffered writer for _LSLOC file saving
      BufferedWriter bw = null;

      try
      {
         // If we're differencing baselines...
         if (RtParams.DiffCode)
         {
            if (cntrResult.EmbOfIdx == -1)
            {
               // Create file for LSLOC storage
               file = new File(FileUtils.BuildTempOutFileName_LSLOC(RtParams, cntrResult));
               bw = new BufferedWriter(new FileWriter(file));
            }
            else
            {
               // Create file for LSLOC storage
               file = new File(
                        FileUtils.BuildTempOutFileName_LSLOC_Embedded(RtParams, cntrResults.get(cntrResult.EmbOfIdx)));

               // If LSLOC file doesn't already exist, then create it
               if (!file.exists())
               {
                  file.createNewFile();
               }

               // true = append file
               FileWriter fileWritter = new FileWriter(file.getAbsolutePath(), true);
               bw = new BufferedWriter(fileWritter);
            }
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

            // Need to delete some certain patterns to correctly count LSLOC

            // Delete everything between GRANT ... TO
            line = line.replaceAll("(\\bgrant\\b)[^&]*(\\bto\\b)", "$1 $2");

            // Delete everything between CREATE PROCEDURE ... AS
            line = line.replaceAll("(\\bcreate procedure\\b)[^&]*(\\bas\\b)", "$1 $2");

            // Delete everything between ALTER PROCEDURE ... AS
            line = line.replaceAll("(\\balter procedure\\b)[^&]*(\\bas\\b)", "$1 $2");

            // Delete everything in CREATE TABLE ( ... )
            // This catches any create tables that were reformatted in the PSLOC
            // post-processing
            index = line.indexOf("create table");
            while (index != -1)
            {
               openPos = line.indexOf("(", index + "create table".length());
               // If we find the open ( after "create table"...
               if (openPos != -1)
               {
                  closePos = FindClose(line, openPos, '(', ')');
                  // If we find the matching close )
                  if (closePos != -1)
                  {
                     // Delete everything inside ()
                     line = line.substring(0, openPos + "(".length()) + line.substring(closePos, line.length()).trim();
                  }
               }
               else // Otherwise, break out of the while
               {
                  break;
               }

               // Find the next instance of "create table" on the line
               index = line.indexOf("create table", openPos);
            }

            if (!line.trim().isEmpty())
            {
               // Count instances of data keywords
               line = CountDataKeywords(line);

               // Count instances of executable keywords
               line = CountExecutableKeywords(line);

               // Count instances of LSLOC keywords
               line = CountLSLOCKeywords(line);
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
               if (RtParams.SearchForDups)
               {
                  // If we're in a pure SQL file
                  if (cntrResult.EmbOfIdx == -1)
                  {
                     if (!cntrResult.UniqueFileName)
                     {
                        cntrResult.FileLineChecksum.add(lineIndex, line.hashCode());
                     }
                  }
                  else // If we're in an embedded SQL file
                  {
                     if (!cntrResults.get(cntrResult.EmbOfIdx).UniqueFileName)
                     {
                        cntrResults.get(cntrResult.EmbOfIdx).FileLineChecksum.add(lineIndex, line.hashCode());
                     }
                  }
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
            cntrResult.NumDataDeclLog = DataKeywrdCnts;
            cntrResult.NumExecInstrLog = ExecKeywrdCnts;
            cntrResult.NumDataDeclPhys = cntrResult.NumDataDeclLog;
            cntrResult.NumExecInstrPhys = cntrResult.NumExecInstrLog;
            cntrResult.NumLSLOC = DataKeywrdCnts + ExecKeywrdCnts;
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
    * Function for counting and removing instances of data keywords.
    * 
    * @param line
    *           Line coming in from file
    * @return The input line with instances of data keywords removed
    */
   protected String CountDataKeywords(String line)
   {
      String regEx;
      Matcher matcher;

      for (int dk = 0; dk < DataKeyword.size(); dk++)
      {
         regEx = "\\b" + DataKeyword.get(dk) + "\\b";
         matcher = Pattern.compile(regEx).matcher(line);
         StringBuffer sbLine = new StringBuffer();
         while (matcher.find())
         {
            matcher.appendReplacement(sbLine, " ");
            DataKeywrdCnts++;
            lslocLineValue++;
         }
         matcher.appendTail(sbLine);
         line = sbLine.toString();
      }

      return line;
   }

   /**
    * Function for counting and removing instances of executable keywords.
    * 
    * @param line
    *           Line coming in from file
    * @return The input line with instances of executable keywords removed
    */
   protected String CountExecutableKeywords(String line)
   {
      String regEx;
      Matcher matcher;

      for (int ek = 0; ek < ExecKeywords.size(); ek++)
      {
         regEx = "\\b" + ExecKeywords.get(ek) + "\\b";
         matcher = Pattern.compile(regEx).matcher(line);
         StringBuffer sbLine = new StringBuffer();
         while (matcher.find())
         {
            matcher.appendReplacement(sbLine, " ");
            ExecKeywrdCnts++;
            lslocLineValue++;
         }
         matcher.appendTail(sbLine);
         line = sbLine.toString();
      }

      return line;
   }

   /**
    * Function for counting and removing instances of LSLOC keywords.
    * 
    * @param line
    *           Line coming in from file
    * @return The input line with instances of executable keywords removed
    */
   protected String CountLSLOCKeywords(String line)
   {
      String regEx;
      Matcher matcher;

      for (int lk = 0; lk < LslocKeywords.size(); lk++)
      {
         regEx = "\\b" + LslocKeywords.get(lk) + "\\b";
         matcher = Pattern.compile(regEx).matcher(line);
         StringBuffer sbLine = new StringBuffer();
         while (matcher.find())
         {
            matcher.appendReplacement(sbLine, " ");
            ExecKeywrdCnts++;
            lslocLineValue++;
         }
         matcher.appendTail(sbLine);
         line = sbLine.toString();
      }

      return line;
   }
}
