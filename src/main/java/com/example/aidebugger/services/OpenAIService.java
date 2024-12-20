package com.example.aidebugger.services;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import org.jetbrains.annotations.NotNull;

@State(
    name = "AIDebuggerSettings",
    storages = {@Storage("aiDebuggerSettings.xml")}
)
public class OpenAIService implements PersistentStateComponent<OpenAIService.State> {
    private State myState = new State();

    public static class State {
        public String apiKey = "";
    }

    @Override
    public State getState() {
        return myState;
    }

    @Override
    public void loadState(@NotNull State state) {
        myState = state;
    }

    public String analyzeDebugState(String debugContext) {
        if (myState.apiKey.isEmpty()) {
            return "Please configure OpenAI API key in settings";
        }

        OpenAiService service = new OpenAiService(myState.apiKey);
        CompletionRequest request = CompletionRequest.builder()
            .model("gpt-4")
            .prompt(createPrompt(debugContext))
            .maxTokens(500)
            .temperature(0.7)
            .build();

        return service.createCompletion(request).getChoices().get(0).getText();
    }

    private String createPrompt(String debugContext) {
        return String.format("""
            Analyze the following debug context and provide:
            1. A summary of the current state
            2. Any potential issues or anomalies
            3. Recommendations for next steps
            
            Debug Context:
            %s
            """, debugContext);
    }
}