# Student Record Manager 📘

A simple Java Swing application for managing student records with a MySQL database backend.

## 🚀 Features

- Add new student records via a form
- View all student records in a tabular format
- Select and delete multiple student entries
- Dynamic dropdowns: Cities update based on selected state
- Clean, responsive interface using Java Swing

## 🧰 Tech Stack

- **Java (Swing GUI)**
- **MySQL**
- **JDBC (MySQL Connector/J)**
- **NetBeans IDE**

## 🗂️ Project Structure

StudentRecordManager/
│
├── src/ # Java source files
├── lib/ # MySQL Connector JAR
├── database/ # SQL dump for the database (see below)
└── README.md # This file



## 🗃️ Database Setup

1. Import the provided SQL file `student_db.sql` into your MySQL server.
2. Default connection settings in the code:
   - **Username**: `root`
   - **Password**: `0000`
   - **Database name**: `student_db`
3. Modify these in the connection code if your configuration is different.

## 🖥️ How to Run

1. Open the project in **NetBeans**.
2. Ensure the **MySQL Connector JAR** is added to your project libraries.
3. Update your MySQL credentials in the connection section.
4. Run the application.



## 📝 License

This project is open-source and free to use under the MIT License.

---

### ✨ Author

[Raghav Singhania](https://github.com/RaghavSinghania77) – Passionate about building clean and functional Java desktop apps.
