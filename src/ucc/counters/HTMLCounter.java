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

import ucc.counters.handlers.MultiLanguageHandler;
import ucc.datatypes.CmplxDataType;
import ucc.datatypes.Constants;
import ucc.datatypes.UCCFile;
import ucc.langprops.LanguageProperties;
import ucc.utils.FileUtils;

/**
 * HTMLCounter class generates the values of specified metrics for code written
 * in HTML.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class HTMLCounter extends CodeCounter
{
   /** Instantiate the Log4j2 logger for this class */
   private static final Logger logger = LogManager.getLogger(HTMLCounter.class);

   /** LSLOC keywords count */
   private boolean writeRawLsloc;

   /**
    * Default constructor to instantiate a HTMLCounter object
    * 
    * @param langProps
    *           Language properties for this counter
    */
   public HTMLCounter(LanguageProperties langProps)
   {
      // Call super class's constructor
      super(langProps);

      // Create the multi-language handler
      MultiLanguageHandler = new MultiLanguageHandler(LangProps, RtParams);
   }

   /**
    * Computes Source Lines of Code metrics for given file. Metrics include:
    * Physical Source Line of Code (PSLOC) counts Logical Source Line of Code
    * (LSLOC) counts Complexity keyword counts Cyclomatic complexity counts
    */
   public void CountSLOC(ArrayList<UCCFile> cntrResults, int i)
   {
      // Cut embedded code up and save into new files
      if (cntrResults.get(i).EmbOfIdx == -1)
      {
         // Initialize complexity keywords/counts for this file
         InitAllCmplxKeywords(cntrResults.get(i));

         cntrResults.get(i).HasEmbCode = true;
         logger.debug("Checking " + cntrResults.get(i).FileName + " for embedded languages");
         MultiLanguageHandler.HandleEmbeddedCode(cntrResults, i);
         logger.debug("Done\n");
      }

      if (cntrResults.get(i).EmbOfIdx != -1)
      {
         logger.debug("Counting SLOC for " + cntrResults.get(i).FileName);
         // Count PSLOC and complexity
         CountFileSLOC(cntrResults, i);
         logger.debug("Done\n");
      }
   }

   /**
    * Function to count file PSLOC and LSLOC
    * 
    * @param cntrResults
    *           A UCCFile object ArrayList to store results of code counters
    * @param i
    *           The index of the UCCFile we want to work on
    */
   protected void CountFileSLOC(ArrayList<UCCFile> cntrResults, int i)
   {
      UCCFile cntrResult = cntrResults.get(i);

      LineNumberReader reader = null;

      int psloc = 0; // Physical SLOC
      int lsloc = 0; // Logical SLOC
      String regEx;
      Matcher matcher;
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

         reader = new LineNumberReader(
                  new InputStreamReader(new FileInputStream(cntrResult.FileName), Constants.CHARSET_NAME));

         // Read first line
         String line = reader.readLine();

         // Delete UTF-8 BOM instance
         line = DeleteUTF8BOM(line);

         // While we have lines in the file...
         while (line != null)
         {
            lslocLineValue = 0;
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

               // Delete escaped quotes
               line = DeleteEscapedQuotes(line);

               // Delete double quotes
               line = DeleteEmptyQuotes(line);
            }

            /* PREPROCESSING FINISH */

            // Count Comments
            line = CommentHandler.HandleComments(line, CompilerDir);

            line = line.trim();

            // Count PSLOC and LSLOC
            if (!line.isEmpty())
            {
               logger.debug("line " + reader.getLineNumber() + " out: " + line);

               psloc++;

               line = line.replaceAll("</", " ");

               regEx = "\\<";
               matcher = Pattern.compile(regEx).matcher(line);

               // Count all open tags as LSLOC
               while (matcher.find())
               {
                  lsloc++;
                  lslocLineValue++;
               }
            }

            // If we're baseline differencing or searching for duplicates...
            if (RtParams.DiffCode || RtParams.SearchForDups)
            {
               if (lslocLineValue > 0)
               {
                  // If we're baseline differencing, write the LSLOC line
                  if (RtParams.DiffCode)
                  {
                     bw.write(lslocLineValueDelim + Integer.toString(lslocLineValue) + lslocLineValueDelim + line
                              + "\n");
                  }

                  // If we're searching for duplicates, checksum the LSLOC line
                  if (RtParams.SearchForDups)
                  {
                     // If we're in a pure HTML file
                     if (cntrResult.EmbOfIdx == -1)
                     {
                        if (!cntrResult.UniqueFileName)
                        {
                           cntrResult.FileLineChecksum.add(lineIndex, line.hashCode());
                        }
                     }
                     else // If we're in an embedded HTML file
                     {
                        if (!cntrResults.get(cntrResult.EmbOfIdx).UniqueFileName)
                        {
                           cntrResults.get(cntrResult.EmbOfIdx).FileLineChecksum.add(lineIndex, line.hashCode());
                        }
                     }
                     lineIndex++;
                  }
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
            cntrResult.NumLSLOC = lsloc;
            cntrResult.NumExecInstrLog = cntrResult.NumLSLOC;
            cntrResult.NumDataDeclPhys = cntrResult.NumDataDeclLog;
            cntrResult.NumExecInstrPhys = cntrResult.NumPSLOC;
            cntrResult.NumEmbeddedComments = CommentHandler.GetEmbeddedCommentTally();
            cntrResult.NumWholeComments = CommentHandler.GetWholeCommentTally();
            cntrResult.LangVersion = LangProps.GetLangVersion();

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

         // If the _LSLOC file was opened...
         if (bw != null)
         {
            // Close the original file
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

         // Reset handlers
         CommentHandler.Reset();
      }
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
            regEx = "\\<" + arr.get(kw).Keyword + "\\b";
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
}
