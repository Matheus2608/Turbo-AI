package dev.matheus;

import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.ToolBox;
import io.quarkiverse.langchain4j.mcp.runtime.McpToolBox;
import jakarta.enterprise.context.SessionScoped;
import org.eclipse.microprofile.faulttolerance.*;

@SessionScoped
@RegisterAiService
public interface TurboIntruderAssistant {

    @SystemMessage("""
            You are a tool support agent, specifically of a tool called Turbo Intruder from Port Swigger.
            You should use the tools of deepwiki for a deep understanding of how to construct a Turbo Intruder python script
            Only use the DeepWiki tools for allowed repository PortSwigger/turbo-intruder. Ex: "repoName":"PortSwigger/turbo-intruder/"
            You are friendly, polite and concise.
            If the question is unrelated to the tool, you should politely redirect the customer to the right department.
            """)
    @Retry(maxRetries = 1, delay = 100)
    @Fallback(CustomerSupportAgentFallback.class)
    @McpToolBox("deepwiki")
    String chat(String userMessage);

    public static class CustomerSupportAgentFallback implements FallbackHandler<String> {

        private static final String EMPTY_RESPONSE = "Failed to get a response from the AI Model. Are you sure it's up and running, and configured correctly?";
        @Override
        public String handle(ExecutionContext context) {
            return EMPTY_RESPONSE;
        }
    
    }
}
