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
        long startTime = System.currentTimeMillis();

        // Find primes using 8 threads
        List<Callable<List<Long>>> tasks = new ArrayList<>();
        int threads = 8;
        int range = (int) Math.pow(10, 8);
        int chunkSize = range / threads;

        ExecutorService executor = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < threads; i++) 
        {

            final int start = i * chunkSize + 2; // Start from 2
            final int end = (i == threads - 1) ? range : (i + 1) * chunkSize;
            tasks.add(() -> findPrimesInRange(start, end));
        }

        try 
        {

            List<Future<List<Long>>> results = executor.invokeAll(tasks);
            List<Long> allPrimes = new ArrayList<>();

            for (Future<List<Long>> result : results) 
            {
                allPrimes.addAll(result.get());
            }

            executor.shutdown();

            // Calculate and print results
            long executionTime = System.currentTimeMillis() - startTime;
            long totalPrimes = allPrimes.size();
            long sumPrimes = allPrimes.stream().mapToLong(Long::valueOf).sum();
            List<Long> topTenPrimes = findTopTenPrimes(allPrimes);

            // Print to file
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
        if (num < 2) 
        {
            return false;
        }


        for (int i = 2; i <= Math.sqrt(num); i++) 
        {
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
        
        primes.sort(null);
        
        int size = primes.size();
        
        for (int i = Math.max(size - 10, 0); i < size; i++) 
        {
            topTenPrimes.add(primes.get(i));
        }

        return topTenPrimes;
    }
};
