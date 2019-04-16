package ucc.counters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ucc.datatypes.Constants;
import ucc.datatypes.UCCFile;
import ucc.langprops.LanguageProperties;
import ucc.utils.FileUtils;

/**
 * CSSCounter class performs various code counting operations on baseline(s)
 * identified by the user. It contains algorithms/methods to count CSS
 * programming language files.
 *
 * @author Integrity Applications Incorporated
 *
 */
public class CSSCounter extends CodeCounter
{
   /** Instantiate the Log4j2 logger for this class */
   private static final Logger logger = LogManager.getLogger(CSSCounter.class);

   /**
    * Default constructor to instantiate a CCPPCounter object
    * 
    * @param langProps
    *           Language properties for this counter
    */
   public CSSCounter(LanguageProperties langProps)
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

      int psloc = 0; // Physical SLOC counter

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
               if (!line.endsWith(","))
               {
                  // Add a newline after all line terminators
                  for (int lt = 0; lt < LineTerminator.size(); lt++)
                  {
                     line = line.replaceAll(LineTerminator.get(lt), "\n");
                  }

                  // Add a newline after all open curly braces
                  line = line.replaceAll("\\{", "\\{\n");

                  // Add a newline before and after all close curly braces
                  line = line.replaceAll("\\}", "\n\\}\n");

                  // Replace all double newlines with single newlines
                  while (line.contains("\n\n"))
                  {
                     line = line.replaceAll("\n\n", "\n");
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
            // Close the _PSLOC file
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
    * between quotes - Count executable keywords - Count data declarations -
    * Delete exclude keywords and characters
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
      String tempLine = ""; // String for storing a temporary version of the
                            // line
      int lineIndex = 0; // Index of the line used for checksumming lines in
                         // sequence

      lslocLineValue = 0;
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

               // Delete exclude keywords
               tempLine = DeleteExcludeKeywords(tempLine);

               // Delete exclude characters
               tempLine = DeleteExcludeCharacters(tempLine);
            }

            // Delete everything between quotes
            line = DeleteQuoteContents(line);

            // Count executable keyword occurrences
            CountKeywords(cntrResult.ExecKeywordCnts, line);

            line = line.trim();

            // Count data declarations
            line = CountDataDeclarations(cntrResult, line);

            // Delete exclude keywords
            line = DeleteExcludeKeywords(line);

            // Delete exclude characters
            line = DeleteExcludeCharacters(line);

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
               if (RtParams.SearchForDups)
               {
                  // If we're in a pure CSS file
                  if (cntrResult.EmbOfIdx == -1)
                  {
                     if (!cntrResult.UniqueFileName)
                     {
                        cntrResult.FileLineChecksum.add(lineIndex, line.hashCode());
                     }
                  }
                  else // If we're in an embedded CSS file
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
            cntrResult.NumLSLOC = cntrResult.NumDataDeclLog + lsloc;
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
      if (line.endsWith("{"))
      {
         line = "";
         cntrResult.NumDataDeclLog++;
         lslocLineValue++;
      }

      return line;
   }
}
