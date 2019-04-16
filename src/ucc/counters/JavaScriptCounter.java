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
 * JavaScriptCounter class performs various code counting operations on
 * baseline(s) identified by the user. It contains algorithms/methods to count
 * JavaScript programming language files.
 *
 * @author Integrity Applications Incorporated
 *
 */
public class JavaScriptCounter extends CodeCounter
{
   /** Instantiate the Log4j2 logger for this class */
   private static final Logger logger = LogManager.getLogger(JavaScriptCounter.class);

   /** LSLOC keywords count */
   private int lslocKeywordsCount;

   /**
    * Default constructor to instantiate a JavaCounter object
    * 
    * @param langProps
    *           Language properties for this counter
    */
   public JavaScriptCounter(LanguageProperties langProps)
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

      String regEx;
      Matcher matcher;
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

               // Delete all styles of comments inside quotes
               line = DeleteUnwantedStringsFromQuotes(line);

               // Delete instances of <!-- and //-->. They were intended to
               // allow browsers that didn't understand the
               // <script> tag to degrade gracefully
               line = line.replaceAll("<!--", "").replaceAll("//-->", "");

               // If the line has become empty from handling <!-- or //-->, skip
               // this line for comment handling
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
               // Delete the contents of for loops
               closePos = 0;
               regEx = "\\bfor\\b";
               matcher = Pattern.compile(regEx).matcher(line);
               while (closePos >= 0 && matcher.find(closePos))
               {
                  openPos = line.indexOf("(", matcher.end());
                  if (openPos != -1)
                  {
                     closePos = FindClose(line, openPos, '(', ')');
                     if (closePos != -1)
                     {
                        line = line.substring(0, openPos + "(".length()) + line.substring(closePos, line.length());
                     }
                  }
                  else
                  {
                     break;
                  }
               }

               regEx = "\\bvar\\b";
               matcher = Pattern.compile(regEx).matcher(line);
               if (matcher.find())
               {
                  line = line.replaceAll(regEx, "\nvar");
               }

               for (int lt = 0; lt < LineTerminator.size(); lt++)
               {
                  line = line.replaceAll(LineTerminator.get(lt), LineTerminator.get(lt) + "\n");
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
    * 
    * @param cntrResults
    *           A UCCFile object ArrayList to store results of code counters
    * @param i
    *           The index of the UCCFile we want to work on
    */
   protected void CountFileLSLOC(ArrayList<UCCFile> cntrResults, int i)
   {
      UCCFile cntrResult = cntrResults.get(i);

      LineNumberReader reader = null;

      int lsloc = 0; // Logical SLOC counter
      lslocKeywordsCount = 0; // LSLOC keyword counter

      String tempLine = ""; // String for storing a temporary version of the
                            // line
      int lineIndex = 0; // Index of the line used for checksumming lines in
                         // sequence

      lslocLineValue = 0;
      File file = null;

      String regEx;
      Matcher matcher;

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
            if (cntrResult.EmbOfIdx == -1 && (RtParams.DiffCode || RtParams.SearchForDups))
            {
               // Save line into a temporary line for LSLOC writing
               tempLine = line;

               // Delete all exclude keywords
               tempLine = DeleteExcludeKeywords(tempLine);

               // Delete exclude characters
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

            // Delete everything inside of loops
            for (int lk = 0; lk < LslocKeywords.size() && !line.isEmpty(); lk++)
            {
               line = DeleteLoopContents(line, LslocKeywords.get(lk));
            }

            // Count data declarations
            line = CountDataDeclarations(cntrResult, line);

            // Count LSLOC keywords
            line = CountLSLOCKeywords(line);

            // Count function keywords
            CountFunctionKeywords(line);

            // Count LSLOC
            if (!line.trim().isEmpty())
            {
               // Count number of line terminators on the line
               for (int lt = 0; lt < LineTerminator.size(); lt++)
               {
                  regEx = LineTerminator.get(lt);
                  matcher = Pattern.compile(regEx).matcher(line);
                  while (matcher.find())
                  {
                     logger.debug("line " + reader.getLineNumber() + " out: " + line);
                     lsloc++;
                     lslocLineValue++;
                  }
               }
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
                  // If we're in a pure JavaScript file
                  if (cntrResult.EmbOfIdx == -1)
                  {
                     if (!cntrResult.UniqueFileName)
                     {
                        cntrResult.FileLineChecksum.add(lineIndex, line.hashCode());
                     }
                  }
                  else // If we're in an embedded JavaScript file
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
      StringBuffer sbLine = new StringBuffer();

      line = line.trim();

      // Loop through the LSLOC keywords list
      for (int lk = 0; lk < LslocKeywords.size() && !line.isEmpty(); lk++)
      {
         regEx = "\\b" + LslocKeywords.get(lk) + "\\b";
         matcher = Pattern.compile(regEx).matcher(line);
         sbLine.setLength(0);

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
    * Function for counting and removing all LSLOC keywords and their (...)'s
    * 
    * @param line
    *           Line coming in from file
    * @return Line with any LSLOC keywords removed
    */
   protected String CountFunctionKeywords(String line)
   {
      String regEx;
      Matcher matcher;
      StringBuffer sbLine = new StringBuffer();

      line = line.trim();

      // Loop through the LSLOC keywords list
      for (int fk = 0; fk < FunctionKeywords.size() && !line.isEmpty(); fk++)
      {
         regEx = "\\b" + FunctionKeywords.get(fk) + "\\b";
         matcher = Pattern.compile(regEx).matcher(line);
         sbLine.setLength(0);

         // While we find LSLOC keywords on the line...
         while (matcher.find() && !line.isEmpty() && line.length() >= FunctionKeywords.get(fk).length())
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

      regEx = "\\bvar\\b";
      pattern = Pattern.compile(regEx);
      matcher = pattern.matcher(line.trim());
      if (matcher.find() && matcher.start() == 0)
      {
         line = "";
         cntrResult.NumDataDeclLog++;
         lslocLineValue++;
      }

      return line;
   }
}
