package ucc.counters.handlers;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ucc.langprops.LanguageProperties;

/**
 * This is a class for handling string literals in C-style languages. Languages
 * which use this include C_CPP, JAVA, and CSHARP.
 *
 * @author Integrity Applications Incorporated
 *
 */
public class QuoteHandler
{
   /** Line to save */
   private String SAVED_LINE;

   /** Quotation characters */
   private ArrayList<String> QUOTE_CHARS = null;

   /** Number of quotes */
   private int QUOTE_COUNT;

   /** Current quote state */
   private boolean OPEN_QUOTE_FLAG = false;

   /** Quote character */
   private String QUOTE_CHAR = "\"";

   /**
    * Default constructor to initialize Java quote handler
    * 
    * @param languageProperties
    *           An object containing language properties
    */
   public QuoteHandler(LanguageProperties languageProperties)
   {
      QUOTE_CHARS = languageProperties.GetQuoteStartChars();

      QUOTE_COUNT = 0;
      SAVED_LINE = "";
   }

   /**
    * This method handles the removal of non-alphanumeric data from quotes in a
    * line.
    * 
    * @param line
    *           Input line
    * @return Input line with non-alphanumeric characters removed from quotes
    */
   public String HandleQuotes(String line)
   {
      if (OPEN_QUOTE_FLAG == true)
      {
         line = SAVED_LINE + " " + line;
         SAVED_LINE = "";
         OPEN_QUOTE_FLAG = false;
      }

      // Count the quotes on the line
      CountQuotes(line);

      // Determine if the line contains an open quote
      DetermineIfOpenQuote();

      // If we're not in an open quote
      if (OPEN_QUOTE_FLAG == false)
      {
         // But we have some quotes on the line
         if (QUOTE_COUNT > 0)
         {
            line = DeleteUnwantedStringsFromClosedQuotes(line);
         }
      }
      else
      {
         line = DeleteUnwantedStringsFromOpenQuotes(line);

         // We want to return an empty line, but keep appending the line to a
         // temporary string until we find the closed
         // quote
         SAVED_LINE += line;
         line = "";
      }

      return line;
   }

   /**
    * This function counts the number of occurrences of " quotes on a line and
    * determines if we're dealing with an open quote based on whether the count
    * is even or odd.
    * 
    * @param line
    *           Input line
    */
   public void CountQuotes(String line)
   {
      String regEx;
      Matcher matcher;
      int counter;

      // Zero the counter
      counter = 0;

      regEx = QUOTE_CHAR;
      matcher = Pattern.compile(regEx).matcher(line);

      // Count number of quotes on the line
      while (matcher.find())
      {
         counter++;
      }

      // Set the quote count to the value of the counter (this will zero the
      // quote count if nothing is found)
      QUOTE_COUNT = counter;

   }

   /**
    * Function for determining if we're dealing with an open quote based on the
    * number of quotes found on the line - Even = all closed quotes - Odd = open
    * quote
    */
   public void DetermineIfOpenQuote()
   {
      // If the number of quotes is odd, flip the open quote flag
      if (QUOTE_COUNT % 2 != 0)
      {
         OPEN_QUOTE_FLAG = !OPEN_QUOTE_FLAG;
      }

   }

   /**
    * Function for removing all non-alphanumeric symbols from a quote
    * 
    * @param line
    *           Line coming in from file
    *
    * @return The modified line coming in
    */
   protected String DeleteUnwantedStringsFromClosedQuotes(String line)
   {
      String front = "";
      String middle = "";
      String back = "";
      String quote = "";
      Matcher matcher;

      for (int qc = 0; qc < QUOTE_CHARS.size() && !line.isEmpty(); qc++)
      {
         matcher = Pattern.compile(QUOTE_CHARS.get(qc) + "([^" + QUOTE_CHARS.get(qc) + "]*)" + QUOTE_CHARS.get(qc))
                  .matcher(line);
         while (matcher.find())
         {
            quote = QUOTE_CHARS.get(qc) + matcher.group(1) + QUOTE_CHARS.get(qc);
            front = line.substring(0, line.indexOf(quote) + QUOTE_CHARS.get(qc).length());
            middle = matcher.group(1);
            back = line.substring(line.indexOf(quote) + quote.length() - QUOTE_CHARS.get(qc).length(), line.length());

            middle = middle.replaceAll("[^\\p{Alnum}\\s]", "").replaceAll("\\s+", "");

            line = front + middle + back;
         }
      }

      return line;
   }

   /**
    * Function for replacing all non-alphanumeric symbols from an open quote
    * 
    * @param line
    *           Line coming in from file
    *
    * @return The modified line coming in
    */
   protected String DeleteUnwantedStringsFromOpenQuotes(String line)
   {
      line = line.substring(0, line.indexOf(QUOTE_CHAR) + QUOTE_CHAR.length())
               + line.substring(line.indexOf(QUOTE_CHAR) + QUOTE_CHAR.length()).replaceAll("[^\\p{Alnum}\\s]", "")
                        .replaceAll("\\s+", "");

      return line;
   }

   /**
    * Function for resetting values that need to be zeroed out when moving to a
    * new file
    */
   public void Reset()
   {
      QUOTE_COUNT = 0;
      SAVED_LINE = "";
   }
}
