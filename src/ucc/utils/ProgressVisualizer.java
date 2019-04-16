package ucc.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is constructed under one of the most intuitive, not fastest, means
 * of implementing a text-line progress bar.
 */
public class ProgressVisualizer
{
   // Instantiate the Log4j logger for this class
   private static final Logger logger = LogManager.getLogger(ProgressVisualizer.class);

   // Class constants
   public static final double DONE = 1.0;
   private static final String RESET_CHARACTER = "\r";

   // Setup progress visualizer state
   private boolean done = false;
   private double previousProgress = 0.0;
   private double percentThreshold = 0.0;

   private String doneMessage = "DONE";
   private String actionMessage = "";
   private int progressBarLength = 10;
   private String progressBar = "";

   /**
    * @param progressBarLength
    *           (int) - the discrete finite length of the progress bar itself
    * @param percentThreshold
    *           (double) - the percentage (out of 100.0%) threshold for
    *           determining when to print the progress bar
    * @param doneMessage
    *           Message to indicate the processing is complete
    * @param actionMessage
    *           Message to indicate the processing being completed
    */
   public ProgressVisualizer(int progressBarLength, double percentThreshold, String doneMessage, String actionMessage)
   {
      this.progressBarLength = progressBarLength;
      this.percentThreshold = percentThreshold;
      this.doneMessage = doneMessage;
      this.actionMessage = actionMessage;
   }

   /**
    * This is a default value constructor for the progress visualizer.
    * 
    * @param actionMessage
    *           Message to indicate the processing being completed
    */
   public ProgressVisualizer(String actionMessage)
   {
      this(50, 1.0, "DONE", actionMessage);
   }

   /**
    * This method prints the progress bar based on the progress normalized
    * between 0.0 and 1.0.
    * 
    * The method can also be used to test whether the progress bar has
    * completed.
    * 
    * @param message
    *           (String) - message to display
    * @param progress
    *           (double) - the current progress normalized between 0.0 and 1.0
    * @return true if the action succeeded and false if it failed
    */
   public boolean printProgressBar(String message, double progress)
   {
      if (done == false)
      {
         // We have to round because due issues using double.
         // Out precision is to five additional places
         progress = Math.round(progress * 100000.0) / 100000.0;
         String resetCharacter = RESET_CHARACTER;
         if (0.0 <= progress && progress <= 1.0)
         {
            // Create the buffered space
            int length = progressBarLength - message.length();
            progressBar = message;
            for (int i = 0; i < length; i++)
            {
               progressBar += ".";
            }

            // Add the completion status
            if (progress < 1.0)
            {
               // Determine how far the progress bar should extend
               length = (int) Math.round(progress * progressBarLength);
               progressBar += (int) Math.round(progress * 100) + "%";
            }
            else
            {
               progressBar += doneMessage;
               // We are done; go to the next line
               resetCharacter = "\n";
               done = true;
            }
            progressBar += resetCharacter;
            System.out.print(progressBar);
         }
         else
         {
            // this can occur, but has no impact on execution
            // logger.warn("The input progress value was invalid: " + progress);
            // logger.warn("The progress must be between 0.0 and 1.0
            // inclusive.");
         }
      }
      return done;
   }

   /**
    * This method prints the progress bar (as determined by a threshold) based
    * on the progress normalized between 0.0 and 1.0.
    * 
    * The method can also be used to test whether the progress bar has
    * completed.
    * 
    * @param message
    *           (String) - message to display
    * @param progress
    *           (double) - the current progress normalized between 0.0 and 1.0
    * @return true if the action succeeded and false if it failed
    */
   public boolean printProgressBarWithThreshold(String message, double progress)
   {
      // NOTE: I chose progress >= 1.0 to be more encompassing, as
      // printProgressBar is capable of handling the erroneous > 1.0 case
      if (((progress - previousProgress) * 100) >= percentThreshold || progress >= 1.0)
      {
         // update the progress tracking status
         previousProgress = progress;
         return printProgressBar(message, progress);
      }
      return false;
   }

   /**
    * This method prints the progress bar (as determined by a threshold) based
    * on the progress normalized between 0.0 and 1.0.
    * 
    * @param progress
    *           (double) - the progress percentage
    * @return (boolean) - whether or not the progress visualizer is done and has
    *         terminated
    */
   public boolean printProgressBarWithThreshold(double progress)
   {
      return printProgressBarWithThreshold(actionMessage, progress);
   }

   /**
    * This method resets the progress visualizer such that it can be used again.
    */
   public void reset()
   {
      done = false;
   }
}