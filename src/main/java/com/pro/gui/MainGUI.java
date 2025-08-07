/*
 * Created by Ahmed Khaled
 * This is the main GUI class for the RAG system
 * Built using Java Swing
 */

package com.pro.gui;

// Import necessary services and libraries
import com.pro.services.EmbeddingService;
import com.pro.services.LLMService;
import com.pro.services.MilvusFileIndexer;
import com.pro.services.MilvusService;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Main graphical user interface class for the RAG assistant.
 * Allows user to input questions, optionally index documents, choose a model, and view answers.
 */
public class MainGUI extends JFrame {

    // UI components declaration
    private JTextArea questionArea;
    private JTextArea answerArea;
    private JComboBox<String> modelBox;
    private JCheckBox indexCheck;
    private JButton askButton;

    // Constructor: Builds the GUI layout and initializes all components
    public MainGUI() {
        setTitle("Namasoft AI Assistant");  // Window title
        setSize(800, 600);  // Window size
        setLocationRelativeTo(null);  // Center window on screen
        setDefaultCloseOperation(EXIT_ON_CLOSE);  // Exit when closed

        // ========== Main Panel Configuration ==========
        JPanel panel = new JPanel(new GridBagLayout());  // Using GridBagLayout for flexibility
        panel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Padding

        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font textFont = new Font("Segoe UI", Font.PLAIN, 14);

        // ========== Question Label ==========
        JLabel questionLabel = new JLabel("📝 Your Question:");
        questionLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(questionLabel, gbc);

        // ========== Question Text Area ==========
        questionArea = new JTextArea(4, 50);
        questionArea.setFont(textFont);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        JScrollPane questionScroll = new JScrollPane(questionArea);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(questionScroll, gbc);

        // ========== Options Panel (Checkbox + Model Selector + Ask Button) ==========
        JPanel optionsPanel = new JPanel();
        optionsPanel.setBackground(new Color(245, 245, 245));
        indexCheck = new JCheckBox("📂 Index file");  // Option to re-index documents
        modelBox = new JComboBox<>(new String[]{"llama2", "llama3"});  // Model selection
        askButton = new JButton("🔍 Ask");  // Ask button
        askButton.setBackground(new Color(33, 150, 243));
        askButton.setForeground(Color.white);
        askButton.setFocusPainted(false);
        askButton.setFont(new Font("Segoe UI", Font.BOLD, 14));

        optionsPanel.add(indexCheck);
        optionsPanel.add(new JLabel("🤖 Model:"));
        optionsPanel.add(modelBox);
        optionsPanel.add(askButton);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(optionsPanel, gbc);

        // ========== Answer Label ==========
        JLabel answerLabel = new JLabel("💡 Answer:");
        answerLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(answerLabel, gbc);

        // ========== Answer Text Area ==========
        answerArea = new JTextArea(10, 50);
        answerArea.setFont(textFont);
        answerArea.setLineWrap(true);
        answerArea.setWrapStyleWord(true);
        answerArea.setEditable(false);
        answerArea.setBackground(new Color(250, 250, 250));
        JScrollPane answerScroll = new JScrollPane(answerArea);
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(answerScroll, gbc);

        // Add the fully built panel to the window
        add(panel);

        // Set up button click event
        askButton.addActionListener(this::handleAsk);
    }

    /**
     * This method is called when the user clicks the Ask button.
     * It handles indexing (if selected), generates embeddings, retrieves matches, builds the final prompt,
     * and sends it to the LLM.
     */
    private void handleAsk(ActionEvent e) {
        try {
            String question = questionArea.getText().trim();
            String modelChoice = (String) modelBox.getSelectedItem();
            boolean shouldIndex = indexCheck.isSelected();

            // Initialize services
            EmbeddingService embeddingService = new EmbeddingService();
            MilvusService milvusService = new MilvusService();

            // If checkbox is selected, index the document
            if (shouldIndex) {
                MilvusFileIndexer indexer = new MilvusFileIndexer(
                        embeddingService.getModel(),
                        milvusService.getStore()
                );
                indexer.indexFile("Namasoft_Company_Info.txt");  // You can change this to any file
            }

            // Embed the question and search for related segments in the database
            Embedding queryEmbedding = embeddingService.embed(question);
            List<EmbeddingMatch<TextSegment>> matches = milvusService.search(queryEmbedding, 3);

            // Combine the matched segments into a context for the LLM
            StringBuilder contextBuilder = new StringBuilder();
            for (EmbeddingMatch<TextSegment> match : matches) {
                contextBuilder.append(match.embedded().text()).append("\n");
            }

            // Final prompt sent to the language model
            String finalPrompt = "Answer the following question based on the context :\n\n"
                    + "Only use information from the context. If the answer is not present, say \"I don't know." + "Context"
                    + contextBuilder.toString() + "\n\n"
                    + "Question: " + question + "\n"
                    + "Answer:";

            // Use selected LLM model to get the answer
            LLMService llmService = new LLMService(modelChoice);
            String response = llmService.ask(finalPrompt);

            // Display the answer in the text area
            answerArea.setText(response);

        } catch (Exception ex) {
            // Display any error messages in the answer area
            answerArea.setText("⚠️ Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Entry point to run the GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI().setVisible(true));
    }
}
