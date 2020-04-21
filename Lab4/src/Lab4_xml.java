import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
Class that reads in and parse the activity file in XML format
 */

public class Lab4_xml {

    private ArrayList<BorrowedBy> borrowedByArrayList = new ArrayList<BorrowedBy>();
    private static final DateFormat XML_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    private static final DateFormat SQL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String NOT_AVAILABLE_VALUE = "N/A";

    public void readXML(String fileName)
    {
        try {
            File file = new File(fileName);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeLst = doc.getElementsByTagName("Borrowed_by");

            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node fstNode = nodeLst.item(s);

                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element sectionNode = (Element) fstNode;

                    String isbn;
                    int member_id = -1;
                    String checkout_date;
                    String checkin_date;

                    NodeList memberIdElementList = sectionNode.getElementsByTagName("MemberID");
                    Element memberIdElmnt = (Element) memberIdElementList.item(0);
                    NodeList memberIdNodeList = memberIdElmnt.getChildNodes();
                    //System.out.println("MemberID : "  + ((Node) memberIdNodeList.item(0)).getNodeValue().trim());
                    member_id = Integer.parseInt(((Node) memberIdNodeList.item(0)).getNodeValue().trim());

                    NodeList secnoElementList = sectionNode.getElementsByTagName("ISBN");
                    Element secnoElmnt = (Element) secnoElementList.item(0);
                    NodeList secno = secnoElmnt.getChildNodes();
                    //System.out.println("ISBN : "  + ((Node) secno.item(0)).getNodeValue().trim());
                    isbn = ((Node) secno.item(0)).getNodeValue().trim().replace("-", "");

                    NodeList codateElementList = sectionNode.getElementsByTagName("Checkout_date");
                    Element codElmnt = (Element) codateElementList.item(0);
                    NodeList cod = codElmnt.getChildNodes();
                    //System.out.println("Checkout_date : "  + ((Node) cod.item(0)).getNodeValue().trim());
                    String xml_checkout_date = ((Node) cod.item(0)).getNodeValue().trim();
                    //System.out.println("Checkout_date : "  + xml_checkout_date);

                    if (!xml_checkout_date.equals(NOT_AVAILABLE_VALUE)) {
                        Date date_checkout = XML_DATE_FORMAT.parse(xml_checkout_date);
                        checkout_date = SQL_DATE_FORMAT.format(date_checkout);
                    } else {
                        checkout_date = xml_checkout_date;
                    }

                    NodeList cidateElementList = sectionNode.getElementsByTagName("Checkin_date");
                    Element cidElmnt = (Element) cidateElementList.item(0);
                    NodeList cid = cidElmnt.getChildNodes();
                    //System.out.println("Checkin_date : "  + ((Node) cid.item(0)).getNodeValue().trim());
                    String xml_checkin_date = ((Node) cid.item(0)).getNodeValue().trim();
                    //System.out.println("Checkin_date : "  + xml_checkin_date);

                    if (!xml_checkin_date.equals(NOT_AVAILABLE_VALUE)) {
                        Date date_checkin = XML_DATE_FORMAT.parse(xml_checkin_date);
                        checkin_date = SQL_DATE_FORMAT.format(date_checkin);
                    } else {
                        checkin_date = xml_checkin_date;
                    }

                    if (!isbn.isEmpty() && member_id > -1 && !checkout_date.isEmpty() && !checkin_date.isEmpty()) {
                        BorrowedBy borrowedBy = new BorrowedBy(isbn, member_id, checkout_date, checkin_date);
                        borrowedByArrayList.add(borrowedBy);
                    }

                    System.out.println();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<BorrowedBy> getBorrowedByArrayList() {
        return this.borrowedByArrayList;
    }
}//end class
