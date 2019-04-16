package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ColdFusionLanguageProperties class stores properties of ColdFusion
 * programming language that will be used to count different types of code
 * metrics for source code written in ColdFusion.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class ColdFusionLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize ColdFusion language properties
    */
   public ColdFusionLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      // Excellent ColdFusion Source: http://www.learncfinaweek.com/

      SetLangVersion("Cold Fusion 13");
      SetLangName("COLDFUSION");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".cfm")));
      SetCaseSensitive(true);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList()));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList()));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList("<!---")));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList("--->")));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList()));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "'")));
      SetLineContChars(new ArrayList<String>(Arrays.asList()));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList("cffunction")));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("cfloop")));
      SetCondKeywords(
               new ArrayList<String>(Arrays.asList("cfif", "cfelseif", "cfelse", "cfmatch", "cfcase", "cfloop")));
      SetLogicalOps(new ArrayList<String>(Arrays.asList("eq", "neq", "gt", "gte", "lt", "lte", "&&", "||", "not", "and",
               "or", "xor", "equiv", "imp", "is")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("cfabort", "cfassociate", "cfbreak", "cfcache", "cfcase",
               "cfcatch", "cfcontinue", "cfcookie", "cfdbinfo", "cfdefaultcase", "cfdirectory", "cfdiv", "cfdocument",
               "cfdump", "cfelse", "cfelseif", "cferror", "cfexchange", "cfexecute", "cfexit", "cffeed", "cffile",
               "cffinally", "cfflush", "cfform", "cfftp", "cfgrid", "cfheader", "cfhtmlhead", "cfhttp", "cfif",
               "cfimage", "cfindex", "cfinput", "cfinsert", "cfinvoke", "cflayout", "cfldap", "cflocation", "cflock",
               "cflog", "cflogin", "cflogout", "cfloop", "cfmail", "cfobject", "cfoutput", "cfparam", "cfpod", "cfpop",
               "cfpresentation", "cfprint", "cfprocessdirective", "cfprocparam", "cfprocresult", "cfquery",
               "cfregistry", "cfrethrow", "cfreturn", "cfsavecontent", "cfschedule", "cfscript", "cfsearch", "cfselect",
               "cfsetting", "cfsilent", "cfstoredproc", "cfswitch", "cfthread", "cfthrow", "cftimer", "cftrace",
               "cftransaction", "cftry", "cfupdate")));
      SetDataKeywords(new ArrayList<String>(Arrays.asList("cfapplication", "cfapplet", "cfargument", "cfcomponent",
               "cffunction", "cfimport", "cfinclude", "cfinterface", "cfproperty", "cfset")));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("+", "-", "*", "/", "++", "--", "mod", "%", "^")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("log", "log10")));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList("acos", "asin", "atn", "cos", "sin", "tan")));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList("abs", "arrayavg", "arraysum", "ceiling",
               "decrementvalue", "exp", "fix", "incrementvalue", "int", "max", "min", "mod", "pi", "precisionvaluate",
               "rand", "randomize", "randrange", "round", "sgn", "sqr")));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("cftry", "cfelse")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList()));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList()));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList("cfif", "cfcatch", "cfmatch", "cfloop")));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList()));
   }
}