package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * VerilogLanguageProperties class stores properties of Verilog programming
 * language that will be used to count different types of code metrics for
 * source code written in Verilog.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class VerilogLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize Verilog language properties
    */
   public VerilogLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("Verilog 2005");
      SetLangName("VERILOG");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".v")));
      SetCaseSensitive(true);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList(";")));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("//")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList("/*")));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList("*/")));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList("`")));
      SetCompilerDirKeywords(
               new ArrayList<String>(Arrays.asList("`define", "`include", "`ifdef", "`else", "`endif", "`timescale")));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"")));
      SetLineContChars(new ArrayList<String>(Arrays.asList()));
      SetFuncStartKeywords(new ArrayList<String>(
               Arrays.asList("module"/* , "task", "function" */)));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("for", "forever", "repeat", "while")));
      SetCondKeywords(new ArrayList<String>(
               Arrays.asList("case", "if", "else if", "else", "for", "forever", "repeat", "while")));
      SetLogicalOps(new ArrayList<String>(Arrays.asList("!", "~", "&", "|", "^", "~&", "~|", "~^", "<<", ">>", "<", ">",
               ">=", "<=", "==", "!=", "===", "^~", "&&", "||")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=", "<=")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("always", "assign", "begin", "case", "casex", "casez",
               "deassign", "defparam", "disable", "end", "endcase", "for", "forever", "fork", "generate", "if",
               "else if", "else", "intitial", "join", "posedge", "repeat", "wait", "while", "$bitstoreal", "$display",
               "$dumpall", "$dumplimit", "$dumpfile", "$dumpflush", "$dumplimit", "$dumpoff", "$dumpon", "$dumpvar",
               "$dumpvars", "$fclose", "$fdisplay", "$finish", "$fmonitor", "$fopen", "$fstrobe", "$fwrite", "$itor",
               "$monitor", "$monitoroff", "$monitoron", "$printtimescale", "random", "readmemb", "readmemh", "realtime",
               "realtobits", "rtoi", "buf", "not", "and", "or", "nand", "nor", "xor", "xnor", "bufif0", "bufif1",
               "notif0", "notif1")));
      SetDataKeywords(new ArrayList<String>(Arrays.asList("event", "function", "genvar", "inout", "input", "integer",
               "localparam", "output", "parameter", "reg", "specparam", "supply0", "supply1", "task", "time", "tri",
               "tri0", "tri1", "triand", "trior", "trireg", "wand", "wire", "wor", "bit", "byte", "shortint", "int",
               "longint", "logic")));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("+", "-", "*", "/", "%")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList()));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList()));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList()));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("else", "endif", "endcase", "end", "default")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList()));
      // SetCyclCmplexKeywords(new ArrayList<String>(
      // Arrays.asList("case", "if", "else if", "else", "for", "forever",
      // "repeat", "while")));
      SetLslocKeywords(
               new ArrayList<String>(Arrays.asList("case", "if", "else if", "for", "forever", "repeat", "while")));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList()));
   }
}