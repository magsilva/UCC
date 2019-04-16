package ucc.diff;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import ucc.counters.CodeCounter;
import ucc.datatypes.Constants;
import ucc.datatypes.DiffResultType;
import ucc.datatypes.DiffResultType.ModificationType;
import ucc.main.RuntimeParameters;
import ucc.utils.FileUtils;

/**
 * A differencer class that produces results at the logical source line of code
 * (LSLOC) level.
 * <p>
 * This class contains operations to compare LSLOC between two files for
 * differencing.
 * 
 * @author Integrity Applications Incorporated
 */
public class LSLOCDifferencer
{
   /** SLOC modification threshold value */
   private double ModThreshold;

   /** Contents of file A - one line per element */
   private ArrayList<String> FileALines;

   /** Contents of file B - one line per element */
   private ArrayList<String> FileBLines;

   /** Get a handle to the single instance of RuntimeParameters object */
   private static RuntimeParameters RtParams = RuntimeParameters.GetInstance();

   /**
    * Default constructor
    */
   public LSLOCDifferencer()
   {
      // Default modification threshold
      ModThreshold = Constants.DEFAULT_MOD_THRESHOLD;

      FileALines = new ArrayList<String>();
      FileBLines = new ArrayList<String>();
   }

   /**
    * Constructor
    * 
    * @param modThresh
    *           Modification threshold value
    */
   public LSLOCDifferencer(double modThresh)
   {
      SetModThreshold(modThresh);
      FileALines = new ArrayList<String>();
      FileBLines = new ArrayList<String>();
   }

   /**
    * Set modification threshold to given value
    * 
    * @param modThresh
    *           Modification threshold value
    */
   public void SetModThreshold(double modThresh)
   {
      ModThreshold = modThresh;
   }

   /**
    * Compares contents of two files to identify differences in SLOC between the
    * two files
    * 
    * @param diffRes
    *           An object to store differencer results of the file pair
    * @return True if successfully processed. False, otherwise
    */
   public boolean CompareFiles(DiffResultType diffRes)
   {
      boolean processed = false;

      // Open files for reading
      if (diffRes.FileNameA == "NA")
      {
         if (diffRes.FileNameB == "NA")
         {
            diffRes.IsDiffed = false;
            System.err.println("Differencer: cannot compare files, invalid file names");
         }
         else
         {
            // There is no file to compare against, count file B's lines as new
            // lines
            if (FileUtils.ReadLines(FileUtils.BuildTempOutFileName_LSLOC(RtParams, diffRes.FileB), FileBLines))
            {
               diffRes.UnmodLines = 0;
               diffRes.ModLines = 0;
               diffRes.NewLines = CountNonBlankLines(FileBLines);
               diffRes.DeletedLines = 0;
               diffRes.ModType = ModificationType.Add;
               diffRes.IsDiffed = true;
               processed = true;
            }
         }
      }
      else if (diffRes.FileNameB == "NA")
      {
         // There is no file to compare against, count file A's lines as deleted
         // lines
         if (FileUtils.ReadLines(FileUtils.BuildTempOutFileName_LSLOC(RtParams, diffRes.FileA), FileALines))
         {
            diffRes.UnmodLines = 0;
            diffRes.ModLines = 0;
            diffRes.NewLines = 0;
            diffRes.DeletedLines = CountNonBlankLines(FileALines);
            diffRes.ModType = ModificationType.Del;
            diffRes.IsDiffed = true;
            processed = true;
         }
      }
      else
      {
         if (FileUtils.ReadLines(FileUtils.BuildTempOutFileName_LSLOC(RtParams, diffRes.FileA), FileALines)
                  && FileUtils.ReadLines(FileUtils.BuildTempOutFileName_LSLOC(RtParams, diffRes.FileB), FileBLines))
         {
            // Count lines that are exact match between the two files
            diffRes.UnmodLines = CountUnmodLines(FileALines, FileBLines);

            // Count lines that were modified between the two files
            int[] modLineCounts = CountModLines(FileALines, FileBLines);
            diffRes.ModLines = modLineCounts[0];
            diffRes.NewLines = modLineCounts[1];
            diffRes.DeletedLines = modLineCounts[2];

            // Count new lines (leftover lines in file B)
            diffRes.NewLines += CountNonBlankLines(FileBLines);

            // Count deleted lines (leftover lines in file A)
            diffRes.DeletedLines += CountNonBlankLines(FileALines);

            // If any lines are modified, new, or deleted, then the file is
            // modified
            if (diffRes.ModLines > 0 || diffRes.NewLines > 0 || diffRes.DeletedLines > 0)
            {
               diffRes.ModType = ModificationType.Mod;
            }
            else
            {
               diffRes.ModType = ModificationType.Unmod;
            }

            diffRes.IsDiffed = true;
            processed = true;
         }
      }

      // Clear the two string buffers containing file contents so they can be
      // re-used for next file pair comparison
      FileALines.clear();
      FileBLines.clear();

      // Delete "_LSLOC" files as they are no longer needed
      if (diffRes.FileNameA != "NA")
      {
         FileUtils.DeleteFile(FileUtils.BuildTempOutFileName_LSLOC(RtParams, diffRes.FileA));
      }

      if (diffRes.FileNameB != "NA")
      {
         FileUtils.DeleteFile(FileUtils.BuildTempOutFileName_LSLOC(RtParams, diffRes.FileB));
      }

      return processed;
   }

   /**
    * Counts number of lines that were modified from file B to file A
    * 
    * @param fileALines
    *           A string buffer that contains file A contents
    * @param fileBLines
    *           A string buffer that contains file B contents
    * @return Number of modified lines between file A and B
    */
   private int[] CountModLines(ArrayList<String> fileALines, ArrayList<String> fileBLines)
   {
      int modCount = 0; // Number of modified lines (increments if threshold is
                        // met)
      int newCount = 0; // Number of new lines as result of a modified line
      int delCount = 0; // Number of deleted lines as result of a modified line
      int levenshteinVal = -1; // Levenstein algorithm value
      int lslocLineValueA = 0;
      int lslocLineValueB = 0;
      String lineA = "";
      String lineB = "";

      // Loop over all the lines in file A...
      for (int i = 0; i < fileALines.size(); i++)
      {
         lineA = fileALines.get(i).trim();
         lslocLineValueA = getLslocLineValue(lineA);
         lineA = removeLslocLineValue(lineA);

         // Loop over all the lines in file B...
         for (int j = 0; j < fileBLines.size(); j++)
         {
            lineB = fileBLines.get(j).trim();
            lslocLineValueB = getLslocLineValue(lineB);
            lineB = removeLslocLineValue(lineB);

            // Get Levenshtein algorithm score for the two lines and check that
            // is less than the threshold
            if (!lineA.isEmpty() && !lineB.isEmpty())
            {
               levenshteinVal = -1;
               levenshteinVal = StringUtils.getLevenshteinDistance(lineA, lineB);

               // If Levenshtein distance is within our threshold, erase the
               // line and increment the modCount
               if (((double) levenshteinVal / (double) lineA.length()) * 100.0 <= (100.0 - ModThreshold))
               {
                  fileALines.set(i, "");
                  fileBLines.set(j, "");

                  lineA = "";
                  lineB = "";

                  if (lslocLineValueB < lslocLineValueA)
                  {
                     delCount += (lslocLineValueA - lslocLineValueB);
                     modCount += lslocLineValueB;
                  }
                  else if (lslocLineValueB > lslocLineValueA)
                  {
                     newCount += (lslocLineValueB - lslocLineValueA);
                     modCount += lslocLineValueA;
                  }
                  else
                  {
                     modCount += lslocLineValueA;
                  }
               }
            }
         }
      }

      int[] counts = { modCount, newCount, delCount };
      return counts;
   }

   /**
    * Counts number of lines that are unmodified from file B to file A
    * 
    * @param fileALines
    *           A string buffer that contains file A contents
    * @param fileBLines
    *           A string buffer that contains file B contents
    * @return Number of lines that are found in both file A and B
    */
   private int CountUnmodLines(ArrayList<String> fileALines, ArrayList<String> fileBLines)
   {
      int unmodCount = 0; // Number of unmodified lines (increments if
                          // Levenstein value is 0)
      int levenshteinVal = -1; // Levenstein algorithm value
      int lslocLineValueA = 0;
      int lslocLineValueB = 0;
      String lineA = "";
      String lineB = "";

      // Loop over all the lines in file A...
      for (int i = 0; i < fileALines.size(); i++)
      {
         lineA = fileALines.get(i).trim();
         lslocLineValueA = getLslocLineValue(lineA);
         lineA = removeLslocLineValue(lineA);

         // Loop over all the lines in file B...
         for (int j = 0; j < fileBLines.size(); j++)
         {
            lineB = fileBLines.get(j).trim();
            lslocLineValueB = getLslocLineValue(lineB);
            lineB = removeLslocLineValue(lineB);

            // Get Levenshtein algorithm score for the two lines
            if (!lineA.isEmpty() && !lineB.isEmpty())
            {
               // If Levenshtein distance is 0, erase the line and increment
               // unmodCount
               levenshteinVal = StringUtils.getLevenshteinDistance(lineA, lineB);
               if (levenshteinVal == 0)
               {
                  fileALines.set(i, "");
                  fileBLines.set(j, "");
                  lineA = "";
                  lineB = "";
                  unmodCount += lslocLineValueA;
               }
            }
         }
      }

      return unmodCount;
   }

   /**
    * Counts number of non-blank LSLOC lines in the given string buffer
    * 
    * @param fileLines
    *           An array list of strings
    * @return Number of non-blank lines in the array list
    */
   private int CountNonBlankLines(ArrayList<String> fileLines)
   {

      int nonblankCount = 0;

      // Get number of new lines by the non-blank entries left in FileBLines
      for (int i = 0; i < fileLines.size(); i++)
      {
         nonblankCount += getLslocLineValue(fileLines.get(i));
      }

      return nonblankCount;
   }

   /**
    * Determines the LSLOC value of a single line based on the provided line
    * value that is determined during the LSLOC Counter operation
    * 
    * @param line
    *           The line to be operated on
    * @return Number of non-blank lines in the array list
    */
   private int getLslocLineValue(String line)
   {
      String opLine = line.trim();
      String delim = CodeCounter.getLslocLineValueDelim();
      int firstDelimIndex = -1;
      int secondDelimIndex = -1;
      int lslocLineValue = 0;

      if (!opLine.isEmpty())
      {
         lslocLineValue = 1;
         firstDelimIndex = opLine.indexOf(delim);
         if (firstDelimIndex > -1)
         {
            secondDelimIndex = opLine.indexOf(delim, firstDelimIndex + delim.length());
            if (secondDelimIndex > -1 && secondDelimIndex > firstDelimIndex)
            {
               lslocLineValue = Integer.parseInt(opLine.substring(firstDelimIndex + delim.length(), secondDelimIndex));
            }
         }
      }

      return lslocLineValue;
   }

   /**
    * Removes the LSLOC value of a single line based on the provided line value
    * that is determined during the LSLOC Counter operation
    * 
    * @param line
    *           The line to be operated on
    * @return Number of non-blank lines in the array list
    */
   private String removeLslocLineValue(String line)
   {
      String opLine = line.trim();
      String delim = CodeCounter.getLslocLineValueDelim();
      int firstDelimIndex = -1;
      int secondDelimIndex = -1;

      if (!opLine.isEmpty())
      {
         firstDelimIndex = opLine.indexOf(delim);
         if (firstDelimIndex > -1)
         {
            secondDelimIndex = opLine.indexOf(delim, firstDelimIndex + delim.length());
            if (secondDelimIndex > -1 && secondDelimIndex > firstDelimIndex)
            {
               opLine = opLine.substring(secondDelimIndex + delim.length(), opLine.length());
            }
         }
      }

      return opLine;
   }
}
