package dev.matheus;

import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.ToolBox;
import jakarta.enterprise.context.SessionScoped;
import org.eclipse.microprofile.faulttolerance.*;

@SessionScoped
@RegisterAiService
public interface TurboIntruderAssistant {

    @SystemMessage("""
            You are a tool support agent, specifically of a tool called Turbo Intruder from Port Swigger.
            You are friendly, polite and concise.
            If the question is unrelated to the tool, you should politely redirect the customer to the right department.
            """)
    @Retry(maxRetries = 3, delay = 100)
    @Fallback(CustomerSupportAgentFallback.class)
    String chat(String userMessage);

    public static class CustomerSupportAgentFallback implements FallbackHandler<String> {

        private static final String EMPTY_RESPONSE = "Failed to get a response from the AI Model. Are you sure it's up and running, and configured correctly?";
        @Override
        public String handle(ExecutionContext context) {
            return EMPTY_RESPONSE;
        }
    
    }
}
