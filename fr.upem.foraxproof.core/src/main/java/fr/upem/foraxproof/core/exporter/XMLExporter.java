package fr.upem.foraxproof.core.exporter;

import fr.upem.foraxproof.core.analysis.Record;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * XMLExporter is exporting records to XML file called result.xml at the
 * root of the working directory.
 */
public final class XMLExporter implements Exporter {
    private final FileOutputStream fileOutputStream;
    private final XMLStreamWriter xmlStreamWriter;

    private XMLExporter(FileOutputStream fileOutputStream, XMLStreamWriter xmlStreamWriter) {
        this.fileOutputStream = fileOutputStream;
        this.xmlStreamWriter = xmlStreamWriter;
    }

    /**
     * Factory method that creates a new XMLExporter with a
     * brunch of default parameters.
     * @return the XMLExporter created.
     */
    public static XMLExporter build() throws FileNotFoundException, XMLStreamException {
        Path output = Paths.get("result.xml");
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        FileOutputStream fileOutputStream = new FileOutputStream(output.toFile());
        XMLStreamWriter xmlStreamWriter = outputFactory.createXMLStreamWriter(fileOutputStream);
        XMLExporter xmlExporter = new XMLExporter(fileOutputStream, xmlStreamWriter);
        xmlExporter.beginXMLDocument();

        return xmlExporter;
    }

    @Override
    public void insertRecord(Record record) {
        try {
            xmlStreamWriter.writeStartElement("record");
            writeRecord(record);
            xmlStreamWriter.writeEndElement();
        } catch (XMLStreamException e) {
            throw new UncheckedExporterException(e);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            closeXMLDocument();
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        } finally {
            fileOutputStream.close();
        }
    }

    private void beginXMLDocument() throws XMLStreamException {
        xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
        xmlStreamWriter.writeStartElement("records");
    }

    private void closeXMLDocument() throws XMLStreamException {
        xmlStreamWriter.writeEndElement(); //Close <records>
        xmlStreamWriter.writeEndDocument();
        xmlStreamWriter.flush();
        xmlStreamWriter.close();
    }

    private void writeRecord(Record record) throws XMLStreamException {
        xmlStreamWriter.writeAttribute("context", record.getLocation().getSource());
        xmlStreamWriter.writeAttribute("rule", record.getRule());
        xmlStreamWriter.writeAttribute("level", record.getLevel().name());
        xmlStreamWriter.writeAttribute("message", record.getMsg());
    }
}
