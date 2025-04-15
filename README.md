# Election Algorithm

A Java-based simulation of two common voting systems: Open Party Listing (OPL) and Instant Runoff Voting (IRV). This program reads election data from CSV files and determines the winners based on the selected voting system.

## Features

- Supports two voting systems:
  - Open Party Listing (OPL)
  - Instant Runoff Voting (IRV)
- Handles parsing and processing of:
  - Candidates
  - Ballots
  - Political parties
- Console-based user interface

## How to Compile and Run

### 1. Compile

```bash
javac ElectionMachine/Main.java
```

### 2. Run

```bash
java ElectionMachine/Main
```

### 3. Input

When prompted, enter the exact name of the CSV file (including extension) containing election data.

## Project Structure

```
ElectionMachine/
├── Main.java                 # Entry point
├── Candidate.java
├── Ballot.java
├── Party.java
├── ...                      # Other supporting classes
```

## Example CSV Files

Ensure your CSV files follow the expected format for either OPL or IRV input. Example files can be added to a `data/` directory for testing.
