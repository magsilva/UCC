package ucc.diff;

import java.util.ArrayList;

import ucc.datatypes.DiffResultType;
import ucc.datatypes.UCCFile;
import ucc.utils.FileUtils;

/**
 * A differencer class that produces results at the file level.
 * <p>
 * This class matches files between two baselines to generate a matched files
 * list. The files from the matched files list will then be used in differencing
 * the files at the SLOC level.
 *
 * @author Integrity Applications Incorporated
 */
public class FileMatcher
{
   /**
    * Default constructor
    */
   public FileMatcher()
   {
   }

   /**
    * Matches files between baseline A and baseline B to create a matched files
    * list, which will then be used for differencing the two baselines
    * 
    * @param cntrResultsA
    *           Counter results data for baseline A
    * @param cntrResultsB
    *           Counter results data for baseline B
    * @param fileBIdx
    *           An array of indices into cntrResultsB object for files that are
    *           to be used for matching
    * @param diffResults
    *           Differencer results data object
    */
   void GenMatchedFileList(ArrayList<UCCFile> cntrResultsA, ArrayList<UCCFile> cntrResultsB,
            ArrayList<Integer> fileBIdx, ArrayList<DiffResultType> diffResults)
   {
      // File objects that contain counter results data
      UCCFile fileA;
      UCCFile fileB;

      // A boolean to indicate if two files are identical
      boolean identical = false;

      // A boolean to indicate if file A found a matching pair in baseline B
      boolean matchFound = false;

      // Loop through duplicate baseline A files and compare them to
      // duplicate baseline B files to find matching pairs (if any)
      for (int i = 0; i < cntrResultsA.size(); i++)
      {
         fileA = cntrResultsA.get(i);
         if (!fileA.IsDup && fileA.EmbOfIdx == -1)
         {
            if (fileA.IsCounted)
            {
               matchFound = false;
               for (int j = 0; j < fileBIdx.size(); j++)
               {
                  // Retrieve the next file to check from the baseline B's index
                  // list
                  fileB = cntrResultsB.get(fileBIdx.get(j).intValue());

                  if (!fileB.IsDup && fileB.EmbOfIdx == -1)
                  {
                     if (fileB.IsCounted)
                     {
                        // Compare the two files
                        if (CompareFiles(fileA, fileB, identical))
                        {
                           diffResults.add(new DiffResultType(fileA.FileName, fileB.FileName, fileA.LangProperty,
                                    identical, false, fileA, fileB));

                           matchFound = true;

                           // Remove matched file index from the list
                           fileBIdx.remove(j);

                           // Decrement index since the current index element
                           // was removed from the list
                           if (j - 1 >= 0)
                           {
                              j--;
                           }
                           break;
                        }
                     }
                     else // File could not be counted
                     {
                        // Remove file index from the list
                        fileBIdx.remove(j);

                        // Decrement index since the current index element was
                        // removed from the list
                        if (j - 1 >= 0)
                        {
                           j--;
                        }
                     }
                  }
               }

               if (!matchFound)
               {
                  // Save file A as not having a matched pair
                  diffResults.add(
                           new DiffResultType(fileA.FileName, "NA", fileA.LangProperty, false, false, fileA, null));
               }
            }
         }
      }

      // Add any remaining duplicate files in baseline B to the matched pairs
      // list as not having a match
      for (int idx = 0; idx < fileBIdx.size(); idx++)
      {
         // Retrieve the next file to check from the baseline B's index list
         fileB = cntrResultsB.get(fileBIdx.get(idx).intValue());

         if (!fileB.IsDup && fileB.IsCounted)
         {
            diffResults.add(new DiffResultType("NA", fileB.FileName, fileB.LangProperty, false, false, null, fileB));
         }
      }
   }

   /**
    * Generates matched pairs from duplicate files in both baselines to create a
    * matched files list, which will then be used for differencing the two
    * baselines
    * 
    * @param cntrResultsA
    *           Counter results data for baseline A
    * @param cntrResultsB
    *           Counter results data for baseline B
    * @param fileBIdx
    *           An array of indices into cntrResultsB object for files that are
    *           to be used for matching
    * @param dupDiffResults
    *           Differencer results data object
    */
   void GenDupMatchedFileList(ArrayList<UCCFile> cntrResultsA, ArrayList<UCCFile> cntrResultsB,
            ArrayList<Integer> fileBIdx, ArrayList<DiffResultType> dupDiffResults)
   {
      // File objects that contain counter results data
      UCCFile fileA;
      UCCFile fileB;

      // A boolean to indicate if two files are identical
      boolean identical = false;

      // A boolean to indicate if file A found a matching pair in baseline B
      boolean matchFound = false;

      // Loop through duplicate baseline A files and compare them to
      // duplicate baseline B files to find matching pairs (if any)
      for (int i = 0; i < cntrResultsA.size(); i++)
      {
         fileA = cntrResultsA.get(i);
         if (fileA.IsDup && fileA.EmbOfIdx == -1)
         {
            if (cntrResultsA.get(fileA.DupOfIdx).IsCounted)
            {
               matchFound = false;
               for (int j = 0; j < fileBIdx.size(); j++)
               {
                  // Retrieve the next file to check from the baseline B's index
                  // list
                  fileB = cntrResultsB.get(fileBIdx.get(j).intValue());

                  if (fileB.IsDup && fileB.EmbOfIdx == -1)
                  {
                     if (cntrResultsB.get(fileB.DupOfIdx).IsCounted)
                     {
                        // Compare the two files
                        if (CompareFiles(fileA, fileB, identical))
                        {
                           dupDiffResults.add(new DiffResultType(fileA.FileName, fileB.FileName, fileA.LangProperty,
                                    identical, true, fileA, fileB));

                           matchFound = true;

                           // Remove matched file index from the list
                           fileBIdx.remove(j);

                           // Decrement index since the current index element
                           // was removed from the list
                           if (j - 1 >= 0)
                           {
                              j--;
                           }
                           break;
                        }
                     }
                     else // File could not be counted
                     {
                        // Remove file index from the list
                        fileBIdx.remove(j);

                        // Decrement index since the current index element was
                        // removed from the list
                        if (j - 1 >= 0)
                        {
                           j--;
                        }
                     }
                  }
               }

               if (!matchFound)
               {
                  // Save file A as not having a matched pair
                  dupDiffResults
                           .add(new DiffResultType(fileA.FileName, "NA", fileA.LangProperty, false, true, fileA, null));
               }
            }
         }
      }

      // Add any remaining duplicate files in baseline B to the matched pairs
      // list as not having a match
      for (int idx = 0; idx < fileBIdx.size(); idx++)
      {
         // Retrieve the next file to check from the baseline B's index list
         fileB = cntrResultsB.get(fileBIdx.get(idx).intValue());

         if (fileB.IsDup && cntrResultsB.get(fileB.DupOfIdx).IsCounted)
         {
            dupDiffResults.add(new DiffResultType("NA", fileB.FileName, fileB.LangProperty, false, true, null, fileB));
         }
      }
   }

   /**
    * Compare two files from different baselines to check if they are a match
    * 
    * @param fileA
    *           A file from baseline A
    * @param fileB
    *           A file from baseline B
    * @param identicalFiles
    *           True if fileA and fileB are identical files
    * @return Returns true if both input files contents are similar
    */
   private boolean CompareFiles(UCCFile fileA, UCCFile fileB, boolean identicalFiles)
   {
      // Extract only the file name from the full file path
      String fileNameA = FileUtils.GetFileName(fileA.FileName);
      String fileNameB = FileUtils.GetFileName(fileB.FileName);

      // Indicates if files are a match
      boolean filesMatch = false;

      // Indicates if files are identical
      identicalFiles = false;

      // Check if the two files are identical - both names and contents match
      if (fileNameA.equals(fileNameB) && fileA.FileChecksum == fileB.FileChecksum)
      {
         filesMatch = true;
         identicalFiles = true;
      }
      // If file names match, consider the two files as a pair
      else if (fileNameA.equals(fileNameB))
      {
         filesMatch = true;
      }
      // If file contents match, consider the two files as a pair
      else if (fileA.FileChecksum > 0 && fileB.FileChecksum > 0 && fileA.FileChecksum == fileB.FileChecksum)
      {
         filesMatch = true;
      }

      return filesMatch;
   }
}
