
import java.util.Scanner;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;

/**
 * Class that contains helper methods for the Review Lab
 **/
public class Review {
  
  private static HashMap<String, Double> sentiment = new HashMap<String, Double>();
  private static ArrayList<String> posAdjectives = new ArrayList<String>();
  private static ArrayList<String> negAdjectives = new ArrayList<String>();
 
  
  private static final String SPACE = " ";
  
  static{
    try {
      Scanner input = new Scanner(new File("cleanSentiment.csv"));
      while(input.hasNextLine()){
        String[] temp = input.nextLine().split(",");
        sentiment.put(temp[0],Double.parseDouble(temp[1]));
        //System.out.println("added "+ temp[0]+", "+temp[1]);
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing cleanSentiment.csv");
    }
  
  
  //read in the positive adjectives in postiveAdjectives.txt
     try {
      Scanner input = new Scanner(new File("positiveAdjectives.txt"));
      while(input.hasNextLine()){
        String temp = input.nextLine().trim();
        System.out.println(temp);
        posAdjectives.add(temp);
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing postitiveAdjectives.txt\n" + e);
    }   
 
  //read in the negative adjectives in negativeAdjectives.txt
     try {
      Scanner input = new Scanner(new File("negativeAdjectives.txt"));
      while(input.hasNextLine()){
        negAdjectives.add(input.nextLine().trim());
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing negativeAdjectives.txt");
    }   
  }
  
  /** 
   * returns a string containing all of the text in fileName (including punctuation), 
   * with words separated by a single space 
   */
  public static String textToString( String fileName )
  {  
    String temp = "";
    try {
      Scanner input = new Scanner(new File(fileName));
      
      //add 'words' in the file to the string, separated by a single space
      while(input.hasNext()){
        temp = temp + input.next() + " ";
      }
      input.close();
      
    }
    catch(Exception e){
      System.out.println("Unable to locate " + fileName);
    }
    //make sure to remove any additional space that may have been added at the end of the string.
    return temp.trim();
  }
  
  /**
   * @returns the sentiment value of word as a number between -1 (very negative) to 1 (very positive sentiment) 
   */
  public static double sentimentVal( String word )
  {
    try
    {
      return sentiment.get(word.toLowerCase());
    }
    catch(Exception e)
    {
      return 0.0;
    }
  }
  
  /**
   * Returns the ending punctuation of a string, or the empty string if there is none 
   */
  public static String getPunctuation( String word )
  { 
    String punc = "";
    for(int i=word.length()-1; i >= 0; i--){
      if(!Character.isLetterOrDigit(word.charAt(i))){
        punc = punc + word.charAt(i);
      } else {
        return punc;
      }
    }
    return punc;
  }
  
  /** 
   * Randomly picks a positive adjective from the positiveAdjectives.txt file and returns it.
   */
  public static String randomPositiveAdj()
  {
    int index = (int)(Math.random() * posAdjectives.size());
    return posAdjectives.get(index);
  }
  
  /** 
   * Randomly picks a negative adjective from the negativeAdjectives.txt file and returns it.
   */
  public static String randomNegativeAdj()
  {
    int index = (int)(Math.random() * negAdjectives.size());
    return negAdjectives.get(index);
    
  }
  
  /** 
   * Randomly picks a positive or negative adjective and returns it.
   */
  public static String randomAdjective()
  {
    boolean positive = Math.random() < .5;
    if(positive){
      return randomPositiveAdj();
    } else {
      return randomNegativeAdj();
    }
  }
  public static double totalSentiment(String fileName)
  {
  
   String review = textToString(fileName); // Used the textToString method to create a string from the file input by the user. 
   String word = "";
   double total = 0;
   for (int i = 0; i < review.length(); i++)
   {
      if (review.substring(i, i+1).equals(" ") == false)
      {
         word += review.substring(i, i+1);
      }
      else
      {
         total += sentimentVal(removePunctuation(word));
         word = "";
      }
   }
   total += sentimentVal(removePunctuation(word));
   return total;
  }
   /**
   * Returns the word after removing any beginning or ending punctuation
   */
  public static String removePunctuation( String word )
  {
    while(word.length() > 0 && !Character.isAlphabetic(word.charAt(0)))
    {
      word = word.substring(1);
    }
    while(word.length() > 0 && !Character.isAlphabetic(word.charAt(word.length()-1)))
    {
      word = word.substring(0, word.length()-1);
    }
    
    return word;
  }
  public static int starRating(String fileName)
  {
      if (totalSentiment(fileName) <= -1)
      {
            return 1;
      }
      else if (totalSentiment(fileName) <= 0)
      {
            return 2;
      }
      else if (totalSentiment(fileName) <= 4)
      {
            return 3;
      }
      else if (totalSentiment(fileName) <= 8) 
      {
            return 4;
      }
      else
      {
            return 5;

      } 
      }
    
  }
   public static String fakeReviewStronger(String fileName)
  {
    String review = textToString(fileName);
    String word = "";
    String sentence = "";

    for (int i = 0; i < review.length(); i++) {
      
      if ((review.substring(i, i + 1).equals(" ")) || (i == review.length() - 1)) {
       
        if (word.endsWith(" ")) word = word.substring(0, word.length()-1);
        word += review.substring(i, i + 1);
        
        if (word.startsWith("*"))
        {
           double s  = sentimentVal(word);

           String newAdj = "";
           if (s < 0)
           {
               while ( (newAdj.equals("")) || (sentimentVal(newAdj) >= s) ) //Utilizes a while looop to generate new negative or postiive adjectives with a corresponding sentiment value of the current word in comparison to the newAdjective to make it stronger. 
                  newAdj = randomNegativeAdj();
           }
           else if (s > 0)
           {
               while ( (newAdj.equals("")) || (sentimentVal(newAdj) <= s) )
                  newAdj = randomPositiveAdj();

           }
           else  // keep neutral adjectives neutral, just remove the *
           {
               newAdj = word.substring(1);
           }

           sentence += newAdj + getPunctuation(word) + " ";
        } else {
           sentence += word + " ";
        }

        word = "";
      }
    }
    return sentence;
  }
  
    public static String fakeReview(String fileName)
      {
        String review = textToString(fileName);
        String word = "";
        String sentence = "";
        for (int i = 0; i < review.length(); i++)
          {
            if (review.substring(i, i+1).equals(" ") || i == review.length() - 1)
            {
              if (i == review.length() -1)  word += review.substring(i, i+1); // adds the last character in the file to the word. 
              
              if (word.startsWith("*"))
              {
                String newAdj = "";
                while (newAdj.equals(""))
                    newAdj = randomAdjective();
                sentence += newAdj + getPunctuation(word) + " ";
                word = "";
              }
              else 
              {
                sentence += word + " ";
                word = "";
              }
            } 
            else
            {
              word += review.substring(i, i+1);
            }
          }
          return sentence;
    }
    
 }
}
