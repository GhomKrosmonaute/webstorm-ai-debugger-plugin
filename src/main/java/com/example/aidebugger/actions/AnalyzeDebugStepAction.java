package com.example.aidebugger.actions;

import com.example.aidebugger.services.OpenAIService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;
import com.intellij.xdebugger.frame.XStackFrame;
import org.jetbrains.annotations.NotNull;

public class AnalyzeDebugStepAction extends AnAction {
    private StringBuilder debugHistory = new StringBuilder();

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        XDebugSession debugSession = XDebuggerManager.getInstance(e.getProject())
            .getCurrentSession();
        
        if (debugSession == null) {
            Messages.showWarningDialog(
                "No active debug session found",
                "AI Debugger"
            );
            return;
        }

        XStackFrame currentFrame = debugSession.getCurrentStackFrame();
        if (currentFrame == null) {
            Messages.showWarningDialog(
                "No stack frame available",
                "AI Debugger"
            );
            return;
        }

        String debugContext = buildDebugContext(currentFrame);
        debugHistory.append(debugContext).append("\n---\n");

        OpenAIService aiService = ServiceManager.getService(OpenAIService.class);
        String analysis = aiService.analyzeDebugState(debugHistory.toString());

        Messages.showInfoMessage(
            analysis,
            "AI Debug Analysis"
        );
    }

    private String buildDebugContext(XStackFrame frame) {
        // Collect current debug information
        StringBuilder context = new StringBuilder();
        context.append("Current File: ").append(frame.getSourcePosition().getFile().getName())
               .append("\nLine: ").append(frame.getSourcePosition().getLine())
               .append("\nVariables:\n");

        // Add variables, call stack, and other relevant debug information
        // This would need to be implemented based on the specific debug API
        
        return context.toString();
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        XDebugSession debugSession = XDebuggerManager.getInstance(e.getProject())
            .getCurrentSession();
        e.getPresentation().setEnabled(debugSession != null);
    }
}