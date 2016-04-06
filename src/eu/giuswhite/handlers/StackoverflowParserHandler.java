package eu.giuswhite.handlers;

import eu.giuswhite.LineCounter;
import eu.giuswhite.ParserManager;
import eu.giuswhite.beans.StackOverflowPost;
import eu.giuswhite.exceptions.SaxTerminatorException;
import eu.giuswhite.utils.CommonUtils;
import eu.giuswhite.utils.FileManager;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GiusWhite on 20/01/2016.
 */
public class StackoverflowParserHandler extends DefaultHandler {
    private StackOverflowPost stackOverflowPost = new StackOverflowPost();
    private List<String> javaAnswersId = new ArrayList<>();

    public static int NUMBER_OF_POSTS = 0;
    private  int NUMBER_OF_QUESTIONS = 0;
    private  int NUMBER_OF_ANSWERS = 0;
    private  int NUMBER_OF_JAVA_QUESTIONS = 0;
    private  int NUMBER_OF_JAVA_ANSWERS = 0;
    private  int NUMBER_OF_JAVA_QUESTIONS_WITH_ANSWERS = 0;
    private  int NUMBER_OF_QUESTIONS_WITH_ANSWERS = 0;
    public static int TOTAL_LINE_NUMBER = 0;

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        FileManager.getInstance().createAndWriteFile("./","stackoverflow_posts_statistics.txt", this.createStatisticString(), false);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //Crea elemento StackOverflowPost
        if (qName.equals("row")) {
            this.stackOverflowPost.setId(Integer.parseInt(attributes.getValue("Id")));
            this.stackOverflowPost.setPostType(Integer.parseInt(attributes.getValue("PostTypeId")));
            this.stackOverflowPost.setTags(attributes.getValue("Tags"));
            String acceptedAnswerId = attributes.getValue("AcceptedAnswerId");
            if (acceptedAnswerId != null) {
                this.stackOverflowPost.setAcceptedAnswerId(Integer.parseInt(attributes.getValue("AcceptedAnswerId")));
            } else {
                this.stackOverflowPost.setAcceptedAnswerId(-1);
            }
            this.stackOverflowPost.setBody(attributes.getValue("Body"));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        this.getOnlyJavaQuestions();
        StackoverflowParserHandler.NUMBER_OF_POSTS++;
        if (CommonUtils.TEST_MODE) {
            if (StackoverflowParserHandler.NUMBER_OF_POSTS >= CommonUtils.STOP_PROCESS_FLAG) {
                throw new SaxTerminatorException();
            }
        }
    }

    private void getOnlyJavaQuestions() {
        if (this.stackOverflowPost.getPostType() == 1) {
            this.NUMBER_OF_QUESTIONS++;
            if (this.stackOverflowPost.getAcceptedAnswerId() != -1) {
                this.NUMBER_OF_QUESTIONS_WITH_ANSWERS++;
                if (this.stackOverflowPost.isJavaQuestion()) {
                    this.NUMBER_OF_JAVA_QUESTIONS++;
                    this.NUMBER_OF_JAVA_QUESTIONS_WITH_ANSWERS++;
                    this.javaAnswersId.add(String.valueOf(this.stackOverflowPost.getAcceptedAnswerId()));
                }
            } else {
                if (this.stackOverflowPost.isJavaQuestion()) {
                    this.NUMBER_OF_JAVA_QUESTIONS++;
                }
            }
        } else if (this.stackOverflowPost.getPostType() == 2) {
            this.NUMBER_OF_ANSWERS++;
            if (this.javaAnswersId.contains(String.valueOf(this.stackOverflowPost.getId()))) {
                this.NUMBER_OF_JAVA_ANSWERS++;
                this.getCodeFromAnswers();
                this.javaAnswersId.remove(String.valueOf(this.stackOverflowPost.getId()));
            }
        }
    }

    private void getCodeFromAnswers() {
        if (this.stackOverflowPost.getCode().size() > 0) {
            for (int i = 0; i < this.stackOverflowPost.getCode().size(); i++) {
                int numberOfLines = LineCounter.getInstance().getNumberOfLines(this.stackOverflowPost.getCode().get(i));
                StackoverflowParserHandler.TOTAL_LINE_NUMBER += numberOfLines;
                String path = "./extracted_data/";
                FileManager.getInstance().createAndWriteFile(path, this.stackOverflowPost.getId() + "_" + i + ".java", this.stackOverflowPost.getCode().get(i), false);
            }
        }
    }

    private String createStatisticString() {
        String result = "";
        result += "NUMBER_OF_POSTS: " + NUMBER_OF_POSTS + "<br>";
        result += "NUMBER_OF_QUESTIONS: " + NUMBER_OF_QUESTIONS + "<br>";
        result += "NUMBER_OF_ANSWERS: " + NUMBER_OF_ANSWERS + "<br>";
        result += "NUMBER_OF_JAVA_QUESTIONS: " + NUMBER_OF_JAVA_QUESTIONS + "<br>";
        result += "NUMBER_OF_QUESTIONS_WITH_ANSWERS: " + NUMBER_OF_QUESTIONS_WITH_ANSWERS + "<br>";
        result += "NUMBER_OF_JAVA_QUESTIONS_WITH_ANSWERS: " + NUMBER_OF_JAVA_QUESTIONS_WITH_ANSWERS + "<br>";
        result += "NUMBER_OF_JAVA_ANSWERS: " + NUMBER_OF_JAVA_ANSWERS + "<br>";
        result += "TOTAL_NUMBER_OF_CODELINE: " + TOTAL_LINE_NUMBER;
        return result;
    }
}

