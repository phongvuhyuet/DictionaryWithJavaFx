public class DictionaryCommandLine {

    public void dictionaryBasic() {
        DictionaryManagement dictionaryManagement = new DictionaryManagement();
        dictionaryManagement.insertFromCommandline();
        dictionaryManagement.showAllWords();
    }

    public void dictionaryAdvanced() {
        DictionaryManagement dictionaryManagement = new DictionaryManagement();
        dictionaryManagement.insertFromFile();
        dictionaryManagement.showAllWords();
        dictionaryManagement.dictionaryLookup();
    }
}
