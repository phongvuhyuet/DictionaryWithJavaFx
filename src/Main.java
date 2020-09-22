public class Main {

    public static void main(String[] args) {
        DictionaryManagement dm = new DictionaryManagement();
        dm.insertFromFile();
        dm.dictionaryLookup();
        dm.dictionarySearcher();
//        dm.dictionaryBasic();
    }
}
