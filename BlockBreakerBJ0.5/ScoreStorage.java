import java.nio.file.*;
import java.util.*;
import java.io.*;


/**
 * This class writes the player's name a score into a local file
 * 
 */
public class ScoreStorage
{
    private String name;
    private int pointStore;

    /**
     * Takes the player's name and score and writes it into a file
     *
     * @param name Player's name
     * @param pointStore Player's score
     */
    public ScoreStorage(String name, int pointStore) throws IOException
    {
        this.name=name;
        this.pointStore=pointStore;
        if (Files.exists(Paths.get("score.txt"))!=true)
        {
//            ScoreStorage.writeToTextFile("score.txt",this.name+" "+this.pointStore+" ");
        	ScoreStorage.writeToTextFile("score.txt",this.name+" "+this.pointStore+"\r\n");
        }
        else
        {
//            ScoreStorage.appendToTextFile("score.txt",this.name+" "+this.pointStore+" " );
        	ScoreStorage.appendToTextFile("score.txt",this.name+" "+this.pointStore+"\r\n");
        }
    }

    /**
     * This method reads the whole file
     * @param fileName This is the name of the file to be read
     * @return String This returns all of the text in the file 
     */
    public String readTextFile(String fileName) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(fileName)));
        return content;
    }

    /**
     * This method reads the whole file line by line
     * @param fileName This is the name of the file to be read
     * @return List This returns all of the text in the file in a list of Strings 
     */
    public List<String> readTextFileByLines(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        return lines;
    }

    /**
     * This method writes a given String to a file
     * @param fileName This is the name of the file to be written to
     * @param content This is the String to be written 
     */
    public static void writeToTextFile(String fileName, String content) throws IOException {
        Files.write(Paths.get(fileName), content.getBytes(), StandardOpenOption.CREATE);
    }

    /**
     * This method appends a given String to the end of a file
     * @param fileName This is the name of the file to be written to
     * @param content This is the String to be appended 
     */
    public static void appendToTextFile(String fileName, String content) throws IOException {
        Files.write(Paths.get(fileName), content.getBytes(), StandardOpenOption.APPEND);
    }

    /**
     * This method clears a file, making it empty
     * @param fileName This is the name of the file to be cleared
     */
    public static void clearFile(String fileName) throws IOException
    {
        new PrintWriter(fileName).close();
    }
}
