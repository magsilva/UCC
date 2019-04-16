package ucc.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ucc.langprops.CustomLanguageProperties;

/**
 * This class performs the Import operation. It stores the properties from an
 * external language properties file in memory.
 * 
 * @author Integrity Applications Incorporated
 *
 */
public class PropertiesReader
{
   /** Instantiate the Log4j2 logger for this class */
   private static final Logger logger = LogManager.getLogger(PropertiesReader.class);

   /** An object to store custom language's properties */
   private static CustomLanguageProperties CustomProps = new CustomLanguageProperties();

   /**
    * Reads in an external language properties file and stores the properties in
    * an array list.
    * 
    * @param filename
    *           the name of the file to read from
    * @return returns whether Import was successful
    */
   public static boolean Import(String filename)
   {
      boolean success = false;
      String property;
      String values;
      String valueSeparator = ",";

      // An array to store the properties after split operation
      String[] array = null;
      try
      {
         BufferedReader reader = new BufferedReader(new FileReader(filename));
         String line = reader.readLine();

         while (line != null)
         {
            // only extract properties if line is not a comment/empty
            if (!line.equals("") && !line.substring(0, 3).equals("###"))
            {
               property = line.substring(0, line.indexOf(":"));
               values = line.substring(line.indexOf(":") + 1);
               values = values.trim();

               // check if value separator was specified in the file
               if (property.equals("ValueSeparator"))
               {
                  valueSeparator = values;
                  valueSeparator = valueSeparator.trim();
                  valueSeparator = valueSeparator.replace("^", "\\^");
                  valueSeparator = valueSeparator.replace("|", "\\|");
                  valueSeparator = valueSeparator.replace("+", "\\+");
                  valueSeparator = valueSeparator.replace("{", "\\{");
                  valueSeparator = valueSeparator.replace("^", "\\^");
                  valueSeparator = valueSeparator.replace("[", "\\[");
                  valueSeparator = valueSeparator.replace("?", "\\?");
                  valueSeparator = valueSeparator.replace("(", "\\(");
                  valueSeparator = valueSeparator.replace(")", "\\)");
                  valueSeparator = valueSeparator.replace("*", "\\*");
                  valueSeparator = valueSeparator.replace("\\G", "\\\\G");
                  valueSeparator = valueSeparator.replace("\\E", "\\\\E");
                  valueSeparator = valueSeparator.replace("\\", "\\\\");
                  valueSeparator = valueSeparator.replace("$", "\\$");
                  valueSeparator = valueSeparator.replace(".", "\\.");
               }
               // if there are more than 0 values, store them in an array
               // The regular expression ignores blank spaces
               if (!values.isEmpty() && !values.matches("^\\s*$"))
               {
                  array = values.split(valueSeparator);
                  // store values in array list
                  ArrayList<String> arrayList = new ArrayList<String>();
                  for (int i = 0; i < array.length; i++)
                  {
                     if (!array[i].equals(""))
                     {
                        arrayList.add(array[i].trim());
                     }
                  }
                  SetProperties(property, arrayList);
               }
            }
            // read the next line of the file
            line = reader.readLine();
         }

         reader.close();
         success = true;
         return success;
      }
      catch (Exception e)
      {
         return success;
      }
   }

   /**
    * Sets the language properties in memory.
    * 
    * @param property
    *           the property to store
    * @param arrayList
    *           list of values associated with the property
    */
   public static void SetProperties(String property, ArrayList<String> arrayList)
   {
      switch (property)
      {
         case "LanguageName":
            CustomProps.SetLangName(arrayList.get(0));
            break;
         case "SimilarTo":
            CustomProps.SetSimilarTo(arrayList.get(0));
            break;
         case "CaseSensitive":
            boolean BooleanToken = Boolean.parseBoolean(arrayList.get(0));
            CustomProps.SetCaseSensitive(BooleanToken);
            break;
         case "LanguageVersion":
            CustomProps.SetLangVersion(arrayList.get(0));
            break;
         case "LanguageFileExtensions":
            CustomProps.SetLangFileExts(arrayList);
            break;
         case "ExecutableLineTerminationCharacters":
            CustomProps.SetExecLineTermChars(arrayList);
            break;
         case "SingleLineCommentStartCharacters":
            CustomProps.SetSingleLineCmntStartChars(arrayList);
            break;
         case "MultiLineCommentStartCharacters":
            CustomProps.SetMultiLineCmntStartChars(arrayList);
            break;
         case "MultiLineCommentEndCharacters":
            CustomProps.SetMultiLineCmntEndChars(arrayList);
            break;
         case "CompilerDirectiveCharacters":
            CustomProps.SetCompilerDirChars(arrayList);
            break;
         case "CompilerDirectiveKeywords":
            CustomProps.SetCompilerDirKeywords(arrayList);
            break;
         case "QuoteStartCharacters":
            CustomProps.SetQuoteStartChars(arrayList);
            break;
         case "LineContinuationCharacters":
            CustomProps.SetLineContChars(arrayList);
            break;
         case "FunctionStartSymbols/Keywords":
            CustomProps.SetFuncStartKeywords(arrayList);
            break;
         case "LoopStatementKeywords":
            CustomProps.SetLoopKeywords(arrayList);
            break;
         case "ConditionStatementKeywords":
            CustomProps.SetCondKeywords(arrayList);
            break;
         case "LogicalOperators":
            CustomProps.SetLogicalOps(arrayList);
            break;
         case "AssignmentOperators":
            CustomProps.SetAssignmentOps(arrayList);
            break;
         case "OtherExecutableKeywords":
            CustomProps.SetOtherExecKeywords(arrayList);
            break;
         case "DataKeywords":
            CustomProps.SetDataKeywords(arrayList);
            break;
         case "CalculationKeywords":
            CustomProps.SetCalcKeywords(arrayList);
            break;
         case "LogOperationKeywords":
            CustomProps.SetLogOpKeywords(arrayList);
            break;
         case "TrigOperationKeywords":
            CustomProps.SetTrigOpKeywords(arrayList);
            break;
         case "OtherMathKeywords":
            CustomProps.SetOtherMathKeywords(arrayList);
            break;
         case "Exclude/IgnoreKeywords":
            CustomProps.SetExcludeKeywords(arrayList);
            break;
         case "Exclude/IgnoreCharacters":
            CustomProps.SetExcludeCharacters(arrayList);
            break;
         case "CyclomaticComplexityKeywords":
            CustomProps.SetCyclCmplexKeywords(arrayList);
            break;
         case "Lslockeywords":
            CustomProps.SetLslocKeywords(arrayList);
            break;
         case "PointerKeywords":
            CustomProps.SetPointerKeywords(arrayList);
            break;
      }
   }

   /**
    * Returns custom language properties object
    * 
    * @return custom language properties object
    */
   public static CustomLanguageProperties GetCustomLangProps()
   {
      return CustomProps;
   }

   /**
    * Prints the stored custom language properties
    */
   public static void PrintProperties()
   {
      StringBuilder sb = new StringBuilder();
      String sep = System.getProperty("line.separator");

      sb.append("Imported " + CustomProps.GetLangName().toUpperCase() + " language properties are:" + sep);
      sb.append("LanguageName: " + CustomProps.GetLangName() + sep);
      sb.append("SimilarTo: " + CustomProps.GetSimilarTo() + sep);
      sb.append("CaseSensitive: " + CustomProps.IsCaseSensitive() + sep);
      sb.append("LanguageVersion: " + CustomProps.GetLangVersion() + sep);
      sb.append("LanguageFileExtensions: " + CustomProps.GetLangFileExts() + sep);
      sb.append("ExecutableLineTerminationCharacters: " + CustomProps.GetExecLineTermChars() + sep);
      sb.append("SingleLineCommentStartCharacters: " + CustomProps.GetSingleLineCmntStartChars() + sep);
      sb.append("MultiLineCommentStartCharacters: " + CustomProps.GetMultiLineCmntStartChars() + sep);
      sb.append("MultiLineCommentEndCharacters: " + CustomProps.GetMultiLineCmntEndChars() + sep);
      sb.append("CompilerDirectiveCharacters: " + CustomProps.GetCompilerDirChars() + sep);
      sb.append("CompilerDirectiveKeywords: " + CustomProps.GetCompilerDirKeywords() + sep);
      sb.append("QuoteStartCharacters: " + CustomProps.GetQuoteStartChars() + sep);
      sb.append("LineContinuationCharacters: " + CustomProps.GetLineContChars() + sep);
      sb.append("FunctionStartSymbols/Keywords: " + CustomProps.GetFuncStartKeywords() + sep);
      sb.append("LoopStatementKeywords: " + CustomProps.GetLoopKeywords() + sep);
      sb.append("ConditionStatementKeywords: " + CustomProps.GetCondKeywords() + sep);
      sb.append("LogicalOperators: " + CustomProps.GetLogicalOps() + sep);
      sb.append("AssignmentOperators: " + CustomProps.GetAssignmentOps() + sep);
      sb.append("OtherExecutableKeywords: " + CustomProps.GetOtherExecKeywords() + sep);
      sb.append("DataKeywords: " + CustomProps.GetDataKeywords() + sep);
      sb.append("CalculationKeywords: " + CustomProps.GetCalcKeywords() + sep);
      sb.append("LogOperationKeywords: " + CustomProps.GetLogOpKeywords() + sep);
      sb.append("TrigOperationKeywords: " + CustomProps.GetTrigOpKeywords() + sep);
      sb.append("OtherMathKeywords: " + CustomProps.GetOtherMathKeywords() + sep);
      sb.append("Exclude/IgnoreKeywords: " + CustomProps.GetExcludeKeywords() + sep);
      sb.append("Exclude/IgnoreCharacters: " + CustomProps.GetExcludeCharacters() + sep);
      sb.append("CyclomaticComplexityKeywords: " + CustomProps.GetCyclCmplexKeywords() + sep);
      sb.append("Lslockeywords:" + CustomProps.GetLslocKeywords() + sep);
      sb.append("PointerKeywords: " + CustomProps.GetPointerKeywords());

      String message = sb.toString();
      System.out.println(message);
      logger.debug(message);
   }
}
