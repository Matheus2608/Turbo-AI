package dev.matheus;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import io.quarkus.logging.Log;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

public class ApachePdfBoxDocumentParserTest {

    // private static final String RELATIVE_PATH_PDF = "../../../../resources/rag/racewhitepaper.pdf";

    @Test
    void should_parse_pdf_file() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("rag/racewhitepaper.pdf")) {
            DocumentParser parser = new ApachePdfBoxDocumentParser(true);
            Document document = parser.parse(inputStream);
            System.out.println(document);
        } catch (IOException e) {
            Log.error("ex: ", e);
        }
    }
}
