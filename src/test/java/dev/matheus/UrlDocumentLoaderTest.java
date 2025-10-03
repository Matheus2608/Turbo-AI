package dev.matheus;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.transformer.jsoup.HtmlToTextDocumentTransformer;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UrlDocumentLoaderTest implements WithAssertions {

    private static final List<String> urls = List.of(
            "https://portswigger.net/research/turbo-intruder-embracing-the-billion-request-attack",
            "https://portswigger.net/research/cracking-recaptcha-turbo-intruder-style",
            "https://portswigger.net/research/the-single-packet-attack-making-remote-race-conditions-local",
            "https://portswigger.net/research/smashing-the-state-machine"
    );

    private HtmlToTextDocumentTransformer extractor = new HtmlToTextDocumentTransformer();

    @Test
    void url() {
        String url = urls.getFirst();

        Document document = UrlDocumentLoader.load(url, new TextDocumentParser());
//        System.out.println(document);
        System.out.println(extractor.transform(document));
    }
}
