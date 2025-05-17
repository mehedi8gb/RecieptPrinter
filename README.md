# TMSAgent

**TMSAgent** is a robust Java-based desktop application designed to act as a bridge between a central Transport Management System (TMS) and local infrastructure. It facilitates tasks such as receiving data from a remote API, managing print queues, monitoring job statuses, and executing automated tasks within local networks.

---

## 🔧 Features

* **Lightweight Java Swing UI**: Provides agent control and status display.
* **Secure API Integration**: Connects seamlessly with TMS.
* **Auto-Start and Background Service Support**: Ensures continuous operation.
* **Print Job Queuing and Processing**: Supports POS/thermal printers.
* **Dynamic Templating**: Generates receipts or job slips.
* **Log Management and Health Check System**: Maintains operational integrity.

---

## 📁 Project Structure

```
TMSAgent/
├── src/
│   └── com/
│       └── TMSAgent/        # Core application logic
├── lib/                     # External dependencies
├── resources/               # Configuration files and templates
├── logs/                    # Runtime logs
├── .idea/                   # IDE configuration files
├── TMSAgent.iml             # IntelliJ IDEA module file
└── README.md                # Project documentation
```

---

## Getting Started

### Prerequisites

* **Java Development Kit (JDK)**: Version 8 or above.
* **Build Tool**: Maven or Ant.

### Build and Run

Using Ant:

```bash
java -jar out/artifacts/TMSAgent_jar/TMSAgent.jar "URL encoded JSON string"
```

Alternatively, open the project in IntelliJ IDEA or your preferred IDE and run the `TMSAgent` class.

---

## 🛠 Configuration

Edit the `config.properties` file located in the `resources/` directory to set:

* **API Base URL**: The endpoint of your TMS.
* **Agent Token**: Authentication token for secure communication.
* **Printer Name**: Specify the target printer.
* **Auto-Start Options**: Configure startup behavior.

---

## 🧾 Printing System

* **ESC/POS and HTML-to-Image Support**: Compatible with most thermal printers.
* **Customizable Templates**: Located in the `resources/templates/` directory.
* **Trigger Mechanisms**: Initiate printing via remote API or local job queues.

---

## 🧠 Developer Notes

* **Design Principles**: Adheres to SOLID principles for maintainability.
* **Template Engine**: Utilizes `StringSubstitutor` for dynamic content injection.
* **Headless Operation**: Capable of running without a GUI for server environments.

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Mehedi** – Entrepreneur & Software Engineer | \[YourCompanyName]

For enterprise integration support, contact: `support@yourcompany.com`

---

### 📘 New Keyword Documentation

* **`StringSubstitutor`**: A utility class from Apache Commons Text that performs variable substitution on strings. It replaces variables in the form `${variableName}` with their corresponding values.
