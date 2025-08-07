package com.pro.services;

// Import necessary classes
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;

import java.util.List;

// Service class responsible for interacting with Milvus vector database
public class MilvusService {

    // Store object that handles storing and searching text embeddings
    private final EmbeddingStore<TextSegment> store;

    // Constructor: initializes the connection to the Milvus database
    public MilvusService() {
        this.store = MilvusEmbeddingStore.builder()
                .uri("http://localhost:19530")     // Local URI where Milvus is running
                .collectionName("namasoft_base")
                .dimension(384)                    // Dimension of the embedding vectors
                .build();
    }

    // Search for similar embeddings in Milvus given a query embedding and result limit
    public List<EmbeddingMatch<TextSegment>> search(Embedding embedding, int maxResults) {
        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(embedding)
                .maxResults(maxResults)
                .build();

        return store.search(request).matches();    // Execute search and return matching segments
    }

    // Getter method to access the embedding store externally if needed
    public EmbeddingStore<TextSegment> getStore() {
        return store;
    }
}