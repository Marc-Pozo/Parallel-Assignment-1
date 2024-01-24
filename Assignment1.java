// Marcelino Pozo
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class Assignment1
{
    public static void main(String []args)
    {
        // Start the timer        
        long startTime = System.currentTimeMillis();

        // Create a 2D list of Longs, the threads range and chunk size
        List<Callable<List<Long>>> tasks = new ArrayList<>();
        int threads = 8;
        int range = (int) Math.pow(10, 8);
        int chunkSize = range / threads;

        ExecutorService executor = Executors.newFixedThreadPool(threads);

        // Set the start and end for each task and call findPrimes in range
        for (int i = 0; i < threads; i++) 
        {
            final int start = i * chunkSize; // Start from 2
            final int end = (i == threads - 1) ? range : (i + 1) * chunkSize;
            tasks.add(() -> findPrimesInRange(start, end));
        }

        try 
        {
            // Execute all tasks and store them as Futures in results
            List<Future<List<Long>>> results = executor.invokeAll(tasks);
            // Master list of primes
            List<Long> allPrimes = new ArrayList<>();

            // Add all primes found to the master list allPrimes
            for (Future<List<Long>> result : results) 
            {
                allPrimes.addAll(result.get());
            }

            // Stops execution
            executor.shutdown();

            // Calculate results
            long executionTime = System.currentTimeMillis() - startTime;
            long totalPrimes = allPrimes.size();
            long sumPrimes = allPrimes.stream().mapToLong(Long::valueOf).sum();
            List<Long> topTenPrimes = findTopTenPrimes(allPrimes);

            // Print to file usiing a writer
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("primes.txt"))) 
            {
                writer.write(executionTime + " " + totalPrimes + " " + sumPrimes);
                writer.newLine();
                for (Long prime : topTenPrimes) 
                {
                    writer.write(prime + " ");
                }
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

    }

    // Checks each number within a range using the helper function isPrime()
    private static List<Long> findPrimesInRange(int start, int end) 
    {
        List<Long> primes = new ArrayList<>();
        for (int i = start; i <= end; i++) 
        {
            if (isPrime(i)) 
            {
                primes.add((long) i);
            }
        }
        return primes;
    }


    // Checks if a number is prime by checking if it has a square root
    private static boolean isPrime(int num) 
    {
        // Any number less than 2 cant be prime
        if (num < 2) 
        {
            return false;
        }

        // Check whether i is a multiple of sqrt num
        for (int i = 2; i <= Math.sqrt(num); i++) 
        {
            // Mod by i
            if (num % i == 0) 
            {
                return false;
            }
        }
        return true;
    }

    // Looks for the top 10 primes by sorting the list of primes
    private static List<Long> findTopTenPrimes(List<Long> primes) 
    {
        List<Long> topTenPrimes = new ArrayList<>();
                
        int size = primes.size();
        
        // Add each prime to the list and return
        for (int i = Math.max(size - 10, 0); i < size; i++) 
        {
            topTenPrimes.add(primes.get(i));
        }

        return topTenPrimes;
    }
};
