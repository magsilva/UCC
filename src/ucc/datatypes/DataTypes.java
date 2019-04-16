package ucc.datatypes;

/**
 * DataTypes class provides data types that are used across the baseline
 * 
 * @author Integrity Applications Incorporated
 *
 */
public class DataTypes
{
   private static int fileIndex = 0;

   /**
    * Enumerated type to identify programming language associated with a file
    */
   public enum LanguagePropertiesType
   {
      // Complete list
      /** Identifier for source code file written in ADA programming language */
      ADA(fileIndex++),
      /** Identifier for source code file written in ASP programming language */
      ASP(fileIndex++),
      /**
       * Identifier for source code file written in Assembly programming
       * language
       */
      ASSEMBLY(fileIndex++),
      /**
       * Identifier for source code file written in Bash programming language
       */
      BASH(fileIndex++),
      /**
       * Identifier for source code file written in C or C++ programming
       * languages
       */
      C_CPP(fileIndex++),
      /** Identifier for source code file written in C# programming language */
      CSHARP(fileIndex++),
      /**
       * Identifier for source code file written in C Shell programming language
       */
      CSHELL(fileIndex++),
      /**
       * Identifier for source code file written in Coldfusion programming
       * language
       */
      COLDFUSION(fileIndex++),
      /**
       * Identifier for source code file written in Coldfusion script
       * programming language
       */
      COLDFUSION_SCRIPT(fileIndex++),
      /** Identifier for source code file written in CSS programming language */
      CSS(fileIndex++),
      /**
       * Identifier for source code file written in DOS Batch programming
       * language
       */
      DOS_BATCH(fileIndex++),
      /**
       * Identifier for source code file written in Fortran programming language
       */
      FORTRAN(fileIndex++),
      /**
       * Identifier for source code file written in HTML programming language
       */
      HTML(fileIndex++),
      /** Identifier for source code file written in IDL programming language */
      IDL(fileIndex++),
      /**
       * Identifier for source code file written in Java programming language
       */
      JAVA(fileIndex++),
      /**
       * Identifier for source code file written in JavaScript programming
       * language
       */
      JAVASCRIPT(fileIndex++),
      /** Identifier for source code file written in JSP programming language */
      JSP(fileIndex++),
      /**
       * Identifier for source code file written in Makefile programming
       * language
       */
      MAKEFILE(fileIndex++),
      /**
       * Identifier for source code file written in Matlab programming language
       */
      MATLAB(fileIndex++),
      /** Identifier for source code file written in NeXtMidas macro language */
      NEXTMIDAS(fileIndex++),
      /**
       * Identifier for source code file written in Pascal programming language
       */
      PASCAL(fileIndex++),
      /**
       * Identifier for source code file written in Perl programming language
       */
      PERL(fileIndex++),
      /** Identifier for source code file written in PHP programming language */
      PHP(fileIndex++),
      /**
       * Identifier for source code file written in Python programming language
       */
      PYTHON(fileIndex++),
      /**
       * Identifier for source code file written in Ruby programming language
       */
      RUBY(fileIndex++),
      /**
       * Identifier for source code file written in Scala programming language
       */
      SCALA(fileIndex++),
      /** Identifier for source code file written in SQL programming language */
      SQL(fileIndex++),
      /** Identifier for source code file written in VB programming language */
      VB(fileIndex++),
      /**
       * Identifier for source code file written in VB Script programming
       * language
       */
      VB_SCRIPT(fileIndex++),
      /**
       * Identifier for source code file written in Verilog programming language
       */
      VERILOG(fileIndex++),
      /**
       * Identifier for source code file written in VHDL programming language
       */
      VHDL(fileIndex++),
      /**
       * Identifier for source code file written in Xmidas programming language
       */
      XMIDAS(fileIndex++),
      /** Identifier for source code file written in XML programming language */
      XML(fileIndex++),
      /**
       * Identifier for source code file written in XSSL programming language
       */
      // XSL(fileIndex++),
      /**
       * Identifier for source code file written in R programming language
       */
      R(fileIndex++),
      /**
       * Identifier for source code file written in GO programming language
       */
      GO(fileIndex++),
      /**
       * Identifier for source code file written in a custom programming
       * language provided by the user
       */
      // NOTE: Keep the custom language as the last language in the list
      CUSTOM(fileIndex++);

      /** Index associated with enumerated value */
      private int LangIdx;

      /**
       * Constructor
       * 
       * @param idx
       *           Index for the Language Properties element
       */
      private LanguagePropertiesType(int idx)
      {
         this.LangIdx = idx;
      }

      /**
       * Get index associated with the enum value
       * 
       * @return Index of language type
       */
      public int GetIndex()
      {
         return this.LangIdx;
      }
   }

   /** Enumerated type to identify input file types */
   public enum SourceFileType
   {
      /**
       * Identifier for source code file written in one of the programming
       * languages
       */
      CODE,
      /** Identifier for file containing data values */
      DATA,
      /** Identifier for text file */
      TEXT
   }
}
