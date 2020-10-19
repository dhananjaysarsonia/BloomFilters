package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class BloomFilter {
    Random random;
    private int nElements, filterSize, nHashes;
    private int[] filter;
    private int[] hashes;


    public BloomFilter(int nElements, int filterSize, int nHashes){
        this.nElements = nElements;
        this.filterSize = filterSize;
        this.nHashes = nHashes;
        initializeFilter();
    }

    private void initializeFilter() {
        random = new Random();
        hashes = random.ints( 1, Integer.MAX_VALUE).distinct().limit(nHashes).toArray();
        filter = new int[filterSize];
    }

    public void insertSimulation(int[] set){
        for(int e : set) {
            for (int i = 0; i < hashes.length; i++) {
                filter[getIndex(e, i)] = 1;
            }
        }
    }

    public boolean checkInFilter(int e){
        for(int i = 0; i < hashes.length; i++){
            int index = getIndex(e, i);
            if(filter[index] == 0){
                return false;
            }
        }
        return true;
    }
    public void lookUpSimulation(int[] set){
        int count = 0;
        for(int i : set){
            if(checkInFilter(i)){
                count++;
            }
        }

        System.out.println("Number of elements found in filter: " + count + "\n");
    }

    public void simulate(){
        int[] A = generateSet();
        System.out.println("Simulation for set A");

        System.out.print("Functions: Insertion and Lookup");
        insertSimulation(A);
        lookUpSimulation(A);

        int[] B = generateSet();
        System.out.println("Simulation for set B");
        System.out.println("Functions: Lookup");
        lookUpSimulation(B);



    }





    private int getIndex(int element, int index){
        return (hashes[index] ^ element)%filterSize;
    }


    public int[] generateSet(){
        return random.ints( 1, Integer.MAX_VALUE).distinct().limit(nElements).toArray();
    }


    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter number of elements to be encoded");
            String element = reader.readLine();

            System.out.println("Please enter number of bits in the filter");
            String filterSize = reader.readLine();

            System.out.println("Please enter number of hashes");
            String nHashes = reader.readLine();

            BloomFilter filter = new BloomFilter(Integer.parseInt(element),Integer.parseInt(filterSize),
                                    Integer.parseInt(nHashes));

            filter.simulate();
        } catch (IOException e) {
            System.out.println("Invalid inputs");
            e.printStackTrace();
        }


    }
}
