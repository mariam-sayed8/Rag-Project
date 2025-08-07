```markdown
# 🤖 Namasoft AI Assistant

An interactive Java-based AI assistant that leverages **Retrieval-Augmented Generation (RAG)** to answer user questions based on documents from Namasoft. The system uses **LangChain4j**, **Milvus** for vector storage, and **LLaMA models** (via Ollama) to generate accurate and context-based answers.

---

## 🧠 Project Overview

This project allows users to:
- Upload or index company documents (e.g., `Namasoft_Company_Info.txt`).
- Ask natural language questions.
- Automatically retrieve the most relevant document segments.
- Generate accurate answers using a locally hosted LLaMA language model.
- Interact via a simple, styled GUI interface.

---

## 🚀 Features

- 🔍 **Semantic Search** using Embedding and Milvus vector database.
- 🧠 **LLM Integration** with LLaMA 2 or LLaMA 3 using Ollama.
- 📄 **Contextual Retrieval** from indexed company data.
- 🎨 **GUI Support** for clean user experience.
- ✅ Modular design with clean Java service architecture.

---

## 🛠️ Technologies Used

| Component       | Technology                     |
|----------------|---------------------------------|
| Language        | Java 17+                        |
| GUI             | Java Swing                      |
| Embedding       | LangChain4j Embedding APIs      |
| Vector DB       | Milvus                         |
| LLM             | LLaMA 2 / LLaMA 3 via Ollama    |
| Indexing        | MilvusFileIndexer               |
| Build Tool      | Maven or Gradle (your choice)   |

---

## 📂 Project Structure

```

com.pro/
│
├── gui/
│   ├── MainGUI.java              # GUI components (JFrame, Panels)
│
├── services/
│   ├── EmbeddingService.java     # Converts text to vector embeddings
│   ├── LLMService.java           # Connects to Ollama for LLaMA responses
│   ├── MilvusService.java        # Handles vector DB operations
│   └── MilvusFileIndexer.java    # Indexes files into Milvus

````

---

## 🧪 How to Run

### ✅ Prerequisites:

- Java 17+
- Ollama installed and running: [https://ollama.com/](https://ollama.com/)
- Milvus installed locally or via Docker
- LLaMA model pulled via Ollama:
  ```bash
  ollama pull llama3
````

---

### ▶️ Run the Project

1. Clone the repo:

   ```bash
   git clone https://github.com/your-username/namasoft-ai-assistant.git
   cd namasoft-ai-assistant
   ```

2. Build and run:

   ```bash
   ./mvnw clean install
   java -jar target/namasoft-ai-assistant.jar
   ```

3. Optionally launch GUI:

   ```bash
   java -cp target/classes com.pro.gui.MainGUI
   ```

---

## 💬 Example Workflow

1. User uploads or indexes a text file about Namasoft.
2. User enters a natural question like:

   > *"What ERP modules does Namasoft provide?"*
3. The system:

   * Embeds the question.
   * Retrieves top relevant document segments from Milvus.
   * Builds a prompt for LLaMA.
   * Displays the AI-generated answer in the console or GUI.

---

## 📌 Future Improvements

* Add support for PDF/Word document indexing.
* Add voice input using Java Speech API.
* Connect GUI directly with file upload and LLM options.
* Deploy as a desktop app using JavaFX.

---

## 👩‍💻 Developed By

* **Mariam Mohamed Sayed**
* **Karima Mahmoud**
* **Ahmed Khaled**

---

## 📜 License

This project is open for educational purposes only.

```
