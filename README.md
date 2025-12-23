# GitHub User Activity CLI

https://roadmap.sh/projects/github-user-activity

A command-line tool (CLI) in Java to view GitHub user activities directly from the terminal.

## 🚀 Features

- Displays recent activities of any GitHub user
- Supports different event types (pushes, issues, pull requests, etc.)
- Simple and intuitive command-line interface
- Clear English messages
- Robust error handling

## 📋 Prerequisites

- Java 11 or higher
- Maven (optional but recommended)
- Internet access to consume GitHub API

## 🛠️ Installation

### Using Maven (Recommended)

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/github-user-activity.git
   cd github-user-activity
   ```

2. Build the project:
   ```bash
   mvn clean package
   ```

### Using Java Only

1. Download the Jakarta JSON dependency (version 2.0.1 or higher)
2. Place the JAR file in the `lib/` directory of the project
3. Compile the code:
   ```bash
   mkdir -p target/classes
   javac -cp "lib/*" -d target/classes src/main/java/CLI.java
   ```

## 🚀 How to Use

### With Maven

```bash
mvn exec:java -Dexec.args="github_username"
```

### With Pure Java

```bash
java -cp "target/classes;lib/*" CLI github_username
```

### Example Output

```
Recent activity for octocat:

- Pushed 3 commit(s) to octocat/Hello-World
- Opened issue in octocat/Spoon-Knife
- Starred octocat/git-consortium
- Forked octocat/Hello-World
```

## 🛠️ Development

### Project Structure

```
github-user-activity/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            └── CLI.java
```

### Dependencies

- Jakarta JSON API (for JSON processing)
- Java 11+ HTTP Client (included in Java 11+)

## 🤝 Contributing

Contributions are welcome! Feel free to open issues and submit pull requests.

---

Built with ❤️ by Nataly
