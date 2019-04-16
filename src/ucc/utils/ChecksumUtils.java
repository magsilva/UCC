package ucc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ucc.datatypes.Constants;

/**
 * This class contains all the common checksum operations. Functions in this
 * class are static so they can be called directly without instantiating an
 * object of this class.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class ChecksumUtils
{
   /** Instantiate the Log4j logger for this class */
   private static final Logger logger = LogManager.getLogger(ChecksumUtils.class);

   /**
    * Calculates checksum of a given file. Refactored from
    * http://www.javadocexamples.com/java/util/zip/Adler32/Adler32().html
    * 
    * @param file
    *           Input file whose checksum this calculates
    * @return Checksum value
    */
   public static long GetFileChecksum(File file)
   {
      long checksum = 0;

      try
      {
         if (file != null && file.exists() && file.isFile())
         {
            CheckedInputStream cis = null;
            try
            {
               cis = new CheckedInputStream(new FileInputStream(file), new Adler32());
            }
            catch (FileNotFoundException e)
            {
               System.err.println("File " + file.getName() + " not found.");
               System.exit(1);
            }

            int buffersize = (int) (file.length());
            byte[] buffer = new byte[buffersize];

            while (cis.read(buffer) >= 0)
            {
               // Read in the file completely
            }

            // Get the checksum value
            checksum = cis.getChecksum().getValue();

            // Close the stream
            cis.close();
         }
      }
      catch (IOException ioe)
      {

         logger.fatal("IO exception thrown while computing checksum for file " + file.getAbsolutePath());
         logger.debug(ioe);
         System.exit(1);
      }

      return checksum;
   }

   public static String makeSHA1Hash(String input)
   {
      MessageDigest md;
      String hexStr = "";
      try
      {
         md = MessageDigest.getInstance("SHA1");
         md.reset();
         byte[] buffer = input.getBytes(Constants.CHARSET_NAME);
         md.update(buffer);
         byte[] digest = md.digest();

         for (int i = 0; i < digest.length; i++)
         {
            hexStr += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
         }
      }
      catch (NoSuchAlgorithmException | UnsupportedEncodingException e)
      {
         logger.error("Error with the SHA1 hashing");
         logger.error(e);
      }
      return hexStr;
   }
}
