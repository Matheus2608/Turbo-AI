package dev.matheus;

import static org.assertj.core.api.Assertions.assertThat;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.loader.github.GitHubDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "GITHUB_TOKEN", matches = ".+")
public class GitHubDocumentLoaderTest {

    private static final String TEST_OWNER = "PortSwigger";
    private static final String TEST_REPO = "turbo-intruder";

    GitHubDocumentLoader loader;

    DocumentParser parser = new TextDocumentParser();

    @BeforeEach
    void beforeEach() {
        loader = GitHubDocumentLoader.builder()
                .gitHubToken(System.getenv("GITHUB_TOKEN"))
                .build();
    }

    @Test
    void should_load_file() {
        List<Document> documents = loader.loadDocuments(TEST_OWNER, TEST_REPO, "master", "resources/examples", parser);
        documents.forEach(doc -> doc.metadata().put("type", "script_examples"));
        System.out.println(documents);
        assertThat(documents)
                .extracting(Document::metadata)
                .as("metadata")
                .allMatch(met -> met.containsKey("type"), "Todos os metadados devem conter a chave 'type'");
    }
}