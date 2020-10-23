/**
 * @author Anik Barua
 * @since 10-23-2020
 * @version 3.0
 *
 * Description: Lab #4 - This java program takes all the full weeks of fiscal
 * quarter CSV files from Spotify's Top 200 Artists CSV dataset, makes queue for
 * each file of song objects, then using a merge function, it returns a single
 * queue. Next it removes any duplicates, sorts the songs in ascending order by
 * song's name. I also have displaySongs method that prints out my songs in
 * queue in order they were added (first to last). Then as you play a song from
 * the queue, it removes the song from queue and plays(prints) the music in
 * "first in first out order", and adds song to my stack to keep track of the
 * recently played song. So if I want to know the last played song, the stack
 * can peek the last one which was recently added because it's in "last in first
 * out order". Also, it can print out the stack for a full history, in order,
 * the songs get played(added) from last to first.
 */
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;
import java.io.FileNotFoundException;

public class spotifyPlaylist { //class started

    public static void main(String[] args) throws Exception { //main

        PrintStream report1 = new PrintStream("allSongs.txt");
        // First output file will contain all the songs in my queue playlist.

        //CSV files of full weeks fiscal quarter in a array.
        String[] myFiles = {"week1.csv", "week2.csv", "week3.csv", "week4.csv",
            "week5.csv", "week6.csv", "week7.csv", "week8.csv", "week9.csv", "week10.csv",
            "week11.csv", "week12.csv", "week13.csv"};

        // Arraylist that will contain the queues for each csv files.
        ArrayList<Queue> allTheWeeks = new ArrayList<>();

        // Using the dataExtract method, it returns a queue after reading the data. 
        for (int i = 0; i < myFiles.length; i++) {
            Queue dataExtract = new Queue(myFiles[i]);
            allTheWeeks.add(dataExtract); //After reading from each csv, add them in queue arraylist.
        }

        // Using for loop, it will merge all my individual queue's in a single queue.
        for (int i = 1; i < allTheWeeks.size(); i++) {
            //Calls the merge fuction that takes two queue object and returns one after merging them.
            mergingFunction(allTheWeeks.get(0), allTheWeeks.get(i));
        }

        // Queue songs will be the playlist that will contain song objects from all my queues for each file. 
        Queue songs = allTheWeeks.get(0);

        songs.removeDuplicates(); // Using a method from the Queue class, it removes the duplicates from the list.

        songs.sort(); // Using a method from Queue class, it sorts the data in ascending order by song's name.

        songs.displaySongs(report1); // Displays all the songs in my queue playlist in first to last added order.

        // listenToSong() prints out the song that is in the first position in my queue playlist (first in first out).
        songs.listenToSong();
        songs.listenToSong();
        songs.listenToSong();
        songs.listenToSong();
        songs.listenToSong();
        songs.listenToSong();
        songs.listenToSong();

        // lastListened prints out the last song played (recently inserted in my stack - last in first out).
        songs.lastListened();

        // fullHistory prints out the history of the songs that was played in first to last order.
        songs.fullHistory();
    }

    /*
    The mergingFunction method takes two Queue and using the addAll method
    of Linkedlist, it adds both of them into one Queue and returns it.
     */
    public static void mergingFunction(Queue one, Queue two) {
        one.addAll(two); // Calls the addAll method from my Queue class.
    }
} //end of spotifyPlaylist class

/*
Song class takes all the information of a song and creates an Song object. It
implements a comparable method so that it can compare the Song objects with
song names and sort it in alphabetical order.
 */
class Song implements Comparable { //Class started

    //The csv files contains the position, artist's name, song name, total streams and the url.
    private int position, streams;
    private String artist, songName, url;

    // Constructor takes the postion, song name, artist's name, total streams, and the url
    public Song(int position, String songName, String artist, int streams, String url) {
        this.position = position;
        this.songName = songName;
        this.artist = artist;
        this.streams = streams;
        this.url = url;
    }

    // Returns the name of the song.
    public String getSong() {
        return this.songName;
    }

    // Returns a string
    @Override
    public String toString() {
        return "Song: " + this.songName + " by " + this.artist;
    }

    // This compareTo methods allows the Collection.sort from the linkedlist class,
    // to sort the list based on the song's name.
    @Override
    public int compareTo(Object obj) { //Takes an object of type Object
        Song name = (Song) obj; // Casting the object to Song class.
        int x = this.songName.compareToIgnoreCase(name.songName); //Comparing the song names.
        return x;
    }
} // end of Song class

/*
This is a Queue class for the Songs's object. It uses the LinkedList class
from the Java library. It contains general Queue methods and I added some
new methods like listenToSong, lastListened, fullHistory, sort, display,
and removeduplicates method.
 */
class Queue { //Class started

    private LinkedList<Song> songList; //LinkedList of Song objects.
    private SongHistoryList historyList; //Stack that will keep track of played song.
    private PrintStream report2; // PrintStream object where the playlist ouput will be printed.

    //Queue constructor that take as file
    public Queue(String x) throws FileNotFoundException {
        this.songList = new LinkedList(); //Instantiates a linkedlist with new keyword
        this.historyList = new SongHistoryList(); //Instantiates a new stack
        this.report2 = new PrintStream("playlist.txt"); //Instantiates a new txt file
        dataExtract(x); //Calls the dataExtract with the file so that it can extract the data.
    }

    /*
    The dataExtract method takes a csv file at a time, uses Multi-Dimensinal array
    from lab 2 to read the data from CSV, creates a song object using the song
    information and adds them to linkedlist.
     */
    public void dataExtract(String x) throws FileNotFoundException {
        //Row and Column number for multi-dimensinal array
        int row = 200;
        int column = 5;
        //Multi-Dimensinal array that will contain readings(Songs's data) from the csv file.
        String[][] array = new String[row][column];

        //Read in the csv file part
        try {
            Scanner sc = new Scanner(new File(x));
            for (int i = 0; i < row; i++) {
                String[] line = sc.nextLine().split(",(?=([^\"]|\"[^\"]*\")*$)");
                // Splits words by "," from each line
                for (int j = 0; j < column; j++) {
                    array[i][j] = line[j];
                }
            }
            sc.close(); //Scanner closed
        } catch (FileNotFoundException e) {
            e.getMessage();
        } // End of try and catch block

        //Using for loop, songs linkedlist is reading the data from the Multi-Dimensinal Array.
        for (int a = 0; a < 200; a++) {
            // Create a Song Object
            Song song = new Song(Integer.parseInt(array[a][0]), array[a][1], array[a][2],
                    Integer.parseInt(array[a][3]), array[a][4]);
            songList.add(song); // Add the song in my song LinkedList
        }
    }

    public void add(Song x) {
        this.songList.push(x); //Adds Song objects in the linkedlist
    }

    public void addAll(Queue x) {
        this.songList.addAll(x.songList); //Adds all the songs objects in the linkedlist
    }

    //Prints out the song that is in the first postion of my queue linkedlist.
    public void listenToSong() {
        this.historyList.addSong(songList.peek());; //Adds the song in my SongHistoryList stack.
        //Poll() method removes the song and prints it out from the queue (first in first out).
        this.report2.println("Playing -> " + songList.poll());

    }

    //Calls the lastListened() method from the SongHistoryList class, and prints out the last song added.
    public void lastListened() {
        report2.println("\nRecently Played - ");
        report2.println(historyList.lastListened()); //Prints out the recently played song.
    }

    //Prints out the fullHistory of my stack from backwards(last in first out order)
    public void fullHistory() {
        Stack stack = historyList.fullHistory(); //Gets the stack from SongHistoryList class
        report2.println("\nHistory - ");
        for (int i = stack.size() - 1; i >= 0; i--) {
            report2.println(stack.get(i)); //Prints out the stack from backwards (last in first out)
        }
    }

    public int size() {
        return this.songList.size(); //Returns the size of linkedlist
    }

    /*
    Since linkedlist is a type of collection, I am using the collections library sort
    method to sort my Song's LinkedList in ascending order. To use the collections.sort
    method, I implemented the comparable interface in my Song class and created a
    compareTo method so that it can sort by comparing the name of Songs.
     */
    public void sort() {
        //Collections.sort() method from the java.util.Collections class
        Collections.sort(this.songList);
    }

    /*
    This removeDuplicates method removes duplicates from my LinkedList.
     */
    public void removeDuplicates() {
        // Using LinkedHashMap map because it will not read any duplicates, and I can
        // change the map back to LinkedList and assign the values to my current LinkedList.
        LinkedHashMap<String, Song> map = new LinkedHashMap<>();

        // Using this for loop, the map uses the name of the song as a key and the
        // Song information as a value.
        for (int i = 0; i < 2600; i++) {
            map.put(songList.get(i).getSong(), songList.get(i));
        }
        // Assigning the map values to my songList. Now my LinkedList doesn't
        // contain any duplicates.
        this.songList = new LinkedList<>(map.values());
    }

    /*
    The displaySongs method prints out the songs in queue in order the song was added.
     */
    public void displaySongs(PrintStream report1) {
        int count = 0; //To keep track of how many song's are in my list
        Iterator x = this.songList.iterator(); //Using an Iterator to loop through my linkedlist
        while (x.hasNext()) {
            count++;
            report1.println(count + " " + x.next().toString());
            report1.println();
        }
    }
} // end of Queue class

/*
This is a Stack class that will keep track of recently played songs. It uses the
Stack class from the Java library. It contains addSong(), lastListened(), and
fullHistory() method.
 */
class SongHistoryList { //Class started

    private Stack<Song> stack; //Stack that will contain song objects

    //Constructor for SongHistoryList
    public SongHistoryList() {
        this.stack = new Stack(); //Instantiates a stack using new keyword.
    }

    // Add song in the stack as it is played
    public void addSong(Song song) {
        this.stack.push(song);  //Pushes song in the stack
    }

    public Song lastListened() {
        return this.stack.peek(); //Without deleting the song it peeks the top
        // song of the stack and returns the value.
    }

    public Stack fullHistory() {
        return this.stack; // Returns my full stack.
    }
} // end of SongHistoryList class