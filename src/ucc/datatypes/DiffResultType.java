package ucc.datatypes;

import ucc.datatypes.DataTypes.LanguagePropertiesType;

/**
 * DiffResultType class contains data structures to store results of differencer
 * operation. This result set is per matched file pair that was run through the
 * differencer.
 *
 * @author Integrity Applications Incorporated
 *
 */
public class DiffResultType
{
   /**
    * Enumerated type to identify how files in a file pair are different from
    * each other
    */
   public enum ModificationType
   {
      /** Identifier for a file that is modified */
      Mod,
      /** Identifier for file that is unmodified */
      Unmod,
      /** Identifier for file that is new (i.e. added to the baseline) */
      Add,
      /**
       * Identifier for file that is deleted (i.e. removed from the baseline)
       */
      Del
   }

   /** Input source file name of baseline A */
   public String FileNameA;

   /** Input source file name of baseline B */
   public String FileNameB;

   /** Input source file object of baseline A */
   public UCCFile FileA;

   /** Input source file object of baseline B */
   public UCCFile FileB;

   /** Programming language of input source file */
   public LanguagePropertiesType LangProperty;

   /**
    * A flag to indicate whether the file pair is identical. If it is, no need
    * to difference the two files.
    */
   public boolean ExactMatch;

   /** A flag to indicate whether the file pair has been differenced */
   public boolean IsDiffed;

   /**
    * A flag to indicate whether the file pair contains a duplicate file from
    * either baseline.
    */
   public boolean IsDup;

   /** Number of new lines between a file pair */
   public int NewLines;

   /** Number of deleted lines between a file pair */
   public int DeletedLines;

   /** Number of modified lines between a file pair */
   public int ModLines;

   /** Number of unmodified lines between a file pair */
   public int UnmodLines;

   /** Indicates how the file pair is different */
   public ModificationType ModType;

   /**
    * Constructs a DiffResultType object with default values
    */
   public DiffResultType()
   {
      FileNameA = "";
      FileNameB = "";
      FileA = null;
      FileB = null;
      LangProperty = LanguagePropertiesType.C_CPP;
      ExactMatch = false;
      IsDiffed = false;
      IsDup = false;
      NewLines = 0;
      DeletedLines = 0;
      ModLines = 0;
      UnmodLines = 0;
      ModType = ModificationType.Mod;
   }

   /**
    * Constructs a DiffResultType object with given values
    * 
    * @param fileNameA
    *           Name of file A
    * @param fileNameB
    *           Name of file B
    * @param langProp
    *           Language property of both files
    * @param fileA
    *           UCCFile object for File A
    * @param fileB
    *           UCCFile object for File B
    */
   public DiffResultType(String fileNameA, String fileNameB, LanguagePropertiesType langProp, UCCFile fileA,
            UCCFile fileB)
   {
      FileNameA = fileNameA;
      FileNameB = fileNameB;
      FileA = fileA;
      FileB = fileB;
      LangProperty = langProp;
      ExactMatch = false;
      IsDiffed = false;
      IsDup = false;
      NewLines = 0;
      DeletedLines = 0;
      ModLines = 0;
      UnmodLines = 0;
      ModType = ModificationType.Mod;
   }

   /**
    * Constructs a DiffResultType object with given values
    * 
    * @param fileNameA
    *           Name of file A
    * @param fileNameB
    *           Name of file B
    * @param langProp
    *           Language property of both files
    * @param exactMatch
    *           Indicates whether File A and File B are identical
    * @param fileA
    *           UCCFile object for File A
    * @param fileB
    *           UCCFile object for File B
    */
   public DiffResultType(String fileNameA, String fileNameB, LanguagePropertiesType langProp, boolean exactMatch,
            UCCFile fileA, UCCFile fileB)
   {
      FileNameA = fileNameA;
      FileNameB = fileNameB;
      FileA = fileA;
      FileB = fileB;
      LangProperty = langProp;
      ExactMatch = exactMatch;
      IsDiffed = false;
      IsDup = false;
      NewLines = 0;
      DeletedLines = 0;
      ModLines = 0;
      UnmodLines = 0;
      ModType = ModificationType.Mod;
   }

   /**
    * Constructs a DiffResultType object with given values
    * 
    * @param fileNameA
    *           Name of file A
    * @param fileNameB
    *           Name of file B
    * @param langProp
    *           Language property of both files
    * @param exactMatch
    *           Indicates whether File A and File B are identical
    * @param isDup
    *           Indicates whether File A is a duplicate or File B is a duplicate
    * @param fileA
    *           UCCFile object for File A
    * @param fileB
    *           UCCFile object for File B
    */
   public DiffResultType(String fileNameA, String fileNameB, LanguagePropertiesType langProp, boolean exactMatch,
            boolean isDup, UCCFile fileA, UCCFile fileB)
   {
      FileNameA = fileNameA;
      FileNameB = fileNameB;
      FileA = fileA;
      FileB = fileB;
      LangProperty = langProp;
      ExactMatch = exactMatch;
      IsDiffed = false;
      IsDup = isDup;
      NewLines = 0;
      DeletedLines = 0;
      ModLines = 0;
      UnmodLines = 0;
      ModType = ModificationType.Mod;
   }
}
