# 🌳 B+ Tree Implementation in Java

A well-structured, efficient, and customizable B+ Tree implementation designed for high-performance key-value storage and retrieval. Ideal for educational use, databases, file systems, and applications requiring fast data access.

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
![Java Version](https://img.shields.io/badge/Java-17%2B-blue)

## Features

- **In-Memory & Disk-Based Modes**  
  Optimized for both temporary memory storage and persistent disk operations.
- **Blazing-Fast Operations**  
  Insert, search, delete, and update keys with O(log n) complexity.
- **Range Queries**  
  Efficiently retrieve all keys within a specified range.
- **Duplicate Key Support**  
  Optional configuration to handle duplicate keys using value lists.
- **Customizable Order**  
  Adjust branching factors to balance memory and performance.
- **Comprehensive Testing**  
  90%+ test coverage with JUnit to ensure reliability.

## Table of Contents
- [Installation](#installation)
- [Quick Start](#quick-start)
- [Usage Examples](#usage-examples)
- [API Documentation](#api-documentation)
- [Contributing](#contributing)
- [License](#license)

---

## Installation

### Prerequisites
- Java JDK 17+
- IntelliJ IDEA (2023.1.1+ recommended) or another Java IDE

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/B-plus-Tree.git
   ```
2. Open the project in IntelliJ:
   - **File > Open** > Select the cloned directory
3. Navigate to `Catalog.java` and run it to see a demo.

---

## Quick Start

```java
// Initialize B+ Tree with order 4
BPlusTree<Integer, String> tree = new BPlusTree<>(4);

// Insert key-value pairs
tree.insert(10, "Data10");
tree.insert(20, "Data20");
tree.insert(5, "Data5");

// Search for a key
System.out.println(tree.search(10));  // Output: [Data10]

// Update a value
tree.update(10, "NewData10");

// Delete a key
tree.delete(5);

// Range query (keys between 10 and 30)
System.out.println(tree.rangeQuery(10, 30));  // Output: [NewData10, Data20]
```

---

## Usage Examples

### Handling Duplicates
```java
BPlusTree<Integer, String> tree = new BPlusTree<>(4, true);

tree.insert(5, "A");
tree.insert(5, "B");
System.out.println(tree.search(5));  // Output: [A, B]
```

### Disk Persistence (Example)
```java
// Save tree to disk
tree.serializeToDisk("tree.dat");

// Load tree from disk
BPlusTree<Integer, String> loadedTree = BPlusTree.deserializeFromDisk("tree.dat");
```

---

## API Documentation

### Core Methods
| Method | Description |
|--------|-------------|
| `insert(K key, V value)` | Inserts a key-value pair. |
| `search(K key)` | Returns all values associated with `key`. |
| `delete(K key)` | Removes all entries for `key`. |
| `update(K key, V newValue)` | Updates all values for `key` to `newValue`. |
| `rangeQuery(K start, K end)` | Returns values for keys in `[start, end]`. |

### Configuration
- `BPlusTree(int order)`  
  Initializes tree with specified branching factor.
- `BPlusTree(int order, boolean allowDuplicates)`  
  Enables duplicate key handling when `allowDuplicates=true`.

---

## Contributing

Contributions are welcome!  
1. Fork the repository.  
2. Create a feature branch: `git checkout -b feature/your-idea`.  
3. Commit changes: `git commit -m 'Add awesome feature'`.  
4. Push to branch: `git push origin feature/your-idea`.  
5. Open a Pull Request.

---

## License

Distributed under the MIT License. See `LICENSE` for details.# B-plus-Tree

Description:

This repository offers a well-structured and efficient implementation of the B+ tree data structure in Java. Designed for clarity and ease of use, it provides essential operations for key-value storage and retrieval, making it suitable for various applications requiring fast data access and organization.


# Key Features:
- Memory-Resident or Disk-Based Usage: Choose between in-memory storage for efficiency or persistent storage on disk for durability. 
- Optimized Insertion and Search: Leverage B+ tree's inherent advantages for efficient key insertion and retrieval, even with large datasets. 
- Range Queries: Perform efficient range queries to retrieve keys within a specific range. 
- Duplicate Key Handling: Optionally support duplicate keys by storing associated values as lists or using custom key-value pair classes. 
- Customizable Order: Adapt the tree's branching factor (order) to balance performance and memory usage as needed. 
- Clear Unit Tests: Ensure code correctness and reliability through comprehensive unit tests.

# Use available methods:
1)insert(key, value)
2)search(key)
3)delete(key)
4)update(key, newValue)


# Getting Started:

1) First install intellj 2023.1.1
2) Then download "B-plus-Tree.zip"
3) Extract the B-plus-Tree.zip"
4) Open the B-plus-Tree file in intellj 2023.1.1
5) Then open Catalog.java file 
6) Run File
