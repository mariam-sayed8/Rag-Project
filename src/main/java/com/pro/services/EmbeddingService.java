package com.pro.services;

// Import classes related to embeddings and the Ollama embedding model
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;

// Service class responsible for managing and generating text embeddings
public class EmbeddingService {

    // The embedding model used to convert text into vector representations
    private final EmbeddingModel embeddingModel;

    // Constructor: initializes the embedding model with Ollama backend
    public EmbeddingService() {
        this.embeddingModel = OllamaEmbeddingModel.builder()
                .baseUrl("http://localhost:11434")      // Local server where Ollama is running
                .modelName("all-minilm")
                .build();
    }

    // Converts a plain text string into its corresponding embedding vector
    public Embedding embed(String text) {
        return embeddingModel.embed(text).content();    // Returns the embedding content
    }

    // Getter method to access the underlying embedding model (if needed externally)
    public EmbeddingModel getModel() {
        return embeddingModel;
    }
}