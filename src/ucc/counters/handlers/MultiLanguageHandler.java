package ucc.counters.handlers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ucc.datatypes.Constants;
import ucc.datatypes.DataTypes;
import ucc.datatypes.DataTypes.LanguagePropertiesType;
import ucc.datatypes.UCCFile;
import ucc.langprops.LanguageProperties;
import ucc.main.RuntimeParameters;
import ucc.utils.FileUtils;

public class MultiLanguageHandler
{
   /** Instantiate the Log4j2 logger for this class */
   private static final Logger logger = LogManager.getLogger(MultiLanguageHandler.class);

   // Variables
   private ArrayList<String> CSSStartTags = new ArrayList<String>();
   private ArrayList<String> CSSEndTags = new ArrayList<String>();
   private String CSSCode;
   private ArrayList<String> JSStartTags = new ArrayList<String>();
   private ArrayList<String> JSEndTags = new ArrayList<String>();
   private String JSCode;
   private ArrayList<String> PHPStartTags = new ArrayList<String>();
   private ArrayList<String> PHPEndTags = new ArrayList<String>();
   private String PHPCode;
   private ArrayList<String> VBSStartTags = new ArrayList<String>();
   private ArrayList<String> VBSEndTags = new ArrayList<String>();
   private String VBSCode;
   private ArrayList<String> ERBStartTags = new ArrayList<String>();
   private ArrayList<String> ERBEndTags = new ArrayList<String>();
   private String ERBCode;
   private ArrayList<String> JSPStartTags = new ArrayList<String>();
   private ArrayList<String> JSPEndTags = new ArrayList<String>();
   private String JSPCode;
   private ArrayList<String> GSPStartTags = new ArrayList<String>();
   private ArrayList<String> GSPEndTags = new ArrayList<String>();
   private String GSPCode;
   private ArrayList<String> ASPStartTags = new ArrayList<String>();
   private ArrayList<String> ASPEndTags = new ArrayList<String>();
   private String ASPCode;
   // private ArrayList<String> HTMLStartTags = new ArrayList<String>();
   // private ArrayList<String> HTMLEndTags = new ArrayList<String>();
   private String HTMLCode;
   // private ArrayList<String> CFStartTags = new ArrayList<String>();
   // private ArrayList<String> CFEndTags = new ArrayList<String>();
   private String CFCode;
   private ArrayList<String> CFSStartTags = new ArrayList<String>();
   private ArrayList<String> CFSEndTags = new ArrayList<String>();
   private String CFSCode;
   private ArrayList<String> SQLStartTags = new ArrayList<String>();
   private ArrayList<String> SQLEndTags = new ArrayList<String>();
   private String SQLCode;
   // private ArrayList<String> XMLStartTags = new ArrayList<String>();
   // private ArrayList<String> XMLEndTags = new ArrayList<String>();
   private String XMLCode;

   private boolean openStartTag;
   private boolean openCFTag;

   private String fileExt; // Use for *.erb and *.gsp implementation
   private String fileLanguage;
   private LanguageState languageState;

   private RuntimeParameters RtParams;

   protected int truncateLinesCount = 0; // counter for truncated lines

   /** Enumerated type to note which language is the current state */
   protected enum LanguageState
   {
      IN_HTML, IN_CSS, IN_JS, IN_PHP, IN_VBS, IN_ERB, IN_JSP, IN_GSP, IN_ASP, IN_CF, IN_CFS, IN_SQL, IN_XML
   }

   /**
    * Default constructor for the multi-language handler
    * 
    * @param languageProperties
    *           An object containing language properties
    * @param aRuntimeParameters
    *           RuntimeParameters object to access through MultiLanguage
    *           processing
    */
   public MultiLanguageHandler(LanguageProperties languageProperties, RuntimeParameters aRuntimeParameters)
   {
      fileLanguage = languageProperties.GetLangName();
      SetupVariables();
      SetupState();

      CSSCode = "";
      JSCode = "";
      PHPCode = "";
      VBSCode = "";
      ERBCode = "";
      JSPCode = "";
      GSPCode = "";
      ASPCode = "";
      HTMLCode = "";
      CFCode = "";
      CFSCode = "";
      SQLCode = "";
      XMLCode = "";
      RtParams = aRuntimeParameters;
   }

   /**
    * Function for getting the file extension of the file being operated on.
    * 
    * @param cntrResult
    *           A UCCFile object to store results of code counter
    */
   public void GetFileExt(UCCFile cntrResult)
   {
      fileExt = FileUtils.GetFileExt(cntrResult.FileName);
   }

   /**
    * Function for setting up the start and end tags for various languages.
    */
   private void SetupVariables()
   {
      // CSS tags
      CSSStartTags.add("<style>");
      CSSEndTags.add("</style>");

      // JS tags
      JSStartTags.add("<script>");
      JSStartTags.add("<script type=\"text/javascript\">");
      JSStartTags.add("<script type='text/javascript'>");
      JSStartTags.add("<script type=text/javascript>");
      JSStartTags.add("<script language=\"javascript\">");
      JSStartTags.add("<script language='javascript'>");
      JSStartTags.add("<script language=javascript>");
      JSStartTags.add("<script language=\"javascript\" type=\"text/javascript\">");
      JSStartTags.add("<script language='javascript' type='text/javascript'>");
      JSStartTags.add("<script language=javascript type=text/javascript>");
      JSEndTags.add("</script>");

      // PHP tags
      PHPStartTags.add("<?php");
      PHPEndTags.add("?>");

      // VBS tags
      VBSStartTags.add("<script type=\"text/vbscript\">");
      VBSStartTags.add("<script type='text/vbscript'>");
      VBSStartTags.add("<script type=text/vbscript>");
      VBSStartTags.add("<script language=\"vbscript\">");
      VBSStartTags.add("<script language='vbscript'>");
      VBSStartTags.add("<script language=vbscript>");
      VBSStartTags.add("<script language=\"vbscript\" type=\"text/vbscript\">");
      VBSStartTags.add("<script language='vbscript' type='text/vbscript'>");
      VBSStartTags.add("<script language=vbscript type=text/vbscript>");
      VBSEndTags.add("</script>");

      // ERB tags
      ERBStartTags.add("<%");
      ERBEndTags.add("%>");

      // JSP tags
      JSPStartTags.add("<%");
      JSPStartTags.add("<jsp:");
      JSPEndTags.add("%>");
      JSPEndTags.add("</jsp:");

      // GSP tags
      GSPStartTags.add("<g:");
      GSPEndTags.add("</g");

      // ASP tags
      ASPStartTags.add("<%");
      ASPStartTags.add("<script runat=");
      ASPStartTags.add("<asp:");
      ASPEndTags.add("%>");
      ASPEndTags.add("</asp:");

      // HTML tags

      // ColdFusion tags

      // ColdFusion Script tags
      CFSStartTags.add("<cfscript>");
      CFSEndTags.add("</cfscript>");

      // SQL tags
      SQLStartTags.add("<cfquery");
      SQLEndTags.add("</cfquery>");
   }

   /**
    * Function for setting up the initial state depending on which language the
    * file is.
    */
   private void SetupState()
   {
      openStartTag = false;
      openCFTag = false;

      if (fileLanguage.equals("HTML"))
      {
         languageState = LanguageState.IN_HTML;
      }
      else if (fileLanguage.equals("CSS"))
      {
         languageState = LanguageState.IN_CSS;
      }
      else if (fileLanguage.equals("JAVASCRIPT"))
      {
         languageState = LanguageState.IN_JS;
      }
      else if (fileLanguage.equals("PHP"))
      {
         languageState = LanguageState.IN_HTML;
      }
      else if (fileLanguage.equals("VBS"))
      {
         languageState = LanguageState.IN_VBS;
      }
      else if (fileLanguage.equals("RUBY"))
      {
         languageState = LanguageState.IN_HTML;
      }
      else if (fileLanguage.equals("JSP"))
      {
         languageState = LanguageState.IN_HTML;
      }
      else if (fileLanguage.equals("GROOVY"))
      {
         languageState = LanguageState.IN_HTML;
      }
      else if (fileLanguage.equals("ASP"))
      {
         languageState = LanguageState.IN_HTML;
      }
      else if (fileLanguage.equals("COLDFUSION"))
      {
         languageState = LanguageState.IN_HTML;
      }
      else if (fileLanguage.equals("XML"))
      {
         languageState = LanguageState.IN_XML;
      }
      else
      {
         logger.error(fileLanguage + " is not a multi-language supported language.");
      }
   }

   /**
    * Function for cutting out the embedded languages and writing them to their
    * own files for later processing.
    * 
    * @param cntrResults
    *           A UCCFile object ArrayList to store results of code counters
    * @param i
    *           The index of the UCCFile we want to work on
    */
   public boolean HandleEmbeddedCode(ArrayList<UCCFile> cntrResults, int i)
   {
      UCCFile cntrResult = cntrResults.get(i);

      int lineNumbers = 0;
      try
      {
         LineNumberReader lnr = new LineNumberReader(
                  new InputStreamReader(new FileInputStream(cntrResult.FileName), Constants.CHARSET_NAME));
         lnr.skip(Long.MAX_VALUE);
         lineNumbers = lnr.getLineNumber() + 1; // Add 1 because line index
                                                // starts at 0
         lnr.close();
      }
      catch (Exception ex)
      {

      }
      finally
      {
         if (lineNumbers > 10000)
         {
            return true;
         }
      }

      LineNumberReader reader = null;

      // Reset truncated lines count
      truncateLinesCount = 0;

      try
      {
         reader = new LineNumberReader(
                  new InputStreamReader(new FileInputStream(cntrResult.FileName), Constants.CHARSET_NAME));

         // Read first line
         String line = reader.readLine();

         // While we have lines in the file...
         while (line != null)
         {
            // Ignore line data beyond the single line threshold value
            if (RtParams.TruncThreshold != 0 && line.length() > RtParams.TruncThreshold)
            {
               line = line.substring(0, RtParams.TruncThreshold);
               truncateLinesCount++;
            }

            // Split code up
            HandleEmbeddedLanguages(line);

            // Read next line
            line = reader.readLine();
         }
      }
      catch (IOException e)
      {
         logger.error("The input reader failed to open.");
         logger.error(e);
      }
      finally
      {
         // If the original file was opened...
         if (reader != null)
         {
            // Close the original file
            try
            {
               cntrResult.IsCounted = true;
               reader.close();
            }
            catch (IOException e)
            {
               logger.error("The input reader failed to close.");
               logger.error(e);
            }
            reader = null;
         }

         PrintCode(cntrResults, i);

         // Log the number of lines in the file that were truncated
         if (truncateLinesCount > 0)
         {
            logger.warn("Truncated " + truncateLinesCount + " total lines in file " + cntrResult.FileName
                     + "  [-trunc set to " + Integer.toString(RtParams.TruncThreshold) + "]");
         }

         // Reset handlers
         Reset();
      }
      return false;
   }

   /**
    * Function for gathering all embedded languages together and saving them
    * into their respective strings.
    * 
    * @param line
    *           String coming in from file
    */
   public void HandleEmbeddedLanguages(String line)
   {
      String regEx;
      Matcher matcher;
      boolean blankLine = false;

      if (line.trim().isEmpty() && (languageState == LanguageState.IN_HTML || languageState == LanguageState.IN_XML))
      {
         blankLine = true;
      }

      // Replace <%= and <%! with <% so we don't have lines starting with ! or =
      line = line.replaceAll("\\<\\%\\=", "\\<\\%").replaceAll("\\<\\%\\!", "\\<\\%");

      // If we're in an HTML file
      if (fileLanguage.equals("HTML"))
      {
         // TODO put in support for *.erb and *.gsp here?

         // If we're in HTML code
         if (languageState == LanguageState.IN_HTML)
         {
            // Look for CSS start tags
            line = SearchForEmbeddedStart(CSSStartTags, CSSEndTags, LanguageState.IN_CSS, CSSCode, line);

            // Look for JS start tags
            line = SearchForEmbeddedStart(JSStartTags, JSEndTags, LanguageState.IN_JS, JSCode, line);

            // Look for VBS start tags
            line = SearchForEmbeddedStart(VBSStartTags, VBSEndTags, LanguageState.IN_VBS, VBSCode, line);
         }
         else if (languageState == LanguageState.IN_CSS) // If we're in CSS code
         {
            line = HandleEmbeddedCode(CSSEndTags, languageState, CSSCode, line);
         }
         else if (languageState == LanguageState.IN_JS) // If we're in JS code
         {
            line = HandleEmbeddedCode(JSEndTags, languageState, JSCode, line);
         }
         else if (languageState == LanguageState.IN_VBS) // If we're in VBS code
         {
            line = HandleEmbeddedCode(VBSEndTags, languageState, VBSCode, line);
         }
      }

      // If we're in an ASP file
      if (fileLanguage.equals("ASP"))
      {
         // If we're in HTML code
         if (languageState == LanguageState.IN_HTML)
         {
            // Look for CSS start tags
            line = SearchForEmbeddedStart(CSSStartTags, CSSEndTags, LanguageState.IN_CSS, CSSCode, line);

            // Look for JS start tags
            line = SearchForEmbeddedStart(JSStartTags, JSEndTags, LanguageState.IN_JS, JSCode, line);

            // Look for VBS start tags
            line = SearchForEmbeddedStart(VBSStartTags, VBSEndTags, LanguageState.IN_VBS, VBSCode, line);

            // Look for ASP start tags
            line = SearchForEmbeddedStart(ASPStartTags, ASPEndTags, LanguageState.IN_ASP, ASPCode, line);
         }
         else if (languageState == LanguageState.IN_CSS) // If we're in CSS code
         {
            line = HandleEmbeddedCode(CSSEndTags, languageState, CSSCode, line);
         }
         else if (languageState == LanguageState.IN_JS) // If we're in JS code
         {
            line = HandleEmbeddedCode(JSEndTags, languageState, JSCode, line);
         }
         else if (languageState == LanguageState.IN_VBS) // If we're in VBS code
         {
            line = HandleEmbeddedCode(VBSEndTags, languageState, VBSCode, line);
         }
         else if (languageState == LanguageState.IN_ASP) // If we're in ASP code
         {
            line = HandleEmbeddedCode(ASPEndTags, languageState, ASPCode, line);
         }
      }

      // If we're in an JSP file
      if (fileLanguage.equals("JSP"))
      {
         // If we're in HTML code
         if (languageState == LanguageState.IN_HTML)
         {
            // Look for CSS start tags
            line = SearchForEmbeddedStart(CSSStartTags, CSSEndTags, LanguageState.IN_CSS, CSSCode, line);

            // Look for JS start tags
            line = SearchForEmbeddedStart(JSStartTags, JSEndTags, LanguageState.IN_JS, JSCode, line);

            // Look for VBS start tags
            line = SearchForEmbeddedStart(VBSStartTags, VBSEndTags, LanguageState.IN_VBS, VBSCode, line);

            // Look for JSP start tags
            line = SearchForEmbeddedStart(JSPStartTags, JSPEndTags, LanguageState.IN_JSP, JSPCode, line);
         }
         else if (languageState == LanguageState.IN_CSS) // If we're in CSS code
         {
            line = HandleEmbeddedCode(CSSEndTags, languageState, CSSCode, line);
         }
         else if (languageState == LanguageState.IN_JS) // If we're in JS code
         {
            line = HandleEmbeddedCode(JSEndTags, languageState, JSCode, line);
         }
         else if (languageState == LanguageState.IN_VBS) // If we're in VBS code
         {
            line = HandleEmbeddedCode(VBSEndTags, languageState, VBSCode, line);
         }
         else if (languageState == LanguageState.IN_JSP) // If we're in JSP code
         {
            line = HandleEmbeddedCode(JSPEndTags, languageState, JSPCode, line);
         }
      }

      // If we're in an PHP file
      if (fileLanguage.equals("PHP"))
      {
         // If we're in HTML code
         if (languageState == LanguageState.IN_HTML)
         {
            // Look for CSS start tags
            line = SearchForEmbeddedStart(CSSStartTags, CSSEndTags, LanguageState.IN_CSS, CSSCode, line);

            // Look for JS start tags
            line = SearchForEmbeddedStart(JSStartTags, JSEndTags, LanguageState.IN_JS, JSCode, line);

            // Look for VBS start tags
            line = SearchForEmbeddedStart(VBSStartTags, VBSEndTags, LanguageState.IN_VBS, VBSCode, line);

            // Look for PHP start tags
            line = SearchForEmbeddedStart(PHPStartTags, PHPEndTags, LanguageState.IN_PHP, PHPCode, line);
         }
         else if (languageState == LanguageState.IN_CSS) // If we're in CSS code
         {
            line = HandleEmbeddedCode(CSSEndTags, languageState, CSSCode, line);
         }
         else if (languageState == LanguageState.IN_JS) // If we're in JS code
         {
            line = HandleEmbeddedCode(JSEndTags, languageState, JSCode, line);
         }
         else if (languageState == LanguageState.IN_VBS) // If we're in VBS code
         {
            line = HandleEmbeddedCode(VBSEndTags, languageState, VBSCode, line);
         }
         else if (languageState == LanguageState.IN_PHP) // If we're in PHP code
         {
            line = HandleEmbeddedCode(PHPEndTags, languageState, PHPCode, line);
         }
      }

      // If we're in an XML file
      if (fileLanguage.equals("XML"))
      {
         // If we're in XML code
         if (languageState == LanguageState.IN_XML)
         {
            // Look for CSS start tags
            line = SearchForEmbeddedStart(CSSStartTags, CSSEndTags, LanguageState.IN_CSS, CSSCode, line);

            // Look for JS start tags
            line = SearchForEmbeddedStart(JSStartTags, JSEndTags, LanguageState.IN_JS, JSCode, line);

            // Look for VBS start tags
            line = SearchForEmbeddedStart(VBSStartTags, VBSEndTags, LanguageState.IN_VBS, VBSCode, line);
         }
         else if (languageState == LanguageState.IN_CSS) // If we're in CSS code
         {
            line = HandleEmbeddedCode(CSSEndTags, languageState, CSSCode, line);
         }
         else if (languageState == LanguageState.IN_JS) // If we're in JS code
         {
            line = HandleEmbeddedCode(JSEndTags, languageState, JSCode, line);
         }
         else if (languageState == LanguageState.IN_VBS) // If we're in VBS code
         {
            line = HandleEmbeddedCode(VBSEndTags, languageState, VBSCode, line);
         }
      }

      // If we're not dealing with an open COLDFUSION tag
      if (openCFTag == false)
      {
         // If we're in a COLDFUSION file
         if (fileLanguage.equals("COLDFUSION"))
         {
            // If we're in COLDFUSION code
            if (languageState == LanguageState.IN_HTML)
            {
               // Look for CSS start tags
               line = SearchForEmbeddedStart(CSSStartTags, CSSEndTags, LanguageState.IN_CSS, CSSCode, line);

               // Look for JS start tags
               line = SearchForEmbeddedStart(JSStartTags, JSEndTags, LanguageState.IN_JS, JSCode, line);

               // Look for VBS start tags
               line = SearchForEmbeddedStart(VBSStartTags, VBSEndTags, LanguageState.IN_VBS, VBSCode, line);

               // Look for COLDFUSION SCRIPT start tags
               line = SearchForEmbeddedStart(CFSStartTags, CFSEndTags, LanguageState.IN_CFS, CFSCode, line);

               // Look for SQL start tags
               line = SearchForMultiLineEmbeddedStart(SQLStartTags, SQLEndTags, LanguageState.IN_SQL, SQLCode, line);
            }
            else if (languageState == LanguageState.IN_CSS) // If we're in CSS
                                                            // code
            {
               line = HandleEmbeddedCode(CSSEndTags, languageState, CSSCode, line);
            }
            else if (languageState == LanguageState.IN_JS) // If we're in JS
                                                           // code
            {
               line = HandleEmbeddedCode(JSEndTags, languageState, JSCode, line);
            }
            else if (languageState == LanguageState.IN_VBS) // If we're in VBS
                                                            // code
            {
               line = HandleEmbeddedCode(VBSEndTags, languageState, VBSCode, line);
            }
            else if (languageState == LanguageState.IN_CFS) // If we're in CFS
                                                            // code
            {
               line = HandleEmbeddedCode(CFSEndTags, languageState, CFSCode, line);
            }
            else if (languageState == LanguageState.IN_SQL) // If we're in SQL
                                                            // code
            {
               line = HandleEmbeddedCode(SQLEndTags, languageState, SQLCode, line);
            }
         }
      }

      // If the line wasn't made blank from above or it was already a blank line
      if (!line.trim().isEmpty() || blankLine == true)
      {
         // If we're in a COLDFUSION file
         if (fileLanguage.equals("COLDFUSION"))
         {
            // Strings for holding COLDFUSION code
            String closedStartTags;
            String closedEndTags;
            String openStartTags;

            // If we're not dealing with an open COLDFUSION tag
            if (openCFTag == false)
            {
               // Cut out all COLDFUSION start tags
               closedStartTags = "";
               regEx = "<cf(.*?)>";
               matcher = Pattern.compile(regEx).matcher(line);
               StringBuffer sbLine1 = new StringBuffer();

               // If we find one wipe it from the line and save it to the
               // closedStartTags string
               while (matcher.find())
               {
                  matcher.appendReplacement(sbLine1, "");
                  closedStartTags += matcher.group(0);
               }
               matcher.appendTail(sbLine1);
               line = sbLine1.toString().trim();

               // If the string holding start tags isn't empty, write it to the
               // CFCode string
               if (!closedStartTags.trim().isEmpty())
               {
                  CFCode += closedStartTags;
               }

               // Cut out all COLDFUSION end tags
               closedEndTags = "";
               regEx = "</cf(.*?)>";
               matcher = Pattern.compile(regEx).matcher(line);
               StringBuffer sbLine2 = new StringBuffer();

               // If we find one wipe it from the line and save it to the
               // closedEndTags string
               while (matcher.find())
               {
                  matcher.appendReplacement(sbLine2, "");
                  closedEndTags += matcher.group(0);
               }
               matcher.appendTail(sbLine2);
               line = sbLine2.toString().trim();

               // If the string holding end tags isn't empty, write it to the
               // CFCode string
               if (!closedEndTags.trim().isEmpty())
               {
                  CFCode += closedEndTags;
               }

               // Handle multi-line start tags (a start tag can be spread over
               // multiple lines)
               openStartTags = "";
               regEx = "<cf(.*?)>";
               matcher = Pattern.compile(regEx).matcher(line);

               // If we don't find a closed start tag
               if (!matcher.find())
               {
                  regEx = "<cf";
                  matcher = Pattern.compile(regEx).matcher(line);

                  // But we find an open start tag
                  if (matcher.find())
                  {
                     // Turn this global boolean to true so we don't operate on
                     // any open tag lines
                     openCFTag = true;

                     // Save the COLDFUSION part into the openStartTags string
                     openStartTags = line.substring(matcher.start());

                     // Cut the COLDFUSION part out of the line
                     line = line.substring(0, matcher.start());
                  }
               }

               // If the string holding open start tags isn't empty, write it to
               // the CFCode string
               if (!openStartTags.trim().isEmpty())
               {
                  CFCode += openStartTags;
               }

               // If any one of the CF tag strings is non-empty, add a newline
               // at the end (this preserves PSLOC)
               if (!closedStartTags.trim().isEmpty() || !closedEndTags.trim().isEmpty()
                        || !openStartTags.trim().isEmpty())
               {
                  CFCode += "\n";
               }
            }
            else // If we are dealing with an open COLDFUSION tag
            {
               // Handle multi-line tags
               openStartTags = "";
               regEx = ">";
               matcher = Pattern.compile(regEx).matcher(line);

               // If we find the close symbol
               if (matcher.find())
               {
                  // We're not in an open CF tag anymore
                  openCFTag = false;

                  // Save the COLDFUSION part into the openStartTags string
                  openStartTags = line.substring(0, matcher.end());

                  // Cut the COLDFUSION part out of the line
                  line = line.substring(matcher.end(), line.length());
               }
               else
               {
                  // Turn this global boolean to true so we don't operate on any
                  // open tag lines
                  openCFTag = true;

                  // Save the line as COLDFUSION code
                  openStartTags = line;

                  // Wipe the line
                  line = "";
               }

               // If the string holding open start tags isn't empty, write it to
               // the CFCode string
               if (!openStartTags.trim().isEmpty())
               {
                  CFCode += (openStartTags + "\n");
               }
            }
         }

         // Anything leftover is HTML
         if (!line.trim().isEmpty() || (openCFTag == false && blankLine == true))
         {
            if (fileLanguage.equals("XML"))
            {
               XMLCode += (line + "\n");
            }
            else
            {
               HTMLCode += (line + "\n");
            }
         }
      }
   }

   /**
    * Function for finding if a line contains a start tag for an embedded
    * language.
    * 
    * @param StartTags
    *           ArrayList of start tags for a particular language
    * @param EndTags
    *           ArrayList of end tags for a particular language
    * @param langState
    *           The language state we change to if start tags are found
    * @param savedCode
    *           String for saving embedded code
    * @param line
    *           String coming in from file
    * @return line with embedded code removed
    */
   public String SearchForEmbeddedStart(ArrayList<String> StartTags, ArrayList<String> EndTags, LanguageState langState,
            String savedCode, String line)
   {
      String regEx;
      Matcher matcher;
      String tempLine = "";

      // Look for start tags
      for (int st = 0; st < StartTags.size(); st++)
      {
         regEx = "\\Q" + StartTags.get(st) + "\\E";
         matcher = Pattern.compile(regEx).matcher(line.toLowerCase());

         // If we find one
         if (matcher.find())
         {
            // Set the state to the new language
            languageState = langState;

            // Grab the rest of the line after the open tag
            tempLine = line.substring(matcher.end()).trim();

            // If the leftovers aren't empty, send the leftovers to the embedded
            // handler
            if (!tempLine.isEmpty())
            {
               line = line.substring(0, matcher.end()) + HandleEmbeddedCode(EndTags, langState, savedCode, tempLine);
            }
            else
            {
               line = line.substring(0, matcher.end());
            }
         }
      }

      return line;
   }

   /**
    * Function for finding if a line contains a start tag for an embedded
    * language, except that this function handles start tags that can span
    * multiple lines.
    * 
    * @param StartTags
    *           ArrayList of start tags for a particular language
    * @param EndTags
    *           ArrayList of end tags for a particular language
    * @param langState
    *           The language state we change to if end tags are found
    * @param savedCode
    *           String for saving embedded code
    * @param line
    *           String coming in from file
    * @return line with embedded code removed
    */
   public String SearchForMultiLineEmbeddedStart(ArrayList<String> StartTags, ArrayList<String> EndTags,
            LanguageState langState, String savedCode, String line)
   {
      String regEx;
      Matcher matcher;
      String tempLine = "";

      // If we haven't already found a start tag
      if (openStartTag == false)
      {
         // Look for start tags
         for (int st = 0; st < StartTags.size(); st++)
         {
            regEx = "\\Q" + StartTags.get(st) + "\\E";
            matcher = Pattern.compile(regEx).matcher(line.toLowerCase());

            // If we find one
            if (matcher.find())
            {
               openStartTag = true;
               break;
            }
         }
      }

      // If we found an open start tag, but haven't found the close
      if (openStartTag == true)
      {
         regEx = "\\>";
         matcher = Pattern.compile(regEx).matcher(line);

         // If we find one
         if (matcher.find())
         {
            openStartTag = false;

            // Set the state to the new language
            languageState = langState;

            // Grab the rest of the line after the open tag
            tempLine = line.substring(matcher.end()).trim();

            // If the leftovers aren't empty, send the leftovers to the embedded
            // handler
            if (!tempLine.isEmpty())
            {
               line = line.substring(0, matcher.end()) + HandleEmbeddedCode(EndTags, langState, savedCode, tempLine);
            }
            else
            {
               line = line.substring(0, matcher.end());
            }
         }
      }

      return line;
   }

   /**
    * Function for saving embedded code into formatted strings until we find an
    * end tag.
    * 
    * @param EndTags
    *           ArrayList of end tags for a particular language
    * @param langState
    *           The language state we change to if end tags are found
    * @param savedCode
    *           String for saving embedded code
    * @param line
    *           String coming in from file
    * @return line with embedded code removed
    */
   public String HandleEmbeddedCode(ArrayList<String> EndTags, LanguageState langState, String savedCode, String line)
   {
      String regEx;
      Matcher matcher;
      String tempLine = line.trim();

      line = "";

      // If the line isn't empty
      if (!tempLine.isEmpty())
      {
         // Look for end tags
         for (int et = 0; et < EndTags.size(); et++)
         {
            regEx = "\\Q" + EndTags.get(et) + "\\E";
            matcher = Pattern.compile(regEx).matcher(tempLine.toLowerCase());

            // If we find one
            if (matcher.find())
            {
               // Reset the state back to the original
               SetupState();

               // Include the end tag in the original line
               line = tempLine.substring(matcher.start());

               // Get the leftover code on the code line
               tempLine = tempLine.substring(0, matcher.start());

               // Kick out of the loop
               break;
            }
         }

         // If the leftover code isn't empty
         if (!tempLine.trim().isEmpty())
         {
            tempLine += "\n";
            savedCode += tempLine;
         }
      }
      else // Handle empty lines
      {
         savedCode += "\n";
      }

      SaveCode(savedCode, langState);

      return line;
   }

   /**
    * Function for saving the summed up formatted strings for each embedded
    * language into their respectively named strings.
    * 
    * @param savedCode
    *           String for saving embedded code
    * @param langState
    *           The language state we are currently in
    */
   public void SaveCode(String savedCode, LanguageState langState)
   {
      if (langState == LanguageState.IN_HTML)
      {
         HTMLCode = savedCode;
      }
      else if (langState == LanguageState.IN_CSS)
      {
         CSSCode = savedCode;
      }
      else if (langState == LanguageState.IN_JS)
      {
         JSCode = savedCode;
      }
      else if (langState == LanguageState.IN_PHP)
      {
         PHPCode = savedCode;
      }
      else if (langState == LanguageState.IN_VBS)
      {
         VBSCode = savedCode;
      }
      else if (langState == LanguageState.IN_ERB)
      {
         ERBCode = savedCode;
      }
      else if (langState == LanguageState.IN_JSP)
      {
         JSPCode = savedCode;
      }
      else if (langState == LanguageState.IN_GSP)
      {
         GSPCode = savedCode;
      }
      else if (langState == LanguageState.IN_ASP)
      {
         ASPCode = savedCode;
      }
      else if (langState == LanguageState.IN_CF)
      {
         CFCode = savedCode;
      }
      else if (langState == LanguageState.IN_CFS)
      {
         CFSCode = savedCode;
      }
      else if (langState == LanguageState.IN_SQL)
      {
         SQLCode = savedCode;
      }
      else if (langState == LanguageState.IN_XML)
      {
         XMLCode = savedCode;
      }
   }

   /**
    * Function for printing and/or writing the accumulated embedded code to
    * file.
    * 
    * @param cntrResults
    *           A UCCFile object ArrayList to store results of code counters
    * @param i
    *           The index of the UCCFile we want to work on
    */
   public void PrintCode(ArrayList<UCCFile> cntrResults, int i)
   {
      UCCFile cntrResult = cntrResults.get(i);

      // Print embedded CSS code
      if (!CSSCode.trim().isEmpty())
      {
         CSSCode = CSSCode.substring(0, CSSCode.length() - 1);
         // logger.debug("Embedded CSS:");
         // logger.debug("\n\n" + CSSCode + "\n");

         WriteFile(cntrResult.FileName, ".EMB.css", CSSCode, DataTypes.LanguagePropertiesType.CSS, cntrResults, i);
      }

      // Print embedded JS code
      if (!JSCode.trim().isEmpty())
      {
         JSCode = JSCode.substring(0, JSCode.length() - 1);
         // logger.debug("Embedded JS:");
         // logger.debug("\n\n'" + JSCode + "'\n");

         WriteFile(cntrResult.FileName, ".EMB.js", JSCode, DataTypes.LanguagePropertiesType.JAVASCRIPT, cntrResults, i);
      }

      // Print embedded PHP code
      if (!PHPCode.trim().isEmpty())
      {
         PHPCode = PHPCode.substring(0, PHPCode.length() - 1);
         // logger.debug("Embedded PHP:");
         // logger.debug("\n\n" + PHPCode + "\n");

         WriteFile(cntrResult.FileName, ".EMB.php", PHPCode, DataTypes.LanguagePropertiesType.PHP, cntrResults, i);
      }

      // Print embedded VBS code
      if (!VBSCode.trim().isEmpty())
      {
         VBSCode = VBSCode.substring(0, VBSCode.length() - 1);
         // logger.debug("Embedded VBS:");
         // logger.debug("\n\n" + VBSCode + "\n");

         WriteFile(cntrResult.FileName, ".EMB.vbs", VBSCode, DataTypes.LanguagePropertiesType.VB_SCRIPT, cntrResults,
                  i);
      }

      // Print embedded ERB code
      if (!ERBCode.trim().isEmpty())
      {
         ERBCode = ERBCode.substring(0, ERBCode.length() - 1);
         // logger.debug("Embedded ERB:");
         // logger.debug("\n\n" + ERBCode + "\n");

         // WriteFile(cntrResult.FileName, ".EMB.erb", ERBCode,
         // DataTypes.LanguagePropertiesType.ERB, cntrResults, i);
      }

      // Print embedded JSP code
      if (!JSPCode.trim().isEmpty())
      {
         JSPCode = JSPCode.substring(0, JSPCode.length() - 1);
         // logger.debug("Embedded JSP:");
         // logger.debug("\n\n" + JSPCode + "\n");

         WriteFile(cntrResult.FileName, ".EMB.jsp", JSPCode, DataTypes.LanguagePropertiesType.JSP, cntrResults, i);
      }

      // Print embedded GSP code
      if (!GSPCode.trim().isEmpty())
      {
         GSPCode = GSPCode.substring(0, GSPCode.length() - 1);
         // logger.debug("Embedded GSP:");
         // logger.debug("\n\n" + GSPCode + "\n");

         // WriteFile(cntrResult.FileName, ".EMB.gsp", GSPCode,
         // DataTypes.LanguagePropertiesType.GSP, cntrResults, i);
      }

      // Print embedded ASP code
      if (!ASPCode.trim().isEmpty())
      {
         ASPCode = ASPCode.substring(0, ASPCode.length() - 1);
         // logger.debug("Embedded ASP:");
         // logger.debug("\n\n" + ASPCode + "\n");

         WriteFile(cntrResult.FileName, ".EMB.asp", ASPCode, DataTypes.LanguagePropertiesType.ASP, cntrResults, i);
      }

      // Print embedded HTML code
      if (!HTMLCode.trim().isEmpty())
      {
         HTMLCode = HTMLCode.substring(0, HTMLCode.length() - 1);
         // logger.debug("Embedded HTML:");
         // logger.debug("\n\n" + HTMLCode + "\n");

         WriteFile(cntrResult.FileName, ".EMB.html", HTMLCode, DataTypes.LanguagePropertiesType.HTML, cntrResults, i);
      }

      // Print embedded COLDFUSION code
      if (!CFCode.trim().isEmpty())
      {
         CFCode = CFCode.substring(0, CFCode.length() - 1);
         // logger.debug("Embedded COLDFUSION:");
         // logger.debug("\n\n" + CFCode + "\n");

         WriteFile(cntrResult.FileName, ".EMB.cfm", CFCode, DataTypes.LanguagePropertiesType.COLDFUSION, cntrResults,
                  i);
      }

      // Print embedded COLDFUSION SCRIPT code
      if (!CFSCode.trim().isEmpty())
      {
         CFSCode = CFSCode.substring(0, CFSCode.length() - 1);
         // logger.debug("Embedded COLDFUSION SCRIPT:");
         // logger.debug("\n\n" + CFSCode + "\n");

         WriteFile(cntrResult.FileName, ".EMB.cfs", CFSCode, DataTypes.LanguagePropertiesType.COLDFUSION_SCRIPT,
                  cntrResults, i);
      }

      // Print embedded SQL code
      if (!SQLCode.trim().isEmpty())
      {
         SQLCode = SQLCode.substring(0, SQLCode.length() - 1);
         // logger.debug("Embedded SQL:");
         // logger.debug("\n\n" + SQLCode + "\n");

         WriteFile(cntrResult.FileName, ".EMB.sql", SQLCode, DataTypes.LanguagePropertiesType.SQL, cntrResults, i);
      }

      // Print embedded XML code
      if (!XMLCode.trim().isEmpty())
      {
         XMLCode = XMLCode.substring(0, XMLCode.length() - 1);
         // logger.debug("Embedded XML:");
         // logger.debug("\n\n'" + XMLCode + "'\n");

         WriteFile(cntrResult.FileName, ".EMB.xml", XMLCode, DataTypes.LanguagePropertiesType.XML, cntrResults, i);
      }
   }

   /**
    * Function for writing the embedded code to file and assigning it to the
    * file list.
    * 
    * @param fileName
    *           Name of file we want to save
    * @param fileExt
    *           Extension of file we want to save
    * @param savedCode
    *           String full of saved embedded code
    * @param lang
    *           The language property type we want associated with the file
    * @param cntrResults
    *           A UCCFile object ArrayList to store results of code counters
    * @param i
    *           The index of the UCCFile we want to work on
    */
   public void WriteFile(String fileName, String fileExt, String savedCode, LanguagePropertiesType lang,
            ArrayList<UCCFile> cntrResults, int i)
   {
      File file = new File(FileUtils.BuildTempOutFileName_embeddedCodeFile(RtParams, cntrResults.get(i), fileExt));
      BufferedWriter bw;
      try
      {
         bw = new BufferedWriter(new FileWriter(file));
         bw.write(savedCode);
         if (bw != null)
         {
            try
            {
               bw.close();

               cntrResults.add(new UCCFile());
               cntrResults.get(cntrResults.size() - 1).FileName =
                        FileUtils.BuildTempOutFileName_embeddedCodeFile(RtParams, cntrResults.get(i), fileExt);
               cntrResults.get(cntrResults.size() - 1).LangProperty = lang;
               cntrResults.get(cntrResults.size() - 1).EmbOfIdx = i;
            }
            catch (IOException e)
            {
               logger.error("The embedded " + fileExt + " writer failed to close.");
               logger.error(e);
            }
            bw = null;
         }
      }
      catch (IOException e)
      {
         logger.error("The embedded " + fileExt + " writer failed to open.");
         logger.error(e);
      }
   }

   /**
    * Function for resetting the state and all embedded code strings.
    */
   public void Reset()
   {
      SetupState();
      CSSCode = "";
      JSCode = "";
      PHPCode = "";
      VBSCode = "";
      ERBCode = "";
      JSPCode = "";
      GSPCode = "";
      ASPCode = "";
      HTMLCode = "";
      CFCode = "";
      CFSCode = "";
      SQLCode = "";
      XMLCode = "";
   }
}