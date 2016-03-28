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
public class ParserHandler extends DefaultHandler {
    private StackOverflowPost stackOverflowPost = new StackOverflowPost();
    private List<String> javaAnswersId = new ArrayList<>();
    private ParserManager parserManager = ParserManager.getInstance();


    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //Crea elemento StackOverflowPost
        if (qName.equals("row")) {
            stackOverflowPost.setId(Integer.parseInt(attributes.getValue("Id")));
            stackOverflowPost.setPostType(Integer.parseInt(attributes.getValue("PostTypeId")));
            stackOverflowPost.setTags(attributes.getValue("Tags"));
            String acceptedAnswerId = attributes.getValue("AcceptedAnswerId");
            if (acceptedAnswerId != null) {
                stackOverflowPost.setAcceptedAnswerId(Integer.parseInt(attributes.getValue("AcceptedAnswerId")));
            } else {
                stackOverflowPost.setAcceptedAnswerId(-1);
            }
            stackOverflowPost.setBody(attributes.getValue("Body"));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        //this.getAllAnswers();
        this.getOnlyJavaQuestions();
        ParserManager.NUMBER_OF_POSTS++;
        if (CommonUtils.TEST_MODE) {
            if (ParserManager.NUMBER_OF_POSTS >= CommonUtils.STOP_PROCESS_FLAG) {
                throw new SaxTerminatorException();
            }
        }
    }

    private void getOnlyJavaQuestions() {
        if (this.stackOverflowPost.getPostType() == 1) {
            ParserManager.NUMBER_OF_QUESTIONS++;
            if (this.stackOverflowPost.getAcceptedAnswerId() != -1) {
                ParserManager.NUMBER_OF_QUESTIONS_WITH_ANSWERS++;
                if (this.stackOverflowPost.isJavaQuestion()) {
                    ParserManager.NUMBER_OF_JAVA_QUESTIONS++;
                    ParserManager.NUMBER_OF_JAVA_QUESTIONS_WITH_ANSWERS++;
                    this.javaAnswersId.add(String.valueOf(this.stackOverflowPost.getAcceptedAnswerId()));
                    System.out.println("ADDED: " + this.stackOverflowPost);
                }
            } else {
                if (this.stackOverflowPost.isJavaQuestion()) {
                    ParserManager.NUMBER_OF_JAVA_QUESTIONS++;
                }
            }
        } else if (this.stackOverflowPost.getPostType() == 2) {
            ParserManager.NUMBER_OF_ANSWERS++;
            if (this.javaAnswersId.contains(String.valueOf(this.stackOverflowPost.getId()))) {
                ParserManager.NUMBER_OF_JAVA_ANSWERS++;
                this.getCodeFromAnswers();
                this.javaAnswersId.remove(String.valueOf(this.stackOverflowPost.getId()));
                System.out.println("FOUND AND REMOVED: " + this.stackOverflowPost.toString());
            }
        }
    }

    private void getCodeFromAnswers() {
        if (this.stackOverflowPost.getCode().size() > 0) {
            for (int i = 0; i < this.stackOverflowPost.getCode().size(); i++) {
                int numberOfLines = LineCounter.getInstance().getNumberOfLines(this.stackOverflowPost.getCode().get(i));
                ParserManager.TOTAL_LINE_NUMBER += numberOfLines;
                //Folder Splitted
                //String path = CommonUtils.EXTRACTED_DATA_FOLDER_PATH + numberOfLines + "\\";
                String path = CommonUtils.EXTRACTED_DATA_FOLDER_PATH;
                FileManager.getInstance().createAndWriteFile(path, this.stackOverflowPost.getId() + "_" + i + ".java", this.stackOverflowPost.getCode().get(i), false);
            }
        }
    }
}

