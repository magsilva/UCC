package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ADALanguageProperties class stores properties of ADA programming language
 * that will be used to count different types of code metrics for source code
 * written in ADA.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class ADALanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize ADA language properties
    */
   public ADALanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("");
      SetLangName("ADA");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".ada", ".adb", ".ads")));
      SetCaseSensitive(false);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList(";")));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("--")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList()));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList()));
      // From https://en.wikibooks.org/wiki/Ada_Programming/Pragmas
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList("pragma")));
      /*
       * THESE ARE ALL THE OFFICIAL PRAGMAS, BUT ALL ARE PRECEDED BY "pragma" SO
       * WE CAN CATCH ALL OF THEM WITH JUST 1 COMPILER DIRECTIVE
       * 
       * "all_calls_remote", "assert", "assertion_policy", "asynchronous",
       * "atomic", "atomic_components", "attach_handler", "controlled",
       * "convention", "cpu", "default_storage_pool", "detect_blocking",
       * "discard_names", "dispatching_domain", "elaborate", "elaborate_all",
       * "elaborate_body", "export", "import", "independent",
       * "independent_component", "inline", "inspection_point", "interface",
       * "interrupt_handler", "interrupt_priority", "linker_options", "list",
       * "locking_policy", "memory_size", "no_return", "normalize_scalars",
       * "optimize", "pack", "page", "partition_elaboration_policy",
       * "preelaborable_initialization", "preelaborate", "priority",
       * "priority_specific_dispatching", "profile", "pure", "queueing_policy",
       * "relative_deadline", "remote_call_interface", "remote_types",
       * "restrictions", "reviewable", "shared", "shared_passive",
       * "storage_size", "storage_unit", "suppress", "system_name",
       * "task_dispatching_policy", "unchecked_union", "unsuppress", "volatile",
       * "volatile_components"
       */
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"")));
      SetLineContChars(new ArrayList<String>(Arrays.asList("\\")));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList("function", "procedure")));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("loop", "while", "for")));
      SetCondKeywords(new ArrayList<String>(Arrays.asList("if", "elsif", "loop", "while", "for", "case", "when")));
      SetLogicalOps(new ArrayList<String>(
               Arrays.asList("=", "<", ">", "/=", ">=", "<=", "&", "and", "or", "xor", "in", "not in")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList(":=")));
      // Leftover reserved keywords in ADA from
      // https://en.wikibooks.org/wiki/Ada_Programming/Keywords
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("abort", "abstract", "accept", "access", "aliased",
               "all", "at", "begin", "body", "declare", "delay", "delta", "digits", "do", "else", "entry", "exception",
               "exit", "generic", "goto", "interface", "is", "limited", "new", "not", "null", "of", "others", "out",
               "overriding", "package", "pragma", "private", "protected", "raise", "range", "record", "renames",
               "requeue", "return", "reverse", "select", "separate", "some", "subtype", "synchronized", "tagged",
               "task", "terminate", "then", "type", "until", "use", "with", "if")));
      // From http://www.modula2.org/sb/env/index117.htm
      SetDataKeywords(new ArrayList<String>(Arrays.asList("array", "constant", "boolean", "byte_boolean",
               "byte_integer", "character", "dword_boolean", "dword_integer", "duration", "float", "integer",
               "long_float", "long_integer", "natural", "positive", "qword_integer", "short_float", "short_integer",
               "string", "unsigned_byte_integer", "unsigned_dword_integer", "unsigned_integer", "unsigned_word_integer",
               "wide_character", "wide_wide_character", "word_boolean", "word_integer")));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("+", "-", "*", "/", "mod", "rem")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("log")));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList("arccos", "arccosh", "arccot", "arccoth", "arcsin",
               "arcsinh", "arctan", "arctanh", "cos", "coth", "sin", "sinh", "tan", "tanh")));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList("abs", "exp", "random", "sqrt")));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("end", "else", "begin", "exception", "declare")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList()));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList("if", "elsif", "loop", "while", "for", "when")));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList("if", "elsif", "loop", "while", "for", "case")));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList()));
   }
}
