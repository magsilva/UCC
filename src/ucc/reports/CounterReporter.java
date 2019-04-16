package ucc.reports;

import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ucc.datatypes.CmplxDataType;
import ucc.datatypes.DataTypes.LanguagePropertiesType;
import ucc.datatypes.UCCFile;
import ucc.main.RuntimeParameters;
import ucc.utils.FileUtils;

/**
 * Abstract CounterReporter class that contains methods to print several types
 * of reports for users. Reports contain count metrics data.
 * <p>
 * Subclasses should implement the abstract methods to print reports in a
 * specific output format, for example, ASCII, CSV, etc.
 *
 * @author Integrity Applications Incorporated
 *
 */
public abstract class CounterReporter extends Reporter
{
   /** Instantiate the Log4j2 logger for this class */
   private static final Logger logger = LogManager.getLogger(CounterReporter.class);

   /** A handle to the Singleton class containing Runtime Parameters */
   protected RuntimeParameters RtParams = RuntimeParameters.GetInstance();

   /**
    * An array of objects to store summary of count metrics for each language
    * type
    */
   protected ArrayList<CounterResultSummary> CntrSummaryResults;

   /**
    * An array of objects to store summary of count metrics for duplicate files
    * of each language type
    */
   protected ArrayList<CounterResultSummary> DupCntrSummaryResults;

   /**
    * An array of objects to store summary of complexity count metrics for each
    * file
    */
   protected ArrayList<ComplexCounterResultsSummary> CmplxCntrSummaryResults;

   /**
    * An array of objects to store summary of complexity count metrics for each
    * duplicate file
    */
   protected ArrayList<ComplexCounterResultsSummary> DupCmplxCntrSummaryResults;

   /**
    * A boolean to indicate whether results for duplicate files are available.
    * This variable will be used to determine whether to generate reports for
    * duplicate files for language specific report and for duplicate pairs
    * report
    */
   protected boolean DupResFound;

   /**
    * A boolean to indicate whether there are any uncounted files. This variable
    * will be used to determine whether to generate uncounted files report
    */
   protected boolean UncountedFilesFound;

   /**
    * A boolean to indicate whether complexity metrics are available. This
    * variable will be used to determine whether to generate complexity report
    */
   protected boolean CyclomaticComplexityResFound;

   /**
    * An Arraylist to hold all operator count for each file
    */
   protected ArrayList<ArrayList<CmplxDataType>> UniqueOperatorList;

   /**
    * An Arraylist to hold all operand count for each file
    */
   protected ArrayList<TreeMap> UniqueOperandList;

   /**
    * Default constructor to instantiate CounterReporter object
    */
   public CounterReporter()
   {
      // Call super class' default constructor
      super();

      // Initialize class member variables
      Initialize();
   }

   /**
    * Constructor to instantiate CounterReporter object with given parameters
    *
    * @param outFileExt
    *           Extension of output report file name
    * @param numLangs
    *           Total number of languages for report generation
    */
   public CounterReporter(String outFileExt, int numLangs)
   {
      super(outFileExt, numLangs);
      Initialize();
   }

   /**
    * Constructor to instantiate CounterReporter object with given parameters
    *
    * @param outFilePrefix
    *           Prefix to prepend to output report file name
    * @param outFileExt
    *           Extension of output report file name
    * @param outDir
    *           Directory location to write output reports
    * @param numLangs
    *           Total number of languages for report generation
    */
   public CounterReporter(String outFilePrefix, String outFileExt, String outDir, int numLangs)
   {
      super(outFilePrefix, outFileExt, outDir, numLangs);
      Initialize();
   }

   /**
    * Initialize class member variables
    */
   private void Initialize()
   {
      CntrSummaryResults = new ArrayList<CounterResultSummary>();
      DupCntrSummaryResults = new ArrayList<CounterResultSummary>();

      CmplxCntrSummaryResults = new ArrayList<ComplexCounterResultsSummary>();
      DupCmplxCntrSummaryResults = new ArrayList<ComplexCounterResultsSummary>();

      DupResFound = false;
      UncountedFilesFound = false;
      CyclomaticComplexityResFound = false;
   }

   /**
    * Computes ratio of two integers
    *
    * @param num1
    *           First number
    * @param num2
    *           Second number
    * @return ratio = num1/num2
    */
   protected double ComputeRatio(int num1, int num2)
   {
      double ratio = num1;
      if (num1 > 0)
      {
         ratio = (double) num1 / (double) num2;
      }

      return ratio;
   }

   /**
    * Initializes complexity keywords - copies keywords from one list into
    * another
    * 
    * @param cmplxOrig
    *           Keywords to copy
    * @param cmplxNew
    *           Keywords to initialize
    */
   private void InitCmplxCntrKeywords(ArrayList<CmplxDataType> cmplxOrig, ArrayList<CmplxDataType> cmplxNew)
   {
      if (cmplxNew != null)
      {
         for (int i = 0; i < cmplxOrig.size(); i++)
         {
            cmplxNew.add(new CmplxDataType(cmplxOrig.get(i).Keyword));
         }
      }
   }

   /**
    * Initializes complexity summary results data structures with keywords
    * 
    * @param cntrResult
    *           Complexity data to copy
    * @param cntrResSummary
    *           Complexity data to initialize
    */
   private void InitLangCmplxCntrSummary(UCCFile cntrResult, CounterResultSummary cntrResSummary)
   {
      InitCmplxCntrKeywords(cntrResult.CmplxMathCnts, cntrResSummary.CmplxMathCnts);
      InitCmplxCntrKeywords(cntrResult.CmplxTrigCnts, cntrResSummary.CmplxTrigCnts);
      InitCmplxCntrKeywords(cntrResult.CmplxLogCnts, cntrResSummary.CmplxLogCnts);
      InitCmplxCntrKeywords(cntrResult.CmplxCalcCnts, cntrResSummary.CmplxCalcCnts);
      InitCmplxCntrKeywords(cntrResult.CmplxCondCnts, cntrResSummary.CmplxCondCnts);
      InitCmplxCntrKeywords(cntrResult.CmplxLogicCnts, cntrResSummary.CmplxLogicCnts);
      InitCmplxCntrKeywords(cntrResult.CmplxPreprocCnts, cntrResSummary.CmplxPreprocCnts);
      InitCmplxCntrKeywords(cntrResult.CmplxAssignCnts, cntrResSummary.CmplxAssignCnts);
      InitCmplxCntrKeywords(cntrResult.CmplxPntrCnts, cntrResSummary.CmplxPntrCnts);
      InitCmplxCntrKeywords(cntrResult.DataKeywordCnts, cntrResSummary.DataKeywrdCnts);
      InitCmplxCntrKeywords(cntrResult.ExecKeywordCnts, cntrResSummary.ExecKeywrdCnts);
      InitCmplxCntrKeywords(cntrResult.BoolOperandCnts, cntrResSummary.BoolOperandCnts);
   }

   /**
    * Computes sum of keyword counts
    * 
    * @param cmplxOrig
    *           Data object containing keyword/count pairs to sum
    * @param cmplxSum
    *           Data object to store sum/total counts
    */
   private void ComputeCmplxKeywordSummaryCnt(ArrayList<CmplxDataType> cmplxOrig, ArrayList<CmplxDataType> cmplxSum)
   {
      if (cmplxOrig != null && cmplxSum != null && cmplxOrig.size() == cmplxSum.size())
      {
         for (int i = 0; i < cmplxOrig.size(); i++)
         {
            cmplxSum.get(i).Count += cmplxOrig.get(i).Count;
         }
      }
   }

   /**
    * Computes total number of complexity counts
    * 
    * @param cmplxData
    *           Complexity data object containing counts to sum
    * @return Integer that is a sum of all counts for the given complexity
    *         category/object
    */
   private int GetCmplxKeywordSummaryCnt(ArrayList<CmplxDataType> cmplxData)
   {
      int cnt = 0;

      if (cmplxData != null)
      {
         for (int i = 0; i < cmplxData.size(); i++)
         {
            cnt += cmplxData.get(i).Count;
         }
      }

      return cnt;
   }

   /**
    * Computes summary data for each language type from given counter results
    * data
    *
    * @param cntrResults
    *           Counter results, organized per file
    */
   protected void ComputeCntrSummaryResults(ArrayList<UCCFile> cntrResults)
   {
      String lang = "";
      boolean cmplxSumInit;
      boolean dupCmplxSumInit;

      // Physically delete the embedded files
      for (int i = 0; i < cntrResults.size(); i++)
      {
         if (cntrResults.get(i).EmbOfIdx != -1)
         {
            FileUtils.DeleteFile(cntrResults.get(i).FileName);
         }
      }

      // Rename embedded files
      for (int j = 0; j < cntrResults.size(); j++)
      {
         // If the file is embedded code, name it based on the top level file
         if (cntrResults.get(j).EmbOfIdx != -1)
         {
            cntrResults.get(j).FileName = cntrResults.get(cntrResults.get(j).EmbOfIdx).FileName;
         }
      }

      for (int i = 0; i < NumLangs; i++)
      {
         lang = LanguagePropertiesType.values()[i].toString();
         cmplxSumInit = false;
         dupCmplxSumInit = false;
         CntrSummaryResults.add(new CounterResultSummary());
         DupCntrSummaryResults.add(new CounterResultSummary());

         for (int j = 0; j < cntrResults.size(); j++)
         {
            if (cntrResults.get(j).LangProperty != null && cntrResults.get(j).LangProperty.name() == lang)
            {
               if (!cntrResults.get(j).IsDup)
               {
                  // Only get data from files that are counted and are not
                  // duplicates
                  if (cntrResults.get(j).IsCounted)
                  {
                     // Fill out the summary data for the language
                     CntrSummaryResults.get(i).NumCountedFiles++;
                     // Decrement the count for the top-level embedded file so
                     // we don't double up on counts
                     if (cntrResults.get(j).HasEmbCode)
                     {
                        CntrSummaryResults.get(i).NumCountedFiles--;
                     }
                     CntrSummaryResults.get(i).NumTotalLines += cntrResults.get(j).NumTotalLines;
                     CntrSummaryResults.get(i).NumBlankLines += cntrResults.get(j).NumBlankLines;
                     CntrSummaryResults.get(i).NumWholeCmnts += cntrResults.get(j).NumWholeComments;
                     CntrSummaryResults.get(i).NumEmbeddedCmnts += cntrResults.get(j).NumEmbeddedComments;
                     CntrSummaryResults.get(i).NumCompDirs += cntrResults.get(j).NumCompilerDirectives;
                     CntrSummaryResults.get(i).NumDataDeclPhys += cntrResults.get(j).NumDataDeclPhys;
                     CntrSummaryResults.get(i).NumExecInstrPhys += cntrResults.get(j).NumExecInstrPhys;
                     CntrSummaryResults.get(i).NumDataDeclLog += cntrResults.get(j).NumDataDeclLog;
                     CntrSummaryResults.get(i).NumExecInstrLog += cntrResults.get(j).NumExecInstrLog;
                     CntrSummaryResults.get(i).NumPSLOC += cntrResults.get(j).NumPSLOC;
                     CntrSummaryResults.get(i).NumLSLOC += cntrResults.get(j).NumLSLOC;

                     // Fill out the language version for reporting purpose
                     CntrSummaryResults.get(i).LangVersion = cntrResults.get(j).LangVersion;

                     // Initialize complex counter data only once per language
                     if (!cmplxSumInit)
                     {
                        InitLangCmplxCntrSummary(cntrResults.get(j), CntrSummaryResults.get(i));
                        cmplxSumInit = true;
                     }

                     // Compute total counts for each keyword occurrence
                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(j).CmplxMathCnts,
                              CntrSummaryResults.get(i).CmplxMathCnts);
                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(j).CmplxTrigCnts,
                              CntrSummaryResults.get(i).CmplxTrigCnts);
                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(j).CmplxLogCnts,
                              CntrSummaryResults.get(i).CmplxLogCnts);
                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(j).CmplxCalcCnts,
                              CntrSummaryResults.get(i).CmplxCalcCnts);
                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(j).CmplxCondCnts,
                              CntrSummaryResults.get(i).CmplxCondCnts);
                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(j).CmplxLogicCnts,
                              CntrSummaryResults.get(i).CmplxLogicCnts);
                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(j).CmplxPreprocCnts,
                              CntrSummaryResults.get(i).CmplxPreprocCnts);
                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(j).CmplxAssignCnts,
                              CntrSummaryResults.get(i).CmplxAssignCnts);
                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(j).CmplxPntrCnts,
                              CntrSummaryResults.get(i).CmplxPntrCnts);

                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(j).DataKeywordCnts,
                              CntrSummaryResults.get(i).DataKeywrdCnts);
                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(j).ExecKeywordCnts,
                              CntrSummaryResults.get(i).ExecKeywrdCnts);
                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(j).BoolOperandCnts,
                              CntrSummaryResults.get(i).BoolOperandCnts);

                  }

                  CntrSummaryResults.get(i).NumTotalFiles++;
                  // Decrement the count for the top-level embedded file so we
                  // don't double up on counts
                  if (cntrResults.get(j).HasEmbCode)
                  {
                     CntrSummaryResults.get(i).NumTotalFiles--;
                  }
               }
               else
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

                  // Only get data from files that are counted and are
                  // duplicates
                  if (cntrResults.get(idx).IsCounted)
                  {
                     // Fill out the summary data for the language
                     DupCntrSummaryResults.get(i).NumCountedFiles++;
                     // Decrement the count for the top-level embedded file so
                     // we don't double up on counts
                     if (cntrResults.get(idx).HasEmbCode)
                     {
                        DupCntrSummaryResults.get(i).NumCountedFiles--;
                     }
                     DupCntrSummaryResults.get(i).NumTotalLines += cntrResults.get(idx).NumTotalLines;
                     DupCntrSummaryResults.get(i).NumBlankLines += cntrResults.get(idx).NumBlankLines;
                     DupCntrSummaryResults.get(i).NumWholeCmnts += cntrResults.get(idx).NumWholeComments;
                     DupCntrSummaryResults.get(i).NumEmbeddedCmnts += cntrResults.get(idx).NumEmbeddedComments;
                     DupCntrSummaryResults.get(i).NumCompDirs += cntrResults.get(idx).NumCompilerDirectives;
                     DupCntrSummaryResults.get(i).NumDataDeclLog += cntrResults.get(idx).NumDataDeclLog;
                     DupCntrSummaryResults.get(i).NumExecInstrLog += cntrResults.get(idx).NumExecInstrLog;
                     DupCntrSummaryResults.get(i).NumDataDeclPhys += cntrResults.get(idx).NumDataDeclPhys;
                     DupCntrSummaryResults.get(i).NumExecInstrPhys += cntrResults.get(idx).NumExecInstrPhys;
                     DupCntrSummaryResults.get(i).NumPSLOC += cntrResults.get(idx).NumPSLOC;
                     DupCntrSummaryResults.get(i).NumLSLOC += cntrResults.get(idx).NumLSLOC;

                     // Fill out the language version for reporting purpose
                     DupCntrSummaryResults.get(i).LangVersion = cntrResults.get(idx).LangVersion;

                     // Initialize duplicate files complex counter data only
                     // once per language
                     if (!dupCmplxSumInit)
                     {
                        InitLangCmplxCntrSummary(cntrResults.get(idx), DupCntrSummaryResults.get(i));
                        dupCmplxSumInit = true;
                     }

                     // Compute total counts for each keyword occurrence
                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(idx).CmplxMathCnts,
                              DupCntrSummaryResults.get(i).CmplxMathCnts);
                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(idx).CmplxTrigCnts,
                              DupCntrSummaryResults.get(i).CmplxTrigCnts);
                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(idx).CmplxLogCnts,
                              DupCntrSummaryResults.get(i).CmplxLogCnts);
                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(idx).CmplxCalcCnts,
                              DupCntrSummaryResults.get(i).CmplxCalcCnts);
                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(idx).CmplxCondCnts,
                              DupCntrSummaryResults.get(i).CmplxCondCnts);
                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(idx).CmplxLogicCnts,
                              DupCntrSummaryResults.get(i).CmplxLogicCnts);
                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(idx).CmplxPreprocCnts,
                              DupCntrSummaryResults.get(i).CmplxPreprocCnts);
                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(idx).CmplxAssignCnts,
                              DupCntrSummaryResults.get(i).CmplxAssignCnts);
                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(idx).CmplxPntrCnts,
                              DupCntrSummaryResults.get(i).CmplxPntrCnts);

                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(idx).DataKeywordCnts,
                              DupCntrSummaryResults.get(i).DataKeywrdCnts);
                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(idx).ExecKeywordCnts,
                              DupCntrSummaryResults.get(i).ExecKeywrdCnts);
                     ComputeCmplxKeywordSummaryCnt(cntrResults.get(j).BoolOperandCnts,
                              CntrSummaryResults.get(i).BoolOperandCnts);

                     DupResFound = true;
                  }

                  DupCntrSummaryResults.get(i).NumTotalFiles++;

                  // Decrement the count for the top-level embedded file so we
                  // don't double up on counts
                  if (cntrResults.get(idx).HasEmbCode)
                  {
                     DupCntrSummaryResults.get(i).NumTotalFiles--;
                  }
               }
            }

            // Determine if uncounted file report needs to be generated
            if (!cntrResults.get(j).IsCounted && !cntrResults.get(j).IsDup)
            {
               UncountedFilesFound = true;
            }
         }
      }

      logger.debug("Finished computing summary data for each language from each file's counter results");
   }

   /**
    * Computes summary complexity count data for each file from given counter
    * results data
    *
    * @param cntrResults
    *           Counter results, organized per file
    */
   protected void ComputeCmplxCntrSummaryResults(ArrayList<UCCFile> cntrResults)
   {
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
               // For files with embedded code, complexity results are stored in
               // the embedded files
               if (cntrResults.get(i).IsCounted && !cntrResults.get(i).HasEmbCode)
               {
                  if (!cntrResults.get(i).CmplxTrigCnts.isEmpty() || !cntrResults.get(i).CmplxLogCnts.isEmpty()
                           || !cntrResults.get(i).CmplxCalcCnts.isEmpty() || !cntrResults.get(i).CmplxMathCnts.isEmpty()
                           || !cntrResults.get(i).CmplxCondCnts.isEmpty()
                           || !cntrResults.get(i).CmplxLogicCnts.isEmpty()
                           || !cntrResults.get(i).CmplxPreprocCnts.isEmpty()
                           || !cntrResults.get(i).CmplxAssignCnts.isEmpty()
                           || !cntrResults.get(i).BoolOperandCnts.isEmpty()
                           || !cntrResults.get(i).CmplxLoopLvlCnts.isEmpty())
                  {
                     // Fill out the complexity summary data for the file
                     CmplxCntrSummaryResults.add(new ComplexCounterResultsSummary(cntrResults.get(i).FileName,
                              cntrResults.get(i).LangProperty.name(),
                              GetCmplxKeywordSummaryCnt(cntrResults.get(i).CmplxTrigCnts),
                              GetCmplxKeywordSummaryCnt(cntrResults.get(i).CmplxLogCnts),
                              GetCmplxKeywordSummaryCnt(cntrResults.get(i).CmplxCalcCnts),
                              GetCmplxKeywordSummaryCnt(cntrResults.get(i).CmplxMathCnts),
                              GetCmplxKeywordSummaryCnt(cntrResults.get(i).CmplxCondCnts),
                              GetCmplxKeywordSummaryCnt(cntrResults.get(i).CmplxLogicCnts),
                              GetCmplxKeywordSummaryCnt(cntrResults.get(i).CmplxPreprocCnts),
                              GetCmplxKeywordSummaryCnt(cntrResults.get(i).CmplxAssignCnts),
                              GetCmplxKeywordSummaryCnt(cntrResults.get(i).CmplxPntrCnts),
                              GetCmplxKeywordSummaryCnt(cntrResults.get(i).DataKeywordCnts),
                              GetCmplxKeywordSummaryCnt(cntrResults.get(i).ExecKeywordCnts),
                              cntrResults.get(i).CmplxLoopLvlCnts));
                  }

                  // Determine whether cyclomatic complexity results are
                  // available
                  if (!cntrResults.get(i).CyclCmplxCnts.isEmpty())
                  {
                     CyclomaticComplexityResFound = true;
                  }
               }
            }
            else
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

               // Only get data from files that are counted and are duplicates
               // For files with embedded code, complexity results are stored in
               // the embedded files
               if (cntrResults.get(idx).IsCounted && !cntrResults.get(idx).HasEmbCode)
               {
                  if (!cntrResults.get(idx).CmplxTrigCnts.isEmpty() || !cntrResults.get(idx).CmplxLogCnts.isEmpty()
                           || !cntrResults.get(idx).CmplxCalcCnts.isEmpty()
                           || !cntrResults.get(idx).CmplxMathCnts.isEmpty()
                           || !cntrResults.get(idx).CmplxCondCnts.isEmpty()
                           || !cntrResults.get(idx).CmplxLogicCnts.isEmpty()
                           || !cntrResults.get(idx).CmplxPreprocCnts.isEmpty()
                           || !cntrResults.get(idx).CmplxAssignCnts.isEmpty()
                           || !cntrResults.get(idx).CmplxLoopLvlCnts.isEmpty())
                  {
                     // Fill out the complexity summary data for the file
                     DupCmplxCntrSummaryResults.add(new ComplexCounterResultsSummary(cntrResults.get(i).FileName,
                              cntrResults.get(idx).LangProperty.name(),
                              GetCmplxKeywordSummaryCnt(cntrResults.get(idx).CmplxTrigCnts),
                              GetCmplxKeywordSummaryCnt(cntrResults.get(idx).CmplxLogCnts),
                              GetCmplxKeywordSummaryCnt(cntrResults.get(idx).CmplxCalcCnts),
                              GetCmplxKeywordSummaryCnt(cntrResults.get(idx).CmplxMathCnts),
                              GetCmplxKeywordSummaryCnt(cntrResults.get(idx).CmplxCondCnts),
                              GetCmplxKeywordSummaryCnt(cntrResults.get(idx).CmplxLogicCnts),
                              GetCmplxKeywordSummaryCnt(cntrResults.get(idx).CmplxPreprocCnts),
                              GetCmplxKeywordSummaryCnt(cntrResults.get(idx).CmplxAssignCnts),
                              GetCmplxKeywordSummaryCnt(cntrResults.get(idx).CmplxPntrCnts),
                              GetCmplxKeywordSummaryCnt(cntrResults.get(i).DataKeywordCnts),
                              GetCmplxKeywordSummaryCnt(cntrResults.get(i).ExecKeywordCnts),
                              cntrResults.get(idx).CmplxLoopLvlCnts));
                  }

                  // Determine whether cyclomatic complexity results are
                  // available
                  if (!cntrResults.get(idx).CyclCmplxCnts.isEmpty())
                  {
                     CyclomaticComplexityResFound = true;
                  }
               }
            }
         }
      }

      logger.debug("Finished computing summary complexity count data for each file from counter results data");
   }

   protected void SumEmbeddedLanguageCounts(ArrayList<UCCFile> cntrResults)
   {
      // Sum up the embedded counts and save them in the top-level file
      for (int j = 0; j < cntrResults.size(); j++)
      {
         if (cntrResults.get(j).EmbOfIdx != -1)
         {
            cntrResults.get(cntrResults.get(j).EmbOfIdx).NumTotalLines += cntrResults.get(j).NumTotalLines;
            cntrResults.get(cntrResults.get(j).EmbOfIdx).NumBlankLines += cntrResults.get(j).NumBlankLines;
            cntrResults.get(cntrResults.get(j).EmbOfIdx).NumWholeComments += cntrResults.get(j).NumWholeComments;
            cntrResults.get(cntrResults.get(j).EmbOfIdx).NumEmbeddedComments += cntrResults.get(j).NumEmbeddedComments;
            cntrResults.get(cntrResults.get(j).EmbOfIdx).NumCompilerDirectives +=
                     cntrResults.get(j).NumCompilerDirectives;
            cntrResults.get(cntrResults.get(j).EmbOfIdx).NumDataDeclLog += cntrResults.get(j).NumDataDeclLog;
            cntrResults.get(cntrResults.get(j).EmbOfIdx).NumExecInstrLog += cntrResults.get(j).NumExecInstrLog;
            cntrResults.get(cntrResults.get(j).EmbOfIdx).NumLSLOC += cntrResults.get(j).NumLSLOC;
            cntrResults.get(cntrResults.get(j).EmbOfIdx).NumPSLOC += cntrResults.get(j).NumPSLOC;
         }
      }
   }

   /**
    * High-level method to generate all counter reports
    *
    * @param cntrResults
    *           An ArrayList of UCCFile objects that contain code counter
    *           results to be used in creation of counter reports
    */
   public abstract void GenCntrReports(ArrayList<UCCFile> cntrResults);

   /**
    * Generates counter reports based on language type. There is a separate
    * report for each type of language found in the baseline(s).
    *
    * @param cntrResults
    *           An ArrayList of UCCFile objects that contain code counter
    *           results to be used in creation of counter reports
    */
   protected abstract void GenLangCntrReport(ArrayList<UCCFile> cntrResults);

   /**
    * Generates one unified counter report for all files in the baseline(s).
    *
    * @param cntrResults
    *           An ArrayList of UCCFile objects that contain code counter
    *           results to be used in creation of counter reports
    */
   protected abstract void GenUnifiedCntrReport(ArrayList<UCCFile> cntrResults);

   /**
    * Generates summary counter report for all language types. Summary report
    * contains aggregated metrics for each type of language found in the
    * baseline(s).
    *
    * @param cntrResults
    *           An ArrayList of UCCFile objects that contain code counter
    *           results to be used in creation of counter reports
    */
   protected abstract void GenSummaryCntrReport(ArrayList<UCCFile> cntrResults);

   /**
    * Generates uncounted file report for unsupported language types. Uncounted
    * report contains a message giving the reason the file was not counted and
    * the file which was not counted.
    *
    * @param cntrResults
    *           An ArrayList of UCCFile objects that contain code counter
    *           results to be used in creation of counter reports
    */
   protected abstract void GenUncountedReport(ArrayList<UCCFile> cntrResults);

   /**
    * Generates complexity counter report for all language types.
    */
   protected abstract void GenCmplxCntrReport();

   /**
    * Generates cyclomatic complexity counter report for all language types.
    * 
    * @param cntrResults
    *           An ArrayList of UCCFile objects that contain code counter
    *           results to be used in creation of counter reports
    */
   protected abstract void GenCyclCmplxCntrReport(ArrayList<UCCFile> cntrResults);

   /**
    * Generates Halstead counter report for all language types.
    */
   protected abstract void GenHalsteadCntrReport();

   /**
    * Generates a report that contains duplicate file pairs of a baseline.
    *
    * @param cntrResults
    *           An ArrayList of UCCFile objects that contain code counter
    *           results to be used in creation of counter reports
    */
   protected abstract void GenDupPairsReport(ArrayList<UCCFile> cntrResults);

   /**
    * Generates summary counter report for all language types for duplicate
    * files within a baseline. Summary report contains aggregated metrics for
    * each type of language found in duplicate files within a baseline.
    * 
    * @param cntrResults
    *           An ArrayList of UCCFile objects that contain code counter
    *           results to be used in creation of counter reports
    */
   protected abstract void GenDupSummaryCntrReport(ArrayList<UCCFile> cntrResults);

   /**
    * Generates counter reports based on language type for duplicate files
    * within a baseline. There is a separate report for each type of language
    * found in duplicate files within a baseline.
    * 
    * @param cntrResults
    *           An ArrayList of UCCFile objects that contain code counter
    *           results to be used in creation of counter reports
    */
   protected abstract void GenDupLangCntrReport(ArrayList<UCCFile> cntrResults);

   /**
    * Generates one unified counter report for all duplicate files found in the
    * baseline(s).
    *
    * @param cntrResults
    *           An ArrayList of UCCFile objects that contain code counter
    *           results to be used in creation of counter reports
    */
   protected abstract void GenDupUnifiedCntrReport(ArrayList<UCCFile> cntrResults);

   /**
    * Generates complexity counter reports based on language type for duplicate
    * files within a baseline. There is a separate report for each type of
    * language found in duplicate files within a baseline.
    */
   protected abstract void GenDupCmplxCntrReport();

   /**
    * Generates cyclomatic complexity counter reports based on language type for
    * duplicate files within a baseline. There is a separate report for each
    * type of language found in duplicate files within a baseline.
    * 
    * @param cntrResults
    *           An ArrayList of UCCFile objects that contain code counter
    *           results to be used in creation of counter reports
    */
   protected abstract void GenDupCyclCmplxCntrReport(ArrayList<UCCFile> cntrResults);
}
