package eu.giuswhite.utils;

import eu.giuswhite.beans.SimianLog;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


/**
 * Created by GiusWhite on 16/03/2016.
 */
public class XmlFileWriter {

    public static void writeSimianUsefulFragmentsToXml(String filename, List<SimianLog> content) {
        try {
            Document doc = new Document();
            Element root = new Element("FRAGMENTS");
            for (SimianLog simianLog : content) {
                Element sL = new Element("SIMIAN_LOG");
                sL.setAttribute("ID", String.valueOf(simianLog.id));

                for (SimianLog.LogFragment logFragment : simianLog.fragmentList) {
                    Element lF = new Element("FRAGMENT_LOG");
                    lF.setAttribute("filePath", logFragment.filePath);
                    lF.setAttribute("start", String.valueOf(logFragment.start));
                    lF.setAttribute("end", String.valueOf(logFragment.end));
                    sL.addContent(lF);
                }
                root.addContent(sL);
            }

            doc.setRootElement(root);
            XMLOutputter outter = new XMLOutputter();
            outter.setFormat(Format.getPrettyFormat());
            outter.output(doc, new FileWriter(new File(CommonUtils.PROJECT_FOLDER_PATH + filename + ".xml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
