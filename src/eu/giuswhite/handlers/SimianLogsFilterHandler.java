package eu.giuswhite.handlers;

import eu.giuswhite.beans.SimianLog;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

/**
 * Created by GiusWhite on 15/03/2016.
 */
public class SimianLogsFilterHandler extends DefaultHandler {
    private List<SimianLog> usefulSimianLog;
    private SimianLog simianLog;
    private SimianLog.LogFragment logFragment;
    private boolean id;
    private boolean file;
    private boolean start;
    private boolean end;

    public SimianLogsFilterHandler(List<SimianLog> list) {
        super();
        this.usefulSimianLog = list;
    }

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
        if (qName.equals("CloneClass")) {
            this.simianLog = new SimianLog();
        }
        if (qName.equals("ID")) {
            this.id = true;
            this.file = false;
            this.end = false;
            this.start = false;
        }
        if (qName.equals("Fragment")) {
            this.logFragment = this.simianLog.getEmptyFragment();
        }
        if (qName.equals("File")) {
            this.id = false;
            this.file = true;
            this.end = false;
            this.start = false;
        }
        if (qName.equals("End")) {
            this.id = false;
            this.file = false;
            this.end = true;
            this.start = false;
        }
        if (qName.equals("Start")) {
            this.id = false;
            this.file = false;
            this.end = false;
            this.start = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length).trim();
        if (value.length() == 0) {
            return; // ignore white space
        }
        if (this.id) {
            this.simianLog.id = Integer.parseInt(value);
        }
        if (this.file) {
            this.logFragment.setFilePath(value);
        }
        if (this.start) {
            this.logFragment.setStart(Integer.parseInt(value));
        }
        if (this.end) {
            this.logFragment.setEnd(Integer.parseInt(value));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("CloneClass")) {
            if(this.simianLog.isUseful()){
                this.usefulSimianLog.add(this.simianLog);
            } else {
                System.out.println("SCARTATO: " + this.simianLog.id);
            }
        }
    }
}
