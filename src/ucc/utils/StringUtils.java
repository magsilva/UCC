package ucc.utils;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * This class contains all the common operations performed on objects of String
 * type. Functions in this class are static so they can be called directly
 * without instantiating an object of this class.
 * 
 * @author Integrity Applications Incorporated
 *
 */
public class StringUtils
{
   /**
    * This class implements Comparator interface for objects of String type. It
    * implements compare(), which can be used to sort collections of String
    * objects by their length in descending order, i.e., the largest to the
    * smallest Strings.
    */
   public static class StringLengthListSort implements Comparator<String>
   {
      public int compare(String s1, String s2)
      {
         return s2.length() - s1.length();
      }
   }

   /**
    * This method determines if a is found in b at the given index start. Note:
    * (1) One might think this is replicated code and index of should be used.
    * However, indexOf continues to search for the string from the start index
    * to the end, but we only want to look for the string at the given index.
    * (2) In addition, this is faster than creating a substring and testing the
    * equality of the substring against the provided string.
    * 
    * Speed test can be seen here:
    */

   /**
    * This method determines if target is found in searchArea at the given index
    * startIndex. NOTE: This method does not handle parameter errors.
    * 
    * @param target
    *           string to find
    * @param searchArea
    *           string where searching occurs
    * @param startIndex
    *           starting index for comparison
    * @return (boolean) Is a in b at start?
    */
   public static boolean directCharacterCompare(String target, String searchArea, int startIndex)
   {
      for (int i = 0; i < target.length(); i++)
      {
         if (target.charAt(i) != searchArea.charAt(startIndex + i))
         {
            return false;
         }
      }
      return true;
   }

   /**
    * This method determines if target is found in searchArea at the given index
    * startIndex. NOTE: This method does not handle parameter errors.
    * 
    * @param target
    *           string to find
    * @param searchArea
    *           string where searching occurs
    * @param startIndex
    *           starting index for comparison
    * @return (boolean) Is a in b at start?
    */
   public static boolean substringCompare(String target, String searchArea, int startIndex)
   {
      String extractedWord = searchArea.substring(startIndex, startIndex + target.length());
      return target.equals(extractedWord);
   }

   /**
    * This method determines if target is found in searchArea at the given index
    * startIndex. NOTE: This method was created due to the results of a speed
    * test. It turns out that the speed difference is so significant at a target
    * length of one (1) that it warrants the use of an if statement.
    * 
    * 
    * @param target
    *           string to find
    * @param searchArea
    *           string where searching occurs
    * @param startIndex
    *           starting index for comparison
    * @return (boolean) Is a in b at start?
    */
   public static boolean isStringAtIndex(String target, String searchArea, int startIndex)
   {
      // Handle the common parameter errors here
      int targetLength = target.length();
      int searchAreaLength = searchArea.length();
      if (startIndex >= searchAreaLength)
      {
         return false;
      }
      else if (targetLength + startIndex > searchAreaLength)
      {
         return false;
      }
      else
      {
         // Actual functionality
         if (targetLength == 1)
         {
            return substringCompare(target, searchArea, startIndex);
         }
         return directCharacterCompare(target, searchArea, startIndex);
      }
   }

   /**
    * This method takes an array of target words and seeks to find one of them
    * at the given index in searchArea NOTE: the target words cannot contain the
    * empty string ("") for correct function.
    * 
    * @param searchArea
    *           string to search
    * @param startIndex
    *           index at which to begin search
    * @param targetWords
    *           words to look for at startIndex in searchArea
    * @return (String) targetWord
    */
   public static String getTargetWord(String searchArea, int startIndex, String... targetWords)
   {
      if (startIndex >= searchArea.length())
      {
         return "";
      }
      int targetWordsLength = targetWords.length;
      for (int i = 0; i < targetWordsLength; i++)
      {
         String targetWord = targetWords[i];
         if (isStringAtIndex(targetWord, searchArea, startIndex + i) == true)
         {
            return targetWord;
         }
      }
      return "";
   }

   /**
    * This method takes an ArrayList of target words and seeks to find one of
    * them at the given index in searchArea NOTE: the target words cannot
    * contain the empty string ("") for correct function.
    * 
    * @param searchArea
    *           string to search
    * @param startIndex
    *           index at which to begin search
    * @param targetWords
    *           words to look for at startIndex in searchArea
    * @return (int) index of target word found in input list
    */
   public static int getStringAtIndex(String searchArea, int startIndex, ArrayList<String> targetWords)
   {
      if (0 > startIndex || startIndex >= searchArea.length())
      {
         return -1;
      }
      int targetWordsLength = targetWords.size();
      for (int i = 0; i < targetWordsLength; i++)
      {
         String targetWord = targetWords.get(i);
         if (isStringAtIndex(targetWord, searchArea, startIndex) == true)
         {
            return i;
         }
      }
      return -1;
   }

   /**
    * This method takes an array of target words and seeks to find one of them
    * at the given index in searchArea NOTE: This will function properly even if
    * the empty string ("") is a target word.
    * 
    * @param searchArea
    *           string to search
    * @param startIndex
    *           index at which to begin search
    * @param targetWords
    *           words to look for at startIndex in searchArea
    * @return (int) ith targetWord provided
    */
   public int getTargetWordLocationInTargetWords(String searchArea, int startIndex, String... targetWords)
   {
      if (startIndex >= searchArea.length())
      {
         // Despite this check existing in isStringAtIndex, isStringAtIndex will
         // need to fail targetWords.length() number of times to conclude that
         // -1 should be returned. This takes care of that issue at once.
         return -1;
      }
      for (int i = 0; i < targetWords.length; i++)
      {
         String targetWord = targetWords[i];
         if (isStringAtIndex(targetWord, searchArea, startIndex + i) == true)
         {
            return i;
         }
      }
      return -1;
   }

   /**
    * This method removes the compiler directive symbol so that the word can be
    * parsed
    * 
    * @param directive
    *           -dl- this is the compiler directive (symbol and name)
    * @param symbol
    *           -dl- compiler directive character that marks its presence
    * @return the compiler directive name (removes the symbol)
    */
   public static String removeCompilerDirectiveCharacter(String directive, String symbol)
   {
      // compilerDirKeyword.substring(1, compilerDirKeyword.length());
      int index = directive.indexOf(symbol);
      return directive.substring(index, directive.length());
   }

   /**
    * This method attempts to find the start or end, depending upon the search
    * direction index of a word by trying to find a semicolon until it reaches
    * the beginning of the line.
    * 
    * @param line
    *           input line to process
    * @param startIndex
    *           starting index
    * @param delimiter
    *           the character at which looking for an end index stops
    * @param direction
    *           the search direction
    * @return (int) the index that encapsulates the word marked by the delimiter
    *         starting from the input index
    */
   public static int findCharacter(String direction, String line, int startIndex, char delimiter)
   {
      int length = line.length();
      if (0 > startIndex || startIndex >= length)
      {
         // index is too large or too small for line
         return -1;
      }

      int i = 0;
      switch (direction)
      {
         case "front":
            for (i = startIndex; i >= 0; i--)
            {
               if (line.charAt(i) == delimiter)
               {
                  break;
               }
            }
            break;
         case "behind":
            i = line.indexOf(delimiter, startIndex);
            if (i < 0)
            {
               i = length;
            }
         default:
            // Erroneous: You should not get here
            return -2;
      }
      return i;
   }

   /**
    * This method iterates through the ArrayList concatenating each string found
    * to the main string. The whole concatenated string is then returned.
    * 
    * @param strings
    *           (ArrayList&lt;String&gt;) strings to concatenate
    * @return a concatenated string
    */
   public static String concatenateStrings(ArrayList<String> strings)
   {
      String line = "";
      int lineSegmentsLength = strings.size();
      for (int i = 0; i < lineSegmentsLength; i++)
      {
         // NOTE: The use of the white space is to
         // ensure that NO two (non-comment) words
         // are accidentally concatenated and
         // possible parsed incorrectly later.
         line += " " + strings.get(i);
      }
      return line;
   }

   /**
    * 
    * 
    * @param line
    *           input line to process
    * @param lineContinuationCharacters
    *           list of line continuation characters
    * @return (int)
    */
   public static int getLineTerminatingCharacter(String line, ArrayList<String> lineContinuationCharacters)
   {
      int index = -1;
      int lineLength = line.length();
      for (int i = lineLength - 1; i >= 0; i--)
      {
         if (line.charAt(i) != ' ')
         {
            index = getStringAtIndex(line, i, lineContinuationCharacters);
            if (index >= 0)
            {
               index = i;
            }
            // IF we do not find one, then
            // the line does not contain one and
            // stop looking
            break;
         }
      }
      return index;
   }

   /**
    * This method constructs a String path from the sub-directories array
    * beginning from start until end and terminating with pathExtension.
    * 
    * @param subdirectories
    *           Array of sub-directories
    * @param start
    *           start index
    * @param end
    *           end index
    * @param pathExtension
    *           path extension
    * @return A concatenated path
    */
   public static String constructPath(String[] subdirectories, int start, int end, String pathExtension)
   {
      int length = subdirectories.length;
      if (0 > start || start > length || 0 > end || end > length || end < start)
      {
         return "";
      }

      String path = "";
      for (int i = start; i < end; i++)
      {
         path = path.concat(subdirectories[i] + "\\");
      }

      path = path.concat(pathExtension);
      return path;
   }

   /**
    * This method handles the hash-bang compiler directive for any language that
    * supports them.
    * 
    * @param line
    *           (String)
    * @return +1 if the line is the hash-bang directive, +0 otherwise
    */
   public static int handleHashBangDirective(String line)
   {
      if (line.startsWith("#!") == true)
      {
         String[] tokens = line.split("/");
         if (tokens.length >= 2)
         {
            return 1;
         }
      }
      return 0;
   }

   /**
    * This method replaces each element of chars founds in line with
    * replacement.
    * 
    * @param line
    *           (String)
    * @param replacement
    *           (String)
    * @param chars
    *           (String)
    * @return (String) a line with all elements in chars replaced
    */
   public static String replace(String line, String replacement, ArrayList<String> chars)
   {
      for (int i = 0; i < chars.size(); i++)
      {
         line.replace(chars.get(i), replacement);
      }
      return line;
   }

   /**
    * Converts an array of Strings to a StringBuffer object
    * 
    * @param strArray
    *           An array of String values
    * @param buf
    *           A StringBuffer for output
    */
   public static void StringArrayToStringBuffer(String[] strArray, StringBuffer buf)
   {
      if (strArray != null && buf != null)
      {
         // Iterate through all String array elements and add them to the String
         // Buffer
         for (int i = 0; i < strArray.length; i++)
         {
            buf.append(strArray[i] + " ");
         }
      }
   }
}