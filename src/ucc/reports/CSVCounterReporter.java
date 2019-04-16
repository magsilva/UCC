package ucc.reports;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ucc.datatypes.Constants;
import ucc.datatypes.DataTypes.LanguagePropertiesType;
import ucc.reports.Reporter.ReportType;
import ucc.datatypes.UCCFile;

/**
 * CSVCounterReporter class is derived from CounterReporter class. This class
 * contains methods to print several types of reports for users in CSV format.
 * Reports contain count metrics data.
 *
 * @author Integrity Applications Incorporated
 *
 */
public class CSVCounterReporter extends CounterReporter
{
   /** Instantiate the Log4j2 logger for this class */
   private static final Logger logger = LogManager.getLogger(CSVCounterReporter.class);

   /**
    * Default constructor to instantiate CSVCounterReporter object
    * 
    * @param numLangs
    *           Total number of languages for report generation
    */
   public CSVCounterReporter(int numLangs)
   {
      super(CSV_EXTENSION, numLangs);
   }

   /**
    * Constructor to instantiate CSVCounterReporter object with given parameters
    *
    * @param outFilePrefix
    *           Prefix to prepend to output report file name
    * @param outDir
    *           Directory location to write output reports
    * @param numLangs
    *           Total number of languages for report generation
    */
   public CSVCounterReporter(String outFilePrefix, String outDir, int numLangs)
   {
      super(outFilePrefix, CSV_EXTENSION, outDir, numLangs);
   }

   /*
    * (non-Javadoc)
    * 
    * @see CounterReporter#GenCntrReports(java.util.ArrayList)
    */
   @Override
   public void GenCntrReports(ArrayList<UCCFile> cntrResults)
   {
      // Compute summary data for each language from each file's counter results
      ComputeCntrSummaryResults(cntrResults);

      if (RtParams.UnifyResults)
      {
         // Generate metrics report for all languages in one unified file
         GenUnifiedCntrReport(cntrResults);
      }
      else
      {
         // Generate metrics report for each language in a separate file
         GenLangCntrReport(cntrResults);
      }

      // Generate summary metrics report
      GenSummaryCntrReport(cntrResults);

      // Generate a report for files that were not counted
      GenUncountedReport(cntrResults);

      if (RtParams.SearchForDups && DupResFound)
      {
         // Generate duplicate file pairs report
         GenDupPairsReport(cntrResults);

         if (RtParams.UnifyResults)
         {
            // Generate metrics report for each language for which duplicate
            // files
            // were found
            GenDupUnifiedCntrReport(cntrResults);
         }
         else
         {
            // Generate metrics report for each language for which duplicate
            // files
            // were found
            GenDupLangCntrReport(cntrResults);
         }

         // Generate a summary metrics report for all duplicate files
         GenDupSummaryCntrReport(cntrResults);
      }

      if (RtParams.CountCmplxMetrics)
      {
         // Compute complexity metrics summary for each language
         ComputeCmplxCntrSummaryResults(cntrResults);

         // Generate complexity metrics report
         GenCmplxCntrReport();

         // GenHalsteadCntrReport();

         // Generate Maintainability Index metrics report
         GenMaintainabilityIndexCntrReport(cntrResults);

         // Generate cyclomatic complexity metrics report
         GenCyclCmplxCntrReport(cntrResults);

         if (RtParams.SearchForDups && DupResFound)
         {
            // Generate complexity metrics report for duplicate files
            GenDupCmplxCntrReport();

            // Generate cyclomatic complexity metrics report for duplicate files
            GenDupCyclCmplxCntrReport(cntrResults);
         }
      }
   }

   private void GenMaintainabilityIndexCntrReport(ArrayList<UCCFile> cntrResults)
   {
      // TODO Auto-generated method stub

      String fileName = GetReportFileName(ReportType.MI_COUNT_REPORT);

      try
      {
         // Open output streams for writing
         OutputStreamWriter outStream = new OutputStreamWriter(new FileOutputStream(fileName), Constants.CHARSET_NAME);
         BufferedWriter bufWriter = new BufferedWriter(outStream);

         // Write the file header
         bufWriter.write(GetFileHeader("MAINTAINABILITY INDEX METRICS RESULTS", RtParams.UserInputStr));
         bufWriter.newLine();

         // Write column headers for data
         bufWriter.write(GetHalsteadColHeader());

         String row;

         for (int i = 0; i < cntrResults.size(); i++)
         {
            // Only get data from files that are counted
            if (cntrResults.get(i).LangProperty != null)
            {
               // check whether the file is a duplicate
               if (!cntrResults.get(i).IsDup)
               {
                  // Only get data from files that are counted and are not
                  // duplicates
                  // For files with embedded code, complexity results are stored
                  // in
                  // the embedded files
                  if (cntrResults.get(i).IsCounted && !cntrResults.get(i).HasEmbCode)
                  {
                     row = cntrResults.get(i).CyclCmplxTotal + "," + cntrResults.get(i).V + ","
                              + cntrResults.get(i).maintainabilityIndexNC + ","
                              + cntrResults.get(i).maintainabilityIndex + "," + cntrResults.get(i).LangProperty.name()
                              + "," + cntrResults.get(i).FileName + Constants.NEW_LINE_SEPARATOR;

                     bufWriter.write(row);

                  }
               }
            }
         }

         // Close the output streams
         bufWriter.close();
         logger.debug("Finished writing complexity report to file " + fileName);
      }
      catch (IOException ex)
      {
         // ex.printStackTrace();
         logger.error("Error writing complexity report to file " + fileName);
         logger.debug(ex);
      }

   }

   /**
    * Returns column headers for language specific counter report
    * 
    * @param lang
    *           Name of the language
    * @return A String that contains column header
    */
   private String GetLangColHeader(String lang)
   {
      String hdr = "RESULTS FOR " + lang + " FILES" + Constants.NEW_LINE_SEPARATOR + Constants.NEW_LINE_SEPARATOR
               + "Total,Blank,Comments,,Compiler,Data,Exec.,Logical,Physical,File,Module" + Constants.NEW_LINE_SEPARATOR
               + "Lines,Lines,Whole,Embedded,Direct.,Decl.,Instr.,SLOC,SLOC,Type,Name" + Constants.NEW_LINE_SEPARATOR;

      return hdr;
   }

   /**
    * Returns column header for summary section of language specific counter
    * report
    * 
    * @param addlInfo
    *           Additional info to add to summary title
    * 
    * @return A String that contains summary column header
    */
   private String GetLangSummaryColHeader(String addlInfo)
   {
      String hdr = "RESULTS SUMMARY" + addlInfo + Constants.NEW_LINE_SEPARATOR + Constants.NEW_LINE_SEPARATOR
               + "Total,Blank,Comments,,Compiler,Data,Exec.,,File,SLOC" + Constants.NEW_LINE_SEPARATOR
               + "Lines,Lines,Whole,Embedded,Direct.,Decl.,Instr.,SLOC,Type,Definition" + Constants.NEW_LINE_SEPARATOR;

      return hdr;
   }

   /**
    * Returns column header for summary counter report
    * 
    * @return A string that contains column header
    */
   private String GetSummaryColHeader()
   {
      String hdr = "Language,Number,Physical,Logical" + Constants.NEW_LINE_SEPARATOR + "Name,of Files,SLOC,SLOC"
               + Constants.NEW_LINE_SEPARATOR;

      return hdr;
   }

   /**
    * Returns column header for complexity report
    * 
    * @param numLvl
    *           Number of maximum loop levels
    * 
    * @return A string that contains column header
    */
   private String GetCmplxColHeader(int numLvl)
   {
      String hdr =
               "Math,Trigonometric,Logarithmic,Calculations,Conditionals," + "Logical,Preprocessor,Assignment,Pointer";

      // Add loop level column headers
      for (int i = 0; i < numLvl; i++)
      {
         hdr += ",L" + String.format("%d", i + 1) + "-Loops";

      }

      hdr += ",Language,File Name" + Constants.NEW_LINE_SEPARATOR;

      return hdr;
   }

   /**
    * Returns column header for complexity report
    * 
    * @param numLvl
    *           Number of maximum loop levels
    * 
    * @return A string that contains column header
    */
   private String GetHalsteadColHeader()
   {
      String hdr = "Cyclomatic Complexity,Halstead Volume,MI without Comments,MI with Comments,Language," + "Filename";

      hdr += Constants.NEW_LINE_SEPARATOR;

      return hdr;
   }

   /**
    * Returns column headers for unified counter report
    * 
    * @param lang
    *           Name of the language
    * @return A String that contains column header
    */
   private String GetUnifiedColHeader(String lang)
   {
      String hdr = "RESULTS FOR " + lang + " FILES" + Constants.NEW_LINE_SEPARATOR
               + "Total,Blank,Comments,,Compiler,Data,Exec.,Logical,Physical,File,Module" + Constants.NEW_LINE_SEPARATOR
               + "Lines,Lines,Whole,Embedded,Direct.,Decl.,Instr.,SLOC,SLOC,Type,Name" + Constants.NEW_LINE_SEPARATOR;

      return hdr;
   }

   /**
    * Returns column header for summary section of unified counter report
    * 
    * @param addlInfo
    *           Additional info to add to summary title
    * 
    * @return A String that contains summary column header
    */
   private String GetUnifiedSummaryColHeader(String addlInfo)
   {
      String hdr = "RESULTS SUMMARY" + addlInfo + Constants.NEW_LINE_SEPARATOR
               + "Total,Blank,Comments,,Compiler,Data,Exec.,,File,SLOC" + Constants.NEW_LINE_SEPARATOR
               + "Lines,Lines,Whole,Embedded,Direct.,Decl.,Instr.,SLOC,Type,Definition" + Constants.NEW_LINE_SEPARATOR;

      return hdr;
   }

   /*
    * (non-Javadoc)
    * 
    * @see CounterReporter#GenLangCntrReport()
    */
   @Override
   protected void GenLangCntrReport(ArrayList<UCCFile> cntrResults)
   {
      // Check if any counter data is available for reporting
      if (!CntrSummaryResults.isEmpty())
      {
         for (int i = 0; i < NumLangs; i++)
         {
            if (CntrSummaryResults.get(i).NumCountedFiles > 0)
            {
               // Get language name, which will be used to form report file name
               String lang = LanguagePropertiesType.values()[i].toString();
               String fileName = GetReportFileName(ReportType.LANG_COUNT_REPORT, lang);
               String row;

               try
               {
                  // Open output streams for writing
                  OutputStreamWriter outStream =
                           new OutputStreamWriter(new FileOutputStream(fileName), Constants.CHARSET_NAME);
                  BufferedWriter bufWriter = new BufferedWriter(outStream);

                  // Write the file header
                  bufWriter.write(GetFileHeader("SLOC COUNT RESULTS", RtParams.UserInputStr));
                  bufWriter.newLine();

                  // Write language version number on which counting rules are
                  // based
                  bufWriter.write("Counting rules based on " + lang + " Version: "
                           + CntrSummaryResults.get(i).LangVersion + Constants.NEW_LINE_SEPARATOR);
                  bufWriter.newLine();

                  // Write column headers for data
                  bufWriter.write(GetLangColHeader(lang));

                  for (int j = 0; j < cntrResults.size(); j++)
                  {
                     if (cntrResults.get(j).LangProperty != null && cntrResults.get(j).LangProperty.name() == lang)
                     {
                        if (!cntrResults.get(j).IsDup && cntrResults.get(j).IsCounted && !cntrResults.get(j).HasEmbCode)
                        {
                           // Write count data for each file
                           row = cntrResults.get(j).NumTotalLines + "," + cntrResults.get(j).NumBlankLines + ","
                                    + cntrResults.get(j).NumWholeComments + "," + cntrResults.get(j).NumEmbeddedComments
                                    + "," + cntrResults.get(j).NumCompilerDirectives + ","
                                    + cntrResults.get(j).NumDataDeclLog + "," + cntrResults.get(j).NumExecInstrLog + ","
                                    + cntrResults.get(j).NumLSLOC + "," + cntrResults.get(j).NumPSLOC + ","
                                    + cntrResults.get(j).FileType.toString() + "," + cntrResults.get(j).FileName
                                    + Constants.NEW_LINE_SEPARATOR;
                           bufWriter.write(row);
                        }
                     }
                  }

                  // Write summary block header for the language
                  bufWriter.newLine();
                  bufWriter.write(GetLangSummaryColHeader(""));

                  // Write summary block data for the language
                  row = CntrSummaryResults.get(i).NumTotalLines + "," + CntrSummaryResults.get(i).NumBlankLines + ","
                           + CntrSummaryResults.get(i).NumWholeCmnts + "," + CntrSummaryResults.get(i).NumEmbeddedCmnts
                           + "," + CntrSummaryResults.get(i).NumCompDirs + ","
                           + CntrSummaryResults.get(i).NumDataDeclPhys + ","
                           + CntrSummaryResults.get(i).NumExecInstrPhys + "," + CntrSummaryResults.get(i).NumPSLOC
                           + ",CODE,Physical" + Constants.NEW_LINE_SEPARATOR;
                  bufWriter.write(row);

                  row = CntrSummaryResults.get(i).NumTotalLines + "," + CntrSummaryResults.get(i).NumBlankLines + ","
                           + CntrSummaryResults.get(i).NumWholeCmnts + "," + CntrSummaryResults.get(i).NumEmbeddedCmnts
                           + "," + CntrSummaryResults.get(i).NumCompDirs + ","
                           + CntrSummaryResults.get(i).NumDataDeclLog + "," + CntrSummaryResults.get(i).NumExecInstrLog
                           + "," + CntrSummaryResults.get(i).NumLSLOC + ",CODE,Logical" + Constants.NEW_LINE_SEPARATOR;
                  bufWriter.write(row);

                  // Write file access/successful count info
                  row = "Number of files successfully accessed," + CntrSummaryResults.get(i).NumCountedFiles
                           + ",out of," + CntrSummaryResults.get(i).NumTotalFiles + Constants.NEW_LINE_SEPARATOR;
                  bufWriter.newLine();
                  bufWriter.write(row);

                  // Write PLSOC to SLOC ratio
                  row = "Ratio of Physical to Logical SLOC," + String.format("%.2f",
                           ComputeRatio(CntrSummaryResults.get(i).NumPSLOC, CntrSummaryResults.get(i).NumLSLOC))
                           + Constants.NEW_LINE_SEPARATOR;
                  bufWriter.newLine();
                  bufWriter.write(row);

                  // Write individual complexity keywords for each category and
                  // their respective counts to the report
                  WriteLogicalKeywordCounts(bufWriter, CntrSummaryResults, lang, i);

                  // Close the output streams
                  bufWriter.close();
                  logger.debug("Finished writing counter report for " + lang + " to file " + fileName);
               }
               catch (IOException ex)
               {
                  // ex.printStackTrace();
                  logger.error("Error writing counter report for " + lang + " to file " + fileName);
                  logger.debug(ex);
               }
            }
         }
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see CounterReporter#GenUnifiedCntrReport()
    */
   @Override
   protected void GenUnifiedCntrReport(ArrayList<UCCFile> cntrResults)
   {
      // Check if any counter data is available for reporting
      if (!cntrResults.isEmpty())
      {
         // Get report file name
         String fileName = GetReportFileName(ReportType.UNIFIED_COUNT_REPORT);
         String row;

         try
         {
            // Open output streams for writing
            OutputStreamWriter outStream =
                     new OutputStreamWriter(new FileOutputStream(fileName), Constants.CHARSET_NAME);
            BufferedWriter bufWriter = new BufferedWriter(outStream);

            // Sum up counts for embedded languages
            SumEmbeddedLanguageCounts(cntrResults);

            // Write the file header
            bufWriter.write(GetFileHeader("SLOC COUNT RESULTS FOR ALL SOURCE FILES", RtParams.UserInputStr));
            bufWriter.newLine();
            bufWriter.newLine();

            // Write column headers for data
            bufWriter.write(GetUnifiedColHeader("ALL NON-WEB LANGUAGE"));

            for (int j = 0; j < cntrResults.size(); j++)
            {
               if (!cntrResults.get(j).IsDup && cntrResults.get(j).IsCounted && cntrResults.get(j).EmbOfIdx == -1)
               {
                  // Write count data for each file
                  row = cntrResults.get(j).NumTotalLines + "," + cntrResults.get(j).NumBlankLines + ","
                           + cntrResults.get(j).NumWholeComments + "," + cntrResults.get(j).NumEmbeddedComments + ","
                           + cntrResults.get(j).NumCompilerDirectives + "," + cntrResults.get(j).NumDataDeclLog + ","
                           + cntrResults.get(j).NumExecInstrLog + "," + cntrResults.get(j).NumLSLOC + ","
                           + cntrResults.get(j).NumPSLOC + "," + cntrResults.get(j).FileType.toString() + ","
                           + cntrResults.get(j).FileName + Constants.NEW_LINE_SEPARATOR;
                  bufWriter.write(row);
               }
            }

            // Write summary block header
            bufWriter.newLine();
            bufWriter.write(GetUnifiedSummaryColHeader(" FOR NON-WEB LANGUAGES"));

            int numCountedFiles = 0;
            int numTotalFiles = 0;
            int totalPSLOC = 0;
            int totalLSLOC = 0;
            int totalLines = 0;
            int totalBlankLines = 0;
            int totalWholeCmnts = 0;
            int totalEmbeddedCmnts = 0;
            int totalCompDirs = 0;
            int totalDataDeclPhys = 0;
            int totalExecInstrPhys = 0;
            int totalDataDeclLog = 0;
            int totalExecInstrLog = 0;

            // Write summary data for all languages
            if (!CntrSummaryResults.isEmpty())
            {
               for (int i = 0; i < NumLangs; i++)
               {
                  if (CntrSummaryResults.get(i).NumCountedFiles > 0)
                  {
                     totalLines += CntrSummaryResults.get(i).NumTotalLines;
                     totalBlankLines += CntrSummaryResults.get(i).NumBlankLines;
                     totalWholeCmnts += CntrSummaryResults.get(i).NumWholeCmnts;
                     totalEmbeddedCmnts += CntrSummaryResults.get(i).NumEmbeddedCmnts;
                     totalCompDirs += CntrSummaryResults.get(i).NumCompDirs;
                     totalDataDeclPhys += CntrSummaryResults.get(i).NumDataDeclPhys;
                     totalExecInstrPhys += CntrSummaryResults.get(i).NumExecInstrPhys;
                     totalDataDeclLog += CntrSummaryResults.get(i).NumDataDeclLog;
                     totalExecInstrLog += CntrSummaryResults.get(i).NumExecInstrLog;
                     totalPSLOC += CntrSummaryResults.get(i).NumPSLOC;
                     totalLSLOC += CntrSummaryResults.get(i).NumLSLOC;
                  }

                  numCountedFiles += CntrSummaryResults.get(i).NumCountedFiles;
                  numTotalFiles += CntrSummaryResults.get(i).NumTotalFiles;
               }
            }

            // Write summary block data for the language
            row = totalLines + "," + totalBlankLines + "," + totalWholeCmnts + "," + totalEmbeddedCmnts + ","
                     + totalCompDirs + "," + totalDataDeclPhys + "," + totalExecInstrPhys + "," + totalPSLOC
                     + ",CODE,Physical" + Constants.NEW_LINE_SEPARATOR;
            bufWriter.write(row);

            row = totalLines + "," + totalBlankLines + "," + totalWholeCmnts + "," + totalEmbeddedCmnts + ","
                     + totalCompDirs + "," + totalDataDeclLog + "," + totalExecInstrLog + "," + totalLSLOC
                     + ",CODE,Logical" + Constants.NEW_LINE_SEPARATOR;
            bufWriter.write(row);

            // Write file access/successful count info
            row = "Number of files successfully accessed," + numCountedFiles + ",out of," + numTotalFiles
                     + Constants.NEW_LINE_SEPARATOR;
            bufWriter.newLine();
            bufWriter.write(row);

            // Write PLSOC to SLOC ratio
            row = "Ratio of Physical to Logical SLOC," + String.format("%.2f", ComputeRatio(totalPSLOC, totalLSLOC))
                     + Constants.NEW_LINE_SEPARATOR;
            bufWriter.newLine();
            bufWriter.write(row);

            // Write individual complexity keywords for each category and
            // their respective counts to the report
            if (!CntrSummaryResults.isEmpty())
            {
               for (int i = 0; i < NumLangs; i++)
               {
                  // Get language name
                  String lang = LanguagePropertiesType.values()[i].toString();

                  if (CntrSummaryResults.get(i).NumCountedFiles > 0)
                  {
                     WriteLogicalKeywordCounts(bufWriter, CntrSummaryResults, lang, i);
                  }
               }
            }

            // Close the output streams
            bufWriter.close();
            logger.debug("Finished writing unified counter report to file " + fileName);
         }
         catch (IOException ex)
         {
            logger.error("Error writing unified counter report to file " + fileName);
            logger.debug(ex);
         }
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see CounterReporter#GenSummaryCntrReport()
    */
   @Override
   protected void GenSummaryCntrReport(ArrayList<UCCFile> cntrResults)
   {
      // Check if any counter data is available for reporting
      if (!CntrSummaryResults.isEmpty())
      {
         // Get report file name
         String fileName = GetReportFileName(ReportType.SUMMARY_COUNT_REPORT);
         try
         {
            // Open output streams for writing
            OutputStreamWriter outStream =
                     new OutputStreamWriter(new FileOutputStream(fileName), Constants.CHARSET_NAME);
            BufferedWriter bufWriter = new BufferedWriter(outStream);

            // Write the file header
            bufWriter.write(GetFileHeader("LANGUAGE COUNT SUMMARY", RtParams.UserInputStr));
            bufWriter.newLine();

            // Write column headers for data
            bufWriter.write(GetSummaryColHeader());

            String row;
            int totalFiles = 0;
            int totalPSLOC = 0;
            int totalLSLOC = 0;
            for (int i = 0; i < CntrSummaryResults.size(); i++)
            {
               if (CntrSummaryResults.get(i).NumCountedFiles > 0)
               {
                  // Write summary of count data for each language
                  row = LanguagePropertiesType.values()[i].toString() + "," + CntrSummaryResults.get(i).NumCountedFiles
                           + "," + CntrSummaryResults.get(i).NumPSLOC + "," + CntrSummaryResults.get(i).NumLSLOC
                           + Constants.NEW_LINE_SEPARATOR;
                  bufWriter.write(row);

                  // Compute totals for writing to the report
                  totalFiles += CntrSummaryResults.get(i).NumCountedFiles;
                  totalPSLOC += CntrSummaryResults.get(i).NumPSLOC;
                  totalLSLOC += CntrSummaryResults.get(i).NumLSLOC;
               }
            }

            // Write a blank line
            bufWriter.newLine();

            // Write totals to the report
            row = "Total," + totalFiles + "," + totalPSLOC + "," + totalLSLOC + Constants.NEW_LINE_SEPARATOR;
            bufWriter.write(row);

            // Close the output streams
            bufWriter.close();
            logger.debug("Finished writing summary counter report to file " + fileName);
         }
         catch (IOException ex)
         {
            logger.error("Error writing summary counter report to file " + fileName);
            logger.debug(ex);
         }
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see CounterReporter#GenLangCntrReport()
    */
   @Override
   protected void GenUncountedReport(ArrayList<UCCFile> cntrResults)
   {
      // Check if any counter data is available for reporting
      if (!CntrSummaryResults.isEmpty() && UncountedFilesFound)
      {
         // Get report file name
         String fileName = GetReportFileName(ReportType.UNCOUNTED_FILE_REPORT);
         try
         {
            // Open output streams for writing
            OutputStreamWriter outStream =
                     new OutputStreamWriter(new FileOutputStream(fileName), Constants.CHARSET_NAME);
            BufferedWriter bufWriter = new BufferedWriter(outStream);

            // Write the file header
            bufWriter.write(GetFileHeader("UNCOUNTED FILES", RtParams.UserInputStr));
            bufWriter.newLine();

            // Write column headers for data
            bufWriter.write("Message,Uncounted File" + Constants.NEW_LINE_SEPARATOR);

            String row;
            for (int i = 0; i < cntrResults.size(); i++)
            {
               // Report any files that were not counted due to not being mapped
               // to a language properties
               if (cntrResults.get(i).LangProperty == null)
               {
                  row = "Unknown File Extension," + cntrResults.get(i).FileName + Constants.NEW_LINE_SEPARATOR;
                  bufWriter.write(row);
               }
               // Report any files that were not counted by the code counter
               else if (!cntrResults.get(i).IsDup && !cntrResults.get(i).IsCounted)
               {
                  row = "Error counting the file," + cntrResults.get(i).FileName + Constants.NEW_LINE_SEPARATOR;
                  bufWriter.write(row);
               }
            }

            // Close the output streams
            bufWriter.close();
            logger.debug("Finished writing uncounted files report to file " + fileName);
         }
         catch (IOException ex)
         {
            logger.error("Error writing uncounted files report to file " + fileName);
            logger.debug(ex);
         }
      }
   }

   /**
    * Write each complexity keyword's total count to the buffered writer
    * 
    * @param bufWriter
    *           A buffered writer object
    * @param cntrResSum
    *           An object that contains counter summary metrics
    */
   private void WriteCmplxKeywordCounts(BufferedWriter bufWriter, ArrayList<CounterResultSummary> cntrResSum)
   {
      if (bufWriter != null)
      {
         // Loop through each language type to get complexity keywords and
         // respective counts
         for (int i = 0; i < NumLangs; i++)
         {
            // Add complexity keyword counts to the report only if files of that
            // language were counted
            if (cntrResSum.get(i).NumCountedFiles > 0)
            {
               // Get language name, which will be used to form report file name
               String lang = LanguagePropertiesType.values()[i].toString();
               String row;

               try
               {
                  if (!cntrResSum.get(i).CmplxTrigCnts.isEmpty() || !cntrResSum.get(i).CmplxLogCnts.isEmpty()
                           || !cntrResSum.get(i).CmplxCalcCnts.isEmpty() || !cntrResSum.get(i).CmplxMathCnts.isEmpty()
                           || !cntrResSum.get(i).CmplxCondCnts.isEmpty() || !cntrResSum.get(i).CmplxLogicCnts.isEmpty()
                           || !cntrResSum.get(i).CmplxPreprocCnts.isEmpty()
                           || !cntrResSum.get(i).CmplxAssignCnts.isEmpty())
                  {
                     // Write the section header
                     bufWriter.newLine();
                     bufWriter.write(
                              "TOTAL OCCURRENCES OF " + lang + " COMPLEXITY COUNTS" + Constants.NEW_LINE_SEPARATOR);
                     bufWriter.write("Math Functions,,Trigonometric,,Logarithmic,,Calculations,,Conditionals,,"
                              + "Logical,,Preprocessor,,Assignment,,Pointer" + Constants.NEW_LINE_SEPARATOR);

                     int mathIdx = 0;
                     int trigIdx = 0;
                     int logIdx = 0;
                     int calcIdx = 0;
                     int condIdx = 0;
                     int logicIdx = 0;
                     int preprocIdx = 0;
                     int assignIdx = 0;
                     int pntrIdx = 0;

                     // Write count data for each language
                     while (mathIdx < cntrResSum.get(i).CmplxMathCnts.size()
                              || trigIdx < cntrResSum.get(i).CmplxTrigCnts.size()
                              || logIdx < cntrResSum.get(i).CmplxLogCnts.size()
                              || calcIdx < cntrResSum.get(i).CmplxCalcCnts.size()
                              || condIdx < cntrResSum.get(i).CmplxCondCnts.size()
                              || logicIdx < cntrResSum.get(i).CmplxLogicCnts.size()
                              || preprocIdx < cntrResSum.get(i).CmplxPreprocCnts.size()
                              || assignIdx < cntrResSum.get(i).CmplxAssignCnts.size()
                              || pntrIdx < cntrResSum.get(i).CmplxPntrCnts.size())
                     {
                        row = "";

                        if (mathIdx < cntrResSum.get(i).CmplxMathCnts.size())
                        {
                           row += cntrResSum.get(i).CmplxMathCnts.get(mathIdx).Keyword + ","
                                    + cntrResSum.get(i).CmplxMathCnts.get(mathIdx).Count;
                           mathIdx++;
                        }
                        else
                        {
                           row += ",";
                        }

                        if (trigIdx < cntrResSum.get(i).CmplxTrigCnts.size())
                        {
                           row += "," + cntrResSum.get(i).CmplxTrigCnts.get(trigIdx).Keyword + ","
                                    + cntrResSum.get(i).CmplxTrigCnts.get(trigIdx).Count;
                           trigIdx++;
                        }
                        else
                        {
                           row += ",,";
                        }

                        if (logIdx < cntrResSum.get(i).CmplxLogCnts.size())
                        {
                           row += "," + cntrResSum.get(i).CmplxLogCnts.get(logIdx).Keyword + ","
                                    + cntrResSum.get(i).CmplxLogCnts.get(logIdx).Count;
                           logIdx++;
                        }
                        else
                        {
                           row += ",,";
                        }

                        if (calcIdx < cntrResSum.get(i).CmplxCalcCnts.size())
                        {
                           row += "," + cntrResSum.get(i).CmplxCalcCnts.get(calcIdx).Keyword + ","
                                    + cntrResSum.get(i).CmplxCalcCnts.get(calcIdx).Count;
                           calcIdx++;
                        }
                        else
                        {
                           row += ",,";
                        }

                        if (condIdx < cntrResSum.get(i).CmplxCondCnts.size())
                        {
                           row += "," + cntrResSum.get(i).CmplxCondCnts.get(condIdx).Keyword + ","
                                    + cntrResSum.get(i).CmplxCondCnts.get(condIdx).Count;
                           condIdx++;
                        }
                        else
                        {
                           row += ",,";
                        }

                        if (logicIdx < cntrResSum.get(i).CmplxLogicCnts.size())
                        {
                           row += "," + cntrResSum.get(i).CmplxLogicCnts.get(logicIdx).Keyword + ","
                                    + cntrResSum.get(i).CmplxLogicCnts.get(logicIdx).Count;
                           logicIdx++;
                        }
                        else
                        {
                           row += ",,";
                        }

                        if (preprocIdx < cntrResSum.get(i).CmplxPreprocCnts.size())
                        {
                           row += "," + cntrResSum.get(i).CmplxPreprocCnts.get(preprocIdx).Keyword + ","
                                    + cntrResSum.get(i).CmplxPreprocCnts.get(preprocIdx).Count;
                           preprocIdx++;
                        }
                        else
                        {
                           row += ",,";
                        }

                        if (assignIdx < cntrResSum.get(i).CmplxAssignCnts.size())
                        {
                           row += "," + cntrResSum.get(i).CmplxAssignCnts.get(assignIdx).Keyword + ","
                                    + cntrResSum.get(i).CmplxAssignCnts.get(assignIdx).Count;
                           assignIdx++;
                        }
                        else
                        {
                           row += ",,";
                        }

                        if (pntrIdx < cntrResSum.get(i).CmplxPntrCnts.size())
                        {
                           row += "," + cntrResSum.get(i).CmplxPntrCnts.get(pntrIdx).Keyword + ","
                                    + cntrResSum.get(i).CmplxPntrCnts.get(pntrIdx).Count;
                           pntrIdx++;
                        }
                        else
                        {
                           row += ",,";
                        }

                        bufWriter.write(row);
                        bufWriter.newLine();
                     }
                  }
               }
               catch (IOException ex)
               {
                  logger.error("Error writing complexity keyword counts to report");
                  logger.debug(ex);
               }
            }
         }
      }
   }

   /**
    * Write logical keywords counts to the buffered writer
    * 
    * @param bufWriter
    *           A buffered writer object
    * @param cntrResSum
    *           An object that contains counter summary metrics
    * @param lang
    *           Language name
    * @param i
    *           Index into CounterResultSummary object
    */
   private void WriteLogicalKeywordCounts(BufferedWriter bufWriter, ArrayList<CounterResultSummary> cntrResSum,
            String lang, int i)
   {
      if (bufWriter != null)
      {
         // Add logical keyword counts to the report only if files of that
         // language were counted
         if (cntrResSum.get(i).NumCountedFiles > 0)
         {
            String row;

            try
            {
               // Write the section header
               bufWriter.newLine();
               bufWriter.write("TOTAL OCCURRENCES OF " + lang + " KEYWORDS" + Constants.NEW_LINE_SEPARATOR);
               bufWriter.write(
                        "Compiler Directives,,Data Keywords,,Executable Keywords" + Constants.NEW_LINE_SEPARATOR);

               int preprocIdx = 0;
               int dataIdx = 0;
               int execIdx = 0;

               // Write count data for each language
               while (preprocIdx < cntrResSum.get(i).CmplxPreprocCnts.size()
                        || dataIdx < cntrResSum.get(i).DataKeywrdCnts.size()
                        || execIdx < cntrResSum.get(i).ExecKeywrdCnts.size())
               {
                  row = "";

                  if (preprocIdx < cntrResSum.get(i).CmplxPreprocCnts.size())
                  {
                     row += cntrResSum.get(i).CmplxPreprocCnts.get(preprocIdx).Keyword + ","
                              + cntrResSum.get(i).CmplxPreprocCnts.get(preprocIdx).Count;
                     preprocIdx++;
                  }
                  else
                  {
                     row += ",";
                  }

                  if (dataIdx < cntrResSum.get(i).DataKeywrdCnts.size())
                  {
                     row += "," + cntrResSum.get(i).DataKeywrdCnts.get(dataIdx).Keyword + ","
                              + cntrResSum.get(i).DataKeywrdCnts.get(dataIdx).Count;
                     dataIdx++;
                  }
                  else
                  {
                     row += ",,";
                  }

                  if (execIdx < cntrResSum.get(i).ExecKeywrdCnts.size())
                  {
                     row += "," + cntrResSum.get(i).ExecKeywrdCnts.get(execIdx).Keyword + ","
                              + cntrResSum.get(i).ExecKeywrdCnts.get(execIdx).Count;
                     execIdx++;
                  }
                  else
                  {
                     row += ",,";
                  }

                  bufWriter.write(row);
                  bufWriter.newLine();
               }
            }
            catch (IOException ex)
            {
               // ex.printStackTrace();
               logger.error("Error writing logical keywords to report");
               logger.debug(ex);
            }
         }

      }
   }

   /**
    * Loops through all the loop level count data to find the highest number of
    * loop level
    * 
    * @param cmplxResSum
    *           A results summary object containing complexity metrics
    * @return Maximum loop level
    */
   private int GetMaxLoopLvl(ArrayList<ComplexCounterResultsSummary> cmplxResSum)
   {
      int maxLvl = 0;

      if (cmplxResSum != null)
      {
         for (int i = 0; i < cmplxResSum.size(); i++)
         {
            if (maxLvl < cmplxResSum.get(i).LoopLvlCnts.size())
            {
               maxLvl = cmplxResSum.get(i).LoopLvlCnts.size();
            }
         }
      }

      return maxLvl;
   }

   /*
    * (non-Javadoc)
    * 
    * @see CounterReporter#GenCmplxCntrReport()
    */
   @Override
   protected void GenCmplxCntrReport()
   {
      // Check if any counter data is available for reporting
      if (!CmplxCntrSummaryResults.isEmpty())
      {
         String fileName = GetReportFileName(ReportType.CMPLX_COUNT_REPORT);

         try
         {
            // Open output streams for writing
            OutputStreamWriter outStream =
                     new OutputStreamWriter(new FileOutputStream(fileName), Constants.CHARSET_NAME);
            BufferedWriter bufWriter = new BufferedWriter(outStream);

            // Write the file header
            bufWriter.write(GetFileHeader("COMPLEXITY COUNT RESULTS", RtParams.UserInputStr));
            bufWriter.newLine();

            // Write column headers for data
            int maxLoopLvl = GetMaxLoopLvl(CmplxCntrSummaryResults);
            bufWriter.write(GetCmplxColHeader(maxLoopLvl));

            int totalMathOps = 0;
            int totalTrigOps = 0;
            int totalLogOps = 0;
            int totalCalcOps = 0;
            int totalCondStmts = 0;
            int totalLogicOps = 0;
            int totalPreprocs = 0;
            int totalAssignOps = 0;
            int totalPntrOps = 0;
            String row;
            for (int i = 0; i < CmplxCntrSummaryResults.size(); i++)
            {
               row = CmplxCntrSummaryResults.get(i).NumMathOps + "," + CmplxCntrSummaryResults.get(i).NumTrigOps + ","
                        + CmplxCntrSummaryResults.get(i).NumLogOps + "," + CmplxCntrSummaryResults.get(i).NumCalcOps
                        + "," + CmplxCntrSummaryResults.get(i).NumCondStmts + ","
                        + CmplxCntrSummaryResults.get(i).NumLogicOps + "," + CmplxCntrSummaryResults.get(i).NumPreprocs
                        + "," + CmplxCntrSummaryResults.get(i).NumAssignOps + ","
                        + CmplxCntrSummaryResults.get(i).NumPntrOps + ",";

               for (int j = 0; j < maxLoopLvl; j++)
               {
                  if (j < CmplxCntrSummaryResults.get(i).LoopLvlCnts.size())
                  {
                     row += CmplxCntrSummaryResults.get(i).LoopLvlCnts.get(j).toString() + ",";
                  }
                  else
                  {
                     row += "0,";
                  }
               }

               row += CmplxCntrSummaryResults.get(i).LangName + ",";
               row += CmplxCntrSummaryResults.get(i).FileName + Constants.NEW_LINE_SEPARATOR;
               bufWriter.write(row);

               // Compute totals for all files
               totalMathOps += CmplxCntrSummaryResults.get(i).NumMathOps;
               totalTrigOps += CmplxCntrSummaryResults.get(i).NumTrigOps;
               totalLogOps += CmplxCntrSummaryResults.get(i).NumLogOps;
               totalCalcOps += CmplxCntrSummaryResults.get(i).NumCalcOps;
               totalCondStmts += CmplxCntrSummaryResults.get(i).NumCondStmts;
               totalLogicOps += CmplxCntrSummaryResults.get(i).NumLogicOps;
               totalPreprocs += CmplxCntrSummaryResults.get(i).NumPreprocs;
               totalAssignOps += CmplxCntrSummaryResults.get(i).NumAssignOps;
               totalPntrOps += CmplxCntrSummaryResults.get(i).NumPntrOps;
            }

            // Write summary block header for the complexity counts
            bufWriter.newLine();
            bufWriter.write("Totals" + Constants.NEW_LINE_SEPARATOR);

            // Write summary block data for the complexity counts
            row = totalMathOps + "," + totalTrigOps + "," + totalLogOps + "," + totalCalcOps + "," + totalCondStmts
                     + "," + totalLogicOps + "," + totalPreprocs + "," + totalAssignOps + "," + totalPntrOps
                     + Constants.NEW_LINE_SEPARATOR;
            bufWriter.write(row);

            // Write individual complexity keywords for each category and their
            // respective counts to the report
            WriteCmplxKeywordCounts(bufWriter, CntrSummaryResults);

            // Close the output streams
            bufWriter.close();
            logger.debug("Finished writing complexity report to file " + fileName);
         }
         catch (IOException ex)
         {
            // ex.printStackTrace();
            logger.error("Error writing complexity report to file " + fileName);
            logger.debug(ex);
         }
      }
   }

   @Override
   protected void GenHalsteadCntrReport()
   {
      // TODO Auto-generated method stub

      // Check if any counter data is available for reporting
      if (!CmplxCntrSummaryResults.isEmpty())
      {
         String fileName = GetReportFileName(ReportType.MI_COUNT_REPORT);

         try
         {
            // Open output streams for writing
            OutputStreamWriter outStream =
                     new OutputStreamWriter(new FileOutputStream(fileName), Constants.CHARSET_NAME);
            BufferedWriter bufWriter = new BufferedWriter(outStream);

            // Write the file header
            bufWriter.write(GetFileHeader("HALSTEAD COUNT RESULTS", RtParams.UserInputStr));
            bufWriter.newLine();

            // Write column headers for data
            bufWriter.write(GetHalsteadColHeader());

            int totalMathOps = 0;
            int totalTrigOps = 0;
            int totalLogOps = 0;
            int totalCalcOps = 0;
            int totalCondStmts = 0;
            int totalLogicOps = 0;
            int totalPreprocs = 0;
            int totalAssignOps = 0;
            int totalPntrOps = 0;
            int totalDataOps = 0;
            int totalExecOps = 0;

            String row;
            for (int i = 0; i < CmplxCntrSummaryResults.size(); i++)
            {
               row = CmplxCntrSummaryResults.get(i).NumMathOps + "," + CmplxCntrSummaryResults.get(i).NumTrigOps + ","
                        + CmplxCntrSummaryResults.get(i).NumLogOps + "," + CmplxCntrSummaryResults.get(i).NumCalcOps
                        + "," + CmplxCntrSummaryResults.get(i).NumCondStmts + ","
                        + CmplxCntrSummaryResults.get(i).NumLogicOps + "," + CmplxCntrSummaryResults.get(i).NumPreprocs
                        + "," + CmplxCntrSummaryResults.get(i).NumAssignOps + ","
                        + CmplxCntrSummaryResults.get(i).NumPntrOps + ",";

               row += CmplxCntrSummaryResults.get(i).NumDataOps + ",";
               row += CmplxCntrSummaryResults.get(i).NumExecOps + ",";

               row += CmplxCntrSummaryResults.get(i).NumBoolOperands + ",";

               row += CmplxCntrSummaryResults.get(i).NumUniqueAndTotalOperators[0] + ",";
               row += CmplxCntrSummaryResults.get(i).NumUniqueAndTotalOperators[1] + ",";

               row += CmplxCntrSummaryResults.get(i).LangName + ",";
               row += CmplxCntrSummaryResults.get(i).FileName + Constants.NEW_LINE_SEPARATOR;
               bufWriter.write(row);

               // Compute totals for all files
               totalMathOps += CmplxCntrSummaryResults.get(i).NumMathOps;
               totalTrigOps += CmplxCntrSummaryResults.get(i).NumTrigOps;
               totalLogOps += CmplxCntrSummaryResults.get(i).NumLogOps;
               totalCalcOps += CmplxCntrSummaryResults.get(i).NumCalcOps;
               totalCondStmts += CmplxCntrSummaryResults.get(i).NumCondStmts;
               totalLogicOps += CmplxCntrSummaryResults.get(i).NumLogicOps;
               totalPreprocs += CmplxCntrSummaryResults.get(i).NumPreprocs;
               totalAssignOps += CmplxCntrSummaryResults.get(i).NumAssignOps;
               totalPntrOps += CmplxCntrSummaryResults.get(i).NumPntrOps;
               totalDataOps += CmplxCntrSummaryResults.get(i).NumDataOps;
               totalExecOps += CmplxCntrSummaryResults.get(i).NumExecOps;
            }

            // Write summary block header for the complexity counts
            bufWriter.newLine();
            bufWriter.write("Totals" + Constants.NEW_LINE_SEPARATOR);

            // Write summary block data for the complexity counts
            row = totalMathOps + "," + totalTrigOps + "," + totalLogOps + "," + totalCalcOps + "," + totalCondStmts
                     + "," + totalLogicOps + "," + totalPreprocs + "," + totalAssignOps + "," + totalPntrOps + ","
                     + totalDataOps + "," + totalExecOps + Constants.NEW_LINE_SEPARATOR;
            bufWriter.write(row);

            // Write individual complexity keywords for each category and their
            // respective counts to the report
            WriteCmplxKeywordCounts(bufWriter, CntrSummaryResults);

            // Write individual Logical keywords for each category and their
            // respective counts to the report
            if (!CntrSummaryResults.isEmpty())
            {
               for (int i = 0; i < NumLangs; i++)
               {
                  // Get language name
                  String lang = LanguagePropertiesType.values()[i].toString();

                  if (CntrSummaryResults.get(i).NumCountedFiles > 0)
                  {
                     WriteLogicalKeywordCounts(bufWriter, CntrSummaryResults, lang, i);
                  }
               }
            }

            // Close the output streams
            bufWriter.close();
            logger.debug("Finished writing complexity report to file " + fileName);
         }
         catch (IOException ex)
         {
            // ex.printStackTrace();
            logger.error("Error writing complexity report to file " + fileName);
            logger.debug(ex);
         }
      }

   }

   /**
    * Returns column header for cyclomatic complexity report's "RESULTS BY FILE"
    * section
    * 
    * @return A string that contains column header
    */
   private String GetCyclCmplxFileDataColHeader()
   {
      String hdr = "RESULTS BY FILE" + Constants.NEW_LINE_SEPARATOR + Constants.NEW_LINE_SEPARATOR;
      hdr += "Cyclomatic Complexity" + Constants.NEW_LINE_SEPARATOR;
      hdr += "Total(CC1),Total(CC2),Total(CC3),Average(CC1),Risk,File Name" + Constants.NEW_LINE_SEPARATOR;

      return hdr;
   }

   /**
    * Returns column header for cyclomatic complexity report's "RESULTS BY
    * FUNCTION" section
    * 
    * @return A string that contains column header
    */
   private String GetCyclCmplxFuncDataColHeader()
   {
      String hdr = "RESULTS BY FUNCTION" + Constants.NEW_LINE_SEPARATOR + Constants.NEW_LINE_SEPARATOR;
      hdr += "Cyclomatic Complexity" + Constants.NEW_LINE_SEPARATOR;
      hdr += "CC1,CC2,CC3,Risk,Function Name,File Name" + Constants.NEW_LINE_SEPARATOR;

      return hdr;
   }

   /**
    * Computes arithmetic mean
    * 
    * @param sum
    *           Sum of all the elements
    * @param n
    *           Number of elements
    * @return average value computed from given values
    */
   private double GetAverage(int sum, int n)
   {
      double avg = sum;
      if (n > 0)
      {
         avg = (double) sum / (double) n;
      }
      return avg;
   }

   /**
    * Returns Risk level based on cyclomatic complexity metric
    * 
    * @param count
    *           Cyclomatic complexity count
    * @return A string that identifies Risk level (Low, Medium, High, Very High)
    */
   private String GetCyclCmplxRiskLvl(double count)
   {
      String riskLvl = "Invalid";

      if (count > 0.0 && count <= 10.0)
      {
         riskLvl = "Low";
      }
      else if (count > 10.0 && count <= 20.0)
      {
         riskLvl = "Medium";
      }
      else if (count > 20.0 && count <= 50.0)
      {
         riskLvl = "High";
      }
      else if (count > 50.0)
      {
         riskLvl = "Very High";
      }

      return riskLvl;
   }

   /*
    * (non-Javadoc)
    * 
    * @see CounterReporter#GenCyclCmplxCntrReport()
    */
   @Override
   protected void GenCyclCmplxCntrReport(ArrayList<UCCFile> cntrResults)
   {
      // Check if any counter data is available for reporting
      if (!CntrSummaryResults.isEmpty() && CyclomaticComplexityResFound)
      {
         // Get report file name
         String fileName = GetReportFileName(ReportType.CYCL_CMPLX_COUNT_REPORT);
         try
         {
            // Open output streams for writing
            OutputStreamWriter outStream =
                     new OutputStreamWriter(new FileOutputStream(fileName), Constants.CHARSET_NAME);
            BufferedWriter bufWriter = new BufferedWriter(outStream);

            // Write the file header
            bufWriter.write(GetFileHeader("CYCLOMATIC COMPLEXITY RESULTS", RtParams.UserInputStr));
            bufWriter.newLine();

            // Write column headers for "RESULTS BY FILE" section of the report
            bufWriter.write(GetCyclCmplxFileDataColHeader());

            int totalCC1 = 0; // Total number of CC1 counts in all files
            int numFuncs = 0; // Total number of functions in all files
            int numFiles = 0; // Total number of files
            String row;
            for (int i = 0; i < cntrResults.size(); i++)
            {
               if (!cntrResults.get(i).IsDup && cntrResults.get(i).IsCounted
                        && !cntrResults.get(i).CyclCmplxCnts.isEmpty() && !cntrResults.get(i).HasEmbCode)
               {
                  // Write cyclomatic complexity data for each file
                  row = cntrResults.get(i).CyclCmplxTotal + "," + // CC1 total
                           "," + // CC2 placeholder
                           "," + // CC3 placeholder
                           String.format("%.2f", cntrResults.get(i).CyclCmplxAvg) + "," + // CC1
                                                                                          // average
                                                                                          // complexity
                           GetCyclCmplxRiskLvl(cntrResults.get(i).CyclCmplxAvg) + "," + // Risk
                                                                                        // level
                           cntrResults.get(i).FileName + Constants.NEW_LINE_SEPARATOR; // File
                                                                                       // Name
                  bufWriter.write(row);

                  // Keep a running total of CC1 counts in all files
                  totalCC1 += cntrResults.get(i).CyclCmplxTotal;

                  // Keep a running total of functions in all files
                  numFuncs += cntrResults.get(i).CyclCmplxCnts.size();

                  // Total number of files that were counted
                  numFiles++;
               }
            }

            // Compute average number of CC1 counts per function
            double avgCC1PerFunc = GetAverage(totalCC1, numFuncs);

            // Compute average number of functions in a file
            double avgFuncPerFile = GetAverage(numFuncs, numFiles);

            // Write summary data for "RESULTS BY FUNCTION" section
            bufWriter.newLine();
            row = totalCC1 + "," + "," + "," + "," + "Totals," + numFuncs + " Functions in " + numFiles + " File(s)"
                     + Constants.NEW_LINE_SEPARATOR;
            row += String.format("%.2f", avgCC1PerFunc) + "," + "," + "," + "," + "Averages,"
                     + String.format("%.2f", avgFuncPerFile) + " Functions per File (Averages = Totals/Functions)"
                     + Constants.NEW_LINE_SEPARATOR;
            bufWriter.write(row);

            // Write column headers for "RESULTS BY FUNCTION" section of the
            // report
            bufWriter.newLine();
            bufWriter.write(GetCyclCmplxFuncDataColHeader());

            totalCC1 = 0; // Total number of CC1 counts in all functions
            for (int i = 0; i < cntrResults.size(); i++)
            {
               if (!cntrResults.get(i).IsDup && cntrResults.get(i).IsCounted && !cntrResults.get(i).HasEmbCode)
               {
                  // Write cyclomatic complexity data for all functions in a
                  // file
                  for (int funcIdx = 0; funcIdx < cntrResults.get(i).CyclCmplxCnts.size(); funcIdx++)
                  {
                     row = cntrResults.get(i).CyclCmplxCnts.get(funcIdx).Count + "," + // CC1
                                                                                       // total
                              "," + // CC2 placeholder
                              "," + // CC3 placeholder
                              GetCyclCmplxRiskLvl((double) cntrResults.get(i).CyclCmplxCnts.get(funcIdx).Count) + "," + // Risk
                                                                                                                        // level
                              cntrResults.get(i).CyclCmplxCnts.get(funcIdx).Keyword + "," + // Function
                                                                                            // name
                              cntrResults.get(i).FileName + Constants.NEW_LINE_SEPARATOR; // File
                                                                                          // Name
                     bufWriter.write(row);

                     // Keep a running total of CC1 counts in all files
                     totalCC1 += cntrResults.get(i).CyclCmplxCnts.get(funcIdx).Count;
                  }
               }
            }

            // Re-compute average number of CC1 counts per function
            avgCC1PerFunc = GetAverage(totalCC1, numFuncs);

            // Write summary data for "RESULTS BY FUNCTION" section
            bufWriter.newLine();
            row = totalCC1 + "," + "," + "," + "Totals," + numFuncs + " Functions," + numFiles + " File(s)"
                     + Constants.NEW_LINE_SEPARATOR;
            row += String.format("%.2f", avgCC1PerFunc) + "," + "," + "," + "Averages,"
                     + String.format("%.2f", avgFuncPerFile) + " Functions per File (Averages = Totals/Functions)"
                     + Constants.NEW_LINE_SEPARATOR;
            bufWriter.write(row);

            // Close the output streams
            bufWriter.close();
            logger.debug("Finished writing cyclomatic complexity report to file " + fileName);
         }
         catch (IOException ex)
         {
            // ex.printStackTrace();
            logger.error("Error writing cyclomatic complexity report to file " + fileName);
            logger.debug(ex);
         }
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see CounterReporter#GenDupPairsReport()
    */
   @Override
   protected void GenDupPairsReport(ArrayList<UCCFile> cntrResults)
   {
      // Check if any counter data is available for reporting
      if (!CntrSummaryResults.isEmpty())
      {
         // Get report file name
         String fileName = GetReportFileName(ReportType.DUP_PAIRS_REPORT);
         try
         {
            // Open output streams for writing
            OutputStreamWriter outStream =
                     new OutputStreamWriter(new FileOutputStream(fileName), Constants.CHARSET_NAME);
            BufferedWriter bufWriter = new BufferedWriter(outStream);

            // Write the file header
            bufWriter.write(GetFileHeader("DUPLICATE FILE PAIRS", RtParams.UserInputStr));
            bufWriter.newLine();

            // Write column headers for data
            bufWriter.write("Original,Duplicate,Percent Duplicate" + Constants.NEW_LINE_SEPARATOR);

            String row;
            for (int i = 0; i < cntrResults.size(); i++)
            {
               if (cntrResults.get(i).IsDup)
               {
                  // Write names of duplicate pairs
                  row = cntrResults.get(cntrResults.get(i).DupOfIdx).FileName + ", " + cntrResults.get(i).FileName
                           + ", " + String.format("%.2f", cntrResults.get(i).DupCodePercent) + "%"
                           + Constants.NEW_LINE_SEPARATOR;
                  bufWriter.write(row);
               }
            }

            // Close the output streams
            bufWriter.close();
            logger.debug("Finished writing duplicate pairs report to file " + fileName);
         }
         catch (IOException ex)
         {
            logger.error("Error writing duplicate pairs report to file " + fileName);
            logger.debug(ex);
         }
      }

   }

   /*
    * (non-Javadoc)
    * 
    * @see CounterReporter#GenDupSummaryCntrReport()
    */
   @Override
   protected void GenDupSummaryCntrReport(ArrayList<UCCFile> cntrResults)
   {
      // Check if any counter data is available for reporting
      if (!DupCntrSummaryResults.isEmpty())
      {
         // Get report file name
         String fileName = GetReportFileName(ReportType.DUP_SUMMARY_COUNT_REPORT);
         try
         {
            // Open output streams for writing
            OutputStreamWriter outStream =
                     new OutputStreamWriter(new FileOutputStream(fileName), Constants.CHARSET_NAME);
            BufferedWriter bufWriter = new BufferedWriter(outStream);

            // Write the file header
            bufWriter.write(GetFileHeader("LANGUAGE COUNT SUMMARY", RtParams.UserInputStr));
            bufWriter.newLine();

            // Write column headers for data
            bufWriter.write(GetSummaryColHeader());

            String row;
            int totalFiles = 0;
            int totalPSLOC = 0;
            int totalLSLOC = 0;
            for (int i = 0; i < DupCntrSummaryResults.size(); i++)
            {
               if (DupCntrSummaryResults.get(i).NumCountedFiles > 0)
               {
                  // Write summary of count data for each language
                  row = LanguagePropertiesType.values()[i].toString() + ","
                           + DupCntrSummaryResults.get(i).NumCountedFiles + "," + DupCntrSummaryResults.get(i).NumPSLOC
                           + "," + DupCntrSummaryResults.get(i).NumLSLOC + Constants.NEW_LINE_SEPARATOR;
                  bufWriter.write(row);

                  // Compute totals for writing to the report
                  totalFiles += DupCntrSummaryResults.get(i).NumCountedFiles;
                  totalPSLOC += DupCntrSummaryResults.get(i).NumPSLOC;
                  totalLSLOC += DupCntrSummaryResults.get(i).NumLSLOC;
               }
            }

            // Write a blank line
            bufWriter.newLine();

            // Write totals to the report
            row = "Total," + totalFiles + "," + totalPSLOC + "," + totalLSLOC + Constants.NEW_LINE_SEPARATOR;
            bufWriter.write(row);

            // Close the output streams
            bufWriter.close();
            logger.debug("Finished writing duplicate summary counter report to file " + fileName);
         }
         catch (IOException ex)
         {
            logger.error("Error writing duplicate summary counter report to file " + fileName);
            logger.debug(ex);
         }
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see CounterReporter#GenDupLangCntrReport()
    */
   @Override
   protected void GenDupLangCntrReport(ArrayList<UCCFile> cntrResults)
   {
      // Check if any counter data is available for reporting
      if (!DupCntrSummaryResults.isEmpty())
      {
         for (int i = 0; i < NumLangs; i++)
         {
            if (DupCntrSummaryResults.get(i).NumCountedFiles > 0)
            {
               // Get language name, which will be used to form report file name
               String lang = LanguagePropertiesType.values()[i].toString();
               String fileName = GetReportFileName(ReportType.DUP_LANG_COUNT_REPORT, lang);
               String row;

               try
               {
                  // Open output streams for writing
                  OutputStreamWriter outStream =
                           new OutputStreamWriter(new FileOutputStream(fileName), Constants.CHARSET_NAME);
                  BufferedWriter bufWriter = new BufferedWriter(outStream);

                  // Write the file header
                  bufWriter.write(GetFileHeader("SLOC COUNT RESULTS", RtParams.UserInputStr));
                  bufWriter.newLine();

                  // Write language version number on which counting rules are
                  // based
                  bufWriter.write("Counting rules based on " + lang + " Version: "
                           + CntrSummaryResults.get(i).LangVersion + Constants.NEW_LINE_SEPARATOR);
                  bufWriter.newLine();

                  // Write column headers for data
                  bufWriter.write(GetLangColHeader(lang));

                  for (int j = 0; j < cntrResults.size(); j++)
                  {
                     if (cntrResults.get(j).LangProperty != null && cntrResults.get(j).LangProperty.name() == lang)
                     {
                        int idx;

                        // If the duplicate file is identical to another file,
                        // then use results of the original file
                        if (cntrResults.get(j).FileChecksum == cntrResults
                                 .get(cntrResults.get(j).DupOfIdx).FileChecksum)
                        {
                           // Get index of the original file to get count data
                           idx = cntrResults.get(j).DupOfIdx;
                        }
                        // Otherwise, it's a threshold duplicate. In that case,
                        // use results of the duplicate file
                        else
                        {
                           // Get index of the duplicate file to get count data
                           idx = j;
                        }

                        if (cntrResults.get(j).IsDup && cntrResults.get(idx).IsCounted)
                        {
                           // Write count data for each file
                           row = cntrResults.get(idx).NumTotalLines + "," + cntrResults.get(idx).NumBlankLines + ","
                                    + cntrResults.get(idx).NumWholeComments + ","
                                    + cntrResults.get(idx).NumEmbeddedComments + ","
                                    + cntrResults.get(idx).NumCompilerDirectives + ","
                                    + cntrResults.get(idx).NumDataDeclLog + "," + cntrResults.get(idx).NumExecInstrLog
                                    + "," + cntrResults.get(idx).NumLSLOC + "," + cntrResults.get(idx).NumPSLOC + ","
                                    + cntrResults.get(idx).FileType.toString() + "," + cntrResults.get(j).FileName
                                    + Constants.NEW_LINE_SEPARATOR;
                           bufWriter.write(row);
                        }
                     }
                  }

                  // Write summary block header for the language
                  bufWriter.newLine();
                  bufWriter.write(GetLangSummaryColHeader(""));

                  // Write summary block data for the language
                  row = DupCntrSummaryResults.get(i).NumTotalLines + "," + DupCntrSummaryResults.get(i).NumBlankLines
                           + "," + DupCntrSummaryResults.get(i).NumWholeCmnts + ","
                           + DupCntrSummaryResults.get(i).NumEmbeddedCmnts + ","
                           + DupCntrSummaryResults.get(i).NumCompDirs + ","
                           + DupCntrSummaryResults.get(i).NumDataDeclPhys + ","
                           + DupCntrSummaryResults.get(i).NumExecInstrPhys + "," + DupCntrSummaryResults.get(i).NumPSLOC
                           + ",CODE,Physical" + Constants.NEW_LINE_SEPARATOR;
                  bufWriter.write(row);

                  row = DupCntrSummaryResults.get(i).NumTotalLines + "," + DupCntrSummaryResults.get(i).NumBlankLines
                           + "," + DupCntrSummaryResults.get(i).NumWholeCmnts + ","
                           + DupCntrSummaryResults.get(i).NumEmbeddedCmnts + ","
                           + DupCntrSummaryResults.get(i).NumCompDirs + ","
                           + DupCntrSummaryResults.get(i).NumDataDeclLog + ","
                           + DupCntrSummaryResults.get(i).NumExecInstrLog + "," + DupCntrSummaryResults.get(i).NumLSLOC
                           + ",CODE,Logical" + Constants.NEW_LINE_SEPARATOR;
                  bufWriter.write(row);

                  // Write file access/successful count info
                  row = "Number of files successfully accessed," + DupCntrSummaryResults.get(i).NumCountedFiles
                           + ",out of," + DupCntrSummaryResults.get(i).NumTotalFiles + Constants.NEW_LINE_SEPARATOR;
                  bufWriter.write(row);

                  // Write PLSOC to SLOC ratio
                  row = "Ratio of Physical to Logical SLOC," + String.format("%.2f",
                           ComputeRatio(DupCntrSummaryResults.get(i).NumPSLOC, DupCntrSummaryResults.get(i).NumLSLOC))
                           + Constants.NEW_LINE_SEPARATOR;
                  bufWriter.newLine();
                  bufWriter.write(row);

                  // Write individual complexity keywords for each category and
                  // their respective counts to the report
                  WriteLogicalKeywordCounts(bufWriter, DupCntrSummaryResults, lang, i);

                  // Close the output streams
                  bufWriter.close();
                  logger.debug("Finished writing duplicate counter report for " + lang + " to file " + fileName);
               }
               catch (IOException ex)
               {
                  // ex.printStackTrace();
                  logger.error("Error writing duplicate counter report for " + lang + " to file " + fileName);
                  logger.debug(ex);
               }
            }
         }
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see CounterReporter#GenDupUnifiedCntrReport()
    */
   @Override
   protected void GenDupUnifiedCntrReport(ArrayList<UCCFile> cntrResults)
   {
      // Check if any counter data is available for reporting
      if (!cntrResults.isEmpty())
      {
         // Get report file name
         String fileName = GetReportFileName(ReportType.DUP_UNIFIED_COUNT_REPORT);
         String row;

         try
         {
            // Open output streams for writing
            OutputStreamWriter outStream =
                     new OutputStreamWriter(new FileOutputStream(fileName), Constants.CHARSET_NAME);
            BufferedWriter bufWriter = new BufferedWriter(outStream);

            // Write the file header
            bufWriter.write(GetFileHeader("SLOC COUNT RESULTS FOR ALL SOURCE FILES", RtParams.UserInputStr));
            bufWriter.newLine();
            bufWriter.newLine();

            // Write column headers for data
            bufWriter.write(GetUnifiedColHeader("ALL NON-WEB LANGUAGE"));

            for (int j = 0; j < cntrResults.size(); j++)
            {
               int idx;

               // If the duplicate file is identical to another file,
               // then use results of the original file
               if (cntrResults.get(j).FileChecksum == cntrResults.get(cntrResults.get(j).DupOfIdx).FileChecksum)
               {
                  // Get index of the original file to get count data
                  idx = cntrResults.get(j).DupOfIdx;
               }
               // Otherwise, it's a threshold duplicate. In that case,
               // use results of the duplicate file
               else
               {
                  // Get index of the duplicate file to get count data
                  idx = j;
               }

               if (cntrResults.get(j).IsDup && cntrResults.get(idx).IsCounted)
               {
                  // Write count data for each file
                  row = cntrResults.get(idx).NumTotalLines + "," + cntrResults.get(idx).NumBlankLines + ","
                           + cntrResults.get(idx).NumWholeComments + "," + cntrResults.get(idx).NumEmbeddedComments
                           + "," + cntrResults.get(idx).NumCompilerDirectives + ","
                           + cntrResults.get(idx).NumDataDeclLog + "," + cntrResults.get(idx).NumExecInstrLog + ","
                           + cntrResults.get(idx).NumLSLOC + "," + cntrResults.get(idx).NumPSLOC + ","
                           + cntrResults.get(idx).FileType.toString() + "," + cntrResults.get(j).FileName
                           + Constants.NEW_LINE_SEPARATOR;
                  bufWriter.write(row);
               }
            }

            // Write summary block header
            bufWriter.newLine();
            bufWriter.write(GetUnifiedSummaryColHeader(" FOR NON-WEB LANGUAGES"));

            int numCountedFiles = 0;
            int numTotalFiles = 0;
            int totalPSLOC = 0;
            int totalLSLOC = 0;
            int totalLines = 0;
            int totalBlankLines = 0;
            int totalWholeCmnts = 0;
            int totalEmbeddedCmnts = 0;
            int totalCompDirs = 0;
            int totalDataDeclPhys = 0;
            int totalExecInstrPhys = 0;
            int totalDataDeclLog = 0;
            int totalExecInstrLog = 0;

            // Write summary data for all languages
            if (!DupCntrSummaryResults.isEmpty())
            {
               for (int i = 0; i < NumLangs; i++)
               {
                  if (DupCntrSummaryResults.get(i).NumCountedFiles > 0)
                  {
                     totalLines += DupCntrSummaryResults.get(i).NumTotalLines;
                     totalBlankLines += DupCntrSummaryResults.get(i).NumBlankLines;
                     totalWholeCmnts += DupCntrSummaryResults.get(i).NumWholeCmnts;
                     totalEmbeddedCmnts += DupCntrSummaryResults.get(i).NumEmbeddedCmnts;
                     totalCompDirs += DupCntrSummaryResults.get(i).NumCompDirs;
                     totalDataDeclPhys += DupCntrSummaryResults.get(i).NumDataDeclPhys;
                     totalExecInstrPhys += DupCntrSummaryResults.get(i).NumExecInstrPhys;
                     totalDataDeclLog += DupCntrSummaryResults.get(i).NumDataDeclLog;
                     totalExecInstrLog += DupCntrSummaryResults.get(i).NumExecInstrLog;
                     totalPSLOC += DupCntrSummaryResults.get(i).NumPSLOC;
                     totalLSLOC += DupCntrSummaryResults.get(i).NumLSLOC;
                  }

                  numCountedFiles += DupCntrSummaryResults.get(i).NumCountedFiles;
                  numTotalFiles += DupCntrSummaryResults.get(i).NumTotalFiles;
               }
            }

            // Write summary block data for the language
            row = totalLines + "," + totalBlankLines + "," + totalWholeCmnts + "," + totalEmbeddedCmnts + ","
                     + totalCompDirs + "," + totalDataDeclPhys + "," + totalExecInstrPhys + "," + totalPSLOC
                     + ",CODE,Physical" + Constants.NEW_LINE_SEPARATOR;
            bufWriter.write(row);

            row = totalLines + "," + totalBlankLines + "," + totalWholeCmnts + "," + totalEmbeddedCmnts + ","
                     + totalCompDirs + "," + totalDataDeclLog + "," + totalExecInstrLog + "," + totalLSLOC
                     + ",CODE,Logical" + Constants.NEW_LINE_SEPARATOR;
            bufWriter.write(row);

            // Write file access/successful count info
            row = "Number of files successfully accessed," + numCountedFiles + ",out of," + numTotalFiles
                     + Constants.NEW_LINE_SEPARATOR;
            bufWriter.newLine();
            bufWriter.write(row);

            // Write PLSOC to SLOC ratio
            row = "Ratio of Physical to Logical SLOC," + String.format("%.2f", ComputeRatio(totalPSLOC, totalLSLOC))
                     + Constants.NEW_LINE_SEPARATOR;
            bufWriter.newLine();
            bufWriter.write(row);

            // Write individual complexity keywords for each category and
            // their respective counts to the report
            if (!DupCntrSummaryResults.isEmpty())
            {
               for (int i = 0; i < NumLangs; i++)
               {
                  // Get language name
                  String lang = LanguagePropertiesType.values()[i].toString();

                  if (DupCntrSummaryResults.get(i).NumCountedFiles > 0)
                  {
                     WriteLogicalKeywordCounts(bufWriter, DupCntrSummaryResults, lang, i);
                  }
               }
            }

            // Close the output streams
            bufWriter.close();
            logger.debug("Finished writing duplicate unified counter report to file " + fileName);
         }
         catch (IOException ex)
         {
            // ex.printStackTrace();
            logger.error("Error writing duplicate unified counter report to file " + fileName);
            logger.debug(ex);
         }
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see CounterReporter#GenDupCmplxCntrReport()
    */
   @Override
   protected void GenDupCmplxCntrReport()
   {
      // Check if any counter data is available for reporting
      if (!DupCmplxCntrSummaryResults.isEmpty())
      {
         String fileName = GetReportFileName(ReportType.DUP_CMPLX_COUNT_REPORT);

         try
         {
            // Open output streams for writing
            OutputStreamWriter outStream =
                     new OutputStreamWriter(new FileOutputStream(fileName), Constants.CHARSET_NAME);
            BufferedWriter bufWriter = new BufferedWriter(outStream);

            // Write the file header
            bufWriter.write(GetFileHeader("COMPLEXITY COUNT RESULTS", RtParams.UserInputStr));
            bufWriter.newLine();

            // Write column headers for data
            int maxLoopLvl = GetMaxLoopLvl(DupCmplxCntrSummaryResults);
            bufWriter.write(GetCmplxColHeader(maxLoopLvl));

            int totalMathOps = 0;
            int totalTrigOps = 0;
            int totalLogOps = 0;
            int totalCalcOps = 0;
            int totalCondStmts = 0;
            int totalLogicOps = 0;
            int totalPreprocs = 0;
            int totalAssignOps = 0;
            int totalPntrOps = 0;
            String row;
            for (int i = 0; i < DupCmplxCntrSummaryResults.size(); i++)
            {
               row = DupCmplxCntrSummaryResults.get(i).NumMathOps + "," + DupCmplxCntrSummaryResults.get(i).NumTrigOps
                        + "," + DupCmplxCntrSummaryResults.get(i).NumLogOps + ","
                        + DupCmplxCntrSummaryResults.get(i).NumCalcOps + ","
                        + DupCmplxCntrSummaryResults.get(i).NumCondStmts + ","
                        + DupCmplxCntrSummaryResults.get(i).NumLogicOps + ","
                        + DupCmplxCntrSummaryResults.get(i).NumPreprocs + ","
                        + DupCmplxCntrSummaryResults.get(i).NumAssignOps + ","
                        + DupCmplxCntrSummaryResults.get(i).NumPntrOps + ",";

               for (int j = 0; j < maxLoopLvl; j++)
               {
                  if (j < DupCmplxCntrSummaryResults.get(i).LoopLvlCnts.size())
                  {
                     row += DupCmplxCntrSummaryResults.get(i).LoopLvlCnts.get(j) + ",";
                  }
                  else
                  {
                     row += "0,";
                  }
               }

               row += DupCmplxCntrSummaryResults.get(i).LangName + ",";
               row += DupCmplxCntrSummaryResults.get(i).FileName + Constants.NEW_LINE_SEPARATOR;
               bufWriter.write(row);

               // Compute totals for all files
               totalMathOps += DupCmplxCntrSummaryResults.get(i).NumMathOps;
               totalTrigOps += DupCmplxCntrSummaryResults.get(i).NumTrigOps;
               totalLogOps += DupCmplxCntrSummaryResults.get(i).NumLogOps;
               totalCalcOps += DupCmplxCntrSummaryResults.get(i).NumCalcOps;
               totalCondStmts += DupCmplxCntrSummaryResults.get(i).NumCondStmts;
               totalLogicOps += DupCmplxCntrSummaryResults.get(i).NumLogicOps;
               totalPreprocs += DupCmplxCntrSummaryResults.get(i).NumPreprocs;
               totalAssignOps += DupCmplxCntrSummaryResults.get(i).NumAssignOps;
               totalPntrOps += DupCmplxCntrSummaryResults.get(i).NumPntrOps;
            }

            // Write summary block header for the complexity counts
            bufWriter.newLine();
            bufWriter.write("Totals" + Constants.NEW_LINE_SEPARATOR);

            // Write summary block data for the complexity counts
            row = totalMathOps + "," + totalTrigOps + "," + totalLogOps + "," + totalCalcOps + "," + totalCondStmts
                     + "," + totalLogicOps + "," + totalPreprocs + "," + totalAssignOps + "," + totalPntrOps
                     + Constants.NEW_LINE_SEPARATOR;
            bufWriter.write(row);

            // Write individual complexity keywords for each category and their
            // respective counts to the report
            WriteCmplxKeywordCounts(bufWriter, DupCntrSummaryResults);

            // Close the output streams
            bufWriter.close();
            logger.debug("Finished writing duplicate complexity counter report to file " + fileName);
         }
         catch (IOException ex)
         {
            logger.error("Error writing duplicate complexity counter report to file " + fileName);
            logger.debug(ex);
         }
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see CounterReporter#GenDupCyclCmplxCntrReport()
    */
   @Override
   protected void GenDupCyclCmplxCntrReport(ArrayList<UCCFile> cntrResults)
   {
      // Check if any counter data is available for reporting
      if (!DupCntrSummaryResults.isEmpty() && CyclomaticComplexityResFound)
      {
         // Get report file name
         String fileName = GetReportFileName(ReportType.DUP_CYCL_CMPLX_COUNT_REPORT);
         try
         {
            // Open output streams for writing
            OutputStreamWriter outStream =
                     new OutputStreamWriter(new FileOutputStream(fileName), Constants.CHARSET_NAME);
            BufferedWriter bufWriter = new BufferedWriter(outStream);

            // Write the file header
            bufWriter.write(GetFileHeader("CYCLOMATIC COMPLEXITY RESULTS", RtParams.UserInputStr));
            bufWriter.newLine();

            // Write column headers for "RESULTS BY FILE" section of the report
            bufWriter.write(GetCyclCmplxFileDataColHeader());

            int totalCC1 = 0; // Total number of CC1 counts in all files
            int numFuncs = 0; // Total number of functions in all files
            int numFiles = 0; // Total number of files
            String row;
            for (int i = 0; i < cntrResults.size(); i++)
            {
               int idx;

               // If the duplicate file is identical to another file,
               // then use results of the original file
               if (cntrResults.get(i).FileChecksum == cntrResults.get(cntrResults.get(i).DupOfIdx).FileChecksum)
               {
                  // Get index of the original file to get count data
                  idx = cntrResults.get(i).DupOfIdx;
               }
               // Otherwise, it's a threshold duplicate. In that case,
               // use results of the duplicate file
               else
               {
                  // Get index of the duplicate file to get count data
                  idx = i;
               }

               if (cntrResults.get(i).IsDup && cntrResults.get(idx).IsCounted
                        && !cntrResults.get(i).CyclCmplxCnts.isEmpty())
               {
                  // Write cyclomatic complexity data for each file
                  row = cntrResults.get(idx).CyclCmplxTotal + "," // CC1 total
                           + "," // CC2 placeholder
                           + "," // CC3 placeholder
                           + String.format("%.2f", cntrResults.get(idx).CyclCmplxAvg) + "," // CC1
                                                                                            // average
                                                                                            // complexity
                           + GetCyclCmplxRiskLvl(cntrResults.get(idx).CyclCmplxAvg) + "," // Risk
                                                                                          // level
                           + cntrResults.get(i).FileName + Constants.NEW_LINE_SEPARATOR; // File
                                                                                         // Name
                  bufWriter.write(row);

                  // Keep a running total of CC1 counts in all files
                  totalCC1 += cntrResults.get(idx).CyclCmplxTotal;

                  // Keep a running total of functions in all files
                  numFuncs += cntrResults.get(idx).CyclCmplxCnts.size();

                  // Total number of files that were counted
                  numFiles++;
               }
            }

            // Compute average number of CC1 counts per function
            double avgCC1PerFunc = GetAverage(totalCC1, numFuncs);

            // Compute average number of functions in a file
            double avgFuncPerFile = GetAverage(numFuncs, numFiles);

            // Write summary data for "RESULTS BY FUNCTION" section
            bufWriter.newLine();
            row = totalCC1 + "," + "," + "," + "," + "Totals," + numFuncs + " Functions in " + numFiles + " File(s)"
                     + Constants.NEW_LINE_SEPARATOR;
            row += String.format("%.2f", avgCC1PerFunc) + "," + "," + "," + "," + "Averages,"
                     + String.format("%.2f", avgFuncPerFile) + " Functions per File (Averages = Totals/Functions)"
                     + Constants.NEW_LINE_SEPARATOR;
            bufWriter.write(row);

            // Write column headers for "RESULTS BY FUNCTION" section of the
            // report
            bufWriter.newLine();
            bufWriter.write(GetCyclCmplxFuncDataColHeader());

            totalCC1 = 0; // Total number of CC1 counts in all functions
            for (int i = 0; i < cntrResults.size(); i++)
            {
               int idx;

               // If the duplicate file is identical to another file,
               // then use results of the original file
               if (cntrResults.get(i).FileChecksum == cntrResults.get(cntrResults.get(i).DupOfIdx).FileChecksum)
               {
                  // Get index of the original file to get count data
                  idx = cntrResults.get(i).DupOfIdx;
               }
               // Otherwise, it's a threshold duplicate. In that case,
               // use results of the duplicate file
               else
               {
                  // Get index of the duplicate file to get count data
                  idx = i;
               }

               if (cntrResults.get(i).IsDup && cntrResults.get(idx).IsCounted)
               {
                  // Write cyclomatic complexity data for all functions in a
                  // file
                  for (int funcIdx = 0; funcIdx < cntrResults.get(idx).CyclCmplxCnts.size(); funcIdx++)
                  {
                     row = cntrResults.get(idx).CyclCmplxCnts.get(funcIdx).Count + "," // CC1
                                                                                       // total
                              + "," // CC2 placeholder
                              + "," // CC3 placeholder
                              + GetCyclCmplxRiskLvl((double) cntrResults.get(idx).CyclCmplxCnts.get(funcIdx).Count)
                              + "," // Risk level
                              + cntrResults.get(idx).CyclCmplxCnts.get(funcIdx).Keyword + "," // Function
                                                                                              // name
                              + cntrResults.get(i).FileName + Constants.NEW_LINE_SEPARATOR; // File
                                                                                            // name

                     bufWriter.write(row);

                     // Keep a running total of CC1 counts in all files
                     totalCC1 += cntrResults.get(idx).CyclCmplxCnts.get(funcIdx).Count;
                  }
               }

            }

            // Re-compute average number of CC1 counts per function
            avgCC1PerFunc = GetAverage(totalCC1, numFuncs);

            // Write summary data for "RESULTS BY FUNCTION" section
            bufWriter.newLine();
            row = totalCC1 + "," + "," + "," + "Totals," + numFuncs + " Functions," + numFiles + " File(s)"
                     + Constants.NEW_LINE_SEPARATOR;
            row += String.format("%.2f", avgCC1PerFunc) + "," + "," + "," + "Averages,"
                     + String.format("%.2f", avgFuncPerFile) + " Functions per File (Averages = Totals/Functions)"
                     + Constants.NEW_LINE_SEPARATOR;
            bufWriter.write(row);

            // Close the output streams
            bufWriter.close();
            logger.debug("Finished writing duplicate cyclomatic complexity counter report to file " + fileName);
         }
         catch (IOException ex)
         {
            logger.error("Error writing duplicate cyclomatic complexity counter report to file " + fileName);
            logger.debug(ex);
         }
      }
   }
}
