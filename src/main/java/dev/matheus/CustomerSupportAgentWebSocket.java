package dev.matheus;

import io.quarkiverse.langchain4j.runtime.aiservice.GuardrailException;
import io.quarkus.logging.Log;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;

@WebSocket(path = "/customer-support-agent")
public class CustomerSupportAgentWebSocket {

    private final TurboIntruderAssistant turboIntruderAssistant;

    public CustomerSupportAgentWebSocket(TurboIntruderAssistant turboIntruderAssistant) {
        this.turboIntruderAssistant = turboIntruderAssistant;
    }

    @OnOpen
    public String onOpen() {
        return "Welcome to Turbo AI Intruder! How can I help you today?";
    }

    @OnTextMessage
    public String onTextMessage(String message) {
        try {
            return turboIntruderAssistant.chat(message);
        } catch (GuardrailException e) {
            Log.errorf(e, "Error calling the LLM: %s", e.getMessage());
            return "Sorry, I am unable to process your request at the moment. It's not something I'm allowed to do.";
        }
    }
}
