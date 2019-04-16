package ucc.diff;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ucc.datatypes.DiffResultType;
import ucc.datatypes.UCCFile;
import ucc.main.RuntimeParameters;

/**
 * Top-level class for differencer that acts as a manager to compare two
 * baselines and produce results at the file level and at the SLOC level.
 *
 * @author Integrity Applications Incorporated
 */
public class Differencer
{
   /** Instantiate the Log4j2 logger for this class */
   private static final Logger logger = LogManager.getLogger(FileMatcher.class);

   /** A handle to the RuntimeParameters class's single instance */
   private static RuntimeParameters RtParams;

   /**
    * A FileMatcher object to compare files in two baselines to generate a
    * matched files list between the two baselines
    */
   private FileMatcher FileMatcherObj;

   /**
    * A LSLOCDifferencer object to compare/difference a matched file pair
    * between two baselines
    */
   private LSLOCDifferencer SLOCDiffObj;

   /** File list of the first baseline */
   public ArrayList<String> FileListA;

   /** File list of the second baseline */
   public ArrayList<String> FileListB;

   /** Duplicate file list of the first baseline */
   public ArrayList<String> DupFileListA;

   /** Duplicate file list of the second baseline */
   public ArrayList<String> DupFileListB;

   /** Differencer results for duplicate files */
   ArrayList<DiffResultType> DupDiffResults;

   /**
    * Default constructor
    */
   public Differencer()
   {
      // Initialize class variables
      RtParams = RuntimeParameters.GetInstance();

      FileMatcherObj = new FileMatcher();
      SLOCDiffObj = new LSLOCDifferencer(RtParams.ModThreshold);

      FileListA = new ArrayList<String>();
      FileListB = new ArrayList<String>();
      DupFileListA = new ArrayList<String>();
      DupFileListB = new ArrayList<String>();
   }

   /**
    * Differences two baselines and stores results in a DifferencerResultType
    * object
    * 
    * @param cntrResultsA
    *           Counter results data for baseline A
    * @param cntrResultsB
    *           Counter results data for baseline B
    * @param diffResults
    *           Differencer results data object for non-duplicate files
    * @param dupDiffResults
    *           Differencer results data object for duplicate files
    */
   public void DiffBaselines(ArrayList<UCCFile> cntrResultsA, ArrayList<UCCFile> cntrResultsB,
            ArrayList<DiffResultType> diffResults, ArrayList<DiffResultType> dupDiffResults)
   {
      // Check that counter results are available
      if (cntrResultsA != null && cntrResultsB != null)
      {
         // Initialize differencer results object if null
         if (diffResults == null)
         {
            diffResults = new ArrayList<DiffResultType>();
         }

         // An array list to keep track of which files from baseline B were
         // paired with baseline A files
         ArrayList<Integer> fileBIdx = new ArrayList<Integer>();
         for (int idx = 0; idx < cntrResultsB.size(); idx++)
         {
            if (cntrResultsB.get(idx).EmbOfIdx == -1)
            {
               fileBIdx.add(new Integer(idx));
            }
         }

         // Pair non-duplicate files from baseline A with non-duplicate files
         // from baseline B
         FileMatcherObj.GenMatchedFileList(cntrResultsA, cntrResultsB, fileBIdx, diffResults);

         // Pair duplicate files from baseline A with duplicate files from
         // baseline B
         if (RtParams.SearchForDups)
         {
            FileMatcherObj.GenDupMatchedFileList(cntrResultsA, cntrResultsB, fileBIdx, dupDiffResults);
         }

         // Difference each non-duplicate matched file pair
         for (int i = 0; i < diffResults.size(); i++)
         {
            if (!SLOCDiffObj.CompareFiles(diffResults.get(i)))
            {
               logger.error("Error comparing file " + diffResults.get(i).FileNameA + " with "
                        + diffResults.get(i).FileNameB);
            }
         }

         // Difference duplicate file pairs
         if (RtParams.SearchForDups)
         {
            for (int i = 0; i < dupDiffResults.size(); i++)
            {
               if (!SLOCDiffObj.CompareFiles(dupDiffResults.get(i)))
               {
                  logger.error("Error comparing file " + dupDiffResults.get(i).FileNameA + " with "
                           + dupDiffResults.get(i).FileNameB);
               }
            }
         }
      }
      else
      {
         logger.error("Counter results for baseline(s) are not available, cannot proceed with differencing.");
      }
   }
}
