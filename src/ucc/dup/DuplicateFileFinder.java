package ucc.dup;

import java.io.File;
import java.util.ArrayList;

import ucc.datatypes.UCCFile;
import ucc.utils.ChecksumUtils;
import ucc.utils.FileUtils;
import ucc.utils.ProgressVisualizer;
import ucc.utils.TimeUtils;

/**
 * This class contains all the common duplicate search operations. Functions in
 * this class are static so they can be called directly without instantiating an
 * object of this class.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class DuplicateFileFinder
{
   /**
    * Identifies any duplicate files within a baseline based on the checksum of
    * the file
    *
    * @param cntrResults
    *           contains the list of results found in UCCFile
    */
   public static void IdentifyDupFiles(ArrayList<UCCFile> cntrResults)
   {
      if (cntrResults != null && !cntrResults.isEmpty())
      {
         ProgressVisualizer progressVisualizer = new ProgressVisualizer("Performing duplicate identification");

         long startTime = 0;
         long endTime = 0;
         long fileLength = 0;
         File file;

         startTime = TimeUtils.GetTime();

         // Loop through all of the files in the counter results object
         for (int i = 0; i < cntrResults.size(); i++)
         {
            file = new File(cntrResults.get(i).FileName);
            fileLength = file.length();

            // If the length of the file is greater than zero
            if (fileLength > 0)
            {
               // If the file has an associated language property
               if (cntrResults.get(i).LangProperty != null)
               {
                  // Get the checksum of the file
                  cntrResults.get(i).FileChecksum = ChecksumUtils.GetFileChecksum(file);
               }
            }
         }
         endTime = TimeUtils.GetTime();
         TimeUtils.PrintElapsedTime(startTime, endTime, "Checksum calculation");

         startTime = TimeUtils.GetTime();

         // Loop through all of the files in the counter results object
         for (int i = 0; i < cntrResults.size() - 1; i++)
         {
            // Only check for duplicates if the file is not a duplicate of
            // another file
            if (!cntrResults.get(i).IsDup)
            {
               for (int j = i + 1; j < cntrResults.size(); j++)
               {
                  // If the files have associated language properties
                  if (cntrResults.get(i).LangProperty != null && cntrResults.get(j).LangProperty != null)
                  {
                     // Files are duplicate of each other if their checksums
                     // match
                     if (cntrResults.get(i).FileChecksum == cntrResults.get(j).FileChecksum)
                     {
                        // Mark the second file as duplicate of the first file
                        cntrResults.get(j).IsDup = true;

                        // Save the index of the first file as the duplicate of
                        // index
                        cntrResults.get(j).DupOfIdx = i;

                        // Set the duplicate code percentage to 100% since the
                        // files are identical
                        cntrResults.get(j).DupCodePercent = 100.0;
                     }
                  }

                  // Print the progress to the screen for duplicate
                  // identification
                  progressVisualizer
                           .printProgressBarWithThreshold((j + i * ((cntrResults.size() - 1) * cntrResults.size()))
                                    / (double) ((cntrResults.size() - 1) * cntrResults.size()));
               }
            }

            // Print the progress to the screen for duplicate identification
            progressVisualizer.printProgressBarWithThreshold((i * ((cntrResults.size() - 1) * cntrResults.size()))
                     / (double) ((cntrResults.size() - 1) * cntrResults.size()));
         }

         // Print the progress to the screen for duplicate identification
         progressVisualizer.printProgressBarWithThreshold(ProgressVisualizer.DONE);

         endTime = TimeUtils.GetTime();
         TimeUtils.PrintElapsedTime(startTime, endTime, "Duplicate check");
      }
   }

   /**
    * Finds duplicate code amongst files
    * 
    * @param cntrResults
    *           An array of UCC file objects containing counter information for
    *           files
    * @param dupThreshold
    *           A threshold for duplicate code identification
    */
   public static void FindDuplicateCode(ArrayList<UCCFile> cntrResults, double dupThreshold)
   {
      ProgressVisualizer progressVisualizer = new ProgressVisualizer("Performing duplicate checking");

      double dupPercent = 0.0;
      ArrayList<Integer> fileAChecksums = new ArrayList<Integer>();
      ArrayList<Integer> fileBChecksums = new ArrayList<Integer>();
      String filenameA;
      String filenameB;

      // Loop through files 0 through N-1...
      for (int i = 0; i < cntrResults.size() - 1; i++)
      {
         // If file A has an associated language property, is not a duplicate,
         // has some checksummed lines, and is not an
         // embedded language file
         if (cntrResults.get(i).LangProperty != null && !cntrResults.get(i).IsDup
                  && cntrResults.get(i).FileLineChecksum.size() > 0 && cntrResults.get(i).EmbOfIdx == -1)
         {
            // Loop through files 1 through N
            for (int j = i + 1; j < cntrResults.size(); j++)
            {
               // Zero out the arrayLists
               fileAChecksums.clear();
               fileBChecksums.clear();

               // If file B has an associated language property, is not a
               // duplicate, has some checksummed lines, and is
               // not an
               // embedded language file
               if (cntrResults.get(j).LangProperty != null && !cntrResults.get(j).IsDup
                        && cntrResults.get(j).FileLineChecksum.size() > 0 && cntrResults.get(j).EmbOfIdx == -1)
               {
                  // If file A has the same name as file B
                  filenameA = FileUtils.GetFileName(cntrResults.get(i).FileName);
                  filenameB = FileUtils.GetFileName(cntrResults.get(j).FileName);

                  if (filenameA.equals(filenameB))
                  {
                     // Loop through all the checksummed lines in file A
                     for (int k = 0; k < cntrResults.get(i).FileLineChecksum.size(); k++)
                     {
                        // Loop through all the checksummed lines in file B
                        for (int l = 0; l < cntrResults.get(j).FileLineChecksum.size(); l++)
                        {
                           // If the A arrayList doesn't already contain the k
                           // element
                           // and If the B arrayList doesn't already contain the
                           // l element
                           // and If the checksums match
                           if (!fileAChecksums.contains(k) && !fileBChecksums.contains(l)
                                    && cntrResults.get(i).FileLineChecksum.get(k)
                                             .equals(cntrResults.get(j).FileLineChecksum.get(l)))
                           {
                              // Add k and l to their respective lists
                              fileAChecksums.add(k);
                              fileBChecksums.add(l);
                           }
                        }
                     }

                     // Get the duplicate percent of file B (number of matched
                     // checksums / total number of checksums)
                     dupPercent = (double) (((double) fileBChecksums.size()
                              / (double) cntrResults.get(j).FileLineChecksum.size()) * 100.0);

                     // If the duplicate percent is greater than our
                     // threshold...
                     if (dupPercent >= dupThreshold)
                     {
                        cntrResults.get(j).IsDup = true;
                        cntrResults.get(j).DupOfIdx = i;

                        // Set duplicate code percentage of the file
                        cntrResults.get(j).DupCodePercent = dupPercent;
                     }

                     // Reset duplicate percent variables
                     dupPercent = 0.0;
                  }
               }

               // Print the progress to the screen for duplicate checking
               progressVisualizer.printProgressBarWithThreshold((j + i * (cntrResults.size() - 1))
                        / ((double) ((cntrResults.size() - 1) * cntrResults.size())));
            }
         }

         // Print the progress to the screen for duplicate checking
         progressVisualizer.printProgressBarWithThreshold(
                  (i * (cntrResults.size() - 1)) / ((double) ((cntrResults.size() - 1) * cntrResults.size())));
      }

      // Print the progress to the screen for duplicate checking
      progressVisualizer.printProgressBarWithThreshold(ProgressVisualizer.DONE);
   }
}
