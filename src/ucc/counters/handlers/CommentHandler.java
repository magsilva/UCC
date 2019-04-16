package ucc.counters.handlers;

import java.util.ArrayList;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ucc.langprops.LanguageProperties;

/**
 * This class handles the following: 1. Removal of single and multiline comments
 * from a given source line of code 2. Counting blank lines 3. Counting embedded
 * comments 4. Counting whole comments
 * 
 * NOTE: This class makes the assumption that comment start and end characters
 * are paired by equivalent index. This means the following: If "<!--" is the
 * comment start character and found at index (5) in its respective list AND
 * "-->" is the comment end character then it is ALSO found at index (5) in its
 * respective list.
 */
public class CommentHandler
{
   /** Instantiate the Log4j2 logger for this class */
   private static final Logger logger = LogManager.getLogger(CommentHandler.class);

   /** Whole comments count */
   private int NumWholeComments = 0;

   /** Embedded comments count */
   private int NumEmbeddedComments = 0;

   /** Blank lines count */
   private int NumBlankLines = 0;

   /**
    * This method returns the total number of embedded comments accumulated at
    * the time this method is called.
    * 
    * @return total number of embedded comments at the moment this function is
    *         called
    */
   public int GetEmbeddedCommentTally()
   {
      return NumEmbeddedComments;
   }

   /**
    * This method returns the total number of whole comments accumulated at the
    * time this method is called.
    * 
    * @return total number of whole comments at the moment this function is
    *         called
    */
   public int GetWholeCommentTally()
   {
      return NumWholeComments;
   }

   /**
    * This method returns the total number of blank lines accumulated at the
    * time this method is called.
    * 
    * @return total number of blank lines at the moment this function is called
    */
   public int GetBlankLineTally()
   {
      return NumBlankLines;
   }

   /** Current state of comment handler */
   private CommentState commentState = CommentState.HAS_NO_COMMENTS;

   /** Indicates whether in the middle of processing a comment */
   private boolean inComment = false;

   /** Comment index */
   private int commentIndex = -1;

   /** Enumerated type to note comment conditions */
   protected enum CommentState
   {
      HAS_NO_COMMENTS, HAS_ONLY_SINGLE_LINE_COMMENTS, HAS_ONLY_MULTILINE_COMMENTS, HAS_SINGLE_AND_MULTILINE_COMMENTS
   }

   /** Enumerated type to note comment conditions */
   protected enum CommentType
   {
      SINGLE, MULTI, NONE
   }

   /** Enumerated type to note type of comment found */
   protected enum CommentFound
   {
      EMBEDDED, WHOLE, NONE
   }

   /** List of single line comment characters */
   private ArrayList<String> SingleLineComment = null;

   /** List of multi line comment start characters */
   private ArrayList<String> MultiLineCommentStart = null;

   /** List of multi line comment end characters */
   private ArrayList<String> MultiLineCommentEnd = null;

   /**
    * Default constructor to initialize comment handler
    * 
    * @param languageProperties
    *           An object containing language properties
    */
   public CommentHandler(LanguageProperties languageProperties)
   {
      SetupVariables(languageProperties);
      SetupState();
   }

   /**
    * Constructor to initialize comment handler with array lists of comment
    * characters
    * 
    * @param singleLineCmnt
    *           An array list containing single line comment characters
    * @param multiLineCmntStart
    *           An array list containing comment characters that indicate
    *           beginning of a multi-line comment
    * @param multiLineCmntEnd
    *           An array list containing comment characters that indicate end of
    *           a multi-line comment
    */
   public CommentHandler(ArrayList<String> singleLineCmnt, ArrayList<String> multiLineCmntStart,
            ArrayList<String> multiLineCmntEnd)
   {
      SetupVariables(singleLineCmnt, multiLineCmntStart, multiLineCmntEnd);
      SetupState();
   }

   /**
    * This method handles parsing the initializing all of the necessary list for
    * correct operation of this class.
    * 
    * @param languageProperties
    *           An object containing language properties
    */
   private void SetupVariables(LanguageProperties languageProperties)
   {
      SingleLineComment = languageProperties.GetSingleLineCmntStartChars();
      MultiLineCommentStart = languageProperties.GetMultiLineCmntStartChars();
      MultiLineCommentEnd = languageProperties.GetMultiLineCmntEndChars();
   }

   /**
    * This method sets up all the necessary comment characters lists for correct
    * operation of this class
    * 
    * @param singleLineCmnt
    *           An array list containing single line comment characters
    * @param multiLineCmntStart
    *           An array list containing comment characters that indicate
    *           beginning of a multi-line comment
    * @param multiLineCmntEnd
    *           An array list containing comment characters that indicate end of
    *           a multi-line comment
    */
   private void SetupVariables(ArrayList<String> singleLineCmnt, ArrayList<String> multiLineCmntStart,
            ArrayList<String> multiLineCmntEnd)
   {
      SingleLineComment = singleLineCmnt;
      MultiLineCommentStart = multiLineCmntStart;
      MultiLineCommentEnd = multiLineCmntEnd;
   }

   /**
    * This method sets up the comment handler's state.
    */
   private void SetupState()
   {
      boolean hasSingleLineComments = false;
      boolean hasMultiLineComments = false;

      if (SingleLineComment.isEmpty() == false)
      {
         // This language has single line comments
         hasSingleLineComments = true;
      }

      if (MultiLineCommentStart.isEmpty() == false)
      {
         // This language has multiline comments
         hasMultiLineComments = true;
      }

      if (hasSingleLineComments == true && hasMultiLineComments == false)
      {
         // This language has only single line comments
         commentState = CommentState.HAS_ONLY_SINGLE_LINE_COMMENTS;
      }
      else if (hasSingleLineComments == false && hasMultiLineComments == true)
      {
         // This language has only multiline comments
         commentState = CommentState.HAS_ONLY_MULTILINE_COMMENTS;
      }
      else if (hasSingleLineComments == true && hasMultiLineComments == true)
      {
         // This language has both single line and multiline comments
         commentState = CommentState.HAS_SINGLE_AND_MULTILINE_COMMENTS;
      }
      else
      {
         commentState = CommentState.HAS_NO_COMMENTS;
      }
   }

   /**
    * This method handles the String if the language only has single line
    * comments
    * 
    * @param line
    *           input
    * @param CompilerDir
    *           Array of the Language's Compiler Directive Keywords to
    *           de-conflict when the language's comment keyword is part of a
    *           compiler directive keyword
    * 
    * @return input line with comments removed
    */
   private String HandleOnlySingleLineComments(String line, ArrayList<String> CompilerDir)
   {
      StringBuilder sbLine = new StringBuilder(line);

      if (sbLine.toString().isEmpty() == true)
      {
         NumBlankLines++;
      }
      else
      {
         int commentStartIndex = -1;
         boolean isCompilerDirective = false;

         for (int slc = 0; slc < SingleLineComment.size() && sbLine.toString().trim().isEmpty() == false; slc++)
         {
            commentStartIndex = sbLine.toString().indexOf(SingleLineComment.get(slc));
            isCompilerDirective = false;

            // Find instances of SingleLineComment after first column and remove
            // from the read-in line
            if (commentStartIndex > -1)
            {
               // Check for Compiler Directives that may interfere with the
               // Comment Character
               if (CompilerDir != null && CompilerDir.size() > 0)
               {
                  for (int cd = 0; cd < CompilerDir.size() && !sbLine.toString().trim().isEmpty(); cd++)
                  {
                     if (commentStartIndex == sbLine.toString().indexOf(CompilerDir.get(cd)))
                     {
                        isCompilerDirective = true;
                        break;
                     }
                  }
               }

               if (!isCompilerDirective)
               {
                  sbLine.replace(commentStartIndex, sbLine.length(), "");

                  // Embedded comment check
                  if (sbLine.toString().trim().isEmpty() == false)
                  {
                     NumEmbeddedComments++;
                  }
                  else
                  {
                     NumWholeComments++;
                  }
               }
            }
         }
      }

      line = sbLine.toString();

      return line;
   }

   /**
    * This method handles the String if the language only has multiline comments
    * 
    * @param line
    *           input
    * @param CompilerDir
    *           Array of the Language's Compiler Directive Keywords to
    *           de-conflict when the language's comment keyword is part of a
    *           compiler directive keyword
    * 
    * @return input line with comments removed
    */
   private String HandleOnlyMultiLineComments(String line, ArrayList<String> CompilerDir)
   {
      StringBuilder sbLine = new StringBuilder(line);

      // Indices to the location in line where the symbols are found
      // -1 denotes that the symbol has not been found
      int commentStartIndex = -1;
      int commentEndIndex = -1;

      // If the line is empty
      if (sbLine.toString().trim().isEmpty() == true)
      {
         // If we're in a comment, increment whole comments
         if (inComment == true)
         {
            NumWholeComments++;
         }
         else // If we're not in a comment, increment blank lines
         {
            NumBlankLines++;
         }
      }
      else // If the line is not empty
      {
         // If we're in a comment
         if (inComment == true)
         {
            // Look for the end
            commentEndIndex = sbLine.toString().indexOf(MultiLineCommentEnd.get(commentIndex));

            // If the end isn't found, empty the line and move on
            if (commentEndIndex == -1)
            {
               sbLine.setLength(0);
               NumWholeComments++;
            }
            else // If the end is found
            {
               // Take line data after the end
               sbLine.replace(0, commentEndIndex + MultiLineCommentEnd.get(commentIndex).length(), "");

               // Embedded comment check
               if (!sbLine.toString().trim().isEmpty())
               {
                  NumEmbeddedComments++;
               }
               else
               {
                  NumWholeComments++;
               }

               inComment = false;
            }
         }
         else // If we're not in a comment, look for the start
         {
            for (int mlc = 0; mlc < MultiLineCommentStart.size() && !line.trim().isEmpty(); mlc++)
            {
               commentStartIndex = sbLine.toString().indexOf(MultiLineCommentStart.get(mlc));

               // If we found the start of a comment
               if (commentStartIndex > -1)
               {
                  // Save the comment index so we know which comment end to
                  // match against
                  commentIndex = mlc;

                  // Look for the end
                  commentEndIndex = sbLine.toString().indexOf(MultiLineCommentEnd.get(commentIndex),
                           commentStartIndex + MultiLineCommentStart.get(commentIndex).length());

                  // If we found the end of a comment
                  if (commentEndIndex > -1)
                  {
                     // Handle multiple instances of multi-line comments on one
                     // line
                     while (commentStartIndex != -1 && commentEndIndex != -1 && commentStartIndex < commentEndIndex)
                     {
                        inComment = false;
                        sbLine.replace(commentStartIndex,
                                 commentEndIndex + MultiLineCommentEnd.get(commentIndex).length(), "");

                        // Embedded comment check
                        if (!sbLine.toString().trim().isEmpty())
                        {
                           NumEmbeddedComments++;
                        }
                        else
                        {
                           NumWholeComments++;
                        }

                        commentStartIndex = sbLine.toString().indexOf(MultiLineCommentStart.get(commentIndex));
                        commentEndIndex = sbLine.toString().indexOf(MultiLineCommentEnd.get(commentIndex));
                     }

                     // We found an unfinished comment after clearing out
                     // embedded ones
                     if (commentStartIndex > -1 && commentEndIndex == -1)
                     {
                        inComment = true;
                        sbLine.replace(commentStartIndex, sbLine.length(), "");

                        // Embedded comment check
                        if (!sbLine.toString().trim().isEmpty())
                        {
                           NumEmbeddedComments++;
                        }
                        else
                        {
                           NumWholeComments++;
                        }
                     }
                  }
                  else // We don't find the end of the comment
                  {
                     inComment = true;
                     sbLine.replace(commentStartIndex, sbLine.length(), "");

                     // Embedded comment check
                     if (!sbLine.toString().trim().isEmpty())
                     {
                        NumEmbeddedComments++;
                     }
                     else
                     {
                        NumWholeComments++;
                     }
                  }
               }
            }
         }
      }

      line = sbLine.toString();

      return line;
   }

   /**
    * This method handles the String if the language has both single line and
    * multiline comments
    * 
    * @param line
    *           input
    * @param CompilerDir
    *           Array of the Language's Compiler Directive Keywords to
    *           de-conflict when the language's comment keyword is part of a
    *           compiler directive keyword
    * @return input line with comments removed
    */
   private String HandleSingleLineAndMultiLineComments(String line, ArrayList<String> CompilerDir)
   {
      // Indices to the location in line where the symbols are found
      // -1 denotes that the symbol has not been found
      int singleLineCommentStartIndex = -1;
      int multiLineCommentStartIndex = -1;

      boolean multiLineCommentStartFound = false;
      boolean singleLineCommentStartFound = false;

      // If we're already in a multi-line comment, we only care about multi-line
      // comment ends
      if (inComment == true)
      {
         line = HandleOnlyMultiLineComments(line, CompilerDir);
      }
      else // If we're not yet in a comment
      {
         // Check for any instance of a multi-line comment start on the line
         for (int mlc = 0; mlc < MultiLineCommentStart.size() && !line.trim().isEmpty(); mlc++)
         {
            multiLineCommentStartIndex = line.indexOf(MultiLineCommentStart.get(mlc));
            if (multiLineCommentStartIndex > -1)
            {
               multiLineCommentStartFound = true;
            }
         }

         // Check for any instance of a single line comment start on the line
         for (int slc = 0; slc < SingleLineComment.size() && !line.trim().isEmpty(); slc++)
         {
            singleLineCommentStartIndex = line.indexOf(SingleLineComment.get(slc));
            if (singleLineCommentStartIndex > -1)
            {
               singleLineCommentStartFound = true;
            }
         }

         if (multiLineCommentStartFound == true && singleLineCommentStartFound == false)
         {
            // If we found any multi-line comment, but no single line comment
            line = HandleOnlyMultiLineComments(line, CompilerDir);
         }
         else if (multiLineCommentStartFound == false && singleLineCommentStartFound == true)
         {
            // If we found any single line comment, but no multi-line comment
            line = HandleOnlySingleLineComments(line, CompilerDir);
         }
         else if (multiLineCommentStartFound == true && singleLineCommentStartFound == true)
         {
            // If we found both a single line comment and multi-line comment

            // If the index of the single line comment is before the index of
            // the multi-line comment,
            // call the single line comment handler
            if (singleLineCommentStartIndex < multiLineCommentStartIndex)
            {
               line = HandleOnlySingleLineComments(line, CompilerDir);
            }
            else
            {
               // First clear the multi-line comment, then the single line
               // comment
               // (the single line could be after the multi-line closes)
               line = HandleOnlyMultiLineComments(line, CompilerDir);
               line = HandleOnlySingleLineComments(line, CompilerDir);
            }
         }
         else // If we found neither a single line comment nor multi-line
              // comment
         {
            // If the line is empty
            if (line.trim().isEmpty() == true)
            {
               NumBlankLines++;
            }
         }
      }

      return line;
   }

   /**
    * Function for handling situations where the language does not contain
    * comments
    * 
    * @param line
    *           input
    * @return unchanged input line
    */
   private String HandleNoComments(String line)
   {
      return line;
   }

   /**
    * This method handles finding a language specific comment in a line.
    * 
    * @param line
    *           input
    * @param CompilerDir
    *           Array of the Language's Compiler Directive Keywords to
    *           de-conflict when the language's comment keyword is part of a
    *           compiler directive keyword
    * @return line with comments removed
    */
   public String HandleComments(String line, ArrayList<String> CompilerDir)
   {
      switch (commentState)
      {
         case HAS_ONLY_SINGLE_LINE_COMMENTS:
            return HandleOnlySingleLineComments(line, CompilerDir);
         case HAS_ONLY_MULTILINE_COMMENTS:
            return HandleOnlyMultiLineComments(line, CompilerDir);
         case HAS_SINGLE_AND_MULTILINE_COMMENTS:
            return HandleSingleLineAndMultiLineComments(line, CompilerDir);
         case HAS_NO_COMMENTS:
            return HandleNoComments(line);
         default:
            // We should not get here!
            logger.error("You need to handle the new state you create or there is a much deeper problem.");
            return "";
      }
   }

   /**
    * This method resets the state of the comment handler, such that it can be
    * used more generally instead of needing to instantiate one per file.
    */
   public void ResetState()
   {
      // Reset State
      inComment = false;
   }

   /**
    * This method resets the external data of the comment handler, such that it
    * can be used more generally instead of needing to instantiate one per file.
    */
   public void ResetExternalData()
   {
      // Reset External Data
      NumWholeComments = 0;
      NumEmbeddedComments = 0;
      NumBlankLines = 0;
   }

   /**
    * This method resets the state and data of the comment handler, such that it
    * can be used more generally instead of need to instantiate one per file.
    */
   public void Reset()
   {
      ResetState();
      ResetExternalData();
   }
}