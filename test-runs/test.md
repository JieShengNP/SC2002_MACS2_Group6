# Running Automated Game Tests

## 1. Compile the Java Project

From the project root directory, run:

```bash
javac -d out -sourcepath src src/restaurantrush/Main.java
```

## 2. Run a Test Case

From the project root directory, run:

```bash
java -cp out restaurantrush.Main < test-runs/<input-file>.txt > test-runs/<output-file>.txt
```

Example:

```bash
java -cp out restaurantrush.Main < test-runs/victory-stage3.txt > test-runs/victory-stage3-output.txt
```

This will:

- Read automated inputs from the specified test file.
- Execute the game automatically.
- Save the console output to the specified output file.

## 3. Available Test Cases

| Input File | Description |
|------------|-------------|
| victory.txt | Stage 1 victory scenario |
| defeat.txt | Stage 1 defeat scenario |
| victory-stage2.txt | Stage 2 victory scenario |
| defeat-stage2.txt | Stage 2 defeat scenario |
| victory-stage3.txt | Stage 3 victory scenario |
| defeat-stage3.txt | Stage 3 defeat scenario |

Expected outputs for each test case are provided in the corresponding `*-output.txt` files.

## 4. Deterministic Execution

The application uses a fixed random seed of `2002` for all pseudo-random behaviour.

Using the same input file and random seed will produce the same game behaviour and output, allowing all test cases to be reproduced consistently.

## 5. Notes

- Run all commands from the project root directory.
- Recompile the project after making code changes.
- Compare generated outputs against the provided expected output files to verify correctness.