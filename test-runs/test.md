Writing

# Running Automated Game Tests

## 1\. Compile the Java Project

From the project root directory, run:

bash

```bash
javac -d out -sourcepath src src/restaurantrush/Main.java
```

## 2\. Run the Test

bash

```bash
java -cp out restaurantrush.Main < test-runs/victory.txt > test-runs/victory-output.txt
```

This will:

- Read automated inputs from `test-runs/victory.txt`.
- Execute the game automatically.
- Save the console output to `test-runs/victory-output.txt`.

## 3\. View the Results

Open `test-runs/victory-output.txt` to inspect the game output.

**Note:** Run both commands from the project root directory. Recompile after making code changes.