package dev.matheus;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.loader.github.GitHubDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.transformer.jsoup.HtmlToTextDocumentTransformer;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RagIngestion2 {

    private static final String REPOSITORY_OWNER = "PortSwigger";
    private static final String REPOSITORY = "turbo-intruder";
    private static final String RELATIVE_PATH_FOLDER = "resources/examples";
    private static final String BRANCH = "master";

    private static final List<String> urls = List.of(
            "https://portswigger.net/research/turbo-intruder-embracing-the-billion-request-attack",
            "https://portswigger.net/research/cracking-recaptcha-turbo-intruder-style",
            "https://portswigger.net/research/the-single-packet-attack-making-remote-race-conditions-local",
            "https://portswigger.net/research/smashing-the-state-machine"
    );

    @Produces
    @ApplicationScoped
    public List<Document> docs() {
        List<Document> res = new ArrayList<>();
        res.addAll(htmlDocs());
        res.addAll(pdfDocument());
        res.addAll(githubDocuments());
        Log.info("docs done");
        return res;
    }

    private List<Document> htmlDocs() {
        Log.info("Loading htmls");

        HtmlToTextDocumentTransformer extractor = new HtmlToTextDocumentTransformer();
        TextDocumentParser textDocumentParser = new TextDocumentParser();
        return urls.stream()
                .map(url -> UrlDocumentLoader.load(url, textDocumentParser))
                .map(extractor::transform)
                .toList();
    }

    private List<Document> pdfDocument() {
        Log.info("Loading pdf");

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("rag/racewhitepaper.pdf")) {
            DocumentParser parser = new ApachePdfBoxDocumentParser(true);
            Document document = parser.parse(inputStream);
            List<Document> res = new ArrayList<>();
            res.add(document);
            return res;
        } catch (IOException e) {
            Log.error("ex: ", e);
            return Collections.emptyList();
        }
    }

    private List<Document> githubDocuments() {
        GitHubDocumentLoader loader = GitHubDocumentLoader.builder()
                .gitHubToken(System.getenv("GITHUB_TOKEN"))
                .build();
        Log.info("Loading folder ");
//                "{} from {}",  RELATIVE_PATH_FOLDER, REPOSITORY_OWNER + "/" + REPOSITORY + ":" + BRANCH);
        List<Document> documents = loader.loadDocuments(REPOSITORY_OWNER, REPOSITORY, BRANCH, RELATIVE_PATH_FOLDER, new TextDocumentParser());
        documents.forEach(doc -> doc.metadata().put("type", "script_example"));
       //  System.out.println(documents);
        return documents;
    }
}
