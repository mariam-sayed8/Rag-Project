package com.pro.services;

// Import LangChain4j classes for embedding and segment storage
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;

// Import Java standard libraries for file operations and collections
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// Service class responsible for reading text files, creating embeddings, and storing them in Milvus
public class MilvusFileIndexer {

    // The model used to convert text into embeddings (e.g., Ollama, OpenAI, etc.)
    private final EmbeddingModel embeddingModel;

    // The storage where embeddings and their original text segments will be saved
    private final EmbeddingStore<TextSegment> store;

    // Constructor: initializes the indexer with a specific embedding model and Milvus store
    public MilvusFileIndexer(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> store) {
        this.embeddingModel = embeddingModel;
        this.store = store;
    }

    // Reads a file, splits it into paragraphs, embeds them, and stores them in Milvus
    public void indexFile(String filePath) throws IOException {
        // Read the full content of the file into a single string
        String fullText = new String(Files.readAllBytes(Paths.get(filePath)));

        // Split the content into paragraphs based on double newlines
        String[] paragraphs = fullText.split("\\n\\s*\\n");

        // List to hold each segment created from paragraphs
        List<TextSegment> segments = new ArrayList<>();

        // Loop through each paragraph
        for (String paragraph : paragraphs) {
            String trimmed = paragraph.trim();  // Remove leading/trailing whitespace

            if (!trimmed.isEmpty()) {
                // Create a TextSegment object from the paragraph
                TextSegment segment = TextSegment.from(trimmed);

                // Generate an embedding for the segment and add it to the store
                store.add(embeddingModel.embed(segment).content(), segment);

                // Optionally store the segment in a local list
                segments.add(segment);
            }
        }

        // Log confirmation after indexing is complete
        System.out.println("✔ File indexed successfully into Milvus.");
    }
}