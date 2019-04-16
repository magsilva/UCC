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
 * ASPCounter class performs various code counting operations on baseline(s)
 * identified by the user. It contains algorithms/methods to count ASP
 * programming language files.
 *
 * @author Integrity Applications Incorporated
 *
 */
public class ASPCounter extends CodeCounter
{
   /** Instantiate the Log4j2 logger for this class */
   private static final Logger logger = LogManager.getLogger(ASPCounter.class);

   /**
    * Default constructor to instantiate a BashCounter object
    * 
    * @param langProps
    *           Language properties for this counter
    */
   public ASPCounter(LanguageProperties langProps)
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
         logger.debug("Counting PSLOC for " + cntrResults.get(i).FileName);
         // Count PSLOC and complexity
         CountFilePSLOC(cntrResults, i);
         logger.debug("Done\n");

         logger.debug("Counting LSLOC for " + cntrResults.get(i).FileName);
         // Count LSLOC and cyclomatic complexity
         CountFileLSLOC(cntrResults, i);
         logger.debug("Done\n");
      }
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
      String regEx;
      Matcher matcher;

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

         // While we have lines in the file...
         while (line != null)
         {
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
            }

            regEx = "\\bthen\\b";
            matcher = Pattern.compile(regEx).matcher(line);
            if (matcher.find())
            {
               line = line.replaceAll(regEx, "then\n");
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

      // Zero out loop level variables for complexity metrics
      ComplexityObj.loopLevelCount.clear();
      ComplexityObj.loopLevel = 0;

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

            // Delete all compiler directive lines left over from PSLOC
            line = DeleteCompilerDirectives(cntrResult, line);

            if (RtParams.CountCmplxMetrics && !line.isEmpty())
            {
               // Determine complexity loop level
               getLoopLevel(line);
            }

            // Count data declarations
            line = CountDataDeclarations(cntrResult, line);

            // Delete exclude keywords
            line = DeleteExcludeKeywords(line);

            // Delete case statements
            line = DeleteCaseStatements(line);

            // Delete lines that start with "end"
            line = DeleteEndLines(line);

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
                  cntrResults.get(cntrResult.EmbOfIdx).FileLineChecksum.add(lineIndex, tempLine.hashCode());
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
            // Scale complexity loop keyword counts to proper loop levels
            ScaleComplexityLoopLevels(cntrResult);
         }

         // If the _PSLOC file was opened...
         if (reader != null)
         {
            // Save LSLOC metrics counted
            cntrResult.NumLSLOC = cntrResult.NumCompilerDirectives + cntrResult.NumDataDeclLog + lsloc;
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
      static int loopLevel;
   }

   /**
    * 
    */
   protected String CountDataDeclarations(UCCFile cntrResult, String line)
   {
      String regEx;
      Matcher matcher;

      line = line.trim();

      // If line starts with "Dim" and is followed by an "As"
      regEx = "\\b" + "dim" + "\\b";
      matcher = Pattern.compile(regEx).matcher(line);

      // If we found "Dim" at the beginning of the line
      if (matcher.find() && matcher.start() == 0)
      {
         // Increment data declarations
         cntrResult.NumDataDeclLog++;

         // Wipe the line
         line = "";

         lslocLineValue++;
         return line;
      }

      // If line starts with "Const" and is followed by an "As"
      regEx = "\\b" + "const" + "\\b";
      matcher = Pattern.compile(regEx).matcher(line.trim());

      // If we found "Const" at the beginning of the line
      if (matcher.find() && matcher.start() == 0)
      {
         // Increment data declarations
         cntrResult.NumDataDeclLog++;

         // Wipe the line
         line = "";

         lslocLineValue++;
         return line;
      }

      // If line starts with "Static"
      regEx = "\\b" + "static" + "\\b";
      matcher = Pattern.compile(regEx).matcher(line.trim());
      if (matcher.find() && matcher.start() == 0)
      {
         // Increment data declarations
         cntrResult.NumDataDeclLog++;

         // Wipe the line
         line = "";

         lslocLineValue++;
         return line;
      }

      return line;
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
                  && !(line.contains("End") || line.contains("Wend") || line.contains("Loop")))
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
         else if (line.contains("End") || line.contains("Wend") || line.contains("Loop")) // If
                                                                                          // the
                                                                                          // line
                                                                                          // contains
                                                                                          // the
                                                                                          // loop
                                                                                          // end
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

      // Delete lines that start with End
      if (line.trim().startsWith("End"))
      {
         line = "";
      }

      for (int ek = 0; ek < ExcludeKeyword.size() && !line.trim().isEmpty(); ek++)
      {
         regEx = "\\b" + ExcludeKeyword.get(ek) + "\\b";
         line = line.replaceAll(regEx, " ");
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
      // Delete case statements
      if (line.trim().startsWith("case"))
      {
         line = "";
      }

      return line;
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
    * Function for deleting lines that start with "end"
    * 
    * @param line
    *           Line coming in from file
    * @return A blank line if the input line started with "end"
    */
   protected String DeleteEndLines(String line)
   {
      String regEx;
      Matcher matcher;

      line = line.trim();

      regEx = "\\b" + "end" + "\\b";
      matcher = Pattern.compile(regEx).matcher(line);

      // If we find the data keyword at the end of the line
      if (matcher.find() && matcher.start() == 0)
      {
         line = "";
      }

      return line;
   }
}
