package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * VHDLLanguageProperties class stores properties of VHDL programming language
 * that will be used to count different types of code metrics for source code
 * written in VHDL.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class VHDLLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize VHDL language properties
    */
   public VHDLLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("VHDL");
      SetLangName("VHDL");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".vhd", ".vhdl")));
      SetCaseSensitive(false);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList(";")));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("--")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList()));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList("-- pragma", "-- synopsis")));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"")));
      SetLineContChars(new ArrayList<String>(Arrays.asList()));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList("function", "procedure")));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("loop", "for", "while")));
      SetCondKeywords(new ArrayList<String>(Arrays.asList("if", "else if", "else", "loop", "for", "while")));
      SetLogicalOps(new ArrayList<String>(Arrays.asList("=", ">", "<", "/=", ">=", "<=", "&", "not", "and", "or",
               "nand", "nor", "xor", "xnor", "sll", "srl", "sla", "sra", "rol", "ror")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=>", ":=", "<=")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("after", "architecture", "assert", "begin", "block",
               "body", "case", "component", "configuration", "disconnect", "else", "elsif", "end", "entity", "exit",
               "for", "function", "generate", "if", "inertial", "library", "loop", "map", "next", "null", "on",
               "others", "package", "port", "procedure", "process", "reject")));
      SetDataKeywords(new ArrayList<String>(
               Arrays.asList(/*
                              * "access", "alias", "attribute", "buffer", "bus",
                              * "constant", "file", "generic", "group", "label",
                              * "linkage", "literal", "new", "range", "record",
                              * "register", "shared", "signal", "subtype",
                              * "type", "units", "variable"
                              */
                        "integer", "real", "boolean", "character", "time", "string", "bit", "bit_vector", "std_ulogic",
                        "std_logic", "std_ulogic_vector", "std_logic_vector")));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("+", "-", "*", "**", "/", "mod", "abs", "rem")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList()));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList()));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList()));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("else", "default")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList()));
      SetCyclCmplexKeywords(
               new ArrayList<String>(Arrays.asList("for", "if", "else", "elseif", "loop", "next", "when", "while")));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList("if", "elsif", "loop", "for", "while", "with")));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList()));
   }
}