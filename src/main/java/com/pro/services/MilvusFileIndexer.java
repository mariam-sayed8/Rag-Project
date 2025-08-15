package com.pro.services;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
  Service class responsible for reading text files,
  splitting them into smaller chunks, generating embeddings,
  and storing them in Milvus.
 */
public class MilvusFileIndexer {

    private static final Logger logger = Logger.getLogger(MilvusFileIndexer.class.getName());

    // Embedding model used to convert text chunks into vector embeddings
    private final EmbeddingModel embeddingModel;

    // Storage backend (Milvus) for embeddings and text segments
    private final EmbeddingStore<TextSegment> store;

    // Default chunk size (number of characters per chunk)
    private final int chunkSize = 500;

    // Overlap between consecutive chunks (characters)
    private final int chunkOverlap = 100;

    public MilvusFileIndexer(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> store) {
        this.embeddingModel = embeddingModel;
        this.store = store;
    }

    /**
      Reads the file, splits it into chunks, generates embeddings in batch, and stores them in Milvus.
     */
    public void indexFile(String filePath) throws IOException {
        // Read file content using UTF-8 to support Arabic and English
        String fullText = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);

        // Split the text into smaller chunks for better retrieval
        List<String> chunks = splitIntoChunks(fullText);

        // Convert chunks into TextSegments
        List<TextSegment> segments = new ArrayList<>();
        for (String chunk : chunks) {
            segments.add(TextSegment.from(chunk));
        }

        // Generate embeddings for all segments in one batch (faster)
        store.addAll(embeddingModel.embedAll(segments).content(), segments);

        logger.info("✔ File indexed successfully into Milvus. Total chunks: " + segments.size());
    }

    /**
      Splits a large text into overlapping chunks to preserve context.
     */
    private List<String> splitIntoChunks(String text) {
        List<String> chunks = new ArrayList<>();
        int start = 0;

        while (start < text.length()) {
            int end = Math.min(start + chunkSize, text.length());
            chunks.add(text.substring(start, end));
            start += (chunkSize - chunkOverlap);
        }

        return chunks;
    }
}
