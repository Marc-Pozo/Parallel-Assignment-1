To compile
1. Go to the directory where Assignment1.java is stored
2. If on Windows, run the command javac Assigment1.java
3. After compilation finishes (there will be a .class file)
   run the command java Assignment1
4. On program completion there will be a file called primes.txt containing all the information

Summary
To summarize my approach, I wanted to spread the workload across the eight threads as evenly as possible. This is why I used 8 lists each with an equivalent number of numbers to check. This way the work could get done in 8x the speed of just using one thread, where each number would need to be checked in order. I believe my approach is correct because it runs quickly relatively to running on a single thread and because it utilizes clean code that clearly designates functions to certain jobs instead of doing all of it inside main. I believe that this approach will not require a supercomputer and will function just fine on what the company can afford because I have taken the most efficient approach.
