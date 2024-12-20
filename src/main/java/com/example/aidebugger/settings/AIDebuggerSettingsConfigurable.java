package com.example.aidebugger.settings;

import com.example.aidebugger.services.OpenAIService;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class AIDebuggerSettingsConfigurable implements Configurable {
    private JPanel myPanel;
    private JTextField apiKeyField;
    private final OpenAIService aiService;

    public AIDebuggerSettingsConfigurable() {
        aiService = ServiceManager.getService(OpenAIService.class);
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "AI Debugger Settings";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        myPanel = new JPanel();
        apiKeyField = new JTextField(30);
        apiKeyField.setText(aiService.getState().apiKey);
        
        myPanel.add(new JLabel("OpenAI API Key:"));
        myPanel.add(apiKeyField);
        
        return myPanel;
    }

    @Override
    public boolean isModified() {
        return !apiKeyField.getText().equals(aiService.getState().apiKey);
    }

    @Override
    public void apply() {
        aiService.getState().apiKey = apiKeyField.getText();
    }
}