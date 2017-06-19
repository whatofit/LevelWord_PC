package level;

import java.util.List;

import model.XmlSent;
import model.XmlWord;

import com.ormlitedao.bean.Word;

public class Xml2Sqlite1Line extends XmlWordIntoSqlite {

    public Xml2Sqlite1Line() {
        super();
    }

    // 所有词性/词义在一行
    public void word2Vector(String line) {
        XmlWord xmlWord = wordParser.getXmlWord(line);

        Word dbWord = new Word();
        dbWord.setId(vecWords.size());
        dbWord.setFrequency(xmlWord.getWordFrequency());
        dbWord.setSpelling(xmlWord.getKey());
        dbWord.setPhoneticDJ(xmlWord.getPs());
        dbWord.setPhoneticKK(xmlWord.getPs2());
        dbWord.setLevel(null);
        List<String> partsOfSpeech = xmlWord.getPartsOfSpeech();
        List<String> meaning = xmlWord.getMeaning();
        String posMeanings = "";
        for (int i = 0; i < partsOfSpeech.size(); i++) {
            String pos = partsOfSpeech.get(i);
            String mean = meaning.get(i);
            if (!posMeanings.isEmpty()) {
                // posMeanings = posMeanings + "||";
            }
            posMeanings = posMeanings + pos + mean;
        }
        dbWord.setPartsOfSpeech(posMeanings);
        dbWord.setMeanings(null);

        List<XmlSent> sents = xmlWord.getSents();
        String sentences = "";
        for (int i = 0; i < sents.size(); i++) {
            XmlSent sent = sents.get(i);
            if (!sentences.isEmpty()) {
                // sentence = sentence + "||";
            }
            sentences = sentences + (i + 1) + ". " + sent.getOrig() + "/"
                    + sent.getTrans() + " ";
        }
        dbWord.setSentences(sentences);
        vecWords.add(dbWord);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            Xml2Sqlite1Line levelSqlite = new Xml2Sqlite1Line();
            levelSqlite.xmlFiles2Words();
            levelSqlite.doInsert2DB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
