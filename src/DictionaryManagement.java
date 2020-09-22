import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DictionaryManagement {
    Dictionary dictionary = new Dictionary();

    public void insertFromCommandline() {
        int numberOfWord;
        String wordTarget, wordExplain;
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of words: ");
        numberOfWord = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < numberOfWord; ++i) {
            System.out.println("\nWord " + (i + 1) + ": ");
            System.out.print("English: ");
            wordTarget = sc.nextLine();
            System.out.print("Vietnamese: ");
            wordExplain = sc.nextLine();

            Word newWord = new Word(wordTarget, wordExplain);
            dictionary.listWords.add(newWord);
        }
    }

    public void showAllWords() {
        System.out.println("No\tEnglish\t\tVietnamese");
        List<Word> list = dictionary.listWords;
        for (int i = 0; i < list.size(); ++i) {
            Word word = list.get(i);
            System.out.println((i + 1) + "\t " + word.getWordTarget() + "\t\t " + word.getWordExplain());
        }
    }

    public void dictionaryBasic() {
        insertFromCommandline();
        showAllWords();
    }

    public List<Word> insertFromFile() {
        FileReader fr = null;
        BufferedReader br = null;
        List<Word> list = new ArrayList<>();

        try {
            fr = new FileReader("dictionary.txt");
            br = new BufferedReader(fr);
            String s;
            while ((s = br.readLine()) != null) {
                String[] word = s.split("\t");
                Word newWord = new Word(word[0], word[1]);
                list.add(newWord);
            }
            br.close();
            fr.close();
        } catch (Exception e) {
                e.printStackTrace();
        }
        this.dictionary.listWords = list;
        return list;
    }

    public void dictionaryExportToFile(List<Word> list) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        String data = "";
        try {
            fw = new FileWriter("dictionary.txt");
            bw = new BufferedWriter(fw);
            for (Word word : list) {
                data += word.getWordTarget() + "\t" + word.getWordExplain() + "\n";
            }
            bw.write(data);
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
